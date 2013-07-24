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

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

//warning: this plugin requires ProtocolLib to run
public class Main extends JavaPlugin {

	private Logger log = Bukkit.getLogger();
	
	private ModFixConfig config;
	
	private MFCommandListener commandl;
	private MFBagFixListener bagl;
	private MFTableFixListener tablel;
	private MFVillagerFixListener villagerl;
	private MFChunkFixListener chunkl;
	private MFExpFixListener expl;
	private MFMinecartFreecamOpenFixListener mpl;
	private MFRailsFixListener rfl;	
	private MFRP2FixListener rp2l;
	
	public ProtocolManager protocolManager = null;
	private boolean enableself = true;
	@Override
	public void onLoad() {
		if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null)
		{
			enableself = false;
			log.severe("[ModFix] ProtolLib is not installed, install it first");
			log.severe("[ModFix] Shutting down server");
			Bukkit.shutdown();
		}
		else {
	    protocolManager = ProtocolLibrary.getProtocolManager();
		}
	}
	
	@Override
	public void onEnable() {
		if (enableself) {
		//init config
		config = new ModFixConfig(this);
		config.loadConfig();
		//init command listener
		commandl = new MFCommandListener(this,config);
		getCommand("modfix").setExecutor(commandl);
		getServer().getPluginManager().registerEvents(commandl, this);
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
		//init minecart bugfix listener
		mpl = new MFMinecartFreecamOpenFixListener(this,config);
		getServer().getPluginManager().registerEvents(mpl, this);
		//init rails bugfix listener
		rfl = new MFRailsFixListener(this,config);
		getServer().getPluginManager().registerEvents(rfl, this);
		rp2l = new MFRP2FixListener(this,config);
		getServer().getPluginManager().registerEvents(rp2l, this);
		}
	}
	
	@Override
	public void onDisable() {
		if (enableself) {
		//null variables for folks reloading plugins
		config.saveConfig();
		config = null;
		commandl = null;
		HandlerList.unregisterAll(this);
		tablel = null;
		villagerl = null;
		mpl = null;
		rfl = null;
		rp2l = null;
		protocolManager.removePacketListeners(this);
		protocolManager = null;
		}
	}
	
	
	
}
