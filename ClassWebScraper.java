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
import org.jsoup.helper.HttpConnection;
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

        long startTime = System.currentTimeMillis();



        System.out.println(System.getenv("CHROME_DRIVER"));
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER")); //chrome driver

        ChromeOptions chromeOptions = new ChromeOptions();
        // setting headless mode to true.. so there isn't any ui
        chromeOptions.setHeadless(false);
        chromeOptions.addArguments("incognito");

        driver = new ChromeDriver(chromeOptions); // opens up chrome browser
        driver.get("https://globalsearch.cuny.edu/CFGlobalSearchTool/search.jsp"); //navigates to the link specified
        String school_name = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/table[1]/tbody/tr[2]/td[3]/span[13]/label")).getText();
        System.out.println("school name "+school_name);
       driver.findElement(By.id("BKL01")).click(); //this clicks on a button with the specific id
      //  driver.findElement(By.id("BLK01")).click(); //this clicks on a button with the specific id
        //  driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");

        Select dropdown = new Select(driver.findElement(By.id("t_pd"))); //selects drop down
        dropdown.selectByIndex(2); //selects an option from the dropdown
        driver.findElement(By.className("SSSBUTTON_CONFIRMLINK")).click(); // clicks confirm button with the class name

        dropdown = new Select(driver.findElement(By.id("subject_ld"))); //creates a dropdown menu Majors
        //  dropdown.selectByValue("CMIS"); // option to click on from drop down from the web page
        List<WebElement> allDropDownOptions = dropdown.getOptions();
        // dropdown.selectByIndex(12);
        boolean success = false;

        //  while(!success) {

        for (int i = 1; i < allDropDownOptions.size(); i++) {
            try {
                dropdown = new Select(driver.findElement(By.id("subject_ld"))); //creates a dropdown menu (Majors)
                dropdown.selectByIndex(i);
                dropdown = new Select(driver.findElement(By.id("courseCareerId"))); //new drop down menu for undergraduate
                //driver.findElements(By.xpath("//select[@id = 'courseCareerId')]/option[contains(text(), 'undergraduate  ')]")).click();
               // dropdown.selectByVisibleText("undergraduate");//select undergrad

                //because if we select different schools the undergraduate option can be different so we have to find it in every school
                List <WebElement> optionsInnerText= dropdown.getOptions(); // this will get all options from the dropdown menu

                for(WebElement option: optionsInnerText){//loop through the options in the list
                    String optionText = option.getText(); // get the text value from each option and store it into a string so we can compare it
                    if(optionText.toLowerCase().contains("undergraduate".toLowerCase())) //if the option menu matches the "undergraduate" then we want that option selected
                        dropdown.selectByVisibleText(optionText); //if true select the option
                }
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
                System.out.println("cookies +" + cookies);
                int startIndex = classLinksNoDuplicatesList.size() / 2; // this is for two seperate threads to go through the links and scrape at the same time instead of just one iterator
                int endIndex = classLinksNoDuplicatesList.size();// half to loop through

                //runnable threads to iterate through list of links to scrape
                Runnable t1 = new Runnable() {
                    public void run() {
                        try {
                            CourseScrapeStore courselinkScraper1 = new CourseScrapeStore();
                            for (int i = startIndex, count = 0; i < endIndex; i++) {
                                String subject = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/span[2]/strong[1]")).getText();//this will get the subject text and pass it through method so we can store it
                                String link = (String) classLinksNoDuplicatesList.get(i); // start from half way and go to the end
                                courselinkScraper1.newCunyLinkScrapeWithBrowser(link, cookies, subject, school_name); //pass link to method to scrape
                                count++;
                                System.out.println("scraper 2 link: " + count);
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
                                courselinkScraper2.newCunyLinkScrapeWithBrowser(link, cookies, subject, school_name); //call method to scrape link we pass
                                System.out.println("scraper 2 link count :" + i);
                            }

                            courselinkScraper2.close(); //close browser when done
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                    }
                };

                List<Thread> threads = new ArrayList<>(); //create new list of threads
                threads.add(new Thread(t1)); // add thread inside list
                threads.add(new Thread(t2)); //add second thread inside list
                for (Thread thread : threads) { //this loop starts all threads created
                    thread.start(); //start thread
                }
                for (Thread thread : threads) { //this loop makes sure all threads are finished before moving on to next code.
                    thread.join(); //wait for thread to finish.
                }

                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.history.go(-1)");
                success = true;


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }


}
