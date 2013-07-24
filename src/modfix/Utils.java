package modfix;

import org.bukkit.block.Block;

public class Utils {

	
	public static String getIDstring(Block bl)
	{
		String blstring = String.valueOf(bl.getTypeId());
		if (bl.getData() !=0) {blstring += ":"+bl.getData();}
		return blstring;
	}
}
