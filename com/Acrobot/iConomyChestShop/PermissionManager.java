package com.Acrobot.iConomyChestShop;

import com.nijiko.permissions.PermissionHandler;
import org.bukkit.entity.Player;

/**
 * iConomyChestShop permission handler
 * @author Acrobot
 */
public class PermissionManager {

    public static PermissionHandler Permissions = null;

    public static boolean hasPermissions(Player player, String permission) {
        if (Permissions != null) {
            return Permissions.has(player, permission);
        } else {
            if (permission.contains("exclude")) {
                return false;
            }
            if (permission.contains("admin")) {
                return player.isOp();
            } else {
                return true;
            }
        }
    }
}
