package com.Acrobot.iConomyChestShop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author Acrobot
 */
public class ShopManager extends PlayerListener {

    public static void transaction(PlayerInteractEvent event) {
        boolean reverseButtons = ConfigManager.getBoolean("reverseButtons");
        Sign sign = (Sign) event.getClickedBlock().getState();
        Block chestBlock = event.getClickedBlock().getFace(BlockFace.valueOf(ConfigManager.getString("position").toUpperCase()), ConfigManager.getInt("distance"));
        Player player = event.getPlayer();
        boolean adminShop = sign.getLine(0).toLowerCase().replace(" ", "").equals("adminshop");

        if (!chestBlock.getType().equals(Material.CHEST) && !adminShop) {
            return;
        }
        Shop shop;
        if (!adminShop) {
            ChestObject chest = new ChestObject((Chest) chestBlock.getState());
            shop = new Shop(sign, chest);
        } else {
            shop = new Shop(sign, null);
        }

        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_BLOCK) {
            if (!iConomyChestShopPlayerListener.canSell(player)) {
                return;
            }
            if (!reverseButtons) {
                sell(shop, player);
            } else {
                buy(shop, player);
            }
        } else {
            event.setCancelled(true);
            if (!iConomyChestShop.enabled(player)) {
                if (!iConomyChestShopPlayerListener.canBuy(player)) {
                    return;
                }
                if (!reverseButtons) {
                    buy(shop, player);
                } else {
                    sell(shop, player);
                }
            } else {
                if (!iConomyChestShopPlayerListener.canSell(player)) {
                    return;
                }
                sell(shop, player);
            }
        }
    }

    public static void buy(Shop shop, Player player) {
        if (!economyManager.hasEnough(player.getName(), shop.getBuyPrice())) {
            player.sendMessage(ConfigManager.getLanguage("You_have_got_not_enough_money"));
            return;
        }
        boolean OK = shop.buy(player);

        if (!OK) {
            return;
        }
    }

    public static void sell(Shop shop, Player player) {
        if (economyManager.hasAccount(shop.owner) && !economyManager.hasEnough(shop.owner, shop.getSellPrice())) {
            player.sendMessage(ConfigManager.getLanguage("Seller_has_not_enough_money"));
            return;
        }
        boolean OK = shop.sell(player);

        if (!OK) {
            return;
        }
    }
}
