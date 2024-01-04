package me.tund.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.tund.main.BuildAttack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class economy implements CommandExecutor {

    private BuildAttack plugin;

    public economy(BuildAttack plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("eco") || cmd.getName().equalsIgnoreCase("economy")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length < 1) {
                    p.sendMessage(ChatColor.GREEN + "[Economy-Help]" + "\n" +
                            ChatColor.STRIKETHROUGH + "                                                     ");
                    p.sendMessage(ChatColor.GREEN + "/economy pay (Spieler) [Menge]: Bezahle einen Spieler.");
                    p.sendMessage(ChatColor.GREEN + "/economy balance: Zeigt dir an, wie reich du bist.");
                    p.sendMessage(ChatColor.GREEN + "/economy baltop: Zeigt die 3 reichsten Spieler an.");
                    p.sendMessage(ChatColor.GREEN + "/economy bazaar: Öffnet den Bazaar.");
                    p.sendMessage(ChatColor.GREEN + "/economy bank: Öffnet das Bankmenü.");
                    p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                                     ");
                    return true;
                }
                if (!isEconomyActive()) {
                    p.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Command ist deaktiviert!");
                    return true;
                }
                String argument = args[0];
                if (argument.equalsIgnoreCase("pay")) {
                    return pay(p, args);
                } else if (argument.equalsIgnoreCase("bal") || argument.equalsIgnoreCase("balance")) {
                    return balance(p, args);
                } else if (argument.equalsIgnoreCase("baltop") || argument.equalsIgnoreCase("balancetop")) {
                    return balanceTop(p, args);
                } else if (argument.equalsIgnoreCase("adminpay")) {
                    return adminpay(p, args);
                } else if (argument.equalsIgnoreCase("bazaar") || argument.equalsIgnoreCase("bz") || argument.equalsIgnoreCase("markt")) {
                    return bazaar(p);
                } else if (argument.equalsIgnoreCase("bank") || argument.equalsIgnoreCase("bk")) {
                    return bank(p);
                } else {
                    p.sendMessage(ChatColor.GREEN + "[Economy-Help]" + "\n" +
                            ChatColor.STRIKETHROUGH + "                                                     ");
                    p.sendMessage(ChatColor.GREEN + "/economy pay (Spieler) [Menge]: Bezahle einen Spieler.");
                    p.sendMessage(ChatColor.GREEN + "/economy baltop: Zeigt die 3 reichsten Spieler an.");
                    p.sendMessage(ChatColor.GREEN + "/economy bazaar: Öffnet den Bazaar.");
                    p.sendMessage(ChatColor.GREEN + "/economy bank: Öffnet das Bankmenü.");
                    p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                                     ");
                    return true;
                }
            }
        }


        return false;
    }

    public boolean pay(Player sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "[Error] Du musst einen Spieler und/oder eine Menge angeben! (/eco pay Spieler Menge)");
            return true;
        }
        String payer = "Player." + sender.getUniqueId();

        if (!plugin.economy.getConfig().contains(payer)) {
            sender.sendMessage(ChatColor.DARK_RED + "[Internal Error] Du hast keinen Konto-Eintrag!");
            plugin.economy.getConfig().set(payer, 0.00F);
            plugin.economy.saveConfig();
            return true;
        }

        float senderMoney = (float) plugin.economy.getConfig().getDouble(payer);
        Player target = Bukkit.getPlayer(args[1]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "[Error] Der Spieler muss online sein!");
            return true;
        }

        String receiver = "Player." + target.getUniqueId();
        float targetMoney = (float) plugin.economy.getConfig().getDouble(receiver);

        String amountS = args[2];
        float amount;

        try {
            if (amountS.contains(",")) {
                amountS = amountS.replaceAll(",", ".");
            }
            amount = Float.parseFloat(amountS);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "[Error] Die angegebene Menge ist keine Zahl!");
            return false;
        }

        if (amount <= 100) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber die Menge darf nicht weniger als 100 sein!");
            return true;
        }

        if (senderMoney - amount < 0) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast nicht genug Geld mit dir!");
            return true;
        }

        plugin.economy.getConfig().set(payer, senderMoney - amount);
        plugin.economy.getConfig().set(receiver, targetMoney + amount);
        plugin.economy.saveConfig();
        sender.sendMessage(ChatColor.GOLD + "[Economy] Du hast dem Spieler " + target.getName() + " " + amount + "$ gegeben!");
        target.sendMessage(ChatColor.GOLD + "[Economy] Der Spieler " + sender.getName() + " hat dir " + amount + "$ gegeben!");

        return true;
    }

    public boolean adminpay(Player sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber nur ein Admin kann dies tun!");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "[Error] Falsche Command-Eingabe!");
            return true;
        }
        String name = args[1];
        float amount;
        try {
            String amountS = args[2];
            amountS = amountS.replaceAll(",", ".");
            amount = Float.parseFloat(amountS);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "[Error] Die angegebene Menge enthält keine Zahl!");
            return true;
        }

        if (Bukkit.getPlayer(name) == null || !Bukkit.getPlayer(name).isOnline()) {
            String uuid = getUUIDFromName(name);
            if (uuid.equals("er")) {
                sender.sendMessage(ChatColor.RED + "[Error] Spieler konnte nicht gefunden werden!");
                return true;
            }
            UUID id = UUID.fromString(uuid);
            if (!plugin.economy.getConfig().contains("Bank." + id))
                plugin.economy.getConfig().set("Bank." + id, 0.00F);

            float bankAmount = (float) plugin.economy.getConfig().getDouble("Bank." + id);
            plugin.economy.getConfig().set("Bank." + id, bankAmount + amount);
            sender.sendMessage(ChatColor.GREEN + "[Admins] Du hast dem Spieler " + name + " " + amount + "$ gegeben!");
            plugin.economy.saveConfig();
        } else {
            Player target = Bukkit.getPlayer(name);
            if (!plugin.economy.getConfig().contains("Bank." + target.getUniqueId()))
                plugin.economy.getConfig().set("Bank." + target.getUniqueId(), 0.00F);

            float bankAmount = (float) plugin.economy.getConfig().getDouble("Bank." + target.getUniqueId());
            plugin.economy.getConfig().set("Bank." + target.getUniqueId(), bankAmount + amount);
            plugin.economy.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "[Admins] Du hast dem Spieler " + name + " " + amount + "$ gegeben!");
            target.sendMessage(ChatColor.GREEN + "[Economy] Ein Admin hat deinen Kontostand um " + amount + "$ verändert!");
        }

        return true;
    }

    public boolean balance(Player sender, String[] args) {
        if (args.length == 1) { // no Player argument
            String purse = "Player." + sender.getUniqueId();
            String bank = "Bank." + sender.getUniqueId();
            if (!plugin.economy.getConfig().contains(purse)) {
                plugin.economy.getConfig().set(purse, 0.00F);
            }
            if (!plugin.economy.getConfig().contains(bank)) {
                plugin.economy.getConfig().set(bank, 0.00F);
            }
            float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);
            float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);
            sender.sendMessage(ChatColor.GOLD + "[Economy] So viel Geld hast du im Moment: ");
            sender.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
            sender.sendMessage(ChatColor.GOLD + "In deiner Purse: " + purseAmount + "$");
            sender.sendMessage(ChatColor.GOLD + "Auf deinem Bankkonto: " + bankAmount + "$");
            sender.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                   ");

        } else if (args.length > 1) {
            String name = args[1];
            UUID uuid;
            if (Bukkit.getPlayer(name) == null || !Bukkit.getPlayer(name).isOnline()) {
                if (!getUUIDFromName(name).equalsIgnoreCase("er")) {
                    uuid = UUID.fromString(getUUIDFromName(name));
                } else {
                    sender.sendMessage(ChatColor.RED + "[Error] Dieser Spieler existiert nicht!");
                    return true;
                }
            } else {
                uuid = Bukkit.getPlayer(name).getUniqueId();
            }

            String purse = "Player." + uuid;
            String bank = "Bank." + uuid;
            if (!plugin.economy.getConfig().contains(purse)) {
                plugin.economy.getConfig().set(purse, 0.00F);
                plugin.economy.saveConfig();
            }
            if (!plugin.economy.getConfig().contains(bank)) {
                plugin.economy.getConfig().set(bank, 0.00F);
                plugin.economy.saveConfig();
            }
            float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);
            float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);

            sender.sendMessage(ChatColor.GOLD + "[Economy] So viel Geld hat " + name + " im Moment: ");
            sender.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
            sender.sendMessage(ChatColor.GOLD + "In deren Purse: " + purseAmount + "$");
            sender.sendMessage(ChatColor.GOLD + "Auf deren Bankkonto: " + bankAmount + "$");
            sender.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                   ");
        }


        return true;
    }

    public boolean balanceTop(Player sender, String[] args) {
        ConfigurationSection cfgPurse = plugin.economy.getConfig().getConfigurationSection("Player");
        ConfigurationSection cfgBank = plugin.economy.getConfig().getConfigurationSection("Bank");
        if (cfgPurse == null || cfgBank == null) {
            plugin.getLogger().log(Level.SEVERE, "ConfigurationSections are null!");
            sender.sendMessage(ChatColor.DARK_RED + "[Fatal Error] Sorry, aber etwas schief gelaufen!");
            return false;
        }

        Map<String, Object> purseMap = cfgPurse.getValues(false);
        Map<String, Object> bankMap = cfgBank.getValues(false);
        Map<String, Object> addedMap = new HashMap<>();

        for (Map.Entry<String, Object> map : bankMap.entrySet()) {
            float f = ((Number) map.getValue()).floatValue();
            String key = map.getKey();

            float p = ((Number) purseMap.get(key)).floatValue();
            addedMap.put(key, p + f);
        }

        UUID topPlayer = null;
        UUID secondPlayer = null;
        UUID thirdPlayer = null;
        float highest = 0;
        float second = 0;
        float third = 0;
        for (Map.Entry<String, Object> map : addedMap.entrySet()) {
            float f = ((Number) map.getValue()).floatValue();
            UUID player = UUID.fromString(map.getKey());

            if (topPlayer == null) {
                topPlayer = player;
                highest = f;
                continue;
            } else if (secondPlayer == null) {
                if (f <= highest) {
                    secondPlayer = player;
                    second = f;
                } else {
                    secondPlayer = topPlayer;
                    second = highest;

                    topPlayer = player;
                    highest = f;
                }
                continue;
            } else if (thirdPlayer == null) {

                if (f < highest && f < second) {
                    thirdPlayer = player;
                    third = f;
                } else if (f <= highest) {
                    thirdPlayer = secondPlayer;
                    third = second;

                    secondPlayer = player;
                    second = f;
                } else if (f > highest) {
                    thirdPlayer = secondPlayer;
                    third = second;

                    secondPlayer = topPlayer;
                    second = highest;

                    topPlayer = player;
                    highest = f;
                }
                continue;
            }

            if (f > highest) {
                UUID last = topPlayer;
                float lastValue = highest;
                UUID lastSecond = secondPlayer;
                float lastSecondValue = second;

                topPlayer = player;
                highest = f;

                secondPlayer = last;
                second = lastValue;

                thirdPlayer = lastSecond;
                third = lastSecondValue;

            } else if (f <= highest && f > second) {
                UUID last = secondPlayer;
                float lastValue = second;

                secondPlayer = player;
                second = f;

                thirdPlayer = last;
                third = lastValue;

            } else if (f <= second && f > third) {
                thirdPlayer = player;
                third = f;
            }

        }
        sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "[Economy] Das sind die 3 Spieler mit dem meisten Geld!");
        sender.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                        ");
        if (Bukkit.getPlayer(topPlayer) == null || !Bukkit.getPlayer(topPlayer).isOnline()) {
            OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(topPlayer);
            sender.sendMessage(ChatColor.GOLD + "Der reichste Spieler: " + offPlayer.getName() + " mit " + highest + "$");
        } else {
            Player p = Bukkit.getPlayer(topPlayer);
            sender.sendMessage(ChatColor.GOLD + "Der reichste Spieler: " + p.getName() + " mit " + highest + "$");
        }
        if (Bukkit.getPlayer(secondPlayer) == null || !Bukkit.getPlayer(secondPlayer).isOnline()) {
            OfflinePlayer offPlayer2 = Bukkit.getOfflinePlayer(secondPlayer);
            sender.sendMessage(ChatColor.GOLD + "Der zweit-reichste Spieler: " + offPlayer2.getName() + " mit " + second + "$");
        } else {
            Player p2 = Bukkit.getPlayer(secondPlayer);
            sender.sendMessage(ChatColor.GOLD + "Der zweit-reichste Spieler: " + p2.getName() + " mit " + second + "$");
        }
        if (Bukkit.getPlayer(thirdPlayer) == null || !Bukkit.getPlayer(thirdPlayer).isOnline()) {
            OfflinePlayer offPlayer3 = Bukkit.getOfflinePlayer(thirdPlayer);
            sender.sendMessage(ChatColor.GOLD + "Der dritt-reichste Spieler: " + offPlayer3.getName() + " mit " + third + "$");
        } else {
            Player p3 = Bukkit.getPlayer(thirdPlayer);
            sender.sendMessage(ChatColor.GOLD + "Der dritt-reichste Spieler: " + p3.getName() + " mit " + third + "$");
        }
        sender.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "                                        ");

        return true;
    }


    public String getUUIDFromName(String name) {
        String uuid = "";
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            uuid = (((JsonObject) JsonParser.parseReader(in)).get("id")).toString().replaceAll("\"", "");
            uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            in.close();
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Unable to get UUID of: " + name + "!");
            uuid = "er";

        }
        return uuid;
    }

    public boolean bazaar(Player p) {
        p.sendMessage(ChatColor.RED + "[Error] Sorry, dieser Command ist momentan deaktiviert!");
        return true;
        //p.openInventory(plugin.gui.select);
        //return true;
    }

    public boolean bank(Player p) {
        Inventory inv = plugin.gui.createBankInv(p);
        p.openInventory(inv);
        return true;
    }

    public boolean isEconomyActive() {
        return plugin.settings.getConfig().get("Economy").equals(true);
    }
}


