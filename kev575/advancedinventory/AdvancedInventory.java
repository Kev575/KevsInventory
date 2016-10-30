package kev575.advancedinventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import kev575.advancedinventory.events.AdvancedInventoryClickEvent;
import kev575.advancedinventory.events.AdvancedInventoryCloseEvent;
import kev575.advancedinventory.events.AdvancedInventoryListener;
import kev575.advancedinventory.events.AdvancedInventoryOpenEvent;

/**
 * This could be a milestone in the creation of graphical user interfaces (GUIs)
 * using the Bukkit inventory API ({@link Inventory}, {@link InventoryEvent} and
 * more).
 * 
 * @author Kev575
 */
public final class AdvancedInventory {

	private final Player player;
	private final AdvancedInventoryType type;
	private String title = "Chest";
	private final ArrayList<AdvancedInventoryListener> listeners = new ArrayList<>();
	private final HashMap<ItemPosition, ItemStack> items = new HashMap<>();
	private Inventory inv;
	private Listener l;

	/**
	 * The default constructor
	 * 
	 * @param player
	 *            a {@link Player} of your choice (NotNull!)
	 * @param type
	 *            a {@link AdvancedInventoryType} of your choice
	 * @param plugin
	 *            the {@link Plugin}
	 */
	public AdvancedInventory(Plugin plugin, Player player, AdvancedInventoryType type) {
		Validate.notNull(player, "The Player can't be null.");
		Validate.notNull(type, "The AdvancedInventoryType can't be null.");
		Validate.notNull(plugin, "The Plugin can't be null.");
		this.player = player;
		this.type = type;
		Bukkit.getPluginManager().registerEvents(l, plugin);
		l = new Listener() {

			@EventHandler
			public void onClick(InventoryClickEvent e) {
				if (inv == null)
					return;
				if (!e.getWhoClicked().getUniqueId().equals(getPlayer().getUniqueId()))
					return;
				if (e.getWhoClicked().getOpenInventory().getTopInventory() == null
						|| !e.getWhoClicked().getOpenInventory().getTopInventory().equals(toInventory()))
					return;
				AdvancedInventoryClickEvent event = new AdvancedInventoryClickEvent(getPlayer(), e.getCurrentItem(),
						e.getSlot(), toItemPosition(e.getSlot()).getRow(), toItemPosition(e.getSlot()).getColumn(),
						e.getAction());
				for (AdvancedInventoryListener l : listeners) {
					l.onClick(event);
				}
				e.setCancelled(event.isCancelled());
			}

			@EventHandler
			public void onClose(InventoryCloseEvent e) {
				if (inv == null)
					return;
				if (!e.getPlayer().getUniqueId().equals(getPlayer().getUniqueId()))
					return;
				for (AdvancedInventoryListener l : listeners) {
					l.onClose(new AdvancedInventoryCloseEvent(getPlayer()));
				}
			}

			@EventHandler
			public void onPlayerQuit(PlayerQuitEvent e) {
				if (inv == null)
					return;
				if (!e.getPlayer().getUniqueId().equals(getPlayer().getUniqueId()))
					return;
				destroy();
			}
		};
	}

	/**
	 * Get the player
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Get the {@link AdvancedInventoryType}
	 * 
	 * @return the type
	 */
	public AdvancedInventoryType getType() {
		return type;
	}

	/**
	 * Get the title<br>
	 * initial value: "Chest"
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		Validate.notNull(title, "title argument can not be null");
		this.title = title;
	}

	/**
	 * @return the listeners added in
	 */
	public ArrayList<AdvancedInventoryListener> getListeners() {
		return listeners;
	}

	/**
	 * @param listener
	 *            the {@link AdvancedInventoryListener}
	 */
	public void addListener(AdvancedInventoryListener listener) {
		listeners.add(listener);
	}

	/**
	 * Opens the inventory
	 */
	public void open() {
		AdvancedInventoryOpenEvent e = new AdvancedInventoryOpenEvent(getPlayer());
		for (AdvancedInventoryListener l : listeners) {
			l.onOpen(e);
		}
		if (e.isCancelled())
			return;
		player.openInventory(toInventory());
	}

	/**
	 * @param row
	 *            the row
	 * @param column
	 *            the column
	 * @param item
	 * @return true when done
	 */
	public boolean setItem(int row, int column, ItemStack item) {
		if (row > type.getRows())
			return false;
		if (column > type.getColumns())
			return false;
		if (row < 0 || column < 0)
			return false;
		if (items.containsKey(new ItemPosition(row, column)))
			return false;
		items.put(new ItemPosition(row, column), item);
		return true;
	}

	/**
	 * @param position
	 *            the position
	 * @param item
	 *            the item
	 * @return true when done
	 */
	public boolean setItem(ItemPosition position, ItemStack item) {
		if (position == null)
			return false;
		return setItem(position.getRow(), position.getColumn(), item);
	}

	/**
	 * 
	 * @param slot
	 * @param item
	 * @return true when done
	 */
	public boolean setItem(int slot, ItemStack item) {
		if (slot < 0)
			return false;
		items.put(toItemPosition(slot), item);
		return true;
	}

	/**
	 * @param row
	 *            the row
	 * @param column
	 *            the column
	 * @return {@link ItemStack}; null when not exists
	 */
	public ItemStack getItem(int row, int column) {
		return items.get(new ItemPosition(row, column));
	}

	/**
	 * @param position
	 *            the item position
	 * @return row and column to position in the bukkit inventory
	 */
	public int toInventoryInteger(ItemPosition position) {
		return (position.getRow() * 9) + position.getColumn();
	}

	/**
	 * 
	 * @param invint
	 *            inventory integer
	 *            {@link AdvancedInventory#toInventoryInteger(ItemPosition)}
	 * @return {@link ItemPosition} row and column; null when
	 *         <code>invint < 0</code>
	 */
	public ItemPosition toItemPosition(int invint) {
		if (invint < 0) {
			return null;
		}
		int row = 0;
		int column;
		while (invint >= 9) {
			row++;
			invint = invint - 9;
		}
		column = invint;
		return new ItemPosition(row, column);
	}

	/**
	 * @return the created inventory
	 */
	public Inventory toInventory() {
		if (inv == null) {
			if (type.toBukkitType() != null)
				inv = Bukkit.createInventory(null, type.toBukkitType(), title);
			else
				inv = Bukkit.createInventory(null, type.getRows() * 9, title);
		}
		for (Entry<ItemPosition, ItemStack> current : items.entrySet()) {
			inv.setItem(toInventoryInteger(current.getKey()), current.getValue());
		}
		return inv;
	}

	/**
	 * Destroys the inventory
	 */
	public void destroy() {
		HandlerList.unregisterAll(l);
		inv = null;
		title = null;
		listeners.clear();
		items.clear();
	}
}
