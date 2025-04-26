package ex;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
public class Json {
    public static void main(String[] args) {
        try (InputStream is = Json.class.getClassLoader().getResourceAsStream("data.json")) {
            if (is == null) {
                System.out.println("File not found in resources.");
                return;
            }

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);

            String name = json.getString("name");
            int version = json.getInt("version");

            System.out.println("Name: " + name);
            System.out.println("Version: " + version);
            System.out.println("All JSON:");
            // System.out.println(json.toString(2));

        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }
}