import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtility {


    public static List<String> getHeaderColumns(XSSFSheet sheet) {
        List<String> headerColumn = new ArrayList<>();
        Row headerRow = sheet.getRow(0);
        Iterator<Cell> cellIterator = headerRow.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            headerColumn.add(cell.getStringCellValue());
        }

        return headerColumn;
    }

    public static int getColumnHeaderIndex(List headerColumn, String headerName) {
        int columnIndex = -1;
        for (int index = 0; index < headerColumn.size(); index++) {
            if(headerColumn.get(index).toString().equalsIgnoreCase(headerName)){
                columnIndex = index;
                break;
            }
        }

        if(columnIndex == -1){
            throw new RuntimeException("No Header Found with the name:- " + headerName);
        }
        return columnIndex;
    }

    public static List<String> getAllColumnValue(XSSFSheet sheet, String columnHeader) {
        List<String> columnDatas = new ArrayList<>();
        Iterator<Row> rows = sheet.rowIterator();
        int columnHeaderIndex = getColumnHeaderIndex(getHeaderColumns(sheet), columnHeader);

        while (rows.hasNext()) {
            Row row = rows.next();
            Cell desiredCell = row.getCell(columnHeaderIndex);

            if(desiredCell == null){
                continue;
            }

            switch (desiredCell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    columnDatas.add(String.valueOf(desiredCell.getNumericCellValue()));
                    break;
                case Cell.CELL_TYPE_STRING:
                    columnDatas.add(desiredCell.getStringCellValue());
                    break;
            }
        }
        // Remove Header column data
        columnDatas.remove(columnHeader);

        return columnDatas;
    }

    public static XSSFSheet getSheet(String filePath, Object index) {
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;

        FileInputStream file = getFile(filePath);

        try {
            //Create Workbook instance holding reference to .xlsx file
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get first/desired sheet from the workbook
        if (index instanceof Integer) {
            sheet = workbook.getSheetAt((int) index);
        } else if (index instanceof String) {
            sheet = workbook.getSheet((String.valueOf(index)));
        }

        return sheet;
    }

    private static FileInputStream getFile(String filePath) {
        FileInputStream file = null;

        try {
            file = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file;
    }
}

