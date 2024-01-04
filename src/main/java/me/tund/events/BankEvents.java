package me.tund.events;

import me.tund.main.BuildAttack;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class BankEvents implements Listener {

    private BuildAttack plugin;

    public BankEvents(BuildAttack plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("Bankkonto")) {
            Inventory inv = e.getClickedInventory();
            e.setCancelled(true);
            if (inv.getType() == InventoryType.PLAYER)
                return;
            int slot = e.getSlot();
            if (slot == 19) {
                Inventory inv2 = plugin.gui.createPickUpInv(p);
                p.openInventory(inv2);
                p.updateInventory();
            } else if (slot == 21) {
                Inventory inv3 = plugin.gui.createDepositInv(p);

                p.openInventory(inv3);
                p.updateInventory();
            }
        } else if (e.getView().getTitle().equals("GELD ABHEBEN")) {
            Inventory inv = e.getClickedInventory();
            e.setCancelled(true);
            if (inv.getType() == InventoryType.PLAYER)
                return;
            int slot = e.getSlot();
            String bank = "Bank." + p.getUniqueId();
            String purse = "Player." + p.getUniqueId();
            String history = "History." + p.getUniqueId();
            float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);
            float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);
            if (slot == 19) {
                plugin.economy.getConfig().set(bank, bankAmount * 0.75F);
                plugin.economy.getConfig().set(purse, purseAmount + bankAmount * 0.25F);
                plugin.economy.getConfig().set(history, bankAmount * 0.25F + "$ von der Bank abgehoben.");
                plugin.economy.saveConfig();
                p.sendMessage(ChatColor.GREEN + "[Economy] Du hast " + bankAmount * 0.25 + "$ aus deinem Konto abgehoben.");
                Inventory inv2 = plugin.gui.createPickUpInv(p);
                p.openInventory(inv2);
            } else if (slot == 21) {
                plugin.economy.getConfig().set(bank, bankAmount * 0.50F);
                plugin.economy.getConfig().set(purse, purseAmount + bankAmount * 0.50F);
                plugin.economy.getConfig().set(history, bankAmount * 0.50F + "$ von der Bank abgehoben.");
                plugin.economy.saveConfig();
                p.sendMessage(ChatColor.GREEN + "[Economy] Du hast " + bankAmount * 0.50 + "$ aus deinem Konto abgehoben.");
                Inventory inv2 = plugin.gui.createPickUpInv(p);
                p.openInventory(inv2);
            } else if (slot == 23) {
                plugin.economy.getConfig().set(bank, 0.00F);
                plugin.economy.getConfig().set(purse, purseAmount + bankAmount);
                plugin.economy.getConfig().set(history, bankAmount + "$ von der Bank abgehoben.");
                plugin.economy.saveConfig();
                p.sendMessage(ChatColor.GREEN + "[Economy] Du hast " + bankAmount + "$ aus deinem Konto abgehoben.");
                Inventory inv2 = plugin.gui.createPickUpInv(p);
                p.openInventory(inv2);
            } else if (slot == 25) {
                final String[] input = {"0"};
                openAnvil(p, bank, purse, history, bankAmount, purseAmount);
            }
        } else if (e.getView().getTitle().equals("GELD EINZAHLEN")) {
            Inventory inv = e.getClickedInventory();
            e.setCancelled(true);
            if (inv.getType() == InventoryType.PLAYER)
                return;
            int slot = e.getSlot();
            String bank = "Bank." + p.getUniqueId();
            String purse = "Player." + p.getUniqueId();
            String history = "History." + p.getUniqueId();
            float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);
            float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);
            if (slot == 19) {
                plugin.economy.getConfig().set(bank, bankAmount + purseAmount * 0.25F);
                plugin.economy.getConfig().set(purse, purseAmount * 0.75F);
                plugin.economy.getConfig().set(history, purseAmount * 0.25F + "$ eingezahlt.");
                plugin.economy.saveConfig();
                p.sendMessage(ChatColor.GREEN + "[Economy] Du hast " + purseAmount * 0.25 + "$ auf dein Konto eingezahlt.");
                p.closeInventory();
                Inventory inv2 = plugin.gui.createDepositInv(p);
                p.openInventory(inv2);
            } else if (slot == 21) {
                plugin.economy.getConfig().set(bank, bankAmount + purseAmount * 0.50F);
                plugin.economy.getConfig().set(purse, purseAmount * 0.50F);
                plugin.economy.getConfig().set(history, purseAmount * 0.50F + "$ eingezahlt.");
                plugin.economy.saveConfig();
                p.sendMessage(ChatColor.GREEN + "[Economy] Du hast " + purseAmount * 0.50 + "$ auf dein Konto eingezahlt.");
                Inventory inv2 = plugin.gui.createDepositInv(p);
                p.openInventory(inv2);
            } else if (slot == 23) {
                plugin.economy.getConfig().set(bank, bankAmount + purseAmount);
                plugin.economy.getConfig().set(purse, 0.00F);
                plugin.economy.getConfig().set(history, purseAmount + "$ eingezahlt.");
                plugin.economy.saveConfig();
                p.sendMessage(ChatColor.GREEN + "[Economy] Du hast " + purseAmount + "$ auf dein Konto eingezahlt.");
                Inventory inv2 = plugin.gui.createDepositInv(p);
                p.openInventory(inv2);
            } else if (slot == 25) {
                openAnvil(p, bank, purse, history, bankAmount, purseAmount);
            }
        }


        //FIXME Mies unfertiger Bazar, aber egal lol

        /* else if (e.getView().getTitle().contains("Verkauf")) {
            Inventory inv = e.getClickedInventory();
            e.setCancelled(true);
            if (inv.getType() == InventoryType.PLAYER)
                return;
            int slot = e.getSlot();
            String bank = "Bank." + p.getUniqueId();
            String purse = "Player." + p.getUniqueId();
            String history = "History." + p.getUniqueId();
            float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);
            float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);
            switch (slot) {

                case 19:
                    String[] m = e.getView().getTitle().split(" ");
                    String material = m[0];


                    switch (material) {
                        case "Diamant":
                            ItemStack item = new ItemStack(Material.DIAMOND, 1);
                            List<SellOrder> diamonds = plugin.getDiamonds();
                            if (!(diamonds.size() < 1)) {
                                SellOrder smallest = diamonds.get(0);
                                SellOrder largest = diamonds.get(0);
                                for (int x = 0; x < diamonds.size(); x++) {
                                    SellOrder sell = diamonds.get(x);
                                    if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                                    if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
                                }

                                if (smallest.getAmount() - 1 > 0) {
                                    float price = smallest.getPricePer();
                                    if (bankAmount + purseAmount < price) {
                                        p.closeInventory();
                                        p.updateInventory();
                                        p.sendMessage(ChatColor.RED + "[Economy] Sorry, aber du hast nicht genug Geld!");
                                        return;
                                    }
                                    if (bankAmount - price < 0) {
                                        float newprice = price - bankAmount;
                                        plugin.economy.getConfig().set(bank, 0.00F);
                                        plugin.economy.getConfig().set(purse, purseAmount - newprice);
                                        plugin.economy.getConfig().set(history, price + "$ für einen Diamanten ausgegeben.");
                                        plugin.economy.saveConfig();
                                    } else {
                                        plugin.economy.getConfig().set(bank, bankAmount - price);
                                        plugin.economy.getConfig().set(history, price + "$ für einen Diamanten ausgegeben.");
                                        plugin.economy.saveConfig();
                                    }
                                    Player owner = smallest.getOwnerPlayer();
                                    UUID ownerID = smallest.getOwner();
                                    float prices = smallest.getPrice();
                                    int amount = smallest.getAmount() - 1;
                                    String ID = smallest.getID();

                                    diamonds.remove(smallest);
                                    SellOrder order = new SellOrder(owner, ownerID, new ItemStack(Material.DIAMOND), prices, ID, amount);
                                    diamonds.add(order);

                                    p.sendMessage(ChatColor.GREEN + "[Economy] Du hast einen Diamanten von " + smallest.getOwnerName() + " gekauft!");
                                    if (smallest.getOwnerPlayer().isOnline() && smallest.getOwnerPlayer() != null) {
                                        smallest.getOwnerPlayer().sendMessage(ChatColor.GREEN + "[Economy] Der Spieler " + p.getName()
                                                + " hat einen Diamanten für " + price + "$ von dir gekauft!");
                                    }
                                    HashMap<Integer, ItemStack> stash;
                                    stash = p.getInventory().addItem(item);
                                    if (!stash.isEmpty()) {
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Da dein Inventar nicht genug Platz hatte, wurden einige Items gespeichert.");
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Benutze /stash um die Items in dein (geleertes) Inventar zu geben, oder /drop um" + "\n" +
                                                "die übrigen Items auf den Boden fallen zu lassen!");
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Der Stash wird nach einer Zeit unwiederruflich geleert!");
                                        if(plugin.stashMap.containsKey(p.getUniqueId()+"Diamonds")){
                                            HashMap<Integer, ItemStack> old =  plugin.stashMap.get(p.getUniqueId()+"Diamonds");
                                            Iterator it = stash.entrySet().iterator();
                                            int count = 0;
                                            while(it.hasNext()){
                                                count ++;
                                            }
                                            for(ItemStack stack : old.values()){
                                                stash.put(count+1, stack);
                                                count++;
                                            }
                                        }
                                        plugin.stashMap.put(p.getUniqueId()+"Diamonds", stash);
                                    }
                                }else{
                                    //Wenn amount nicht größer als 0 ...
                                }
                            }

                        case "Emerald":
                            item = new ItemStack(Material.EMERALD, 1);
                            diamonds = plugin.getEmeralds();
                            if (!(diamonds.size() < 1)) {
                                SellOrder smallest = diamonds.get(0);
                                SellOrder largest = diamonds.get(0);
                                for (int x = 0; x < diamonds.size(); x++) {
                                    SellOrder sell = diamonds.get(x);
                                    if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                                    if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
                                }

                                if (smallest.getAmount() - 1 > 0) {
                                    float price = smallest.getPricePer();
                                    if (bankAmount + purseAmount < price) {
                                        p.closeInventory();
                                        p.updateInventory();
                                        p.sendMessage(ChatColor.RED + "[Economy] Sorry, aber du hast nicht genug Geld!");
                                        return;
                                    }
                                    if (bankAmount - price < 0) {
                                        float newprice = price - bankAmount;
                                        plugin.economy.getConfig().set(bank, 0.00F);
                                        plugin.economy.getConfig().set(purse, purseAmount - newprice);
                                        plugin.economy.getConfig().set(history, price + "$ für einen Emerald ausgegeben.");
                                        plugin.economy.saveConfig();
                                    } else {
                                        plugin.economy.getConfig().set(bank, bankAmount - price);
                                        plugin.economy.getConfig().set(history, price + "$ für einen Emerald ausgegeben.");
                                        plugin.economy.saveConfig();
                                    }
                                    Player owner = smallest.getOwnerPlayer();
                                    UUID ownerID = smallest.getOwner();
                                    float prices = smallest.getPrice();
                                    int amount = smallest.getAmount() - 1;
                                    String ID = smallest.getID();

                                    diamonds.remove(smallest);
                                    SellOrder order = new SellOrder(owner, ownerID, new ItemStack(Material.EMERALD), prices, ID, amount);
                                    diamonds.add(order);

                                    p.sendMessage(ChatColor.GREEN + "[Economy] Du hast einen Emerald von " + smallest.getOwnerName() + " gekauft!");
                                    if (smallest.getOwnerPlayer().isOnline() && smallest.getOwnerPlayer() != null) {
                                        smallest.getOwnerPlayer().sendMessage(ChatColor.GREEN + "[Economy] Der Spieler " + p.getName()
                                                + " hat einen Emerald für " + price + "$ von dir gekauft!");
                                    }
                                    HashMap<Integer, ItemStack> stash;
                                    stash = p.getInventory().addItem(item);
                                    if (!stash.isEmpty()) {
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Da dein Inventar nicht genug Platz hatte, wurden einige Items gespeichert.");
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Benutze /stash um die Items in dein (geleertes) Inventar zu geben, oder /drop um" + "\n" +
                                                "die übrigen Items auf den Boden fallen zu lassen!");
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Der Stash wird nach einer Zeit unwiederruflich geleert!");
                                        if(plugin.stashMap.containsKey(p.getUniqueId()+"Emeralds")){
                                            HashMap<Integer, ItemStack> old =  plugin.stashMap.get(p.getUniqueId()+"Emeralds");
                                            Iterator it = stash.entrySet().iterator();
                                            int count = 0;
                                            while(it.hasNext()){
                                                count ++;
                                            }
                                            for(ItemStack stack : old.values()){
                                                stash.put(count+1, stack);
                                                count++;
                                            }
                                        }
                                        plugin.stashMap.put(p.getUniqueId()+"Emeralds", stash);
                                    }
                                }else{
                                    //Wenn amount nicht größer als 0 ...
                                }
                            }
                        case "Netherite":
                            item = new ItemStack(Material.NETHERITE_INGOT, 1);
                            diamonds = plugin.getNetherite();
                            if (!(diamonds.size() < 1)) {
                                SellOrder smallest = diamonds.get(0);
                                SellOrder largest = diamonds.get(0);
                                for (int x = 0; x < diamonds.size(); x++) {
                                    SellOrder sell = diamonds.get(x);
                                    if (sell.getPricePer() > largest.getPricePer()) largest = sell;
                                    if (sell.getPricePer() < smallest.getPricePer()) smallest = sell;
                                }

                                if (smallest.getAmount() - 1 > 0) {
                                    float price = smallest.getPricePer();
                                    if (bankAmount + purseAmount < price) {
                                        p.closeInventory();
                                        p.updateInventory();
                                        p.sendMessage(ChatColor.RED + "[Economy] Sorry, aber du hast nicht genug Geld!");
                                        return;
                                    }
                                    if (bankAmount - price < 0) {
                                        float newprice = price - bankAmount;
                                        plugin.economy.getConfig().set(bank, 0.00F);
                                        plugin.economy.getConfig().set(purse, purseAmount - newprice);
                                        plugin.economy.getConfig().set(history, price + "$ für einen Netherite ausgegeben.");
                                        plugin.economy.saveConfig();
                                    } else {
                                        plugin.economy.getConfig().set(bank, bankAmount - price);
                                        plugin.economy.getConfig().set(history, price + "$ für einen Netherite ausgegeben.");
                                        plugin.economy.saveConfig();
                                    }
                                    Player owner = smallest.getOwnerPlayer();
                                    UUID ownerID = smallest.getOwner();
                                    float prices = smallest.getPrice();
                                    int amount = smallest.getAmount() - 1;
                                    String ID = smallest.getID();

                                    diamonds.remove(smallest);
                                    SellOrder order = new SellOrder(owner, ownerID, new ItemStack(Material.NETHERITE_INGOT), prices, ID, amount);
                                    diamonds.add(order);

                                    p.sendMessage(ChatColor.GREEN + "[Economy] Du hast einen Netherite von " + smallest.getOwnerName() + " gekauft!");
                                    if (smallest.getOwnerPlayer().isOnline() && smallest.getOwnerPlayer() != null) {
                                        smallest.getOwnerPlayer().sendMessage(ChatColor.GREEN + "[Economy] Der Spieler " + p.getName()
                                                + " hat einen Netherite für " + price + "$ von dir gekauft!");
                                    }
                                    HashMap<Integer, ItemStack> stash;
                                    stash = p.getInventory().addItem(item);
                                    if (!stash.isEmpty()) {
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Da dein Inventar nicht genug Platz hatte, wurden einige Items gespeichert.");
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Benutze /stash um die Items in dein (geleertes) Inventar zu geben, oder /drop um" + "\n" +
                                                "die übrigen Items auf den Boden fallen zu lassen!");
                                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[Warning] Der Stash wird nach einer Zeit unwiederruflich geleert!");
                                        if(plugin.stashMap.containsKey(p.getUniqueId()+"Emeralds")){
                                            HashMap<Integer, ItemStack> old =  plugin.stashMap.get(p.getUniqueId()+"Netherite");
                                            Iterator it = stash.entrySet().iterator();
                                            int count = 0;
                                            while(it.hasNext()){
                                                count ++;
                                            }
                                            for(ItemStack stack : old.values()){
                                                stash.put(count+1, stack);
                                                count++;
                                            }
                                        }
                                        plugin.stashMap.put(p.getUniqueId()+"Netherite", stash);
                                    }
                                }else{
                                    //Wenn amount nicht größer als 0 ...
                                }
                            }
                        case "Ghasttear":
                            //Ghasttear
                        case "Gunpowder":
                            //Gunpowder
                        case "Gold":
                            //Gold
                    }


            }
        }*/
    }

    private void openAnvil(Player p, String bank, String purse, String history, float bankAmount, float purseAmount) {
        new AnvilGUI.Builder().onClose(stateSnapshot -> {
            stateSnapshot.getPlayer().sendMessage(ChatColor.GREEN + "[Bank] Transaktion abgeschlossen.");
        }).onClick((slots, stateSnapshot) -> {  //Returns a List of Response-Actions!
            if (slots != AnvilGUI.Slot.OUTPUT) {
                return Collections.emptyList();
            }
            String text = stateSnapshot.getText().replaceAll(",", ".");
            float amount;
            try {
                amount = Float.parseFloat(text);
            } catch (NumberFormatException exception) {
                stateSnapshot.getPlayer().sendMessage(ChatColor.RED + "[Error] Dein Input ist keine Zahl!");
                return AnvilGUI.Response.close();
            }
            if (amount > bankAmount) {
                stateSnapshot.getPlayer().sendMessage(ChatColor.RED + "[Economy] Sorry, aber du hast nicht genug Geld!");
                return AnvilGUI.Response.close();
            }
            plugin.economy.getConfig().set(bank, bankAmount - amount);
            plugin.economy.getConfig().set(purse, purseAmount + amount);
            plugin.economy.getConfig().set(history, amount + "$ von der Bank abgehoben.");
            plugin.economy.saveConfig();
            return AnvilGUI.Response.close();

        }).itemLeft(new ItemStack(Material.HOPPER)).title("Gib eine custom Menge ein!").plugin(plugin).open(p);
    }
}
