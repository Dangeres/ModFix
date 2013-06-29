package modfix;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class MFMinecartPortalListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFMinecartPortalListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}
	
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMinecratPortalTeleport(EntityPortalEvent e) 
	{
		if (!config.enableMinecartFix) {return;}
		if (config.minecartsIDs.contains(e.getEntity().getType().getTypeId()))
		{
			e.setCancelled(true);
		}
	}
	
}
