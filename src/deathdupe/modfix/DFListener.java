/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 */

package deathdupe.modfix;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

//notice: every fix will be in own listener soon;
public class DFListener implements Listener, CommandExecutor {
	private Main main;
	private ModFixConfig config;

	DFListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}


	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		if (arg0 instanceof ConsoleCommandSender) {
			ConsoleCommandSender sender = (ConsoleCommandSender) arg0;
			config.LoadConfig();
			sender.sendMessage("[ModFix] Config reloaded");
			return true;
		}
		return false;
	}
	
	
	// Villagers fix start
	// Restrict shift-click
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void VillagerIncClickEvent(InventoryClickEvent event) {
		if (config.enableVillagersFix) {
			if (event.getView().getTopInventory() != null
					&& event.getView().getTopInventory().getType()
							.equals(InventoryType.MERCHANT)) {
				if (event.isShiftClick()) {

					if (event.getSlotType().equals(SlotType.RESULT)
							&& event.getCurrentItem().getType() != Material.EMERALD) {
						event.setCancelled(true);
						event.getWhoClicked().closeInventory();
						Bukkit.getPlayer(event.getWhoClicked().getName())
								.sendMessage(
										ChatColor.RED
												+ "Запрещено покупать у жителей за изумруды shift-кликом");
					}
				}
			}
		}
	}
	// Villagers fix end
	


	// BackPack fix start
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerD(PlayerDeathEvent event) {
		if (config.enableBackPackFix) {
			if (event.getEntity() instanceof Player) {
				Player p = (Player) event.getEntity();
				{
					p.closeInventory();
				}
			}
		}

	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerExit(PlayerQuitEvent event) {
		if (config.enableBackPackFix) {
					event.getPlayer().closeInventory();
			}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event) {
		if (config.enableBackPackFix) {
					event.getPlayer().closeInventory();
			}
	}
	// BackPack fix end




	
	
	// ChunkUnloadInventoryFix start
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerChangedChunkEvent(PlayerMoveEvent e)
	{
		if (config.enableChunkUnloadFix)
		{
			if (!e.getFrom().getChunk().equals(e.getTo().getChunk()))
			{
				e.getPlayer().closeInventory();
			}
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onTp(PlayerTeleportEvent e)
	{
		if (config.enableChunkUnloadFix)
		{
			if (!e.getFrom().getChunk().equals(e.getTo().getChunk()))
			{
				e.getPlayer().closeInventory();
			}
		}
	}
	//ChunkUnloadInventoryFix end
	
	
	//CraftingTablesFix begin
	private HashMap<Block, String> protectblocks =new HashMap<Block, String>();
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void OnPlayerIneractTable(PlayerInteractEvent e)
	{
		if (!config.enableTablesFix) {return;}
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Player pl = e.getPlayer();
			Block interact = e.getClickedBlock();
			String checkid = getIDstring(interact);
			if (config.TablesIDs.contains(checkid))
			{
				if (protectblocks.get(interact) == null)
				{ //Put block to list of protected blocks
					protectblocks.put(interact, pl.getName());
					return;
				}
				//If it's the same player let him open this
				if (pl.getName().equals(protectblocks.get(interact))) {return;}


				// There is aready an owner of the blocks somewhere, let's check where it is
				OfflinePlayer oldpl = Bukkit.getOfflinePlayer(protectblocks.get(interact));
				if (!oldpl.isOnline()) 	{protectblocks.remove(interact); protectblocks.put(interact, pl.getName()); return;}
				Location plloc = oldpl.getPlayer().getLocation();
				if (!plloc.getWorld().equals(interact.getLocation().getWorld())) {protectblocks.remove(interact); protectblocks.put(interact, pl.getName()); return;}
				if (Math.abs(plloc.getBlockX() - interact.getLocation().getBlockX()) >= 10 && Math.abs(plloc.getBlockY() - interact.getLocation().getBlockY()) >= 10 && Math.abs(plloc.getBlockZ() - interact.getLocation().getBlockZ()) >= 10) {protectblocks.remove(interact);protectblocks.put(interact, pl.getName()); return;}
				//We reached here, well, sorry player, but you can't open this for now.
				pl.sendMessage(ChatColor.RED + "Вы не можете открыть этот стол, по крайней мере сейчас");
				e.setCancelled(true);
			}
		}
	}
	//CraftingTablesFix end
	
	
	private String getIDstring(Block bl)
	{
		String blstring = null;
		if (bl.getData() !=0) {
			blstring = bl.getTypeId()+":"+bl.getData();
		}
		else 
		{
			blstring = String.valueOf(bl.getTypeId());
		}
		return blstring;
	}
	
	
	
	
}


