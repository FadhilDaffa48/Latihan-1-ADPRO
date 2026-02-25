package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.edge.EdgeDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.beans.factory.annotation.Value;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class EditDeleteFunctionalTest {
    @LocalServerPort
    private int localPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testUrl;

    @Value("Sampo cap Bambang")
    private String namaProduk;

    private String baseUrl;
    @BeforeEach
    void Starter(){
        baseUrl = String.format("%s:%d", testUrl, localPort);
    }

    @Test
    void checkDeletion(EdgeDriver driver){
        driver.get(baseUrl + "/product/create");

        driver.findElement(By.id("nameInput")).sendKeys(namaProduk);
        driver.findElement(By.id("quantityInput")).sendKeys("100");

        driver.findElement(By.tagName("button")).click();

        assertEquals(baseUrl + "/product/list", driver.getCurrentUrl());
        driver.findElement(By.id("deleteButton")).click();

        String xPath = "//tr[contains(text(), 'Sampo cap Bambang')]";

        assertEquals(0, driver.findElements(By.xpath(xPath)).size());
    }
}