# Flipkard Test Automation

Selenium Test Project | Web Site Testing

## Table of Contentes

- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contribution)
- [License](#license)

## Installation
Java + Maven + Cucumber Installation Guide (Windows, macOS, and Linux)

This guide sets up a complete automation testing environment using:

Java JDK 17 or 21 (LTS recommended)
Maven
Cucumber
Selenium WebDriver
IDE: IntelliJ IDEA or Visual Studio Code
Prerequisites
Component	Recommended Version
Java	JDK 17 or JDK 21
Maven	3.9.x or later
Cucumber	7.x
Selenium	4.x
Test Runner	JUnit 5
1. Install Java
Verify Existing Installation

Open a terminal or command prompt:

java -version
javac -version

If Java is not installed, follow your operating system instructions below.

Windows Installation
Step 1: Download Java

Download the JDK from:

Oracle JDK
Eclipse Temurin (Recommended)

Install JDK 17 or 21.

Step 2: Configure Environment Variables

Open:

Settings → System → About → Advanced System Settings → Environment Variables

Create:

JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21

Add to Path:

%JAVA_HOME%\bin
Step 3: Verify Installation
java -version
javac -version
macOS Installation
Install with Homebrew

Install Homebrew if needed.

brew install openjdk@21

Configure Java:

sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk \
/Library/Java/JavaVirtualMachines/openjdk-21.jdk

Add to your shell profile (~/.zshrc):

export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH=$JAVA_HOME/bin:$PATH

Reload:

source ~/.zshrc

Verify:

java -version
Linux Installation (Ubuntu/Debian)

Update packages:

sudo apt update

Install Java:

sudo apt install openjdk-21-jdk -y

Set environment variables in ~/.bashrc:

export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

Reload:

source ~/.bashrc

Verify:

java -version
2. Install Maven
Windows

Download Maven from:

Apache Maven

Extract to:

C:\Tools\apache-maven-3.9.11

Create environment variable:

MAVEN_HOME=C:\Tools\apache-maven-3.9.11

Add to Path:

%MAVEN_HOME%\bin

Verify:

mvn -version
macOS
brew install maven

Verify:

mvn -version
Linux
sudo apt install maven -y

Verify:

mvn -version
3. Create a Maven Project

Using terminal:

mvn archetype:generate \
-DgroupId=com.example.automation \
-DartifactId=cucumber-framework \
-DarchetypeArtifactId=maven-archetype-quickstart \
-DinteractiveMode=false

Move into the project:

cd cucumber-framework
4. Project Structure
cucumber-framework/
├── pom.xml
├── src
│   ├── test
│   │   ├── java
│   │   │   ├── runners
│   │   │   ├── stepdefinitions
│   │   │   └── hooks
│   │   └── resources
│   │       └── features
│   └── main
│       └── java

Create folders manually if they do not exist.

5. Configure pom.xml

Replace the contents of pom.xml with:

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example.automation</groupId>
    <artifactId>cucumber-framework</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>

        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

        <cucumber.version>7.20.1</cucumber.version>
        <selenium.version>4.33.0</selenium.version>
        <junit.version>5.12.2</junit.version>
    </properties>

    <dependencies>

        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit-platform-engine</artifactId>
            <version>${cucumber.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.platform</groupId>
            <artifactId>junit-platform-suite</artifactId>
            <version>1.12.2</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
        </dependency>

        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>6.1.0</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.14.0</version>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.3</version>
            </plugin>

        </plugins>
    </build>

</project>
6. Create a Cucumber Test Runner

Create:

src/test/java/runners/TestRunner.java
package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "stepdefinitions")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty,html:target/cucumber-report.html")
public class TestRunner {
}
7. Create a Feature File

Create:

src/test/resources/features/login.feature
Feature: Login

  Scenario: Successful login

    Given the user opens the application
    When the user enters valid credentials
    Then the user should be logged in
8. Create Step Definitions

Create:

src/test/java/stepdefinitions/LoginSteps.java
package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

    @Given("the user opens the application")
    public void openApplication() {
        System.out.println("Application opened");
    }

    @When("the user enters valid credentials")
    public void enterCredentials() {
        System.out.println("Credentials entered");
    }

    @Then("the user should be logged in")
    public void verifyLogin() {
        System.out.println("Login successful");
    }
}
9. Execute Tests

Run from terminal:

mvn clean test

Generated reports:

target/cucumber-report.html
10. Recommended IDE Plugins
IntelliJ IDEA

Install:

Cucumber for Java
Gherkin
Maven Integration
Visual Studio Code

Install extensions:

Extension Pack for Java
Cucumber (Gherkin) Full Support
Maven for Java
11. Useful Maven Commands
mvn clean
mvn test
mvn compile
mvn package
mvn dependency:tree
mvn verify
12. Troubleshooting
Java Version Mismatch

Check:

echo $JAVA_HOME
java -version
mvn -version

Windows:

echo %JAVA_HOME%
Maven Not Found

Ensure MAVEN_HOME/bin is added to your system PATH.

Cucumber Steps Not Recognized

Verify:

Feature files are under:
src/test/resources/features
Step definitions match the GLUE_PROPERTY_NAME.
Browser Driver Issues

Using WebDriverManager automatically downloads the correct browser driver:

WebDriverManager.chromedriver().setup();
