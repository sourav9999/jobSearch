import org.testng.annotations.Test;

public class TestFunctionality {
    // Test Data
    String input = "{\n" +
            "\"studentId\":\"1234\",\n" +
            "\"filter\":{\n" +
            "\"experience\":\"3-5 year,7-9 year\",\n" +
            "\"type\":\"Permanent,Contract\",\n" +
            "\"skills\":\"Java,Sql,Python\",\n" +
            "\"company\":\"Aa\",\n" +
            "\"location\":\"any\"\n" +
            "}\n" +
            "}";

    private JobSearchUtility jobSearchUtility;

    @Test
    void search() {
        jobSearchUtility = new JobSearchUtility(input);

        System.out.println(jobSearchUtility.searchedResult());
    }
}
