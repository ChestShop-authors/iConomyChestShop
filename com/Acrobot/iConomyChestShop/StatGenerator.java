package com.Acrobot.iConomyChestShop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.bukkit.Material;

/**
 * Generating a file with statistics for buy/sell
 * @author Acrobot
 */
public class StatGenerator implements Runnable{
    private static iConomyChestShop plugin;
    private static long time;
    private static String row = fileToString("row");
    private static String header = fileToString("header");
    private static String footer = fileToString("footer");
    private static boolean unusedItems = ConfigManager.getBoolean("showUnusedItemsInGeneratedList");
    private static FileWriter fileWriter;
    private static BufferedWriter buf;
    
    public static void setPlugin(iConomyChestShop plugin){
        StatGenerator.plugin = plugin;
    }
    public static void generateStatisticForItem(int itemID){
        generateStatisticForItem(itemID, -1);
    }
    public static float generateAverageBuyPrice(int itemID){
        float price = 0;
        List<Transaction> prices = plugin.getDatabase().find(Transaction.class).where().eq("itemID", itemID).eq("buy", 1).findList();
        for(Transaction t : prices){
            price += t.getPrice();
        }
        float toReturn = price/prices.size();
        return (!Float.isNaN(toReturn) ? toReturn : 0);
    }
    
    public static float generateAverageSellPrice(int itemID){
        float price = 0;
        List<Transaction> prices = plugin.getDatabase().find(Transaction.class).where().eq("itemID", itemID).eq("buy", 0).findList();
        for(Transaction t : prices){
            price += t.getPrice()/t.getAmount();
        }
        float toReturn = price/prices.size();
        return (!Float.isNaN(toReturn) ? toReturn : 0);
    }
    
    public static double generateItemTotal(int itemID){
        return generateItemTotal(itemID, false);
    }
    
    public static double generateItemTotal(int itemID, boolean bought){
        return generateItemTotal(itemID, bought, false);
    }
    
    public static double generateItemTotal(int itemID, boolean bought, boolean sold){
        double amount = 0;
        List<Transaction> list;
        if(bought){
            list = plugin.getDatabase().find(Transaction.class).where().eq("buy", 1).eq("itemID", itemID).findList();
        }else if (sold){
            list = plugin.getDatabase().find(Transaction.class).where().eq("buy", 0).eq("itemID", itemID).findList();
        }else{
            list = plugin.getDatabase().find(Transaction.class).where().eq("itemID", itemID).findList();
        }
        for(Transaction t : list){
            amount += t.getAmount();
        }
        return amount;
    }
    
    public static double generateTotalBought(int itemID){
        return generateItemTotal(itemID, true);
    }
    
    public static double generateTotalSold(int itemID){
        return generateItemTotal(itemID,false,true);
    }
    
    public static void generateStatisticForItem(int itemID, int itemDurability){
        try{
            //FileWriter fw = new FileWriter("plugins/iConomyChestShop/ChestShop.stats.html", true);
            //BufferedWriter bw = new BufferedWriter(fw);
            double total = generateItemTotal(itemID);
            double bought = generateTotalBought(itemID);
            double sold = generateTotalSold(itemID);
            String matName = Material.getMaterial(itemID).name().replace("_", " ").toLowerCase();
            float buyPrice = generateAverageBuyPrice(itemID);
            int maxStackSize = Material.getMaterial(itemID).getMaxStackSize();
            if(total == 0 && !unusedItems){
                return;
            }
            buf.write(row.replace("%material", matName)
                    .replace("%total", total + "")
                    .replace("%bought", bought+"")
                    .replace("%sold", sold + "")
                    .replace("%maxStackSize", maxStackSize + "")
                    .replace("%pricePerStack", (buyPrice * maxStackSize) + "")
                    .replace("%pricePerItem", buyPrice + ""));
            
            
        } catch (Exception e){
            return;
        }
    }
    public static void fileStart() throws IOException{
        FileWriter fw = new FileWriter("plugins/iConomyChestShop/ChestShop.stats.html");
        fw.write(header);
        fw.close();
    }
    public static void fileEnd() throws IOException{
        FileWriter fw = new FileWriter("plugins/iConomyChestShop/ChestShop.stats.html", true);
        fw.write(footer.replace("%time", time + ""));
        fw.close();
    }
    public static void generateStatistics(){
        try {
            time = System.currentTimeMillis();
            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Logging());
            fileStart();
            
            fileWriter = new FileWriter("plugins/iConomyChestShop/ChestShop.stats.html", true);
            buf = new BufferedWriter(fileWriter);
            
            for (Material m : Material.values()) {
                generateStatisticForItem(m.getId());
            }
            buf.close();
            time = System.currentTimeMillis() - time;
            fileEnd();
        } catch (Exception e) {
            return;
        }
    }
    public static String fileToString(String fileName) {
        try {
            File f = new File("plugins/iConomyChestShop/" + fileName + ".txt");
            FileReader rd = new FileReader(f);
            char[] buf = new char[(int) f.length()];
            rd.read(buf);
            return new String(buf);
        } catch (IOException e) {
            return "";
        }
    }
    
    @Override
    public void run(){
        generateStatistics();
    }
}
