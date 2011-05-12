package com.Acrobot.iConomyChestShop;

import com.griefcraft.lwc.LWCPlugin;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijikokun.register.payment.Methods;
import info.somethingodd.bukkit.OddItem.OddItem;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.yi.acru.bukkit.Lockette.Lockette;

/**
 * iConomyChestShop Plugin Listener
 * @author Acrobot
 */
public class iConomyChestShopPluginListener extends ServerListener{

    private Methods Methods = new Methods();
    public iConomyChestShopPluginListener()  { }
    
    @Override
    public void onPluginEnable(PluginEnableEvent event) {

        //Economy plugins
        if(!this.Methods.hasMethod()){
            if(this.Methods.setMethod(event.getPlugin())){
                EconomyManager.economy = this.Methods.getMethod();
                System.out.println("[iConomyChestShop] " + EconomyManager.economy.getName() + " version " + EconomyManager.economy.getVersion() + " loaded.");
            }
        }

        //Permissions
        if (PermissionManager.Permissions == null) {
            Plugin permissions = iConomyChestShop.getBukkitServer().getPluginManager().getPlugin("Permissions");

            if (permissions != null) {
                //iConomyChestShop.getBukkitServer().getPluginManager().enablePlugin(permissions);
                PermissionManager.Permissions = ((Permissions) permissions).getHandler();
                PluginDescriptionFile pDesc = permissions.getDescription();
                System.out.println("[iConomyChestShop] " + pDesc.getName() + " version " + pDesc.getVersion() + " loaded.");
            }
        }

        //LWC
        if (ProtectionManager.lwc == null) {
            Plugin lwcPlugin = iConomyChestShop.getBukkitServer().getPluginManager().getPlugin("LWC");

            if (lwcPlugin != null) {
                PluginDescriptionFile pDesc = lwcPlugin.getDescription();
                ProtectionManager.lwc = ((LWCPlugin) lwcPlugin).getLWC();
                System.out.println("[iConomyChestShop] " + pDesc.getName() + " version " + pDesc.getVersion() + " loaded.");
            }
        }
        
        //OddItem
        if (Basic.OI == null) {
            Plugin oddItem = iConomyChestShop.getBukkitServer().getPluginManager().getPlugin("OddItem");

            if (oddItem != null) {
                PluginDescriptionFile pDesc = oddItem.getDescription();
                Basic.OI = (OddItem) iConomyChestShop.getBukkitServer().getPluginManager().getPlugin("OddItem");
                System.out.println("[iConomyChestShop] " + pDesc.getName() + " version " + pDesc.getVersion() + " loaded.");
            }
        }
        
        //Lockette
        if (ProtectionManager.lockette == null) {
            Plugin lockette = iConomyChestShop.getBukkitServer().getPluginManager().getPlugin("Lockette");

            if (lockette != null) {
                PluginDescriptionFile pDesc = lockette.getDescription();
                ProtectionManager.lockette = ((Lockette) lockette);
                System.out.println("[iConomyChestShop] " + pDesc.getName() + " version " + pDesc.getVersion() + " loaded.");
            }
        }
    }
}
