package com.Acrobot.iConomyChestShop;

import com.aranai.virtualchest.VirtualChest;
import org.bukkit.inventory.ItemStack;

/**
 * Compatibility with VirtualChests (IN FUTURE RELASES)
 * @author Acrobot
 */
public class VirtualChestObject {
    private final VirtualChest chest;
    
    VirtualChestObject(VirtualChest v){
        this.chest = v;
    }
    
    public ItemStack[] getContents(){
        return chest.getContents();
    }
    
    public void setSlot(int slot, ItemStack item){
        chest.setItem(slot, item);
    }
    
    public void clearSlot(int slot){
        chest.setItem(slot, null);
    }
    
    public void addItem(ItemStack item, int amount){
        chest.addItem(new ItemStack(item.getType(), amount, item.getDurability()));
    }
    
    public void removeItem(ItemStack item, int amount){
        int type = item.getTypeId();
        boolean checkDurability = true;
        if(ConfigManager.getBoolean("allowUsedItemsToBeSold") && type >= 256 && type <= 317){
            checkDurability = false;
        }
        for (int i = 0; i < chest.usedCases(); i++) {
            ItemStack is = chest.getItem(i);
            if (is != null) {
                short durability = is.getDurability();
                if (is.getTypeId() == type && (!checkDurability || durability == -1 || (is.getDurability() == durability))) {
                    if (is.getAmount() - amount > 0) {
                        chest.setItem(i, new ItemStack(type, is.getAmount() - amount, durability));
                        return;
                    } else if (is.getAmount() - amount == 0) {
                        return;
                    } else {
                        amount -= is.getAmount();
                        chest.setItem(i, null);
                    }
                }
            }
        }
    }
    
    public int amount(ItemStack item) {
        int type = item.getTypeId();
        int amount = 0;
        boolean checkDurability = true;
        if (ConfigManager.getBoolean("allowUsedItemsToBeSold") && type >= 256 && type <= 317) {
            checkDurability = false;
        }
        
        for (int i = 0; i < chest.usedCases(); i++) {
            ItemStack is = chest.getItem(i);
            if (is != null) {
                short durability = is.getDurability();
                if (is.getTypeId() == type && (!checkDurability || durability == -1 || (is.getDurability() == durability))) {
                    amount += is.getAmount();
                }
            }
        }
        return amount;
    }
    
    public boolean hasEnough(ItemStack item, int amount){
        return amount(item) >= amount;
    }
    
}
