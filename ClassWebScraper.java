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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/**
 * @author sk
 */
public class ClassWebScraper {
    static String[] links = null;
    static int linksCount = 0;
    private static WebDriver driver = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sk\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe");

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
        // TODO code application logic here
         dropdown = new Select(driver.findElement(By.id("subject_ld")));
         //dropdown.selectByValue("CMIS");
        dropdown.selectByIndex(1);
         System.out.println(dropdown);
          dropdown = new Select(driver.findElement(By.id("courseCareerId")));
         dropdown.selectByValue("UGRD");
         driver.findElement(By.id("open_classId")).click();
          driver.findElement(By.id("btnGetAjax")).click();
        driver.findElement(By.id("imageDivLink_inst0")).click();

        List<WebElement> linksize = driver.findElements(By.className("cunylite_PAGROUPDIVIDER"));

        linksCount = linksize.size();
        System.out.println("Total no of links Available: "+linksCount);
        links= new String[linksCount];
        System.out.println("List of links Available: ");
        for (int i =0 ; i<5; i++){
            driver.findElement(By.id("imageDivLink"+i)).click();
        }
        List<WebElement> links = driver.findElements(By.tagName("tr"));
        System.out.println(links.size());//
        Set<Cookie> cookies = driver.manage().getCookies();

        CourseScrapeStore courselink = new CourseScrapeStore();
        for (int i = 2; i<links.size()-10 ; i++){//*[@id="contentDivImg0"]/table/tbody/tr[2]/td[2]/a

         WebElement link =   driver.findElement(By.xpath("//*[@id=\"contentDivImg0\"]/table/tbody/tr["+i+"]/td[2]/a"));
         String testlink  = link.getAttribute("href");
            courselink.newCunyLinkScrapeWithBrowser(testlink, cookies);
            Thread.sleep(10000);
            System.out.println(courselink.toString());


        }



    }
}
