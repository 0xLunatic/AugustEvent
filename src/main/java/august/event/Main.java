package august.event;

import august.event.command.AugustCommand;
import august.event.data.DataManager;
import august.event.data.LocationManager;
import august.event.db.Database;
import august.event.head.Heads;
import august.event.listener.DistanceListener;
import august.event.listener.GUIListener;
import august.event.listener.InteractListener;
import august.event.listener.JoinListener;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public final class Main extends JavaPlugin {
    public DataManager data;
    public LocationManager location;
    private Database database;
    public static Economy econ = null;

    @Override
    public void onEnable() {
        // Plugin startup logic

        data = new DataManager(this);
        location = new LocationManager(this);

        System.out.println("Plugin Event 17 Agustus Enabled!");

        if (!setupEconomy() ) {
            System.out.println("AugustEvent - Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()){
            saveResource("config.yml", true);
        }else{
            saveDefaultConfig();
        }
        try {
            this.database = new Database(this);
            database.initializeDatabase();
        } catch (SQLException ex) {
            System.out.println("Failed to connect to the Database and create Tables!");
            ex.printStackTrace();
        }

        new DistanceListener(this).runTaskTimer(this, 20, 20);

        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        Objects.requireNonNull(getCommand("buyrelic")).setExecutor(new AugustCommand(this));
        Objects.requireNonNull(getCommand("setcenterevent")).setExecutor(new AugustCommand(this));
        Objects.requireNonNull(getCommand("17agustus")).setExecutor(new AugustCommand(this));
        Objects.requireNonNull(getCommand("buyrpglimited")).setExecutor(new AugustCommand(this));
        Objects.requireNonNull(getCommand("showkemerdekaanpoints")).setExecutor(new AugustCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static ItemStack createSkull(String url, String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        if (url.isEmpty()) return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            assert headMeta != null;
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
    public static ItemStack getHead(String name) {
        for (Heads head : Heads.values()) {
            if (head.getName().equals(name)) {
                return head.getItemStack();
            }
        }
        return null;

    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
    public Database getDatabase(){
        return database;
    }
}
