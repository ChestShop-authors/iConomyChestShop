package com.Acrobot.iConomyChestShop.MinecartMania;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;

public class MinecartManiaWorld {
	private static ConcurrentHashMap<Location,MinecartManiaChest> chests = new ConcurrentHashMap<Location,MinecartManiaChest>();
	private static ConcurrentHashMap<String, Object> configuration = new ConcurrentHashMap<String,Object>();
	
	/**
	 ** Returns a new MinecartManiaChest from storage if it already exists, or creates and stores a new MinecartManiaChest object, and returns it
	 ** @param the chest to wrap
	 **/
	 public static MinecartManiaChest getMinecartManiaChest(Chest chest) {
		MinecartManiaChest testChest = chests.get(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()));
		if (testChest == null) {
			MinecartManiaChest newChest = new MinecartManiaChest(chest);
			chests.put(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()), newChest);
			return newChest;
		} 
		else {
			//Verify that this block is still a chest (could have been changed)
			if (MinecartManiaWorld.getBlockIdAt(testChest.getWorld(), testChest.getX(), testChest.getY(), testChest.getZ()) == Material.CHEST.getId()) {
				testChest.updateInventory(testChest.getInventory());
				return testChest;
			}
			else {
				chests.remove(new Location(chest.getWorld(), chest.getX(), chest.getY(), chest.getZ()));
				return null;
			}
		}
	}
	 
	/**
	 ** Returns true if the chest with the given location was deleted, false if not.
	 ** @param the  location of the chest to delete
	 **/
	 public static boolean delMinecartManiaChest(Location v) {
		if (chests.containsKey(v)) {
			chests.remove(v);
			return true;
		}
		return false;
	}
	
	/**
	* Returns an arraylist of all the MinecartManiaChests stored by this class
	* @return arraylist of all MinecartManiaChest
	*/
	public static ArrayList<MinecartManiaChest> getMinecartManiaChestList() {
		Iterator<Entry<Location, MinecartManiaChest>> i = chests.entrySet().iterator();
		ArrayList<MinecartManiaChest> chestList = new ArrayList<MinecartManiaChest>(chests.size());
		while (i.hasNext()) {
			chestList.add(i.next().getValue());
		}
		return chestList;
	 }
	 
	/**
	 ** Returns the value from the loaded configuration
	 ** @param the string key the configuration value is associated with
	 **/
	 public static Object getConfigurationValue(String key) {
		 if (configuration.containsKey(key)) {
			 return configuration.get(key);
		 }
		 return null;
	 }
	 
	/**
	 ** Creates a new configuration value if it does not already exists, or resets an existing value
	 ** @param the string key the configuration value is associated with
	 ** @param the value to store
	 **/	 
	 public static void setConfigurationValue(String key, Object value) {
		 if (value == null) {
			 configuration.remove(key);
		 }
		 else {
			 configuration.put(key, value);
		 }
	 }
	 
	 public static ConcurrentHashMap<String, Object> getConfiguration() {
		 return configuration;
	 }
	 
	/**
	 ** Returns an integer value from the given object, if it exists
	 ** @param the object containing the value
	 **/		 
	 public static int getIntValue(Object o) {
		 if (o != null) {
			if (o instanceof Integer) {
				return (Integer) o;
			}
		}
		return 0;
	 }
	 
	 public static double getDoubleValue(Object o) {
		 if (o != null) {
			if (o instanceof Double) {
				return (Double) o;
			}
			//Attempt integer value
			return getIntValue(o);
		}
		return 0;
	 }
	 
	
	public static int getMaximumMinecartSpeedPercent() {
		return getIntValue(getConfigurationValue("MaximumMinecartSpeedPercent"));
	}
	
	public static int getDefaultMinecartSpeedPercent() {
		return getIntValue(getConfigurationValue("DefaultMinecartSpeedPercent"));
	}
	
	public static int getMinecartsClearRailsSetting() {
		return getIntValue(getConfigurationValue("MinecartsClearRails"));
	}
	
	public static boolean isKeepMinecartsLoaded() {
		Object o = getConfigurationValue("KeepMinecartsLoaded");
		if (o != null) {
            return (Boolean)o;
		}
		return false;
	}
	
	public static boolean isMinecartsKillMobs() {
		Object o = getConfigurationValue("MinecartsKillMobs");
		if (o != null) {
            return (Boolean)o;
		}
		return true;
	}

	public static boolean isReturnMinecartToOwner() {
		Object o = getConfigurationValue("MinecartsReturnToOwner");
		if (o != null) {
            return (Boolean)o;
		}
		return true;
	}
	
	/**
	 ** Returns the block at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/	
	public static Block getBlockAt(World w, int x, int y, int z) {
		return w.getBlockAt(x, y, z);
	}
	
	/**
	 ** Returns the block type id at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/	
	public static int getBlockIdAt(World w, int x, int y, int z) {
		return w.getBlockTypeIdAt(x, y, z);
	}
	
	/**
	 ** Returns the block at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param new block type id
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static void setBlockAt(World w, int type, int x, int y, int z) {
		w.getBlockAt(x, y, z).setTypeId(type);
	}
	
	/**
	 ** Returns the block data at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static byte getBlockData(World w, int x, int y, int z) {
		return w.getBlockAt(x, y, z).getData();
	}
	
	/**
	 ** sets the block data at the given x, y, z coordinates
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param new data to set
	 **/
	public static void setBlockData(World w, int x, int y, int z, int data) {
		w.getBlockAt(x, y, z).setData((byte) (data));
	}
	
	/**
	 ** Returns true if the block at the given x, y, z coordinates is indirectly powered
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static boolean isBlockIndirectlyPowered(World w, int x, int y, int z) {
		return getBlockAt(w, x, y, z).isBlockIndirectlyPowered();
	}
	
	/**
	 ** Returns true if the block at the given x, y, z coordinates is directly powered
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 **/
	public static boolean isBlockPowered(World w, int x, int y, int z) {
		return getBlockAt(w, x, y, z).isBlockPowered();
	}
	
	/**
	 ** Sets the block at the given x, y, z coordinates to the given power state, if possible
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param power state
	 **/
	public static void setBlockPowered(World w, int x, int y, int z, boolean power) {
		MaterialData md = getBlockAt(w, x, y, z).getState().getData();
		int data = getBlockData(w, x, y, z);
		if (getBlockAt(w, x, y, z).getTypeId() == (Material.DIODE_BLOCK_OFF.getId()) && power) {
			setBlockAt(w, Material.DIODE_BLOCK_ON.getId(), x, y, z);
			setBlockData(w, x, y, z, (byte)data);
		}
		else if (getBlockAt(w, x, y, z).getTypeId() == (Material.DIODE_BLOCK_ON.getId()) && !power) {
			setBlockAt(w, Material.DIODE_BLOCK_OFF.getId(), x, y, z);
			setBlockData(w, x, y, z, (byte)data);
		}
		else if (md instanceof Lever || md instanceof Button) {
			setBlockData(w, x, y, z, ((byte)(power ? data | 0x8 : data & 0x7)));
		}
	}
	
	/**
	 ** Sets the block at the given x, y, z coordinates, as well as any block directly touch the given block to the given power state, if possible
	 ** @param w World to take effect in
	 ** @param x coordinate
	 ** @param y coordinate
	 ** @param z coordinate
	 ** @param power state
	 **/
	public static void setBlockIndirectlyPowered(World w, int x, int y, int z, boolean power) {
		setBlockPowered(w, x, y, z, power);
		setBlockPowered(w, x-1, y, z, power);
		setBlockPowered(w, x+1, y, z, power);
		setBlockPowered(w, x, y-1, z, power);
		setBlockPowered(w, x, y+1, z, power);
		setBlockPowered(w, x, y, z-1, power);
		setBlockPowered(w, x, y, z+1, power);
	}

	
}








