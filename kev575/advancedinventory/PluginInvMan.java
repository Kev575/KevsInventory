package kev575.advancedinventory;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * the plugin class
 * 
 * @author Kev575
 */
public class PluginInvMan extends JavaPlugin {

	@Override
	public void onEnable() {
	}

	protected PluginInvMan getInstance() {
		return getPlugin(PluginInvMan.class);
	}

}
