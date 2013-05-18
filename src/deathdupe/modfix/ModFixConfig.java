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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ModFixConfig {
	private Main main;
	ModFixConfig(Main main)
	{
		this.main = main;
		LoadConfig();
	}
	
	
	protected boolean enableVillagersFix = true;
	protected boolean enableBackPackFix = true;
	protected boolean enableChunkUnloadFix = true;
	protected boolean enableTablesFix = true;
	protected HashSet<String> TablesIDs= new HashSet<String>();
	
	public void LoadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/ModFix/config.yml"));
		enableBackPackFix = config.getBoolean("EnableBackPackFix",enableBackPackFix);
		enableVillagersFix = config.getBoolean("EnableVillagersFix",enableVillagersFix);
		enableChunkUnloadFix = config.getBoolean("EnableChunkUnloadFix",enableChunkUnloadFix);
		enableTablesFix = config.getBoolean("enableTablesFix",enableTablesFix);
		TablesIDs = new HashSet<String>(config.getStringList("enableTablesFix.IDs"));
		config.set("EnableBackPackFix",enableBackPackFix);
		config.set("EnableVillagersFix",enableVillagersFix);
		config.set("EnableChunkUnloadFix",enableChunkUnloadFix);
		config.set("enableTablesFix",enableTablesFix);
		config.set("enableTablesFix.IDs",new ArrayList<String>(TablesIDs));
		try {
			config.save(new File("plugins/ModFix/config.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
