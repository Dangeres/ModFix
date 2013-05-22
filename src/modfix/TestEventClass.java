package modfix;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class TestEventClass implements Listener {

	private Main main;
	private ModFixConfig config;
	
	TestEventClass(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}

	//restrict using 1-9 buttons in modded inventories
	public void initTestListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.SERVER_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Server.BLOCK_CHANGE) {
					@Override
				    public void onPacketSending(PacketEvent e) {
				    	if (!(e.getPacketID() == Packets.Server.BLOCK_CHANGE)) {return;}
				    	//check click type , 2 ==  1..9 buttons
				    	List<Integer> coords = e.getPacket().getIntegers().getValues();
				    	System.out.println(new Location(e.getPlayer().getWorld(), coords.get(0),coords.get(1),coords.get(2)).getBlock().getTypeId());
				    }
				  });
	}
}
