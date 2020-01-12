/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassWebScraper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Application;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
        /*        String option = "";
         System.setProperty("webdriver.chrome.driver", "C:\\Users\\sk\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe");
        
         driver  = new ChromeDriver();
         driver.get("https://globalsearch.cuny.edu/CFGlobalSearchTool/search.jsp");
         // driver.findElement(By.id("magicpowerpd")).click();//next_btn
         driver.findElement(By.id("BKL01")).click();
         // selectByVisibleText("2020 spring");
         Select dropdown = new Select(driver.findElement(By.id("t_pd")));
         dropdown.selectByIndex(2);
         driver.findElement(By.className("SSSBUTTON_CONFIRMLINK")).click();
         // TODO code application logic here
         //System.out.println("what is your subject?");
         //Scanner scan = new Scanner(System.in);
         //  option = scan.next();
         //  if(option.equalsIgnoreCase("s")){
         dropdown = new Select(driver.findElement(By.id("subject_ld")));
         dropdown.selectByValue("CMIS");
         System.out.println(dropdown);
         dropdown = new Select(driver.findElement(By.id("courseCareerId")));
         dropdown.selectByValue("UGRD");
         driver.findElement(By.id("open_classId")).click();
         driver.findElement(By.id("btnGetAjax")).click();
         driver.findElement(By.id("imageDivLink_inst0")).click();
         driver.findElement(By.id("imageDivLink1")).click();
         driver.get("https://globalsearch.cuny.edu/CFGlobalSearchTool/CFSearchToolController?class_number_searched=MzgwNDA=&session_searched=MQ==&term_searched=MTIwMg==&inst_searched=QnJvb2tseW4gQ29sbGVnZQ==");
         System.out.println(driver.getTitle());
         //  driver.findElement(By.xpath("//a[contains(.,'About')]")).click();
         //  }*/

// fetch the document over HTTP
        String urlhome = "https://globalsearch.cuny.edu/CFGlobalSearchTool/search.jsp";
        String url = "https://globalsearch.cuny.edu/CFGlobalSearchTool/CFSearchToolController?class_number_searched=MzgwNDA=&session_searched=MQ==&term_searched=MTIwMg==&inst_searched=QnJvb2tseW4gQ29sbGVnZQ==";
      //  Document doc = Jsoup.connect("https://globalsearch.cuny.edu/CFGlobalSearchTool/CFSearchToolController?class_number_searched=MzgwNDA=&session_searched=MQ==&term_searched=MTIwMg==&inst_searched=QnJvb2tseW4gQ29sbGVnZQ==").get();
        //  System.out.println(doc);
        Response res = Jsoup.connect(urlhome).execute();
        //System.out.println( con.toString());
       // Map<String, String> cookie = res.cookies();
        String cookie  = res.cookie("JSESSIONID");
        System.out.println(cookie);
        try {

            res = Jsoup.connect(url)
                    .cookie("JSESSIONID", cookie)
                    .method(org.jsoup.Connection.Method.GET)
                    .execute();

         //   System.out.println(res.parse());
            Document doc2 = Jsoup.connect(url)
            .cookie("JSESSIONID", cookie)
            .get();
             System.out.println(doc2);
           // System.out.println("Autenticato");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
