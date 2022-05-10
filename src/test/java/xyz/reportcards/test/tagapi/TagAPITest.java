package xyz.reportcards.test.tagapi;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.reportcards.tagapi.TagPlayer;

public class TagAPITest extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // im gonna cry - dad
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void onSomething(PlayerJoinEvent event) {
        TagPlayer tagPlayer = TagPlayer.from(event.getPlayer());
        tagPlayer.setSkin(TagSkin);
    }

}
