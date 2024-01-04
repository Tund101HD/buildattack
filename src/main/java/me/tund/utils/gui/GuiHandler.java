package me.tund.utils.gui;

import me.tund.main.BuildAttack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiHandler {

    public Inventory market;
    public Inventory select;

    private BuildAttack plugin = BuildAttack.getPlugin(BuildAttack.class);

    public GuiHandler() {
        createMarket();
        createMarketSelect();
    }

    public void createMarketSelect() {
        Inventory inv = Bukkit.createInventory(null, 45, "Welthandel");

        ItemStack buy = new ItemStack(Material.GOLD_BLOCK, 1);
        ItemMeta meta = buy.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Kaufe ein Item.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        buy.setItemMeta(meta);

        ItemStack sell = new ItemStack(Material.HOPPER, 1);
        meta = sell.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Verkaufe ein Item.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        sell.setItemMeta(meta);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        meta = filler.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.ITALIC + "Nichts zu sehen hier...");
        filler.setItemMeta(meta);

        for (int i = 0; i < 45; i++) {
            inv.setItem(i, filler);
        }
        inv.setItem(20, buy);
        inv.setItem(24, sell);

        this.select = inv;
    }

    public void createMarket() {
        List<SellOrder> diamonds = plugin.getDiamonds();
        List<SellOrder> emeralds = plugin.getEmeralds();
        List<SellOrder> netherites = plugin.getNetherite();
        List<SellOrder> ghasts = plugin.getGhasttear();
        List<SellOrder> guns = plugin.getGunpowder();
        List<SellOrder> golds = plugin.getGold();
        if (diamonds == null) {
            diamonds = new ArrayList<>();
        }
        if (emeralds == null) {
            emeralds = new ArrayList<>();
        }
        if (netherites == null) {
            netherites = new ArrayList<>();
        }
        if (ghasts == null) {
            ghasts = new ArrayList<>();
        }
        if (guns == null) {
            guns = new ArrayList<>();
        }
        if (golds == null) {
            golds = new ArrayList<>();
        }
        Inventory inv = Bukkit.createInventory(null, 45, "Handelsplatz");

        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = diamond.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Diamant-Kurs");
        if (!(diamonds.size() < 1)) {
            SellOrder smallest = diamonds.get(0);
            SellOrder largest = diamonds.get(0);
            for (int x = 0; x < diamonds.size(); x++) {
                SellOrder sell = diamonds.get(x);
                if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
            }
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.LIGHT_PURPLE + "Der Kurs für einen Diamanten ist: ");
            lore.add(2, ChatColor.GOLD + "Höchster Kurs: " + largest.getPricePer() + "$(" + largest.getItem().getAmount() + ")");
            lore.add(3, ChatColor.GOLD + "Niedrigster Kurs: " + smallest.getPricePer() + "$ (" + smallest.getItem().getAmount() + ")");
            lore.add(4, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            diamond.setItemMeta(meta);
        } else {
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.GOLD.toString() + ChatColor.BOLD + "Es werden noch keine Diamanten verkauft!");
            lore.add(2, ChatColor.GOLD.toString() + ChatColor.BOLD + "Sei jetzt der Erste und bestimme den Preis.");
            lore.add(3, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            diamond.setItemMeta(meta);
        }
        inv.setItem(20, diamond);


        ItemStack emerald = new ItemStack(Material.EMERALD, 1);
        meta = emerald.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Emerald-Kurs");
        if (!(emeralds.size() < 1) && emeralds != null) {
            SellOrder smallest = emeralds.get(0);
            SellOrder largest = emeralds.get(0);
            for (int x = 0; x < emeralds.size(); x++) {
                SellOrder sell = emeralds.get(x);
                if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
            }
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.LIGHT_PURPLE + "Der Kurs für einen Emerald ist: ");
            lore.add(2, ChatColor.GOLD + "Höchster Kurs: " + largest.getPricePer() + "$(" + largest.getItem().getAmount() + ")");
            lore.add(3, ChatColor.GOLD + "Niedrigster Kurs: " + smallest.getPricePer() + "$ (" + smallest.getItem().getAmount() + ")");
            lore.add(4, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            emerald.setItemMeta(meta);
        } else {
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.GOLD.toString() + ChatColor.BOLD + "Es werden noch keine Emeralds verkauft!");
            lore.add(2, ChatColor.GOLD.toString() + ChatColor.BOLD + "Sei jetzt der Erste und bestimme den Preis.");
            lore.add(3, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            emerald.setItemMeta(meta);
        }
        inv.setItem(21, emerald);


        ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1);
        meta = gold.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Gold-Kurs");
        if (!(golds.size() < 1) && golds != null) {
            SellOrder smallest = golds.get(0);
            SellOrder largest = golds.get(0);
            for (int x = 0; x < golds.size(); x++) {
                SellOrder sell = golds.get(x);
                if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
            }
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.LIGHT_PURPLE + "Der Kurs für einen Gold-Barren ist: ");
            lore.add(2, ChatColor.GOLD + "Höchster Kurs: " + largest.getPricePer() + "$(" + largest.getItem().getAmount() + ")");
            lore.add(3, ChatColor.GOLD + "Niedrigster Kurs: " + smallest.getPricePer() + "$ (" + smallest.getItem().getAmount() + ")");
            lore.add(4, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            gold.setItemMeta(meta);
        } else {
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.GOLD.toString() + ChatColor.BOLD + "Es werden noch keine Gold-Barren verkauft!");
            lore.add(2, ChatColor.GOLD.toString() + ChatColor.BOLD + "Sei jetzt der Erste und bestimme den Preis.");
            lore.add(3, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            gold.setItemMeta(meta);
        }
        inv.setItem(22, gold);


        ItemStack netherite = new ItemStack(Material.NETHERITE_INGOT, 1);
        meta = netherite.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Netherite-Kurs");
        if (!(netherites.size() < 1) && netherites != null) {
            SellOrder smallest = netherites.get(0);
            SellOrder largest = netherites.get(0);
            for (int x = 0; x < netherites.size(); x++) {
                SellOrder sell = netherites.get(x);
                if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
            }
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.LIGHT_PURPLE + "Der Kurs für einen Netherite-Barren ist: ");
            lore.add(2, ChatColor.GOLD + "Höchster Kurs: " + largest.getPricePer() + "$(" + largest.getItem().getAmount() + ")");
            lore.add(3, ChatColor.GOLD + "Niedrigster Kurs: " + smallest.getPricePer() + "$ (" + smallest.getItem().getAmount() + ")");
            lore.add(4, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            netherite.setItemMeta(meta);
        } else {
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.GOLD.toString() + ChatColor.BOLD + "Es werden noch keine Netherite-Barren verkauft!");
            lore.add(2, ChatColor.GOLD.toString() + ChatColor.BOLD + "Sei jetzt der Erste und bestimme den Preis.");
            lore.add(3, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            netherite.setItemMeta(meta);
        }
        inv.setItem(23, netherite);


        ItemStack gun = new ItemStack(Material.GUNPOWDER, 1);
        meta = gun.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Gunpowder-Kurs");
        if (!(guns.size() < 1) && guns != null) {
            SellOrder smallest = guns.get(0);
            SellOrder largest = guns.get(0);
            for (int x = 0; x < guns.size(); x++) {
                SellOrder sell = guns.get(x);
                if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
            }
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.LIGHT_PURPLE + "Der Kurs für ein Gunpowder ist: ");
            lore.add(2, ChatColor.GOLD + "Höchster Kurs: " + largest.getPricePer() + "$(" + largest.getItem().getAmount() + ")");
            lore.add(3, ChatColor.GOLD + "Niedrigster Kurs: " + smallest.getPricePer() + "$ (" + smallest.getItem().getAmount() + ")");
            lore.add(4, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            gun.setItemMeta(meta);
        } else {
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.GOLD.toString() + ChatColor.BOLD + "Es wird noch kein Gunpowder verkauft!");
            lore.add(2, ChatColor.GOLD.toString() + ChatColor.BOLD + "Sei jetzt der Erste und bestimme den Preis.");
            lore.add(3, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            gun.setItemMeta(meta);
        }
        inv.setItem(24, gun);


        ItemStack ghast = new ItemStack(Material.GUNPOWDER, 1);
        meta = ghast.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Ghast-Tear-Kurs");
        if (!(ghasts.size() < 1) && ghasts != null) {
            SellOrder smallest = ghasts.get(0);
            SellOrder largest = ghasts.get(0);
            for (int x = 0; x < ghasts.size(); x++) {
                SellOrder sell = ghasts.get(x);
                if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
            }
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.LIGHT_PURPLE + "Der Kurs für eine Ghast-Tear ist: ");
            lore.add(2, ChatColor.GOLD + "Höchster Kurs: " + largest.getPricePer() + "$(" + largest.getItem().getAmount() + ")");
            lore.add(3, ChatColor.GOLD + "Niedrigster Kurs: " + smallest.getPricePer() + "$ (" + smallest.getItem().getAmount() + ")");
            lore.add(4, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            ghast.setItemMeta(meta);
        } else {
            List<String> lore = new ArrayList<>();
            lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            lore.add(1, ChatColor.GOLD.toString() + ChatColor.BOLD + "Es wird noch keine Ghast-Tear verkauft!");
            lore.add(2, ChatColor.GOLD.toString() + ChatColor.BOLD + "Sei jetzt der Erste und bestimme den Preis.");
            lore.add(3, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                              ");
            meta.setLore(lore);
            ghast.setItemMeta(meta);
        }
        inv.setItem(26, ghast);

        this.market = inv;
    }


    public Inventory createBankInv(Player player) {
        Inventory inv = Bukkit.createInventory(player, 45, "Bankkonto");

        String bank = "Bank." + player.getUniqueId();
        String history = "History." + player.getUniqueId();
        float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);
        String aktion;
        if (plugin.economy.getConfig().getConfigurationSection("History") != null && plugin.economy.getConfig().getConfigurationSection("History")
                .contains(player.getUniqueId().toString()))
            aktion = plugin.economy.getConfig().getString(history);
        else
            aktion = "Keine Daten verfügbar!";

        ItemStack last = new ItemStack(Material.BIRCH_SIGN, 1);
        ItemMeta meta = last.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Letzte Aktion");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + aktion);
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        last.setItemMeta(meta);
        inv.setItem(25, last);

        ItemStack money = new ItemStack(Material.EMERALD_BLOCK, 1);
        meta = money.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + bankAmount);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore = new ArrayList<>();
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Das ist dein Kontostand momentan.");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        money.setItemMeta(meta);
        inv.setItem(23, money);

        ItemStack abheben = new ItemStack(Material.HOPPER, 1);
        meta = abheben.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "GELD ABHEBEN");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore = new ArrayList<>();
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Hebe hier dein Geld ab.");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        abheben.setItemMeta(meta);
        inv.setItem(19, abheben);

        ItemStack einzahlen = new ItemStack(Material.ANVIL, 1);
        meta = einzahlen.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "GELD EINZAHLEN");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore = new ArrayList<>();
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Zahle hier dein Geld ein.");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        einzahlen.setItemMeta(meta);
        inv.setItem(21, einzahlen);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        meta = filler.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Nichts hier.");
        lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.GRAY + "Nichts zu sehen hier.");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        filler.setItemMeta(meta);
        for (int x = 0; x < 44; x++) {
            if (x != 19 && x != 21 && x != 23 && x != 25) {
                inv.setItem(x, filler);
            }
        }

        return inv;
    }

    public Inventory createPickUpInv(Player player) {
        Inventory inv = Bukkit.createInventory(player, 45, "GELD ABHEBEN");
        String bank = "Bank." + player.getUniqueId();
        String history = "History." + player.getUniqueId();
        float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);


        List<String> lore = new ArrayList<>();
        ItemStack oneFourth = new ItemStack(Material.DROPPER, 1);
        ItemMeta meta = oneFourth.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Hebe 25% ab.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Hebe nun " + Math.round(bankAmount / 4.00F) + "$ ab!");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        oneFourth.setItemMeta(meta);
        inv.setItem(19, oneFourth);

        lore = new ArrayList<>();
        ItemStack half = new ItemStack(Material.DROPPER, 1);
        meta = half.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Hebe 50% ab.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Hebe nun " + Math.round(bankAmount / 2.00F) + "$ ab!");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        half.setItemMeta(meta);
        inv.setItem(21, half);

        lore = new ArrayList<>();
        ItemStack all = new ItemStack(Material.DROPPER, 1);
        meta = all.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Hebe alles ab.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Hebe nun " + bankAmount + "$ ab!");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        all.setItemMeta(meta);
        inv.setItem(23, all);

        lore = new ArrayList<>();
        ItemStack custom = new ItemStack(Material.DROPPER, 1);
        meta = custom.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Hebe eine eigene Menge ab.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Maximal: " + bankAmount + "$!");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        custom.setItemMeta(meta);
        inv.setItem(25, custom);


        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        meta = filler.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Nichts hier.");
        lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.GRAY + "Nichts zu sehen hier.");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        filler.setItemMeta(meta);
        for (int x = 0; x < 44; x++) {
            if (x != 19 && x != 21 && x != 23 && x != 25) {
                inv.setItem(x, filler);
            }
        }

        return inv;
    }

    public Inventory createDepositInv(Player player) {
        Inventory inv = Bukkit.createInventory(player, 45, "GELD EINZAHLEN");
        String purse = "Player." + player.getUniqueId();
        String history = "History." + player.getUniqueId();
        float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);


        List<String> lore = new ArrayList<>();
        ItemStack oneFourth = new ItemStack(Material.DROPPER, 1);
        ItemMeta meta = oneFourth.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Zahle 25% ein.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Zahle nun " + Math.round(purseAmount / 4.00F) + "$ ein!");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        oneFourth.setItemMeta(meta);
        inv.setItem(19, oneFourth);

        lore = new ArrayList<>();
        ItemStack half = new ItemStack(Material.DROPPER, 1);
        meta = half.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Zahle 50% ein.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Zahle nun " + Math.round(purseAmount / 2.00F) + "$ ein!");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        half.setItemMeta(meta);
        inv.setItem(21, half);

        lore = new ArrayList<>();
        ItemStack all = new ItemStack(Material.DROPPER, 1);
        meta = all.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Zahle alles ein.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Zahle nun " + purseAmount + "$ ein!");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        all.setItemMeta(meta);
        inv.setItem(23, all);

        lore = new ArrayList<>();
        ItemStack custom = new ItemStack(Material.DROPPER, 1);
        meta = custom.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Zahle eine eigene Menge ein.");
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Maximal: " + purseAmount + "$!");
        lore.add(2, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        custom.setItemMeta(meta);
        inv.setItem(25, custom);


        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        meta = filler.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Nichts hier.");
        lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.GRAY + "Nichts zu sehen hier.");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        filler.setItemMeta(meta);
        for (int x = 0; x < 44; x++) {
            if (x != 19 && x != 21 && x != 23 && x != 25) {
                inv.setItem(x, filler);
            }
        }

        return inv;
    }

    public Inventory createItemBuy(Material mat, String name) {
        Inventory inv = Bukkit.createInventory(null, 45, name + " Kauf");

        ItemStack eins = new ItemStack(mat, 1);
        ItemMeta meta = eins.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "1x " + name);
        List<String> lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Kaufe 1 Mal " + name + "!");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        eins.setItemMeta(meta);
        inv.setItem(19, eins);
        lore.clear();

        ItemStack halb = new ItemStack(mat, 32);
        meta = halb.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "32x " + name);
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Kaufe 32 Mal " + name + "!");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        halb.setItemMeta(meta);
        inv.setItem(21, halb);
        lore.clear();

        ItemStack stack = new ItemStack(mat, 64);
        meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "64x " + name);
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Kaufe 64 Mal " + name + "!");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        inv.setItem(23, stack);
        lore.clear();

        ItemStack custom = new ItemStack(mat, 100);
        meta = custom.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Custom Menge");
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Kaufe eine Custom Menge an " + name + "!");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        custom.setItemMeta(meta);
        inv.setItem(25, custom);
        lore.clear();
        inv.setMaxStackSize(100);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        meta = filler.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Nichts hier.");
        lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.GRAY.toString() + "Nichts zu sehen hier.");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        filler.setItemMeta(meta);
        for (int x = 0; x < 44; x++) {
            if (x != 19 && x != 21 && x != 23 && x != 25) {
                inv.setItem(x, filler);
            }
        }
        return inv;
    }

    public Inventory createItemSell(Material mat, String name) {
        Inventory inv = Bukkit.createInventory(null, 45, name + " Verkauf");

        ItemStack eins = new ItemStack(mat, 1);
        ItemMeta meta = eins.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "1x " + name);
        List<String> lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Verkaufe 1 Mal " + name + "!");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        eins.setItemMeta(meta);
        inv.setItem(19, eins);
        lore.clear();

        ItemStack halb = new ItemStack(mat, 32);
        meta = halb.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "32x " + name);
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Verkaufe 32 Mal " + name + "!");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        halb.setItemMeta(meta);
        inv.setItem(21, halb);
        lore.clear();

        ItemStack stack = new ItemStack(mat, 64);
        meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "64x " + name);
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Verkaufe 64 Mal " + name + "!");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        inv.setItem(23, stack);
        lore.clear();

        ItemStack custom = new ItemStack(mat, 100);
        meta = custom.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Custom Menge");
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Verkaufe eine Custom Menge an " + name + "!");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        custom.setItemMeta(meta);
        inv.setItem(25, custom);
        lore.clear();
        inv.setMaxStackSize(100);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        meta = filler.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Nichts hier.");
        lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        lore.add(1, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "Nichts zu sehen hier.");
        lore.add(2, ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "                                 ");
        meta.setLore(lore);
        filler.setItemMeta(meta);
        for (int x = 0; x < 44; x++) {
            if (x != 19 && x != 21 && x != 23 && x != 25) {
                inv.setItem(x, filler);
            }
        }
        return inv;
    }

}
