package stepdefinitions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

public class Hooks {

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        // Optional: log scenario start to Extent
        ExtentTest test = ExtentCucumberAdapter.getCurrentScenario();
        if (test != null) {
            test.log(Status.INFO, "Starting scenario: " + scenario.getName());
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        ExtentTest test = ExtentCucumberAdapter.getCurrentScenario();

        if (scenario.isFailed()) {
            // Take screenshot
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // Attach to Cucumber report (still works)
            scenario.attach(screenshot, "image/png", "failure_screenshot");

            // Also attach to Extent Report
            if (test != null) {
                String base64Screenshot = java.util.Base64.getEncoder().encodeToString(screenshot);
                test.log(Status.FAIL, "Screenshot: " +
                        test.addScreenCaptureFromBase64String(base64Screenshot, "Failure Image").getModel().getMedia().get(0).getPath());
                test.log(Status.FAIL, "Test Failed: " + scenario.getStatus());
            }
        } else {
            if (test != null) {
                test.log(Status.PASS, "Scenario passed: " + scenario.getName());
            }
        }

        DriverManager.quitDriver();
    }
}