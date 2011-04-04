package com.Acrobot.iConomyChestShop.MinecartMania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

/**
 * An enum of all items accepted by the official server + client
 */
public enum Item {
    AIR(0),
    STONE(1),
    GRASS(2),
    DIRT(3),
    COBBLESTONE(4),
    WOOD(5),
    SAPLING(6),
    BEDROCK(7),
    WATER(8),
    STATIONARY_WATER(9),
    LAVA(10),
    STATIONARY_LAVA(11),
    SAND(12),
    GRAVEL(13),
    GOLD_ORE(14),
    IRON_ORE(15),
    COAL_ORE(16),
    LOG(17, 0),
    REDWOOD_LOG(17, 1),
    BIRCH_LOG(17, 2),
    LEAVES(18, 0),
    REDWOOD_LEAVES(18, 1),
    BIRCH_LEAVES(18, 2),
    SPONGE(19),
    GLASS(20),
    LAPIS_ORE(21),
    LAPIS_BLOCK(22),
    DISPENSER(23),
    SANDSTONE(24),
    NOTE_BLOCK(25),
    BED_BLOCK(26),
    WOOL(35, 0),
    ORANGE_WOOL(35, 1),
    MAGENTA_WOOL(35, 2),
    LIGHT_BLUE_WOOL(35, 3),
    YELLOW_WOOL(35, 4),
    LIGHT_GREEN_WOOL(35, 5),
    PINK_WOOL(35, 6),
    GRAY_WOOL(35, 7),
    LIGHT_GRAY_WOOL(35, 8),
    CYAN_WOOL(35, 9),
    PURPLE_WOOL(35, 10),
    BLUE_WOOL(35, 11),
    BROWN_WOOL(35, 12),
    DARK_GREEN_WOOL(35, 13),
    RED_WOOL(35, 14),
    BLACK_WOOL(35, 15),
    YELLOW_FLOWER(37),
    RED_ROSE(38),
    BROWN_MUSHROOM(39),
    RED_MUSHROOM(40),
    GOLD_BLOCK(41),
    IRON_BLOCK(42),
    STONE_DOUBLE_STEP(43, 0),
    SANDSTONE_DOUBLE_STEP(43, 1),
    WOODEN_DOUBLE_STEP(43, 2),
    COBBLESTONE_DOUBLE_STEP(43, 3),
    STONE_STEP(44, 0),
    SANDSTONE_STEP(44, 1),
    WOODEN_STEP(44, 2),
    COBBLESTONE_STEP(44, 3),
    BRICK(45),
    TNT(46),
    BOOKSHELF(47),
    MOSSY_COBBLESTONE(48),
    OBSIDIAN(49),
    TORCH(50),
    FIRE(51),
    MOB_SPAWNER(52),
    WOOD_STAIRS(53),
    CHEST(54),
    REDSTONE_WIRE(55),
    DIAMOND_ORE(56),
    DIAMOND_BLOCK(57),
    WORKBENCH(58),
    CROPS(59),
    SOIL(60),
    FURNACE(61),
    BURNING_FURNACE(62),
    SIGN_POST(63),
    WOODEN_DOOR(64),
    LADDER(65),
    RAILS(66),
    COBBLESTONE_STAIRS(67),
    WALL_SIGN(68),
    LEVER(69),
    STONE_PLATE(70),
    IRON_DOOR_BLOCK(71),
    WOOD_PLATE(72),
    REDSTONE_ORE(73),
    GLOWING_REDSTONE_ORE(74),
    REDSTONE_TORCH_OFF(75),
    REDSTONE_TORCH_ON(76),
    STONE_BUTTON(77),
    SNOW(78),
    ICE(79),
    SNOW_BLOCK(80),
    CACTUS(81),
    CLAY(82),
    SUGAR_CANE_BLOCK(83),
    JUKEBOX(84),
    FENCE(85),
    PUMPKIN(86),
    NETHERRACK(87),
    SOUL_SAND(88),
    GLOWSTONE(89),
    PORTAL(90),
    JACK_O_LANTERN(91),
    CAKE_BLOCK(92),
    DIODE_BLOCK_OFF(93),
    DIODE_BLOCK_ON(94),
    // ----- Item Separator -----
    IRON_SPADE(256),
    IRON_PICKAXE(257),
    IRON_AXE(258),
    FLINT_AND_STEEL(259),
    APPLE(260),
    BOW(261),
    ARROW(262),
    COAL(263, 0),
    CHARCOAL(263, 1),
    DIAMOND(264),
    IRON_INGOT(265),
    GOLD_INGOT(266),
    IRON_SWORD(267),
    WOOD_SWORD(268),
    WOOD_SPADE(269),
    WOOD_PICKAXE(270),
    WOOD_AXE(271),
    STONE_SWORD(272),
    STONE_SPADE(273),
    STONE_PICKAXE(274),
    STONE_AXE(275),
    DIAMOND_SWORD(276),
    DIAMOND_SPADE(277),
    DIAMOND_PICKAXE(278),
    DIAMOND_AXE(279),
    STICK(280),
    BOWL(281),
    MUSHROOM_SOUP(282),
    GOLD_SWORD(283),
    GOLD_SPADE(284),
    GOLD_PICKAXE(285),
    GOLD_AXE(286),
    STRING(287),
    FEATHER(288),
    SULPHUR(289),
    WOOD_HOE(290),
    STONE_HOE(291),
    IRON_HOE(292),
    DIAMOND_HOE(293),
    GOLD_HOE(294),
    SEEDS(295),
    WHEAT(296),
    BREAD(297),
    LEATHER_HELMET(298),
    LEATHER_CHESTPLATE(299),
    LEATHER_LEGGINGS(300),
    LEATHER_BOOTS(301),
    CHAINMAIL_HELMET(302),
    CHAINMAIL_CHESTPLATE(303),
    CHAINMAIL_LEGGINGS(304),
    CHAINMAIL_BOOTS(305),
    IRON_HELMET(306),
    IRON_CHESTPLATE(307),
    IRON_LEGGINGS(308),
    IRON_BOOTS(309),
    DIAMOND_HELMET(310),
    DIAMOND_CHESTPLATE(311),
    DIAMOND_LEGGINGS(312),
    DIAMOND_BOOTS(313),
    GOLD_HELMET(314),
    GOLD_CHESTPLATE(315),
    GOLD_LEGGINGS(316),
    GOLD_BOOTS(317),
    FLINT(318),
    PORK(319),
    GRILLED_PORK(320),
    PAINTING(321),
    GOLDEN_APPLE(322),
    SIGN(323),
    WOOD_DOOR(324),
    BUCKET(325),
    WATER_BUCKET(326),
    LAVA_BUCKET(327),
    MINECART(328),
    SADDLE(329),
    IRON_DOOR(330),
    REDSTONE(331),
    SNOW_BALL(332),
    BOAT(333),
    LEATHER(334),
    MILK_BUCKET(335),
    CLAY_BRICK(336),
    CLAY_BALL(337),
    SUGAR_CANE(338),
    PAPER(339),
    BOOK(340),
    SLIME_BALL(341),
    STORAGE_MINECART(342),
    POWERED_MINECART(343),
    EGG(344),
    COMPASS(345),
    FISHING_ROD(346),
    WATCH(347),
    GLOWSTONE_DUST(348),
    RAW_FISH(349),
    COOKED_FISH(350),
    INK_SACK(351, 0),
    ROSE_RED(351, 1),
    CACTUS_GREEN(351, 2),
    COCOA_BEANS(351, 3),
    LAPIS_LAZULI(351, 4),
    PURPLE_DYE(351, 5),
    CYAN_DYE(351, 6),
    LIGHT_GRAY_DYE(351, 7),
    GRAY_DYE(351, 8),
    PINK_DYE(351, 9),
    LIME_DYE(351, 10),
    DANDELION_YELLOW(351, 11),
    LIGHT_BLUE_DYE(351, 12),
    MAGENTA_DYE(351, 13),
    ORANGE_DYE(351, 14),
    BONEMEAL(351, 15),
    BONE(352),
    SUGAR(353),
    CAKE(354),
    BED(355),
    DIODE(356),
    GOLD_RECORD(2256),
    GREEN_RECORD(2257);

