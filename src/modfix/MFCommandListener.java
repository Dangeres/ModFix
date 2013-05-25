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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public class MFCommandListener implements  CommandExecutor {
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;

	MFCommandListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2,
			String[] args) {
		Player player = null;
		
		//check permissions
		if ((sender instanceof Player)) {
		// Player, lets check if player isOp or have permission
		player = (Player) sender;
		if (!player.isOp() && !player.hasPermission("modfix.conf")) 
		{
		sender.sendMessage(ChatColor.BLUE+"Нет прав");
		return true;
		}
		} else if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
		// Success, this was from the Console or Remote Console
		} else {
		// Who are you people?
		sender.sendMessage(ChatColor.BLUE+"Ты вообще кто такой? давай до свиданья!");
		return true;
		}
		
		//now handle commands
		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE+"Используйте команд /modfix help для получения списка комманд");
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
		{
			config.loadConfig();
			sender.sendMessage(ChatColor.BLUE+"Конфиг перезагружен");
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("help"))
		{
			displayHelp(sender);
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("iinfo"))
		{
			displayItemInfo(sender);
			return true;
		}
		else if (args.length == 3 && args[0].equalsIgnoreCase("bagfix") && args[1].equalsIgnoreCase("add"))
		{
			if (args[2].equalsIgnoreCase("19block"))
			{
				addBag19Id(sender);
				return true;
			}
		}
		else if (args.length == 3 && args[0].equalsIgnoreCase("tablefix") && args[1].equalsIgnoreCase("add"))
		{
			if (args[2].equalsIgnoreCase("iblock"))
			{
				addIBlockId(sender);
				return true;
			}
		}
		else if (args.length == 3 && args[0].equalsIgnoreCase("tablefix") && args[1].equalsIgnoreCase("add"))
		{
			if (args[2].equalsIgnoreCase("bblock"))
			{
				addBBlockId(sender);
				return true;
			}
		}
		return false;
	}
	
	
	private void displayHelp(CommandSender sender)
	{
		sender.sendMessage(ChatColor.AQUA+"/modfix reload "+ChatColor.WHITE+"-"+ChatColor.BLUE+" перезагрузить конфиг плагина");
		sender.sendMessage(ChatColor.AQUA+"/modfix iinfo "+ChatColor.WHITE+"-"+ChatColor.BLUE+" получить id и subid итема в руке");
		sender.sendMessage(ChatColor.AQUA+"/modfix bagfix add 19block"+ChatColor.WHITE+"-"+ChatColor.BLUE+" добавляет предмет в руке к списку фикса сумок (баг с кнопками 1-9)");
		sender.sendMessage(ChatColor.AQUA+"/modfix tablefix add iblock"+ChatColor.WHITE+"-"+ChatColor.BLUE+" добавляет предмет в руке к списку фикса столов (баг с одновременно открытым у 2х человек столом)");
		sender.sendMessage(ChatColor.AQUA+"/modfix tablefix add bblock"+ChatColor.WHITE+"-"+ChatColor.BLUE+" добавляет предмет в руке к списку фикса столов (баг с ломанием открытого стола )) Внимание: не спасает от слома стола предметами из модов");
	}
	
	private void displayItemInfo(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			Player pl = (Player) sender;
			String subid;
			if (pl.getItemInHand().getDurability() !=0 )
			{
				subid = String.valueOf(pl.getItemInHand().getDurability());
			} else
			{
				subid = "Отсутствует";
			}	
			pl.sendMessage(ChatColor.BLUE+"id итема: "+pl.getItemInHand().getTypeId() + ", subid итема: "+ subid);
		}
		else
		{
			sender.sendMessage(ChatColor.BLUE+"А не может у тебя быть итема в руке, тыж консоль, у тебя и рук то нет");
		}
	}
	
	private void addBag19Id(CommandSender sender)
	{
		if (sender instanceof Player)
		{
		Player pl = (Player) sender;
		config.BackPacks19IDs.add(pl.getItemInHand().getTypeId());
		config.saveConfig();
		pl.sendMessage(ChatColor.BLUE+"Предмет добавлен в список");
		}
		else
		{
			sender.sendMessage(ChatColor.BLUE+"А не может у тебя быть итема в руке, тыж консоль, у тебя и рук то нет");
		}
	}
	
	private void addIBlockId(CommandSender sender)
	{
		if (sender instanceof Player)
		{
		Player pl = (Player) sender;
		String add = String.valueOf(pl.getItemInHand().getTypeId());
		if (pl.getItemInHand().getDurability() !=0) {add += ":"+pl.getItemInHand().getDurability();}
		config.IntTablesIDs.add(add);
		config.saveConfig();
		pl.sendMessage(ChatColor.BLUE+"Предмет добавлен в список");
		}
		else
		{
			sender.sendMessage(ChatColor.BLUE+"А не может у тебя быть итема в руке, тыж консоль, у тебя и рук то нет");
		}
	}
	
	private void addBBlockId(CommandSender sender)
	{
		if (sender instanceof Player)
		{
		Player pl = (Player) sender;
		String add = String.valueOf(pl.getItemInHand().getTypeId());
		if (pl.getItemInHand().getDurability() !=0) {add += ":"+pl.getItemInHand().getDurability();}
		config.IntTablesIDs.add(add);
		config.BrkTablesIDs.add(add);
		config.saveConfig();
		pl.sendMessage(ChatColor.BLUE+"Предмет добавлен в список");
		}
		else
		{
			sender.sendMessage(ChatColor.BLUE+"А не может у тебя быть итема в руке, тыж консоль, у тебя и рук то нет");
		}
	}
		
}


