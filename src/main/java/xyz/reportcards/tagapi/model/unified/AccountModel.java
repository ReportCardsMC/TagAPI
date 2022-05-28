package xyz.reportcards.tagapi.model.unified;

import xyz.reportcards.tagapi.model.unified.textures.RawTextureModel;

import java.util.UUID;

public class AccountModel {
    
    public int code = 200;
    public String error = null;
    public String reason = null;
    
    private String uuid;
    public String username;
    public TexturesModel textures;
    public String created_at;
    
    public UUID getUuid() {
        return UUID.fromString(uuid);
    }
    
}
