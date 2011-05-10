package com.Acrobot.iConomyChestShop;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * iConomyChestShop block listener
 * @author Acrobot
 */
public class iConomyChestShopBlockListener extends BlockListener {
    boolean debug = false;
    
    
    
    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        Material mat = e.getBlockAgainst().getType();
        if ((mat.equals(Material.SIGN) || mat.equals(Material.SIGN_POST) || mat.equals(Material.WALL_SIGN))) {
        	Sign sign = (Sign) e.getBlockAgainst().getState();
        	if(SignManager.mySign(sign)){
        		e.setCancelled(true);
        	}
        }
    }
    
    public iConomyChestShopBlockListener() {
    }
}
