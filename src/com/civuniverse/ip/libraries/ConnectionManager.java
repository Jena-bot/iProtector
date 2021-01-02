package com.civuniverse.ip.libraries;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ConnectionManager {

    public static JSONObject checkIPHUB(String ip) throws Exception {
        URL iphub = new URL("http://v2.api.iphub.info/ip/" + ip);

        // todo add usage of multiple keys.
        HttpURLConnection c = (HttpURLConnection) iphub.openConnection();
        c.setRequestProperty("x-key", "MTE1MzI6OGV2VWlzZkJySW5obWVWWUcxRkpQVXJuNlVMZW93Yjg=");

        if (c.getResponseCode() == 429) {
            return checkIPHUNTER(ip);
        }

        return (JSONObject) new JSONParser().parse(new InputStreamReader(c.getInputStream()));
    }

    public static JSONObject checkIPHUNTER(String ip) throws Exception {
        URL iphub = new URL("https://www.iphunter.info:8082/v1/ip/" + ip);

        // todo add usage of multiple keys.
        HttpsURLConnection c = (HttpsURLConnection) iphub.openConnection();
        c.setRequestProperty("x-key", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.WzEzOTMsMTYwOTYyMzA0MywyMDAwXQ.UGkXAaDxEPW7GtN15CwizCeZ10TaifdfZXS8BeG59gI");

        if (c.getResponseCode() == 429) {
            return null;
        }

        return (JSONObject) new JSONParser().parse(new InputStreamReader(c.getInputStream()));
    }


}
