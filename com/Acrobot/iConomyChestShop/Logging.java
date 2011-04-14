package com.Acrobot.iConomyChestShop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author Acrobot
 */
public class Logging {
    private static final Logger logger = Logger.getLogger("Minecraft.iConomyChestShop");
    
    public static String getDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        Date date = new Date();
        
        return dateFormat.format(date);
    }
    public static void log(String msg){
        logger.info("[iConomyChestShop] " + msg);
        String filePath = ConfigManager.getString("logFilePath");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
            out.write(getDateAndTime() + " [iConomyChestShop] "+ msg);
            out.newLine();
            out.close();
        } catch (IOException e) {
            logger.info("[iConomyChestShop] Cannot write to file \"");
        }
    }
    
}
