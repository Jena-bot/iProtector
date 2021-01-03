package com.civuniverse.ip;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class iProtectorCommand implements CommandExecutor {
    public String[] info = new String[]{"§b§liProtector", "§8,_._._._._._._._._|__________________________________________________________,"
            , "|_|_|_|_|_|_|_|_|_|_________________________________________________________/", "                  !",
            "§3Developed by Hafixion, also known as Jena-bot",
            "&diProtector is a plugin to prevent VPNs and Proxies from logging onto your server.",
            "§dStopping players from joining with non-residential IP addresses and malicious ones."};
    public String prefix = "§6[§biProtector§6]§r";

    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {
        // Info Command
        if (args.length == 0) cs.sendMessage(info);

        switch (args[0]) {
            case "whitelist":
                if (args.length >= 2) switch (args[1]) {
                        case "add":
                            // For adding things to the whitelist.
                            if (args.length >= 4) switch (args[2]) {
                                case "uuid":
                                    List<UUID> uuidwhitelist = iProtectorMain.config.getWhitelisted_uuids();

                                    // Check if UUID is already whitelisted.
                                    if (uuidwhitelist.contains(UUID.fromString(args[3]))) {
                                        cs.sendMessage(prefix + "§cThat UUID is already whitelisted!");
                                        return false;
                                    }
                                    uuidwhitelist.add(UUID.fromString(args[3]));

                                    iProtectorMain.config.setWhitelisted_uuids(uuidwhitelist);
                                    cs.sendMessage(prefix + "§bAdded " + args[3] + " to the whitelist.");
                                case "ip":
                                    if (!args[3].contains(".")) {
                                        cs.sendMessage(prefix + "§cInvalid IP Address.");
                                        return false;
                                    }

                                    List<String> ipwhitelist = iProtectorMain.config.getWhitelisted_ips();

                                    // Check if IP is already whitelisted.
                                    if (ipwhitelist.contains(args[3])) {
                                        cs.sendMessage(prefix + "§cThat IP is already whitelisted!");
                                        return false;
                                    }
                                    ipwhitelist.add(args[3]);

                                    iProtectorMain.config.setWhitelisted_ips(ipwhitelist);
                                    cs.sendMessage(prefix + "§bAdded " + args[3] + " to the whitelist.");
                                case "name":
                                    List<String> namewhitelist = iProtectorMain.config.getWhitelisted_names();

                                    // Check if Name is already whitelisted.
                                    if (namewhitelist.contains(args[3])) {
                                        cs.sendMessage(prefix + "§c" + args[3] + " is already whitelisted!");
                                        return false;
                                    }
                                    namewhitelist.add(args[3]);

                                    iProtectorMain.config.setWhitelisted_names(namewhitelist);
                                    cs.sendMessage(prefix + "§bAdded " + args[3] + " to the whitelist.");
                            } else cs.sendMessage(prefix + "§cYou forgot to specify a uuid, ip, or name to whitelist!");
                            
                        case "remove":
                            if (args.length >= 4) switch (args[2]) {
                                case "uuid":
                                    List<UUID> uuidwhitelist = iProtectorMain.config.getWhitelisted_uuids();

                                    // Check if UUID is whitelisted.
                                    if (!uuidwhitelist.contains(UUID.fromString(args[3]))) {
                                        cs.sendMessage(prefix + "§cThat UUID isn't whitelisted");
                                        return false;
                                    }
                                    uuidwhitelist.remove(UUID.fromString(args[3]));

                                    iProtectorMain.config.setWhitelisted_uuids(uuidwhitelist);
                                    cs.sendMessage(prefix + "§bRemoved " + args[3] + " from the whitelist.");
                                case "ip":
                                    if (!args[3].contains(".")) {
                                        cs.sendMessage(prefix + "§cInvalid IP Address.");
                                        return false;
                                    }

                                    List<String> ipwhitelist = iProtectorMain.config.getWhitelisted_ips();

                                    // Check if IP is whitelisted.
                                    if (!ipwhitelist.contains(args[3])) {
                                        cs.sendMessage(prefix + "§cThat IP isn't whitelisted");
                                        return false;
                                    }
                                    ipwhitelist.remove(args[3]);

                                    iProtectorMain.config.setWhitelisted_ips(ipwhitelist);
                                    cs.sendMessage(prefix + "§bRemoved " + args[3] + " from the whitelist.");
                                case "name":
                                    List<String> namewhitelist = iProtectorMain.config.getWhitelisted_names();

                                    // Check if Name is whitelisted.
                                    if (!namewhitelist.contains(args[3])) {
                                        cs.sendMessage(prefix + "§c" + args[3] + " isn't whitelisted");
                                        return false;
                                    }
                                    namewhitelist.remove(args[3]);

                                    iProtectorMain.config.setWhitelisted_names(namewhitelist);
                                    cs.sendMessage(prefix + "§bRemoved " + args[3] + " from the whitelist.");
                            } else cs.sendMessage(prefix + "§cYou forgot to specify a uuid, ip, or name to unwhitelist!");
                    } else cs.sendMessage(prefix + "§cWhat do you want to do? add or remove someone from the whitelist?");
            case "blacklist":
                if (args.length >= 2) switch (args[1]) {
                    case "add":
                        // For adding things to the blacklist.
                        if (args.length >= 4) switch (args[2]) {
                            case "uuid":
                                List<UUID> uuidblacklist = iProtectorMain.config.getBlacklisted_uuids();

                                // Check if UUID is already blacklisted.
                                if (uuidblacklist.contains(UUID.fromString(args[3]))) {
                                    cs.sendMessage(prefix + "§cThat UUID is already blacklisted!");
                                    return false;
                                }
                                uuidblacklist.add(UUID.fromString(args[3]));

                                iProtectorMain.config.setBlacklisted_uuids(uuidblacklist);
                                cs.sendMessage(prefix + "§bAdded " + args[3] + " to the blacklist.");
                            case "ip":
                                if (!args[3].contains(".")) {
                                    cs.sendMessage(prefix + "§cInvalid IP Address.");
                                    return false;
                                }

                                List<String> ipblacklist = iProtectorMain.config.getBlacklisted_ips();

                                // Check if IP is already blacklisted.
                                if (ipblacklist.contains(args[3])) {
                                    cs.sendMessage(prefix + "§cThat IP is already blacklisted!");
                                    return false;
                                }
                                ipblacklist.add(args[3]);

                                iProtectorMain.config.setBlacklisted_ips(ipblacklist);
                                cs.sendMessage(prefix + "§bAdded " + args[3] + " to the Blacklist.");
                            case "name":
                                List<String> nameBlacklist = iProtectorMain.config.getBlacklisted_names();

                                // Check if Name is already Blacklisted.
                                if (nameBlacklist.contains(args[3])) {
                                    cs.sendMessage(prefix + "§c" + args[3] + " is already Blacklisted!");
                                    return false;
                                }
                                nameBlacklist.add(args[3]);

                                iProtectorMain.config.setBlacklisted_names(nameBlacklist);
                                cs.sendMessage(prefix + "§bAdded " + args[3] + " to the Blacklist.");
                        } else cs.sendMessage(prefix + "§cYou forgot to specify a uuid, ip, or name to Blacklist!");

                    case "remove":
                        if (args.length >= 4) switch (args[2]) {
                            case "uuid":
                                List<UUID> uuidBlacklist = iProtectorMain.config.getBlacklisted_uuids();

                                // Check if UUID is Blacklisted.
                                if (!uuidBlacklist.contains(UUID.fromString(args[3]))) {
                                    cs.sendMessage(prefix + "§cThat UUID isn't Blacklisted");
                                    return false;
                                }
                                uuidBlacklist.remove(UUID.fromString(args[3]));

                                iProtectorMain.config.setBlacklisted_uuids(uuidBlacklist);
                                cs.sendMessage(prefix + "§bRemoved " + args[3] + " from the Blacklist.");
                            case "ip":
                                if (!args[3].contains(".")) {
                                    cs.sendMessage(prefix + "§cInvalid IP Address.");
                                    return false;
                                }

                                List<String> ipBlacklist = iProtectorMain.config.getBlacklisted_ips();

                                // Check if IP is Blacklisted.
                                if (!ipBlacklist.contains(args[3])) {
                                    cs.sendMessage(prefix + "§cThat IP isn't Blacklisted");
                                    return false;
                                }
                                ipBlacklist.remove(args[3]);

                                iProtectorMain.config.setBlacklisted_ips(ipBlacklist);
                                cs.sendMessage(prefix + "§bRemoved " + args[3] + " from the Blacklist.");
                            case "name":
                                List<String> nameBlacklist = iProtectorMain.config.getBlacklisted_names();

                                // Check if Name is Blacklisted.
                                if (!nameBlacklist.contains(args[3])) {
                                    cs.sendMessage(prefix + "§c" + args[3] + " isn't Blacklisted");
                                    return false;
                                }
                                nameBlacklist.remove(args[3]);

                                iProtectorMain.config.setBlacklisted_names(nameBlacklist);
                                cs.sendMessage(prefix + "§bRemoved " + args[3] + " from the blacklist.");
                        } else cs.sendMessage(prefix + "§cYou forgot to specify a uuid, ip, or name to unblacklist!");
                } else cs.sendMessage(prefix + "§cWhat do you want to do? add or remove someone from the blacklist?");
            case "reload":
                iProtectorMain.getInstance().reload();
                cs.sendMessage(prefix + "§b Reloading...");
        }
        return false;
    }
}
