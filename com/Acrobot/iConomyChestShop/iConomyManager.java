package com.Acrobot.iConomyChestShop;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;
import com.nijiko.coelho.iConomy.system.Bank;

/**
 *
 * @author Acrobot
 */
public class iConomyManager {
    private static iConomy iConomy = null;
    static Bank bank = null;
    
    public static iConomy getiConomy() {
        return iConomy;
    }

    public static boolean setiConomy(iConomy plugin) {
        if (iConomy == null) {
            iConomy = plugin;
            bank = iConomy.getBank();
        } else {
            return false;
        }
        return true;
    }
    
    public static void add(String name, int amount){
        Account acc = bank.getAccount(name);
        acc.add(amount);
    }
    
    public static void substract(String name, int amount){
        Account acc = bank.getAccount(name);
        acc.subtract(amount);
    }
    public static boolean hasEnough(String name, int amount){
        Account acc = bank.getAccount(name);
        return acc.hasEnough(amount);
    }
    public static String getCurrency(){
        return bank.getCurrency();
    }
    public static double balance(String name){
        return bank.getAccount(name).getBalance();
    }
    public static String formatedBalance(double amount){
        return bank.format(amount);
    }
}
