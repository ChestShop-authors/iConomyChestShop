package com.Acrobot.iConomyChestShop;

import info.somethingodd.bukkit.OddItem.OddItem;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
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
        Material mat = getMat(name);
        if (OI != null) {
            try {
                return OI.getItemStack(name);
            } catch (IllegalArgumentException e) {
                if (mat != null) {
                    return new ItemStack(mat);
                }
            }
        }
        if (mat == null) {
            return null;
        }
        return new ItemStack(mat);
    }

    //Returns Alias of the Item - in case OddItem is enabled
    public static String returnAlias(String name) {
        if (OI == null) {
            return null;
        }
        Set<String> aliases = Basic.OI.getAliases(name);
        Iterator iter = aliases.iterator();
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

    public static String stripName(String name) {
        int length = name.length();
        if (length > 15) {
            length = 15;
        }
        return name.substring(0, length);
    }

    public static int getItemAmountFromInventory(Inventory inv, ItemStack is) {
        int count = 0;
        ItemStack Items[] = inv.getContents();
        for (int i = 0; i < Items.length; i++) {
            if (Items[i] == null) {
                continue;
            }
            if (Items[i].getType() == is.getType() && (Items[i].getDurability() == is.getDurability() || Items[i].getDurability() == -1)) {
                count += Items[i].getAmount();
            }
        }
        return count;
    }

    public static void removeItemStackFromInventory(Inventory inv, ItemStack is) {
        ItemStack[] Items = inv.getContents();
        int left = is.getAmount();
        for (int i = 0; i < Items.length; i++) {
            if (left <= 0) {
                return;
            }
            ItemStack curItem = Items[i];
            if (curItem == null) {
                continue;
            }
            if (curItem.getType() == is.getType() && (curItem.getDurability() == is.getDurability() || curItem.getDurability() == -1)) {
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
}
