package xyz.reportcards.tagapi.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.reportcards.tagapi.TagSkin;

public class PlayerNametagUpdateEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean isCancelled;
    private final Player player;
    private final String nametag;

    public PlayerNametagUpdateEvent(Player player, String nametag) {
        this.isCancelled = false;
        this.player = player;
        this.nametag = nametag;
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public Player getPlayer() {
        return player;
    }

    public String getNametag() {
        return nametag;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
