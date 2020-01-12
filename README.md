# classnotificationapp
project that will notify a person when their class has room available to sign up for

Project will use JSOUP or SELENIUM headless browser with Chrome driver to scrape https://globalsearch.cuny.edu for class and the seat numbers and will update a firebase firestore database and then use that database to connect to android mobile app and give notifications based on the changes in the database

# Links
https://jsoup.org/download to download jsoup libary. To add downloaded lib into IDE google " how to add JSOUP to (your IDE)"
https://selenium.dev/downloads/ to download selenium library for headless browser just incase JSOUP won't work. To add downloaded lib into IDE google " how to add SELENIUM to (your IDE)"

https://chromedriver.chromium.org/ to download headless browser that works together with selenium. need to also add this to IDE
