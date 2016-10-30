# AdvancedInventory
Create bukkit inventorys with a cool API.

### Small Guide
```java
AdvancedInventory inv = new AdvancedInventory(plugin, player, AdvancedInventoryType.CHEST);
inv.addListener(new AdvancedInventoryListener() {
	@Override
	public void onClick(AdvancedInventoryClickEvent e) {
		e.setCancelled(true);
	}
});
inv.setTitle("§cAdvancedInventory");
inv.open();
```