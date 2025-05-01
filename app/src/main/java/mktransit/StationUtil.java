package mktransit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationUtil {
    private Map<String, String> idToNameMap;

    // Constructor รับ list สถานีมาแล้วสร้าง map
    public StationUtil(List<Station> stations) {
        idToNameMap = new HashMap<>();
        for (Station station : stations) {
            idToNameMap.put(station.getId(), station.getName());
        }
    }

    // ฟังก์ชันแปลง ID -> Name
    public String IDtoName(String id) {
        return idToNameMap.getOrDefault(id, "Unknown Station");
    }
}
