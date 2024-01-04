package me.tund.events;

import me.tund.main.BuildAttack;
import me.tund.utils.data.DataHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChunkEvents implements Listener {
    private BuildAttack plugin;
    private DataHandler handler;

    public ChunkEvents(BuildAttack plugin) {
        this.plugin = plugin;
        this.handler = new DataHandler(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (claimIsOff())
            return;
        if (chestOnly()) {
            if (e.getClickedBlock() == null)
                return;

            if (e.getClickedBlock().getType() != Material.CHEST && e.getClickedBlock().getType()
                    != Material.BARREL && e.getClickedBlock().getType() != Material.SHULKER_BOX && e.getClickedBlock().getType()
                    != Material.HOPPER) {
                if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    return;
                } else {
                    Player breaker = e.getPlayer();
                    Chunk chunk = e.getClickedBlock().getLocation().getChunk();
                    int x = chunk.getX() << 4;
                    int z = chunk.getZ() << 4;
                    String chunkID = "Claim/" + x + "/" + z + "/" + breaker.getWorld().getName();
                    if (!(breaker.getInventory().getItemInMainHand().getType() == Material.STICK)) {
                        return;
                    }
                    if (!handler.isClaimedChunk(chunkID)) {
                        return;
                    }

                    if (handler.isChunkOwner(chunkID, breaker.getUniqueId())) {
                        return;
                    }

                    if (handler.isTrusted(chunkID, breaker.getName())) {
                        return;
                    }

                    OfflinePlayer owner = Bukkit.getOfflinePlayer(handler.getChunkOwner(chunkID));
                    breaker.sendMessage(ChatColor.GREEN + "[Claims] Dieser Chunk ist von " + owner.getName() + " geclaimt!");
                    if (handler.isTrusted(chunkID, breaker.getName()))
                        breaker.sendMessage(ChatColor.GREEN + "[Claims] Du bist hier getrustet!");

                }
                return;
            }


            Player breaker = e.getPlayer();
            Chunk chunk = e.getClickedBlock().getLocation().getChunk();
            int x = chunk.getX() << 4;
            int z = chunk.getZ() << 4;
            String chunkID = "Claim/" + x + "/" + z + "/" + breaker.getWorld().getName();
            if (!handler.isClaimedChunk(chunkID)) {
                return;
            }

            if (handler.isChunkOwner(chunkID, breaker.getUniqueId())) {
                return;
            }

            if (handler.isTrusted(chunkID, breaker.getName())) {
                return;
            }

            e.setCancelled(true);
            breaker.sendMessage(ChatColor.RED + "[Claims] Sorry, aber du kannst mit diesem Container nicht interagieren!");
        } else {
            Player breaker = e.getPlayer();
            Chunk chunk = breaker.getLocation().getChunk();
            int x = chunk.getX() << 4;
            int z = chunk.getZ() << 4;
            String chunkID = "Claim/" + x + "/" + z + "/" + breaker.getWorld().getName();
            if (!handler.isClaimedChunk(chunkID))
                return;
            if (handler.isChunkOwner(chunkID, breaker.getUniqueId()))
                return;
            if (handler.isTrusted(chunkID, breaker.getName()))
                return;
            e.setCancelled(true);
            breaker.sendMessage(ChatColor.RED + "[Claims] Sorry, aber du kannst in diesem Chunk nichts bauen!");
        }
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        if (claimIsOff())
            return;
        if (chestOnly())
            return;

        Player breaker = e.getPlayer();
        Chunk chunk = e.getRightClicked().getLocation().getChunk();
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;
        String chunkID = "Claim/" + x + "/" + z + "/" + breaker.getWorld().getName();
        if (!handler.isClaimedChunk(chunkID))
            return;
        if (handler.isChunkOwner(chunkID, breaker.getUniqueId()))
            return;
        if (handler.isTrusted(chunkID, breaker.getName()))
            return;
        e.setCancelled(true);
        breaker.sendMessage(ChatColor.RED + "[Claims] Sorry, aber du kannst in diesem Chunk mit Entities nicht interagieren!");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (claimIsOff())
            return;
        if (chestOnly())
            return;
        if (!(e.getDamager() instanceof Player))
            return;
        Player damager = (Player) e.getDamager();
        Entity entity = e.getEntity();
        if (entity.getType() != EntityType.WOLF || entity.getType() != EntityType.CAT || entity.getType() != EntityType.HORSE || entity.getType() != EntityType.SKELETON_HORSE)
            return;
        Chunk chunk = e.getEntity().getLocation().getChunk();
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;
        String chunkID = "Claim/" + x + "/" + z + "/" + damager.getWorld().getName();
        if (!handler.isClaimedChunk(chunkID))
            return;
        if (handler.isChunkOwner(chunkID, damager.getUniqueId()))
            return;
        if (handler.isTrusted(chunkID, damager.getName()))
            return;
        e.setCancelled(true);
        damager.sendMessage(ChatColor.RED + "[Claims] Sorry, aber du kannst in diesem Chunk nicht mit Entities interagieren!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (claimIsOff())
            return;
        if (chestOnly()) {
            if (e.getBlock() == null)
                return;

            if (e.getBlock().getType() != Material.CHEST && e.getBlock().getType() != Material.BARREL && e.getBlock().getType() != Material.SHULKER_BOX && e.getBlock().getType() != Material.HOPPER)
                return;

            Player breaker = e.getPlayer();
            Chunk chunk = e.getBlock().getChunk();
            int x = chunk.getX() << 4;
            int z = chunk.getZ() << 4;
            String chunkID = "Claim/" + x + "/" + z + "/" + breaker.getWorld().getName();
            if (!handler.isClaimedChunk(chunkID)) {
                return;
            }

            if (handler.isChunkOwner(chunkID, breaker.getUniqueId())) {
                return;
            }

            if (handler.isTrusted(chunkID, breaker.getName())) {
                return;
            }

            e.setCancelled(true);
            breaker.sendMessage(ChatColor.RED + "[Claims] Sorry, aber du kannst diesen Container nicht abbauen!");
        } else {
            Player breaker = e.getPlayer();
            Chunk chunk = breaker.getLocation().getChunk();
            int x = chunk.getX() << 4;
            int z = chunk.getZ() << 4;
            String chunkID = "Claim/" + x + "/" + z + "/" + breaker.getWorld().getName();
            if (!handler.isClaimedChunk(chunkID))
                return;
            if (handler.isChunkOwner(chunkID, breaker.getUniqueId()))
                return;
            if (handler.isTrusted(chunkID, breaker.getName()))
                return;
            e.setCancelled(true);
            breaker.sendMessage(ChatColor.RED + "[Claims] Sorry, aber du kannst in diesem Chunk nichts bauen!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (claimIsOff())
            return;
        if (chestOnly()) {
            if (e.getBlock() == null)
                return;

            if (e.getBlock().getType() != Material.CHEST && e.getBlock().getType() != Material.BARREL && e.getBlock().getType() != Material.SHULKER_BOX && e.getBlock().getType() != Material.HOPPER)
                return;

            Player breaker = e.getPlayer();
            Chunk chunk = e.getBlock().getChunk();
            int x = chunk.getX() << 4;
            int z = chunk.getZ() << 4;
            String chunkID = "Claim/" + x + "/" + z + "/" + breaker.getWorld().getName();
            if (!handler.isClaimedChunk(chunkID)) {
                return;
            }

            if (handler.isChunkOwner(chunkID, breaker.getUniqueId())) {
                return;
            }

            if (handler.isTrusted(chunkID, breaker.getName())) {
                return;
            }

            e.setCancelled(true);
            breaker.sendMessage(ChatColor.RED + "[Claims] Sorry, aber du kannst diesen Container nicht setzen!");
        } else {
            Player breaker = e.getPlayer();
            Chunk chunk = breaker.getLocation().getChunk();
            int x = chunk.getX() << 4;
            int z = chunk.getZ() << 4;
            String chunkID = "Claim/" + x + "/" + z + "/" + breaker.getWorld().getName();
            if (!handler.isClaimedChunk(chunkID))
                return;
            if (handler.isChunkOwner(chunkID, breaker.getUniqueId()))
                return;
            if (handler.isTrusted(chunkID, breaker.getName()))
                return;
            e.setCancelled(true);
            breaker.sendMessage(ChatColor.RED + "[Claims] Sorry, aber du kannst in diesem Chunk nichts bauen!");
        }
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        if (claimIsOff()) return;
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (claimIsOff()) return;
        if (chestOnly()) return;
        if (tntDamageIsON()) return;

        Entity entity = e.getEntity();
        Chunk chunk = entity.getLocation().getChunk();
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;
        String chunkID = "Claim/" + x + "/" + z + "/" + entity.getWorld().getName();
        if (!handler.isClaimedChunk(chunkID))
            return;
        e.setCancelled(true);

    }


    public boolean chestOnly() {
        return plugin.settings.getConfig().get("Mode").equals("Chest-Only");
    }

    public boolean claimIsOff() {
        return plugin.settings.getConfig().get("Mode").equals("Off");
    }

    public boolean claimIsOn() {
        return plugin.settings.getConfig().get("Mode").equals("Chunk");
    }

    public boolean tntDamageIsON() {
        return plugin.settings.getConfig().getBoolean("TNT-Damage");
    }
}
