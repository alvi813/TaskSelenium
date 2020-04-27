import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.*;


public class SeleniumTask {

    private static final List<String> DRIVER_NAMES = new ArrayList<String>() {{
        add("webdriver.gecko.driver");
        add("webdriver.chrome.driver");
    }};

    static WebDriverWait wait;
    static int numberOfLinkInTextFile = 1; // номер ссылки из текстового файла, с которой будем работать

    String urlTextFilePathName = "src/main/files/startUrl.txt";
    String yandexZenChannelUrl = "https://zen.yandex.ru/profile/editor/id/5ea219f9b6e0833eb9eeaa9f";
    String firefoxProfile = "VA";

    static public void setDriver(String driverName, String driverFilePath) {
        System.setProperty(driverName, driverFilePath);
    }

    public void openWebPage(WebDriver driver, String urlLine) {
        driver.get(urlLine);
    }

    public void specifyFirefoxProfile(String firefoxProfile) {
        System.setProperty("webdriver.firefox.profile", firefoxProfile);
    }

    public void closeDriver(WebDriver driver) {
        driver.quit();
    }


    public static void main(String[] args) throws IOException {

        SeleniumTask seleniumTask = new SeleniumTask();
        setDriver(DRIVER_NAMES.get(0), "drivers/geckodriver.exe");


        for(int counterOnHowManyPagesWillCollectLinks = 10;
            numberOfLinkInTextFile <= counterOnHowManyPagesWillCollectLinks;
            numberOfLinkInTextFile++) {

            WebDriver firefoxDriver = new FirefoxDriver();

            LinksCollection linksCollection = new LinksCollection(firefoxDriver, seleniumTask.urlTextFilePathName);
            String urlLine = linksCollection.getUrlLineFromFile(numberOfLinkInTextFile);
            seleniumTask.openWebPage(firefoxDriver, urlLine);
            List<WebElement> webLinksList = linksCollection.collectWebElementLinks(urlLine);
            Set<String> urlLinksSet = linksCollection.collectNonrepeatingStringLinks(webLinksList);
            linksCollection.writeLinksToFile(urlLinksSet);
            //linksCollection.printLinkCollectionToConsole(urlLinksSet);

            ParagraphsCollection paragraphsCollection = new ParagraphsCollection(firefoxDriver);
            List<WebElement> webTextList = paragraphsCollection.collectWebElementParagraphs();
            List<String> textList = paragraphsCollection.collectStringParagraphs(webTextList);
            String articleTitle = paragraphsCollection.collectArticleTitle(firefoxDriver);
            //paragraphsCollection.printArticleTitleToConsole(articleTitle);
            //paragraphsCollection.printTextCollectionToConsole(textList);

            seleniumTask.closeDriver(firefoxDriver);


            //--------------------------------------------------------------------------------------------------


            WebDriver firefoxDriver2 = new FirefoxDriver();

            TextTranslation textTranslation = new TextTranslation(firefoxDriver2);
            String googleTranslateUrl = textTranslation.getGoogleTranslateUrl();
            seleniumTask.openWebPage(firefoxDriver2, googleTranslateUrl);
            List<String> translatedTextList = textTranslation.translateParagraphs(textList);
            String translatedArticleTitle = textTranslation.translateArticleTitle(articleTitle);
            //textTranslation.printTranslatedTextWithTitleToConsole(translatedArticleTitle, translatedTextList);

            seleniumTask.closeDriver(firefoxDriver2);


            //--------------------------------------------------------------------------------------------------


            seleniumTask.specifyFirefoxProfile(seleniumTask.firefoxProfile);
            WebDriver firefoxDriver3 = new FirefoxDriver();

            YandexZenArticle yandexZenArticle = new YandexZenArticle(firefoxDriver3);
            seleniumTask.openWebPage(firefoxDriver3, seleniumTask.yandexZenChannelUrl);
            yandexZenArticle.addArticleToYandexZen(translatedArticleTitle, translatedTextList);

            seleniumTask.closeDriver(firefoxDriver3);
        }
    }
}
