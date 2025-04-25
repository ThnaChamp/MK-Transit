// JsonReader.java
package mktransit;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class JsonReader {
    private String name = "N/A";
    private int version = -1;

    public void loadJsonData() {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream("stations.json")) {
        if (is == null) {
            System.out.println("File not found in resources.");
            return;
        }
        System.out.println("File found. Reading content..."); // เพิ่มข้อความนี้
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        // System.out.println("File content: " + content); // เพิ่มข้อความนี้เพื่อดูข้อมูลที่อ่านได้

        JSONObject json = new JSONObject(content);
        name = json.optString("name", "Unknown");
        version = json.optInt("version", -1);

    } catch (Exception e) {
        System.out.println("Error reading JSON file: " + e.getMessage());
    }
}

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }
}
