// add your own package her!

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * @author Kev575
 * @version 1.0a
 */
@SuppressWarnings("unused")
public class KevsInventory {

	/**
	 * The event used in {@link KevsInventory}'s constructor
	 */
    public class KevInvClickEvent {
        private int slot;

        private ItemStack item;
        private Player player;
        private boolean close = true;
        private boolean destroy = true;
        
        
        /**
         * Creates a "better" InventoryClickEvent<br>
         * <u>Not usable for average developers</u>
         * @param p the Player
         * @param slot the slot the click was performed
         * @param itemStack the ItemStack clicked <b>Can be null!</b>
         */
        public KevInvClickEvent(Player p, int slot, ItemStack itemStack) {
            this.slot = slot;
            this.item = itemStack;
            this.player = p;
        }
        
        /**
		 * @return the player specified in the constructor
		 */
        public Player getPlayer() {
			return player;
		}
        
        /**
		 * @return {@link Integer}<br>
		 *         the slot specified in the constructor
		 */
        public int getSlot() {
            return slot;
        }

		/**
		 * @return {@link ItemStack}<br>
		 *         the item specified in the constructor
		 */
        public ItemStack getItem() {
            return item;
        }

		/**
		 * @default false
		 * @return {@link Boolean}<br>
		 *         if the inventory will close after clicking
		 */
        public boolean getWillClose() {
            return close;
        }

		/**
		 * 
		 * @param close
		 *            {@link Boolean}<br>
		 *            if the inventory will close after clicking
		 */
        public void setWillClose(boolean close) {
            this.close = close;
        }

		/**
		 * @default {@link Boolean}: false
		 * @return if the inventory will be destroyed after clicking
		 */
        public boolean getWillDestroy() {
            return destroy;
        }

		/**
		 * 
		 * @param destroy
		 *            {@link Boolean}<br>
		 *            if the inventory will be destroyed after clicking
		 */
        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

	/**
	 * interface to execute later in {@link KevsInventory}
	 */
    public interface KevInvClickEventHandler {
		/**
		 * @param event
		 *            {@link KevInvClickEvent}
		 */
        void onInventoryClick(KevInvClickEvent event);
    }

    private Player player;

	private KevInvClickEventHandler handler;

    private Inventory inv;

    private Listener listener;

	/**
	 * 
	 * @param plugin
	 *            the {@link Plugin}<br>
	 *            <b>Not Null!</b><br>
	 * @param player
	 *            the {@link Player} to get this opened<br>
	 *            <b>Not Null!</b><br>
	 * @param handler
	 *            the {@link KevInvClickEventHandler} executed on click<br>
	 *            <b>Not Null!</b><br>
	 */
    public KevsInventory(Plugin plugin, Player player, final KevInvClickEventHandler handler) {
    	Validate.notNull(plugin, "Plugin can't be null");
    	Validate.notNull(player, "Player can't be null");
    	Validate.notNull(handler, "KevInvClickEventHandler can't be null");
        this.player = player;
        this.handler = handler;

        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {
                    Player clicker = (Player) event.getWhoClicked();

                    if (event.getInventory().equals(inv)) {
                        event.setCancelled(true);
                        
                        KevInvClickEvent clickEvent = new KevInvClickEvent(clicker, event.getRawSlot(), event.getCurrentItem());
                        
                        handler.onInventoryClick(clickEvent);

                        if (clickEvent.getWillClose()) {
                            event.getWhoClicked().closeInventory();
                        }

                        if (clickEvent.getWillDestroy()) {
                            destroy();
                        }
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Inventory inv = event.getInventory();

                    if (inv.equals(KevsInventory.this.inv)) {
                        inv.clear();
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(getPlayer())) {
                    destroy();
                }
            }
        };
		// registering event to PluginManager - removing in
		// KevsInventory#destroy
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

	/**
	 * @return {@link Player}
	 */
    public Player getPlayer() {
        return player;
    }

	/**
	 * Open the inventory to the {@link Player} (get at
	 * {@link KevsInventory#getPlayer()})
	 */
    public void open() {
        player.openInventory(inv);
    }
    
	/**
	 * 
	 * @param inventory
	 *            setting the {@link Inventory}
	 */
    public void setInventory(Inventory inventory) {
		this.inv = inventory;
	}
    
	/**
	 * @param title
	 *            {@link String} the inventory title<br>
	 * @param slots
	 *            {@link Integer} the slots (need to be x*9, not 0)
	 * @return {@link Inventory}
	 */
    public Inventory createInventory(String title, int slots) {
    	Inventory inv = Bukkit.createInventory(null, slots, title);
    	this.inv = inv;
    	return inv;
    }
    
	/**
	 * 
	 * @param title
	 *            {@link String} the inventory title<br>
	 * @param type
	 *            {@link InventoryType} the type of the inventory (not
	 *            {@link InventoryType#CRAFTING}, {@link InventoryType#ANVIL} or
	 *            {@link InventoryType#CREATIVE})<br>
	 *            <b>That could cause issues. For crafting use
	 *            {@link Player#openWorkbench(org.bukkit.Location, boolean)}</b>
	 * @return {@link Inventory}
	 */
    public Inventory createInventory(String title, InventoryType type) {
    	Inventory inv = Bukkit.createInventory(null, type, title);
    	this.inv = inv;
    	return inv;
    }
    
	/**
	 * Set an item in the {@link Inventory}
	 * @param slot
	 *            {@link Integer} the slot
	 * @param item
	 *            {@link ItemStack} the item
	 */
    public void setItem(int slot, ItemStack item) {
    	if (inv != null)
    		inv.setItem(slot, item);
    }
    
	/**
	 * Add an item to the {@link Inventory}
	 * @param item
	 *            {@link ItemStack}
	 */
    public void addItem(ItemStack item) {
    	if (inv != null)
    		inv.addItem(item);
    }

	/**
	 * Destroys the inventory (all to null and unregister the Listener)
	 */
    public void destroy() {
        player = null;
        handler = null;
        
        HandlerList.unregisterAll(listener);

        listener = null;
    }
	
}
