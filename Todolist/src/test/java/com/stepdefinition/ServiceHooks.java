package com.stepdefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Reporter;

import java.io.File;


public class ServiceHooks {
    public static WebDriver driver;
    @Before
    public void initializeTest(){

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void embedScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            String screenshotName = scenario.getName().replaceAll(" " , " ");
            try {
                TakesScreenshot ts = (TakesScreenshot) driver;
                File sourcePath = ts.getScreenshotAs(OutputType.FILE);
                File destinationPath = new File(
                        System.getProperty("user.dir") + "\\target\\cucumber-reports\\screenshots\\" + screenshotName + ".png");
                FileUtils.copyFile(sourcePath, destinationPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        driver.close();
    }
}