package xyz.reportcards.tagapi;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.reportcards.tagapi.utils.PacketUtils;

import java.util.Collections;
import java.util.UUID;

@SuppressWarnings("unused")
public class TagPlayer {

	private final UUID player;
	private String nametag;
	private TagSkin skin;

	public TagPlayer(Player player) {
		this.player = player.getUniqueId();
		this.nametag = player.getName();
		this.skin = TagSkin.from(player).getNow(null);
	}

	public TagPlayer(UUID uuid) throws IllegalArgumentException {
		if (Bukkit.getPlayer(uuid) == null)
			throw new IllegalArgumentException("Player with UUID " + uuid + " is not online");

		this.player = uuid;
		this.nametag = getPlayer().getName();
		this.skin = TagSkin.from(Bukkit.getPlayer(player)).getNow(null);
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
		return this.nametag;
	}

	public TagSkin getCurrentSkin() {
		return this.skin;
	}
	
	public TagSkin getActualSkin() {
		return TagSkin.from(getPlayer()).getNow(null);
	}
	
	public User getPacketUser() {
		return PacketEvents.getAPI().getPlayerManager().getUser(player);
	}
	
	public int getPing() {
		return PacketEvents.getAPI().getPlayerManager().getPing(player);
	}

	private WrapperPlayServerPlayerInfo.PlayerData getPlayerData() {
		return getPlayerData(getPacketUser().getProfile());
	}
	
	private WrapperPlayServerPlayerInfo.PlayerData getPlayerData(UserProfile profile) {
		return new WrapperPlayServerPlayerInfo.PlayerData(
			Component.text(this.getNameTag()),
			profile,
			GameMode.valueOf(getPlayer().getGameMode().name().toUpperCase()),
			getPing()
		);
	}

	public void update() {
		PacketUtils.broadcast(new WrapperPlayServerPlayerInfo(
			WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER,
			getPlayerData()
		));
		
		WrapperPlayServerPlayerInfo reconnect = new WrapperPlayServerPlayerInfo(
			WrapperPlayServerPlayerInfo.Action.ADD_PLAYER
		);
		
		UserProfile profile = getPacketUser().getProfile();
		profile.getTextureProperties().clear();
		profile.getTextureProperties().add(skin.toProperty());
		
		reconnect.setPlayerDataList(Collections.singletonList(getPlayerData(profile)));
		PacketUtils.broadcast(reconnect);
		
		refresh();
	}

	public void refresh() {
		PacketUtils.broadcast(
			new WrapperPlayServerDestroyEntities(getPlayer().getEntityId()),
			Collections.singletonList(getPlayer())
		);
		
		PacketUtils.broadcast(
			new WrapperPlayServerSpawnPlayer(
				getPlayer().getEntityId(),
				getPlayer().getUniqueId(),
				PacketUtils.getPacketLocation(getPlayer().getLocation())
			),
			Collections.singletonList(getPlayer())
		);
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
