package org.seleniumx.util;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.seleniumx.annotations.ServerSettings;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SetupServer extends Start {
    private Platform platform;
    private String SERVER_URL;
    private String SERVER_PORT;
    private DesiredCapabilities capabilities;

    void setup(Method m) throws MalformedURLException {
        ServerSettings serverSettings = m.getAnnotation(ServerSettings.class);
        platform = serverSettings.PLATFORM();
        browser = serverSettings.BROWSER();
        BASE_URL = serverSettings.BASE_URL();
        VERSION = serverSettings.VERSION();
        SERVER_URL = serverSettings.SERVER_URL();
        SERVER_PORT = serverSettings.SERVER_PORT();
        implicit_wait = serverSettings.IMPLICIT_WAIT();
        log.info("[{}]", "OS=" + platform + ",BROWSER=" + browser + ",SERVER_URL=" + SERVER_URL + ",SERVER_PORT=" + SERVER_PORT + ",\n" +
                "VERSION=" + VERSION + ",IMPLICIT_WAIT=" + implicit_wait + ",BASE_URL=" + BASE_URL);

        if (browser.equals(Set.BROWSER.CHROME)) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--remote-debugin-port=9222", "--screen-size=1280x720");
            capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        } else if (browser.equals(Set.BROWSER.FIREFOX)) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--remote-debugin-port=9222", "--screen-size=1280x720");
            capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
        }

        capabilities.setCapability("version", VERSION);
        capabilities.setPlatform(platform);

        driver = new RemoteWebDriver(new URL(SERVER_URL + ":" + SERVER_PORT + "/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(implicit_wait, TimeUnit.MILLISECONDS);
        Dimension expectedWindowSize = new Dimension(1360, 1020);
        driver.manage().window().setSize(expectedWindowSize);
        driver.get(BASE_URL);
    }
}
