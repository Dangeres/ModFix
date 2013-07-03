package modfix;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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
		//check if player clicked an antity inventory
		if (!(e.getView().getTopInventory().getHolder() instanceof Entity)) {return;}
		Entity minecart = (Entity)e.getView().getTopInventory().getHolder();
		//check if player clicked minecart inventory
		if (!config.minecartsIDs.contains(minecart.getType().getTypeId())) {return;}
		
		//now lets check if this click was valid
		if (!minecart.isValid())
		{
			System.out.println("Minecart is invalid");
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
		}
	/*	{
			System.out.println("Check");
			System.out.println(e.getWhoClicked().getLocation().distanceSquared(minecart.getLocation());
			//if distance is too long we must close inventory
			if (e.getWhoClicked().getLocation().distanceSquared(minecart.getLocation()) > 25)
			{
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}
		}
		*/
	}
	
}
