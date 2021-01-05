package me.nanigans.pandoracombattag.Commands;


import me.nanigans.pandoracombattag.CombatTag.Combat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Tag implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equals("tag")){
            if(args.length > 0) {
                Player player = Bukkit.getPlayerExact(args[0]);
                if(player != null){

                    new Combat(player).startCounting();
                    sender.sendMessage(ChatColor.GREEN+"Player tagged!");

                    return true;

                }else{
                    sender.sendMessage(ChatColor.RED+"Invalid player");
                }
            }else{
                sender.sendMessage(ChatColor.RED+"Please specify a player");
                return true;
            }

        }

        return false;
    }
}
