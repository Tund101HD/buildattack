package me.tund.events;

import me.tund.main.BuildAttack;
import me.tund.utils.data.DataHandler;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PvpEvents implements Listener {

    private BuildAttack plugin;
    private DataHandler handler;

    public PvpEvents(BuildAttack plugin) {
        this.plugin = plugin;
        this.handler = plugin.getHandler();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (plugin.settings.getConfig().getBoolean("Public-PVP"))
            return;
        if ((e.getEntity() == null))
            return;
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
            return;

        Player ver = (Player) e.getEntity();
        Player ang = (Player) e.getDamager();

        if (!handler.playerHasClan(ver.getUniqueId()) || !handler.playerHasClan(ang.getUniqueId())) {
            ang.sendMessage(ChatColor.RED + "[PVP] Sorry, aber Public-PVP ist aus! Beide Spieler brauchen einen Clan" + "\n"
                    + " der PVP aktiviert hat.");
            e.setCancelled(true);
            return;
        }
        String vername = handler.getPlayerClan(ver.getUniqueId());
        String angname = handler.getPlayerClan(ang.getUniqueId());

        if (!handler.hasPvpEnabled(vername) || !handler.hasPvpEnabled(angname)) {
            ang.sendMessage(ChatColor.RED + "[PVP] Sorry, aber Public-PVP ist aus! Beide Spieler brauchen einen Clan" + "\n"
                    + " der PVP aktiviert hat.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrystalDamage(EntityDamageByEntityEvent e) {
        if (plugin.settings.getConfig().getBoolean("Public-PVP"))
            return;
        if (e.getEntity() == null)
            return;
        if (!(e.getEntity() instanceof Player))
            return;

        if (e.getDamager().getType() == EntityType.ENDER_CRYSTAL) {
            if (!(e.getEntity().getWorld().getEnvironment() == World.Environment.THE_END)) {
                Player p = (Player) e.getEntity();
                if (!handler.hasPvpEnabled(handler.getPlayerClan(p.getUniqueId()))) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.GREEN
                            + "[PVP] Da dein Clan kein PVP aktiviert hat, wurdest vor Schaden beschützt!");
                }

            }

        }

    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e) {
        if (plugin.settings.getConfig().getBoolean("Public-PVP"))
            return;
        if (!(e.getEntity() instanceof Arrow) && !(e.getEntity() instanceof Trident))
            return;
        if (e.getHitEntity() == null)
            return;
        if (!(e.getHitEntity() instanceof Player))
            return;
        Player hit = (Player) e.getHitEntity();

        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();

            if (!(arrow.getShooter() instanceof Player))
                return;

            Player damager = (Player) arrow.getShooter();
            if (!handler.playerHasClan(hit.getUniqueId()) || !handler.playerHasClan(damager.getUniqueId())) {
                damager.sendMessage(ChatColor.RED + "[PVP] Sorry, aber Public-PVP ist aus! Beide Spieler brauchen einen Clan" + "\n"
                        + " der PVP aktiviert hat.");
                e.setCancelled(true);
                return;
            }
            String verclan = handler.getPlayerClan(hit.getUniqueId());
            String angclan = handler.getPlayerClan(damager.getUniqueId());
            if (!handler.hasPvpEnabled(verclan) || !handler.hasPvpEnabled(angclan)) {
                damager.sendMessage(ChatColor.RED + "[PVP] Sorry, aber Public-PVP ist aus! Beide Spieler brauchen einen Clan" + "\n"
                        + " der PVP aktiviert hat.");
                e.setCancelled(true);
                return;
            }

        } else if (e.getEntity() instanceof Trident) {
            Trident trident = (Trident) e.getEntity();
            if (!(trident.getShooter() instanceof Player))
                return;
            Player damager = (Player) trident.getShooter();
            if (!handler.playerHasClan(hit.getUniqueId()) || !handler.playerHasClan(damager.getUniqueId())) {
                damager.sendMessage(ChatColor.RED + "[PVP] Sorry, aber Public-PVP ist aus! Beide Spieler brauchen einen Clan" + "\n"
                        + " der PVP aktiviert hat.");
                e.setCancelled(true);
                return;
            }
            String verclan = handler.getPlayerClan(hit.getUniqueId());
            String angclan = handler.getPlayerClan(damager.getUniqueId());
            if (!handler.hasPvpEnabled(verclan) || !handler.hasPvpEnabled(angclan)) {
                damager.sendMessage(ChatColor.RED + "[PVP] Sorry, aber Public-PVP ist aus! Beide Spieler brauchen einen Clan" + "\n"
                        + " der PVP aktiviert hat.");
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onSplash(PotionSplashEvent e) {
        if (plugin.settings.getConfig().getBoolean("Public-PVP"))
            return;
        if (e.getAffectedEntities() == null)
            return;
        if (!(e.getEntity().getShooter() instanceof Player))
            return;
        Player damager = (Player) e.getEntity().getShooter();

        List<Player> hit = new ArrayList<Player>();
        for (Entity ent : e.getAffectedEntities()) {
            if (ent instanceof Player) {
                if (ent != damager) {
                    Player p = (Player) ent;
                    hit.add(p);
                }
            }
        }
        for (PotionEffect ev : e.getPotion().getEffects()) {
            if (ev.getType().equals(PotionEffectType.POISON) || ev.getType().equals(PotionEffectType.HARM)
                    || ev.getType().equals(PotionEffectType.WEAKNESS)) {

                for (Player damaged : hit) {
                    if (!(handler.playerHasClan(damaged.getUniqueId()))
                            || !handler.hasPvpEnabled(handler.getPlayerClan(damaged.getUniqueId()))
                            || !(handler.playerHasClan(damager.getUniqueId()))
                            || !handler.hasPvpEnabled(handler.getPlayerClan(damager.getUniqueId()))) {
                        e.setIntensity(damaged, 0.0);
                        damager.sendMessage(ChatColor.RED + "[PVP] Sorry, aber Public-PVP ist aus! Der Clan von " + damaged.getName() + " hat PVP deaktiviert.");
                    }
                }
            }
        }
        return;
    }

    @EventHandler
    public void onRocket(EntityDamageByEntityEvent e) {
        if (plugin.settings.getConfig().getBoolean("Public-PVP"))
            return;
        if (e.getEntity() == null || e.getDamager() == null)
            return;
        if (!(e.getEntity() instanceof Player))
            return;
        if (!(e.getDamager() instanceof Firework))
            return;
        Firework rocket = (Firework) e.getDamager();
        Player damager = (Player) rocket.getShooter();
        Player hit = (Player) e.getEntity();
        if (!handler.playerHasClan(hit.getUniqueId()) || !handler.playerHasClan(damager.getUniqueId())) {

            damager.sendMessage(ChatColor.RED + "[PVP] Sorry, aber Public-PVP ist aus! Beide Spieler brauchen einen Clan" + "\n"
                    + " der PVP aktiviert hat.");
            e.setDamage(0.0);
            e.setCancelled(true);

            return;
        }
        String verclan = handler.getPlayerClan(hit.getUniqueId());
        String angclan = handler.getPlayerClan(damager.getUniqueId());
        if (!handler.hasPvpEnabled(verclan) || !handler.hasPvpEnabled(angclan)) {
            damager.sendMessage(ChatColor.RED + "[Error] You can't attack this player since your or the "
                    + "players clan hasn't enabled PVP!");
            e.setDamage(0.0);
            e.setCancelled(true);
            return;
        }
    }
}
