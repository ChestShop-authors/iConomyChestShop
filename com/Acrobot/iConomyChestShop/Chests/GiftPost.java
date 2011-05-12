package com.Acrobot.iConomyChestShop.Chests;

import com.Acrobot.iConomyChestShop.Basic;
import com.Acrobot.iConomyChestShop.ConfigManager;
import com.aranai.virtualchest.VirtualChest;
import org.bukkit.inventory.ItemStack;

/**
 * Compatibility with VirtualChests - future relases
 * @author Acrobot
 */
public class GiftPost implements ChestObject{
    private final VirtualChest chest;

    public GiftPost(VirtualChest v){
        this.chest = v;
    }

    public ItemStack[] getContents() {
        return chest.getContents();
    }

    public void setSlot(int slot, ItemStack item) {
        chest.setItem(slot, item);
    }

    public void clearSlot(int slot) {
        chest.setItem(slot, null);
    }

    public void addItem(ItemStack item, int amount) {
        item.setAmount(amount);
        chest.addItem(item);
    }

    public void removeItem(ItemStack item, int amount) {
        int type = item.getTypeId();
        boolean checkDurability = true;
        if(ConfigManager.getBoolean("allowUsedItemsToBeSold") && type >= 256 && type <= 317){
            checkDurability = false;
        }
        for (int i = 0; i < chest.usedCases(); i++) {
            ItemStack is = chest.getItem(i);
            if (is != null) {
                short durability = item.getDurability();
                int isAmount = is.getAmount();
                if (is.getTypeId() == type && (!checkDurability || durability == -1 || (is.getDurability() == durability))) {
                    if (isAmount - amount > 0) {
                        chest.setItem(i, new ItemStack(type, isAmount - amount, durability));
                        return;
                    } else if (isAmount - amount == 0) {
                        return;
                    } else {
                        amount -= isAmount;
                        chest.setItem(i, null);
                    }
                }
            }
        }
    }

    public int amount(ItemStack item) {
        return Basic.getItemAmount(chest.getContents(), item);
    }

    public boolean hasEnough(ItemStack item, int amount) {
        return amount(item) >= amount;
    }

    public boolean fits(ItemStack item, int amount) {
        return true;
    }

    public int getSize() {
        return chest.usedCases();
    }
}
