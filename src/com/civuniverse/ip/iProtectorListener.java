package com.civuniverse.ip;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.json.JSONException;
import org.json.JSONObject;

public class iProtectorListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) throws JSONException {
        // Check if they're in the blacklist or whitelist

        // Whitelist check
        if (iProtectorMain.config.getWhitelisted_names().contains(event.getName())) {
            event.allow();
            Bukkit.getLogger().info(event.getName() + " is whitelisted.");
            return;
        }
        if (iProtectorMain.config.getWhitelisted_ips().contains(event.getAddress().getHostAddress())) {
            event.allow();
            Bukkit.getLogger().info(event.getAddress().getHostAddress() + " is whitelisted.");
            return;
        }
        if (iProtectorMain.config.getWhitelisted_uuids().contains(event.getUniqueId())) {
            event.allow();
            Bukkit.getLogger().info(event.getUniqueId().toString() + " is whitelisted.");
            return;
        }

        // Blacklist check
        if (iProtectorMain.config.getBlacklisted_names().contains(event.getName())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYour Username is blacklisted.\n§c" + event.getName());
            Bukkit.getLogger().info(event.getName() + " is blacklisted.");
            return;
        }
        if (iProtectorMain.config.getBlacklisted_ips().contains(event.getAddress().getHostAddress())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYour IP is blacklisted.\n§c" + event.getAddress().getHostAddress());
            Bukkit.getLogger().info(event.getAddress().getHostAddress() + " is blacklisted.");
            return;
        }
        if (iProtectorMain.config.getBlacklisted_uuids().contains(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYour UUID is blacklisted.\n§c" + event.getUniqueId().toString());
            Bukkit.getLogger().info(event.getUniqueId().toString() + " is blacklisted.");
            return;
        }

        // Check verified and flagged ips.
        if (iProtectorMain.config.getVerifiedips().contains(event.getAddress().getHostAddress())) {
            event.allow();
            Bukkit.getLogger().info(event.getName() + " has a safe IP address. (local)");
            return;
        }
        if (iProtectorMain.config.getFlaggedips().contains(event.getAddress().getHostAddress())) {
            if (iProtectorMain.config.prevent)
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYou are using a VPN or Proxy.");
            Bukkit.getLogger().info(event.getName() + " logged in with a VPN or Proxy. (local)");
            return;
        }

        // Retrieve the IP's status.
        ConnectionManager manager = new ConnectionManager();
        JSONObject status;
        try {
            status = manager.getIpStatus(event.getAddress().getHostAddress());
        } catch (Exception e) {
            // Exception while getting IP,
            e.printStackTrace();
            event.allow();
            return;
        }

        // If there's an error in getting the IP status.
        if (status.getString("status") == "error") {
            event.allow();
            Bukkit.getLogger().info("ERROR WHILE GETTING STATUS FOR " + event.getName());
            return;
        }

        // todo add block-level 3
        switch (status.getJSONObject("data").getInt("block")) {
            case 0:
                event.allow();
                Bukkit.getLogger().info(event.getName() + " has a safe IP address.");
                // Verify the address locally.
                iProtectorMain.config.verify(event.getAddress().getHostAddress());
                return;
            case 1:
                if (iProtectorMain.config.prevent)
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYou are using a VPN or Proxy.");
                Bukkit.getLogger().info(event.getName() + " logged in with a VPN or Proxy.");
                // Saving the flagged IP
                iProtectorMain.config.addFlag(event.getAddress().getHostAddress());

                // Alert players
                if (iProtectorMain.config.isAlert()) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if (player.hasPermission("iprotector.alert"))
                            player.sendMessage("§c" + event.getName() + " attempted to connect with a VPN or Proxy or malicious IP.");
                    });
                }
                return;
            case 2:
                if (iProtectorMain.config.getBlock() == 2) {
                    if (iProtectorMain.config.prevent)
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cSuspicious IP");
                    Bukkit.getLogger().info(event.getName() + " has a suspicious IP, disallowed them to join.");

                    // Alert Staff
                    if (iProtectorMain.config.isAlert()) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            if (player.hasPermission("iprotector.alert"))
                                player.sendMessage("§c" + event.getName() + " attempted to connect with a suspicious IP.");
                        });
                    }
                } else {
                    event.allow();
                    Bukkit.getLogger().info(event.getName() + " has a suspicious IP.");

                    // Alert Staff
                    if (iProtectorMain.config.isAlert()) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            if (player.hasPermission("iprotector.alert"))
                                player.sendMessage("§c" + event.getName() + " connected with a suspicious IP.");
                        });
                    }
                }
        }
    }
}
