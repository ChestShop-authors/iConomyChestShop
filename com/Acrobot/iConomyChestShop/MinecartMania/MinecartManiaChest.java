package com.Acrobot.iConomyChestShop.MinecartMania;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartManiaChest extends MinecartManiaSingleContainer implements MinecartManiaInventory{

	private final Location chest;
	private boolean redstonePower;
	private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String,Object>();
	public MinecartManiaChest(Chest chest) {
		super(chest.getInventory());
		this.chest = chest.getBlock().getLocation().clone();
		setRedstonePower(MinecartManiaWorld.isBlockIndirectlyPowered(chest.getWorld(), getX(), getY(), getZ()));
	}
	
	public int getX() {
		return this.chest.getBlockX();
	}
	
	public int getY() {
		return this.chest.getBlockY();
	}
	
	public int getZ() {
		return this.chest.getBlockZ();
	}
	
	public World getWorld() {
		return this.chest.getWorld();
	}
	
	public Location getLocation() {
		return chest;
	}
	
	public Chest getChest() {
		return (Chest)chest.getBlock().getState();
	}
	
	
	/**
	 * Returns the neighbor chest to this chest, or null if none exists
	 * @return the neighbor chest
	 */
	public MinecartManiaChest getNeighborChest() {
		return getNeighborChest(chest.getWorld(), getX(), getY(), getZ());
	}
	
	/**
	 * Returns the neighbor chest to this chest, or null if none exists
	 * @param w the world to search in
	 * @param x coordinate to search
	 * @param y coordinate to search
	 * @param z coordinate to search
	 */
	 public static MinecartManiaChest getNeighborChest(World w, int x, int y, int z)
	 {
    	if (MinecartManiaWorld.getBlockAt(w, x - 1, y, z).getTypeId() == Item.CHEST.getId()) {
            return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(w, x - 1, y, z).getState());
        }
        if(MinecartManiaWorld.getBlockAt(w, x + 1, y, z).getTypeId() == Item.CHEST.getId()) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(w, x + 1, y, z).getState());
        }
        if(MinecartManiaWorld.getBlockAt(w, x, y, z - 1).getTypeId() == Item.CHEST.getId()) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(w, x, y, z - 1).getState());
        }
        if (MinecartManiaWorld.getBlockAt(w, x, y, z + 1).getTypeId() == Item.CHEST.getId()) {
        	return MinecartManiaWorld.getMinecartManiaChest((Chest)MinecartManiaWorld.getBlockAt(w, x, y, z + 1).getState());
		}

		return null;
    }
	 
	/**
	 * Returns the value from the loaded data
	 * @param key the string key the data value is associated with
	 * @return the object stored by the key
	 */
	 public Object getDataValue(String key) {
		 if (data.containsKey(key)) {
			 return data.get(key);
		 }
		 return null;
	 }
	 
	/**
	 ** Creates a new data value if it does not already exists, or resets an existing value
	 ** @param key the data value is associated with
	 ** @param value to store
	 **/	 
	 public void setDataValue(String key, Object value) {
		 if (value == null) {
			 data.remove(key);
		 }else {
			 data.put(key, value);
		 }
	 }
	
	/**
	 * Attempts to add an itemstack to this chest. It adds items in a 'smart' manner, merging with existing itemstacks, until they
	 * reach the maximum size (64). If it fails, it will not alter the chest's previous contents.
	 * @param item to add
	 */
	public boolean addItem(ItemStack item) {
		if (item == null) {
			return true;
		}
		//WTF is it with air
		if (item.getTypeId() == Item.AIR.getId()) {
			return false;
		}
		//Backup contents
		ItemStack[] backup = getContents().clone();
		ItemStack backupItem = new ItemStack(item.getTypeId(), item.getAmount(), item.getDurability());
		
		//First attempt to merge the itemstack with existing item stacks that aren't full (< 64)
		for (int i = 0; i < size(); i++) {
			if (getItem(i) != null) {
				if (getItem(i).getTypeId() == item.getTypeId() && getItem(i).getDurability() == item.getDurability()) {
					if (getItem(i).getAmount() + item.getAmount() <= 64) {
						setItem(i, new ItemStack(item.getTypeId(), getItem(i).getAmount() + item.getAmount(), item.getDurability()));
						return true;
					}
					else {
						int diff = getItem(i).getAmount() + item.getAmount() - 64;
						setItem(i, new ItemStack(item.getTypeId(), getItem(i).getAmount() + item.getAmount(), item.getDurability()));
						item = new ItemStack(item.getTypeId(), diff, item.getDurability());
					}
				}
			}
		}
		
		//Attempt to add the item to an empty slot
		int emptySlot = firstEmpty();
		if (emptySlot > -1) {
			setItem(emptySlot, item);
			update();
			return true;
		}
		
		
		//Try to merge the itemstack with the neighbor chest, if we have one
		MinecartManiaChest neighbor = getNeighborChest();
		if (neighbor != null) {
			//flag to prevent infinite recursion
			if (getDataValue("neighbor") == null) {
				neighbor.setDataValue("neighbor", Boolean.TRUE);
				if (getNeighborChest().addItem(item)) {
					update();
					return true;
				}
			}
			else {
				//reset flag
				setDataValue("neighbor", null);
			}
		}
			
		//if we fail, reset the inventory and item back to previous values
		getChest().getInventory().setContents(backup);
		item = backupItem;
		return false;
	}
	
	
	/**
	 * Attempts to remove the specified amount of an item type from this chest. If it fails, it will not alter the chests previous contents.
	 * @param type to remove
	 * @param amount to remove
	 * @param durability of the item to remove
	 */
	public boolean removeItem(int type, int amount, short durability) {
		//Backup contents
		ItemStack[] backup = getContents().clone();
		
		for (int i = 0; i < size(); i++) {
			if (getItem(i) != null) {
				if (getItem(i).getTypeId() == type && (durability == -1 || (getItem(i).getDurability() == durability))) {
					if (getItem(i).getAmount() - amount > 0) {
						setItem(i, new ItemStack(type, getItem(i).getAmount() - amount, durability));
						update();
						return true;
					}
					else if (getItem(i).getAmount() - amount == 0) {
						setItem(i, null);
						update();
						return true;
					}
					else{
						amount -=  getItem(i).getAmount();
						setItem(i, null);
					}
				}
			}
		}
		
		MinecartManiaChest neighbor = getNeighborChest();
		if (neighbor != null) {
			//flag to prevent infinite recursion
			if (getDataValue("neighbor") == null) {
				neighbor.setDataValue("neighbor", Boolean.TRUE);
				if (neighbor.removeItem(type, amount)) {
					update();
					return true;
				}
			}
			else {
				//reset flag
				setDataValue("neighbor", null);
			}
		}
		
			
		//if we fail, reset the inventory back to previous values
		getChest().getInventory().setContents(backup);
		return false;
	}

	public void setRedstonePower(boolean redstonePower) {
		this.redstonePower = redstonePower;
	}

	public boolean isRedstonePower() {
		return redstonePower;
	}
	
	public void update() {
		 getChest().update();
	}
	
	public String toString() {
		return "[" + getX() + ":" + getY() + ":" + getZ() + "]";
	}

	public Inventory getInventory() {
		return getChest().getInventory();
	}
}
