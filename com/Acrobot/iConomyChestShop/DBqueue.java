package com.Acrobot.iConomyChestShop;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Acrobot
 */
public class DBqueue implements Runnable{
    private static List<Transaction> queue = new LinkedList<Transaction>();
    
    public static void addToQueue(Transaction t){
        queue.add(t);
    }
    
    public static void saveQueue(){
        iConomyChestShop.getBukkitServer().getScheduler().scheduleAsyncDelayedTask(iConomyChestShop.getPlugin(), new DBqueue());
    }

    public static void saveQueueOnExit(){
        try{
            iConomyChestShop.getPlugin().getDatabase().save(queue);
            queue.clear();
        }catch (Exception ignored){}
    }
    
    public void run(){
        iConomyChestShop.getPlugin().getDatabase().save(queue);
        queue.clear();
    }
}
