package modfix;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class MFRP2FixListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFRP2FixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}
	
	//check if we have RP2 wires near the redstone wire and then break it if so
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onRedstoneUpdate(BlockRedstoneEvent e)
	{
		if (!config.enableRP2wiresfix) {return;}
		
		Block b = e.getBlock();
		if (b.getType() == Material.REDSTONE_WIRE)
		{
			if (isRP2WiresNear(b))
			{
				b.breakNaturally();
			}
		}
	}
	
	private boolean isRP2WiresNear(Block b)
	{	
		if (
			config.RP2WiresIDs.contains(b.getRelative(BlockFace.DOWN,1).getTypeId()) || config.RP2WiresIDs.contains(b.getRelative(BlockFace.UP,1).getTypeId())
			|| config.RP2WiresIDs.contains(b.getRelative(BlockFace.NORTH,1).getTypeId()) || config.RP2WiresIDs.contains(b.getRelative(BlockFace.SOUTH,1).getTypeId())
			|| config.RP2WiresIDs.contains(b.getRelative(BlockFace.WEST,1).getTypeId()) || config.RP2WiresIDs.contains(b.getRelative(BlockFace.EAST,1).getTypeId())
		)
		{
			return true;
		}
		return false;
	}

}
