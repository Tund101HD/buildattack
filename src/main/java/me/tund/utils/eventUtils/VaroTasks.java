package me.tund.utils.eventUtils;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VaroTasks {
    private static Map<UUID, Integer> TASKS = new HashMap<UUID, Integer>();
    private final UUID uuid;

    public VaroTasks(UUID uuid) {
        this.uuid = uuid;
    }

    public int getID() {
        return TASKS.get(uuid);
    }

    public void setID(int id) {
        TASKS.put(uuid, id);
    }

    public boolean hasID() {
        return TASKS.containsKey(uuid);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(TASKS.get(uuid));
        TASKS.remove(uuid);
    }
}
