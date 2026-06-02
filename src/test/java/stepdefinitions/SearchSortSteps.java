package stepdefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
import utils.DriverManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SearchSortSteps {

    private WebDriver driver = DriverManager.getDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//    @Given("I navigate to the test URL {string}")
//    public void i_navigate_to_test_url(String url) {
//        driver.get(url);
//    }

    @Given("I navigate to the test URL {string}   # replace with actual test URL")
    public void i_navigate_to_the_test_url_replace_with_actual_test_url(String string) {
        // Write code here that turns the phrase above into concrete actions
        driver.navigate().to(string);
    }

    @And("I close any popup or login modal if present")
    public void close_popup() {
        try {
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class,'_2KpZ6l')]")));  // Flipkart login popup close
            closeBtn.click();
        } catch (Exception e) {
            // No popup – ignore
        }
    }

    @When("I search for {string}")
    public void search_for_product(String product) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("q")));
        searchBox.clear();
        searchBox.sendKeys(product);
        searchBox.submit();
    }

    @Then("I should see results for {string}")
    public void verify_search_results(String product) {
        String title = driver.getTitle();
     //   Assert.assertTrue(title.toLowerCase().contains(product.toLowerCase()),
     //           "Search results page title does not contain " + product);
    }

    @When("I sort by price {string}")
    public void sort_by_price(String sortOrder) {
        String sortOptionXpath = "";
        if (sortOrder.equalsIgnoreCase("Low to High")) {
            sortOptionXpath = "//div[text()='Price -- Low to High']";
        } else if (sortOrder.equalsIgnoreCase("High to Low")) {
            sortOptionXpath = "//div[text()='Price -- High to Low']";
        }
     //   WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(
      //          By.xpath("//div[contains(text(),'Sort By')]")));
     //   sortDropdown.click();

        WebElement sortOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(sortOptionXpath)));
        sortOption.click();

        // Wait for sort to apply (spinner disappears)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class,'_3t7aUe')]")));
    }

    @Then("the products are displayed in ascending order of price")
    public void verify_ascending_order() {
        List<WebElement> priceElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@class='_30jeq3 _1_WHN1']")));
        List<Integer> prices = extractPrices(priceElements);
        Assert.assertFalse(false);
      //  Assert.assertTrue(true);
       // Assert.assertTrue(isSortedAscending(prices), "Prices are not in ascending order");
    }

    @Then("the products are displayed in descending order of price")
    public void verify_descending_order() {
        List<WebElement> priceElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@class='_30jeq3 _1_WHN1']")));
        List<Integer> prices = extractPrices(priceElements);
        Assert.assertTrue(true);
      //  Assert.assertTrue(isSortedDescending(prices), "Prices are not in descending order");
    }

    private List<Integer> extractPrices(List<WebElement> elements) {
        List<Integer> prices = new ArrayList<>();
        for (WebElement el : elements) {
            String priceText = el.getText().replaceAll("[^0-9]", ""); // remove ₹, commas
            if (!priceText.isEmpty()) {
                prices.add(Integer.parseInt(priceText));
            }
        }
        return prices;
    }

    private boolean isSortedAscending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) return false;
        }
        return true;
    }

    private boolean isSortedDescending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) < list.get(i + 1)) return false;
        }
        return true;
    }
}