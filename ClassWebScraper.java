import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author sk
 */
public class ClassWebScraper {

    private static WebDriver driver = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String option = "";
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sk\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe");
        //  System.setProperty("webdriver.phantomjs", "C:\\Users\\sk\\Downloads\\phantomjs-2.1.1-windows\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        ChromeOptions options = new ChromeOptions();
        // setting headless mode to true.. so there isn't any ui
        options.setHeadless(false);
        driver  = new ChromeDriver(options); // opens up chrome browser
        driver.get("https://globalsearch.cuny.edu/CFGlobalSearchTool/search.jsp"); //navigates to the link specified
        // driver.findElement(By.id("magicpowerpd")).click();//next_btn
        driver.findElement(By.id("BKL01")).click(); //this clicks on a button with the specific id
        // selectByVisibleText("2020 spring");
        Select dropdown = new Select(driver.findElement(By.id("t_pd"))); //selects drop down
        dropdown.selectByIndex(2); //selects an option from the dropdown
        driver.findElement(By.className("SSSBUTTON_CONFIRMLINK")).click(); // clicks button with the class name
        // TODO code application logic here
        //System.out.println("what is your subject?");
        //Scanner scan = new Scanner(System.in);
        //  option = scan.next();
        //  if(option.equalsIgnoreCase("s")){
        // dropdown = new Select(driver.findElement(By.id("subject_ld")));
        // dropdown.selectByValue("CMIS");
        // System.out.println(dropdown);
        //  dropdown = new Select(driver.findElement(By.id("courseCareerId")));
        // dropdown.selectByValue("UGRD");
        // driver.findElement(By.id("open_classId")).click();
        //  driver.findElement(By.id("btnGetAjax")).click();
        //driver.findElement(By.id("imageDivLink_inst0")).click();
        //  driver.findElement(By.id("imageDivLink1")).click();
        //  System.out.println(driver.getTitle());
        try {
            while (true) {
                driver.get("https://globalsearch.cuny.edu/CFGlobalSearchTool/CFSearchToolController?class_number_searched=MzgwNDA=&session_searched=MQ==&term_searched=MTIwMg==&inst_searched=QnJvb2tseW4gQ29sbGVnZQ==");


                String seatList = driver.findElement(By.xpath("//*[@id=\"SSR_CLS_DTL_WRK_AVAILABLE_SEATS\"]")).getText(); //selects element with a certain xpath, to get xpath use developer tools -> click on an element ->  right click the code -> copy -> XPATH
                //  driver.findElement(By.xpath("//a[contains(.,'About')]")).click();
                //  }
                System.out.println(seatList);
                Thread.sleep(2 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



// fetch the document over HTTP JSOUP
//        String urlhome = "https://globalsearch.cuny.edu/CFGlobalSearchTool/search.jsp";
//        String url = "https://globalsearch.cuny.edu/CFGlobalSearchTool/CFSearchToolController?class_number_searched=MzgwNDA=&session_searched=MQ==&term_searched=MTIwMg==&inst_searched=QnJvb2tseW4gQ29sbGVnZQ==";
//        //  Document doc = Jsoup.connect("https://globalsearch.cuny.edu/CFGlobalSearchTool/CFSearchToolController?class_number_searched=MzgwNDA=&session_searched=MQ==&term_searched=MTIwMg==&inst_searched=QnJvb2tseW4gQ29sbGVnZQ==").get();
//        //  System.out.println(doc);
//        Connection connection = Jsoup.connect(urlhome);
//        //System.out.println( con.toString());
//        // Map<String, String> cookie = res.cookies();
//
//        //String cookie  = connection.cookie("JSESSIONID");
//        //System.out.println(cookies);
//        try {
//
////            Response res = connection.execute();
////            cookies.putAll(res.cookies());
////            System.out.println(cookies);
////            Document doc2 = res.parse();
//            Document doc2 = get(url);
//            System.out.println("hello\n" + doc2);
//            // System.out.println("Autenticato");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
//    public static Document get(String url) throws IOException {
//        Connection connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
//        Connection.Response response = connection.execute();
//        connection.cookies(response.cookies());
//        System.out.println(response.cookies());
//        return connection.get();
//    }
