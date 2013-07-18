package modfix;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MFMinecartPortalListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFMinecartPortalListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerClickMinecartInventoryEvent(InventoryClickEvent e)
	{
		if (!config.enableMinecartFix) {return;}
		if (e.getView().getTopInventory() == null) {return;}

		InventoryHolder minecart = e.getView().getTopInventory().getHolder();
		//check if player clicked minecart inventory
		if (minecart instanceof Minecart)
		{
			System.out.println(((Entity) minecart).isValid());
			//now lets check if this click was valid
			if (!((Entity) minecart).isValid())
			{
				System.out.println("Minecart is invalid");
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}
		}

	}
	
}
