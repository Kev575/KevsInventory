package kev575.advancedinventory.events;

import org.bukkit.entity.Player;

/**
 * The Event called when an inventory gets closed
 * 
 * @author Kev575
 */
public class AdvancedInventoryCloseEvent {

	private final Player player;

	/**
	 * @param p
	 *            the player
	 */
	public AdvancedInventoryCloseEvent(Player p) {
		this.player = p;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
