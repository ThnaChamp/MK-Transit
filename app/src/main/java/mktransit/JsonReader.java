package mktransit;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReader {

    public List<Line> loadJsonData() {
        List<Line> lines = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("stations.json")) {
            if (is == null) {
                System.out.println("File not found!");
                return lines;
            }

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(content);                  // 👈 root object
            JSONArray linesArray = root.getJSONArray("lines");         // 👈 "lines" array

            for (int i = 0; i < linesArray.length(); i++) {
                JSONObject lineObj = linesArray.getJSONObject(i);
                String lineName = lineObj.getString("name");
                String lineColor = lineObj.getString("color");

                JSONArray stationsArray = lineObj.getJSONArray("stations");
                List<Station> stations = new ArrayList<>();

                for (int j = 0; j < stationsArray.length(); j++) {
                    JSONObject stationObj = stationsArray.getJSONObject(j);
                    String id = stationObj.getString("id");
                    String name = stationObj.getString("name");
                    boolean interchange = stationObj.optBoolean("interchange", false);

                    JSONArray connectionsArray = stationObj.getJSONArray("connections");
                    List<Connection> connections = new ArrayList<>();

                    for (int k = 0; k < connectionsArray.length(); k++) {
                        JSONObject connObj = connectionsArray.getJSONObject(k);
                        String to = connObj.getString("to");
                        int time = connObj.getInt("time");
                        String type = connObj.optString("type", "normal");
                        String connLine = connObj.optString("line", lineName);

                        connections.add(new Connection(to, time, type, connLine));
                    }

                    stations.add(new Station(id, name, interchange, connections));
                }

                lines.add(new Line(lineName, lineColor, stations));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return lines;
    }
}
