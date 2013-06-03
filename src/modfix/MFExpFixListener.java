package modfix;

import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class MFExpFixListener implements Listener {

	private Main main;
	private ModFixConfig config;

	MFExpFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
	}

	
	private HashSet<String> plinf = new HashSet<String>(); //We will add player to this list when he enters furnace and remove when he leaves
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void OnPlayerIneractFurnace(PlayerInteractEvent e)
	{
		if (!config.enableExpFix) {return;}
		
	
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{

			String checkid = getIDstring(e.getClickedBlock());
			if (config.furnSlotIDs.contains(checkid))
			{
				plinf.add(e.getPlayer().getName());
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e)
	{//player can quit without closing furnace inventory, let's check it
		plinf.remove(e.getPlayer().getName());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent e)
	{//player can be kicked without closing furnace inventory, let's check it
		plinf.remove(e.getPlayer().getName());
	}
	
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.CLOSE_WINDOW) {
					@Override
				    public void onPacketReceiving(PacketEvent e) {
						
				    	if (e.getPlayer().getName().contains("[")) {return;}
				    	
						plinf.remove(e.getPlayer().getName());
				    }
				});
	}
	
	
	private void initServerCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.SERVER_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Server.CLOSE_WINDOW) {
					@Override
				    public void onPacketSending(PacketEvent e) {
						
				    	if (e.getPlayer().getName().contains("[")) {return;}
				    	
						plinf.remove(e.getPlayer().getName());
				    }
				});
	}
	
    //won't allow player to earn exp if he is in furnace	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onExpGain(PlayerExpChangeEvent e)
	{
		if (!config.enableExpFix) {return;}
		if (plinf.contains(e.getPlayer().getName()))
		{
			e.setAmount(0);
		}
	}
	
	
	private String getIDstring(Block bl)
	{
		String blstring = String.valueOf(bl.getTypeId());
		if (bl.getData() !=0) {blstring += ":"+bl.getData();}
		return blstring;
	}
}
