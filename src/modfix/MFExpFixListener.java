package modfix;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
		initExpBugFixListener();
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
	}

	
	private HashMap<String, Integer> plinvmode = new HashMap<String, Integer>(); //String - playername , int - furnace type (1 - 3slot, 2 - 5slot)
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void OnPlayerIneractFurnace(PlayerInteractEvent e)
	{
		if (!config.enableExpFix) {return;}
		
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Player pl = e.getPlayer();
			Block binteract = e.getClickedBlock();
			String checkid = getIDstring(binteract);
			if (config.Furn3slotIDs.contains(checkid))
			{
				plinvmode.put(pl.getName(), 1);
			}
			if (config.Furn5slotIDs.contains(checkid))
			{
				plinvmode.put(pl.getName(), 2);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e)
	{//player can quit without closing furnace inventory, let's check it
		String plname = e.getPlayer().getName();
		if (plinvmode.containsKey(plname))
		plinvmode.remove(plname);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent e)
	{//player can be kicked without closing furnace inventory, let's check it
		String plname = e.getPlayer().getName();
		plinvmode.remove(plname);
	}
	
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.CLOSE_WINDOW) {
					@Override
				    public void onPacketReceiving(PacketEvent e) {
						
				    	if (e.getPlayer().getName().contains("[")) {return;}
				    	
						String plname = e.getPlayer().getName();
						if (plinvmode.containsKey(plname))
						plinvmode.remove(plname);
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
				    	
						String plname = e.getPlayer().getName();
						plinvmode.remove(plname);
				    }
				});
	}
	
	//set exp back if player clicked final furnace slot
	private void initExpBugFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.WINDOW_CLICK) {
					@Override
				    public void onPacketReceiving(final PacketEvent e) {
						
				    	if (e.getPlayer().getName().contains("[")) {return;}
				    	
				    	
						final Player player = e.getPlayer();
						if (!plinvmode.containsKey(player.getName())) {return;} //ignore if player clicked not furnace inventory
						
						boolean revertxp = false;
						if (plinvmode.get(player.getName()) == 1)
						{
							if (e.getPacket().getIntegers().getValues().get(1) == 2)
							{
								revertxp = true;
							}
						}
						if (plinvmode.get(player.getName()) == 2)
						{
							if (e.getPacket().getIntegers().getValues().get(1) == 3 || e.getPacket().getIntegers().getValues().get(1) == 4)
							{
								revertxp = true;
							}
						}
						
						//revert xp if needed
						if (revertxp) {
							Runnable setexp = new Runnable()
							{
								String pl = player.getName();
								int level = player.getLevel();
								float exp = player.getExp();
								public void run()
								{
									if (Bukkit.getOfflinePlayer(pl).isOnline())
									{
										Bukkit.getPlayerExact(pl).setLevel(level);
										Bukkit.getPlayerExact(pl).setExp(exp);
									}
								}
						
							};
							Bukkit.getScheduler().scheduleSyncDelayedTask(main, setexp, config.reverxpticks);
						}
				    }
				});
	}
	
	
	private String getIDstring(Block bl)
	{
		String blstring = String.valueOf(bl.getTypeId());
		if (bl.getData() !=0) {blstring += ":"+bl.getData();}
		return blstring;
	}
}
