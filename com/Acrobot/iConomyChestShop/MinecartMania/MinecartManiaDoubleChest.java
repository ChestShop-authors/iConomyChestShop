package com.Acrobot.iConomyChestShop.MinecartMania;

import com.Acrobot.iConomyChestShop.Basic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A temporary object that allows for easy use of accessing and altering the contents of a double chest. 
 * References are not safe, do not store them for later retrieval. Create them only as needed.
 * @author Afforess
 */
public class MinecartManiaDoubleChest implements MinecartManiaInventory{

	private final MinecartManiaChest chest1;
	private final MinecartManiaChest chest2;
	public MinecartManiaDoubleChest(MinecartManiaChest left, MinecartManiaChest right) {
		this.chest1 = left;
		this.chest2 = right;
	}
	
	public boolean equals(Location loc) {
        return loc.equals(chest1.getLocation()) || loc.equals(chest2.getLocation());
    }
	
	public boolean canAddItem(ItemStack item) {
		return chest1.canAddItem(item) || chest2.canAddItem(item);
	}
	
	/**
	 * Attempts to add an itemstack to this storage minecart. It adds items in a 'smart' manner, merging with existing itemstacks, until they
	 * reach the maximum size (64). If it fails, it will not alter the storage minecart's previous contents.
	 * @param item to add
	 * @return true if the item was successfully added
	 */
	public boolean addItem(ItemStack item) {
		return chest1.addItem(item);
	}

	/**
	 ** Attempts to add a single item of the given type to this storage minecart. If it fails, it will not alter the storage minecart's previous contents
	 ** @param type to add
	 **/
	public boolean addItem(int type) {
		return chest1.addItem(type);
	}

	/**
	 ** Attempts to add a given amount of a given type to this storage minecart. If it fails, it will not alter the storage minecart's previous contents
	 ** @param type to add
	 ** @param amount to add
	 **/
	public boolean addItem(int type, int amount) {
		return chest1.addItem(type, amount);
	}
	
	public boolean canRemoveItem(int type, int amount, short durability) {
		return chest1.canRemoveItem(type, amount, durability) || chest2.canRemoveItem(type, amount, durability);
	}
	
	/**
	 * Attempts to remove the specified amount of an item type from this storage minecart. If it fails, it will not alter the storage minecart's previous contents
	 * @param type to remove
	 * @param amount to remove
	 * @param durability of the item to remove
	 * @return true if the items were successfully removed
	 */
	public boolean removeItem(int type, int amount, short durability) {
		return chest1.removeItem(type, amount, durability);
	}
	
	/**
	 * Attempts to remove the specified amount of an item type from this storage minecart. If it fails, it will not alter the storage minecart's previous contents
	 * @param type to remove
	 * @param amount to remove
	 * @return true if the items were successfully removed
	 */
	public boolean removeItem(int type, int amount) {
		return chest1.removeItem(type, amount);
	}
	
	/**
	 * Attempts to remove a single item type from this storage minecart. If it fails, it will not alter the storage minecart previous contents
	 * @param type to remove
	 * @return true if the item was successfully removed
	 */
	public boolean removeItem(int type) {
		return chest1.removeItem(type);
	}

	/**
	 * Gets the itemstack at the given slot, or null if empty
	 * @param the slot to get
	 * @return the itemstack at the given slot
	 */
	public ItemStack getItem(int slot) {
		if (slot < chest1.size()) {
			return chest1.getItem(slot);
		}
		return chest2.getItem(slot-chest1.size());
	}

	/**
	 * Sets the given slot to the given itemstack. If the itemstack is null, the slot's contents will be cleared.
	 * @param slot to set.
	 * @param item to set in the slot
	 */
	public void setItem(int slot, ItemStack item) {
		if (slot < chest1.size()) {
			chest1.setItem(slot, item);
		}else {
			chest2.setItem(slot-chest1.size(), item);
		}
	}

	/**
	 * Get's the first empty slot in this storage minecart
	 * @return the first empty slot in this storage minecart
	 */
	public int firstEmpty() {
		if (chest1.firstEmpty() != -1) {
			return chest1.firstEmpty();
		}
		if (chest2.firstEmpty() != -1) {
			return chest2.firstEmpty()+chest1.size();
		}
		return -1;
	}

