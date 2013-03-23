package deathdupe.modfix;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private DFListener lis;
	@Override
	public void onEnable() {
		lis = new DFListener(this);
		getServer().getPluginManager().registerEvents(lis, this);
		getCommand("modfix").setExecutor(lis);
		lis.LoadIDs();
	}
	public void onDisable() {
		lis = null;
	}
	
	
	
}
