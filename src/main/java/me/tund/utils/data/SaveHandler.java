package me.tund.utils.data;

import me.tund.main.BuildAttack;
import me.tund.utils.gui.SellOrder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SaveHandler {

    private BuildAttack plugin;

    public SaveHandler(BuildAttack plugin) {
        this.plugin = plugin;
    }

    public void saveClaims() {
        if (plugin.pClaims.getConfig().contains("Claims.")) {
            plugin.pClaims.deleteConfig();
            plugin.pClaims.reloadConfig();
        }
        for (Map.Entry<String, UUID> data : plugin.claimedChunk.entrySet()) {
            plugin.pClaims.getConfig().set("Claims." + data.getKey(), data.getValue().toString());
        }
        plugin.pClaims.saveConfig();
    }

    public void saveClaimList() {
        if (plugin.pClaims.getConfig().contains("ClaimList.")) {
            plugin.pClaims.deleteConfig();
            plugin.pClaims.reloadConfig();
        }
        for (Map.Entry<UUID, List<String>> data : plugin.claimList.entrySet()) {
            plugin.pClaims.getConfig().set("ClaimList." + data.getKey(), data.getValue());
        }
        plugin.pClaims.saveConfig();
    }

    public void saveTrusts() {
        if (plugin.pClaims.getConfig().contains("Trusts.")) {
            plugin.pClaims.deleteConfig();
            plugin.pClaims.reloadConfig();
        }
        for (Map.Entry<String, List<String>> data : plugin.trust.entrySet()) {
            plugin.pClaims.getConfig().set("Trusts." + data.getKey(), data.getValue());
        }
        plugin.pClaims.saveConfig();
    }

    public void saveChunkTrusts() {
        if (plugin.pClaims.getConfig().contains("ChunkTrust.")) {
            plugin.pClaims.deleteConfig();
            plugin.pClaims.reloadConfig();
        }
        for (Map.Entry<String, List<String>> data : plugin.trustList.entrySet()) {
            plugin.pClaims.getConfig().set("ChunkTrust." + data.getKey(), data.getValue());
        }
        plugin.pClaims.saveConfig();
    }

    public void saveMembers() {
        if (plugin.clans.getConfig().contains("Members.")) {
            plugin.clans.deleteConfig();
            plugin.clans.reloadConfig();
        }
        for (Map.Entry<String, List<UUID>> data : plugin.clanMembers.entrySet()) {
            List<String> list = new ArrayList<>();
            List<UUID> ids = data.getValue();
            for (int x = 0; x < ids.size(); x++) {
                list.add(x, ids.get(x).toString());
            }
            plugin.clans.getConfig().set("Members." + data.getKey(), list);

        }
        plugin.clans.saveConfig();
    }

    public void saveLeaders() {
        if (plugin.clans.getConfig().contains("Leaders.")) {
            plugin.clans.deleteConfig();
            plugin.clans.reloadConfig();
        }
        for (Map.Entry<String, UUID> data : plugin.clanLeaders.entrySet()) {
            plugin.clans.getConfig().set("Leaders." + data.getKey(), data.getValue().toString());
        }
        plugin.clans.saveConfig();
    }

    public void savePrivacy() {
        if (plugin.clans.getConfig().contains("Privacy.")) {
            plugin.clans.deleteConfig();
            plugin.clans.reloadConfig();
        }
        for (Map.Entry<String, Boolean> data : plugin.privacy.entrySet()) {
            plugin.clans.getConfig().set("Privacy." + data.getKey(), data.getValue());
        }
        plugin.clans.saveConfig();
    }

    public void savePvp() {
        if (plugin.clans.getConfig().contains("PVP")) {
            plugin.clans.deleteConfig();
            plugin.clans.reloadConfig();
        }
        for (Map.Entry<String, Boolean> data : plugin.pvp.entrySet()) {
            plugin.clans.getConfig().set("PVP." + data.getKey(), data.getValue());
        }
        plugin.clans.saveConfig();
    }


    public void loadClaims() {
        ConfigurationSection configsec = plugin.pClaims.getConfig().getConfigurationSection("Claims");
        if (configsec == null)
            return;

        for (String chunk : configsec.getKeys(false)) {
            plugin.claimedChunk.put(chunk, UUID.fromString((String) configsec.get(chunk)));
        }
    }

    public void loadClaimList() {
        ConfigurationSection configsec = plugin.pClaims.getConfig().getConfigurationSection("ClaimList");
        if (configsec == null)
            return;

        for (String ID : configsec.getKeys(false)) {
            plugin.claimList.put(UUID.fromString(ID), (List<String>) configsec.get(ID));
        }
    }

    public void loadTrust() {
        ConfigurationSection configsec = plugin.pClaims.getConfig().getConfigurationSection("Trusts");
        if (configsec == null)
            return;

        for (String name : configsec.getKeys(false)) {
            plugin.trust.put(name, (List<String>) configsec.get(name));
        }
    }

    public void loadChunkTrust() {
        ConfigurationSection configsec = plugin.pClaims.getConfig().getConfigurationSection("ChunkTrust");
        if (configsec == null)
            return;

        for (String ID : configsec.getKeys(false)) {
            plugin.trustList.put(ID, (List<String>) configsec.get(ID));
        }
    }

    public void loadMembers() {
        ConfigurationSection configsec = plugin.clans.getConfig().getConfigurationSection("Members");
        if (configsec == null)
            return;

        for (String ID : configsec.getKeys(false)) {
            List<String> list = (List<String>) configsec.get(ID);
            List<UUID> ids = new ArrayList<>();
            for (String b : list) {
                ids.add(UUID.fromString(b));
            }
            plugin.clanMembers.put(ID, ids); // <-- does this work? Now it does lol.
        }
    }

    public void loadLeaders() {
        ConfigurationSection configsec = plugin.clans.getConfig().getConfigurationSection("Leaders");
        if (configsec == null)
            return;

        for (String ID : configsec.getKeys(false)) {
            plugin.clanLeaders.put(ID, UUID.fromString((String) configsec.get(ID)));
        }
    }

    public void loadPrivacy() {
        ConfigurationSection configsec = plugin.clans.getConfig().getConfigurationSection("Privacy");
        if (configsec == null)
            return;

        for (String ID : configsec.getKeys(false)) {
            plugin.privacy.put(ID, (Boolean) configsec.get(ID));
        }
    }

    public void loadPvp() {
        ConfigurationSection configsec = plugin.clans.getConfig().getConfigurationSection("PVP");
        if (configsec == null)
            return;

        for (String ID : configsec.getKeys(false)) {
            plugin.pvp.put(ID, (Boolean) configsec.get(ID));
        }
    }

    //LOL das ist auch noch nicht fertig :DDDDD
    public void loadSellOrders() {
        ConfigurationSection configsec = plugin.sells.getConfig().getConfigurationSection("SellOrders");
        if (configsec == null)
            return;

        for (String ID : configsec.getKeys(false)) {
            String name = (String) configsec.get(ID + ".Owner");
            String item = (String) configsec.get(ID + ".Item");
            int amount = (int) configsec.get(ID + ".Amount");
            float price = (float) configsec.get(ID + ".Price");
            String id = (String) configsec.get(ID + ".ID");
            SellOrder s;
            switch (item) {
                case "Diamond":
                    s = new SellOrder((Player) Bukkit.getOfflinePlayer(name),
                            UUID.fromString(ID), new ItemStack(Material.DIAMOND), price, id, amount);
                    plugin.diamonds.add(s);
                    //TODO break statement
                case "Emerald":
                    s = new SellOrder((Player) Bukkit.getOfflinePlayer(name),
                            UUID.fromString(ID), new ItemStack(Material.EMERALD), price, id, amount);
                    plugin.emeralds.add(s);
                case "Netherite":
                    s = new SellOrder((Player) Bukkit.getOfflinePlayer(name),
                            UUID.fromString(ID), new ItemStack(Material.NETHERITE_INGOT), price, id, amount);
                    plugin.netherite.add(s);
                case "GunPowder":
                    s = new SellOrder((Player) Bukkit.getOfflinePlayer(name),
                            UUID.fromString(ID), new ItemStack(Material.GUNPOWDER), price, id, amount);
                    plugin.gunpowder.add(s);
                case "Gold":
                    s = new SellOrder((Player) Bukkit.getOfflinePlayer(name),
                            UUID.fromString(ID), new ItemStack(Material.GOLD_INGOT), price, id, amount);
                    plugin.gold.add(s);
                case "GhastTear":
                    s = new SellOrder((Player) Bukkit.getOfflinePlayer(name),
                            UUID.fromString(ID), new ItemStack(Material.GHAST_TEAR), price, id, amount);
                    plugin.ghasttear.add(s);
            }

        }
    }

    // Das auch nicht :DDD I want to die
    public void saveSellOrders() {
        List<SellOrder> all = plugin.getDiamonds();
        all.addAll(plugin.getEmeralds());
        all.addAll(plugin.getGhasttear());
        all.addAll(plugin.getGold());
        all.addAll(plugin.getGunpowder());
        all.addAll(plugin.getNetherite());
        if (plugin.sells.getConfig().contains("SellOrders.")) {
            plugin.sells.deleteConfig();
            plugin.sells.reloadConfig();
        }
        for (SellOrder s : all) {
            HashMap<String, String> map = s.toHashMap();
            String id = map.get("UUID");
            plugin.sells.getConfig().set("SellOrders." + id + ".Owner", map.get("Owner"));
            plugin.sells.getConfig().set("SellOrders." + id + ".Item", map.get("Item"));
            plugin.sells.getConfig().set("SellOrders." + id + ".Amount", map.get("Amount"));
            plugin.sells.getConfig().set("SellOrders." + id + ".Price", map.get("Price"));
            plugin.sells.getConfig().set("SellOrders." + id + ".ID", map.get("ID"));
        }
        plugin.saveConfig();
    }

    public void loadALl() {
        loadClaims();
        loadTrust();
        loadChunkTrust();
        loadMembers();
        loadLeaders();
        loadPrivacy();
        loadClaimList();
        loadPvp();
        loadSellOrders();

    }

    public void saveAll() {
        saveClaimList();
        saveClaims();
        saveChunkTrusts();
        saveTrusts();
        saveMembers();
        saveLeaders();
        savePrivacy();
        savePvp();
        saveSellOrders();
        plugin.settings.saveConfig();
        plugin.timers.saveConfig();
    }
}
