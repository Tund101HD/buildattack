package me.tund.utils.timer;

import me.tund.main.BuildAttack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Timer extends Thread {

    public BuildAttack plugin;

    public Timer(BuildAttack plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (plugin.timers.getConfig().contains(p.getUniqueId().toString()))
                plugin.timers.getConfig().set(p.getUniqueId().toString(), plugin.timers.getConfig().getInt(p.getUniqueId().toString()) + 1);
            else
                plugin.timers.getConfig().set(p.getUniqueId().toString(), 0);
            plugin.timers.saveConfig();
            int seconds = plugin.timers.getConfig().getInt(p.getUniqueId().toString());
            int day = (int) TimeUnit.SECONDS.toDays(seconds);
            long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
            long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
            long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
            String timeString = "";
            if (day > 0) {
                timeString = String.format("%02dd " + "%02dh " + "%02dm " + "%02ds", day, hours, minutes, second);
            } else {
                timeString = String.format("%02dh " + "%02dm " + "%02ds", hours, minutes, second);
            }
            String message = ChatColor.GREEN + "Spielzeit: " + timeString;
            plugin.sendToActionBar(p, message);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error waiting one Second.");
        }
    }
}
