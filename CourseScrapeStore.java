import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CourseScrapeStore {

    private String course_title_number;
    private String course_detail;
    private String dates;               //dates when the class takes place
    private String days_time;           //days and time the class meets
    private String grading;             // field in class info i.e Undergraduate Letter Grades
    private String instruction_mode;    //in person or online etc...
    private String campus;              //what campus the class takes place in
    private String class_components;    //i.e lecture
    private String course_number;       // class number i.e 38058
    private String room;                //which room the lecture takes place in
    private String instructor;          //name of instructor
    private String location;            // similar to campus i.e "Main - Brooklyn College;
    private String status;              // i.e waitlist.
    private String session;             // i.e regular academic session
    private String units;              // units i.e credits
    private Integer wait_list_capacity; // usually 5 max wait list cap
    private Integer wait_list_total;    // how many are in wait list
    private Integer enrolled;           //how many are enrolled in clas
    private Integer class_capacity;     //class capacity
    private Integer available_seats;    // available seats in class
    private WebDriver driver;

    public CourseScrapeStore(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sk\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe");
        this.driver = new ChromeDriver();

    };
    public CourseScrapeStore(String link) {
        //selects element with a certain id or xpath, to get xpath or id use developer tools -> click on an element with selector tool ->  right click the code that gets highlighted ->  copy -> "XPATH", same thing for id except copy "selector"
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sk\\Downloads\\chromedriver_win32 (2)\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        // setting headless mode to true.. so there isn't any ui
        chromeOptions.setHeadless(true);
        try {

            driver = new ChromeDriver(chromeOptions); // opens up chrome browser

            driver.get("https://globalsearch.cuny.edu/CFGlobalSearchTool/search.jsp"); //navigates to the link specified
            driver.findElement(By.id("magicpowerpd")).click();//next_btn
            driver.findElement(By.id("BKL01")).click(); //this clicks on a button with the specific id
            // selectByVisibleText("2020 spring");
            Select dropdown = new Select(driver.findElement(By.id("t_pd"))); //selects drop down
            dropdown.selectByIndex(2); //selects an option from the dropdown
            driver.findElement(By.className("SSSBUTTON_CONFIRMLINK")).click(); // clicks button with the class name
            driver.get(link);
            course_title_number = driver.findElement(By.id("DERIVED_CLSRCH_DESCR200")).getText();
            course_detail = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/span[2]")).getText();
            dates = driver.findElement(By.xpath("//*[@id=\"ACE_$ICField37\"]/tbody/tr[4]/td")).getText();               //dates when the class takes place
            days_time = driver.findElement(By.id("MTG_SCHED$0")).getText();          //days and time the class meets
            grading = driver.findElement(By.xpath("//*[@id=\"ACE_$ICField37\"]/tbody/tr[6]/td")).getText();             // field in class info i.e Undergraduate Letter Grades
            instruction_mode = driver.findElement(By.id("CAMPUS_TBL_DESCR")).getText();    //in person or online etc...
            campus = driver.findElement(By.xpath("//*[@id=\"ACE_$ICField37\"]/tbody/tr[10]/td")).getText();              //what campus the class takes place in
            class_components = driver.findElement(By.xpath("//*[@id=\"ACE_$ICField56\"]/tbody/tr[12]/td")).getText();    //i.e lecture
            course_number = driver.findElement(By.id("SSR_CLS_DTL_WRK_SSR_DATE_LONG")).getText();       // class number i.e 38058
            room = driver.findElement(By.id("MTG_LOC$0")).getText();                //which room the lecture takes place in
            instructor = driver.findElement(By.id("MTG_INSTR$0")).getText();         //name of instructor
            location = driver.findElement(By.xpath("//*[@id=\"ACE_$ICField37\"]/tbody/tr[8]/td")).getText();            // similar to campus i.e "Main - Brooklyn College;
            status = driver.findElement(By.id("SSR_CLS_DTL_WRK_SSR_DESCRSHORT")).getText(); //get text of status             // i.e waitlist.
            session = driver.findElement(By.id("GRADE_BASIS_TBL_DESCRFORMAL")).getText();            // i.e regular academic session
            units = driver.findElement(By.id("CAMPUS_LOC_VW_DESCR")).getText();              // units i.e credits
            wait_list_capacity = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_WAIT_CAP")).getText()); // usually 5 max wait list cap
            wait_list_total = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_WAIT_TOT")).getText());    // how many are in wait list
            enrolled = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_ENRL_TOT")).getText());           //how many are enrolled in clas
            class_capacity = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_ENRL_CAP")).getText());     //class capacity
            available_seats = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_AVAILABLE_SEATS")).getText());
            String projectID = "class-notification-app";
            FileInputStream serviceAccount =
                    new FileInputStream("C:\\Users\\sk\\Documents\\brooklyn college\\class-notification-app-firebase-adminsdk-l62tb-87b92f0156.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://class-notification-app.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            Firestore db = FirestoreClient.getFirestore();
            Map<String, Object> docData = new HashMap<>();
            docData.put("course detail", course_detail);
            docData.put("dates", dates);
            docData.put("days & times", days_time);
            docData.put("grading", grading);
            docData.put("instruction mode", instruction_mode);
            docData.put("campus", campus);
            docData.put("class components", class_components);
            docData.put("course number", course_number);
            docData.put("room", room);
            docData.put("instructor", instructor);
            docData.put("location", location);
            docData.put("status", status);
            docData.put("units", units);
            docData.put("wait list capacity", wait_list_capacity);
            docData.put("wait list total", wait_list_total);
            docData.put("enrolled", enrolled);
            docData.put("class capacity", class_capacity);
            docData.put("available seats", available_seats);

            String course_collection_name = course_title_number.substring(0 , 12);
            String course_section_doc_name = course_title_number.substring(13, 18);

            course_collection_name = course_collection_name.replaceAll("\\.","");
            course_section_doc_name = course_section_doc_name.replaceAll("-","");

            System.out.println("course number collection "+course_collection_name);
            System.out.println("course section number "+course_section_doc_name);
            //  docData.put("regions", Arrays.asList("west_coast", "socal"));
            // Add a new document (asynchronously) in collection "cities" with id "LA"
            ApiFuture<WriteResult> future = db.collection("cuny").document("BLK01").collection("courses").document("Computer Science").collection(course_collection_name).document(course_section_doc_name).set(docData);
            //  ...
            //  future.get() blocks on response
            System.out.println("Update time : " + future.get().getUpdateTime());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }


    public void newCunyLinkScrapeWithBrowser(String link, Set<Cookie> cookies) { //method to get new link, and view the browser
        //selects element with a certain id or xpath, to get xpath or id use developer tools -> click on an element with selector tool ->  right click the code that gets highlighted ->  copy -> "XPATH", same thing for id except copy "selector"


        try {
            System.out.println("here");
            for( Cookie cookie : cookies){
                System.out.println("hi");
            driver.manage().addCookie(cookie);
            }
            driver.get(link);
            System.out.println("cookies "+driver.manage().getCookies());
            //Actions action = new Actions(driver);

            //driver.get(link.getAttribute("href"));

//            action.keyDown(Keys.LEFT_CONTROL)
//                    .click(link)
//                    .keyUp(Keys.LEFT_CONTROL)
//                    .build()
//                    .perform();
            course_title_number = driver.findElement(By.id("DERIVED_CLSRCH_DESCR200")).getText();
            course_detail = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/span[2]")).getText();
            dates = driver.findElement(By.id("SSR_CLS_DTL_WRK_SSR_DATE_LONG")).getText();               //dates when the class takes place
            days_time = driver.findElement(By.id("MTG_SCHED$0")).getText();          //days and time the class meets
            grading = driver.findElement(By.id("GRADE_BASIS_TBL_DESCRFORMAL")).getText();             // field in class info i.e Undergraduate Letter Grades
            instruction_mode = driver.findElement(By.id("CAMPUS_TBL_DESCR")).getText();    //in person or online etc...
            campus = driver.findElement(By.id("CAMPUS_TBL_DESCR")).getText();              //what campus the class takes place in
            class_components = driver.findElement(By.id("CAMPUS_TBL_DESCR")).getText();    //i.e lecture
            course_number = driver.findElement(By.id("SSR_CLS_DTL_WRK_SSR_DATE_LONG")).getText();       // class number i.e 38058
            room = driver.findElement(By.id("MTG_LOC$0")).getText();                //which room the lecture takes place in
            instructor = driver.findElement(By.id("MTG_INSTR$0")).getText();         //name of instructor
            location = driver.findElement(By.id("CAMPUS_LOC_VW_DESCR")).getText();            // similar to campus i.e "Main - Brooklyn College;
            status = driver.findElement(By.id("SSR_CLS_DTL_WRK_SSR_DESCRSHORT")).getText(); //get text of status             // i.e waitlist.
            session = driver.findElement(By.id("GRADE_BASIS_TBL_DESCRFORMAL")).getText();            // i.e regular academic session
            units = driver.findElement(By.id("CAMPUS_LOC_VW_DESCR")).getText();              // units i.e credits
            wait_list_capacity = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_WAIT_CAP")).getText()); // usually 5 max wait list cap
            wait_list_total = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_WAIT_TOT")).getText());    // how many are in wait list
            enrolled = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_ENRL_TOT")).getText());           //how many are enrolled in clas
            class_capacity = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_ENRL_CAP")).getText());     //class capacity
            available_seats = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_AVAILABLE_SEATS")).getText());
            String projectID = "class-notification-app";
            FileInputStream serviceAccount =
                    new FileInputStream("C:\\Users\\sk\\Documents\\brooklyn college\\class-notification-app-firebase-adminsdk-l62tb-87b92f0156.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://class-notification-app.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            Firestore db = FirestoreClient.getFirestore();
            Map<String, Object> docData = new HashMap<>();
            docData.put("course detail", course_detail);
            docData.put("dates", dates);
            docData.put("days & times", days_time);
            docData.put("grading", grading);
            docData.put("instruction mode", instruction_mode);
            docData.put("campus", campus);
            docData.put("class components", class_components);
            docData.put("course number", course_number);
            docData.put("room", room);
            docData.put("instructor", instructor);
            docData.put("location", location);
            docData.put("status", status);
            docData.put("units", units);
            docData.put("wait list capacity", wait_list_capacity);
            docData.put("wait list total", wait_list_total);
            docData.put("enrolled", enrolled);
            docData.put("class capacity", class_capacity);
            docData.put("available seats", available_seats);

            String course_collection_name = course_title_number.substring(0 , 12);
            String course_section_doc_name = course_title_number.substring(13, 18);

            course_collection_name = course_collection_name.replaceAll("\\.","");
            course_section_doc_name = course_section_doc_name.replaceAll("-","");

            System.out.println("course number collection "+course_collection_name);
            System.out.println("course section number "+course_section_doc_name);
            //  docData.put("regions", Arrays.asList("west_coast", "socal"));
            // Add a new document (asynchronously) in collection "cities" with id "LA"
            ApiFuture<WriteResult> future = db.collection("cuny").document("BLK01").collection("courses").document("Accounting").collection(course_collection_name).document(course_section_doc_name).set(docData);
            //  ...
            //  future.get() blocks on response
            System.out.println("Update time : " + future.get().getUpdateTime());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void newCunyLinkScrapeNoBrowser(String link) { //this method will not open up a new chrome browser
        //selects element with a certain id or xpath, to get xpath or id use developer tools -> click on an element with selector tool ->  right click the code that gets highlighted ->  copy -> "XPATH", same thing for id except copy "selector"

        ChromeOptions chromeOptions = new ChromeOptions();
        // setting headless mode to true.. so there isn't any ui
        chromeOptions.setHeadless(true); //set to true so no browser shows up (no GUI)
        driver = new ChromeDriver(chromeOptions); // opens up chrome browser
        try {
            driver.get(link);
            course_title_number = driver.findElement(By.id("DERIVED_CLSRCH_DESCR200")).getText();
            course_detail = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/form/span[2]")).getText();
            dates = driver.findElement(By.id("SSR_CLS_DTL_WRK_SSR_DATE_LONG")).getText();               //dates when the class takes place
            days_time = driver.findElement(By.id("MTG_SCHED$0")).getText();          //days and time the class meets
            grading = driver.findElement(By.id("GRADE_BASIS_TBL_DESCRFORMAL")).getText();             // field in class info i.e Undergraduate Letter Grades
            instruction_mode = driver.findElement(By.id("CAMPUS_TBL_DESCR")).getText();    //in person or online etc...
            campus = driver.findElement(By.id("CAMPUS_TBL_DESCR")).getText();              //what campus the class takes place in
            class_components = driver.findElement(By.id("CAMPUS_TBL_DESCR")).getText();    //i.e lecture
            course_number = driver.findElement(By.id("SSR_CLS_DTL_WRK_SSR_DATE_LONG")).getText();       // class number i.e 38058
            room = driver.findElement(By.id("MTG_LOC$0")).getText();                //which room the lecture takes place in
            instructor = driver.findElement(By.id("MTG_INSTR$0")).getText();         //name of instructor
            location = driver.findElement(By.id("CAMPUS_LOC_VW_DESCR")).getText();            // similar to campus i.e "Main - Brooklyn College;
            status = driver.findElement(By.id("SSR_CLS_DTL_WRK_SSR_DESCRSHORT")).getText(); //get text of status             // i.e waitlist.
            session = driver.findElement(By.id("GRADE_BASIS_TBL_DESCRFORMAL")).getText();            // i.e regular academic session
            units = driver.findElement(By.id("CAMPUS_LOC_VW_DESCR")).getText();              // units i.e credits
            wait_list_capacity = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_WAIT_CAP")).getText()); // usually 5 max wait list cap
            wait_list_total = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_WAIT_TOT")).getText());    // how many are in wait list
            enrolled = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_ENRL_TOT")).getText());           //how many are enrolled in clas
            class_capacity = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_ENRL_CAP")).getText());     //class capacity
            available_seats = Integer.parseInt(driver.findElement(By.id("SSR_CLS_DTL_WRK_AVAILABLE_SEATS")).getText());

            //  Create a Map to store the data we want to set via firebase https://firebase.google.com/docs/firestore/manage-data/add-data


        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }


    @Override
    public String toString() {
        return "Course Details: \n" +
                "course_title_number='" + course_title_number + '\'' +
                ",\n course_detail='" + course_detail + '\'' +
                ",\n dates='" + dates + '\'' +
                ",\n days_time='" + days_time + '\'' +
                ",\n grading='" + grading + '\'' +
                ",\n instruction_mode='" + instruction_mode + '\'' +
                ",\n campus='" + campus + '\'' +
                ",\n class_components='" + class_components + '\'' +
                ",\n course_number='" + course_number + '\'' +
                ",\n room='" + room + '\'' +
                ",\n instructor='" + instructor + '\'' +
                ",\n location='" + location + '\'' +
                ",\n status='" + status + '\'' +
                ",\n session='" + session + '\'' +
                ",\n units='" + units + '\'' +
                ",\n wait_list_capacity=" + wait_list_capacity +
                ",\n wait_list_total=" + wait_list_total +
                ",\n enrolled=" + enrolled +
                ",\n class_capacity=" + class_capacity +
                ",\n available_seats=" + available_seats +
                ",\n driver=" + driver;
    }

    //setters
    public void setCourse_title_number(String course_title_number) {
        this.course_title_number = course_title_number;
    }

    public void setCourse_detail(String course_detail) {
        this.course_detail = course_detail;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public void setDays_time(String days_time) {
        this.days_time = days_time;
    }

    public void setGrading(String grading) {
        this.grading = grading;
    }

    public void setInstruction_mode(String instruction_mode) {
        this.instruction_mode = instruction_mode;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setClass_components(String class_components) {
        this.class_components = class_components;
    }

    public void setCourse_number(String course_number) {
        this.course_number = course_number;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setWait_list_capacity(Integer wait_list_capacity) {
        this.wait_list_capacity = wait_list_capacity;
    }

    public void setWait_list_total(Integer wait_list_total) {
        this.wait_list_total = wait_list_total;
    }

    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    public void setClass_capacity(Integer class_capacity) {
        this.class_capacity = class_capacity;
    }

    public void setAvailable_seats(Integer available_seats) {
        this.available_seats = available_seats;
    }

    public void setDriver(ChromeDriver driver) {
        this.driver = driver;
    }

    //getters
    public String getCourse_title_number() {
        return course_title_number;
    }

    public String getCourse_detail() {
        return course_detail;
    }

    public String getDates() {
        return dates;
    }

    public String getDays_time() {
        return days_time;
    }

    public String getGrading() {
        return grading;
    }

    public String getInstruction_mode() {
        return instruction_mode;
    }

    public String getCampus() {
        return campus;
    }

    public String getClass_components() {
        return class_components;
    }

    public String getCourse_number() {
        return course_number;
    }

    public String getRoom() {
        return room;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getSession() {
        return session;
    }

    public String getUnits() {
        return units;
    }

    public Integer getWait_list_capacity() {
        return wait_list_capacity;
    }

    public Integer getWait_list_total() {
        return wait_list_total;
    }

    public Integer getEnrolled() {
        return enrolled;
    }

    public Integer getClass_capacity() {
        return class_capacity;
    }

    public Integer getAvailable_seats() {
        return available_seats;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