    private final int id;
    private final short data;
    private boolean hasData;
    private int amount = -1;
    private static final Map<ArrayList<Integer>, Item> lookupId = new HashMap<ArrayList<Integer>, Item>();
    private static final Map<String, Item> lookupName = new HashMap<String, Item>();

    private Item(final int id) {
        this(id, 0);
        hasData = false;
    }

    private Item(final int id, final int data) {
        this.id = id;
        this.data = (short)data;
        hasData = true;
    }

    /**
     * Gets the item ID or block ID of this Item
     *
     * @return ID of this Item
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the data for this Item
     *
     * @return Data for this Item
     */
    public int getData() {
        return data;
    }

    /**
     * Checks if this Item is a placable block
     *
     * @return true if this Item is a block
     */
    public boolean isBlock() {
        return id < 256;
    }
    
    /**
     * Checks to see if this Item has more than one Item using the same id
     *
     * @return true if this Item has more than one Item using the same id
     */
    public boolean hasData() {
    	return hasData;
    }
    
    public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
	
	public boolean isInfinite() {
		return amount == -1;
	}
    
    /**
     * Finds the bukkit material associated with this item id, and returns it
     * @return the material if found, or null
     */
    public Material toMaterial() {
    	return Material.getMaterial(id);
    }
    
    public boolean equals(Item i) {
    	return i != null && i.getId() == id && data == i.getData();
    }
    
