package xyz.reportcards.tagapi;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.User;
import kong.unirest.Unirest;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.reportcards.tagapi.model.unified.AccountModel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TagSkin {
	
	// To avoid rate limits, we will use a unified api to get the skin data.
	private static final String MOJANG_API = "https://api.ashcon.app/mojang/v2/user/";
	
	protected TagSkin() {}
	
	protected TagSkin(String texture, String signature) {
		this.texture = texture;
		this.signature = signature;
	}
	
	/**
	 * Fetches the texture and signature of a skin based on a player's username.
	 * 
	 * @apiNote This method should be run asynchronously, Minecraft will send a
	 *          request to Mojang's API if the user is not cached.
	 * @param username The username of the player wearing the skin
	 * @return TagSkin
	 */
	public static CompletableFuture<TagSkin> from(String username) {
		CompletableFuture<TagSkin> result = new CompletableFuture<>();
		TagSkin skin = new TagSkin();
		
		Unirest.get(MOJANG_API+username)
				.asObjectAsync(AccountModel.class)
				.thenAccept(response -> {
					AccountModel data = response.getBody();
					if (!response.isSuccess()) result.completeExceptionally(new RuntimeException("Failed to fetch skin data: "+data.reason));
					skin.texture = data.textures.raw.value;
					skin.signature = data.textures.raw.signature;
					
					result.complete(skin);
				});
		
		return result;
	}

	/**
	 * Fetches the texture and signature of a skin based on an OfflinePlayer.
	 * 
	 * @param player The player wearing the skin
	 * @return TagSkin
	 */
	public static CompletableFuture<TagSkin> from(OfflinePlayer player) {
		if (player.isOnline()) return from(player.getPlayer());
		else return from(player.getUniqueId());
	}
	
	/**
	 * Fetches the texture and signature of a skin based on a Player.
	 * Notice: Unlike the other methods, this method will not send requests to mojang's api.
	 *
	 * @param player The player wearing the skin
	 * @return TagSkin
	 */
	public static CompletableFuture<TagSkin> from(Player player) {
		User user = TagPlayer.from(player).getPacketUser();
		TagSkin skin = new TagSkin();
		
		List<TextureProperty> properties = user.getProfile().getTextureProperties();
		TextureProperty property = properties.size() > 0 ? properties.get(0) : null;
		
		if (property == null) return TagSkin.from(player.getName());
		
		skin.texture = property.getValue();
		skin.signature = property.getSignature();
		
		return CompletableFuture.completedFuture(skin);
	}

	/**
	 * Fetches the texture and signature of a skin based on a UUID.
	 * 
	 * @apiNote This method should be run asynchronously, Minecraft will send a
	 *          request to Mojang's API if the user is not cached.
	 * @param uuid The uuid of the player wearing the skin
	 * @return TagSkin
	 */
	public static CompletableFuture<TagSkin> from(UUID uuid) {
		CompletableFuture<TagSkin> result = new CompletableFuture<>();
		TagSkin skin = new TagSkin();
		
		Unirest.get(MOJANG_API+uuid.toString())
			.asObjectAsync(AccountModel.class)
			.thenAccept(response -> {
				AccountModel data = response.getBody();
				if (!response.isSuccess()) result.completeExceptionally(new RuntimeException("Failed to fetch skin data: "+data.reason));
				skin.texture = data.textures.raw.value;
				skin.signature = data.textures.raw.signature;
				
				result.complete(skin);
		});
		
		return result;
	}

//	/**
//	 * Fetches the texture and signature of a skin based on a player's username.
//	 *
//	 * @apiNote This method should be run asynchronously, this is to fetch the
//	 *          texture and signature
//	 * @param url The url to get a skin from
//	 * @return TagSkin
//	 */
//	public static TagSkin from(URL url) {
//		return null;
//	}
	
	public String texture;
	public String signature;

	public TextureProperty toProperty() {
		return new TextureProperty("textures", texture, signature);
	}
}
