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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ModFixConfig {
	@SuppressWarnings("unused")
	private Main main;
	ModFixConfig(Main main)
	{
		this.main = main;
	}
	
	protected boolean enableVillagersFix = true;
	protected boolean enableBackPackFix = true;
	protected HashSet<Integer> BackPacks19IDs = new HashSet<Integer>();
	protected boolean enableChunkUnloadFix = true;
	protected boolean enableTablesFix = true;
	protected HashSet<String> IntTablesIDs= new HashSet<String>();
	protected HashSet<String> BrkTablesIDs= new HashSet<String>();
	protected boolean enableExpFix = true;
	protected HashSet<String> furnSlotIDs= new HashSet<String>();

	
	public void loadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/ModFix/config.yml"));
		enableBackPackFix = config.getBoolean("BackPackFix.enable",enableBackPackFix);
		BackPacks19IDs = new HashSet<Integer>(config.getIntegerList("BackPackFix.19BlockIDs"));
		enableVillagersFix = config.getBoolean("VillagersFix.enable",enableVillagersFix);
		enableChunkUnloadFix = config.getBoolean("ChunkUnloadFix.enable",enableChunkUnloadFix);
		enableTablesFix = config.getBoolean("TablesFix.enable",enableTablesFix);
		IntTablesIDs = new HashSet<String>(config.getStringList("TablesFix.InteractBlockIDs"));
		BrkTablesIDs = new HashSet<String>(config.getStringList("TablesFix.BreakBlockIDs"));
		enableExpFix = config.getBoolean("ExpFix.enable",enableExpFix);
		furnSlotIDs = new HashSet<String>(config.getStringList("ExpFix.FurnaceIds"));
		
		
		saveConfig();
	}
	
	public void saveConfig()
	{
		FileConfiguration config = new YamlConfiguration();
		config.set("BackPackFix.enable",enableBackPackFix);
		config.set("BackPackFix.19BlockIDs",new ArrayList<Integer>(BackPacks19IDs));
		config.set("VillagersFix.enable",enableVillagersFix);
		config.set("ChunkUnloadFix.enable",enableChunkUnloadFix);
		config.set("TablesFix.enable",enableTablesFix);
		config.set("TablesFix.InteractBlockIDs",new ArrayList<String>(IntTablesIDs));
		config.set("TablesFix.BreakBlockIDs",new ArrayList<String>(BrkTablesIDs));
		config.set("ExpFix.enable",enableExpFix);
		config.set("ExpFix.FurnaceIds",new ArrayList<String>(furnSlotIDs));
		try {
			config.save(new File("plugins/ModFix/config.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
