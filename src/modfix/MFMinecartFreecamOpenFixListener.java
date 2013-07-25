package modfix;

import java.util.HashMap;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class MFMinecartFreecamOpenFixListener implements Listener {
	
	private Main main;
	private ModFixConfig config;
	
	MFMinecartFreecamOpenFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
		initMinecartInventoryClickCheck();
	}
	
	private HashMap<String,Entity> playersopenedminecart = new HashMap<String,Entity>();
	
	//add player to list when he opens minecart
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerOpenedMinecart(PlayerInteractEntityEvent e)
	{
		if (!config.enableMinecartFix)  {return;}
		
		if (config.minecartsIDs.contains(e.getRightClicked().getType().getTypeId()))
		{
			playersopenedminecart.put(e.getPlayer().getName(),e.getRightClicked());
		}
	}
	
	
	private void initMinecartInventoryClickCheck()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.WINDOW_CLICK) {
					@Override
				    public void onPacketReceiving(PacketEvent e) {	
				    	
						if (!config.enableMinecartFix) {return;}
				    
						String plname = e.getPlayer().getName();
						
						if (playersopenedminecart.containsKey(plname))
						{
							Entity ent = playersopenedminecart.get(plname);
							if (!ent.isValid() || ent.getLocation().distanceSquared(e.getPlayer().getLocation()) > 36 || !ent.getWorld().equals(e.getPlayer().getWorld()))
							{
								e.setCancelled(true);
								e.getPlayer().closeInventory();
							}
						}
					}
				});
	}
	
	
	
	//remove player from list when he closes minecart
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.CLOSE_WINDOW) {
					@Override
				    public void onPacketReceiving(PacketEvent e) {
				    	playersopenedminecart.remove(e.getPlayer().getName());
				    }
				});
	}
	
	//remove player from list when he closes minecart
	private void initServerCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.SERVER_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Server.CLOSE_WINDOW) {
					@Override
				    public void onPacketSending(PacketEvent e) {
			    		playersopenedminecart.remove(e.getPlayer().getName());
				    }
				});
	}
	
}
