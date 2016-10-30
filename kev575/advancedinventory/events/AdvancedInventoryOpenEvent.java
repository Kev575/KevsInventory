package kev575.advancedinventory.events;

import org.bukkit.entity.Player;

/**
 * The Event called when an inventory gets opened
 * 
 * @author Kev575
 */
public class AdvancedInventoryOpenEvent extends Cancellable {

	private final Player player;

	/**
	 * @param p
	 *            the player
	 */
	public AdvancedInventoryOpenEvent(Player p) {
		this.player = p;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
