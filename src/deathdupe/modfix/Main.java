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

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;


public class Main extends JavaPlugin {

	
	public HashMap<String, HashSet<String> > playersmods = new HashMap<String, HashSet<String>>();
		
	private DFListener lis;
	private ModFixConfig config;
	
	public ProtocolManager protocolManager = null;
	@Override
	public void onLoad() {
	    protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	@Override
	public void onEnable() {
		config = new ModFixConfig(this);
		lis = new DFListener(this,config);
		getServer().getPluginManager().registerEvents(lis, this);
		getCommand("modfix").setExecutor(lis);
		if (protocolManager != null) {lis.iniProtocolLiblisteners();}
	}
	
	@Override
	public void onDisable() {
		lis = null;
		config = null;
	}
	
	
	
}
