import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public static Set<Cookie> cookies;

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
         dropdown.selectByIndex(7);

        dropdown = new Select(driver.findElement(By.id("courseCareerId"))); //new drop down from a different page or different menu
        dropdown.selectByValue("UGRD");//select undergrad
        driver.findElement(By.id("open_classId")).click(); //click element
        driver.findElement(By.id("btnGetAjax")).click(); //click element
        driver.findElement(By.id("imageDivLink_inst0")).click(); //click element

        List<WebElement> allLinksList = driver.findElements(By.tagName("a")); //find all links and store it in a list
        HashSet<String> allLinksNoDuplicatesSet = new HashSet<String>(); //create hashset to dump links into and get rid of duplicate links

        for (WebElement link : allLinksList) { //loop through list of all links and add them into hashset if they meet certain criteria to remove duplicates and unencessary links
            String hrefLink = link.getAttribute("href"); //store the href into a string so we can later pass to a method in the CourseScrapeStore class so we can scrape the link
            if (hrefLink != null) { //if link isn't null
                if (hrefLink.contains("https://globalsearch.cuny.edu/CFGlobalSearchTool/CFSearchToolController?class_number_searched") == true) //some links are unenncessary like the homepage link so we just check for the links with classes and add them into hashset
                    allLinksNoDuplicatesSet.add(hrefLink); // add class href link into set
            }
        }

        ArrayList<String> classLinksNoDuplicatesList = new ArrayList<>(allLinksNoDuplicatesSet); //convert set backinto a string arraylist so we can iterate half way
        System.out.println("Total no of links Available: " + classLinksNoDuplicatesList.size()); //check if the class links size matches with the result page links size

        cookies = driver.manage().getCookies(); //set cookies so we save the session. //cuny website needs cookies set when opening new browser and pasting link otherwise itll reroute you to homepage

        int startIndex = classLinksNoDuplicatesList.size() / 2; // this is for two seperate threads to go through the links and scrape at the same time instead of just one iterator
        int endIndex = classLinksNoDuplicatesList.size();// half to loop through

        //runnable threads to iterate through list of links to scrape
        Runnable t1 = new Runnable() {
            public void run() {
                try {
                    CourseScrapeStore courselinkScraper1 = new CourseScrapeStore();
                    for (int i = startIndex; i < endIndex; i++) {
                        String subject = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/span[2]/strong[1]")).getText();//this will get the subject text and pass it through method so we can store it
                        String link = (String) classLinksNoDuplicatesList.get(i); // start from half way and go to the end
                        courselinkScraper1.newCunyLinkScrapeWithBrowser(link, cookies, subject); //pass link to method to scrape
                    }
                    courselinkScraper1.close(); //close browser when done
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        };

        //second thread to iterate through list
        Runnable t2 = new Runnable() {
            public void run() {
                try {
                    CourseScrapeStore courselinkScraper2 = new CourseScrapeStore();
                    for (int i = 0; i < startIndex; i++) {
                        String subject = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/span[2]/strong[1]")).getText(); //this will get the subject text and pass it through method so we can store it
                        String link = (String) classLinksNoDuplicatesList.get(i); // this will iterate through our link arrays and start from beggining to the half way mark
                        courselinkScraper2.newCunyLinkScrapeWithBrowser(link, cookies, subject); //call method to scrape link we pass
                    }

                    courselinkScraper2.close(); //close browser when done
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        };

        new Thread(t1).start(); //start first thread
        new Thread(t2).start(); //start second thread

    }


}
