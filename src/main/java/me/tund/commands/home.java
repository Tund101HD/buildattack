package me.tund.commands;

import me.tund.main.BuildAttack;
import me.tund.utils.ErrorObject;
import me.tund.utils.data.DataHandler;
import me.tund.utils.gui.HomeGui;
import me.tund.utils.homes.HomesHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class home implements CommandExecutor {
    private HomesHandler homes;
    private BuildAttack plugin;
    private HomeGui gui;

    public home(BuildAttack plugin, HomesHandler homes) {
        this.plugin = plugin;
        this.homes = homes;
        this.gui = new HomeGui(plugin, homes);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length < 1) {
            return showHomes((Player) sender, homes, plugin);
        } else {
            String argument = args[0];
            if (argument.equalsIgnoreCase("clan"))
                return clanHomes((Player) sender, homes, plugin.getHandler(), args);
            else if (argument.equalsIgnoreCase("create") || argument.equalsIgnoreCase("c")) {
                return createHome((Player) sender, homes, args);
            } else if (argument.equalsIgnoreCase("delete") || argument.equalsIgnoreCase("d")) {
                return deleteHomeWithName((Player) sender, homes, args);
            } else {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
                    return true;
                }

            }
        }

        return false;
    }

    public boolean showHomes(Player sender, HomesHandler homes, BuildAttack plugin) {
        sender.openInventory(gui.getHomeInventory(sender.getUniqueId(), homes, plugin));
        return true;
    }

    public boolean showClanHomes(Player sender, HomesHandler homes, BuildAttack plugin) {

        return true;
    }

    public boolean createHome(Player sender, HomesHandler homes, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Bitte gib einen Namen für das Home ein!");
            return true;
        }
        String homeName = args[1];
        ErrorObject e = homes.createHome(sender.getUniqueId(), sender.getLocation().add(0.5, 0, 0.5), homeName);
        if (!e.isSuccess()) {
            sender.sendMessage(ChatColor.RED + "[Error] " + e.getMessage());
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "[Homes] " + e.getMessage());
        return true;
    }

    public boolean deleteHomeWithName(Player sender, HomesHandler homes, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Bitte gib einen Namen für das Home ein!");
            return true;
        }
        String homeName = args[1];
        ErrorObject e = homes.deleteHomeByName(sender.getUniqueId(), homeName);
        if (!e.isSuccess()) {
            sender.sendMessage(ChatColor.RED + "[Error] " + e.getMessage());
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "[Homes] " + e.getMessage());
        return true;
    }

    public boolean clanHomes(Player sender, HomesHandler homes, DataHandler handler, String[] args) {
        if (args.length < 2) return showClanHomes(sender, homes, plugin);
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst einen Namen eingeben!");
            return true;
        }
        String operation = args[1];
        String name = args[2];
        if (!handler.isInClan(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du bist in keinem Clan!");
            return true;
        }
        if (operation.equalsIgnoreCase("create") || operation.equalsIgnoreCase("c")) {
            ErrorObject e = homes.createClanHome(handler.getClanNameByList(sender.getUniqueId()), sender.getLocation().add(0.5, 0, 0.5), name);
            if (!e.isSuccess()) {
                sender.sendMessage(ChatColor.RED + "[Error] " + e.getMessage());
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "[Homes] " + e.getMessage());
            List<UUID> l = handler.getClanMembers(handler.getClanNameByList(sender.getUniqueId()));
            Location loc = sender.getLocation().add(0.5, 0, 0.5);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (handler.isInClan(p.getUniqueId())) {
                    if (l.contains(p.getUniqueId()) && p != sender) {
                        p.sendMessage(ChatColor.GREEN + "[Clans] " + sender.getName() + " hat ein Clan-Home bei X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ() + " erstellt!");
                    }
                }
            }
        } else {
            ErrorObject e = homes.deleteClanHomeByName(handler.getClanNameByList(sender.getUniqueId()), name);
            if (!e.isSuccess()) {
                sender.sendMessage(ChatColor.RED + "[Error] " + e.getMessage());
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "[Homes] " + e.getMessage());
            List<UUID> l = handler.getClanMembers(handler.getClanNameByList(sender.getUniqueId()));
            Location loc = homes.getClanHomeByName(handler.getClanNameByList(sender.getUniqueId()), name);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (handler.isInClan(p.getUniqueId())) {
                    if (l.contains(p.getUniqueId()) && p != sender) {
                        p.sendMessage(ChatColor.GREEN + "[Clans] " + sender.getName() + " hat ein Clan-Home bei X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ() + " gelöscht!");
                    }
                }
            }
        }
        return true;
    }

    public boolean homesAdmin(Player sender, String[] args, HomesHandler homes, DataHandler handler) {


        return true;
    }

}
