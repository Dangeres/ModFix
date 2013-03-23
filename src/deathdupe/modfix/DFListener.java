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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DFListener implements Listener, CommandExecutor {
	private static final Logger log = Bukkit.getLogger();
	private Main main;

	DFListener(Main main) {
		this.main = main;
	}

	HashSet<Integer> BadIDs = new HashSet<Integer>();

	
	
	@EventHandler
	public void onPlayerD(PlayerDeathEvent event) {

		Player p = event.getEntity();
		if (p.getItemOnCursor().getType() != Material.AIR) {
				if (BadIDs.contains(p.getItemInHand().getTypeId())) {
					//If this is null, then it means that we are dealing with backppacks... probably
					if (p.getOpenInventory() == null) {
						p.setItemOnCursor(new ItemStack(Material.AIR));
						log.info("[ModFix] "+p.getName()+" probably tried to use a dupe");
					}
				}
			}

	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		if (arg0 instanceof ConsoleCommandSender)
		{
			ConsoleCommandSender sender = (ConsoleCommandSender) arg0;
			LoadIDs();
			sender.sendMessage("[ModFix] Config reloaded");
			return true;
		}
		return false;
	}
	
	public void LoadIDs(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/ModFix/config.yml"));
		BadIDs = new HashSet<Integer>(config.getIntegerList("BadIDs"));
		config.set("BadIDs", new ArrayList<Integer>(BadIDs));
		try {
			config.save(new File("plugins/ModFix/config.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
