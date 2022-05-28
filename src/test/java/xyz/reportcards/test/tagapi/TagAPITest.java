package xyz.reportcards.test.tagapi;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TagAPITest extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		getCommand("setskin").setExecutor(new SkinCommand());
	}

}
