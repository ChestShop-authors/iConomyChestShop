package com.Acrobot.iConomyChestShop;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Manages transactions
 * @author Acrobot
 */
public class Shop {
    
    
    private final ChestObject chest;
    public final String owner;
    private final float buyPrice;
    private final float sellPrice;
    public final ItemStack stock;
    public final int stockAmount;


    Shop(Sign sign, ChestObject obj) {
        //this.sign = sign;
        this.chest = obj;

        this.owner = SignManager.getOwner(sign);
        this.buyPrice = SignManager.buyPrice(sign);
        this.sellPrice = SignManager.sellPrice(sign);
        this.stock = Basic.getItemStack(sign.getLine(3).replace(":", ";"));
        this.stockAmount = SignManager.getItemAmount(sign);
    }


    public float getBuyPrice(){
        return (buyPrice == -1 ? 0 : buyPrice);
    }
    
    public float getSellPrice(){
        return (sellPrice == -1 ? 0 : sellPrice);
    }
    
    ////////////////////////////////////////////////////
    public boolean buy(Player player){
        if(stock == null){
            return false;
        }
        if(!isSelling()){
            player.sendMessage(ConfigManager.getLanguage("No_buying_from_this_shop"));
            return false;
        }
        if(!playerHasFreeSpace(player)){
            player.sendMessage(ConfigManager.getLanguage("Your_inventory_is_full"));
            return false;
        }
        if(!isAdminShop()){
            if(!hasEnoughStock()){
                player.sendMessage(ConfigManager.getLanguage("Shop_is_out_of_stock"));
                if(ConfigManager.getBoolean("showOutOfStock")){
                    String msg = ConfigManager.getLanguage("Your_shop_is_out_of_stock");
                    msg = msg.replace("<item>", stock.getType().name());
                    sendMsgToOwner(msg);
                }
                return false;
            }
            
        }
        if(!isAdminShop()){
            chest.removeItem(stock, stockAmount);
            EconomyManager.add(owner, getBuyPrice());
        }else if(EconomyManager.hasAccount(owner)){
            EconomyManager.add(owner, getBuyPrice());
        }
        stock.setAmount(stockAmount);
        Basic.addItemToInventory(player.getInventory(), stock, stockAmount);
        player.updateInventory();
        
        //////////////PAYMENT////////////////
        EconomyManager.substract(player.getName(), getBuyPrice());
        
        ConfigManager.buyingString(stockAmount, stock.getType().name(), owner, player, getBuyPrice());
        Logging.logToDB(true, this, player);
        return true;
    }
    
    public boolean sell(Player player){
        if(stock == null){
            return false;
        }
        if(!isBuying()){
            player.sendMessage(ConfigManager.getLanguage("No_selling_to_this_shop"));
            return false;
        }
        if(Basic.getItemAmountFromInventory(player.getInventory(), stock) < stockAmount){
            player.sendMessage(ConfigManager.getLanguage("You_have_not_enough_items"));
            return false;
        }
        
        if(!isAdminShop()){
            if(!hasFreeSpace()){
                player.sendMessage(ConfigManager.getLanguage("Chest_is_full"));
                return false;
            }
            chest.addItem(stock, stockAmount);
            EconomyManager.substract(owner, getSellPrice());
        }else if(EconomyManager.hasAccount(owner)){
            EconomyManager.substract(owner, getSellPrice());
        }
        
        Basic.removeItemStackFromInventory(player.getInventory(), stock, stockAmount);
        player.updateInventory();
        
        //////////////PAYMENT////////////////
        EconomyManager.add(player.getName(), getSellPrice());
        
        ConfigManager.sellingString(stockAmount, stock.getType().name(), owner, player, getSellPrice());
        Logging.logToDB(false, this, player);
        return true;
    }
    
    public boolean isAdminShop(){
        return owner.toLowerCase().replace(" ", "").equals("adminshop");
    }
    ////////////////////////////////////////////////////
    
    
    //Private functions
    private void sendMsgToOwner(String msg){
        if(isAdminShop()){
            return;
        }
        Player player = iConomyChestShop.getBukkitServer().getPlayer(owner);
        if(player == null){
            return;
        }
        player.sendMessage(msg);
    }
    
    private boolean hasEnoughStock(){
        return chest.hasEnough(stock, stockAmount);
    }
    
    private boolean isSelling(){
        return buyPrice != 0;
    }
    
    private boolean isBuying(){
        return sellPrice != 0;
    }
    
    private boolean hasFreeSpace(){
        return chest.hasFreeSpace(stock, stockAmount);
    }
    
    private boolean playerHasFreeSpace(Player player){
        return Basic.checkFreeSpace(player.getInventory(), stock, stockAmount);
        //return true;
    }
    
}
