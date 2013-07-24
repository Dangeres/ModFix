package modfix;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;

public class MFMinecartFreecamOpenFixListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFMinecartFreecamOpenFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onEnityTeleportEvent(EntityTeleportEvent e)
	{
		if (!config.enableMinecartFix) {return;}

		if (config.minecartsIDs.contains(e.getEntity().getType().getTypeId()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onEnityTeleportEvent(EntityPortalEvent e)
	{
		if (!config.enableMinecartFix) {return;}
		
		if (config.minecartsIDs.contains(e.getEntity().getType().getTypeId()))
		{
			e.setCancelled(true);
		}
	}
	
}
