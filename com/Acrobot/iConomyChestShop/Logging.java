package com.Acrobot.iConomyChestShop;

import com.avaje.ebean.EbeanServer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

/**
 *
 * @author Acrobot
 */
public class Logging implements Runnable{
    private static final Logger logger = Logger.getLogger("Minecraft.iConomyChestShop");
    private static iConomyChestShop plugin;
    
    public static void setPlugin(iConomyChestShop ics){
        plugin = ics;
    }
    
    public static String getDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        Date date = new Date();
        
        return dateFormat.format(date);
    }
    public static void log(String msg){
        logger.info("[iConomyChestShop] " + msg);
        if(!ConfigManager.getBoolean("logToFile")){
            return;
        }
        String filePath = ConfigManager.getString("logFilePath");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
            out.write(getDateAndTime() + " [iConomyChestShop] "+ msg);
            out.newLine();
            out.close();
        } catch (Exception e) {
            logger.info("[iConomyChestShop] Cannot write to file \"" + filePath + "\"");
        }
    }
    public static void logToDB(boolean playerBuysFromShop, Shop shop, Player player){
        logToDB(playerBuysFromShop, shop.owner, player.getName(), shop.stock.getTypeId(), shop.stock.getDurability(), shop.stockAmount, (playerBuysFromShop ? shop.getBuyPrice() : shop.getSellPrice()));
        if (ConfigManager.getBoolean("log")) {
            Logging.log(player.getName() + (playerBuysFromShop ? " bought " : " sold ") + shop.stockAmount + " " + shop.stock.getType() + " with durability of "
                    + shop.stock.getDurability() + (playerBuysFromShop ? " from " : " to ") + shop.owner + " for " + economyManager.formatedBalance((playerBuysFromShop ? shop.getBuyPrice() : shop.getSellPrice())));
        }
    }
    public static void logToDB(boolean playerBuysFromShop, String shopOwnerName, String user, int itemID, int itemDurability, int amount, float price){
        if(!ConfigManager.getBoolean("useDB")){
            return;
        }
        long time = System.currentTimeMillis()/1000;
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Logging());
        
        EbeanServer db = plugin.getDatabase();
        
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setBuy(playerBuysFromShop);
        transaction.setItemID(itemID);
        transaction.setItemDurability(itemDurability);
        transaction.setSec(time);
        transaction.setShopOwner(shopOwnerName);
        transaction.setShopUser(user);
        transaction.setPrice(price);
        
        db.save(transaction);
    }
    public static void removeOld(long time){
        EbeanServer database = plugin.getDatabase();
        List<Transaction> toDelete = database.find(Transaction.class).where().lt("sec", time - ConfigManager.getInt("howLongToStoreInformationInDB")).findList();
        database.delete(toDelete);
    }
    
    @Override
    public void run(){
        removeOld(System.currentTimeMillis()/1000);
    }
    
}
