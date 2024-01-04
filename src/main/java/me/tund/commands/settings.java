package me.tund.commands;

import fr.mrmicky.fastboard.FastBoard;
import me.tund.main.BuildAttack;
import me.tund.utils.TeamHandler;
import me.tund.utils.eventUtils.VaroHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class settings implements CommandExecutor {
    public VaroHandler varo;
    private BuildAttack plugin;

    public settings(BuildAttack plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("settings") || cmd.getName().equalsIgnoreCase("sett")) {
            if (!(sender instanceof Player))
                return true;
            Player p = (Player) sender;
            if (args.length < 1) {

                if (!p.isOp()) {
                    p.sendMessage(ChatColor.RED + "[Error] Sorry, aber nur ein Admin kann diesen Command ausführen!");
                    return true;
                }
                p.sendMessage(ChatColor.GREEN + "[Settings-Help]" + "\n" +
                        ChatColor.STRIKETHROUGH + "                                                     ");
                p.sendMessage(ChatColor.GREEN + "/sett claimMode [Off/Chest/All]: Setzt den Claim-Mode");
                p.sendMessage(ChatColor.GREEN + "/sett moneyMultiplier [Menge%]: Setzt wie viel Geld Spieler bekommen.");
                p.sendMessage(ChatColor.GREEN + "/sett toggleEconomy: Aktiviert/Deaktiviert das Economy-System.");
                p.sendMessage(ChatColor.GREEN + "/sett toggleEvent: Aktiviert/Deaktiviert das momentane Event!");
                p.sendMessage(ChatColor.GREEN + "/sett tablistHeader [String]: Setzt die Tablist-Überschrift!");
                p.sendMessage(ChatColor.GREEN + "/sett tablistFooter [String]: Setzt die Tablist-Unterschrift!");
                p.sendMessage(ChatColor.GREEN + "/sett togglePublicPVP: Aktiviert/Deaktiviert PVP-Schutz!");
                p.sendMessage(ChatColor.GREEN + "/sett setItems: True/False");
                p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                                     ");
                return true;
            }
            String argument = args[0];
            if (argument.equalsIgnoreCase("claimMode") || argument.equalsIgnoreCase("cm")) {
                return toggleChunkClaim(p, args);
            } else if (argument.equalsIgnoreCase("moneyMultiplier") || argument.equalsIgnoreCase("mm")) {
                return moneyMultiplier(p, args);
            } else if (argument.equalsIgnoreCase("toggleEconomy") || argument.equalsIgnoreCase("te")) {
                return ecoToggle(p);
            } else if (argument.equalsIgnoreCase("toggleEvent") || argument.equalsIgnoreCase("tev")) {
                return eventToggle(p);
            } else if (argument.equalsIgnoreCase("tablistHeader") || argument.equalsIgnoreCase("th")) {
                return setTablistHeader(p, args);
            } else if (argument.equalsIgnoreCase("tablistFooter") || argument.equalsIgnoreCase("tf")) {
                return setTablistFooter(p, args);
            } else if (argument.equalsIgnoreCase("togglePublicPVP") || argument.equalsIgnoreCase("tpp")) {
                return togglePublicPvp(p);
            } else if (argument.equalsIgnoreCase("toggleTNT") || argument.equalsIgnoreCase("ttnt")) {
                return toggleTNT(p);
            } else if (argument.equalsIgnoreCase("setItems") || argument.equalsIgnoreCase("SI")) {
                return setITEMS(p, args);
            } else if (argument.equalsIgnoreCase("setChangeTablist") || argument.equalsIgnoreCase("sCT")) {
                return toggleChangeTablist(p);
            } else if (argument.equalsIgnoreCase("toggleTimer") || argument.equalsIgnoreCase("tT")) {
                return toggleTimer(p);
            }


        }


        return true;
    }

    public boolean toggleTimer(Player sender) {
        if (!plugin.timers.getConfig().contains("Enabled-" + sender.getUniqueId())) {
            plugin.timers.getConfig().set("Enabled-" + sender.getUniqueId(), true);
            plugin.timers.saveConfig();
            return true;
        }
        boolean toggle = plugin.timers.getConfig().getBoolean("Enabled-" + sender.getUniqueId());
        if (!toggle)
            sender.sendMessage(ChatColor.GREEN + "[Settings] Der Timer wird dir nun angezeigt.");
        else
            sender.sendMessage(ChatColor.GREEN + "[Settings] Der Timer wird dir nun nicht mehr angezeigt.");

        plugin.timers.getConfig().set("Enabled-" + sender.getUniqueId(), !toggle);
        plugin.timers.saveConfig();
        return true;
    }

    public boolean toggleChunkClaim(Player sender, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        if (!plugin.settings.getConfig().contains("Mode"))
            plugin.settings.getConfig().set("Mode", "Chest-Only");

        if (args.length < 2) {
            sender.sendMessage(ChatColor.GREEN + "[Settings] Claim-Mode ist gesetzt auf: " + plugin.settings.getConfig().getString("Mode"));
        } else {
            String mode = args[1];
            if (mode.equalsIgnoreCase("chest")) {
                plugin.settings.getConfig().set("Mode", "Chest-Only");
                sender.sendMessage(ChatColor.GREEN + "[Settings] Claim-Mode gesetzt auf: " + plugin.settings.getConfig().getString("Mode"));
            } else if (mode.equalsIgnoreCase("off")) {
                plugin.settings.getConfig().set("Mode", "Off");
                sender.sendMessage(ChatColor.GREEN + "[Settings] Claim-Mode gesetzt auf: " + plugin.settings.getConfig().getString("Mode"));
            } else if (mode.equalsIgnoreCase("all")) {
                plugin.settings.getConfig().set("Mode", "Chunk");
                sender.sendMessage(ChatColor.GREEN + "[Settings] Claim-Mode gesetzt auf: " + plugin.settings.getConfig().getString("Mode"));
            }
            plugin.settings.saveConfig();
        }
        return true;
    }

    public boolean moneyMultiplier(Player sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Falsche Command-Eingabe!");
            return true;
        }

        String amountS = args[1];
        float amount;

        try {
            amountS = amountS.replaceAll(",", ".");
            if (amountS.contains("%")) {
                amountS.replaceAll("%", "");
                amount = Float.parseFloat(amountS);
                amount = amount / 100;
            } else {
                amount = Float.parseFloat(amountS);
            }

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Der angegebene Wert enthält keine Zahl!");
            return true;
        }
        plugin.settings.getConfig().set("MoneyMultiplier", amount);
        sender.sendMessage(ChatColor.GOLD + "[Settings] Der Money-Multiplier wurde auf " + amount * 100 + "% gesetzt!");

        return true;
    }

    public boolean ecoToggle(Player sender) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        boolean toggle;
        if (plugin.settings.getConfig().contains("Economy"))
            toggle = (boolean) plugin.settings.getConfig().get("Economy");
        else
            toggle = false;

        plugin.settings.getConfig().set("Economy", !toggle);
        if (!toggle)
            sender.sendMessage(ChatColor.GOLD + "[Settings] Die Economy ist nun aktiviert!");
        else
            sender.sendMessage(ChatColor.GOLD + "[Settings] Die Economy ist nun deaktiviert!");
        plugin.settings.saveConfig();
        return true;
    }

    public boolean eventToggle(Player sender) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        boolean toggle;
        if (plugin.settings.getConfig().contains("Event.Enable"))
            toggle = (boolean) plugin.settings.getConfig().get("Event.Enable");
        else
            toggle = false;

        plugin.settings.getConfig().set("Event.Enable", !toggle);
        if (!toggle) {
            /* TODO Move to own plugin
            sender.sendMessage(ChatColor.GOLD+"[Settings] Das Event ist nun aktiviert!");
            Bukkit.broadcastMessage(ChatColor.GOLD+"[Event] Varo hat nun begonnen! Ihr seit für 5 Minuten unverwundbar durch Spielereinwirkung.");
            Bukkit.broadcastMessage(ChatColor.GOLD+"[Event] Viel Glück.");
            plugin.settings.getConfig().set("Event.Invincible", true);
            plugin.settings.saveConfig();
            for(Player p : Bukkit.getOnlinePlayers()){
                plugin.score.addPlayerScoreboard(p);
            }
            new BukkitRunnable() {
                int count = 300;
                @Override
                public void run() {
                    if(count < 1){
                        Bukkit.broadcastMessage(ChatColor.GOLD+"[Event] Die unverwundbarkeit ist nun zu Ende.");
                        Bukkit.broadcastMessage(ChatColor.GOLD+"[Event] Jetzt wirds ernst.");
                        plugin.settings.getConfig().set("Event.Invincible", false);
                        plugin.settings.saveConfig();
                        varo = new VaroHandler(plugin);
                        plugin.pm.registerEvents(varo, plugin);
                        this.cancel();
                    }
                    for(Player p : Bukkit.getOnlinePlayers()){
                        FastBoard board = plugin.score.getBoardByPlayer(p);
                        plugin.score.updateBoardCounter(board, count, 0);
                    }
                    count--;
                }
            }.runTaskTimerAsynchronously(plugin, 0, 20L); */

        } else {
            /* TODO Move to own plugin
            for(Player p : Bukkit.getOnlinePlayers()){
                plugin.score.removePlayerScoreboard(p);
                plugin.score.addPlayerScoreboard(p);
            }
            sender.sendMessage(ChatColor.GOLD+"[Settings] Das Event ist nun deaktiviert!");
            */
        }
        plugin.settings.saveConfig();
        return true;
    }

    public boolean setTablistHeader(Player sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        TeamHandler teams = new TeamHandler();
        String message = "";
        for (int x = 1; x < args.length; x++) {
            String add = args[x];
            message = message + add + " ";
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setPlayerListHeader(teams.format(message));
        }
        sender.sendMessage(ChatColor.GOLD + "[Settings] Der Tablist-Header ist nun: " + teams.format(message));
        plugin.settings.getConfig().set("Tablist-Header", message);
        plugin.settings.saveConfig();
        return true;
    }

    public boolean setTablistFooter(Player sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        TeamHandler teams = new TeamHandler();
        String message = "";
        for (int x = 1; x < args.length; x++) {
            String add = args[x];
            message = message + add + " ";
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setPlayerListFooter(teams.format(message));
        }
        sender.sendMessage(ChatColor.GOLD + "[Settings] Der Tablist-Footer ist nun: " + teams.format(message));
        plugin.settings.getConfig().set("Tablist-Footer", message);
        plugin.settings.saveConfig();
        return true;
    }

    public boolean togglePublicPvp(Player sender) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        boolean toggle = true;
        if (plugin.settings.getConfig().contains("Public-PVP")) {
            toggle = plugin.settings.getConfig().getBoolean("Public-PVP");
        }
        plugin.settings.getConfig().set("Public-PVP", !toggle);
        plugin.settings.saveConfig();
        if (!toggle) {
            sender.sendMessage(ChatColor.GOLD + "[Settings] Public-PVP ist nun aktiviert!");
            for (Team teams : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                teams.setSuffix("");
            }

            Bukkit.broadcastMessage(ChatColor.RED + "[INFO] Public-PVP ist nun aktiviert!");
        } else {
            sender.sendMessage(ChatColor.GOLD + "[Settings] Public-PVP ist nun deaktiviert!");

            List<String> allTeams = new ArrayList<>();
            for (Team teams : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                allTeams.add(teams.getName());
            }
            for (String b : allTeams) {
                if (plugin.getHandler().hasPvpEnabled(b))
                    plugin.getTeams().updateSuffix(b, ChatColor.AQUA + "[" + ChatColor.RED + "PVP" + ChatColor.AQUA + "]");
            }

            Bukkit.broadcastMessage(ChatColor.RED + "[INFO] Public-PVP ist nun deaktiviert!");
        }
        return true;
    }

    public boolean toggleChangeTablist(Player sender) {
        if (!sender.isOp() || !sender.getName().equals("Tund_101_HD")) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        boolean toggle = plugin.settings.getConfig().getBoolean("Change-Tablist");
        if (!toggle)
            sender.sendMessage(ChatColor.GOLD + "[Settings] Change-Tablist ist nun an!");
        else {
            sender.sendMessage(ChatColor.GOLD + "[Settings] Change-Tablist ist nun an!");
        }
        plugin.settings.getConfig().set("Change-Tablist", !toggle);
        plugin.settings.saveConfig();
        return true;
    }

    public boolean toggleTNT(Player sender) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        boolean toggle = true;
        if (plugin.settings.getConfig().contains("TNT-Damage")) {
            toggle = plugin.settings.getConfig().getBoolean("TNT-Damage");
        }
        plugin.settings.getConfig().set("TNT-Damage", !toggle);
        if (!toggle)
            sender.sendMessage(ChatColor.GOLD + "[Settings] TNT-Schaden auf geclaimten Chunks ist nun aus!");
        else
            sender.sendMessage(ChatColor.GOLD + "[Settings] TNT-Schaden auf geclaimten Chunks ist nun an!");
        return true;
    }

    public boolean setITEMS(Player sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "true":
                plugin.settings.getConfig().set("C-Items", true);
                sender.sendMessage(ChatColor.GOLD + "[Settings] Custom-Items wurden angeschalten!");
                break;
            case "false":
                plugin.settings.getConfig().set("C-Items", false);
                sender.sendMessage(ChatColor.GOLD + "[Settings] Custom-Items wurden ausgeschalten!");
                break;
            default:
                if (!plugin.settings.getConfig().contains("C-Items")) {
                    plugin.settings.getConfig().set("C-Items", false);
                    sender.sendMessage(ChatColor.GREEN + "[Settings] Custom-Items sind momentan gesetzt auf: false (aus)!");
                    break;
                }
                sender.sendMessage(ChatColor.GREEN + "[Settings] Custom-Items sind mometan gesetzt auf: " + plugin.getConfig().get("C-Items") + "!");
                break;
        }
        plugin.settings.saveConfig();
        return true;
    }

    public boolean setRules(Player sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, aber nur ein Admin darf das tun!");
            return true;
        }
        if (!plugin.settings.getConfig().contains("C-Rules")) {
            plugin.settings.getConfig().set("C-Rules", new ArrayList<String>());
            plugin.settings.saveConfig();
        }
        String arg = args[0];
        String arg2 = args[1];
        List<String> rules = plugin.settings.getConfig().getStringList("C-Rules");
        switch (arg.toLowerCase()) {
            case "add":
                String msg = "";
                for (int x = 1; x < args.length; x++) {
                    msg = msg + " " + args[x];
                }
                rules.add((rules.size() + 1) + ". " + msg);
                break;
            case "removeindex":
                int index = 0;
                try {
                    index = Integer.parseInt(arg2);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.DARK_RED + "[Error] Da du keine Zahl angegeben hast, hat sich nichts verändert!");
                }
                if (index == 0)
                    break;
                index--;

                if (index > rules.size()) {
                    sender.sendMessage(ChatColor.DARK_RED + "[Error] Sorry, but there's no rule at that index!");
                    break;
                }

                List<String> copy = new ArrayList<>();
                for (int y = index + 1; y < rules.size(); y++) {
                    String[] ary = rules.get(y).split(".");
                    ary[0] = String.valueOf(y);

                    copy.add(ary[0] + "." + ary[1]);
                }
                rules.remove(index);
                rules.addAll(copy);
                break;
            case "show":
                sender.sendMessage(ChatColor.GREEN + "[Settings] So sehen die Regeln auf diesem Server momentan aus: ");
                for (String s : rules)
                    sender.sendMessage(s);
                break;
            case "change":

                break;
            case "remove":
                break;

        }
        return true;
    }


}

