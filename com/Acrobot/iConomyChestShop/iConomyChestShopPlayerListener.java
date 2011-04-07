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
        Block clickedBlock = event.getClickedBlock();
        Action action = event.getAction();
        boolean sneakMode = ConfigManager.getBoolean("sneakMode");
        if (action != Action.RIGHT_CLICK_BLOCK && (action != Action.LEFT_CLICK_BLOCK && sneakMode)) {
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
        if (iConomyManager.getiConomy() == null) {
            System.out.println("[iConomyChestShop] No iConomy found!");
            player.sendMessage("[iConomyChestShop] No iConomy found!");
            return;
        }
        if (!Basic.checkConfig(player)) {
            player.sendMessage("[iConomyChestShop] Aborting!");
            return;
        }
        if (!(clickedBlock.getFace(BlockFace.valueOf(ConfigManager.getString("position").toUpperCase()), ConfigManager.getInt("distance")).getType() == Material.CHEST)) {
            return;
        }
        if (!SignManager.mySign(sign)) {
            return;
        }
        if (iConomyManager.bank.getAccount(name) == null && !name.toLowerCase().replace(" ", "").equals("adminshop")) {
            player.sendMessage(ConfigManager.getLanguage("Seller_has_no_account"));
            return;
        }
        if (sign.getLine(0).equals(player.getName())) {
            player.sendMessage(ConfigManager.getLanguage("You_cannot_use_your_own_shop"));
            return;
        }
        if (ConfigManager.getBoolean("sneakMode")) {
            if (player.isSneaking()) {
                return;
            }
            if (action == Action.LEFT_CLICK_BLOCK) {
                ShopManager.sell(event);
            } else {
                ShopManager.buy(event);
            }
            return;
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            if (!plugin.enabled(player)) {
                if (!PermissionManager.hasPermissions(player, "iConomyChestShop.shop.buy")) {
                    player.sendMessage("[Permissions]" + ChatColor.RED.toString() + " You can't buy from shops!");
                    return;
                }
                ShopManager.buy(event);
            } else {
                if (!PermissionManager.hasPermissions(player, "iConomyChestShop.shop.sell")) {
                    player.sendMessage("[Permissions]" + ChatColor.RED.toString() + " You can't sell to shops!");
                    return;
                }
                ShopManager.sell(event);
            }
        }
    }

    public iConomyChestShopPlayerListener(iConomyChestShop instance) {
        plugin = instance;
    }
}
