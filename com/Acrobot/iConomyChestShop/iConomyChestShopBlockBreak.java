package com.Acrobot.iConomyChestShop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

/**
 * iConomy Chest shop block break event
 * @author Acrobot
 */
public class iConomyChestShopBlockBreak extends BlockListener{
    
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player p = event.getPlayer();
        boolean admin = PermissionManager.hasPermissions(p, "iConomyChestShop.shop.admin");
        if(admin){
            return;
        }
        if (block.getTypeId() == 54) {
            ProtectionManager.chestDestroy(block, p, event);
        }else if (block.getType().equals(Material.SIGN) || block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) {
            ProtectionManager.signDestroy(block, p, event);
        }else{
            ProtectionManager.blockDestroy(block, p, event);
        }
    }
}
