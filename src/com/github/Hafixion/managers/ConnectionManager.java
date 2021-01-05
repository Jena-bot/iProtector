package com.github.Hafixion.managers;

import com.github.Hafixion.iProtectorMain;
import com.github.Hafixion.object.ipData;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class ConnectionManager {
    private int level;

    public static void main(String[] args) {
        String ip = "24.48.0.1";
        System.out.println(new ConnectionManager(1).getIpDetails(ip, Connection.INTEL));
    }

    public ConnectionManager(int level) {
        this.level = level;
    }

    public ipData getIpStatus(String ip, int protect) {
        ipData data = new ipData();
        try {
            switch (protect) {
                case 1:
                    JSONObject api = getIpDetails(ip, Connection.API);
                    if (!api.getString("status").equals("success")) {
                        return new ipData();
                    }
                    data = new ipData(api, Connection.API);
                    break;

                case 2:
                    // Get both types, and return the highest threat
                    ipData temp = getIpStatus(ip, 1);
                    JSONObject temporary = getIpDetails(ip, Connection.PROXY);

                    if (!temporary.getString("status").equals("ok"))
                        return temp;
                    data = new ipData(temporary, Connection.PROXY);

                    if (temp.getThreat() > data.getThreat())
                        data.setThreat(temp.getThreat());
                    data.setCity(temp.getCity());
                    break;

                case 3:
                    // Get the old ipData, and check if there's a higher threat level.
                    data = getIpStatus(ip, 2);
                    JSONObject intel = getIpDetails(ip, Connection.INTEL);

                    if (!intel.getString("status").equals("success"))
                        return data;
                    temp = new ipData(intel, Connection.INTEL);

                    if (temp.getThreat() > data.getThreat())
                        data.setThreat(temp.getThreat());
                    break;
            }
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ipData();
        }
    }

    public JSONObject getIpDetails(String ip, Connection connect) {
        try {
            switch (connect) {
                case API:
                    return new JSONObject(IOUtils.toString(new InputStreamReader(new URL("http://ip-api.com/json/" + ip).openStream())));
                case INTEL:
                    return new JSONObject(IOUtils.toString(new InputStreamReader(new URL("http://check.getipintel.net/check.php?ip=" + ip + "&contact=" + iProtectorMain.getInstance().config.getContact() + "&format=json").openStream())));
                case PROXY:
                    URL url = new URL("http://proxycheck.io/v2/" + ip + "&vpn=1&asn=1&risk=1");
                    JSONObject json = new JSONObject(IOUtils.toString(new InputStreamReader(url.openStream())));

                    // If the server went over the 100 per day limit
                    if (json.getString("status").equalsIgnoreCase("denied")) {
                        URL key = new URL("http://proxycheck.io/v2/" + ip +  "?key=o59668-q9460b-3845v2-i06139&vpn=1&asn=1&risk=1");
                        if (iProtectorMain.getInstance().config.getApi_key() != null)
                            key = new URL("http://proxycheck.io/v2/" + ip +  "?key=" + iProtectorMain.getInstance().config.getApi_key() + "&vpn=1&asn=1&risk=1");

                        return new JSONObject(IOUtils.toString(new InputStreamReader(key.openStream())));
                    }

                    return json;
                default:
                    return new JSONObject();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public enum Connection {
        API,
        INTEL,
        PROXY,
    }
}
