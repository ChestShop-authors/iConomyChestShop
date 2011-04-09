package com.Acrobot.iConomyChestShop;

import com.Acrobot.iConomyChestShop.MinecartMania.MinecartManiaChest;
import java.util.logging.Level;
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

/**
 * Manages signs
 * @author Acrobot
 */
public class SignManager extends BlockListener{
    
    @Override
    public void onSignChange(SignChangeEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();
        Sign sign = null;
        Material type = block.getType();
        boolean isAdmin = PermissionManager.hasPermissions(p, "iConomyChestShop.shop.admin");
        
        if (type.equals(Material.SIGN) || type.equals(Material.SIGN_POST) || type.equals(Material.WALL_SIGN)) {
            sign = (Sign) block.getState();
        } else {
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
                isGoodSign = (Basic.isInt(split[0]) && Basic.isInt(split[1])) || isFormated;
            } catch (ArrayIndexOutOfBoundsException aioob) {
                return;
            }
            if (isGoodSign) {
                if(Basic.OI == null){
                    Logging.log("OddItem is not available, item aliases and colored wool/dyes/wood won't work.");
                }
                if (!PermissionManager.hasPermissions(p, "iConomyChestShop.shop.create")) {
                    p.sendMessage("[Permissions] You can't make shops!");
                    event.getBlock().setType(Material.AIR);
                    return;
                }
                if (!(!text[0].equals("") && isAdmin)) {
                    event.setLine(0, p.getName());
                }
                
                if (chest != null) {
                    if (ProtectionManager.isProtected(ChestB) && !isAdmin) {
                        if (!ProtectionManager.protectedByWho(ChestB).equals(Basic.stripName(p.getName()))) {
                            p.sendMessage(ConfigManager.getLanguage("You_tried_to_steal"));
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            return;
                        }
                    }
                    MinecartManiaChest mmc = new MinecartManiaChest((Chest) chest);
                    MinecartManiaChest neighbor = mmc.getNeighborChest();
                    if(neighbor != null)
                    {
                        CraftSign sig = ProtectionManager.getSign(mmc.getNeighborChest().getChest().getBlock(), true);
                        if(sig != null){
                            if (!sig.getLine(0).equals(p.getName()) && !isAdmin) {
                                p.sendMessage(ConfigManager.getLanguage("You_tried_to_steal"));
                                event.setCancelled(true);
                                event.getBlock().setType(Material.AIR);
                                return;
                            }
                        }
                    }
                } else if(!text[0].toLowerCase().replace(" ", "").equals("adminshop")){
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    p.sendMessage(ConfigManager.getLanguage("Shop_cannot_be_created"));
                    return;
                }
                if ((text[2].length() > 11 && !isFormated) || text[1].length() > 15 || text[3].length() > 15) {
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    return;
                }
                if (Basic.isInt(text[3])) {
                    Material signMat = Material.getMaterial(Integer.parseInt(text[3]));
                    if(signMat == null){
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        p.sendMessage(ConfigManager.getLanguage("incorrectID"));
                        return;
                    }
                    String alias = Basic.returnAlias(signMat.name());
                    if (alias != null) {
                        event.setLine(3, alias);
                    } else if (signMat.name() != null) {
                        event.setLine(3, signMat.name());
                    } else {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        p.sendMessage(ConfigManager.getLanguage("incorrectID"));
                        return;
                    }
                }else if(text[3].contains(";")){
                    String alias = Basic.returnAlias(text[3]);
                    if (alias != null) {
                        event.setLine(3, alias);
                    }
                    
                }
                if (Basic.getItemStack(text[3]) == null) {
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    p.sendMessage(ConfigManager.getLanguage("incorrectID"));
                    return;
                }
                if (!isFormated) {
                    event.setLine(2, "B " + split[0] + ":" + split[1] + " S");
                }
                if (chest != null) {
                    if(ProtectionManager.protectBlock(ChestB, text[0]) == true){
                        p.sendMessage(ConfigManager.getLanguage("Shop_was_LWC_protected"));
                    }
                    p.sendMessage(ConfigManager.getLanguage("Shop_is_created"));
                }

            }
        }
    }
    
    
    public static boolean mySign(Sign sign) {
        if (sign == null) {
            return false;
        }
        try {
            try {
                String text[] = sign.getLines();
                String bs[] = text[2].split(":");
                if (Basic.isInt(bs[0].substring(2)) && Basic.isInt(bs[1].substring(0, bs[1].length() - 2)) && text[0].length() > 1) {
                    return true;
                }
                return false;
            } catch (ArrayIndexOutOfBoundsException aioob) {
                return false;
            }
        } catch (StringIndexOutOfBoundsException sioob) {
            return false;
        }
    }

    public static boolean mySign(String text[]) {
        try {
            try {
                String bs[] = text[2].split(":");
                if (bs[0].substring(0, 1).equals("B") && bs[1].substring(bs[1].length() - 1, bs[1].length()).equals("S") && Basic.isInt(bs[0].substring(2)) && Basic.isInt(bs[1].substring(0, bs[1].length() - 2)) && text[0].length() > 1) {
                    return true;
                }
                return false;
            } catch (ArrayIndexOutOfBoundsException aioob) {
                return false;
            }
        } catch (StringIndexOutOfBoundsException sioob) {
            return false;
        }
    }

    public static int buyPrice(Sign sign) {
        String text[] = sign.getLines();
        String bs[] = text[2].split(":");
        return Integer.parseInt(bs[0].substring(2));
    }

    public static int sellPrice(Sign sign) {
        String text[] = sign.getLines();
        String bs[] = text[2].split(":");
        return Integer.parseInt(bs[1].substring(0, bs[1].length() - 2));
    }
}
