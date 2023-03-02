package com.draftdev.saucelabjenkinsexample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
//import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Platform;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.remote.CapabilityType;

/**
 * Demo tests with Selenium.
 */
public class SimpleSauceTest {

    public RemoteWebDriver driver;

    /**
     * A Test Watcher is needed to be able to get the results of a Test so that
     * it can be sent to Sauce Labs. Note that the name is never actually used
     */
    @RegisterExtension
    public SauceTestWatcher watcher = new SauceTestWatcher();

    @BeforeEach
    public void setup(TestInfo testInfo) throws MalformedURLException {
        
        
        /*ChromeOptions options = new ChromeOptions();
        options.setPlatformName("Windows 10");
        options.setBrowserVersion("latest");*/
        DesiredCapabilities desiredCap = new DesiredCapabilities();
        desiredCap.setBrowserName(System.getenv("SELENIUM_BROWSER"));        
        desiredCap.setCapability("Platform","WINDOWS"); 
                                 
                                 //System.getenv("SELENIUM_PLATFORM"));
       
        System.out.println("the browser name is:" + desiredCap.getBrowserName());
            
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("name", testInfo.getDisplayName());

        desiredCap.setCapability("sauce:options", sauceOptions);
        URL url = new URL("https://oauth-2da9thpwr-65cdf:456b155a-3c54-4341-9bd3-f128dbb12256@ondemand.eu-central-1.saucelabs.com:443/wd/hub");

        driver = new RemoteWebDriver(url, desiredCap);
    }

    @DisplayName("Selenium Navigation Test")
    @Test
    public void navigateAndClose() {
        driver.navigate().to("https://www.saucedemo.com");
        Assertions.assertEquals("Swag Las", driver.getTitle());
      
    }
    @Test
    public void navigateAndClose2() {
        driver.navigate().to("https://www.saucedemo.com");
  
        Assertions.assertEquals("Swag Labs", driver.getTitle());
    }

    /**
     * Custom TestWatcher for Sauce Labs projects.
     */
    public class SauceTestWatcher implements TestWatcher {

        @Override
        public void testSuccessful(ExtensionContext context) {
            driver.executeScript("sauce:job-result=passed");
            driver.quit();
        }

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            driver.executeScript("sauce:job-result=failed");
            driver.quit();
        }
    }
}
