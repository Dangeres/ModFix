package deathdupe.modfix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ModLogger extends Handler {

	private Main main;
	private ModFixConfig config;
	ModLogger(Main main, ModFixConfig config)
	{
		this.main = main;
		this.config = config;
	}
	
	@Override
	public void close() throws SecurityException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publish(LogRecord record) {
		if (config.checkmodlist) {
		if (!isLoggable(record))
			return;
		String logmessage = record.getMessage();
		if (logmessage.contains("connecting with mods")) {
			logmessage = logmessage.replace(",", "");
			logmessage = logmessage.replace("[", "");
			logmessage = logmessage.replace("]", "");
		String[] plmods = logmessage.split(" ");
		String player = plmods[1];
		ArrayList<String> amods = new ArrayList<String>();
			for (int i = 5; i<plmods.length; i++)
			{
				amods.add(plmods[i]);
			}
		main.playersmods.put(player, new HashSet<String>(amods));
		}
		}
	}

}
