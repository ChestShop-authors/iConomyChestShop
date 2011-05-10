package com.Acrobot.iConomyChestShop;

import com.Acrobot.iConomyChestShop.MinecartMania.MinecartManiaChest;
import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.griefcraft.model.ProtectionTypes;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Sign;
import org.yi.acru.bukkit.Lockette.Lockette;

/**
 *
 * @author Acrobot
 */
public class ProtectionManager {
    public static LWC lwc = null;
    public static Lockette lockette = null;
    
    
    
    public static boolean isProtected(Block chest){
        if(lwc != null){
            Protection lwcProt = lwc.findProtection(chest);
            if(lwcProt != null){
                return true;
            }
        }
        if (lockette != null) {
            boolean lockProt = Lockette.isProtected(chest);
            if(lockProt){
                return true;
            }
        }
        return false;

    }

    public static String protectedByWho(Block chest) {
        if (chest == null) {
            return null;
        }
        String name = "";
        if (lwc != null) {
            Protection prot = lwc.findProtection(chest);
            if(prot != null){
                name = prot.getOwner();
            }
        }
        if (lockette != null) {
            name = Lockette.getProtectedOwner(chest);
        }
        return name;
    }
    
    public static boolean protectBlock(Block block, String name){
        if(!ConfigManager.getBoolean("LWCprotection")){
            return false;
        }
        if(lwc != null){
            lwc.getPhysicalDatabase().registerProtection(block.getTypeId(), ProtectionTypes.PRIVATE, block.getWorld().getName(), name, "", block.getX(), block.getY(), block.getZ());
            return true;
        }else{
            return false;
        }
    }
    

    public static void chestDestroy(Block chest, Player player, BlockBreakEvent event) {
        if (!cfgProtection()) {
            return;
        }
        CraftSign sign = getSign(chest, false);
        if (sign == null) {
            return;
        } else if (!SignManager.mySign(sign)) {
            return;
        }
        if (!sign.getLine(0).equals(player.getName())) {
            player.sendMessage(ConfigManager.getLanguage("You_tried_to_steal"));
            event.setCancelled(true);
        }
    }
    
    public static void chestInteract(Block chest, Player player, PlayerInteractEvent event) {
        if (!cfgProtection()) {
            return;
        }
        CraftSign sign = getSign(chest, false);
        if (sign == null) {
            return;
        } else if (!SignManager.mySign(sign)) {
            return;
        }
        if (!sign.getLine(0).equals(player.getName())) {
            player.sendMessage(ConfigManager.getLanguage("You_tried_to_steal"));
            event.setCancelled(true);
        }
    }
    
    public static void blockDestroy(Block block, Player player, BlockBreakEvent event){
        Block[] blocks = {
            block.getFace(BlockFace.EAST),
            block.getFace(BlockFace.WEST),
            block.getFace(BlockFace.NORTH),
            block.getFace(BlockFace.SOUTH)};

        for (Block block1 : blocks) {
            Material blockType = block1.getType();
            if (blockType.equals(Material.SIGN) || blockType.equals(Material.SIGN_POST) || blockType.equals(Material.WALL_SIGN)) {
                CraftSign s = (CraftSign) block1.getState();
                if (SignManager.mySign(s)) {
                    if (!s.getLine(0).equals(player.getName())) {
                        player.sendMessage(ConfigManager.getLanguage("You_tried_to_steal"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public static void signDestroy(Block sign, Player player, BlockBreakEvent event) {
        CraftSign s = (CraftSign) sign.getState();
        if (!SignManager.mySign(s)) {
            return;
        }
        if (!s.getLine(0).equals(player.getName())) {
            player.sendMessage(ConfigManager.getLanguage("You_tried_to_steal"));
            event.setCancelled(true);
            s.update();
        }

    }

    public static CraftSign getSign(Block chest, boolean recurency) {
        CraftSign sign = Basic.findSign(chest);
        if (sign != null) {
            return sign;
        } else if (!recurency) {
            MinecartManiaChest mmc = new MinecartManiaChest((Chest) chest.getState());
            MinecartManiaChest nbChest = mmc.getNeighborChest();
            if (nbChest != null) {
                Chest nbchest = nbChest.getChest();
                return getSign(nbchest.getBlock(), true);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean cfgProtection() {
        return ConfigManager.getBoolean("protection");
    }
}
