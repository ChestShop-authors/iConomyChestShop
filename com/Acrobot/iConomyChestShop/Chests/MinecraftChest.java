package com.Acrobot.iConomyChestShop.Chests;

import com.Acrobot.iConomyChestShop.Basic;
import com.Acrobot.iConomyChestShop.MinecartMania.MinecartManiaChest;
import com.Acrobot.iConomyChestShop.MinecartMania.MinecartManiaDoubleChest;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Acrobot
 */
public class MinecraftChest implements ChestObject{
    public final MinecartManiaChest main;
    public final MinecartManiaChest extended;
    
    public MinecraftChest(Chest main){
        this.main = new MinecartManiaChest(main);
        this.extended = this.main.getNeighborChest();
    }

    public boolean exists(){
        return main != null;
    }
    
    private MinecartManiaDoubleChest doubleChest(){
        return new MinecartManiaDoubleChest(main, extended);
    }
    
    public ItemStack[] getContents(){
        if(extended != null){
            return doubleChest().getContents();
        }
        return main.getContents();
    }
    
    public void setSlot(int slot, ItemStack item){
        if(extended != null){
            doubleChest().setItem(slot, item);
            return;
        }
        main.setItem(slot, item);
    }
    
    public void clearSlot(int slot){
        if(extended != null){
            doubleChest().setItem(slot, null);
            return;
        }
        main.setItem(slot, null);
    }
    
    public void addItem(ItemStack item, int amount){
        if(extended != null){
            doubleChest().addItem(new ItemStack(item.getType(), amount, item.getDurability()));
            return;
        }
        main.addItem(new ItemStack(item.getType(), amount, item.getDurability()));
    }
    
    public void removeItem(ItemStack item, int amount){
        if(extended != null){
            doubleChest().removeItem(item.getTypeId(), amount, item.getDurability());
            return;
        }
        main.removeItem(item.getTypeId(), amount, item.getDurability());
    }
    
    public int amount(ItemStack item){
        if(extended != null){
            return doubleChest().amount(item);
        }
        return main.amount(item);
    }
    
    public boolean hasEnough(ItemStack item, int amount){
        return amount(item) >= amount;
    }
    
    public boolean fits(ItemStack item, int amount){
        if(extended != null){
            return Basic.checkFreeSpace(main, item, amount) || Basic.checkFreeSpace(extended, item, amount);
        }
        return Basic.checkFreeSpace(main, item, amount);
    }
    
    public int getSize(){
        return (extended != null ? 2*main.size() : main.size());
    }
}
