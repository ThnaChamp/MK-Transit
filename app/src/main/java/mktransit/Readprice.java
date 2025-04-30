package mktransit;

import java.util.Map;

public class Readprice {
    public static void main(String[] args) {
        try {
            TrainFareReader reader = new TrainFareReader("src/main/resources/TrainPrice.xlsx");
            Map<String, Map<String, Integer>> fareTable = reader.loadFareTable();

            TrainFareService service = new TrainFareService(fareTable);

            String from = "N24";
            String to = "N2";
            Integer fare = service.findFare(from, to);

            if (fare != null) {
                System.out.println("ราคาค่าโดยสารจาก " + from + " ไป " + to + " คือ " + fare + " บาท");
            } else {
                System.out.println("ไม่พบข้อมูลราคาค่าโดยสาร");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}