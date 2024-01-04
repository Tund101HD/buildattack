package me.tund.utils.gui;

import me.tund.main.BuildAttack;
import me.tund.utils.data.DataHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {

    private BuildAttack plugin;
    private GuiHandler gui;
    private DataHandler handler;

    public ItemHandler(BuildAttack plugin) {
        this.plugin = plugin;
        this.gui = new GuiHandler();
        this.handler = plugin.getHandler();
    }


    public ItemStack getItem(int def, int amount) {
        if (amount > 64)
            amount = 64;

        ItemStack item = null;
        ItemMeta meta = new ItemStack(Material.BARRIER, amount).getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "[Severe Error #0] Oh no! Something went wrong. ");
        switch (def) {
            case 0:
                item = new ItemStack(Material.STICK, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.MAGIC + "k" + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Stick of Truth" + ChatColor.GOLD.toString() + ChatColor.MAGIC + "k");
                List<String> lore = new ArrayList<>();
                lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
                lore.add(1, ChatColor.LIGHT_PURPLE + "The Stick of Truth holds the power to change the reality");
                lore.add(2, ChatColor.LIGHT_PURPLE + "of your surroundings. Use it wisely or it may turn against");
                lore.add(3, ChatColor.LIGHT_PURPLE + "you at some point!");
                lore.add(4, ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "RIGHT CLICK!");
                lore.add(5, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
                meta.setLore(lore);

            case 1:
                item = new ItemStack(Material.GOLDEN_SHOVEL, amount);
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.MAGIC + "k" + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Shovel of Gods" + ChatColor.GOLD.toString() + ChatColor.MAGIC + "k");
                lore = new ArrayList<>();
                lore.add(0, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
                lore.add(1, ChatColor.LIGHT_PURPLE + "The Shovel of Gods can dig almost any ground it faces. It was");
                lore.add(2, ChatColor.LIGHT_PURPLE + "forged by Kratos in the deep depths of a Quaesar, now ready");
                lore.add(3, ChatColor.LIGHT_PURPLE + "to prevail a eternity!");
                lore.add(4, ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
                meta.setLore(lore);


        }
        item.setItemMeta(meta);
        return item;
    }
}
