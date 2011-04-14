package com.Acrobot.iConomyChestShop;

import com.Acrobot.iConomyChestShop.MinecartMania.MinecartManiaChest;
import com.Acrobot.iConomyChestShop.MinecartMania.MinecartManiaDoubleChest;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * iConomyChestShop Chest Manager
 * @author Acrobot
 */
public class ChestManager{
    public static boolean isDoubleChest(MinecartManiaChest chest){
        return (chest.getNeighborChest()) != null;
    }
    
    public static MinecartManiaDoubleChest returnDoubleChest(MinecartManiaChest chest){
        MinecartManiaDoubleChest doublechest = new MinecartManiaDoubleChest(chest, chest.getNeighborChest());
        return doublechest;
    }
    
    public static MinecartManiaChest returnChest(MinecartManiaChest chest){
        return chest.getNeighborChest();
    }
    
    public static ItemStack[] getContents(MinecartManiaChest chest) {
        if (isDoubleChest(chest)) {
            return returnDoubleChest(chest).getContents();
        } else {
            return chest.getContents();
        }
    }
    
    public static boolean hasFreeSpace(MinecartManiaChest chest, ItemStack is){
        if(isDoubleChest(chest)){
            Inventory chest1 = chest.getInventory();
            Inventory chest2 = returnChest(chest).getInventory();

            if (Basic.checkFreeSpace(chest1, is) || Basic.checkFreeSpace(chest2, is)) {
                return true;
            } else {
                return false;
            }
        }else{
            if(Basic.checkFreeSpace(chest.getInventory(), is)){
                return true;
            }else{
                return false;
            }
        }
    }

    public static int firstEmpty(MinecartManiaChest chest) {
        if (isDoubleChest(chest)) {
            return returnDoubleChest(chest).firstEmpty();
        } else {
            return chest.firstEmpty();
        }
    }

    public static boolean addItem(MinecartManiaChest chest, ItemStack item) {
        if (isDoubleChest(chest)) {
            return returnDoubleChest(chest).addItem(item);
        } else {
            return chest.addItem(item);
        }
    }
    
    public static int getItemAmount(MinecartManiaChest chest, ItemStack is) {
        if (isDoubleChest(chest)) {
            int count = 0;
            MinecartManiaDoubleChest doublechest = returnDoubleChest(chest);
            ItemStack Items[] = doublechest.getContents();
            for (int i = 0; i < Items.length; i++) {
                if(Items[i] == null){
                    continue;
                }
                if(Items[i].getType() == is.getType() && (Items[i].getDurability() == is.getDurability() || Items[i].getDurability() == -1)){
                    count += Items[i].getAmount();
                }
            }
            return count;
        } else {
            int count = 0;
            ItemStack Items[] = chest.getContents();
            for (int i = 0; i < Items.length; i++) {
                if(Items[i] == null){
                    continue;
                }
                if((Items[i].getType() == is.getType() && Items[i].getDurability() == is.getDurability())){
                    count += Items[i].getAmount();
                }
            }
            return count;
        }
    }

    public static int getSize(MinecartManiaChest chest) {
        if (isDoubleChest(chest)) {
            return chest.getInventory().getSize() * 2;
        } else {
            return chest.getInventory().getSize();
        }
    }

    public static ItemStack getItem(MinecartManiaChest chest, int slot) {
        if (isDoubleChest(chest)) {
            MinecartManiaDoubleChest doublechest = returnDoubleChest(chest);
            return doublechest.getItem(slot);
        } else {
            return chest.getItem(slot);
        }
    }
    
    public static void setItem(MinecartManiaChest chest, int slot, int amount) {
        if (isDoubleChest(chest)) {
            MinecartManiaDoubleChest doublechest = returnDoubleChest(chest);
            doublechest.getItem(slot).setAmount(amount);
        } else {
            chest.getItem(slot).setAmount(amount);
        }
    }

    public static int first(MinecartManiaChest chest, ItemStack item) {
        if (isDoubleChest(chest)) {
            MinecartManiaDoubleChest doublechest = returnDoubleChest(chest);
            return doublechest.first(item.getTypeId(), item.getDurability());
        } else {
            return chest.first(item.getTypeId(), item.getDurability());
        }
    }

    public static void clear(MinecartManiaChest chest, int slot) {
        if (isDoubleChest(chest)) {
            MinecartManiaDoubleChest doublechest = returnDoubleChest(chest);
            doublechest.setItem(slot, null);
        } else {
            chest.setItem(slot, null);
        }
    }
    
    public static void removeItems(MinecartManiaChest chest, ItemStack item, int left){
        if (isDoubleChest(chest)) {
            MinecartManiaDoubleChest doublechest = returnDoubleChest(chest);
            ItemStack[] Items = doublechest.getContents();
            //int left = item.getAmount();
            for(int i = 0; i < doublechest.size(); i++){
                if(left <= 0){
                    return;
                }
                ItemStack curItem = Items[i];
                if(curItem == null){
                    continue;
                }
                if(curItem.getType() == item.getType() && (curItem.getDurability() == item.getDurability() || curItem.getDurability() == -1)){
                    if(curItem.getAmount() > left){
                        curItem.setAmount(curItem.getAmount() - left);
                        left -= curItem.getAmount();
                    }else{
                        left -= curItem.getAmount();
                        curItem = null;
                    }
                }
                if (curItem != null) {
                    if (curItem.getType() != Material.AIR) {
                        doublechest.setItem(i, curItem);
                    }
                }else{
                    doublechest.setItem(i, null);
                }
            }
        } else {
            ItemStack[] Items = chest.getContents();
            //int left = item.getAmount();
            for(int i = 0; i < chest.size(); i++){
                if(left <= 0){
                    return;
                }
                ItemStack curItem = Items[i];
                if(curItem == null){
                    continue;
                }
                if(curItem.getType() == item.getType() && (curItem.getDurability() == item.getDurability() || curItem.getDurability() == -1)){
                    if(curItem.getAmount() > left){
                        curItem.setAmount(curItem.getAmount() - left);
                        left -= curItem.getAmount();
                    }else{
                        left -= curItem.getAmount();
                        curItem = null;
                    }
                }
                if (curItem != null) {
                    if (curItem.getType() != Material.AIR) {
                        chest.setItem(i, curItem);
                    }
                }else{
                    chest.setItem(i, null);
                }
                
            }
        }
    }
}
