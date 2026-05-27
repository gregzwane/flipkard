package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = System.getProperty("browser", "chrome").toLowerCase();
            switch (browser) {
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOpts = new EdgeOptions();
                    edgeOpts.addArguments("--headless");
                    edgeOpts.addArguments("--disable-gpu");
                    driver.set(new EdgeDriver(edgeOpts));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOpts = new FirefoxOptions();
                    firefoxOpts.addArguments("--headless");
                    driver.set(new FirefoxDriver(firefoxOpts));
                    break;
                default: // chrome
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOpts = new ChromeOptions();
                    chromeOpts.addArguments("--headless");
                    chromeOpts.addArguments("--disable-gpu");
                    chromeOpts.addArguments("--no-sandbox");
                    driver.set(new ChromeDriver(chromeOpts));
                    break;
            }
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}