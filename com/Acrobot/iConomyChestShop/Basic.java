package com.Acrobot.iConomyChestShop;

import info.somethingodd.bukkit.OddItem.OddItem;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Acrobot
 */
public class Basic {

    public static OddItem OI = null;
    
    //Checks if string is an integer
    public static boolean isInt(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //Checks if config exists
    public static boolean cfgExists() {
        return new File("plugins/iConomyChestShop/config.yml").exists();
    }

    //Checks if config is properly formated
    public static boolean goodCfg() {
        try {
            BlockFace.valueOf(ConfigManager.getString("position"));
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    //Gets the Material from bukkit enum
    public static Material getMat(String name) {
        if (Basic.isInt(name)) {
            return getMat(Integer.parseInt(name));
        } else {
            return Material.getMaterial(returnID(name));
        }
    }

    //Gets the Material from ID
    public static Material getMat(int id) {
        return Material.getMaterial(id);
    }

    //Returns the id of a material
    public static int returnID(String name) {
        Material[] mat = Material.values();
        int temp = 9999;
        Material tmp = null;
        for (Material m : mat) {
            if (m.name().toLowerCase().replaceAll("_", "").startsWith(name.toLowerCase().replaceAll("_", "").replaceAll(" ", ""))) {
                if (m.name().length() < temp) {
                    tmp = m;
                    temp = m.name().length();
                }
            }
        }
        if (tmp != null) {
            return tmp.getId();
        }
        return -1;
    }

    //Returns ItemStack - in case OddItem is enabled
    public static ItemStack getItemStack(String name) {
        int dataPosition = name.indexOf(';');
        dataPosition = (dataPosition != -1 ? dataPosition : -1);
        int dataValue = (isInt(name.substring(dataPosition + 1)) ? Integer.parseInt(name.substring(dataPosition + 1)) : 0);
        dataValue = (dataValue > 30 || dataValue < 0 ? 0 : dataValue);
        Material mat;
        if(dataPosition != -1){
            mat = getMat(name.substring(0,dataPosition));
        }else{
            mat = getMat(name);
        }
        if (OI != null) {
            try {
                return OI.getItemStack(name);
            } catch (Exception e) {
            }
        }
        if (mat != null && mat != Material.AIR) {
            return new ItemStack(mat, 0, (short) dataValue);
        }else{
            return null;
        }
    }

    //Returns Alias of the Item - in case OddItem is enabled
    public static String returnAlias(String name) {
        if (OI == null) {
            return null;
        }
        Set<String> aliases = Basic.OI.getAliases(name);
        Iterator<String> iter = aliases.iterator();
        String alias = null;
        if (iter.hasNext()) {
            alias = (String) iter.next();
        }
        return alias;
    }

    //Changes the & to color code sign
    public static String colorChat(String msg) {
        String langChar = new Character((char) 167).toString();
        msg = msg.replaceAll("&", langChar);
        return msg;
    }

    //Checks if config is correct
    public static boolean checkConfig(Player p) {
        if (!Basic.cfgExists()) {
            p.sendMessage("[iConomyChestShop] Config file doesn't exist!");
            p.sendMessage("[iConomyChestShop] Be sure to copy everything from .zip file to /plugins");
            return false;
        } else if (!Basic.goodCfg()) {
            p.sendMessage("[iConomyChestShop] Incorrect config file!");
            return false;
        } else {
            return true;
        }
    }

    //Strips name to 15 chars - that's how many sign can hold
    public static String stripName(String name) {
        int length = name.length();
        if (length > 15) {
            length = 15;
        }
        return name.substring(0, length);
    }

    //What do I even have to say?
    public static int getItemAmountFromInventory(Inventory inv, ItemStack is) {
        int id = is.getTypeId();
        boolean checkDurability = true;
        if(ConfigManager.getBoolean("allowUsedItemsToBeSold") && id >= 256 && id <= 317){
            checkDurability = false;
        }
        int count = 0;
        ItemStack Items[] = inv.getContents();
        for (int i = 0; i < Items.length; i++) {
            if (Items[i] == null) {
                continue;
            }
            if (Items[i].getType() == is.getType() && ((Items[i].getDurability() == is.getDurability() || Items[i].getDurability() == -1) || !checkDurability) && Items[i].getAmount() > 0) {
                count += Items[i].getAmount();
            }
        }
        return count;
    }

    //Again...
    public static void removeItemStackFromInventory(Inventory inv, ItemStack is, int left) {
        ItemStack[] Items = inv.getContents();
        boolean checkDurability = true;
        int id = is.getTypeId();
        if(ConfigManager.getBoolean("allowUsedItemsToBeSold") && id >= 256 && id <= 317){
            checkDurability = false;
        }
        for (int i = 0; i < Items.length; i++) {
            if (left <= 0) {
                return;
            }
            ItemStack curItem = Items[i];
            if (curItem == null) {
                continue;
            }
            if (curItem.getType() == is.getType() && ((curItem.getDurability() == is.getDurability() || curItem.getDurability() == -1) || !checkDurability)) {
                if (curItem.getAmount() > left) {
                    curItem.setAmount(curItem.getAmount() - left);
                    left -= curItem.getAmount();
                } else {
                    left -= curItem.getAmount();
                    curItem = null;
                }
            }
            if (curItem != null) {
                if (curItem.getType() != Material.AIR) {
                    inv.setItem(i, curItem);
                }
            } else {
                inv.setItem(i, null);
            }

        }
    }
    
    //Adds item to inventory
    public static void addItemToInventory(Inventory inv, ItemStack is, int left){
        int maxStackSize = is.getType().getMaxStackSize();
        if(left <= maxStackSize){
            inv.addItem(is);
            return;
        }
        if(maxStackSize != 64){
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            for (int i = 0; i < Math.ceil(left / maxStackSize); i++) {
                if (left < maxStackSize) {
                    ItemStack tmp = is;
                    tmp.setAmount(left);
                    items.add(tmp);
                    return;
                }else{
                    ItemStack tmp = is;
                    tmp.setAmount(maxStackSize);
                    items.add(tmp);
                }
            }
            Object[] iArray = items.toArray();
            for(Object o : iArray){
                inv.addItem((ItemStack) o);
            }
        }else{
            inv.addItem(is);
        }
    }
    
    //Checks if there is enough free space in inventory
    public static boolean checkFreeSpace(Inventory inv, ItemStack is, int left){
        ItemStack[] contents = inv.getContents();
        Material type = is.getType();
        short durability = is.getDurability();
        int maxStack = is.getType().getMaxStackSize();
        for(int i=0; i < inv.getSize(); i++){
            if(left <= 0){
                return true;
            }
            ItemStack curitem = contents[i];
            if(curitem == null){
                left = left - maxStack;
                continue;
            }
            if(!curitem.getType().equals(type) || curitem.getDurability() != durability){
                continue;
            }
            int amount = curitem.getAmount();
            int maxStackSize = curitem.getMaxStackSize();
            if(amount < maxStackSize){
                left = left - (maxStackSize - amount);
            }
        }
        if(left <= 0){
            return true;
        }else{
            return false;
        }
    }
    
    //Sends messages to players - future use
    /*
    public static void sendMessage(Player p, String message){
        p.sendMessage(message);
    }*/
    
    public static void cancelEventAndDropSign(SignChangeEvent event){
        event.setCancelled(true);
        Block block = event.getBlock();
        block.setType(Material.AIR);
        block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.SIGN));
    }
}
