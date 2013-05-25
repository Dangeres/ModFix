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

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

//warning: this plugin requires ProtocolLib to run
public class Main extends JavaPlugin {

	private ModFixConfig config;
	
	private MFCommandListener commandl;
	private MFBagFixListener bagl;
	private MFTableFixListener tablel;
	private MFVillagerFixListener villagerl;
	private MFChunkFixListener chunkl;
	private MFExpFixListener expl;
	
	//private TestEventClass testl;
	
	public ProtocolManager protocolManager = null;
	@Override
	public void onLoad() {
	    protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	@Override
	public void onEnable() {
		//init config
		config = new ModFixConfig(this);
		config.loadConfig();
		//init command listener
		commandl = new MFCommandListener(this,config);
		getCommand("modfix").setExecutor(commandl);
		//init bag bugfix listener
		bagl = new MFBagFixListener(this,config);
		getServer().getPluginManager().registerEvents(bagl, this);
		//init table bugfix listener
		tablel = new MFTableFixListener(this,config);
		getServer().getPluginManager().registerEvents(tablel, this);
		//init villager bugfix listener
		villagerl = new MFVillagerFixListener(this,config);
		getServer().getPluginManager().registerEvents(villagerl, this);
        //init chunk bugfix listener
		chunkl = new MFChunkFixListener(this,config);
		getServer().getPluginManager().registerEvents(chunkl, this);
        //init exp bugfix listener
		expl = new MFExpFixListener(this,config);
		getServer().getPluginManager().registerEvents(expl, this);
		
	}
	
	@Override
	public void onDisable() {
		//null variables for folks reloading plugins
		config.saveConfig();
		config = null;
		commandl = null;
		tablel = null;
		villagerl = null;
	}
	
	
	
}
