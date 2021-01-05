package com.github.Hafixion;

import com.github.Hafixion.object.ipData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class iProtectorConfig {
    // Whether or not encryption is enabled. Using (REDACTED) encryption, key is the encryption key used.
    public boolean encrypt;
    private final String key;

    // Whether or not to prevent players from joining
    public boolean prevent;

    // Whether or not to alert people with permission
    public boolean alert;

    // Protection Level
    public int protect;

    public String api_key;

    public String contact;

    // Local IP storage, normally encrypted, used to keep data locally in order to avoid querying too much.
    public List<ipData> localips = new ArrayList<>();

    // Black/Whitelist
    public List<UUID> whitelisted_uuids = new ArrayList<>();
    public List<UUID> blacklisted_uuids = new ArrayList<>();

    public List<String> whitelisted_ips = new ArrayList<>();
    public List<String> blacklisted_ips = new ArrayList<>();

    public List<String> whitelisted_names = new ArrayList<>();
    public List<String> blacklisted_names = new ArrayList<>();

    public iProtectorConfig(FileConfiguration config, FileConfiguration data) throws InvalidConfigurationException {
        boolean[] error = {false, false, false, false};

        // import encryption config
        encrypt = config.getBoolean("encryption");
        if (encrypt) key = config.getString("key");
        else key = "disabled";

        // Import locally stored IP data
        if (encrypt) {
            data.getValues(true).keySet().forEach(s -> {
                try {
                    localips.add(new ipData((JSONObject) new JSONParser().parse(iProtectorMain.getInstance().encryptor.decrypt(data.getString(s)))));
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            });
        } else {
            data.getValues(true).keySet().forEach(s -> {
                try {
                    localips.add(new ipData((JSONObject) new JSONParser().parse(data.getString(s))));
                } catch (ParseException | JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        // import prevent player join
        prevent = config.getBoolean("prevent-player-join");

        // Import api key and contact info
        contact = config.getString("contact-info");
        api_key = config.getString("api-key");

        // import block level and alerts
        protect = config.getInt("protection-level");
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
    public void save(File file, File verify) throws IOException {
        YamlConfiguration yaml = new YamlConfiguration();
        YamlConfiguration local = new YamlConfiguration();

        // Saving Local IPs
        if (encrypt) localips.forEach(ip -> local.set(iProtectorMain.getInstance().encryptor.encrypt(ip.getIp()),
                iProtectorMain.getInstance().encryptor.encrypt(ip.toJSON().toString())));
        else localips.forEach(ip -> local.set(ip.getIp(), ip.toJSON().toString()));

        // Save Whitelist
        List<String> whitelist = new ArrayList<>();

        whitelisted_names.forEach(s -> whitelist.add("(Name) " + s));
        whitelisted_ips.forEach(s -> whitelist.add("(IP) " + s));
        whitelisted_uuids.forEach(u -> whitelist.add("(UUID) " + u.toString()));

        yaml.set("whitelist", whitelist);

        // Save blacklist
        List<String> blacklist = new ArrayList<>();

        blacklisted_names.forEach(s -> blacklist.add("(Name) " + s));
        blacklisted_ips.forEach(s -> blacklist.add("(IP) " + s));
        blacklisted_uuids.forEach(u -> blacklist.add("(UUID) " + u.toString()));

        yaml.set("blacklist", blacklist);


        // Save it to the config.
        yaml.save(file);
        local.save(verify);
    }

    // Getters and setters

    public boolean isAlert() {
        return alert;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public int getProtect() {
        return protect;
    }

    public boolean isPrevent() {
        return prevent;
    }

    public String getApi_key() {
        return api_key;
    }

    public String getContact() {
        return contact;
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

    public void setBlacklisted_ips(List<String> blacklisted_ips) {
        this.blacklisted_ips = blacklisted_ips;
    }

    public void setBlacklisted_names(List<String> blacklisted_names) {
        this.blacklisted_names = blacklisted_names;
    }

    public void setBlacklisted_uuids(List<UUID> blacklisted_uuids) {
        this.blacklisted_uuids = blacklisted_uuids;
    }

    public void setWhitelisted_ips(List<String> whitelisted_ips) {
        this.whitelisted_ips = whitelisted_ips;
    }

    public void setWhitelisted_names(List<String> whitelisted_names) {
        this.whitelisted_names = whitelisted_names;
    }

    public void setWhitelisted_uuids(List<UUID> whitelisted_uuids) {
        this.whitelisted_uuids = whitelisted_uuids;
    }

    public List<ipData> getLocalips() {
        return localips;
    }

    public void setLocalips(List<ipData> localips) {
        this.localips = localips;
    }

    public void addIp(ipData ip) {
        this.localips.add(ip);
    }

    public void removeIp(ipData ip) {
        this.localips.forEach(ipData -> {
            if (ipData.equals(ip)) this.localips.remove(ip);
        });
    }
}
