package kev575.advancedinventory.events;

import kev575.advancedinventory.AdvancedInventory;

/**
 * The listener that could be added to the {@link AdvancedInventory}
 * 
 * @see AdvancedInventory#addListener(AdvancedInventoryListener)
 * 
 * @author Kev575
 */
public abstract class AdvancedInventoryListener {

	/**
	 * Event is called after click, but before click done; cancellable
	 * 
	 * @param e
	 *            the event;
	 */
	public void onClick(AdvancedInventoryClickEvent e) {
	}

	/**
	 * Event is called before inventory opens; cancellable
	 * 
	 * @param e
	 *            the event
	 */
	public void onOpen(AdvancedInventoryOpenEvent e) {
	}

	/**
	 * Event is called after inventory closes; not cancellable
	 * 
	 * @param e
	 *            the event
	 */
	public void onClose(AdvancedInventoryCloseEvent e) {
	}

}
