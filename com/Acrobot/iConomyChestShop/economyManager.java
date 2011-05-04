package com.Acrobot.iConomyChestShop;

import com.iConomy.*;
import com.iConomy.system.Holdings;
import cosine.boseconomy.BOSEconomy;

/**
 *
 * @author Acrobot
 */
public class economyManager {
    private static iConomy iConomy = null;
    public static BOSEconomy BOSEconomy = null;
    
    public static iConomy getiConomy() {
        return iConomy;
    }

    public static boolean hasAccount(String p){
        if(iConomy != null){
            return iConomy.hasAccount(p);
        }
        if(BOSEconomy != null){
            return true;
        }
        return false;
    }
    public static boolean setiConomy(iConomy plugin) {
        if (iConomy == null) {
            iConomy = plugin;
        } else {
            return false;
        }
        return true;
    }
    
    public static void add(String name, float amount){
        if(iConomy != null){
            Holdings balance = iConomy.getAccount(name).getHoldings();
            balance.add(amount);
        }
        if(BOSEconomy != null){
            int intAmount = Math.round(amount);
            BOSEconomy.addPlayerMoney(name, intAmount, false);
        }
    }
    
    public static void substract(String name, float amount){
        if(iConomy != null){
            Holdings balance = iConomy.getAccount(name).getHoldings();
            balance.subtract(amount);
        }
        if(BOSEconomy != null){
            int intAmount = Math.round(amount);
            BOSEconomy.addPlayerMoney(name, -intAmount, false);
        }
    }
    public static boolean hasEnough(String name, float amount) {
        if (iConomy != null) {
            Holdings balance = iConomy.getAccount(name).getHoldings();
            return balance.hasEnough(amount);
        }
        if(BOSEconomy != null){
            return (BOSEconomy.getPlayerMoney(name) >= amount);
        }
        return false;
    }
    
    public static double balance(String name){
        if(iConomy != null){
            return iConomy.getAccount(name).getHoldings().balance();
        }
        if(BOSEconomy != null){
            return BOSEconomy.getPlayerMoney(name);
        }
        return 0;
    }
    
    public static String formatedBalance(double amount){
        if(iConomy != null){
            return iConomy.format(amount);
        }
        String stringAmount = amount + "";
        stringAmount = stringAmount.replace(".0", "");
        if(BOSEconomy != null){
            return stringAmount + " " + BOSEconomy.getMoneyNameCaps();
        }
        return amount + "";
    }
}
