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
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	
	public HashMap<String, HashSet<String> > playersmods = new HashMap<String, HashSet<String>>();
	
	private static final Logger log = Logger.getLogger("ForgeModLoader");
	
	private DFListener lis;
	private ModLogger ml;
	private ModFixConfig config;
	@Override
	public void onEnable() {
		config = new ModFixConfig(this);
		lis = new DFListener(this,config);
		getServer().getPluginManager().registerEvents(lis, this);
		getCommand("modfix").setExecutor(lis);
		ml = new ModLogger(this,config);
		log.addHandler(ml);
	}
	
	@Override
	public void onDisable() {
		lis = null;
		ml = null;
		config = null;
	}
	
	
	
}
