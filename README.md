 BDD + Selenium framework. It includes all setup steps, framework explanation, and the two CI/CD YAML files inline.

markdown
# Selenium BDD Test Framework – Login + Search & Sort (Galaxy Z Fold)

This framework automates **login functionality** (20+ test scenarios) and **product search + price sorting** for an e‑commerce website (e.g., Flipkart). It uses:

- **Java 17** + **Maven**
- **Selenium WebDriver** (headless by default)
- **Cucumber (BDD)** with Gherkin feature files
- **JUnit** as the test runner
- **Cross‑browser** support: Chrome, Edge, Firefox
- **CI/CD** ready: GitHub Actions + GitLab CI

---

## 1. Prerequisites

Make sure the following are installed on your local machine:

| Tool         | Version | Verification Command        |
|--------------|---------|-----------------------------|
| Java JDK     | 17+     | `java -version`             |
| Maven        | 3.9+    | `mvn -version`              |
| Git          | latest  | `git --version`             |
| Chrome/Edge/Firefox | latest | (optional – headless runs without GUI) |

---

## 2. Clone the Repository

Replace the URL below with your actual Git repository URL.

```bash
git clone https://github.com/your-org/your-repo-name.git
cd your-repo-name
3. Project Structure
text
.
├── pom.xml
├── README.md
├── .github/workflows/selenium-tests.yml
├── .gitlab-ci.yml
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   ├── runners/
│   │   │   │   └── TestRunner.java
│   │   │   ├── stepdefinitions/
│   │   │   │   ├── Hooks.java
│   │   │   │   ├── LoginSteps.java          # (optional – for login tests)
│   │   │   │   └── SearchSortSteps.java
│   │   │   └── utils/
│   │   │       └── DriverManager.java
│   │   └── resources/
│   │       └── features/
│   │           ├── login.feature
│   │           └── search_sort_galaxy_zfold.feature
4. Configuration – Test URL & Browsers
All browser settings are in utils/DriverManager.java.
Headless mode is enabled for all browsers (Chrome, Edge, Firefox).
The base URL is defined in the feature file’s Background step:

gherkin
Given I navigate to the test URL "https://www.flipkart.com"
Change this URL in search_sort_galaxy_zfold.feature (and login.feature) to match your test environment.

5. Running Tests Locally
5.1 Run with default browser (Chrome headless)
bash
mvn clean test
5.2 Run with a specific browser
bash
# Chrome
mvn test -Dbrowser=chrome

# Edge
mvn test -Dbrowser=edge

# Firefox
mvn test -Dbrowser=firefox
5.3 Run only login scenarios
bash
mvn test -Dcucumber.filter.tags="@login"
5.4 Run only search & sort scenarios
bash
mvn test -Dcucumber.filter.tags="@search and @price"
After execution, reports are generated in:

target/cucumber-reports-<browser>/index.html – HTML report

target/surefire-reports/ – JUnit XML reports

6. Feature File Explanation
File: src/test/resources/features/search_sort_galaxy_zfold.feature

gherkin
@search @sort @price
Feature: Search for Galaxy Z Fold and sort by price

  Background:
    Given I navigate to the test URL "https://www.flipkart.com"
    And I close any popup or login modal if present

  @lowToHigh
  Scenario Outline: Search for "Galaxy Z Fold" and sort price Low to High
    When I search for "<product>"
    Then I should see results for "<product>"
    When I sort by price "Low to High"
    Then the products are displayed in ascending order of price

    Examples:
      | product        |
      | Galaxy Z Fold  |

  @highToLow
  Scenario Outline: Search for "Galaxy Z Fold" and sort price High to Low
    When I search for "<product>"
    Then I should see results for "<product>"
    When I sort by price "High to Low"
    Then the products are displayed in descending order of price

    Examples:
      | product        |
      | Galaxy Z Fold  |
Background steps run before every scenario (navigate + close popup).

Scenario Outline with Examples allows easy addition of more products later.

Tags (@lowToHigh, @highToLow) let you run a single sorting direction.

7. Step Definitions Explanation
File: src/test/java/stepdefinitions/SearchSortSteps.java

Step	Implementation Summary
I navigate to the test URL	Calls driver.get(url)
I close any popup or login modal if present	Tries to click Flipkart’s login popup close button; ignores if not present
I search for "..."	Finds search box by name="q", enters text, submits
I should see results for "..."	Asserts page title contains the product name
I sort by price "Low to High"	Clicks “Sort By” dropdown, selects the option, waits for spinner to disappear
products displayed in ascending/descending order	Extracts all product prices (remove ₹ and commas), converts to integers, verifies order
Extract price logic – handles Indian currency format (₹54,999 → 54999).

8. Test Runner Explanation
File: src/test/java/runners/TestRunner.java

java
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"stepdefinitions"},
    plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"},
    tags = "@search and @price"
)
public class TestRunner {}
features – location of .feature files.

glue – package where step definitions live.

plugin – generates pretty console output, HTML, and JSON reports.

tags – filters scenarios to run. You can override via Maven: -Dcucumber.filter.tags="@smoke".

9. CI/CD Pipelines
9.1 GitHub Actions – .github/workflows/selenium-tests.yml
This workflow runs on every push to main/develop, on pull requests, and daily at 6 AM UTC. It uses a matrix to execute tests in parallel on Chrome, Edge, and Firefox (all headless).

yaml
name: Selenium BDD Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]
  schedule:
    - cron: '0 6 * * *'

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      fail-fast: false
      matrix:
        browser: [chrome, edge, firefox]
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Cache Maven dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
      - name: Install Microsoft Edge
        run: |
          curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg
          sudo install -o root -g root -m 644 microsoft.gpg /etc/apt/trusted.gpg.d/
          sudo sh -c 'echo "deb [arch=amd64] https://packages.microsoft.com/repos/edge stable main" > /etc/apt/sources.list.d/microsoft-edge-dev.list'
          sudo apt-get update
          sudo apt-get install -y microsoft-edge-stable
      - name: Run tests
        run: mvn clean test -Dbrowser=${{ matrix.browser }} -Dcucumber.filter.tags="@search and @price"
      - name: Archive reports
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: cucumber-report-${{ matrix.browser }}
          path: target/cucumber-reports-${{ matrix.browser }}/
9.2 GitLab CI – .gitlab-ci.yml
This pipeline runs three parallel jobs (Chrome, Edge, Firefox) using a Docker image that contains all browsers.

yaml
image: mcr.microsoft.com/playwright:v1.40.0-focal

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository"

cache:
  paths:
    - .m2/repository

stages:
  - test

before_script:
  - apt-get update && apt-get install -y maven

chrome:
  stage: test
  script:
    - mvn clean test -Dbrowser=chrome -Dcucumber.filter.tags="@search and @price"
  artifacts:
    when: always
    paths:
      - target/cucumber-reports-chrome/
    reports:
      junit: target/surefire-reports/*.xml

edge:
  stage: test
  script:
    - mvn clean test -Dbrowser=edge -Dcucumber.filter.tags="@search and @price"
  artifacts:
    when: always
    paths:
      - target/cucumber-reports-edge/

firefox:
  stage: test
  script:
    - mvn clean test -Dbrowser=firefox -Dcucumber.filter.tags="@search and @price"
  artifacts:
    when: always
    paths:
      - target/cucumber-reports-firefox/
Note: The Playwright Docker image includes Edge, Chrome, and Firefox. No extra installation is required.

10. Login Test Cases Summary
The framework includes 20 detailed login test cases covering:

Category	Example Scenario
Positive	Valid credentials
Negative	Invalid password, unregistered email
Validation	Empty fields, malformed email
Edge	Leading/trailing spaces, max length email
Security	SQL injection, XSS, brute force protection
Usability	Remember Me, password masking, keyboard nav
These are implemented in login.feature and LoginSteps.java (not shown here for brevity).

11. Troubleshooting
Issue	Solution
WebDriverException: unknown error: cannot find Chrome binary	Install Chrome or set webdriver.chrome.driver system property. WebDriverManager usually handles it.
Tests fail in headless mode	Run locally with -Dbrowser=chrome without headless to debug. Modify DriverManager temporarily.
Edge browser not found in CI	Use the provided GitHub Actions step or GitLab’s Playwright image.
Cucumber step definitions not found	Ensure glue in TestRunner matches the package name exactly (case‑sensitive).
HTML report shows no data	Check that target/cucumber-reports-<browser> is created; if not, add @CucumberOptions(plugin = {"html:..."}).
12. Contributing & Extending
To add a new test scenario, create a new .feature file under src/test/resources/features/.

Implement step definitions in stepdefinitions package.

For new browser support, extend DriverManager and add the browser to CI matrix.

To run a single tag locally: mvn test -Dcucumber.filter.tags="@smoke"
