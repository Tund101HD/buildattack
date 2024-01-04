package me.tund.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.tund.main.BuildAttack;
import me.tund.utils.data.DataHandler;
import me.tund.utils.TeamHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class clans implements CommandExecutor {
    private BuildAttack plugin;
    private TeamHandler teams;
    private DataHandler handler;


    public clans(BuildAttack plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clans") || cmd.getName().equalsIgnoreCase("clan")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            teams = plugin.getTeams();
            handler = plugin.getHandler();
            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "[Error] Das ist kein valider Command! (/clans help)");
                return true;
            }
            String argument = args[0];
            if (argument.equalsIgnoreCase("create") || argument.equalsIgnoreCase("new")) {
                return create(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("remove") || argument.equalsIgnoreCase("delete")) {
                return delete(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("rename")) {
                return rename(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("giveLeader") || argument.equalsIgnoreCase("leader")) {
                return assignLeader(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("kick") || argument.equalsIgnoreCase("kickPlayer")) {
                return kick(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("join")) {
                return join(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("invite")) {
                return invite(p, args, teams, handler);
            } else if (argument.equals("followinv")) {
                return followinv(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("togglePrivacy") || argument.equalsIgnoreCase("togpriv")) {
                return togglePriv(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("leave")) {
                return leaveClan(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("togglepvp") || argument.equalsIgnoreCase("togpvp")) {
                return toggleClanPvp(p, args, teams, handler);
            } else if (argument.equalsIgnoreCase("chat") || argument.equalsIgnoreCase("c")) {
                return chat(p, args, teams, handler);
            } else {
                p.sendMessage(ChatColor.GREEN + "[Clans-Help]" + "\n" +
                        ChatColor.STRIKETHROUGH + "                                                     ");
                p.sendMessage(ChatColor.GREEN + "/clan create (Name): Erstellt einen Clan.");
                p.sendMessage(ChatColor.GREEN + "/clan remove (Name): Löscht einen Clan.");
                p.sendMessage(ChatColor.GREEN + "/clan rename (Rename): Benennt einen Clan um.");
                p.sendMessage(ChatColor.GREEN + "/clan leader (Spieler): Übergibt die Leitung eines Clans.");
                p.sendMessage(ChatColor.GREEN + "/clan kick (Spieler): Kickt einen Spieler aus dem Clan.");
                p.sendMessage(ChatColor.GREEN + "/clan join (Name): Tritt einem öffentlichen Clan bei.");
                p.sendMessage(ChatColor.GREEN + "/clan leave: Verlasse einen Clan.");
                p.sendMessage(ChatColor.GREEN + "/clan chat: Schreibe deinem Clan.");
                p.sendMessage(ChatColor.GREEN + "/clan invite (Spieler): Lade einen Spieler in einen Clan ein.");
                p.sendMessage(ChatColor.GREEN + "/clan togpriv: Ändert die Öffentlichkeit deines Clans.");
                p.sendMessage(ChatColor.GREEN + "/clan togpvp: Aktiviert/Deaktiviert den PVP-Modus des Clans.");
                p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                                     ");
                return true;
            }
        }

        return false;
    }

    public boolean chat(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber deine Nachricht ist zu kurz.");
            return true;
        }
        if (!handler.isInClan(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du bist in keinem Clan!");
            return true;
        }
        String message = "";
        for (int i = 1; i < args.length; i++) {
            message += args[i] + " ";
        }
        String clanname = handler.getClanNameByList(sender.getUniqueId());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (handler.isInClan(p.getUniqueId()) && handler.getClanNameByList(p.getUniqueId()).equals(clanname))
                p.sendMessage(ChatColor.GOLD + "[Clan] " + p.getName() + ": " + teams.format(message));
        }
        return true;
    }

    public boolean create(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst einen Namen eingeben!");
            return true;
        }
        String clanname = teams.format(args[1]);
        String nameWOColors = ChatColor.stripColor(clanname);
        if (nameWOColors.length() > 16) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Name ist zu lang! (max. 16)");
            return true;
        }
        if (handler.playerHasClan(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast bereits einen Clan!");
            return true;
        }
        handler.addClan(clanname, sender.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + "[Clans] Du hast den clan " + clanname + ChatColor.GREEN + " erstellt!" + "\n" +
                "Gib /clans help ein, um Hilfe mit den Commands zu bekommen!");
        plugin.getLogger().log(Level.INFO, "Clan " + clanname + " has been created! Leader: " + sender.getName());
        return true;
    }

    public boolean delete(Player sender, String[] args, TeamHandler teams, DataHandler handler) {

        if (args.length < 2) { // /clans delete (own Clan, no Admin)
            if (!handler.playerHasClan(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast keinen Clan!");
                return true;
            }
            String clanname = handler.getPlayerClan(sender.getUniqueId());
            if (!handler.getClanLeader(clanname).equals(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst der Besitzer des Clans sein!");
                return true;
            }
            List<UUID> list = handler.getClanMembers(clanname);
            for (UUID players : list) {
                Player p = Bukkit.getPlayer(players);
                if (p != null && p.isOnline() && p != sender) {
                    p.sendMessage(ChatColor.DARK_RED + "[Clans] Dein Clan wurde aufgelöst!");
                }
            }

            handler.removeClan(clanname);
            sender.sendMessage(ChatColor.RED + "[Clans] Du hast den Clan aufgelöst!");

        } else if (args.length > 1) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "[Error] Du musst ein Admin sein um das auszuführen!");
                return true;
            }
            String clanname = args[1];
            if (!handler.isExistingClan(clanname)) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber diesen Clan gibt es nicht!");
                return true;
            }
            List<UUID> list = handler.getClanMembers(clanname);

            for (UUID players : list) {
                Player p = Bukkit.getPlayer(players);
                if (p != null && p.isOnline()) {
                    p.sendMessage(ChatColor.DARK_RED + "[Clans] Dein Clan wurde von einem Admin aufgelöst!");
                }

            }
            handler.removeClan(clanname);
            sender.sendMessage(ChatColor.GREEN + "[Admins] Der Clan " + teams.format(clanname) + " wurde aufgelöst!");
        }
        return true;
    }

    public boolean rename(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Bitte gib einen neuen Namen ein!");
            return true;
        }
        if (args.length == 2) { // normal rename, no Admin
            String clanname = args[1];
            if (!handler.playerHasClan(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast keinen Clan!");
                return true;
            }
            String original = handler.getPlayerClan(sender.getUniqueId());
            if (!handler.getClanLeader(original).equals(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst der Besitzer des Clans sein!");
                return true;
            }

            List<UUID> members = handler.getClanMembers(original);
            members.remove(sender.getUniqueId());
            handler.removeClan(original);
            handler.addClan(clanname, sender.getUniqueId());
            for (UUID players : members) {
                handler.addPlayerToClan(clanname, players);
                if (Bukkit.getPlayer(players) != null && Bukkit.getPlayer(players).isOnline())
                    teams.addPlayerTeam(Bukkit.getPlayer(players), clanname);

                Player p = Bukkit.getPlayer(players);
                if (p != null && p.isOnline() && p != sender) {
                    p.sendMessage(ChatColor.GOLD + "[Clans] Dein Clan wurde umbenannt zu " + teams.format(clanname) + ChatColor.GREEN + "!");
                }
            }
            sender.sendMessage(ChatColor.GREEN + "[Clans] DU hast deinen Clan zu " + teams.format(clanname) + ChatColor.GREEN + " umbenannt!");
        } else if (args.length > 2) { // Admin Command Execution
            String clanname = args[1];
            String rename = args[2];

            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "[Error] Du musst ein Admin sein um das auszuführen!");
                return true;
            }

            if (!handler.isExistingClan(clanname)) {
                sender.sendMessage(ChatColor.RED + "[Error] Dieser Clan existiert nicht!");
                return true;
            }

            List<UUID> members = handler.getClanMembers(clanname);
            UUID leader = handler.getClanLeader(clanname);
            members.remove(leader);
            handler.removeClan(clanname);
            handler.addClan(rename, leader);
            for (UUID players : members) {
                handler.addPlayerToClan(clanname, players);
                Player p = Bukkit.getPlayer(players);
                if (p != null && p.isOnline()) {
                    p.sendMessage(ChatColor.GOLD + "[Clans] Dein Clan wurde umbenannt zu " + teams.format(rename) + ChatColor.GREEN + "!");
                }
            }
            sender.sendMessage(ChatColor.GREEN + "[Admins] Du hast den Clan zu " + teams.format(rename) + ChatColor.GREEN + " umbenannt!");

            Player Leader = Bukkit.getPlayer(leader);
            if (Leader != null && Leader.isOnline()) {
                Leader.sendMessage(ChatColor.DARK_RED + "Dein Clan wurde von einem Admin zu " + teams.format(rename) + ChatColor.DARK_RED + " umbenannt!");
            }

        }
        return true;
    }

    public boolean assignLeader(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Bitte gib den Namen des Spielers ein!");
            return true;
        }
        if (args.length == 2) { // Leader Command Input
            if (!handler.playerHasClan(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Du hast keinen Clan!");
                return true;
            }
            String clanname = handler.getPlayerClan(sender.getUniqueId());
            if (!handler.getClanLeader(clanname).equals(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst der Besitzer eines Clans sein!");
                return true;
            }
            String name = args[1];
            Player target = Bukkit.getPlayer(name);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler muss online sein!");
                return true;
            }
            if (target == sender) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du kannst dir nicht selber den Besitz geben!");
                return true;
            }
            if (!handler.getPlayerClan(target.getUniqueId()).equals(clanname)) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler muss in deinem Clan sein!");
                return true;
            }

            handler.updateLeader(clanname, target.getUniqueId());
            sender.sendMessage(ChatColor.GREEN + "[Clans] Du hast den Besitz an " + target.getName() + " abgegeben!");
            target.sendMessage(ChatColor.GOLD + "[Clans] Dir wurde der Besitz des Clans " + teams.format(clanname) + " gegeben!");
            List<UUID> list = handler.getClanMembers(clanname);
            for (UUID players : list) {
                Player p = Bukkit.getPlayer(players);
                if (p != null && p.isOnline() && p != sender && p != target)
                    p.sendMessage(ChatColor.GREEN + "[Clans] Der Besitz deines Clans wurde an " + target.getName() + " übergeben!");
            }
        } else if (args.length > 2) { // Admin Command Input

            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "[Error] Du musst ein Admin sein um das auszuführen!");
            }
            String clanname = args[1];
            if (!handler.isExistingClan(clanname)) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Clan existiert nicht!");
                return true;
            }

            String name = args[2];
            Player target = Bukkit.getPlayer(name);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler muss online sein!");
                return true;
            }
            if (target == sender && handler.playerHasClan(sender.getUniqueId()) && !handler.getPlayerClan(sender.getUniqueId()).equals(clanname)) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast bereits einen Clan!");
                return true;
            }
            if (target != sender && handler.playerHasClan(target.getUniqueId()) && !handler.getPlayerClan(target.getUniqueId()).equals(clanname)) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Spieler hat bereits einen Clan!");
                return true;
            }

            if (handler.playerHasClan(target.getUniqueId())) {
                if (handler.isClanLeader(clanname, target.getUniqueId())) {
                    sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Clan gehört bereits " + target.getName() + "!");
                    return true;
                }
                Player leader = Bukkit.getPlayer(handler.getClanLeader(clanname));
                handler.updateLeader(clanname, target.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "[Clans] Du hast den Besitz an " + target.getName() + " abgegeben!");
                target.sendMessage(ChatColor.GOLD + "[Clans] Dir wurde der Besitz des Clans " + teams.format(clanname) + " gegeben!");
                List<UUID> list = handler.getClanMembers(clanname);
                for (UUID players : list) {
                    Player p = Bukkit.getPlayer(players);
                    if (p != null && p.isOnline() && p != sender && p != target)
                        p.sendMessage(ChatColor.GREEN + "[Clans] Der Besitz deines Clans wurde an " + target.getName() + " übergeben!");
                }
                leader.sendMessage(ChatColor.DARK_RED + "[Clans] Ein Admin hat den Besitz deines Clans geändert!");
            } else {
                Player leader = Bukkit.getPlayer(handler.getClanLeader(clanname));
                handler.updateLeader(clanname, target.getUniqueId());
                handler.addPlayerToClan(clanname, target.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "[Clans] Du hast den Besitz an " + target.getName() + " abgegeben!");
                target.sendMessage(ChatColor.GOLD + "[Clans] Dir wurde der Besitz des Clans " + teams.format(clanname) + ChatColor.GREEN + " gegeben!");
                List<UUID> list = handler.getClanMembers(clanname);
                for (UUID players : list) {
                    Player p = Bukkit.getPlayer(players);
                    if (p != null && p.isOnline() && p != sender && p != target)
                        p.sendMessage(ChatColor.GREEN + "[Clans] Der Besitz deines Clans wurde an " + target.getName() + " übergeben!");
                }
                leader.sendMessage(ChatColor.DARK_RED + "[Clans] Ein Admin hat den Besitz deines Clans geändert!");
            }
            sender.sendMessage(ChatColor.GREEN + "[Admins] Du hast den Besitz des Clans " +
                    teams.format(clanname) + ChatColor.GREEN + " an " + target.getName() + " gegeben!");
        }
        return true;
    }

    public boolean kick(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Bitte gib den Namen des Spielers an!");
            return true;
        }

        if (!handler.playerHasClan(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du besitzt keinen Clan!");
            return true;
        }
        String clanname = handler.getPlayerClan(sender.getUniqueId());
        if (!handler.isClanLeader(clanname, sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst der Besitzer des Clans sein!");
            return true;
        }

        String name = args[1];

        String ID = getUUIDFromName(name);
        if (!ID.equals("er")) {
            sender.sendMessage(ChatColor.RED + "[Error] Dieser Spieler existiert nicht!");
            return true;
        }
        UUID uuid = UUID.fromString(name);

        if (uuid.equals(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du kannst dich nicht selber aus dem Clan werfen!");
            return true;
        }
        if (!handler.playerHasClan(uuid)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler hat keinen Clan!");
            return true;
        }
        if (!handler.getPlayerClan(uuid).equals(clanname)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler muss in deinem Clan sein!");
            return true;
        }
        handler.removePlayerFromClan(clanname, uuid);

        List<UUID> members = handler.getClanMembers(clanname);
        for (UUID id : members) {
            Player p = Bukkit.getPlayer(id);
            if (p != null && p.isOnline() && p != sender) {
                p.sendMessage(ChatColor.GREEN + "[Clans] Der Spieler " + name + " wurde aus dem Clan geworfen!");
            }
        }
        sender.sendMessage(ChatColor.GREEN + "[Clans] Du hast den Spieler " + name + " aus dem Clan geworfen!");

        Player kicked = Bukkit.getPlayer(name);
        if (kicked != null && kicked.isOnline()) {
            kicked.sendMessage(ChatColor.DARK_RED + "[Clans] Du wurdest aus deinem Clan geworfen!");
        }

        return true;
    }

    public String getUUIDFromName(String name) {
        String uuid = "";
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            uuid = (((JsonObject) JsonParser.parseReader(in)).get("id")).toString().replaceAll("\"", "");
            uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            in.close();
        } catch (Exception e) {
            System.out.println("Unable to get UUID of: " + name + "!");
            uuid = "er";

        }
        return uuid;
    }

    public boolean invite(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Du musst einen Spieler angeben!");
            return true;
        }
        if (!handler.playerHasClan(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst in einem Clan sein!");
            return true;
        }
        String name = args[1];
        String clanname = handler.getPlayerClan(sender.getUniqueId());
        Player target = Bukkit.getPlayer(name);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler muss online sein!");
            return true;
        }
        if (handler.playerHasClan(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler hat bereits einen Clan!");
            return true;
        }
        if (!handler.isClanLeader(clanname, sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Du musst der Besitzer sein um das zu tun!");
            return true;
        }

        target.sendMessage(ChatColor.GREEN + "[Clans] Du wurdest in den Clan " + teams.format(clanname) + ChatColor.GREEN + " eingeladen");
        target.sendMessage(ChatColor.DARK_BLUE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
        TextComponent message = new TextComponent("Möchtest du dem Clan beitreten?");
        message.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        message.setBold(true);
        message.setUnderlined(true);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clans followinv " + clanname));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Hier clicken um dem Clan " + teams.format(clanname) + "§d beizutreten!")
                        .color(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE).italic(true).create()));
        target.spigot().sendMessage(message);
        target.sendMessage(ChatColor.DARK_BLUE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
        handler.joins.add(target);
        sender.sendMessage(ChatColor.GREEN + "[Clans] Du hast den Spieler " + target.getName() + " eingeladen!");
        new BukkitRunnable() {

            @Override
            public void run() {
                if (handler.joins.contains(target)) {
                    handler.joins.remove(target);
                    sender.sendMessage(ChatColor.RED + "[Clans] Der Spieler " + target.getName() + " hat die Einladung nicht angenommen!");
                }

            }

        }.runTaskLater(plugin, 20 * 45);

        return true;
    }

    public boolean join(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Du musst den Namen des Clans angeben!");
            return true;
        }

        String clanname = args[1];
        if (!handler.isExistingClan(clanname)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Clan existiert nicht!!");
            return true;
        }

        if (handler.playerHasClan(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du besitzt bereits einen Clan!");
            return true;
        }

        if (handler.clanIsPrivate(clanname)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Clan ist privat!");
            return true;
        }
        List<UUID> members = handler.getClanMembers(clanname);

        handler.addPlayerToClan(clanname, sender.getUniqueId());
        teams.addPlayerTeam(sender, clanname);
        sender.sendMessage(ChatColor.GREEN + "[Clans] Du bist dem Clan " + teams.format(clanname) + ChatColor.GREEN + " beigetreten!");
        for (UUID m : members) {
            Player member = Bukkit.getPlayer(m);
            if (member != null && member.isOnline()) {
                member.sendMessage(ChatColor.GREEN + "[Clans] Der Spieler " + sender.getName() + " ist dem Clan beigetreten!");
            }
        }

        return true;
    }

    public boolean followinv(Player sender, String[] args, TeamHandler teams, DataHandler handler) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.DARK_RED + "[Fatal Error] Oh nein! Etwas ist schief gelaufen!");
            return false;
        }
        if (handler.playerHasClan(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast bereits einen Clan!");
            return true;
        }
        String clanname = args[1];
        if (!handler.isExistingClan(clanname)) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Oh nein! Es schein so als würde der Clan " +
                    teams.format(clanname) + ChatColor.GREEN + " nicht (mehr) existieren!");
            return false;
        }
        if (!handler.joins.contains(sender)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dir fehlt die Berechtigung. Versuche es noch einmal!");
            return true;
        }
        List<UUID> members = handler.getClanMembers(clanname);
        handler.addPlayerToClan(clanname, sender.getUniqueId());
        teams.addPlayerTeam(sender, clanname);
        handler.joins.remove(sender);
        sender.sendMessage(ChatColor.GREEN + "[Clans] Du bist dem Clan " + teams.format(clanname) + ChatColor.GREEN + " beigetreten!");
        for (UUID m : members) {
            Player member = Bukkit.getPlayer(m);
            if (member != null && member.isOnline() && !handler.isClanLeader(clanname, member.getUniqueId())) {
                member.sendMessage(ChatColor.GREEN + "[Clans] Der Spieler " + sender.getName() + " ist dem Clan beigetreten!");
            } else if (handler.isClanLeader(clanname, member.getUniqueId())) {
                member.sendMessage(ChatColor.GREEN + "[Clans] Der Spieler " + sender.getName() + " ist deine Einladung gefolgt!");
            }
        }
        return true;
    }

    public boolean togglePriv(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length == 1) { // Leader Command Input
            if (!handler.playerHasClan(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du besitzt keinen Clan!");
                return true;
            }
            String clanname = handler.getPlayerClan(sender.getUniqueId());
            if (!handler.isClanLeader(clanname, sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Du musst der Besitzer sein um dies zu tun!");
                return true;
            }
            handler.togglePrivacy(clanname);

            if (handler.clanIsPrivate(clanname))
                sender.sendMessage(ChatColor.GREEN + "[Clans] Dein Clan ist nun privat!");
            else
                sender.sendMessage(ChatColor.GREEN + "[Clans] Dein Clan ist nun nicht mehr privat!");


        } else if (args.length == 2) { // Admin Command Input
            String clanname = args[1];
            if (!handler.isExistingClan(clanname)) {
                sender.sendMessage(ChatColor.RED + "[Error] Dieser Clan existiert nicht!!");
                return true;
            }
            handler.togglePrivacy(clanname);
            if (handler.clanIsPrivate(clanname)) {
                sender.sendMessage(ChatColor.GREEN + "[Admins] Du hast den Clan " + teams.format(clanname) + ChatColor.GREEN + " privat gemacht!");
                Player leader = Bukkit.getPlayer(handler.getClanLeader(clanname));
                if (leader != null && leader.isOnline()) {
                    leader.sendMessage(ChatColor.RED + "[Clans] Ein Admin hat deinen Clan privat gemacht!");
                }
            } else {
                sender.sendMessage(ChatColor.GREEN + "[Admins] Du hast den Clan " + teams.format(clanname) + ChatColor.GREEN + " öffentlich gemacht!");
                Player leader = Bukkit.getPlayer(handler.getClanLeader(clanname));
                if (leader != null && leader.isOnline()) {
                    leader.sendMessage(ChatColor.RED + "[Clans] Ein Admin hat deinen Clan öffentlich gemacht!");
                }
            }
        }
        return true;
    }

    public boolean leaveClan(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (!handler.playerHasClan(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast keinen Clan!");
            return true;
        }

        if (handler.isClanLeader(handler.getPlayerClan(sender.getUniqueId()), sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Du bist der Besitzer dieses Clans!");
            return true;
        }
        String clanname = handler.getPlayerClan(sender.getUniqueId());
        handler.removePlayerFromClan(clanname, sender.getUniqueId());

        sender.sendMessage(ChatColor.GREEN + "[Clans] Du hast den Clan verlassen!");
        List<UUID> members = handler.getClanMembers(clanname);
        for (UUID m : members) {
            Player member = Bukkit.getPlayer(m);
            if (member != null && member.isOnline() && !handler.isClanLeader(clanname, member.getUniqueId())) {
                member.sendMessage(ChatColor.GREEN + "[Clans] Der Spieler " + sender.getName() + " hat den Clan veralssen!");
            }
        }
        return true;
    }


    public boolean toggleClanPvp(Player sender, String[] args, TeamHandler teams, DataHandler handler) {
        if (args.length < 2) {
            if (!handler.playerHasClan(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast keinen Clan!");
                return true;
            }
            String clanname = handler.getPlayerClan(sender.getUniqueId());
            if (!handler.isClanLeader(clanname, sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst der Besitzer des Clans sein!");
                return true;
            }
            if (plugin.settings.getConfig().getBoolean("Public-PVP")) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Command ist deaktiviert!");
                return true;
            }

            sender.sendMessage(ChatColor.YELLOW + "[Clans] PVP wird in 30 Sekunden aktiviert/deaktiviert!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    handler.togglePvp(clanname);
                    List<UUID> members = handler.getClanMembers(clanname);
                    if (handler.hasPvpEnabled(clanname)) {
                        sender.sendMessage(ChatColor.GREEN + "[Clans] PVP ist nun für deinen Clan aktiviert!");
                        teams.updateSuffix(clanname, ChatColor.AQUA + "[" + ChatColor.RED + "PVP" + ChatColor.AQUA + "]");
                        for (UUID m : members) {
                            Player member = Bukkit.getPlayer(m);
                            if (member != null && member.isOnline() && !handler.isClanLeader(clanname, member.getUniqueId())) {
                                member.sendMessage(ChatColor.GREEN + "[Clans] PVP ist nun für deinen Clan aktiviert!");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "[Clans] PVP ist nun für deinen Clan deaktiviert!");
                        teams.updateSuffix(clanname, "");
                        for (UUID m : members) {
                            Player member = Bukkit.getPlayer(m);
                            if (member != null && member.isOnline() && !handler.isClanLeader(clanname, member.getUniqueId())) {
                                member.sendMessage(ChatColor.GREEN + "[Clans] PVP ist nun für deinen Clan deaktiviert!");
                            }
                        }
                    }
                }
            }.runTaskLater(plugin, 20 * 30);


        } else {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber nur ein Admin kann das tun!");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "[Error] Bitte gib einen Clan-Namen ein!");
                return true;
            }
            if (plugin.settings.getConfig().getBoolean("Public-PVP")) {
                sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Command ist deaktiviert!");
                return true;
            }
            String clanname = args[1];
            handler.togglePvp(clanname);

            List<UUID> members = handler.getClanMembers(clanname);
            if (handler.hasPvpEnabled(clanname)) {
                sender.sendMessage(ChatColor.GOLD + "[Admins] PVP ist nun für diesen Clan aktiviert!");
                teams.updateSuffix(clanname, ChatColor.AQUA + "[" + ChatColor.RED + "PVP" + ChatColor.AQUA + "]");
                for (UUID m : members) {
                    Player member = Bukkit.getPlayer(m);
                    if (member != null && member.isOnline()) {
                        member.sendMessage(ChatColor.GREEN + "[Clans] PVP ist nun für deinen Clan aktiviert!");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + "[Admins] PVP ist nun für diesen Clan deaktiviert!");
                teams.updateSuffix(clanname, "");
                for (UUID m : members) {
                    Player member = Bukkit.getPlayer(m);
                    if (member != null && member.isOnline()) {
                        member.sendMessage(ChatColor.GREEN + "[Clans] PVP ist nun für deinen Clan deaktiviert!");
                    }
                }
            }
        }
        return true;
    }
}
