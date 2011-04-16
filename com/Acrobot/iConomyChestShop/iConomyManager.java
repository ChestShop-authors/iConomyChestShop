package com.Acrobot.iConomyChestShop;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;
import com.nijiko.coelho.iConomy.system.Bank;
import cosine.boseconomy.BOSEconomy;

/**
 *
 * @author Acrobot
 */
public class iConomyManager {
    private static iConomy iConomy = null;
    public static BOSEconomy BOSEconomy = null;
    static Bank bank = null;
    
    public static iConomy getiConomy() {
        return iConomy;
    }

    public static boolean hasAccount(String p){
        if(iConomy != null){
            return bank.hasAccount(p);
        }
        if(BOSEconomy != null){
            return true;
        }
        return false;
    }
    public static boolean setiConomy(iConomy plugin) {
        if (iConomy == null) {
            iConomy = plugin;
            bank = com.nijiko.coelho.iConomy.iConomy.getBank();
        } else {
            return false;
        }
        return true;
    }
    
    public static void add(String name, int amount){
        if(iConomy != null){
            Account acc = bank.getAccount(name);
            acc.add(amount);
        }
        if(BOSEconomy != null){
            BOSEconomy.addPlayerMoney(name, amount, false);
        }
    }
    
    public static void substract(String name, int amount){
        if(iConomy != null){
            Account acc = bank.getAccount(name);
            acc.subtract(amount);
        }
        if(BOSEconomy != null){
            BOSEconomy.addPlayerMoney(name, -amount, false);
        }
    }
    public static boolean hasEnough(String name, int amount) {
        if (iConomy != null) {
            Account acc = bank.getAccount(name);
            return acc.hasEnough(amount);
        }
        if(BOSEconomy != null){
            return (BOSEconomy.getPlayerMoney(name) >= amount);
        }
        return false;
    }
    public static String getCurrency(){
        if(iConomy != null){
            return bank.getCurrency();
        }
        if(BOSEconomy != null){
            return BOSEconomy.getMoneyNameCaps();
        }
        return null;
    }
    public static double balance(String name){
        if(iConomy != null){
            return bank.getAccount(name).getBalance();
        }
        if(BOSEconomy != null){
            return BOSEconomy.getPlayerMoney(name);
        }
        return 0;
    }
    public static String formatedBalance(double amount){
        if(iConomy != null){
            return bank.format(amount);
        }
        String stringAmount = amount + "";
        stringAmount = stringAmount.replace(".0", "");
        if(BOSEconomy != null){
            return stringAmount + " " + BOSEconomy.getMoneyNameCaps();
        }
        return amount + "";
    }
}
