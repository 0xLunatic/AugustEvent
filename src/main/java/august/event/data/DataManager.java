package august.event.data;

import august.event.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private final Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(Main plugin) {
        this.plugin = plugin;
        saveResource("storage.yml");
    }

    public void reloadConfig(String data) {
        if (this.configFile != null) {
            this.configFile = new File(this.plugin.getDataFolder(), data);
            this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

            InputStream defaultStream = this.plugin.getResource(data);
            if (defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                this.dataConfig.setDefaults(defaultConfig);
            }
        }
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), data);
            this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

            InputStream defaultStream = this.plugin.getResource(data);
            if (defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                this.dataConfig.setDefaults(defaultConfig);
            }
        }
    }

    public FileConfiguration getConfig(String data) {
        if (this.dataConfig == null)
            reloadConfig(data);
        return this.dataConfig;
    }

    public void saveConfig(String data) {
        if (this.dataConfig == null || this.configFile == null)
            return;
        try {
            this.getConfig(data).save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to" + this.configFile, e);
        }
    }

    public void saveDefaultConfig(String data) {
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), data);
        if (!this.configFile.exists()) {
            this.plugin.saveResource(data, false);
        }
    }
    public void saveResource(String data){
        this.configFile = new File(this.plugin.getDataFolder(), data);
        if (!this.configFile.exists()) {
            this.plugin.saveResource(data, false);
        }
    }
}
