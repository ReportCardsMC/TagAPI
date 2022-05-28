package xyz.reportcards.tagapi.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class PacketUtils {
    
    public static Location getPacketLocation(org.bukkit.Location location) {
        return new Location(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    public static void broadcast(PacketWrapper<?> packet) {
        broadcast(packet, Collections.emptyList());
    }
    
    public static void broadcast(PacketWrapper<?> packet, List<Player> ignore) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (!ignore.contains(player)) sendPacket(player, packet);
    }
    
    public static void sendPacket(Player player, PacketWrapper<?> packet) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
    
}
