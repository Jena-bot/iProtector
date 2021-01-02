package com.civuniverse.ip;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {
    // Config Options, plus verified ips in order to avoid making too many calls to the IP fraud detector website
    public boolean enabled = false;
    protected iProtectorConfig config;

    @Override
    public void onEnable() {
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
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {

    }
}
