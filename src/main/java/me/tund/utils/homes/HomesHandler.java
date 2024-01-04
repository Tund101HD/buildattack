package me.tund.utils.homes;

import me.tund.main.BuildAttack;
import me.tund.utils.ErrorObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

public class HomesHandler {

    private BuildAttack plugin;

    public HomesHandler(BuildAttack plugin) {
        this.plugin = plugin;
    }

    public ErrorObject createHome(UUID player, Location location, String name) {
        checkHashMapEntry(player);
        List<HashMap<Location, String>> list = plugin.homesMap.get(player);
        if (isValidHome(player, location) || isValidHomeName(player, name))
            return new ErrorObject("Sorry, aber du besitzt bereits ein Home an der Stelle oder mit dem Namen!", false);
        if (list.size() + 1 > 4) {
            return new ErrorObject("Sorry, aber die maximale Anzahl an Homes ist 4!", false);
        }
        HashMap<Location, String> home = new HashMap<>();
        home.put(location, name);
        list.add(home);
        plugin.homesMap.put(player, list);
        return new ErrorObject("Dein Home an der Stelle X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ() + " wurde erstellt.", true);
    }

    public ErrorObject deleteHome(UUID player, Location location) {
        checkHashMapEntry(player);
        List<HashMap<Location, String>> list = plugin.homesMap.get(player);
        for (HashMap<Location, String> m : list) {
            if (m.containsKey(location)) list.remove(m);
        }
        if (!isValidHome(player, location))
            return new ErrorObject("Sorry, aber etwas ist schiefgelaufen (Das Home existiert nicht.)", false);
        plugin.homesMap.put(player, list);
        return new ErrorObject("Dein Home an der Stelle X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ() + " wurde gelöscht.", true);
    }

    public ErrorObject deleteHomeByName(UUID player, String name) {
        checkHashMapEntry(player);
        if (!isValidHomeName(player, name))
            return new ErrorObject("Sorry, aber du hast kein Home namens " + name + "!", false);
        List<HashMap<Location, String>> list = plugin.homesMap.get(player);
        Location loc = new Location(Bukkit.getWorld(player), 0, 0, 0);
        for (HashMap<Location, String> m : list) {
            if (m.containsValue(name)) {
                loc = getHomeByString(player, name);
                list.remove(m);
            }
        }
        plugin.homesMap.put(player, list);
        return new ErrorObject("Dein Home an der Stelle X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ() + " wurde gelöscht.", true);
    }

    public List<HashMap<Location, String>> getPlayerHomes(UUID player) {
        checkHashMapEntry(player);
        return plugin.homesMap.get(player);
    }

    public Location getHomeByString(UUID player, String name) {
        checkHashMapEntry(player);
        if (!isValidHomeName(player, name)) return null;
        Location loc = null;
        for (HashMap<Location, String> m : getPlayerHomes(player)) {
            for (Map.Entry e : m.entrySet()) {
                if (e.getValue().equals(name)) loc = (Location) e.getKey();
            }
        }

        return loc;
    }

    public String getHomeName(UUID player, Location location) {
        checkHashMapEntry(player);
        if (!isValidHome(player, location)) return "";
        String name = "";
        for (HashMap<Location, String> m : getPlayerHomes(player)) {
            if (m.containsKey(location)) name = m.get(location);
        }
        return name;
    }

    public boolean isValidHome(UUID player, Location location) {
        checkHashMapEntry(player);
        List<HashMap<Location, String>> list = plugin.homesMap.get(player);
        boolean isValid = false;
        for (HashMap<Location, String> l : list) {
            if (l.containsKey(location)) isValid = true;
        }

        return isValid;
    }

    public boolean isValidHomeName(UUID player, String name) {
        checkHashMapEntry(player);
        List<HashMap<Location, String>> list = plugin.homesMap.get(player);
        boolean isValid = false;
        for (HashMap<Location, String> l : list) {
            for (Map.Entry e : l.entrySet())
                if (e.getValue().equals(name)) isValid = true;

        }
        return isValid;
    }

    public ErrorObject createClanHome(String name, Location location, String homeName) {
        checkHashMapEntryClan(name);
        List<HashMap<Location, String>> list = getClanHomes(name);
        for (HashMap<Location, String> m : list) {
            if (m.containsKey(location))
                return new ErrorObject("Sorry, aber es ist bereits ein Clan-Home an der Stelle vorhanden!", false);
        }
        if (list.size() + 1 > 8)
            return new ErrorObject("Sorry, aber dein Clan hat die maximale Anzahl an Homes erreicht (8)!", false);
        HashMap<Location, String> map = new HashMap<>();
        map.put(location, homeName);
        list.add(map);
        plugin.clanHomes.put(name, list);
        return new ErrorObject("Es wurde ein Clan-Home an der Stelle X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ() + "erstellt.", true);
    }

    public ErrorObject deleteClanHome(String name, Location location) {
        checkHashMapEntryClan(name);
        List<HashMap<Location, String>> list = getClanHomes(name);
        if (isValidClanHome(name, location))
            return new ErrorObject("Sorry, aber dieser Ort ist bereits ein Clan-Home", false);
        for (HashMap<Location, String> m : list) {
            if (m.containsKey(location)) list.remove(m);
        }
        plugin.clanHomes.put(name, list);
        return new ErrorObject("Du hast der Clan-Home bei X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ() + " gelöscht.", true);
    }

    public ErrorObject deleteClanHomeByName(String name, String homeName) {
        checkHashMapEntryClan(name);
        List<HashMap<Location, String>> list = getClanHomes(name);
        if (!isValidClanHomeName(name, homeName))
            return new ErrorObject("Sorry, aber du hast kein Clan mit diesm Namen", false);
        for (HashMap<Location, String> m : list) {
            if (m.containsValue(name)) list.remove(m);
        }
        plugin.clanHomes.put(name, list);
        Location loc = getClanHomeByName(name, homeName);
        return new ErrorObject("Du hast der Clan-Home bei X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ() + " gelöscht.", true);
    }

    public boolean isValidClanHome(String name, Location location) {
        checkHashMapEntryClan(name);
        List<HashMap<Location, String>> list = getClanHomes(name);
        boolean isValid = false;
        for (HashMap<Location, String> m : list) {
            if (m.containsKey(location)) isValid = true;
        }
        return isValid;
    }

    public boolean isValidClanHomeName(String name, String homeName) {
        checkHashMapEntryClan(name);
        List<HashMap<Location, String>> list = getClanHomes(name);
        boolean isValid = false;
        for (HashMap<Location, String> m : list) {
            if (m.containsValue(homeName)) isValid = true;
        }
        return isValid;
    }

    public Location getClanHomeByName(String name, String homeName) {
        checkHashMapEntryClan(name);
        if (!isValidClanHomeName(name, homeName)) return null;
        Location loc = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
        List<HashMap<Location, String>> list = getClanHomes(name);
        for (HashMap<Location, String> m : list) {
            if (m.containsValue(homeName)) {
                for (Map.Entry e : m.entrySet()) loc = (Location) e.getKey();
            }
        }
        return loc;
    }

    public List<HashMap<Location, String>> getClanHomes(String name) {
        checkHashMapEntryClan(name);
        return plugin.clanHomes.get(name);
    }

    public void checkHashMapEntry(UUID player) {
        if (!plugin.homesMap.containsKey(player)) plugin.homesMap.put(player, new ArrayList<>());
    }

    public void checkHashMapEntryClan(String name) {
        if (!plugin.clanHomes.containsKey(name)) plugin.clanHomes.put(name, new ArrayList<>());
    }

}
