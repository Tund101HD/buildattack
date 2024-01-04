package me.tund.main;

import me.tund.commands.claim;
import me.tund.commands.clans;
import me.tund.commands.economy;
import me.tund.commands.settings;
import me.tund.configs.*;
import me.tund.events.BankEvents;
import me.tund.events.ChunkEvents;
import me.tund.events.HandlingEvents;
import me.tund.events.PvpEvents;
import me.tund.utils.TeamHandler;
import me.tund.utils.data.DataHandler;
import me.tund.utils.data.SaveHandler;
import me.tund.utils.eventUtils.ScoreboardHandler;
import me.tund.utils.gui.GuiHandler;
import me.tund.utils.gui.ItemHandler;
import me.tund.utils.gui.SellOrder;
import me.tund.utils.homes.HomesHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class BuildAttack extends JavaPlugin {

    public static BuildAttack instance;
    public HashMap<String, UUID> claimedChunk;
    public HashMap<UUID, List<String>> claimList;
    public HashMap<String, List<String>> trust;
    public HashMap<String, List<String>> trustList;
    public HashMap<String, UUID> clanLeaders;
    public HashMap<String, List<UUID>> clanMembers;
    public HashMap<String, Boolean> privacy;
    public HashMap<String, Boolean> pvp;
    public HashMap<String, PermissionAttachment> perms;
    public HashMap<String, HashMap> stashMap;
    public HashMap<UUID, List<HashMap<Location, String>>> homesMap;
    public HashMap<String, List<HashMap<Location, String>>> clanHomes;
    public List<SellOrder> diamonds;
    public List<SellOrder> netherite;
    public List<SellOrder> emeralds;
    public List<SellOrder> gold;
    public List<SellOrder> gunpowder;
    public List<SellOrder> ghasttear;
    public SaveHandler save;
    public PlayerEconomy economy;
    public Settings settings;
    public TimerSettings timers;
    public HomeSettings homeSettings;
    public PlayerClaims pClaims;
    public Clans clans;
    public SellOrders sells;
    public GuiHandler gui;
    public ItemHandler items;
    public HandlingEvents handlingEvents;
    public ScoreboardHandler score;
    public PluginManager pm;
    private DataHandler handler;
    private TeamHandler teams;
    private HomesHandler homes;
    private Timer timer;

    public static BuildAttack getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().log(Level.INFO, "Init");
        this.score = new ScoreboardHandler(this);
        pm = Bukkit.getPluginManager();
        pm.registerEvents(new ChunkEvents(this), this);
        pm.registerEvents(new HandlingEvents(this), this);
        pm.registerEvents(new BankEvents(this), this);
        pm.registerEvents(new PvpEvents(this), this);
        pm.registerEvents(score, this);

        this.economy = new PlayerEconomy(this);
        this.settings = new Settings(this);
        this.pClaims = new PlayerClaims(this);
        this.clans = new Clans(this);
        this.sells = new SellOrders(this);
        this.timers = new TimerSettings(this);
        this.homeSettings = new HomeSettings(this);
        if (!settings.getConfig().contains("Mode")) {
            settings.getConfig().set("Mode", "Chest-Only");
            settings.saveConfig();
            this.getLogger().log(Level.INFO, "Setze Claim-Mode auf den Standard Chest-Only, da keine Einstellung gefunden wurde!");
        }
        if (!settings.getConfig().contains("MoneyMultiplier")) {
            settings.getConfig().set("MoneyMultiplier", 1.00F);
            settings.saveConfig();
            this.getLogger().log(Level.INFO, "Setze Money-Multiplier auf den Standard 100%, da keine Einstellung gefunden wurde!");
        }
        if (!settings.getConfig().contains("Economy")) {
            settings.getConfig().set("Economy", true);
            settings.saveConfig();
            this.getLogger().log(Level.INFO, "Setze Economy auf den Standard aktiviert, da keine Einstellung gefunden wurde!");
        }

        if (!settings.getConfig().contains("Event.Enabled")) {
            settings.getConfig().set("Event", false);
            settings.saveConfig();
            this.getLogger().log(Level.INFO, "Setze Event auf den Standard deaktiviert, da keine Einstellung gefunden wurde!");
        }

        if (!settings.getConfig().contains("Change-Tablist")) {
            settings.getConfig().set("Change-Tablist", true);
            this.getLogger().log(Level.INFO, "Setze Change-Tablist zu Build-Attack-Standard, da keine Einstellung gefunden wurde!");
        }

        if (!settings.getConfig().contains("Tablist-Header")) {
            settings.getConfig().set("Tablist-Header", "&7&m" + "               " + "&7[&e&l" + "Build Attack" + "&7]" + "&7&m" + "               "
                    + "\n" + "&e&lBuild Attack Season 4!");
            this.getLogger().log(Level.INFO, "Setze Tablist-Header zu Build-Attack-Standard, da keine Einstellung gefunden wurde!");
        }
        if (!settings.getConfig().contains("Tablist-Footer")) {
            settings.getConfig().set("Tablist-Footer", "&d/eco help , /claim help , /clans help");
            this.getLogger().log(Level.INFO, "Setze Tablist-Footer zu Build-Attack-Standard, da keine Einstellung gefunden wurde!");
        }
        if (!settings.getConfig().contains("Public-PVP")) {
            settings.getConfig().set("Public-PVP", true);
            this.getLogger().log(Level.INFO, "Setze Public-PVP auf den Standard true, da keine Einstellung gefunden wurde!");
        }
        if (!settings.getConfig().contains("TNT-Damage")) {
            settings.getConfig().set("TNT-Damage", true);
            this.getLogger().log(Level.INFO, "Setze TNT-Damage auf den Standard true, da keine Einstellung gefunden wurde!");
        }

        if (!settings.getConfig().contains("C-Items")) {
            settings.getConfig().set("C-Items", false);
            this.getLogger().log(Level.INFO, "Setze Custom-Items auf den Standard false, da keine Einstellung gefunden wurde!");
        }

        if (!settings.getConfig().contains("C-Rules")) {
            settings.getConfig().set("C-Rules", false);
            this.getLogger().log(Level.INFO, "Setze Custom-Regeln auf den Standard false, da keine Einstellung gefunden wurde!");
        }


        this.diamonds = new ArrayList<>();
        this.netherite = new ArrayList<>();
        this.emeralds = new ArrayList<>();
        this.ghasttear = new ArrayList<>();
        this.gold = new ArrayList<>();
        this.gunpowder = new ArrayList<>();

        this.stashMap = new HashMap<>();
        this.gui = new GuiHandler();

        this.claimedChunk = new HashMap<>();
        this.clanMembers = new HashMap<>();
        this.clanLeaders = new HashMap<>();
        this.claimList = new HashMap<>();
        this.trust = new HashMap<>();
        this.trustList = new HashMap<>();
        this.privacy = new HashMap<>();
        this.perms = new HashMap<>();
        this.pvp = new HashMap<>();
        this.homesMap = new HashMap<>();


        this.teams = new TeamHandler();
        this.handler = new DataHandler(this);
        this.homes = new HomesHandler(this);
        this.handlingEvents = new HandlingEvents(this);
        this.save = new SaveHandler(this);
        save.loadALl();

        Bukkit.getPluginCommand("clans").setExecutor(new clans(this));
        Bukkit.getPluginCommand("claim").setExecutor(new claim(this));
        Bukkit.getPluginCommand("unclaim").setExecutor(new claim(this));
        Bukkit.getPluginCommand("trust").setExecutor(new claim(this));
        Bukkit.getPluginCommand("untrust").setExecutor(new claim(this));
        Bukkit.getPluginCommand("eco").setExecutor(new economy(this));
        Bukkit.getPluginCommand("settings").setExecutor(new settings(this));
        this.getLogger().log(Level.INFO, "Finished Init.");

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            BuildAttack.getInstance().getLogger().log(Level.INFO, "Autosaving all data...");
            save.saveAll();
        }, 0, 20 * 60 * 30);

        timer = new Timer(); //Instanziere einen neuen Timer, der auf einem eigenen Thread läuft
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { //Der Timer soll etwas alle x millisekunden ausführen, spezifiziert durch "period"
                for (Player p : Bukkit.getOnlinePlayers()) { //Loop durch alle Spieler
                    if (timers.getConfig().contains(p.getUniqueId().toString()))
                        timers.getConfig().set(p.getUniqueId().toString(), timers.getConfig().getInt(p.getUniqueId().toString()) + 1);
                    else
                        timers.getConfig().set(p.getUniqueId().toString(), 0);
                    timers.saveConfig(); //Erhöhe die Sekundenzahl um 1 und speicher den Wert
                    int seconds = timers.getConfig().getInt(p.getUniqueId().toString()); //Zeitumrechnung
                    int day = (int) TimeUnit.SECONDS.toDays(seconds);
                    long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
                    long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
                    long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
                    String timeString = "";
                    if (day > 0) {
                        timeString = String.format("%02dd " + "%02dh " + "%02dm " + "%02ds", day, hours, minutes, second);
                    } else {
                        timeString = String.format("%02dh " + "%02dm " + "%02ds", hours, minutes, second);
                    }
                    String message = ChatColor.GREEN + "Spielzeit: " + timeString;
                    if (timers.getConfig().getBoolean("Enabled-" + p.getUniqueId())) { //Zeige dem Spieler den Timer, wenn er ihn aktiviert hat
                        sendToActionBar(p, message);
                    }
                }
            }
        }, 0, 1000);


        if (Bukkit.getOnlinePlayers() != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                handler.createAtt(p);
                if (settings.getConfig().getBoolean("Change-Tablist")) {
                    p.setPlayerListHeader(teams.format((String) settings.getConfig().get("Tablist-Header")));
                    p.setPlayerListFooter(teams.format((String) settings.getConfig().get("Tablist-Footer")));
                }
                handlingEvents.start(p);
            }
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "Plugin wird entladen und alle Daten werden gesichert!");
        timer.cancel();
        save.saveAll();
        this.getLogger().log(Level.INFO, "Fertig.");
    }

    public void sendToActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    private boolean isSameDate(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    public DataHandler getHandler() {
        return this.handler;
    }

    public TeamHandler getTeams() {
        return this.teams;
    }

    public PlayerEconomy getEconomy() {
        return this.economy;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public List<SellOrder> getDiamonds() {
        return this.diamonds;
    }

    public List<SellOrder> getNetherite() {
        return this.netherite;
    }

    public List<SellOrder> getEmeralds() {
        return this.emeralds;
    }

    public List<SellOrder> getGhasttear() {
        return this.ghasttear;
    }

    public List<SellOrder> getGold() {
        return this.gold;
    }

    public List<SellOrder> getGunpowder() {
        return this.gunpowder;
    }
}
