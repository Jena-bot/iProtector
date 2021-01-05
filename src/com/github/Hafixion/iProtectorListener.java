package com.github.Hafixion;

import com.github.Hafixion.managers.ConnectionManager;
import com.github.Hafixion.object.ipData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class iProtectorListener implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        // Initialize the threshold
        float threshold = iProtectorMain.getInstance().config.getThreshold() * 3;

        // Check if they're in the blacklist or whitelist

        // Whitelist check
        if (iProtectorMain.getInstance().config.getWhitelisted_names().contains(event.getName())) {
            event.allow();
            Bukkit.getLogger().info(event.getName() + " is whitelisted.");
            return;
        }
        if (iProtectorMain.getInstance().config.getWhitelisted_ips().contains(event.getAddress().getHostAddress())) {
            event.allow();
            Bukkit.getLogger().info(event.getAddress().getHostAddress() + " is whitelisted.");
            return;
        }
        if (iProtectorMain.getInstance().config.getWhitelisted_uuids().contains(event.getUniqueId())) {
            event.allow();
            Bukkit.getLogger().info(event.getUniqueId().toString() + " is whitelisted.");
            return;
        }

        // Blacklist check
        if (iProtectorMain.getInstance().config.getBlacklisted_names().contains(event.getName())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYour Username is blacklisted.\n§c" + event.getName());
            Bukkit.getLogger().info(event.getName() + " is blacklisted.");
            return;
        }
        if (iProtectorMain.getInstance().config.getBlacklisted_ips().contains(event.getAddress().getHostAddress())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYour IP is blacklisted.\n§c" + event.getAddress().getHostAddress());
            Bukkit.getLogger().info(event.getAddress().getHostAddress() + " is blacklisted.");
            return;
        }
        if (iProtectorMain.getInstance().config.getBlacklisted_uuids().contains(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYour UUID is blacklisted.\n§c" + event.getUniqueId().toString());
            Bukkit.getLogger().info(event.getUniqueId().toString() + " is blacklisted.");
            return;
        }

        // Check the local database
        for (ipData ip : iProtectorMain.getInstance().config.getLocalips()) {
            if (ip.getIp().equals(event.getAddress().getHostAddress())) {
                if (ip.getThreat() > threshold) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cBad IP Address Detected.");
                    Bukkit.getLogger().info(event.getName() + " logged in with a bad IP. (local)");
                } else {
                    event.allow();
                    Bukkit.getLogger().info(event.getName() + " has a safe IP address. (local)");
                }
                return;
            }
        }

        // Pull an IP Check on them, and add it to the database.
        ConnectionManager manager = new ConnectionManager(iProtectorMain.getInstance().config.getProtect());
        ipData data = manager.getIpStatus(event.getAddress().getHostAddress());

        if (data.getIp() == null) {
            Bukkit.getLogger().warning(event.getName() + " logged in with an invalid ip address.");
            return;
        }

        if (data.getThreat() > threshold) {
            if (iProtectorMain.getInstance().config.prevent) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cBad IP Address detected.");
            }
            Bukkit.getLogger().info(event.getName() + " has a bad IP, logging into the database...");
        } else {
            Bukkit.getLogger().info(event.getName() + " has a good ip, allowing them to join, logging into the database...");
        }
        iProtectorMain.getInstance().config.addIp(data);
    }
}
