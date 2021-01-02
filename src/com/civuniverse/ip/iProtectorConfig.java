package com.civuniverse.ip;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class iProtectorConfig {
    // Whether or not encryption is enabled. Using (REDACTED) encryption, key is the encryption key used.
    protected boolean encrypt;
    protected String key;

    // Whether or not to alert people with permission
    protected boolean alert;

    // Block level
    protected int block;

    // Verified IPs, using to avoid pinging the IP Fraud checker too much, it has a limit of 1k ip queries per day.
    protected List<String> verifiedips = new ArrayList<>();

    // Black/Whitelist
    protected List<UUID> whitelisted_uuids = new ArrayList<>();
    protected List<UUID> blacklisted_uuids = new ArrayList<>();

    protected List<String> whitelisted_ips = new ArrayList<>();
    protected List<String> blacklisted_ips = new ArrayList<>();

    protected List<String> whitelisted_names = new ArrayList<>();
    protected List<String> blacklisted_names = new ArrayList<>();

    protected iProtectorConfig(FileConfiguration config, FileConfiguration verified) throws InvalidConfigurationException {
        boolean[] error = {false, false, false, false};
        
        // import verified ips
        verifiedips.addAll(verified.getStringList("ips"));

        // import encryption config
        encrypt = config.getBoolean("encryption");
        if (encrypt) key = config.getString("key");
        else key = "disabled";

        // import block level and alerts
        block = config.getInt("block-level");
        alert = config.getBoolean("alert");

        // Whitelisted
        config.getStringList("whitelist").forEach(s -> {
            
            if (s.contains("(IP)")) {
                s = s.replace("(IP) ", "");
                whitelisted_ips.add(s);
                return;
            }
            
            if (s.contains("(UUID)")) {
                s = s.replace("(UUID) ", "");
                whitelisted_uuids.add(UUID.fromString(s));
                return;
            }
            
            if (s.contains("(Name)")) {
                s = s.replace("(Name) ", "");
                whitelisted_names.add(s);
                return;
            }

            error[0] = true;
        });
        if (error[0]) throw new InvalidConfigurationException("Whitelist Invalid!");


        // Blacklist
        config.getStringList("blacklist").forEach(s -> {

            if (s.contains("(IP)")) {
                s = s.replace("(IP) ", "");
                blacklisted_ips.add(s);
                return;
            }

            if (s.contains("(UUID)")) {
                s = s.replace("(UUID) ", "");
                blacklisted_uuids.add(UUID.fromString(s));
                return;
            }

            if (s.contains("(Name)")) {
                s = s.replace("(Name) ", "");
                blacklisted_names.add(s);
                return;
            }

            error[1] = true;
        });
        if (error[1]) throw new InvalidConfigurationException("Blacklist Invalid!");
    }
    
    // Save Function
    protected void save(File file, File verify) throws IOException {
        YamlConfiguration yaml;
        
        // Save verified ips
        yaml = new YamlConfiguration();
        yaml.set("verified", verifiedips);
        yaml.save(verify);
        
        // Save Whitelist
        yaml = new YamlConfiguration();
        List<String> whitelist = new ArrayList<>();
        
        whitelisted_names.forEach(s -> whitelist.add("(Name) " + s));
        whitelisted_ips.forEach(s -> whitelist.add("(IP) " + s));
        whitelisted_uuids.forEach(u -> whitelist.add("(UUID) " + u.toString()));
        
        yaml.set("whitelist", whitelist);

        // Save blacklist
        yaml = new YamlConfiguration();
        List<String> blacklist = new ArrayList<>();

        blacklisted_names.forEach(s -> blacklist.add("(Name) " + s));
        blacklisted_ips.forEach(s -> blacklist.add("(IP) " + s));
        blacklisted_uuids.forEach(u -> blacklist.add("(UUID) " + u.toString()));

        yaml.set("blacklist", blacklist);


        // Save it to the config.
        yaml.save(file);
    }

    // Getters and setters

    public boolean isAlert() {
        return alert;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public int getBlock() {
        return block;
    }

    public List<String> getBlacklisted_ips() {
        return blacklisted_ips;
    }

    public List<String> getBlacklisted_names() {
        return blacklisted_names;
    }

    public List<String> getWhitelisted_ips() {
        return whitelisted_ips;
    }

    public List<String> getVerifiedips() {
        return verifiedips;
    }

    public List<String> getWhitelisted_names() {
        return whitelisted_names;
    }

    public List<UUID> getBlacklisted_uuids() {
        return blacklisted_uuids;
    }

    public List<UUID> getWhitelisted_uuids() {
        return whitelisted_uuids;
    }

    protected String getKey() {
        return key;
    }
}
