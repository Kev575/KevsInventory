/* Copyright 2016 Acquized
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.acquized.inventoy;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Helper;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

// TODO: Inventory Builder
public class AInventory {

    @Getter @Setter private static JavaPlugin plugin;

    private ClickHandler handler;
    private Listener listener;
    private Inventory inventory;

    public AInventory(final ClickHandler handler, int size, String title) {
        this.handler = handler;

        Validate.notNull(plugin, "Please set first the Plugin.");

        if((title != null) && (!title.isEmpty())) {
            inventory = Bukkit.createInventory(null, size, title);
        } else {
            inventory = Bukkit.createInventory(null, size);
        }

        Bukkit.getPluginManager().registerEvents(listener = new Listener() {

            @EventHandler
            public void onClick(InventoryClickEvent e) {
                if(e.getWhoClicked() instanceof Player) {
                    Player p = (Player) e.getWhoClicked();

                    if(e.getInventory() == inventory) {
                        e.setCancelled(true);

                        ClickEvent event = new ClickEvent(p, e.getCurrentItem(), e.getSlot(), e);
                        handler.onClick(event);

                        if(event.shouldAutoClose()) {
                            p.closeInventory();
                        }
                        if(event.shouldAutoDestroy()) {
                            destroy();
                        }
                    }
                }
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) {
                if(e.getPlayer() instanceof Player) {
                    if(e.getInventory() == inventory) {
                        e.getInventory().clear();
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent e) {
                Player p = e.getPlayer();
                if((p.getOpenInventory().getTopInventory() != null) && (p.getOpenInventory().getTopInventory() == inventory)) {
                    destroy();
                }
            }

            @EventHandler
            public void onKick(PlayerKickEvent e) {
                Player p = e.getPlayer();
                if((p.getOpenInventory().getTopInventory() != null) && (p.getOpenInventory().getTopInventory() == inventory)) {
                    destroy();
                }
            }

        }, plugin);

    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

    public void destroy() {
        HandlerList.unregisterAll(listener);
        this.handler = null;
        this.listener = null;
        this.inventory = null;
    }

    public AInventory item(int slot, ItemStack stack) {
        inventory.setItem(slot, stack);
        return this;
    }

    // ----------------------------------------------------

    public @Helper class ClickEvent {

        private ItemStack item;
        private int slot;
        private Player p;
        private boolean autoClose = true;
        private boolean autoDestroy = false;

        private InventoryClickEvent original;

        public ClickEvent(Player p, ItemStack item, int slot, InventoryClickEvent original) {
            this.p = p;
            this.item = item;
            this.slot = slot;
            this.original = original;
        }

        public InventoryClickEvent __INVALID__originalEvent() { return original; }

        public Player getPlayer() {
            return p;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getSlot() {
            return slot;
        }

        public boolean shouldAutoClose() {
            return autoClose;
        }

        public boolean shouldAutoDestroy() {
            return autoDestroy;
        }

        public void setAutoClose(boolean autoClose) {
            this.autoClose = autoClose;
        }

        public void setAutoDestroy(boolean autoDestroy) {
            this.autoDestroy = autoDestroy;
        }

    }

    public @UtilityClass interface ClickHandler {
        void onClick(ClickEvent event);
    }

}
