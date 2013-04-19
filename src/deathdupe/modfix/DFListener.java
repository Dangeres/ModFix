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

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;

//notice: every fix will be in own listener soon;
public class DFListener implements Listener, CommandExecutor {
	private static final Logger log = Bukkit.getLogger();
	private Main main;
	private ModFixConfig config;

	DFListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}

	// Villagers fix
	@EventHandler
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

	// BackPack Death fix
	@EventHandler
	public void onPlayerD(EntityDamageEvent event) {
		if (config.enableBackPackFix) {
			if (event.getEntity() instanceof Player) {
				Player p = (Player) event.getEntity();
				if (p.getHealth() - event.getDamage() <= 0) {

					if (config.BadIDs.contains(p.getItemInHand().getTypeId())) {
						p.closeInventory();
					}
				}
			}
		}

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

}
