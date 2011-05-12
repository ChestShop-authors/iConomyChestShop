package com.Acrobot.iConomyChestShop.Chests;

import org.bukkit.inventory.ItemStack;

public interface ChestObject {
    public ItemStack[] getContents();

    public void setSlot(int slot, ItemStack item);
    public void clearSlot(int slot);

    public void addItem(ItemStack item, int amount);
    public void removeItem(ItemStack item, int amount);

    public int amount(ItemStack item);
    public boolean hasEnough(ItemStack item, int amount);
    public boolean fits(ItemStack item, int amount);

    public int getSize();
}
