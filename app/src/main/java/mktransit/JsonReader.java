// JsonReader.java
package mktransit;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReader {
    public void loadJsonData() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("stations.json")) {
            if (is == null) {
                System.out.println("File not found in resources.");
                return;
            }
            System.out.println("File found. Reading content...");

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // เปลี่ยนจาก JSONObject เป็น JSONArray
            JSONArray linesArray = new JSONArray(content);

            for (int i = 0; i < linesArray.length(); i++) {
                JSONObject lineObj = linesArray.getJSONObject(i);
                String lineName = lineObj.optString("name", "Unknown Line");
                String color = lineObj.optString("color", "Unknown Color");
                System.out.println("Line: " + lineName + " (Color: " + color + ")");

                JSONArray stationsArray = lineObj.getJSONArray("stations");
                for (int j = 0; j < stationsArray.length(); j++) {
                    JSONObject stationObj = stationsArray.getJSONObject(j);
                    String stationId = stationObj.optString("id", "Unknown ID");
                    String stationName = stationObj.optString("name", "Unknown Station");
                    boolean isInterchange = stationObj.optBoolean("interchange", false);

                    System.out.println("  Station: " + stationName + " (ID: " + stationId + ", Interchange: " + isInterchange + ")");

                    JSONArray connectionsArray = stationObj.getJSONArray("connections");
                    for (int k = 0; k < connectionsArray.length(); k++) {
                        JSONObject conn = connectionsArray.getJSONObject(k);
                        String to = conn.optString("to", "Unknown Destination");
                        int time = conn.optInt("time", -1);
                        String type = conn.optString("type", "normal");
                        String connLine = conn.optString("line", lineName); // ถ้าไม่มี line ให้ใช้ชื่อ line เดิม

                        System.out.println("    → To: " + to + ", Time: " + time + " min, Type: " + type + ", Line: " + connLine);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }
}
