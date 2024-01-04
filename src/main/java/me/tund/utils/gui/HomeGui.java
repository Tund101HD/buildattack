package me.tund.utils.gui;

import me.tund.main.BuildAttack;
import me.tund.utils.homes.HomesHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class HomeGui {
    private BuildAttack plugin;
    private HomesHandler homes;

    public HomeGui(BuildAttack plugin, HomesHandler homes) {
        this.plugin = plugin;
        this.homes = homes;
    }


    public Inventory getHomeInventory(UUID player, HomesHandler homes, BuildAttack plugin) {
        Inventory inv = Bukkit.createInventory(Bukkit.getPlayer(player), 54, Bukkit.getPlayer(player).getName() + "'s Spieler-Homes");

        List<HashMap<Location, String>> playerHomes = homes.getPlayerHomes(player);
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Nichts hier.");
        List<String> lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.GRAY + "Nichts zu sehen hier.");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        filler.setItemMeta(meta);
        for (int x = 0; x < 53; x++) {
            if (x != 19 && x != 21 && x != 23 && x != 25 && x != 28 && x != 30 && x != 32 && x != 34) {
                inv.setItem(x, filler);
            }
        }
        ItemStack emptyHome = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        meta = emptyHome.getItemMeta();
        lore = new ArrayList<>();
        lore.add(0, ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.RED + "Dieser Slot ist noch nicht belegt.");
        lore.add(2, ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        emptyHome.setItemMeta(meta);
        ItemStack emptyOption = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        meta = emptyOption.getItemMeta();
        lore = new ArrayList<>();
        lore.add(0, ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.GRAY + "Erstelle zu erst ein Home!");
        lore.add(2, ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        emptyOption.setItemMeta(meta);
        ItemStack closeOption = new ItemStack(Material.RED_WOOL, 1);
        meta = closeOption.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Inventar schließen.");
        meta.addEnchant(Enchantment.CHANNELING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore = new ArrayList<>();
        lore.add(0, ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.RED + "Schließe diese Inventar.");
        lore.add(2, ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        closeOption.setItemMeta(meta);
        ItemStack createOption = new ItemStack(Material.GREEN_WOOL, 1);
        meta = createOption.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Erstelle ein Home.");
        meta.addEnchant(Enchantment.CHANNELING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore = new ArrayList<>();
        lore.add(0, ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.GREEN + "Erstelle ein Home an der Stelle.");
        lore.add(2, ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        createOption.setItemMeta(meta);

        int start = 19;
        for (int i = 0; i < 4; i++) {
            inv.setItem(start, emptyHome);
            start += 2;
        }
        start = 28;
        for (int i = 0; i < 4; i++) {
            inv.setItem(start, emptyOption);
            start += 2;
        }
        inv.setItem(45, createOption);
        inv.setItem(53, closeOption);
        if (playerHomes.size() < 1)
            return inv;
        int len = playerHomes.size();
        start = 19;
        int setStart = 28;
        for (int i = 0; i < len; i++) {
            switch (i) {
                case 0:
                    ItemStack firstHome = new ItemStack(Material.GRASS_BLOCK, 1);
                    meta = firstHome.getItemMeta();
                    HashMap<Location, String> m = playerHomes.get(i);
                    String name = "DISPLAY_HOLDER (Das ist definitiv ein Bug)";
                    Location loc = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
                    for (Map.Entry e : m.entrySet()) {
                        name = (String) e.getValue();
                        loc = (Location) e.getKey();
                    }
                    meta.setDisplayName(plugin.getTeams().format(name));
                    meta.addEnchant(Enchantment.CHANNELING, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore = new ArrayList<>();
                    lore.add(0, ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    lore.add(1, ChatColor.LIGHT_PURPLE + "Dein Home an der Stelle: ");
                    lore.add(2, ChatColor.LIGHT_PURPLE + "X: " + loc.getX());
                    lore.add(3, ChatColor.LIGHT_PURPLE + "Y: " + loc.getY());
                    lore.add(4, ChatColor.LIGHT_PURPLE + "Z: " + loc.getZ());
                    lore.add(5, ChatColor.LIGHT_PURPLE + "Welt: " + loc.getWorld().getName());
                    lore.add(6, ChatColor.LIGHT_PURPLE + "Name Roh: " + name);
                    lore.add(7, ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    meta.setLore(lore);
                    firstHome.setItemMeta(meta);
                    inv.setItem(start, firstHome);
                    ItemStack firstOptions = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                    meta = firstOptions.getItemMeta();
                    meta.setDisplayName(plugin.getTeams().format(name) + " verwalten");
                    meta.addEnchant(Enchantment.CHANNELING, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore = new ArrayList<>();
                    lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    lore.add(1, ChatColor.GRAY + "Verwalte dieses Home");
                    lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    meta.setLore(lore);
                    firstOptions.setItemMeta(meta);
                    inv.setItem(setStart, firstOptions);
                    break;
                case 1:
                    ItemStack secondHome = new ItemStack(Material.OAK_PLANKS, 1);
                    meta = secondHome.getItemMeta();
                    m = playerHomes.get(i);
                    name = "DISPLAY_HOLDER (Das ist definitiv ein Bug)";
                    loc = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
                    for (Map.Entry e : m.entrySet()) {
                        name = (String) e.getValue();
                        loc = (Location) e.getKey();
                    }
                    meta.setDisplayName(plugin.getTeams().format(name));
                    meta.addEnchant(Enchantment.CHANNELING, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore = new ArrayList<>();
                    lore.add(0, ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    lore.add(1, ChatColor.LIGHT_PURPLE + "Dein Home an der Stelle: ");
                    lore.add(2, ChatColor.LIGHT_PURPLE + "X: " + loc.getX());
                    lore.add(3, ChatColor.LIGHT_PURPLE + "Y: " + loc.getY());
                    lore.add(4, ChatColor.LIGHT_PURPLE + "Z: " + loc.getZ());
                    lore.add(5, ChatColor.LIGHT_PURPLE + "Welt: " + loc.getWorld().getName());
                    lore.add(6, ChatColor.LIGHT_PURPLE + "Name Roh: " + name);
                    lore.add(7, ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    meta.setLore(lore);
                    secondHome.setItemMeta(meta);
                    inv.setItem(start, secondHome);
                    ItemStack secondOptions = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                    meta = secondOptions.getItemMeta();
                    meta.setDisplayName(plugin.getTeams().format(name) + " verwalten");
                    meta.addEnchant(Enchantment.CHANNELING, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore = new ArrayList<>();
                    lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    lore.add(1, ChatColor.GRAY + "Verwalte dieses Home");
                    lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    meta.setLore(lore);
                    secondOptions.setItemMeta(meta);
                    inv.setItem(setStart, secondOptions);
                    break;
                case 2:
                    ItemStack thirdHome = new ItemStack(Material.SNOW_BLOCK, 1);
                    meta = thirdHome.getItemMeta();
                    m = playerHomes.get(i);
                    name = "DISPLAY_HOLDER (Das ist definitiv ein Bug)";
                    loc = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
                    for (Map.Entry e : m.entrySet()) {
                        name = (String) e.getValue();
                        loc = (Location) e.getKey();
                    }
                    meta.setDisplayName(plugin.getTeams().format(name));
                    meta.addEnchant(Enchantment.CHANNELING, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore = new ArrayList<>();
                    lore.add(0, ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    lore.add(1, ChatColor.LIGHT_PURPLE + "Dein Home an der Stelle: ");
                    lore.add(2, ChatColor.LIGHT_PURPLE + "X: " + loc.getX());
                    lore.add(3, ChatColor.LIGHT_PURPLE + "Y: " + loc.getY());
                    lore.add(4, ChatColor.LIGHT_PURPLE + "Z: " + loc.getZ());
                    lore.add(5, ChatColor.LIGHT_PURPLE + "Welt: " + loc.getWorld().getName());
                    lore.add(6, ChatColor.LIGHT_PURPLE + "Name Roh: " + name);
                    lore.add(7, ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    meta.setLore(lore);
                    thirdHome.setItemMeta(meta);
                    inv.setItem(start, thirdHome);
                    ItemStack thirdOptions = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                    meta = thirdOptions.getItemMeta();
                    meta.setDisplayName(plugin.getTeams().format(name) + " verwalten");
                    meta.addEnchant(Enchantment.CHANNELING, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore = new ArrayList<>();
                    lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    lore.add(1, ChatColor.GRAY + "Verwalte dieses Home");
                    lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    meta.setLore(lore);
                    thirdOptions.setItemMeta(meta);
                    inv.setItem(setStart, thirdOptions);
                    break;
                case 3:
                    ItemStack fourthItem = new ItemStack(Material.COBBLESTONE, 1);
                    meta = fourthItem.getItemMeta();
                    m = playerHomes.get(i);
                    name = "DISPLAY_HOLDER (Das ist definitiv ein Bug)";
                    loc = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
                    for (Map.Entry e : m.entrySet()) {
                        name = (String) e.getValue();
                        loc = (Location) e.getKey();
                    }
                    meta.setDisplayName(plugin.getTeams().format(name));
                    meta.addEnchant(Enchantment.CHANNELING, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore = new ArrayList<>();
                    lore.add(0, ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    lore.add(1, ChatColor.LIGHT_PURPLE + "Dein Home an der Stelle: ");
                    lore.add(2, ChatColor.LIGHT_PURPLE + "X: " + loc.getX());
                    lore.add(3, ChatColor.LIGHT_PURPLE + "Y: " + loc.getY());
                    lore.add(4, ChatColor.LIGHT_PURPLE + "Z: " + loc.getZ());
                    lore.add(5, ChatColor.LIGHT_PURPLE + "Welt: " + loc.getWorld().getName());
                    lore.add(6, ChatColor.LIGHT_PURPLE + "Name Roh: " + name);
                    lore.add(7, ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    meta.setLore(lore);
                    fourthItem.setItemMeta(meta);
                    inv.setItem(start, fourthItem);
                    ItemStack fourthOptions = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                    meta = fourthOptions.getItemMeta();
                    meta.setDisplayName(plugin.getTeams().format(name) + " verwalten");
                    meta.addEnchant(Enchantment.CHANNELING, 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore = new ArrayList<>();
                    lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    lore.add(1, ChatColor.GRAY + "Verwalte dieses Home");
                    lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
                    meta.setLore(lore);
                    fourthOptions.setItemMeta(meta);
                    inv.setItem(setStart, fourthOptions);
                    break;
            }
            start += 2;
            setStart += 2;
        }
        if (len > 3) {
            ItemStack disabledCreate = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
            meta = disabledCreate.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Sorry!");
            lore = new ArrayList<>();
            lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
            lore.add(1, ChatColor.RED + "Du hast die maximale Anzahl an Homes erreicht.");
            lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
            meta.setLore(lore);
            disabledCreate.setItemMeta(meta);
            inv.setItem(45, disabledCreate);
        }
        return inv;
    }


}
