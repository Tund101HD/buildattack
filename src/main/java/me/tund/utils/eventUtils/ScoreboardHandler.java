package me.tund.utils.eventUtils;

import fr.mrmicky.fastboard.FastBoard;
import me.tund.main.BuildAttack;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class ScoreboardHandler implements Listener {

    private HashMap<UUID, FastBoard> boards;
    private BuildAttack plugin;


    public ScoreboardHandler(BuildAttack plugin) {
        this.plugin = plugin;
        this.boards = new HashMap<>();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        /* TODO Deactivated for feature move
        Player p = e.getPlayer();
        FastBoard board = new FastBoard(p);
        if(plugin.settings.getConfig().getBoolean("Event.Enabled")){
            board.updateTitle(ChatColor.GOLD.toString()+ ChatColor.UNDERLINE+"----[BuildAttack Varo]----");
            boards.put(p.getUniqueId(), board);
            return;
        }
        board.updateTitle(ChatColor.AQUA+"----["+ChatColor.GOLD+"Build Attack Season 4"+ChatColor.AQUA+"]----");
        boards.put(p.getUniqueId(), board);
        */
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        /* TODO Deactivated for feature move
        Player player = e.getPlayer();
        FastBoard board = this.boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
        */
    }

    public void updateBoardCounter(FastBoard board, int timeRemaining, int playersRemaining) {
        if (timeRemaining > 30) {
            board.updateLines(
                    "",
                    ChatColor.GOLD + "Tode : " + playersRemaining,
                    "",
                    ChatColor.GOLD + "Kills: " + board.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + " s",
                    "",
                    ChatColor.GOLD + "Restl. Zeit: " + timeRemaining,
                    ""
            );
        } else {
            board.updateLines(
                    "",
                    ChatColor.GOLD + "Tode : " + playersRemaining,
                    "",
                    ChatColor.GOLD + "Kills: " + board.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + " s",
                    "",
                    ChatColor.RED + "Restl. Zeit: " + timeRemaining,
                    ""
            );
        }
    }

    public void updateBoardVaro(FastBoard board, int playersRemaining, VaroHandler handler, String timeUntilKick) {
        if (handler.getDead().contains(board.getPlayer())) {
            board.updateLines(
                    "",
                    ChatColor.GOLD + "Tode : " + playersRemaining,
                    "",
                    ChatColor.GOLD + "Kills: " + board.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + " s",
                    "",
                    ChatColor.RED + "Dein Status: Tot",
                    "",
                    "Übrige Spielzeit: " + timeUntilKick,
                    ""
            );
        } else {
            board.updateLines(
                    "",
                    ChatColor.GOLD + "Tode : " + playersRemaining,
                    "",
                    ChatColor.GOLD + "Kills: " + board.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + " s",
                    "",
                    ChatColor.GREEN + "Dein Status: Am Leben",
                    ""
            );
        }


    }

    public FastBoard addPlayerScoreboard(Player p) {
        FastBoard board = new FastBoard(p);
        if (plugin.settings.getConfig().getBoolean("Event.Enabled")) {
            board.updateTitle(ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "----[BuildAttack Varo]----");
            boards.put(p.getUniqueId(), board);
            return board;
        }
        board.updateTitle(ChatColor.AQUA + "----[" + ChatColor.GOLD + "Build Attack Season 4" + ChatColor.AQUA + "]----");
        boards.put(p.getUniqueId(), board);
        return board;
    }

    public void removePlayerScoreboard(Player p) {
        FastBoard board = this.boards.remove(p.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public FastBoard getBoardByPlayer(Player p) {
        return boards.get(p.getUniqueId());
    }


}
