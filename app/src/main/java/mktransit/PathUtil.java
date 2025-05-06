// mktransit/PathUtil.java
package mktransit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PathUtil {
    public static List<String> filterImportantStepsWithActualTransfers(
        List<String> fullPath,
        Map<String, Station> stationMap) {

        List<String> filteredSteps = new ArrayList<>();

        String previousLine = null;
        for (int i = 0; i < fullPath.size() - 1; i++) {
            String currentId = fullPath.get(i);
            String nextId = fullPath.get(i + 1);

            Station currentStation = stationMap.get(currentId);
            if (currentStation == null) continue;

            String currentLine = getLineBetweenStations(currentStation, nextId);

            // ตรวจสอบการเปลี่ยนสาย
            if (previousLine != null && currentLine != null && !previousLine.equals(currentLine)) {
                // เพิ่มการเปลี่ยนสายในรูปแบบ "จาก -> ไป"
                filteredSteps.add(currentId + "->" + nextId);
            }

            previousLine = currentLine;
        }

        return filteredSteps;
    }


    private static String getLineBetweenStations(Station fromStation, String toId) {
        for (Connection conn : fromStation.getConnections()) {
            if (conn.getTo().equalsIgnoreCase(toId)) {
                return conn.getLine();
            }
        }
        return null;
    }

}

