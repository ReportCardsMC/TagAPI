package xyz.reportcards.tagapi;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class TagAPI {
    
    public static void setSkin(Player player, String username) {
        TagPlayer data = TagPlayer.from(player);
        if (Bukkit.getPlayer(username) != null) data.setSkin(TagSkin.from(Bukkit.getPlayer(username)));
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