package com.civuniverse.ip.libraries;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ConnectionManager {

    public static JSONObject checkIPHUB(String ip) throws Exception {
        URL iphub = new URL("http://v2.api.iphub.info/ip/");

        // todo add usage of multiple keys.
        HttpURLConnection c = (HttpURLConnection) iphub.openConnection();
        c.setRequestProperty("x-key", "MTE1MzI6OGV2VWlzZkJySW5obWVWWUcxRkpQVXJuNlVMZW93Yjg=");

        return (JSONObject) new JSONParser().parse(new InputStreamReader(c.getInputStream()));
    }
}
