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

public class TrainFareReader {
    private final String filePath;

    public TrainFareReader(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, Map<String, Integer>> loadFareTable() throws Exception {
        Map<String, Map<String, Integer>> fareTable = new HashMap<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("TrainPrice.xlsx")) {
            if (is == null) {
                throw new FileNotFoundException("ไม่พบไฟล์ TrainPrice.xlsx ใน resources/");
            }

            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("GreenLine");
            if (sheet == null) throw new RuntimeException("ไม่พบ sheet");

            // อ่านชื่อสถานีปลายทาง (แนวนอน)
            Row headerRow = sheet.getRow(7); //แถว 8
            List<String> toStations = new ArrayList<>();
            for (int c = 5; c < headerRow.getLastCellNum(); c++) { //เริ่ม column F
                Cell cell = headerRow.getCell(c);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    toStations.add(cell.getStringCellValue().trim());
                } else {
                    toStations.add(""); // หรือข้ามเลยก็ได้
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
                    if (priceCell == null) continue;
                    if (priceCell.getCellType() == CellType.NUMERIC) {
                        priceMap.put(toStations.get(c - 5), (int) priceCell.getNumericCellValue());
                    }
                }
                fareTable.put(fromStation, priceMap);
            }
        }

        return fareTable;
    }

    public Map<String, Map<String, Map<String, Integer>>> loadFareTables() throws Exception {
        Map<String, Map<String, Map<String, Integer>>> allFareTables = new HashMap<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("TrainPrice.xlsx")) {
            if (is == null) {
                throw new FileNotFoundException("ไม่พบไฟล์ TrainPrice.xlsx ใน resources/");
            }

            Workbook workbook = new XSSFWorkbook(is);

            // วนลูปอ่านทุกหน้าในไฟล์ Excel
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                if (sheet == null) continue;

                String lineName = sheet.getSheetName(); // ชื่อสาย (ชื่อหน้า)
                System.out.println("กำลังอ่านข้อมูลจากสาย: " + lineName);

                // อ่านชื่อสถานีปลายทาง (แนวนอน)
                Row headerRow = sheet.getRow(7); // สมมติว่าแถว 8 เป็น header
                if (headerRow == null) continue;

                List<String> toStations = new ArrayList<>();
                for (int c = 5; c < headerRow.getLastCellNum(); c++) { // เริ่มจากคอลัมน์ F
                    Cell cell = headerRow.getCell(c);
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        toStations.add(cell.getStringCellValue().trim());
                    } else {
                        toStations.add(""); // หรือข้ามเลยก็ได้
                    }
                }

                // อ่านราคาค่าโดยสาร
                Map<String, Map<String, Integer>> fareTable = new HashMap<>();
                for (int r = 8; r <= sheet.getLastRowNum(); r++) { // เริ่มจากแถว 9
                    Row row = sheet.getRow(r);
                    if (row == null) continue;

                    Cell fromCell = row.getCell(4); // คอลัมน์ E
                    if (fromCell == null || fromCell.getCellType() != CellType.STRING) continue;
                    String fromStation = fromCell.getStringCellValue().trim();

                    Map<String, Integer> priceMap = new HashMap<>();
                    for (int c = 5; c < row.getLastCellNum(); c++) { // เริ่มจากคอลัมน์ F
                        Cell priceCell = row.getCell(c);
                        if (priceCell == null) continue;
                        if (priceCell.getCellType() == CellType.NUMERIC) {
                            priceMap.put(toStations.get(c - 5), (int) priceCell.getNumericCellValue());
                        }
                    }
                    fareTable.put(fromStation, priceMap);
                }

                // เก็บข้อมูลค่าโดยสารของสายนี้
                allFareTables.put(lineName, fareTable);
            }
        }

        return allFareTables;
    }
}