    public boolean equals(Material m) {
    	return m != null && m.getId() == id;
    }
    
    public boolean equals(int id, short data) {
    	return id == this.id && data == this.data;
    }
    
    public boolean equals(int id) {
    	return id == this.id;
    }

    /**
     * Attempts to get the Item with the given ID and data value
     *
     * @param id ID of the Item to get
     * @param the data value of the Item to get
     * @return Item if found, or null
     */
    public static Item getItem(final int id, int data) {
    	ArrayList<Integer> a = new ArrayList<Integer>(2);
    	a.add(id);
    	a.add(data);
    	return lookupId.get(a);
    }
    
    /**
     * Attempts to get the list of items with the given id
     *
     * @param id ID of the list of Items to get
     * @return Items if found, or empty arraylist
     */
    public static ArrayList<Item> getItem(final int id) {
    	ArrayList<Item> list = new ArrayList<Item>();
    	for (int i = 0; i < 16; i++){
    		Item temp = getItem(id, i);
    		if (temp != null) {
    			list.add(temp);
    		}
    	}
    	return list;
    }

    /**
     * Attempts to get the Item with the given name.
     * This is a normal lookup, names must be the precise name they are given
     * in the enum.
     *
     * @param name Name of the Item to get
     * @return Item if found, or null
     */
    public static Item getItem(final String name) {
        return lookupName.get(name);
    }
    
    /**
     *  Finds the item associated with the given bukkit material, and returns it
     *  @return the Item if found, or null
     */
    public static Item materialToItem(Material m) {
    	ArrayList<Integer> a = new ArrayList<Integer>(2);
    	a.add(m.getId());
    	a.add(0);
    	return lookupId.get(a);
    }

	static {
        for (Item i : values()) {
        	ArrayList<Integer> a = new ArrayList<Integer>(2);
        	a.add(i.getId());
        	a.add(i.getData());
            lookupId.put(a, i);
            lookupName.put(i.name(), i);
        }
    }
}