package mktransit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mktransit.*;

public class AlgorithmTest {
    public static void main(String[] args) {
        JsonReader reader = new JsonReader();
        reader.loadJsonData();

        Map<String, Station> stationMap = reader.getStationMap();

        PathFinder pathFinder = new PathFinder(stationMap);

        // โหลดสถานีมาจาก JsonReader
        List<Station> stationList = new ArrayList<>(reader.getStationMap().values());

        // สร้าง StationUtil
        StationUtil stationUtil = new StationUtil(stationList);

        // ลองใช้งาน IDtoName
        // String stationName = stationUtil.IDtoName("BTS_Siam");

        // System.out.println(stationName); // จะได้ "สยาม" (ถ้าข้อมูลมีใน JSON)


        // ลองหาเส้นทางที่ "ไม่ผ่าน interchange"

        String startId = "CEN";
        String endId = "YL01";

        PathResult result = pathFinder.findShortestPath(startId, endId);
        int i=0;

        if (result.getFullPath().isEmpty()) {
            System.out.println("❌ ไม่พบเส้นทางจาก " + stationUtil.IDtoName(startId)+"("+startId+")" + " ไปยัง " + stationUtil.IDtoName(endId)+"("+endId+")");
        } else {
            System.out.println("✅ เจอเส้นทาง!");
            System.out.println("เส้นทางเดินทั้งหมด:");

            for (String stationId : result.getFullPath()) {
                Station station = stationMap.get(stationId);
                System.out.println("- " + station.getName() + " (" + station.getId() + ")");
                i++;
            }

            List<String> fullPath = result.getFullPath();
            List<String> importantSteps = PathUtil.filterImportantStepsWithActualTransfers(fullPath, stationMap);

            if (importantSteps.isEmpty()) {
                System.out.print("📍 ไม่มีจุดที่ต้องเปลี่ยนสายตลอดเส้นทาง | จำนวน " + i + " สถานี ");
                System.out.println(stationUtil.IDtoName(startId) + " (" + startId + ") ➜ " + stationUtil.IDtoName(endId) + " (" + endId + ")");
            } else {
                System.out.println("📍 เส้นทางนี้มีการเปลี่ยนสาย | จำนวน " + i + " สถานี");

                boolean firstStep = true;
                for (int j = 0; j < importantSteps.size(); j++) {
                    String step = importantSteps.get(j);
                    String[] parts = step.split("->");
                    String fromId = parts[0];
                    String toId = parts[1];

                    String fromName = stationUtil.IDtoName(fromId);
                    String toName = stationUtil.IDtoName(toId);

                    if (firstStep) {
                        // เริ่มต้นจากสถานีต้นทางไปยังจุดเปลี่ยนสายแรก
                        System.out.print("🔄 " + fromName + " (" + fromId + ") ➜ " + toName + " (" + toId + ")");
                        firstStep = false;
                    } else {
                        // แสดงเฉพาะจุดเปลี่ยนสายถัดไป
                        System.out.print(" ➜ " + fromName + " (" + fromId + ") ➜ " + toName + " (" + toId + ")");
                    }
                }

                // จบด้วยปลายทางถ้ายังไม่ได้แสดง
                String lastToId = importantSteps.get(importantSteps.size() - 1).split("->")[1];
                if (!lastToId.equals(endId)) {
                    System.out.print(" ➜ " + stationUtil.IDtoName(endId) + " (" + endId + ")");
                }

                System.out.println(); // ขึ้นบรรทัดใหม่
            }


            


            System.out.println("\n🕒 เวลารวมทั้งหมด: " + result.getTotalTime() + " นาที");
        }
    }
}
