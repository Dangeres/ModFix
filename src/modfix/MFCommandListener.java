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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class MFCommandListener implements  CommandExecutor {
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;

	MFCommandListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] arg3) {
		if (sender instanceof ConsoleCommandSender) {
			ConsoleCommandSender csender = (ConsoleCommandSender) sender;
			config.LoadConfig();
			csender.sendMessage("[ModFix] Config reloaded");
			return true;
		}
		return false;
	}
	
		
}


