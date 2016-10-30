package kev575.advancedinventory;

import org.bukkit.event.inventory.InventoryType;

/**
 * The type required in
 * {@link AdvancedInventory#AdvancedInventory(org.bukkit.plugin.Plugin, org.bukkit.entity.Player, AdvancedInventoryType)}
 * 
 * @author Kev575
 */
public class AdvancedInventoryType {
	/**
	 * A default chest with 3 rows and 9 columns
	 * 
	 * @see InventoryType#CHEST
	 */
	public static final AdvancedInventoryType CHEST = new AdvancedInventoryType(InventoryType.CHEST, 3, 9);
	/**
	 * A big chest with 6 rows and 9 columns
	 */
	public static final AdvancedInventoryType BIGCHEST = new AdvancedInventoryType(null, 6, 9);
	/**
	 * A hopper with 1 row and 5 columns
	 * 
	 * @see InventoryType#HOPPER
	 */
	public static final AdvancedInventoryType SMALL = new AdvancedInventoryType(InventoryType.HOPPER, 1, 5);

	private final InventoryType t;
	private final int r, c;

	/**
	 * default constructor
	 */
	private AdvancedInventoryType(InventoryType type, int rows, int columns) {
		t = type;
		r = rows;
		c = columns;
	}

	/**
	 * The {@link InventoryType} provided by {@link AdvancedInventoryType#SMALL}
	 * and{@link AdvancedInventoryType#CHEST}
	 * 
	 * @return bukkit's {@link InventoryType}
	 */
	public InventoryType toBukkitType() {
		return t;
	}

	/**
	 * 1 means hoper;<br>
	 * 3 means chest;<br>
	 * 6 means double chest;
	 * 
	 * @return the row count
	 */
	public int getRows() {
		return r;
	}

	/**
	 * 9 columns means chest;<br>
	 * 5 columns means hopper
	 * 
	 * @return the column count
	 */
	public int getColumns() {
		return c;
	}

	/**
	 * Create a custom {@link AdvancedInventoryType}
	 * 
	 * @param columns
	 *            (1 to {@link Integer#MAX_VALUE}, but that's useless; good
	 *            maximum is 8)
	 * @return the created {@link AdvancedInventoryType}
	 */
	public static AdvancedInventoryType createType(int columns) {
		return new AdvancedInventoryType(null, 9, columns);
	}

}
