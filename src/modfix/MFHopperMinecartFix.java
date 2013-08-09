package modfix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class MFHopperMinecartFix implements Listener {

	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFHopperMinecartFix(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEnteredHopperMinecart(VehicleEnterEvent e)
	{
		if (!config.enablehopperminecartfix) {return;}
		if (e.getEntered() instanceof Player && e.getVehicle().getType().getTypeId() == config.hopperminecartid)
		{
			e.setCancelled(true);
		}
	}
	
}
