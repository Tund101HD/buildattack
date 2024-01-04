package me.tund.events;

import com.mojang.authlib.GameProfile;
import me.tund.main.BuildAttack;
import me.tund.utils.TaskIdent;
import me.tund.utils.TeamHandler;
import me.tund.utils.data.DataHandler;
import me.tund.utils.gui.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitRunnable;

import javax.print.DocFlavor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class HandlingEvents implements Listener {

    private BuildAttack plugin;
    private DataHandler handler;
    private TeamHandler teams;
    private ItemHandler items;
    private int taskID;

    public HandlingEvents(BuildAttack plugin) {
        this.plugin = plugin;
        handler = new DataHandler(plugin);
        teams = new TeamHandler();
        items = new ItemHandler(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        handler.createAtt(p);
        if (plugin.settings.getConfig().getBoolean("Change-Tablist")) {
            p.setPlayerListHeader(teams.format((String) plugin.settings.getConfig().get("Tablist-Header")));
            p.setPlayerListFooter(teams.format((String) plugin.settings.getConfig().get("Tablist-Footer")));
        }
        if (!plugin.timers.getConfig().contains(e.getPlayer().getUniqueId().toString())) {
            plugin.timers.getConfig().set(e.getPlayer().getUniqueId().toString(), 0);
            plugin.timers.saveConfig();
        }
        if (!plugin.timers.getConfig().contains("Enabled-" + p.getUniqueId())) {
            plugin.timers.getConfig().set("Enabled-" + p.getUniqueId(), true);
            plugin.timers.saveConfig();
        }
        int seconds = plugin.timers.getConfig().getInt(p.getUniqueId().toString());
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        String timeString = "";
        if (day < 1) {
            timeString = String.format("%02dh " + "%02dm " + "%02ds", hours, minutes, second);
        } else {
            timeString = String.format("%02dd " + "%02dh " + "%02dm " + "%02ds", day, hours, minutes, second);
        }

        p.sendTitle("", ChatColor.GREEN + "Deine Spielzeit bisher: " + timeString, 20, 20 * 5, 20);

        e.setJoinMessage(ChatColor.YELLOW + e.getPlayer().getName() + " hat den Server betreten!");
        if (!p.hasPlayedBefore()) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    //Regeln
                    p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                             ");
                    p.sendMessage(ChatColor.GREEN + "1. Kein Cheaten, Hacken oder X-Rayen!");
                    p.sendMessage(ChatColor.GREEN + "2. Kein Killfarming, vor allem bei unseren Neuankömmligen!");
                    p.sendMessage(ChatColor.GREEN + "3. Seid bitte nett zu einander!");
                    p.sendMessage(ChatColor.GREEN + "4. Stehlen ist nicht erlaubt.");
                    p.sendMessage(ChatColor.GREEN + "5. Exploits bitte melden und nicht ausnutzen.");
                    p.sendMessage(ChatColor.GREEN + "6. Spaß haben und die Zeit genießen! :D.");
                    p.sendMessage(ChatColor.GREEN + "7. Griefen außerhalb des Roleplays ist untersagt.");
                    p.sendMessage(ChatColor.GREEN + "Bei Regelbruch gibt es Strafen die entweder im Roleplay " + "\n" +
                            "vollzogen werden, oder ihr werdet gebannt/gekickt.");
                    p.sendMessage(ChatColor.GREEN + "Mit dem Weiterspielen nach dieser Nachricht stimmt ihr " + "\n" +
                            "den Regeln zu, und bestätigt, dass ihr sie gelesen habt.");
                    p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                             ");

                }
            }.runTaskLater(plugin, 20 * 3);
        }
        if (handler.isInClan(p.getUniqueId()) && !teams.hasTeam(p)) {
            teams.addPlayerTeam(p, handler.getClanNameByList(p.getUniqueId()));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        handler.removeAtt(p);
        TaskIdent ident = new TaskIdent(p.getUniqueId());
        if (ident.hasID())
            ident.stop();
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + " hat den Server verlassen!");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!isEconomyActive())
            return;
        Player p = e.getPlayer();
        Block block = e.getBlock();
        String purse = "Player." + p.getUniqueId();
        String bank = "Bank." + p.getUniqueId();
        if (!plugin.economy.getConfig().contains(purse)) {
            plugin.economy.getConfig().set(purse, 0.00F);
        }
        if (!plugin.economy.getConfig().contains(bank)) {
            plugin.economy.getConfig().set(bank, 0.00F);
        }
        float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);

        if (block.getType() == Material.TALL_GRASS || block.getType() == Material.BLUE_ORCHID || block.getType() == Material.ROSE_BUSH ||
                block.getType() == Material.DANDELION || block.getType() == Material.ARMOR_STAND || block.getType() == Material.BROWN_MUSHROOM ||
                block.getType() == Material.RED_MUSHROOM)
            return;
        plugin.economy.getConfig().set(purse, purseAmount + 10.00F);
        plugin.economy.saveConfig();
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null)
            return;
        Player p = e.getEntity().getKiller();
        Player dead = e.getEntity();
        ItemStack playerSkull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) playerSkull.getItemMeta();
        meta.setOwnerProfile(dead.getPlayerProfile());
        meta.setDisplayName(" ");
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(currentDate);
        String weaponName = "Hand";
        String weaponType = "Main Hand";
        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getItemMeta() != null) {
            String s = p.getInventory().getItemInMainHand().getType().getTranslationKey();
            weaponName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().isEmpty())
                weaponName = s.substring(s.lastIndexOf(".") + 1).toUpperCase();
            weaponType = s.substring(s.lastIndexOf(".") + 1).toUpperCase();
        }
        List<String> lore = new ArrayList<>();
        lore.add(0,
                ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + "        " + ChatColor.AQUA
                        + "[" + ChatColor.GOLD + dead.getName() + "'s Kopf" + ChatColor.STRIKETHROUGH
                        + ChatColor.AQUA + "]" + ChatColor.AQUA + ChatColor.STRIKETHROUGH + "        ");

        lore.add(1, ChatColor.RED + "Getötet von: " + p.getName());
        lore.add(2, ChatColor.RED + "Getötet am: " + currentDateTime);
        lore.add(3, ChatColor.RED + "Waffe: " + weaponName);
        lore.add(4, ChatColor.RED + "Waffen-Typ: " + weaponType);
        lore.add(5, ChatColor.RED + "Übrige HP: " + p.getHealth() + "♥");
        lore.add(6, ChatColor.AQUA.toString() + ChatColor.STRIKETHROUGH + "                                         ");
        meta.setLore(lore);
        playerSkull.setItemMeta(meta);
        e.getEntity().getWorld().dropItem(e.getEntity().getLocation().add(0.5, 0.5, 0.5), playerSkull);
        if (!isEconomyActive())
            return;
        String purse = "Player." + p.getUniqueId();
        String bank = "Bank." + p.getUniqueId();
        if (!plugin.economy.getConfig().contains(purse)) {
            plugin.economy.getConfig().set(purse, 0.00F);
        }
        if (!plugin.economy.getConfig().contains(bank)) {
            plugin.economy.getConfig().set(bank, 0.00F);
        }
        float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);

        plugin.economy.getConfig().set(purse, purseAmount + 100.00F);
        String dpurse = "Player." + dead.getUniqueId();
        String dbank = "Bank." + dead.getUniqueId();
        if (!plugin.economy.getConfig().contains(purse)) {
            plugin.economy.getConfig().set(purse, 0.00F);
        }
        if (!plugin.economy.getConfig().contains(bank)) {
            plugin.economy.getConfig().set(bank, 0.00F);
        }
        float dpurseAmount = (float) plugin.economy.getConfig().getDouble(purse);
        plugin.economy.getConfig().set(dpurse, dpurseAmount / 2);
        plugin.economy.saveConfig();
    }

    @EventHandler
    public void entityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player)
            return;
        if (!(e.getEntity().getKiller() instanceof Player))
            return;
        if (!isEconomyActive())
            return;

        Player p = e.getEntity().getKiller();
        String purse = "Player." + p.getUniqueId();
        String bank = "Bank." + p.getUniqueId();
        if (!plugin.economy.getConfig().contains(purse)) {
            plugin.economy.getConfig().set(purse, 0.00F);
        }
        if (!plugin.economy.getConfig().contains(bank)) {
            plugin.economy.getConfig().set(bank, 0.00F);
        }
        float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);

        plugin.economy.getConfig().set(purse, purseAmount + 60.00F);
        plugin.economy.saveConfig();
    }

    @EventHandler
    public void invClose(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        p.updateInventory();
    }


    public void start(Player p) {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            TaskIdent id = new TaskIdent(p.getUniqueId());
            String purse = "Player." + p.getUniqueId();
            String bank = "Bank." + p.getUniqueId();

            @Override
            public void run() {
                if (isEconomyActive()) {
                    if (!id.hasID()) {
                        id.setID(taskID);
                    }

                    if (!plugin.economy.getConfig().contains(purse)) {
                        plugin.economy.getConfig().set(purse, 0.00F);
                    }
                    if (!plugin.economy.getConfig().contains(bank)) {
                        plugin.economy.getConfig().set(bank, 0.00F);
                    }
                    float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);

                    plugin.economy.getConfig().set(purse, purseAmount + 500.00F);
                    plugin.economy.saveConfig();
                }

            }

        }, 0, 20 * 60 * 15);
    }

    @EventHandler
    public void onItemRightClick(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;
        if (!cItemsActive())
            return;
        Player p = e.getPlayer();

        //Stick of Truth
        if (p.getInventory().getItemInMainHand().getItemMeta() == items.getItem(0, 1).getItemMeta()
                || p.getInventory().getItemInOffHand().getItemMeta() == items.getItem(0, 1).getItemMeta()) {


            //Shovel of Gods
        } else if (p.getInventory().getItemInMainHand().getItemMeta() == items.getItem(1, 1).getItemMeta()
                || p.getInventory().getItemInOffHand().getItemMeta() == items.getItem(0, 1).getItemMeta()) {

        }


    }

    public boolean isEconomyActive() {
        return plugin.settings.getConfig().get("Economy").equals(true);
    }

    public boolean cItemsActive() {
        return plugin.settings.getConfig().get("C-Items").equals(true);
    }
}
