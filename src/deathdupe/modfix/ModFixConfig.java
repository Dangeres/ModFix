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
	protected HashSet<Integer> BadIDs = new HashSet<Integer>();
	
	public void LoadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/ModFix/config.yml"));
		enableBackPackFix = config.getBoolean("EnableBackPackFix",enableBackPackFix);
		BadIDs = new HashSet<Integer>(config.getIntegerList("BadIDs"));
		enableVillagersFix = config.getBoolean("EnableVillagersFix",enableVillagersFix);
		config.set("EnableBackPackFix",enableBackPackFix);
		config.set("BadIDs", new ArrayList<Integer>(BadIDs));
		config.set("EnableVillagersFix",enableVillagersFix);
		try {
			config.save(new File("plugins/ModFix/config.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
