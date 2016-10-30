package kev575.advancedinventory.events;

/**
 * @author Kev575
 */
class Cancellable {
	private boolean cancelled;

	/**
	 * @param cancelled
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * @return if cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
}
