package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class EditDeleteFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    void createProduct(ChromeDriver driver, String name, String quantity) {
        driver.get(baseUrl + "/product/create");
        driver.findElement(By.name("productName")).sendKeys(name);
        driver.findElement(By.name("productQuantity")).sendKeys(quantity);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }


    @Test
    void testEditProduct_Success(ChromeDriver driver) {
        createProduct(driver, "Produk Awal", "10");
        driver.get(baseUrl + "/product/list");

        WebElement editButton = driver.findElement(By.xpath("//td[contains(text(), 'Produk Awal')]/..//a[contains(@href, 'edit')]"));
        editButton.click();

        WebElement nameInput = driver.findElement(By.name("productName"));
        nameInput.clear();
        nameInput.sendKeys("Produk Diedit");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Produk Diedit"));
    }

    @Test
    void testDeleteProduct_Success(ChromeDriver driver) {
        createProduct(driver, "Produk Dihapus", "5");
        driver.get(baseUrl + "/product/list");

        WebElement deleteButton = driver.findElement(By.xpath("//td[contains(text(), 'Produk Dihapus')]/..//a[contains(@href, 'delete')]"));
        deleteButton.click();

        String pageSource = driver.getPageSource();
        assertFalse(pageSource.contains("Produk Dihapus"));
    }


    @Test
    void testEditProduct_Negative_IdNotFound(ChromeDriver driver) {
        driver.get(baseUrl + "/product/edit/id-ngasal-yang-pasti-tidak-ada");

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product/list"));
    }

    @Test
    void testDeleteProduct_Negative_IdNotFound(ChromeDriver driver) {

        driver.get(baseUrl + "/product/delete/id-ngasal-yang-pasti-tidak-ada");

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/product/list"));
    }
}