// add your own package her!

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

public class KevsInventory {

    public class KevInvClickEvent {
        private int slot;

        private ItemStack item;
        private Player player;
        private boolean close = true;
        private boolean destroy = true;

        public KevInvClickEvent(Player p, int slot, ItemStack itemStack) {
            this.slot = slot;
            this.item = itemStack;
            this.player = p;
        }
        
        public Player getPlayer() {
			return player;
		}

        public int getSlot() {
            return slot;
        }

        public ItemStack getItem() {
            return item;
        }

        public boolean getWillClose() {
            return close;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public boolean getWillDestroy() {
            return destroy;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

    public interface KevInvClickEventHandler {
        void onInventoryClick(KevInvClickEvent event);
    }

    private Player player;

	private KevInvClickEventHandler handler;

    private Inventory inv;

    private Listener listener;

    public KevsInventory(Player player, final KevInvClickEventHandler handler) {
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
                    Player player = (Player) event.getPlayer();
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

        Bukkit.getPluginManager().registerEvents(listener, KevsBans.getPlugin(KevsBans.class));
    }

    public Player getPlayer() {
        return player;
    }

    public void open() {
        player.openInventory(inv);
    }
    
    public void setInventory(Inventory inventory) {
		this.inv = inventory;
	}
    
    public Inventory createInventory(String title, int slots) {
    	Inventory inv = Bukkit.createInventory(null, slots, title);
    	this.inv = inv;
    	return inv;
    }
    
    public Inventory createInventory(String title, InventoryType type) {
    	Inventory inv = Bukkit.createInventory(null, type, title);
    	this.inv = inv;
    	return inv;
    }
    
    public void setItem(int slot, ItemStack item) {
    	if (inv != null)
    		inv.setItem(slot, item);
    }
    
    public void addItem(ItemStack item) {
    	if (inv != null)
    		inv.addItem(item);
    }

    public void destroy() {
        player = null;
        handler = null;
        
        HandlerList.unregisterAll(listener);

        listener = null;
    }
	
}
