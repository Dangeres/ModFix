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

package modfix;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

//CraftingTablesFix
public class MFTableFixListener implements Listener {

	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFTableFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}
	
	
	private HashMap<Block, String> protectblocks = new HashMap<Block, String>();
	
	//allow only one player to interact with table at a time
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
				if (Math.abs(plloc.getBlockX() - interact.getLocation().getBlockX()) > 16 || Math.abs(plloc.getBlockY() - interact.getLocation().getBlockY()) > 16) {protectblocks.remove(interact);protectblocks.put(interact, pl.getName()); return;}
				//We reached here, well, sorry player, but you can't open this for now.
				pl.sendMessage(ChatColor.RED + "Вы не можете открыть этот стол, по крайней мере сейчас");
				e.setCancelled(true);
			}
		}
	}
	
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
