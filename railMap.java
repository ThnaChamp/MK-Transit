import java.util.Scanner;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

class jsonReader {
    public void jsonRead(String startStationInput, String terminalStationInput) {
        try {
            FileReader reader = new FileReader("stations.json");
            Gson gson = new Gson();
            MetroData metroData = gson.fromJson(reader, MetroData.class);
            reader.close();

            Station startStation = null;
            Station terminalStation = null;

            // ค้นหาสถานีในข้อมูล JSON
            for (Line line : metroData.lines) {
                for (Station station : line.stations) {
                    if (station.name.equalsIgnoreCase(startStationInput)) {
                        startStation = station;
                    }
                    if (station.name.equalsIgnoreCase(terminalStationInput)) {
                        terminalStation = station;
                    }
                }
            }

            // แสดงผลลัพธ์
            if (startStation == null || terminalStation == null) {
                System.out.println("\n❌ ไม่พบสถานีต้นทางหรือปลายทาง กรุณาตรวจสอบชื่อให้ถูกต้อง");
            } else {
                for (Connection connection : startStation.connections) {
                    System.out.println("To: " + connection.to + ", Distance: " + connection.distance + " km");
                }
                System.out.println("\n✅ Start: " + startStation.name + " (" + startStation.id + ")");
                System.out.println("✅ Terminal: " + terminalStation.name + " (" + terminalStation.id + ")");
            }

        } catch (IOException e) {
            System.err.println("Error reading JSON: " + e.getMessage());
        }
    }
}

// คลาสหลักสำหรับการรับข้อมูลจากผู้ใช้
public class railMap {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("****************");
        System.out.println("Fastest Rail Map");
        System.out.println("****************\n");

        System.out.print("Enter your start station: ");
        String startStationInput = sc.nextLine().trim();

        System.out.print("Enter your terminal station: ");
        String terminalStationInput = sc.nextLine().trim();

        // สร้างอินสแตนซ์ของ jsonReader และเรียกใช้เมธอด jsonRead
        jsonReader reader = new jsonReader();
        reader.jsonRead(startStationInput, terminalStationInput);

        sc.close();
    }
}