import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

public class YandexZenArticle {

    private WebDriver driver;
    static WebDriverWait wait;

    public YandexZenArticle(WebDriver driver) throws IOException {
        this.driver = driver;
    }


    public void addArticleToYandexZen(String translatedArticleTitle, List<String> translatedTextList) {
        wait = new WebDriverWait(driver, 5);

        // ждём, пока появится кнопка "+":
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[@class='control link link_theme_zen']"))));

        // нажать на кнопку "+":
        driver.findElement(By.xpath("//span[@class='control link link_theme_zen']")).click();

        // ждём, пока появится кнопка "статья":
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='new-publication-dropdown__button']"))));

        // нажать на кнопку "статья":
        driver.findElement(By.xpath("//div[@class='new-publication-dropdown__button']")).click();

        // ждём, пока появится окно с подсказками:
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'Статья')]")));
        // закрываем появившееся окно с подсказками:
        driver.findElement(By.xpath("//div[@class='close-cross close-cross_black close-cross_size_s help-popup__close-cross']")).click();

        // ждём, когда появится окно с подсказкой:
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'Теперь в статью можно вставить виджет Яндекс Форм')]")));
        // закрываем появившееся окно с подсказкой:
        driver.findElement(By.xpath("//div[@class='ui-lib-popup-element__close']")).click();

        // ждём, пока появится поле ввода текста:
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='editor__content']")));

        // копируем заголовок в поле ввода заголовка:
        driver.findElement(By.xpath("//div[@class='editable-input editor__title-input']//div[@class='notranslate public-DraftEditor-content']")).sendKeys(translatedArticleTitle);

        // копируем текст поабзацно в поле ввода статьи:
        for (String paragraph : translatedTextList) {
            WebElement webTextInputField = driver.findElement(By.xpath("//div[@class='zen-editor']//div[@class='notranslate public-DraftEditor-content']"));
            webTextInputField.sendKeys(paragraph);
            webTextInputField.sendKeys(Keys.ENTER);
        }

        // ждём, пока появится надпись "Сохранено":
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Сохранено')]")));
    }

}