	/**
	 * Gets the size of the inventory of this storage minecart
	 * @return the size of the inventory
	 */
	public int size() {
		return chest1.size()+chest2.size();
	}

	/**
	 * Gets an array of the Itemstack's inside this storage minecart. Empty slots are represented by air stacks
	 * @return the contents of this inventory
	 */
	public ItemStack[] getContents() {
		ItemStack[] contents = new ItemStack[size()];
		for (int i = 0; i < chest1.size(); i++) {
			contents[i] = chest1.getItem(i);
		}
		for (int i = 0; i < chest2.size(); i++) {
			contents[i+chest1.size()] = chest2.getItem(i);
		}
		return contents;
	}
	
	/**
	 * Set's the contents of this inventory with an array of items.
	 * @param contents to set as the inventory
	 */
	public void setContents(ItemStack[] contents) {
		ItemStack[] side1 = new ItemStack[chest1.size()];
		ItemStack[] side2 = new ItemStack[chest2.size()];
		for (int i = 0; i < contents.length; i++) {
			if (i < side1.length) {
				side1[i] = contents[i];
			}
			else {
				side2[i-side1.length] = contents[i];
			}
		}
		chest1.setContents(side1);
		chest2.setContents(side2);
	}
	
	/**
	 * Get's the first slot containing the given material, or -1 if none contain it
	 * @param material to search for
	 * @return the first slot with the given material
	 */
	public int first(Material material) {
		return first(material.getId(), (short) -1);
	}
	
	/**
	 * Get's the first slot containing the given item, or -1 if none contain it
	 * @param item to search for
	 * @return the first slot with the given item
	 */
	public int first(ItemStack item) {
		return first(item.getTypeId(), item.getDurability() > 0 ? item.getDurability() : -1);
	}

	/**
	 * Get's the first slot containing the given type id, or -1 if none contain it
	 * @param type id to search for
	 * @return the first slot with the given item
	 */
	public int first(int type) {
		return first(type, (short)-1);
	}
	
	/**
	 * Get's the first slot containing the given type id and matching durability, or -1 if none contain it.
	 * If the durability is -1, it get's the first slot with the matching type id, and ignores durability
	 * @param type id to search for
	 * @param durability of the type id to search for
	 * @return the first slot with the given type id and durability
	 */
	public int first(int type, short durability) {
		if (chest1.first(type, durability) != -1) {
			return chest1.first(type);
		}
		if (chest2.first(type, durability) != -1) {
			return chest2.first(type)+chest1.size();
		}
		return -1;
	}

	/**
	 * Searches the inventory for any items
	 * @return true if the inventory contains no items
	 */
	public boolean isEmpty() {
		for (ItemStack i : getContents()) {
			//I hate you too, air.
			if (i != null && i.getType() != Material.AIR) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Searches the inventory for any items that match the given Material
	 * @param material to search for
	 * @return true if the material is found
	 */
	public boolean contains(Material material) {
		return (chest1.contains(material) || chest2.contains(material));
	}
	
	/**
	 * Searches the inventory for any items that match the given Item
	 * @param item to search for
	 * @return true if the Item is found
	 */
	public boolean contains(ItemStack item) {
		return (chest1.contains(item) || chest2.contains(item));
	}

	/**
	 * Searches the inventory for any items that match the given type id
	 * @param type id to search for
	 * @return true if an item matching the type id is found
	 */
	public boolean contains(int type) {
		return chest1.contains(type) || chest2.contains(type);
	}
	
	/**
	 * Searches the inventory for any items that match the given type id and durability
	 * @param type id to search for
	 * @param durability to search for
	 * @return true if an item matching the type id and durability is found
	 */
	public boolean contains(int type, short durability) {
		return chest1.contains(type, durability) || chest2.contains(type, durability);
	}

    public int amount(ItemStack item) {
        return Basic.getItemAmountFromInventory(chest1.getInventory(), item) + Basic.getItemAmountFromInventory(chest2.getInventory(), item);
    }

}
