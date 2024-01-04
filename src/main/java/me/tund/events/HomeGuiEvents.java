package me.tund.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HomeGuiEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().contains("Spieler-Homes") && e.getClickedInventory().getType() != InventoryType.PLAYER) {
            e.setCancelled(true);
            Inventory inv = e.getClickedInventory();
            int slot = e.getSlot();
            switch (slot) {
                case 28:
                    ItemStack item = inv.getItem(slot);
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        if (item.getItemMeta().getDisplayName().contains("verwalten")) {
                            //Open Configuration window for this home.
                        }
                    }
                    break;
                case 30:
                    item = inv.getItem(slot);
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        if (item.getItemMeta().getDisplayName().contains("verwalten")) {
                            //Open Configuration window for this home.
                        }
                    }
                    break;
                case 32:
                    item = inv.getItem(slot);
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        if (item.getItemMeta().getDisplayName().contains("verwalten")) {
                            //Open Configuration window for this home.
                        }
                    }
                    break;
                case 34:
                    item = inv.getItem(slot);
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        if (item.getItemMeta().getDisplayName().contains("verwalten")) {
                            //Open Configuration window for this home.
                        }
                    }
                    break;
            }
        }
    }
}
