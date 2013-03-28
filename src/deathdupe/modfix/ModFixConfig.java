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
	protected boolean checkmodlist = false;
	protected HashSet<String> AllowedModsList = new HashSet<String>();
	protected String modscheckfailedmessage  = "Перезакачайте наш клиент";
	
	public void LoadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/ModFix/config.yml"));
		enableBackPackFix = config.getBoolean("EnableBackPackFix",enableBackPackFix);
		BadIDs = new HashSet<Integer>(config.getIntegerList("BadIDs"));
		enableVillagersFix = config.getBoolean("EnableVillagersFix",enableVillagersFix);
		checkmodlist = config.getBoolean("CheckSentClientMods",checkmodlist);
		AllowedModsList = new HashSet<String>(config.getStringList("AllowedModsList"));
		modscheckfailedmessage = config.getString("ModsCheckFailedMessage", modscheckfailedmessage);
		config.set("EnableBackPackFix",enableBackPackFix);
		config.set("BadIDs", new ArrayList<Integer>(BadIDs));
		config.set("EnableVillagersFix",enableVillagersFix);
		config.set("CheckSentClientMods", checkmodlist);
		config.set("AllowedModsList", new ArrayList<String>(AllowedModsList));
		config.set("ModsCheckFailedMessage", modscheckfailedmessage);
		try {
			config.save(new File("plugins/ModFix/config.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
