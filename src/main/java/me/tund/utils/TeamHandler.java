package me.tund.utils;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamHandler {

    public void createTeam(String team) {
        Team teams = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(team);
        teams.setPrefix(ChatColor.AQUA + "[" + ChatColor.GOLD + format(team) + ChatColor.AQUA + "] ");
        teams.setCanSeeFriendlyInvisibles(true);
        teams.setAllowFriendlyFire(true);
    }

    public void createTeam(String team, String suffix) {
        Team teams = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(team);
        teams.setPrefix(ChatColor.AQUA + "[" + ChatColor.GOLD + format(team) + ChatColor.AQUA + "] ");
        teams.setCanSeeFriendlyInvisibles(true);
        teams.setAllowFriendlyFire(true);
        teams.setSuffix(suffix);
    }


    public void deleteTeam(String team) {
        List<String> allTeams = new ArrayList<String>();
        for (Team teams : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            allTeams.add(teams.getName());
        }
        if (allTeams.contains(team)) {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam(team).unregister();
        }
    }

    @SuppressWarnings("deprecation")
    public Team getTeam(Player p) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p);
        return team;
    }

    @SuppressWarnings("deprecation")
    public void remPlayerTeam(Player p) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p);
        team.removePlayer(p);

        return;
    }

    @SuppressWarnings({"deprecation"})
    public void addPlayerTeam(Player p, String team) {
        List<String> allTeams = new ArrayList<String>();
        for (Team teams : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            allTeams.add(teams.getName());
        }
        if (allTeams.contains(team)) {
            Team teams = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(team);
            teams.addPlayer(p);
        }

    }

    @SuppressWarnings("deprecation")
    public Boolean hasTeam(Player p) {
        return Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p) != null;
    }

    @SuppressWarnings("deprecation")
    public void updateSuffix(String team, String suffix) {
        List<String> allTeams = new ArrayList<String>();
        for (Team teams : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            allTeams.add(teams.getName());
        }
        if (allTeams.contains(team)) {
            Team teams = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(team);
            teams.setSuffix(suffix);
        }

    }

    public String format(String string) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', string);
    }


    public void Test() {
        ClientboundClearTitlesPacket packet;
    }

}
