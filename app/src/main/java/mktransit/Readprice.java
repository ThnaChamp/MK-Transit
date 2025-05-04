package mktransit;

import java.util.Map;

public class Readprice {
    public static void main(String[] args) {
        try {
            // โหลดข้อมูลค่าโดยสารจากทุกสาย
            TrainFareReader reader = new TrainFareReader("src/main/resources/TrainPrice.xlsx");
            Map<String, Map<String, Map<String, Integer>>> allFareTables = reader.loadFareTables();

            // ระบุสายและสถานี
            String lineName = "GoldLine"; // ชื่อสาย
            String fromStation = "G1";   // สถานีต้นทาง
            String toStation = "G2";     // สถานีปลายทาง

            // ค้นหาราคาค่าโดยสาร
            Map<String, Map<String, Integer>> fareTable = allFareTables.get(lineName);
            if (fareTable != null) {
                Map<String, Integer> priceMap = fareTable.get(fromStation);
                if (priceMap != null) {
                    Integer fare = priceMap.get(toStation);
                    if (fare != null) {
                        System.out.println("ราคาค่าโดยสารจาก " + fromStation + " ไป " + toStation + " ในสาย " + lineName + " คือ " + fare + " บาท");
                    } else {
                        System.out.println("ไม่พบข้อมูลราคาค่าโดยสารจาก " + fromStation + " ไป " + toStation + " ในสาย " + lineName);
                    }
                } else {
                    System.out.println("ไม่พบข้อมูลสถานีต้นทาง: " + fromStation + " ในสาย " + lineName);
                }
            } else {
                System.out.println("ไม่พบข้อมูลสาย: " + lineName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}