package com.github.Hafixion;

import com.github.Hafixion.managers.EncryptionManager;
import org.apache.commons.validator.routines.EmailValidator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class iProtectorMain extends JavaPlugin {
    // Config Options, plus verified ips in order to avoid making too many calls to the IP fraud detector website
    private static iProtectorMain plugin;
    public iProtectorConfig config;
    protected EncryptionManager encryptor;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;

        if (!getConfig().getBoolean("enabled")) {
            getLogger().severe("IPROTECTOR DISABLED, CHECK CONFIG.YML");
            this.setEnabled(false);
            return;
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
            return;
        }

        // Check if the key is set to the default value.
        if (config.encrypt) {
            if (config.getKey().equalsIgnoreCase("NULL")) {
                getLogger().severe("Encryption was enabled but key was not yet, change the key in config.yml to a unique value.");
                this.setEnabled(false);
                return;
            }
        } else getLogger().warning("Encryption is disabled, it's recommended to enable it to improve data security.");

        // Check if Valid Contact Info.
        if (config.getProtect() == 3 && !EmailValidator.getInstance().isValid(config.getContact())) {
            getLogger().severe("PROTECTION LEVEL IS 3 YET CONTACT INFO IS INVALID, PLEASE CHANGE CONFIG.YML TO A VALID EMAIL.");
            this.setEnabled(false);
            return;
        }

        // Initialize Encryption
        encryptor = new EncryptionManager(config.getKey());

        // Register Listener
        //Bukkit.getPluginManager().registerEvents(new iProtectorListener(), this);

        // Register Command
        getCommand("/iprotect").setExecutor(new iProtectorCommand());

        getLogger().info("iProtector loaded successfully.");

    }

    @Override
    public void onDisable() {
        // Save the config, including flagged and verified ips.
        if (config != null) {
            try {
                config.save(new File("plugins/iProtector/config.yml"), new File("plugins/iProtector/data.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        getServer().getConsoleSender().sendMessage("Reloading iProtector...");

        // OnDisable
        if (config != null) {
            try {
                config.save(new File("plugins/iProtector/config.yml"), new File("plugins/iProtector/data.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        encryptor = new EncryptionManager(config.getKey());

        getServer().getConsoleSender().sendMessage("iProtector reloaded successfully.");
    }

    public static iProtectorMain getInstance() {
        return plugin;
    }
}
