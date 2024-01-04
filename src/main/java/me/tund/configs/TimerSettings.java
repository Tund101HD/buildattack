package me.tund.configs;

import me.tund.main.BuildAttack;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class TimerSettings {
    private BuildAttack plugin;
    private FileConfiguration dataconfig = null;
    private File configFile = null;

    public TimerSettings(BuildAttack plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }


    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "timer.yml");

        }
        this.dataconfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("timer.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataconfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataconfig == null) {
            reloadConfig();
        }

        return this.dataconfig;
    }

    public void saveConfig() {
        if (this.dataconfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save data to" + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "timer.yml");
        }

        if (!this.configFile.exists()) {
            this.plugin.saveResource("timer.yml", false);
        }
    }
}
