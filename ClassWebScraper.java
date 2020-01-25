import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.WriteResult;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import com.google.firebase.cloud.FirestoreClient;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/**
 * @author sk
 */
public class ClassWebScraper {
    static int linksCount = 0;
    private static WebDriver driver = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception, IOException, ExecutionException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sk\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe"); //chrome driver

        ChromeOptions chromeOptions = new ChromeOptions();
        // setting headless mode to true.. so there isn't any ui
        chromeOptions.setHeadless(false);

        driver = new ChromeDriver(chromeOptions); // opens up chrome browser
        driver.get("https://globalsearch.cuny.edu/CFGlobalSearchTool/search.jsp"); //navigates to the link specified
        driver.findElement(By.id("BKL01")).click(); //this clicks on a button with the specific id
        //  driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");

        Select dropdown = new Select(driver.findElement(By.id("t_pd"))); //selects drop down
        dropdown.selectByIndex(2); //selects an option from the dropdown
        driver.findElement(By.className("SSSBUTTON_CONFIRMLINK")).click(); // clicks button with the class name

        dropdown = new Select(driver.findElement(By.id("subject_ld"))); //creates a dropdown menu for selenium to click to
        //dropdown.selectByValue("CMIS"); // option to click on from drop down
        dropdown.selectByIndex(1);

        dropdown = new Select(driver.findElement(By.id("courseCareerId"))); //new drop down from a different page or different menu
        dropdown.selectByValue("UGRD");//select undergrad
        driver.findElement(By.id("open_classId")).click(); //click element
        driver.findElement(By.id("btnGetAjax")).click(); //click element
        driver.findElement(By.id("imageDivLink_inst0")).click(); //click element

        //List<WebElement>  = driver.findElements(By.className("cunylite_PAGROUPDIVIDER")); // links of all classes list in the search
        List<WebElement> allLinks = driver.findElements(By.tagName("a")); //find all links and store it in a list
       HashSet<String> allLinksNoDuplicates = new HashSet<String>(); //create hashset to dump links into and get rid of duplicate links
        for(WebElement link : allLinks){ //loop through list of all links and add them into hashset if they meet certain criteria
            String hrefLink = link.getAttribute("href"); //store the href into a string
            if( hrefLink != null) { //if link isn't null
                if(hrefLink.contains("https://globalsearch.cuny.edu/CFGlobalSearchTool/CFSearchToolController?class_number_searched") == true) //some links are unenncessary like the homepage link so we just check for the links with classes and add them into hashset
                    allLinksNoDuplicates.add(hrefLink); // add class href link into set
            }
        }


        System.out.println("Total no of links Available: " + allLinksNoDuplicates.size()); //check if the class links size matches with the result page links size
      //  links = new String[linksCount];
       // System.out.println("List of links Available: ");
//        for (int i =0 ; i<linksCount; i++){
//            driver.findElement(By.id("imageDivLink"+i)).click();
//        }

        Set<Cookie> cookies = driver.manage().getCookies(); //set cookies so we save the session
        CourseScrapeStore courselink = new CourseScrapeStore();
        for (String links : allLinksNoDuplicates){
            String subject = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/span[2]/strong[1]")).getText();
            courselink.newCunyLinkScrapeWithBrowser(links, cookies, subject);
        }
        driver.close();
        courselink.close();
   // driver.close();
//        CourseScrapeStore courselink = new CourseScrapeStore();
//        for (int j = 0; j < links.size(); j++) {
//            for (int i = 2; i < 3; i++) {
//                WebElement link = driver.findElement(By.xpath("//*[@id=\"contentDivImg" + j + "\"]/table/tbody/tr[" + i + "]/td[2]/a"));
//                String subject = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/span[2]/strong[1]")).getText();
//                String testlink = link.getAttribute("href");
//                courselink.newCunyLinkScrapeWithBrowser(testlink, cookies, subject);
//
//                System.out.println(courselink.toString());
//
//
//            }
//        }


    }
}
