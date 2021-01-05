package com.github.Hafixion.object;

import com.github.Hafixion.managers.ConnectionManager;
import javafx.geometry.Point2D;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

@SuppressWarnings("unused")
public class ipData {
    // Base Details
    private String ip = null;
    private String provider = null;
    private float threat = 0;

    // Geolocation details
    private String country = null;
    private String city = null;
    private Point2D location = null;

    // Security Details
    private boolean detailed = false;
    private IpType type = null;

    // Convert Connection Inputs into ipData
    public ipData(JSONObject json, ConnectionManager.Connection type) {
        try {
            switch (type) {

                // getipintel.net
                case INTEL:
                    // Not much info is available besides the threat level.
                    ip = json.getString("queryIP");
                    threat = Float.parseFloat(json.getString("result")) * 3;

                // proxycheck.io
                case PROXY:
                    // Get the IP address
                    Iterator keys = json.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        if (json.get(key) instanceof JSONObject) {
                            ip = key;
                        }
                        // This basically sets json to the part we want.
                        json = json.getJSONObject(key);
                    }

                    provider = json.getString("provider");

                    // Geolocation
                    country = json.getString("isocode");
                    location = new Point2D(Float.parseFloat(json.getString("latitude")), Float.parseFloat(json.getString("longitude")));
                    if (json.getString("proxy").equalsIgnoreCase("yes")) {
                        this.type = IpType.PROXY;
                        this.threat = 3;
                    } else {
                        threat = (float) (json.getDouble("risk") * 3);

                        String temp = json.getString("type");
                        switch (temp) {
                            case "Residential":
                                this.type = IpType.GENERIC;
                            case "Wireless":
                                this.type = IpType.MOBILE;
                            case "Business":
                                this.type = IpType.NON_RESIDENTIAL;
                            case "Hosting":
                                this.type = IpType.HOSTED;
                            case "TOR":
                                this.type = IpType.PROXY;
                            case "Compromised Server":
                                this.type = IpType.NON_RESIDENTIAL;
                                this.threat = 3;
                            case "Inference Engine":
                                this.type = IpType.PROXY;
                                this.threat = 3;
                            default:
                                this.type = IpType.VPN;
                                this.threat = 2;
                        }
                    }

                // ip-api.com
                case API:
                    this.ip = json.getString("query");

                    // Exception for Providers with VPN in their name, which is a dead giveaway.
                    this.provider = json.getString("org");
                    if (provider.contains("VPN") || provider.contains("vpn")) {
                        threat = 2;
                        this.type = IpType.VPN;
                    }

                    // Geolocation
                    this.country = json.getString("countrycode");
                    this.city = json.getString("city");
                    this.location = new Point2D(json.getDouble("lat"), json.getDouble("lon"));

                    // Security
                    this.detailed = true;

                    if (this.type == null) {
                        if (json.getBoolean("proxy")) this.type = IpType.PROXY;
                        else if (json.getBoolean("hosted")) this.type = IpType.HOSTED;
                        else if (json.getBoolean("mobile")) this.type = IpType.MOBILE;
                        else this.type = IpType.GENERIC;
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Regular JSON data
    public ipData(JSONObject json) throws JSONException {
        ip = json.getString("ip");
        provider = json.getString("provider");
        threat = json.getInt("threat");

        // Geolocation
        JSONObject geo = json.getJSONObject("geolocation");
        country = geo.getString("geolocation");
        city = geo.getString("city");
        location = new Point2D(geo.getDouble("x"), geo.getDouble("y"));

        // Security
        JSONObject security = json.getJSONObject("security");
        detailed = security.getBoolean("detailed");
        type = IpType.valueOf(security.getString("type"));
    }

    public ipData() { }

    // JSON Saving, allows conversion
    public JSONObject toJSON() {
        try {
            JSONObject json = new JSONObject();

            // Base Details
            json.put("ip", ip);
            json.put("provider", provider);
            json.put("threat", threat);

            // Geolocation
            JSONObject geolocation = new JSONObject();
            geolocation.put("country", country);
            geolocation.put("city", city);
            geolocation.put("x", location.getX());
            geolocation.put("y", location.getY());
            json.put("geolocation", geolocation);

            // Security
            JSONObject security = new JSONObject();
            security.put("detailed", detailed);
            security.put("type", type.toString());
            json.put("security", security);

            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Getters and Setters


    public IpType getType() {
        return type;
    }

    public void setType(IpType type) {
        this.type = type;
    }

    public boolean UnsureSecurity() {
        return !detailed;
    }

    public float getThreat() {
        return threat;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public Point2D getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIp() {
        return ip;
    }

    public String getProvider() {
        return provider;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void SecurityDetail(boolean detailed) {
        this.detailed = detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setThreat(float threat) {
        this.threat = threat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ipData ipData = (ipData) o;
        return Float.compare(ipData.threat, threat) == 0 && detailed == ipData.detailed && Objects.equals(ip, ipData.ip) && Objects.equals(provider, ipData.provider) && Objects.equals(country, ipData.country) && Objects.equals(city, ipData.city) && Objects.equals(location, ipData.location) && type == ipData.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, provider, threat, country, city, location, detailed, type);
    }

    public enum IpType {
        GENERIC,
        NON_RESIDENTIAL,
        PROXY,
        HOSTED,
        MOBILE,
        VPN,
    }

}
