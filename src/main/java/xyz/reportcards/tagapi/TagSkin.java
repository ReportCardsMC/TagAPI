package xyz.reportcards.tagapi;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.net.URL;
import java.util.UUID;

public class TagSkin {

    /**
     * Fetches the texture and signature of a skin based on a player's username.
     * @apiNote This method should be run asynchronously, Minecraft will send a request to Mojang's API if the user is not cached.
     * @param username The username of the player wearing the skin
     * @return TagSkin
     */
    public static TagSkin from(String username) {
        return from(Bukkit.getOfflinePlayer(username));
    }

    /**
     * Fetches the texture and signature of a skin based on an OfflinePlayer.
     * @param player The player wearing the skin
     * @return TagSkin
     */
    public static TagSkin from(OfflinePlayer player) {
        return null;
    }

    /**
     * Fetches the texture and signature of a skin based on a UUID.
     * @apiNote This method should be run asynchronously, Minecraft will send a request to Mojang's API if the user is not cached.
     * @param uuid The uuid of the player wearing the skin
     * @return TagSkin
     */
    public static TagSkin from(UUID uuid) {
        return null;
    }

    /**
     * Fetches the texture and signature of a skin based on a player's username.
     * @apiNote This method should be run asynchronously, this is to fetch the texture and signature
     * @param url The url to get a skin from
     * @return TagSkin
     */
    public static TagSkin from(URL url) {
        return null;
    }


    public String texture;
    public String signature;
}
