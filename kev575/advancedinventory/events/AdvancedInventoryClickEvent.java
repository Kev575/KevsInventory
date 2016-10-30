package kev575.advancedinventory.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

/**
 * The Event called when an inventory click happens
 * 
 * @author Kev575
 */
public class AdvancedInventoryClickEvent extends Cancellable {

	private final Player player;
	private final ItemStack item;
	private final int slot, row, column;
	private final InventoryAction action;

	/**
	 * @param p
	 *            the player
	 * @param item
	 *            the item
	 * @param slot
	 *            the slot
	 * @param row
	 *            the row
	 * @param column
	 *            the column
	 * @param action
	 *            the action
	 * 
	 */
	public AdvancedInventoryClickEvent(Player p, ItemStack item, int slot, int row, int column,
			InventoryAction action) {
		this.player = p;
		this.item = item;
		this.slot = slot;
		this.row = row;
		this.column = column;
		this.action = action;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the item
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the action
	 */
	public InventoryAction getAction() {
		return action;
	}
}
