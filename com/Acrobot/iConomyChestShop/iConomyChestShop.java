package com.Acrobot.iConomyChestShop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * iConomyChestShop for Bukkit
 *
 * @author Acrobot
 */
public class iConomyChestShop extends JavaPlugin {
    private final iConomyChestShopPlayerListener playerListener = new iConomyChestShopPlayerListener(this);
    private final iConomyChestShopBlockListener blockListener = new iConomyChestShopBlockListener(this);
    private final iConomyChestShopPluginListener pluginListener = new iConomyChestShopPluginListener();
    private final SignManager signManager = new SignManager();
    private final iConomyChestShopBlockBreak blockBreakListener = new iConomyChestShopBlockBreak();
    
    private static Server Server = null;
    private final HashMap<Player, Boolean> sellUsers = new HashMap<Player, Boolean>();
    

    public void onEnable() {
        Server = getServer();
        
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockBreakListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_INTERACT, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, signManager, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Highest, this);
        //pm.registerEvent(Event.Type.INVENTORY_CLOSE, playerListener, Priority.Normal, this); disabled untill it's in bukkit
        
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[" + pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " initialized!");
    }

    public boolean enabled(Player player) {
        return sellUsers.containsKey(player);
    }
    
    public void sellCommand(Player player) {
        if (!PermissionManager.hasPermissions(player, "iConomyChestShop.shop.sell")) {
            player.sendMessage("[Permissions]" + ChatColor.RED.toString() + " You can't sell to shops!");
        }
        if (ConfigManager.getBoolean("sell")) {
            sellUsers.put(player, null);
            player.sendMessage(ConfigManager.getLanguage("Mode_changed_to_sell"));
        } else {
            player.sendMessage("[Shop] Selling to shops turned off in the config.");
        }
    }

    public void buyCommand(Player player) {
        if (enabled(player)) {
            sellUsers.remove(player);
            player.sendMessage(ConfigManager.getLanguage("Mode_changed_to_buy"));
        } else {
            player.sendMessage(ConfigManager.getLanguage("Mode_changed_to_buy"));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String commandName = cmd.getName().toLowerCase();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (commandName.equals("buy")) {
                buyCommand(player);
                return true;
            }
            if (commandName.equals("sell")) {
                sellCommand(player);
                return true;
            }
            if(commandName.equals("chest")){
                if(args[0] == null){
                    return false;
                }
                if(args[0].equals("buy")){
                    buyCommand(player);
                    return true;
                }
                if(args[0].equals("sell")){
                    sellCommand(player);
                    return true;
                }
                return false;
            }
            if (commandName.equals("iteminfo")) {
                if (!PermissionManager.hasPermissions(player, "iConomyChestShop.command.iteminfo")) {
                    player.sendMessage("[Permissions]" + ChatColor.RED.toString() + " You can't see item informations!");
                    return true;
                }
                Material mat = null;

                if (args.length == 0) {
                    mat = player.getItemInHand().getType();
                } else if (args.length == 1) {
                    if (Basic.isInt(args[0])) {
                        mat = Material.getMaterial(Integer.parseInt(args[0]));
                    } else {
                        //mat = Basic.getMat(args[0]);
                        try {
                            mat = Basic.getItemStack(args[0]).getType();
                        } catch (Exception ex) {
                            mat = Basic.getMat(args[0]);
                        }
                    }
                } else {
                    return false;
                }

                if (mat == null) {
                    return false;
                }
                String matName = "";
                String othernames = "";
                if (Basic.OI != null) {
                    Set<String> aliases = Basic.OI.getAliases(mat.getId() + "");
                    Iterator iter = aliases.iterator();
                    //int loops = 0;
                    while (iter.hasNext()) {
                        //loops++;
                        Object obj = iter.next();
                        if (!(obj + "").equalsIgnoreCase(mat.name()) && !Basic.isInt(obj+"")) {
                                othernames += ", " + obj;
                        }
                    }
                }else{
                    matName = mat.getId() + "";
                }
                player.sendMessage(Basic.colorChat(ConfigManager.getLanguage("iteminfo")));
                String msg = "&b" + mat.getId() + "&f - " + "&b" + mat.name() + othernames;
                player.sendMessage(Basic.colorChat(msg));
                return true;
                
            }
            return false;
        }
        return true;
    }

    public static Server getBukkitServer() {
        return Server;
    }
    
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[" + pdfFile.getName() + "]"  + " version " + pdfFile.getVersion() + " distabled!");
    }
}

