package me.tund.utils.eventUtils;

import me.tund.main.BuildAttack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class VaroHandler implements Listener {


    private boolean active = false;
    private BuildAttack plugin;
    private BukkitTask task;

    private List<Player> alive;
    private List<Player> dead;

    private HashMap<Player, HashMap<LocalDate, Boolean>> login;
    private HashMap<Player, Integer> taskIds;
    private VaroHandler instance;

    public VaroHandler(BuildAttack plugin) {
        this.plugin = plugin;
        this.instance = this;
        login = new HashMap<>();
        taskIds = new HashMap<>();
        active = true;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isOp())
                alive.add(p);
        }


        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (isActive()) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        login.put(p, new HashMap<>());
                        VaroTasks id = new VaroTasks(p.getUniqueId());
                        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                            int count = 60 * 15;

                            @Override
                            public void run() {
                                if (!id.hasID()) {
                                    id.setID(this.hashCode());
                                    instance.taskIds.put(p, this.hashCode());
                                }
                                if (alive.contains(p)) {
                                    plugin.score.updateBoardVaro(plugin.score.getBoardByPlayer(p), alive.size(), instance, Duration.ofSeconds(count).toString());
                                } else {
                                    plugin.score.updateBoardVaro(plugin.score.getBoardByPlayer(p), alive.size(), instance, "Ausgeschieden.");
                                }
                                if (count == 0) {
                                    HashMap<LocalDate, Boolean> logins = login.get(p);
                                    LocalDate date = LocalDate.now();
                                    logins.put(date, false);
                                    p.kickPlayer(ChatColor.RED + "Deine Zeit ist abgelaufen.");
                                }

                            }
                        }, 0, 20);
                    }
                } else {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[Event] Das Event wurde beended! Gratulation an die Gewinner.");
                }
                if (alive.size() < 3 && alive.size() > 1) {
                    if (plugin.getTeams().getTeam(alive.get(0)).equals(plugin.getTeams().getTeam(alive.get(1)))) {
                        active = false;
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Event] Gratulation an " + alive.get(0).getName() + " und " + alive.get(1) + " fürs Gewinnen von Varo 1!");
                    }
                } else if (alive.size() < 2) {
                    Set<OfflinePlayer> players = plugin.getTeams().getTeam(alive.get(0)).getPlayers();
                    List<OfflinePlayer> ps = players.stream().toList();
                    if (ps.size() > 1)
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Event] Gratulation an " + ps.get(0).getName() + " und " + ps.get(1) + " fürs Gewinnen von Varo 1!");
                    else
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[Event] Gratulation an " + alive.get(0).getName() + " fürs Gewinnen von Varo 1!");
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!plugin.settings.getConfig().getBoolean("Event.Enabled")) return;
        if (alive.contains(e.getEntity())) {
            alive.remove(e.getEntity());
            dead.add(e.getEntity());
            Bukkit.broadcastMessage(ChatColor.RED + "[Varo] Der Spieler " + e.getEntity().getName() + " aus dem Team " + plugin.getTeams().getTeam(e.getEntity()).getName() + " ist ausgeschieden.");
            Bukkit.broadcastMessage(ChatColor.RED + "[Varo] Verbleibende Spieler: " + ChatColor.GOLD + alive.size());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!plugin.settings.getConfig().getBoolean("Event.Enabled")) return;
        if (dead.contains(e.getPlayer()) || !alive.contains(e.getPlayer()) && !e.getPlayer().isOp()) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            return;
        }
        if (login.containsKey(e.getPlayer())) {
            if (login.get(e.getPlayer()).containsKey(LocalDate.now()) || !login.get(e.getPlayer()).get(LocalDate.now())) {
                e.getPlayer().kickPlayer(ChatColor.RED + "Deine Zeit für heute ist abgelaufen.");
                return;
            }
        }
        VaroTasks id = new VaroTasks(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int count = 60 * 15;

            @Override
            public void run() {
                if (!id.hasID()) {
                    id.setID(this.hashCode());
                    instance.taskIds.put(e.getPlayer(), this.hashCode());
                }
                if (alive.contains(e.getPlayer())) {
                    plugin.score.updateBoardVaro(plugin.score.getBoardByPlayer(e.getPlayer()), alive.size(), instance, Duration.ofSeconds(count).toString());
                } else {
                    plugin.score.updateBoardVaro(plugin.score.getBoardByPlayer(e.getPlayer()), alive.size(), instance, "Ausgeschieden.");
                }
                if (count == 0) {
                    HashMap<LocalDate, Boolean> logins = login.get(e.getPlayer());
                    LocalDate date = LocalDate.now();
                    logins.put(date, false);
                    e.getPlayer().kickPlayer(ChatColor.RED + "Deine Zeit ist abgelaufen.");
                }
            }
        }, 0, 20);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (!plugin.settings.getConfig().getBoolean("Event.Enabled")) return;
        if (dead.contains(e.getPlayer())) return;
        VaroTasks id = new VaroTasks(e.getPlayer().getUniqueId());
        if (id.hasID()) {
            id.stop();
            if (taskIds.containsKey(e.getPlayer()))
                taskIds.remove(e.getPlayer());
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BuildAttack getPlugin() {
        return plugin;
    }

    public void setPlugin(BuildAttack plugin) {
        this.plugin = plugin;
    }

    public BukkitTask getTask() {
        return task;
    }

    public void setTask(BukkitTask task) {
        this.task = task;
    }

    public List<Player> getAlive() {
        return alive;
    }

    public void setAlive(List<Player> alive) {
        this.alive = alive;
    }

    public List<Player> getDead() {
        return dead;
    }

    public void setDead(List<Player> dead) {
        this.dead = dead;
    }
}
