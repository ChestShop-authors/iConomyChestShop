package com.Acrobot.iConomyChestShop;

import com.Acrobot.iConomyChestShop.MinecartMania.MinecartManiaChest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Manages signs
 * @author Acrobot
 */
public class SignManager extends BlockListener{
    
    @Override
    public void onSignChange(SignChangeEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        Location blockLoc = block.getLocation();
        String location = blockLoc.getWorld().getName() + ", x = " + blockLoc.getBlockX() + ", y = " + blockLoc.getBlockY() + ", z = " + blockLoc.getBlockZ();
        Material type = block.getType();
        String playerName = p.getName();
        boolean isAdmin = PermissionManager.hasPermissions(p, "iConomyChestShop.admin");
        
        if (!(type.equals(Material.SIGN) || type.equals(Material.SIGN_POST) || type.equals(Material.WALL_SIGN))) {
            return;
        }
        if (!Basic.checkConfig(p)) {
            p.sendMessage("[iConomyChestShop] Aborting!");
            return;
        }
        
        String text[] = event.getLines();
        Block ChestB = event.getBlock().getFace(BlockFace.valueOf(ConfigManager.getString("position").toUpperCase()), ConfigManager.getInt("distance"));
        ContainerBlock chest = null;
        
        if (ChestB.getTypeId() == 54) {
            chest = (ContainerBlock) ChestB.getState();
        }
        if (Basic.isInt(text[1])) {
            String split[] = text[2].split(":");
            boolean isFormated = mySign(text);
            boolean isGoodSign;
            try {
                isGoodSign = (split.length == 2 ? (Basic.isFloat(split[0]) && Basic.isFloat(split[1])) : Basic.isFloat(text[2])) || isFormated;
            } catch (Exception ex) {
                return;
            }
            if (isGoodSign) {
                if (!(!text[0].equals("") && isAdmin)) {
                    event.setLine(0, playerName);
                }
                if (chest != null) {
                    if (ProtectionManager.isProtected(ChestB) && !isAdmin) {
                        if (ProtectionManager.protectedByWho(ChestB) != null) {
                            if (!ProtectionManager.protectedByWho(ChestB).equals(Basic.stripName(playerName))) {
                                p.sendMessage(ConfigManager.getLanguage("You_tried_to_steal"));
                                Basic.cancelEventAndDropSign(event);
                                return;
                            }
                        }
                    }
                    MinecartManiaChest mmc = new MinecartManiaChest((Chest) chest);
                    MinecartManiaChest neighbor = mmc.getNeighborChest();
                    if(neighbor != null)
                    {
                        CraftSign sig = ProtectionManager.getSign(neighbor.getChest().getBlock(), true);
                        if(sig != null){
                            if (!sig.getLine(0).equals(playerName) && !isAdmin) {
                                p.sendMessage(ConfigManager.getLanguage("You_tried_to_steal"));
                                Basic.cancelEventAndDropSign(event);
                                return;
                            }
                        }
                    }
                } else if(!text[0].toLowerCase().replace(" ", "").equals("adminshop")){
                    Basic.cancelEventAndDropSign(event);
                    p.sendMessage(ConfigManager.getLanguage("Shop_cannot_be_created"));
                    return;
                }
                if (priceIsNegative(text[2])) {
                    Basic.cancelEventAndDropSign(event);
                    p.sendMessage(ConfigManager.getLanguage("Negative_price"));
                    return;
                }
                if (Integer.parseInt(text[1]) < 1) {
                    Basic.cancelEventAndDropSign(event);
                    p.sendMessage(ConfigManager.getLanguage("Incorrect_item_amount"));
                    return;
                }
                ItemStack itemStack = Basic.getItemStack(text[3].replace(":", ";"));
                if (itemStack == null) {
                    Basic.cancelEventAndDropSign(event);
                    p.sendMessage(ConfigManager.getLanguage("incorrectID"));
                    return;
                }
                if (!isAdmin && (PermissionManager.hasPermissions(p,"iConomyChestShop.shop.exclude."+itemStack.getTypeId()) || (!PermissionManager.hasPermissions(p, "iConomyChestShop.shop.create") && !PermissionManager.hasPermissions(p, "iConomyChestShop.shop.create." + itemStack.getTypeId())))) {
                    p.sendMessage("[Permissions] You can't make this type of shop!");
                    Basic.cancelEventAndDropSign(event);
                    return;
                }
                if (Basic.isInt(text[3])) {
                    Material signMat = itemStack.getType();
                    if(signMat == null){
                        Basic.cancelEventAndDropSign(event);
                        p.sendMessage(ConfigManager.getLanguage("incorrectID"));
                        return;
                    }
                    String alias = Basic.returnAlias(signMat.name());
                    if (alias != null) {
                        event.setLine(3, alias);
                    } else if (signMat.name() != null) {
                        event.setLine(3, signMat.name());
                    } else {
                        Basic.cancelEventAndDropSign(event);
                        p.sendMessage(ConfigManager.getLanguage("incorrectID"));
                        return;
                    }
                }else if(text[3].contains(";") || text[3].contains(":")){
                    String alias = Basic.returnAlias(text[3].replace(":", ";"));
                    if (alias != null) {
                        event.setLine(3, alias);
                    }
                    
                }
                if ((text[2].length() > 11 && !isFormated) || text[1].length() > 15) {
                    Basic.cancelEventAndDropSign(event);
                    p.sendMessage(ConfigManager.getLanguage("Couldnt_fit_on_sign"));
                    return;
                }
                if (!isFormated) {
                    event.setLine(2, "B " + (split.length == 2 ? split[0] + ":" + split[1] + " S" : text[2]));
                }
                if (ConfigManager.getBoolean("signLWCprotection")) {
                    ProtectionManager.protectBlock(block, text[0]);
                }
                if (ChestB != null) {
                    if(ProtectionManager.protectBlock(ChestB, text[0]) == true){
                        p.sendMessage(ConfigManager.getLanguage("Shop_was_LWC_protected"));
                    }
                }
                p.sendMessage(ConfigManager.getLanguage("Shop_is_created"));
                text = event.getLines();
                Logging.log(playerName + " created shop in " + location + ", which first line is " + text[0] + " and it buys/sells " + text[1] + " " + text[3] + ", and the buy/sell line looks like: \"" + text[2] + "\"");
            }
        }
    }
    
    
    public static boolean mySign(Sign sign) {
        return mySign(sign.getLines());
    }

