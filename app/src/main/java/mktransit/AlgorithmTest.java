package mktransit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        String startId = "YL01";
        String endId = "RW02";

        PathResult result = pathFinder.findShortestPath(startId, endId);

        if (result.getFullPath().isEmpty()) {
            System.out.println("❌ ไม่พบเส้นทางจาก " + startId + " ไปยัง " + endId);
        } else {
            System.out.println("✅ เจอเส้นทาง!");
            System.out.println("เส้นทางเดินทั้งหมด:");

            for (String stationId : result.getFullPath()) {
                Station station = stationMap.get(stationId);
                System.out.println("- " + station.getName() + " (" + station.getId() + ")");
            }

            
            List<String> importantSteps = result.getImportantSteps();
            for (int i = 0; i < importantSteps.size(); i++) {
                String currentId = importantSteps.get(i);
            
                // ป้องกัน IndexOutOfBounds: ต้องเช็ก i > 0 ก่อนใช้ i - 1
                if (i == importantSteps.size() - 1 && i > 0 && currentId.equals(importantSteps.get(i - 1))) {
                    continue; // ข้ามถ้าซ้ำกับสถานีก่อนหน้า
                }
                Station station = stationMap.get(currentId);
                if (i == 0) {
                    System.out.print("\n📍 จุดสำคัญ (Important Steps):\n");
                    System.out.print(stationUtil.IDtoName(startId)+"("+startId+")" + " -> ");
                    System.out.print(stationUtil.IDtoName(currentId)+"("+station.getId()+")");
                } else {
                    System.out.print(" -> " + stationUtil.IDtoName(currentId)+"("+station.getId()+")");
                }
            }
            System.out.println();

            System.out.println("\n🕒 เวลารวมทั้งหมด: " + result.getTotalTime() + " นาที");
        }
    }
}
