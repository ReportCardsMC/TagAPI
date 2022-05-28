package xyz.reportcards.tagapi;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.reportcards.tagapi.wrapper.impl.WrapperPlayServerEntityDestroy;
import xyz.reportcards.tagapi.wrapper.impl.WrapperPlayServerNamedEntitySpawn;
import xyz.reportcards.tagapi.wrapper.impl.WrapperPlayServerPlayerInfo;

import java.util.*;

public class TagPlayer {

	private final UUID player;
	private String nametag;
	private TagSkin skin;

	public TagPlayer(Player player) {
		this.player = player.getUniqueId();
		this.nametag = player.getName();
		this.skin = TagSkin.from(player);
	}

	public TagPlayer(UUID uuid) throws IllegalArgumentException {
		if (Bukkit.getPlayer(uuid) == null)
			throw new IllegalArgumentException("Player with UUID " + uuid + " is not online");

		this.player = uuid;
		this.nametag = getPlayer().getName();
		this.skin = TagSkin.from(Bukkit.getPlayer(player));
	}

	public void setSkin(TagSkin skin) {
		this.skin = skin;
		update();
	}
	
	public void setNametag(String nametag) {
		this.nametag = nametag;
		update();
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}

	public String getNameTag() {
		return nametag;
	}

	public TagSkin getCurrentSkin() {
		return skin;
	}
	
	public TagSkin getActualSkin() {
		return TagSkin.from(getPlayer());
	}

	private List<PlayerInfoData> getPlayerInfoData() {
		return getPlayerInfoData(WrappedGameProfile.fromPlayer(getPlayer()));
	}

	private List<PlayerInfoData> getPlayerInfoData(WrappedGameProfile profile) {
		return Collections.singletonList(new PlayerInfoData(
				profile,
				0,
				EnumWrappers.NativeGameMode.fromBukkit(getPlayer().getGameMode()),
				WrappedChatComponent.fromText(getNameTag())));
	}

	public void update() {
		WrapperPlayServerPlayerInfo disconnect = new WrapperPlayServerPlayerInfo();
		disconnect.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
		disconnect.setData(getPlayerInfoData());
		disconnect.broadcast();
		
		 WrapperPlayServerPlayerInfo reconnect = new WrapperPlayServerPlayerInfo();
		 reconnect.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
		
		 WrappedGameProfile profile = WrappedGameProfile.fromPlayer(getPlayer());
		 profile.getProperties().removeAll("textures");
		 profile.getProperties().put("textures", skin.toProperty());
		
		 reconnect.setData(getPlayerInfoData(profile));
		 reconnect.broadcast();
		
		 refresh();
	}

	public void refresh() {
		WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
		destroy.setEntityId(getPlayer().getEntityId());
		destroy.broadcast(Collections.singletonList(getPlayer()));

		WrapperPlayServerNamedEntitySpawn spawn = new WrapperPlayServerNamedEntitySpawn();
		spawn.setPlayer(getPlayer());
		spawn.broadcast(Collections.singletonList(getPlayer()));
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
