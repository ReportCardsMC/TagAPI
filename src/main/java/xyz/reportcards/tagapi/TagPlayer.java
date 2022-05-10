package xyz.reportcards.tagapi;


import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.reportcards.tagapi.wrapper.impl.WrapperPlayServerPlayerInfo;

import java.util.Collections;
import java.util.UUID;

public class TagPlayer {

    private final UUID player;
    private String nameTag;
    private TagSkin skin;

    public TagPlayer(Player player) {
        this.player = player.getUniqueId();
        this.nameTag = player.getName();
        this.skin = TagSkin.from(player);
    }

    public TagPlayer(UUID uuid) throws IllegalArgumentException {
        if (Bukkit.getPlayer(uuid) == null) throw new IllegalArgumentException("Player with UUID " + uuid + " is not online");
        this.player = uuid;
        this.nameTag = getPlayer().getName();
        this.skin = TagSkin.from(player);
    }

    public void setSkin(TagSkin skin) {
        this.skin = skin;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public String getNameTag() {
        return nameTag;
    }

    public TagSkin getSkin() {
        return skin;
    }

    public void refresh() {
        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo();
        packet.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        packet.setData(Collections.singletonList(new PlayerInfoData(WrappedGameProfile.fromPlayer(getPlayer()));

        packet.sendPacket(getPlayer());
    }

    public static TagPlayer from(Player player) {
        return new TagPlayer(player);
    }

    public static TagPlayer from(UUID uuid) {
        return new TagPlayer(uuid);
    }

    public static TagPlayer from(String name) {
        return new TagPlayer(Bukkit.getPlayer(name));
    }
}
