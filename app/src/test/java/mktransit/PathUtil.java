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

    public List<String> findImportantSteps(List<String> fullPath, Map<String, Station> stationMap) {
        List<String> importantSteps = new ArrayList<>();
    
        for (int i = 1; i < fullPath.size(); i++) {
            String prevId = fullPath.get(i - 1);
            String currId = fullPath.get(i);
    
            Station prevStation = stationMap.get(prevId);
            Station currStation = stationMap.get(currId);
    
            if (prevStation == null || currStation == null) continue;
    
            // หา connection จาก prev ไป curr
            Connection conn = prevStation.getConnections().stream()
                .filter(c -> c.getTo().equalsIgnoreCase(currId))
                .findFirst()
                .orElse(null);
    
            if (conn == null) continue;
    
            String prevLine = conn.getLine();
    
            // หา connection จาก curr ไป prev เพื่อดูว่ากลับทางสายอะไร
            Connection backConn = currStation.getConnections().stream()
                .filter(c -> c.getTo().equalsIgnoreCase(prevId))
                .findFirst()
                .orElse(null);
    
            String currLine = backConn != null ? backConn.getLine() : prevLine;
    
            // ถ้า line เปลี่ยน ให้เพิ่ม prev กับ curr
            if (!prevLine.equals(currLine)) {
                if (!importantSteps.contains(prevId)) importantSteps.add(prevId);
                importantSteps.add(currId);
            }
        }
    
        return importantSteps;
    }
    

}

