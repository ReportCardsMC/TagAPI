package xyz.reportcards.test.tagapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.reportcards.tagapi.TagAPI;
import xyz.reportcards.tagapi.TagPlayer;
import xyz.reportcards.tagapi.TagSkin;

public class SkinCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§c/skin <player>");
            return true;
        } else if (args[0].equalsIgnoreCase("update")) {
            TagPlayer.from(player).update();
            player.sendMessage("§aUpdated");
        } else if (args[0].equalsIgnoreCase("refresh")) {
            TagPlayer.from(player).refresh();
            player.sendMessage("§aRefreshed");
        } else {
            TagAPI.setSkin(player, args[0]);
            player.sendMessage("§aDone :)");
        }
        
        return true;
    }
    
}
