package xyz.reportcards.tagapi;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import org.bukkit.entity.Player;
import xyz.reportcards.tagapi.model.unified.AccountModel;

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
					if (data.code != 200) result.completeExceptionally(new RuntimeException("Failed to fetch skin data: "+data.reason));
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
		CompletableFuture<TagSkin> result = new CompletableFuture<>();
		
		if (player.isOnline()) result.complete(from(player.getPlayer()));
		else return from(player.getUniqueId());
		
		return result;
	}
	
	/**
	 * Fetches the texture and signature of a skin based on a Player.
	 * Notice: Unlike the other methods, this method will not send requests to mojang's api.
	 *
	 * @param player The player wearing the skin
	 * @return TagSkin
	 */
	public static TagSkin from(Player player) {
		WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
		TagSkin skin = new TagSkin();
		
		if (!profile.getProperties().containsKey("textures")) return null;
		WrappedSignedProperty property = profile.getProperties().get("textures").iterator().next();
		
		skin.texture = property.getValue();
		skin.texture = property.getSignature();
		
		return skin;
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
				if (data.code != 200) result.completeExceptionally(new RuntimeException("Failed to fetch skin data: "+data.reason));
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

	public WrappedSignedProperty toProperty() {
		return new WrappedSignedProperty("textures", texture, signature);
	}
}
