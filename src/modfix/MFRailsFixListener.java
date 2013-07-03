package modfix;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class MFRailsFixListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFRailsFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPistonMovedRails(BlockPistonExtendEvent e)
	{
		if (!config.enableRailsFix) {return;}
		for (Block b : e.getBlocks())
		{
			if (config.RailsIDs.contains(b.getTypeId()))
			{
				b.breakNaturally();
				return;
			}
		}
	}
	
}
