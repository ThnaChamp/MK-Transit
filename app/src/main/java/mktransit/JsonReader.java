package mktransit;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReader {
    private List<Line> lines = new ArrayList<>();
    private Map<String, Station> stationMap = new HashMap<>();

    public void loadJsonData() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("stations.json")) {
            if (is == null) {
                System.out.println("File not found!");
                return;
            }

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(content);
            JSONArray linesArray = root.getJSONArray("lines");

            for (int i = 0; i < linesArray.length(); i++) {
                JSONObject lineObj = linesArray.getJSONObject(i);
                String lineName = lineObj.getString("name");
                String lineColor = lineObj.getString("color");

                JSONArray stationsArray = lineObj.getJSONArray("stations");
                List<Station> stations = new ArrayList<>();

                for (int j = 0; j < stationsArray.length(); j++) {
                    JSONObject stationObj = stationsArray.getJSONObject(j);
                    String id = stationObj.getString("id").toUpperCase(); // normalize id
                    String name = stationObj.getString("name");
                    boolean interchange = stationObj.optBoolean("interchange", false);

                    JSONArray connectionsArray = stationObj.getJSONArray("connections");
                    List<Connection> connections = new ArrayList<>();

                    for (int k = 0; k < connectionsArray.length(); k++) {
                        JSONObject connObj = connectionsArray.getJSONObject(k);
                        String to = connObj.getString("to").toUpperCase();  // normalize id
                        int time = connObj.getInt("time");
                        String type = connObj.optString("type", "normal");
                        String connLine = connObj.optString("line", lineName);

                        connections.add(new Connection(to, time, type, connLine));
                    }

                    Station station = new Station(id, name, interchange, connections, lineColor);
                    stations.add(station);
                    stationMap.put(id, station);
                }

                lines.add(new Line(lineName, lineColor, stations));
            }

            // หลังจากโหลดเสร็จแล้ว เช็ค connection ว่าปลายทางมีจริงไหม
            validateConnections();

        } catch (Exception e) {
            System.out.println("Error reading JSON: " + e.getMessage());
        }
    }

    private void validateConnections() {
        for (Station station : stationMap.values()) {
            for (Connection conn : station.getConnections()) {
                if (!stationMap.containsKey(conn.getTo())) {
                    System.out.println("⚠️ Warning: Connection from " + station.getId() + " to " + conn.getTo() + " not found.");
                }
            }
        }
    }

    public List<Line> getLines() {
        return lines;
    }

    public Map<String, Station> getStationMap() {
        return stationMap;
    }
}
