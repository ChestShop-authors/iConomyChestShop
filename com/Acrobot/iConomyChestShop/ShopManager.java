package com.Acrobot.iConomyChestShop;

import com.Acrobot.iConomyChestShop.Chests.ChestObject;
import com.Acrobot.iConomyChestShop.Chests.MinecraftChest;
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
        Player player = event.getPlayer();
        boolean adminShop = sign.getLine(0).toLowerCase().replace(" ", "").equals("adminshop");

        Shop shop = new Shop(sign, (!adminShop ? Basic.findChest(event.getClickedBlock()) : null));

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
        if (!EconomyManager.hasEnough(player.getName(), shop.getBuyPrice())) {
            player.sendMessage(ConfigManager.getLanguage("You_have_got_not_enough_money"));
            return;
        }
        boolean OK = shop.buy(player);

        if (!OK) {
            //You couldn't buy
        }
    }

    public static void sell(Shop shop, Player player) {
        if (EconomyManager.hasAccount(shop.shopOwnerName()) && !EconomyManager.hasEnough(shop.shopOwnerName(), shop.getSellPrice())) {
            player.sendMessage(ConfigManager.getLanguage("Seller_has_not_enough_money"));
            return;
        }
        boolean OK = shop.sell(player);

        if (!OK) {
            //You couldn't sell
        }
    }
}
