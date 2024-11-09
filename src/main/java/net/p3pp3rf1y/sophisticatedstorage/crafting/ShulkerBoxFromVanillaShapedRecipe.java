package net.p3pp3rf1y.sophisticatedstorage.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.crafting.IWrapperRecipe;
import net.p3pp3rf1y.sophisticatedcore.crafting.RecipeWrapperSerializer;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import net.p3pp3rf1y.sophisticatedstorage.init.ModBlocks;
import net.p3pp3rf1y.sophisticatedstorage.item.StackStorageWrapper;
import net.p3pp3rf1y.sophisticatedstorage.item.WoodStorageBlockItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ShulkerBoxFromVanillaShapedRecipe extends ShapedRecipe implements IWrapperRecipe<ShapedRecipe> {
	private final ShapedRecipe compose;

	public ShulkerBoxFromVanillaShapedRecipe(ShapedRecipe compose) {
		super(compose.getGroup(), compose.category(), compose.pattern, compose.result);
		this.compose = compose;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		return super.matches(input, level) && getVanillaShulkerBox(input).map(storage -> !(storage.getItem() instanceof WoodStorageBlockItem) || !WoodStorageBlockItem.isPacked(storage)).orElse(false);
	}

	@Override
	public ShapedRecipe getCompose() {
		return compose;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack upgradedStorage = super.assemble(input, registries);
		getVanillaShulkerBox(input).ifPresent(vanillaShulkerBox -> {
			@Nullable IItemHandler itemCap = vanillaShulkerBox.getCapability(Capabilities.ItemHandler.ITEM);
			if (itemCap != null) {
				StackStorageWrapper wrapper = StackStorageWrapper.fromStack(registries, upgradedStorage);
				InventoryHelper.iterate(itemCap, (slot, stack) -> {
					if (!stack.isEmpty()) {
						wrapper.getInventoryHandler().insertItem(stack, false);
					}
				});
			}
		});
		return upgradedStorage;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	private Optional<ItemStack> getVanillaShulkerBox(CraftingInput input) {
		for (int slot = 0; slot < input.size(); slot++) {
			ItemStack slotStack = input.getItem(slot);
			if (slotStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof net.minecraft.world.level.block.ShulkerBoxBlock) {
				return Optional.of(slotStack);
			}
		}

		return Optional.empty();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModBlocks.SHULKER_BOX_FROM_VANILLA_SHAPED_RECIPE_SERIALIZER.get();
	}

	public static class Serializer extends RecipeWrapperSerializer<ShapedRecipe, ShulkerBoxFromVanillaShapedRecipe> {
		public Serializer() {
			super(ShulkerBoxFromVanillaShapedRecipe::new, RecipeSerializer.SHAPED_RECIPE);
		}
	}
}
