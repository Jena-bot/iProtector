package com.civuniverse.ip;

import com.civuniverse.ip.libraries.EncryptionManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class iProtectorMain extends JavaPlugin {
    // Config Options, plus verified ips in order to avoid making too many calls to the IP fraud detector website
    public boolean enabled = false;
    protected static iProtectorConfig config;

    @Override
    public void onEnable() {

        if (!getConfig().getBoolean("enabled")) {
            getLogger().severe("IPROTECTOR DISABLED, CHECK CONFIG.YML");
            this.setEnabled(false);
        }

        // Load Verified IPs
        YamlConfiguration verify = new YamlConfiguration();
        try {
            verify.load(new File("plugins/iProtector/data.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            // What. the. fuck.
            e.printStackTrace();
        }

        // Load Config
        try {
            config = new iProtectorConfig(getConfig(), verify);
        } catch (InvalidConfigurationException e) {
            getServer().getConsoleSender().sendMessage(e.getMessage());
            this.setEnabled(false);
        }

        // Check if the key is set to the default value.
        if (config.encrypt) {
            if (config.getKey().equalsIgnoreCase("NULL")) {
                getLogger().severe("Encryption was enabled but key was not yet, change the key in config.yml to a unique value.");
                this.setEnabled(false);
            }
        } else getLogger().warning("Encryption is disabled, it's recommended to enable it to improve data security.");

        // Initialize Encryption
        EncryptionManager.main(new String[]{config.getKey()});

        getLogger().info("iProtector loaded successfully.");

    }

    public void reload() {
        getServer().getConsoleSender().sendMessage("Reloading iProtector...");

        if (!getConfig().getBoolean("enabled")) {
            getServer().getConsoleSender().sendMessage("IPROTECTOR DISABLED, CHECK CONFIG.YML");
            this.setEnabled(false);
        }

        // Load Verified IPs
        YamlConfiguration verify = new YamlConfiguration();
        try {
            verify.load(new File("plugins/iProtector/data.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            // What. the. fuck.
            e.printStackTrace();
        }

        // Load Config
        try {
            config = new iProtectorConfig(getConfig(), verify);
        } catch (InvalidConfigurationException e) {
            getServer().getConsoleSender().sendMessage(e.getMessage());
            this.setEnabled(false);
        }

        getServer().getConsoleSender().sendMessage("iProtector reloaded successfully.");
    }
}
