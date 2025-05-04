package mktransit;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class newTrainFareReader {
    private final StationUtil stationUtil;
    private final String filePath;

    // Constructor
    public newTrainFareReader(String filePath, StationUtil stationUtil) {
        this.filePath = filePath;
        this.stationUtil = stationUtil;
    }

    // คำนวณค่าโดยสารทั้งหมด
    public int calculateTotalFare(List<Station> fromStations, List<Station> toStations) throws Exception {
        if (fromStations.isEmpty()) {
            System.err.println("⚠ รายการสถานีไม่สามารถว่างได้");
            return 0;
        }

        // กำหนด sheetCode จาก stationId ของสถานีแรกใน fromStations
        int sheetCode = getSheetCodeForFirstStation(fromStations.get(0).getId());

        // โหลดตารางค่าโดยสารจาก sheetCode
        Map<String, Map<String, Integer>> fareTable = loadFareTable(sheetCode);
        int totalFare = 0;

        int segmentCount = Math.min(fromStations.size(), toStations.size());
        for (int i = 0; i < segmentCount; i++) {
            String from = stationUtil.IDtoName(fromStations.get(i).getId()); 
            String to = stationUtil.IDtoName(toStations.get(i).getId());
            Map<String, Integer> priceMap = fareTable.get(from);
            
            if (priceMap == null || !priceMap.containsKey(to)) {
                System.err.println("⚠ ไม่พบราคาสำหรับเส้นทาง: " + from + " ➡ " + to);
                continue;
            }

            int fare = priceMap.get(to);
            totalFare += fare;

            System.out.println("💰 ช่วงที่ " + (i + 1) + ": " + from + " ➡ " + to + " = " + fare + " บาท");
        }

        return totalFare;
    }

    // ฟังก์ชันนี้จะเลือก sheetCode ตาม stationId
    private int getSheetCodeForFirstStation(String stationId) {
        switch (stationId.charAt(0)) {
            case 'W': case 'N': case 'S': case 'E':
                return 0;
            case 'R':
                return 1;
            case 'P':
                return stationId.charAt(1) == 'P' ? 2 : -1;
            case 'B':
                return stationId.charAt(1) == 'L' ? 3 : -1;
            case 'Y':
                return stationId.charAt(1) == 'L' ? 4 : -1;
            case 'K':
                return stationId.charAt(1) == 'K' ? 5 : -1;
            case 'A':
                return 6;
            case 'G':
                return 7;
            default:
                return -1; // กรณีไม่พบ
        }
    }

    // โหลดตารางค่าโดยสารจากไฟล์ Excel ตาม sheetCode
    public Map<String, Map<String, Integer>> loadFareTable(int sheetCode) throws Exception {
        Map<String, Map<String, Integer>> fareTable = new HashMap<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                throw new FileNotFoundException("ไม่พบไฟล์ " + filePath + " ใน resources/");
            }

            Workbook workbook = new XSSFWorkbook(is);
            if (sheetCode < 0 || sheetCode >= workbook.getNumberOfSheets()) {
                throw new RuntimeException("หมายเลข sheet ไม่ถูกต้อง");
            }

            Sheet sheet = workbook.getSheetAt(sheetCode);
            if (sheet == null) throw new RuntimeException("ไม่พบ sheet");

            // อ่านชื่อสถานีปลายทาง
            Row headerRow = sheet.getRow(7); //แถว 8
            List<String> toStations = new ArrayList<>();
            for (int c = 5; c < headerRow.getLastCellNum(); c++) {
                Cell cell = headerRow.getCell(c);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    toStations.add(cell.getStringCellValue().trim());
                }
            }

            // อ่านราคาค่าโดยสาร
            for (int r = 8; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                Cell fromCell = row.getCell(4);
                if (fromCell == null || fromCell.getCellType() != CellType.STRING) continue;
                String fromStation = fromCell.getStringCellValue().trim();

                Map<String, Integer> priceMap = new HashMap<>();
                for (int c = 5; c < row.getLastCellNum(); c++) {
                    Cell priceCell = row.getCell(c);
                    if (priceCell != null && priceCell.getCellType() == CellType.NUMERIC) {
                        priceMap.put(toStations.get(c - 5), (int) priceCell.getNumericCellValue());
                    }
                }
                fareTable.put(fromStation, priceMap);
            }
        }

        return fareTable;
    }
}
