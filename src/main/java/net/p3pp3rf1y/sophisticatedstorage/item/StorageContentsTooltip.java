package net.p3pp3rf1y.sophisticatedstorage.item;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record StorageContentsTooltip(ItemStack shulkerItem) implements TooltipComponent {
	public ItemStack getStorageItem() {
		return shulkerItem;
	}
}
