package com.Acrobot.iConomyChestShop.MinecartMania;

import com.Acrobot.iConomyChestShop.Basic;
import com.Acrobot.iConomyChestShop.ConfigManager;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MinecartManiaChest extends MinecartManiaSingleContainer implements MinecartManiaInventory {

    public static int SPAWN_DELAY = 1000;
    private long lastSpawnTime = -1;
    private final Location chest;
    private boolean redstonePower;
    private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<String, Object>();

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
        return (Chest) chest.getBlock().getState();
    }

    /**
     * Returns the neighbor chest to this chest, or null if none exists
     * @return the neighbor chest
     */
    public MinecartManiaChest getNeighborChest() {
        return getNeighborChest(chest.getWorld(), getX(), getY(), getZ());
    }

    public static boolean hasNeightborChest(Block block){
        return (getNeighborChest(block.getWorld(), block.getX(), block.getY(), block.getZ()) != null);
    }

    public static Block getNeightborChestBlock(Block block){
        return (hasNeightborChest(block) ? getNeighborChest(block.getWorld(), block.getX(), block.getY(), block.getZ()).getChest().getBlock() : null);
    }

    /**
     * Returns the neighbor chest to this chest, or null if none exists
     * @param w the world to search in
     * @param x coordinate to search
     * @param y coordinate to search
     * @param z coordinate to search
     * @return neighbor chest
     */
    public static MinecartManiaChest getNeighborChest(World w, int x, int y, int z) {
        if (MinecartManiaWorld.getBlockAt(w, x - 1, y, z).getTypeId() == Material.CHEST.getId()) {
            return MinecartManiaWorld.getMinecartManiaChest((Chest) MinecartManiaWorld.getBlockAt(w, x - 1, y, z).getState());
        }
        if (MinecartManiaWorld.getBlockAt(w, x + 1, y, z).getTypeId() == Material.CHEST.getId()) {
            return MinecartManiaWorld.getMinecartManiaChest((Chest) MinecartManiaWorld.getBlockAt(w, x + 1, y, z).getState());
        }
        if (MinecartManiaWorld.getBlockAt(w, x, y, z - 1).getTypeId() == Material.CHEST.getId()) {
            return MinecartManiaWorld.getMinecartManiaChest((Chest) MinecartManiaWorld.getBlockAt(w, x, y, z - 1).getState());
        }
        if (MinecartManiaWorld.getBlockAt(w, x, y, z + 1).getTypeId() == Material.CHEST.getId()) {
            return MinecartManiaWorld.getMinecartManiaChest((Chest) MinecartManiaWorld.getBlockAt(w, x, y, z + 1).getState());
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
        } else {
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
        if (item.getTypeId() == Material.AIR.getId()) {
            return false;
        }

        int max = (ConfigManager.getBoolean("stackUnstackableItems") ? 64 : item.getType().getMaxStackSize());

        //First attempt to merge the itemstack with existing item stacks that aren't full (< 64)
        for (int i = 0; i < size(); i++) {
            if (getItem(i) != null) {
                if (getItem(i).getTypeId() == item.getTypeId() && (getItem(i).getDurability() == item.getDurability() || getItem(i).getDurability() == -1)) {
                    if (getItem(i).getAmount() + item.getAmount() <= max) {
                        setItem(i, new ItemStack(item.getTypeId(), getItem(i).getAmount() + item.getAmount(), item.getDurability()));
                        return true;
                    } else {
                        int diff = getItem(i).getAmount() + item.getAmount() - max;
                        setItem(i, new ItemStack(item.getTypeId(), max, item.getDurability()));
                        item = new ItemStack(item.getTypeId(), diff, item.getDurability());
                    }
                }
            }
        }

        //Attempt to add the item to an empty slot
        int emptySlot = firstEmpty();
        int amount = item.getAmount();
        if (emptySlot > -1 && amount <= max) {
            //Basic.addItemToInventory(this, item, amount);
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
            } else {
                //reset flag
                setDataValue("neighbor", null);
            }
        }
        return false;
    }

    /**
     * Attempts to remove the specified amount of an item type from this chest. If it fails, it will not alter the chests previous contents.
     * @param type to remove
     * @param amount to remove
     * @param durability of the item to remove
     */
    public boolean removeItem(int type, int amount, short durability) {
        boolean checkDurability = true;
        if(ConfigManager.getBoolean("allowUsedItemsToBeSold") && type >= 256 && type <= 317){
            checkDurability = false;
        }
        for (int i = 0; i < size(); i++) {
            if (getItem(i) != null) {
                if (getItem(i).getTypeId() == type && (!checkDurability || durability == -1 || (getItem(i).getDurability() == durability))) {
                    if (getItem(i).getAmount() - amount > 0) {
                        setItem(i, new ItemStack(type, getItem(i).getAmount() - amount, durability));
                        update();
                        return true;
                    } else if (getItem(i).getAmount() - amount == 0) {
                        setItem(i, null);
                        update();
                        return true;
                    } else {
                        amount -= getItem(i).getAmount();
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
            } else {
                //reset flag
                setDataValue("neighbor", null);
            }
        }
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

    public boolean canSpawnMinecart() {
        if (lastSpawnTime == -1 || Math.abs(System.currentTimeMillis() - lastSpawnTime) > SPAWN_DELAY) {
            lastSpawnTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}