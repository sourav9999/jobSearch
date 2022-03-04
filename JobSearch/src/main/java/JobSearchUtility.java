import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;
import java.util.*;

public class JobSearchUtility {

    static Map inputObject;
    static ObjectMapper mapper;

    JobSearchUtility(String jsonInput) {
        mapper = new ObjectMapper();
        inputObject = convertToMap(jsonInput);
    }

    public String getStudentId() {
        return String.valueOf(inputObject.get("studentId"));
    }

    public String searchedResult() {
        List<String> headerColumns;
        List<String> jobIds;
        Map<String, String> filteredJobs;
        List<String> searchedMappedResponse = new ArrayList<>();

        LinkedHashMap<String, String> filterParams = (LinkedHashMap) inputObject.get("filter");
        XSSFSheet sheet = ExcelUtility.getSheet("src/main/resources/DataTables/JobsData.xlsx", 0);

        // Fetch headers column
        headerColumns = ExcelUtility.getHeaderColumns(sheet);

        // Store All Job Id's
        jobIds = ExcelUtility.getAllColumnValue(sheet, "JOBID");

        // Check if Multi-param passed for a search filter
        for (Map.Entry filters : filterParams.entrySet()) {
            String filter = filters.getKey().toString();
            List<String> multiParams = Arrays.asList(filters.getValue().toString().split(","));
            int headerColumnIndex = ExcelUtility.getColumnHeaderIndex(headerColumns, filter);

            // Eliminate job from list which does not have the filter param
            for (int i = 1; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
                Row row = sheet.getRow(i);

                Cell desiredCell = row.getCell(headerColumnIndex);

                if (!multiParams.contains(desiredCell.getStringCellValue()) && !multiParams.contains("any")) {
                    jobIds.remove(row.getCell(0).getStringCellValue());
                }
            }
        }

        if (jobIds.isEmpty()) {
            throw new RuntimeException("No Search result found");
        }

        filteredJobs = new LinkedHashMap<>();

        // Store Filtered Jobs in a map
        for (String jobId : jobIds) {
            for (int i = 1; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
                Row row = sheet.getRow(i);
                Cell desiredCell = row.getCell(0);

                if (desiredCell.getStringCellValue().equalsIgnoreCase(jobId)) {
                    for (int j = 0; j < row.getPhysicalNumberOfCells() - 1; j++) {
                        switch (row.getCell(j).getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                filteredJobs.put(headerColumns.get(j), String.valueOf(row.getCell(j).getNumericCellValue()));
                                break;
                            case Cell.CELL_TYPE_STRING:
                                filteredJobs.put(headerColumns.get(j), row.getCell(j).getStringCellValue());
                                break;
                        }
                    }
                }
            }

            try {
                searchedMappedResponse.add(mapper.writeValueAsString(filteredJobs));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        // Store the filtered result to a map and obtain desired output schema
        HashMap<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("JobList", searchedMappedResponse);

        return String.valueOf(responseSchema);
    }

    /**
     * Convert input Json format to Map Object
     *
     * @param input
     * @return
     */
    private Map convertToMap(String input) {
        Map<String, String> map = new HashMap<>();

        try {
            // convert JSON string to Map
            map = mapper.readValue(input, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
