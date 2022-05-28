package xyz.reportcards.tagapi;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class TagAPI {
    
    public static void register(Plugin plugin) {
        if (PacketEvents.getAPI() != null && isLoaded()) return; // Already registered from another plugin
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin));
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().init();
        Bukkit.getLogger().info("[TagAPI] PacketEvents has been registered.");
    }
    
    public static boolean isLoaded() { return PacketEvents.getAPI().isLoaded(); }
    
    public static void setSkin(Player player, String username) {
        TagPlayer data = TagPlayer.from(player);
        if (Bukkit.getPlayer(username) != null) TagSkin.from(Bukkit.getPlayer(username)).thenAccept(data::setSkin);
        else TagSkin.from(username).thenAccept(data::setSkin);
    }
    
    public static void setSkin(Player player, OfflinePlayer target) {
        TagPlayer data = TagPlayer.from(player);
        TagSkin.from(target).thenAccept(data::setSkin);
    }
    
    public static void setNametag(Player player, String nametag) {
        TagPlayer data = TagPlayer.from(player);
        data.setNametag(nametag);
    }
    
}