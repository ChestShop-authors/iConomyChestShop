package com.Acrobot.iConomyChestShop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.persistence.PersistenceException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * iConomyChestShop for Bukkit
 *
 * @author Acrobot
 */
public class iConomyChestShop extends JavaPlugin {
    private final iConomyChestShopPlayerListener playerListener = new iConomyChestShopPlayerListener();
    private final iConomyChestShopBlockListener blockListener = new iConomyChestShopBlockListener();
    private final iConomyChestShopPluginListener pluginListener = new iConomyChestShopPluginListener();
    private final SignManager signManager = new SignManager();
    private final iConomyChestShopBlockBreak blockBreakListener = new iConomyChestShopBlockBreak();
    
    private static Server Server = null;
    private static final HashMap<Player, Boolean> sellUsers = new HashMap<Player, Boolean>();
    

    public void onEnable() {
        Server = getServer();
        
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockBreakListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, signManager, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Highest, this);
        
        PluginDescriptionFile pdfFile = this.getDescription();

        setupDatabaseFile(); //To prevent errors.
        ConfigManager.load();
        int interval;

        if(ConfigManager.getBoolean("useDB")){
            setupDatabase(); //DB stuff
            if((interval = ConfigManager.getInt("intervalBetweenGeneratingTransactionList")) == 0){
                interval = 300;
            }
            getServer().getScheduler().scheduleAsyncRepeatingTask(this, new DBqueue(), 20L * 10, 20L * 10);
            if(ConfigManager.getBoolean("generateTransactionList")){
                getServer().getScheduler().scheduleAsyncRepeatingTask(this, new StatGenerator(), 20L * interval, 20L * interval);
            }
        }
        if((interval = ConfigManager.getInt("intervalBetweenTransactions")) == 0){
            interval = 300;
        }
        iConomyChestShopPlayerListener.interval = interval;
        
        Logging.setPlugin(this);
        StatGenerator.setPlugin(this);
        
        System.out.println("[" + pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " initialized!");
    }

    public static boolean enabled(Player player) {
        return sellUsers.containsKey(player);
    }
    
    private void setupDatabaseFile(){
        File file = new File("ebean.properties");
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch (Exception e){
                System.out.println("[iConomyChestShop] Failed to create ebean.properties file.");
            }
        }
    }
    
    private void setupDatabase(){
        try{
            getDatabase().find(Transaction.class).findRowCount();
        } catch (PersistenceException ex) {
            System.out.println("[iConomyChestShop] Installing database for " + getDescription().getName() + " due to first time usage.");
            installDDL();
        }
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(Transaction.class);
        return list;
    }
    
    public void sellCommand(Player player) {
        if(!iConomyChestShopPlayerListener.canSell(player)){
            return;
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
        PluginDescriptionFile pdfFile = this.getDescription();
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
            if(commandName.equals("icsversion")){
                player.sendMessage("iConomyChestShop's version is: " + pdfFile.getVersion());
                return true;
            }
            if(commandName.equals("chest")){
                if(args.length != 1){
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
                Material mat;
                String dmgValue = "";
                if (args.length == 0) {
                    ItemStack itemInHand = player.getItemInHand();
                    mat = itemInHand.getType();
                    int id = mat.getId();
                    dmgValue = (id >= 256 && id <= 317 ? "" : ";" + itemInHand.getDurability());
                } else if (args.length == 1) {
                    if (Basic.isInt(args[0])) {
                        mat = Material.getMaterial(Integer.parseInt(args[0]));
                    } else {
                        //mat = Basic.getMat(args[0]);
                        try {
                            ItemStack itemStack = Basic.getItemStack(args[0].replace(":", ";"));
                            mat = itemStack.getType();
                            int id = mat.getId();
                            dmgValue = (id >= 256 && id <= 317 ? "" : ";" + itemStack.getDurability());
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
                String othernames = "";
                if (Basic.OI != null) {
                    Set<String> aliases = Basic.OI.getAliases(mat.getId() + dmgValue.replace(":", ";"));
                    for (String alias : aliases) {
                        if (!(alias + "").equalsIgnoreCase(mat.name()) && !Basic.isInt(alias + "")) {
                            othernames += ", " + alias;
                        }
                    }
                }
                player.sendMessage(Basic.colorChat(ConfigManager.getLanguage("iteminfo")));
                String msg = "&b" + mat.getId() + (!dmgValue.equals("") ? dmgValue.replace(";", ":") : "") + "&f - " + "&b" + mat.name() + othernames;
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
    
    public static iConomyChestShop getPlugin(){
        return Logging.plugin;
    }
    
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[" + pdfFile.getName() + "]"  + " version " + pdfFile.getVersion() + " distabled!");
        
        DBqueue.saveQueueOnExit();
    }
}