    public static boolean mySign(String text[]) {
        try {
            String textToParse = text[2].replace(" ", "");
            if ((textToParse.contains("S") || textToParse.contains("B")) && !text[3].isEmpty() && Basic.isInt(text[1])) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public static float buyPrice(String text) {
        text = text.replace(" ", "");
        text = text.toLowerCase();
        if(!text.contains("b")){
            return 0;
        }
        //text = text.replaceAll("(?!free)[A-Z,a-z]", "");
        text = text.replace("b", "");
        String bs[] = text.split(":");
        if (bs.length == 1) {
            //text = text.replace("B", "");
            if (text.equals("free")) {
                return -1;
            }
            if (Basic.isFloat(text)) {
                return Float.parseFloat(text);
            }
        } else if (bs.length == 2) {
            text = bs[0];
            //text = text.replace("B", "");
            if (text.equals("free")) {
                return -1;
            }
            if (Basic.isFloat(text)) {
                return Float.parseFloat(text);
            }
        }
        return 0;
    }
    public static float buyPrice(Sign sign) {
        String text = sign.getLine(2);
        return buyPrice(text);
    }

    public static float sellPrice(String text) {
        text = text.replace(" ", "");
        text = text.toLowerCase();
        if (!text.contains("s")) {
            return 0;
        }
        text = text.replace("s", "");
        String bs[] = text.split(":");
        if (bs.length == 1) {
            if (text.equals("free")) {
                return -1;
            }
            if (Basic.isFloat(text)) {
                return Float.parseFloat(text);
            }
        } else if (bs.length == 2) {
            text = bs[1];
            if (text.equals("free")) {
                return -1;
            }
            if (Basic.isFloat(text)) {
                return Float.parseFloat(text);
            }
        }
        return 0;
    }
    
    public static boolean priceIsNegative(String text){
        text = text.replace(" ", "");
        String bs[] = text.split(":");
        if(bs.length == 1){
            String fNum = text.replaceAll("[A-Z,a-z]", "");
            if(Basic.isFloat(fNum)){
                if(Float.parseFloat(fNum) < 0){
                    return true;
                }
            }
        } else if(bs.length == 2){
            for (int i = 0; i <= 1; i++) {
                text = bs[i];
                String fNum = text.replaceAll("[A-Z,a-z]", "");
                if (Basic.isFloat(fNum)) {
                    if (Float.parseFloat(fNum) < 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static float sellPrice(Sign sign) {
        String text = sign.getLine(2);
        return sellPrice(text);
    }
    
    public static int getItemAmount(Sign sign){
        String test = sign.getLine(1);
        if(Basic.isInt(test)){
            return Integer.parseInt(test);
        }
        return 0;
    }
    
    public static String getOwner(Sign sign){
        String owner = sign.getLine(0);
        return owner;
    }
}
