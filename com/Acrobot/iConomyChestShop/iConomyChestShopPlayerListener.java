package com.Acrobot.iConomyChestShop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * Handle events for all Player related events
 * @author Acrobot
 */
public class iConomyChestShopPlayerListener extends PlayerListener {

    private final iConomyChestShop plugin;
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getName().toLowerCase().replace(" ", "").equals("adminshop")) {
            if (!PermissionManager.hasPermissions(player, "iConomyChestShop.shop.admin")) {
                player.sendMessage("[iConomyChestShop] Do you really think you can trick me?");
                Logging.log("Player " + player.getName() + " with ip: " + player.getAddress().getAddress().getHostAddress() + " tried to use this plugin.");
                event.setCancelled(true);
                return;
            }
        }
        Block clickedBlock = event.getClickedBlock();
        Action action = event.getAction();
        boolean sneakMode = ConfigManager.getBoolean("sneakMode");
        if ((action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK) || (action == Action.LEFT_CLICK_BLOCK && !sneakMode)) {
            return;
        }
        if (!PermissionManager.hasPermissions(player, "iConomyChestShop.shop.admin")) {
            if (clickedBlock.getTypeId() == 54) {
                ProtectionManager.chestInteract(clickedBlock, player, event);
            }
        }
        if (!(clickedBlock.getType().equals(Material.SIGN) || clickedBlock.getType().equals(Material.SIGN_POST) || clickedBlock.getType().equals(Material.WALL_SIGN))) {
            return;
        }
        Sign sign = (Sign) event.getClickedBlock().getState();
        String name = sign.getLine(0);
        if (iConomyManager.getiConomy() == null && iConomyManager.BOSEconomy == null) {
            System.out.println("[iConomyChestShop] No economy plugin found!");
            player.sendMessage("[iConomyChestShop] No economy plugin found!");
            return;
        }
        if (!Basic.checkConfig(player)) {
            player.sendMessage("[iConomyChestShop] Aborting!");
            return;
        }
        if (!(clickedBlock.getFace(BlockFace.valueOf(ConfigManager.getString("position").toUpperCase()), ConfigManager.getInt("distance")).getType() == Material.CHEST) && !name.toLowerCase().replace(" ", "").equals("adminshop")) {
            return;
        }
        if (!SignManager.mySign(sign)) {
            return;
        }
        if (!name.toLowerCase().replace(" ", "").equals("adminshop")) {
            if (!iConomyManager.hasAccount(name)) {
                player.sendMessage(ConfigManager.getLanguage("Seller_has_no_account"));
                return;
            }
        }
        if (sign.getLine(0).equals(player.getName())) {
            player.sendMessage(ConfigManager.getLanguage("You_cannot_use_your_own_shop"));
            return;
        }
        if(Integer.parseInt(sign.getLine(1)) < 1){
            return;
        }
        if (ConfigManager.getBoolean("sneakMode") && action == Action.LEFT_CLICK_BLOCK) {
            if (player.isSneaking()) {
                return;
            }
            if (!canSell(player)) {
                return;
            }
            ShopManager.sell(event);
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            if (!plugin.enabled(player)) {
                if(!canBuy(player)){
                    return;
                }
                ShopManager.buy(event);
            } else {
                if(!canSell(player)){
                    return;
                }
                ShopManager.sell(event);
            }
        }
    }

    public static boolean canSell(Player p) {
        if(!ConfigManager.getBoolean("sell")){
            p.sendMessage("[iConomyChestShop]" + ChatColor.RED.toString() + " Selling to shops is turned off!");
            return false;
        }
        if (!PermissionManager.hasPermissions(p, "iConomyChestShop.shop.sell")) {
            p.sendMessage("[Permissions]" + ChatColor.RED.toString() + " You can't sell to shops!");
            return false;
        }
        return true;
    }
    
    public static boolean canBuy(Player p) {
        if (!PermissionManager.hasPermissions(p, "iConomyChestShop.shop.buy")) {
            p.sendMessage("[Permissions]" + ChatColor.RED.toString() + " You can't buy from shops!");
            return false;
        }
        return true;
    }

    public iConomyChestShopPlayerListener(iConomyChestShop instance) {
        plugin = instance;
    }
}
