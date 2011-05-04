package com.Acrobot.iConomyChestShop.MinecartMania;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface MinecartManiaInventory {
	
	public boolean contains(Material m);
	
	public boolean contains(ItemStack i);
	
	public boolean contains(int type);
	
	public boolean contains(int type, short durability);
	
	public int amount(ItemStack item);
	
	public boolean canAddItem(ItemStack item);
	
	public boolean addItem(ItemStack item);
	
	public boolean addItem(int type);
	
	public boolean addItem(int type, int amount);
	
	public boolean canRemoveItem(int type, int amount, short durability);
	
	public boolean removeItem(int type, int amount, short durability);
	
	public boolean removeItem(int type, int amount);
	
	public boolean removeItem(int type);
	
	public ItemStack getItem(int slot);
	
	public void setItem(int slot, ItemStack item);
	
	public int firstEmpty();
	
	public int size();
	
	public int first(Material m);
	
	public int first(ItemStack i);
	
	public int first(int type);
	
	public boolean isEmpty();
	
	//Just is just a kindness, but not nessecary.
	//public Inventory getInventory();
	
	public ItemStack[] getContents();
	
	public void setContents(ItemStack[] contents);
}
