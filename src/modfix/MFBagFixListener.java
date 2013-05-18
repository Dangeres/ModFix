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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

// BackPack fix
public class MFBagFixListener implements Listener {
	private Main main;
	private ModFixConfig config;
	
	MFBagFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
		initBagBugFixListener();
	}
	
	//close inventory on death
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
	
	//close inventory on quit
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerExit(PlayerQuitEvent event) {
		if (config.enableBackPackFix) {
					event.getPlayer().closeInventory();
			}
	}
	
	//close inventory on kick
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event) {
		if (config.enableBackPackFix) {
					event.getPlayer().closeInventory();
			}
	}
	
	//restrict using 1-9 buttons in modded inventories
	private void initBagBugFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.WINDOW_CLICK) {
				    @SuppressWarnings("deprecation")
					@Override
				    public void onPacketReceiving(PacketEvent e) {
				    	if (!config.enableBackPackFix) {return;}
				    	if (!(e.getPacketID() == Packets.Client.WINDOW_CLICK)) {return;}
				    	//check click type , 2 ==  1..9 buttons
				    	{if (e.getPacket().getIntegers().getValues().get(3) == 2)
				    		//if item in hand is one of the bad ids block action
				    		if (config.BackPacksIDs.contains(e.getPlayer().getItemInHand().getTypeId())) {
				    			e.setCancelled(true);
				    			e.getPlayer().closeInventory();
				    			e.getPlayer().updateInventory();
				    			e.getPlayer().sendMessage(ChatColor.RED+"Кнопками 1-9 нельзя пользоваться в этом инвентаре");
				    		}
				 	   	}

				    }
				});
	}

}
