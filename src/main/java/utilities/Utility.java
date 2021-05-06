package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

//This class is main utility class for Getting Elements and perform actions on target elements
public class Utility {
	private static String configurationFilePath = GlobalConstants.configurationFilePath;
	private static Properties prop = new Properties();
	private static WebDriver driver;
	private WebDriverWait wait;
	private static Utility util;
	private int timeout = 60;

	// Utility constructor method to load config properties to global constants,
	// initialize driver, read input data from excel sheets
	private Utility() {
		loadGlobalConstants();
		loadDriver();
		System.out.println("Succesfully driver loaded..!");
		loadWait();
		wait = new WebDriverWait(driver, timeout);
	}

	// This method is singleton method to provide single access globally for it's members
	public static Utility getInstance() {
		if (util == null) {
			synchronized (Utility.class) {
				if (util == null)
					util = new Utility();
			}
		}
		return util;
	}

	// This method is to load config properties files to GlobalConstants
	private static void loadGlobalConstants() {
		try {
			prop.load(new FileInputStream(configurationFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GlobalConstants.automationType = prop.getProperty("automationType");
		GlobalConstants.browserName = prop.getProperty("browserName");	
		GlobalConstants.mobileAutomationType = prop.getProperty("mobileAutomationType");
		GlobalConstants.mobileBrowserName = prop.getProperty("mobileBrowserName");
		GlobalConstants.iautomationName = prop.getProperty("executionCycle");
		if ("nativeApp".equalsIgnoreCase(GlobalConstants.mobileAutomationType)) {
			GlobalConstants.mobileOS = prop.getProperty("mobileOS");
			GlobalConstants.appiumUrl = prop.getProperty("appiumUrl");
			GlobalConstants.unicodeKeyboard = prop.getProperty("unicodeKeyboard");
			GlobalConstants.resetKeyboard = prop.getProperty("resetKeyboard");
			if ("Android".equalsIgnoreCase(GlobalConstants.mobileOS)) {
				GlobalConstants.automationName = prop.getProperty("automationName");
				GlobalConstants.deviceName = prop.getProperty("deviceName");
				GlobalConstants.appPackage = prop.getProperty("appPackage");
				GlobalConstants.appActivity = prop.getProperty("appActivity");
			} else {
				GlobalConstants.automationName = prop.getProperty("iautomationName");
				GlobalConstants.ideviceName = prop.getProperty("ideviceName");
				GlobalConstants.iplatformVersion = prop.getProperty("iplatformVersion");
				GlobalConstants.ibundleId = prop.getProperty("ibundleId");
				GlobalConstants.iudid = prop.getProperty("iudid");
			}
		}
	}

	// This method is to load driver based on automation type as web/native apps and
	// browsers
	@SuppressWarnings("rawtypes")
	private static WebDriver loadDriver() {
		if ("browser".equals(GlobalConstants.automationType)) {
			if ("chrome".equalsIgnoreCase(GlobalConstants.browserName)) {
				System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
				if (GlobalConstants.headlessBrowser) {
					ChromeOptions options = new ChromeOptions();
					options.addArguments("headless");
					options.addArguments("window-size=1200x600");
					driver = new ChromeDriver(options);
				} else {
					driver = new ChromeDriver();
				}
			} else if ("firefox".equalsIgnoreCase(GlobalConstants.browserName)) {
				System.setProperty("webdriver.gecko.driver", "resources/geckodriver.exe");
				System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
				driver = new FirefoxDriver();
			} else if ("IE".equalsIgnoreCase(GlobalConstants.browserName)) {
				System.setProperty("webdriver.ie.driver", "resources/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}
		} else {
			if ("nativeApp".equalsIgnoreCase(GlobalConstants.mobileAutomationType)) {
				if ("Android".equalsIgnoreCase(GlobalConstants.mobileOS)) {
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability("deviceName", GlobalConstants.deviceName);
					capabilities.setCapability("platformName", GlobalConstants.mobileOS);
					capabilities.setCapability("appPackage", GlobalConstants.appPackage);
					capabilities.setCapability("appActivity", GlobalConstants.appActivity);
//					capabilities.setCapability("unicodeKeyboard", GlobalConstants.unicodeKeyboard);
//					capabilities.setCapability("resetKeyboard", GlobalConstants.resetKeyboard);
					capabilities.setCapability("automationName", GlobalConstants.automationName);
					capabilities.setCapability("noReset", true);
					try {
						driver = new AndroidDriver(new URL(GlobalConstants.appiumUrl), capabilities);
						System.out.println(capabilities.getCapability("deviceModel"));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				} else {
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability("platformName", GlobalConstants.mobileOS);
					capabilities.setCapability("deviceName", GlobalConstants.ideviceName);
					capabilities.setCapability("bundleId", GlobalConstants.ibundleId);
					capabilities.setCapability("udid", GlobalConstants.iudid);
					capabilities.setCapability("unicodeKeyboard", GlobalConstants.unicodeKeyboard);
					capabilities.setCapability("resetKeyboard", GlobalConstants.resetKeyboard);
					capabilities.setCapability("automationName", GlobalConstants.automationName);
					capabilities.setCapability("noReset", true);
					try {
						driver = new IOSDriver(new URL(GlobalConstants.appiumUrl), capabilities);
						System.out.println(capabilities.getCapability("deviceModel"));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			} else {
				if ("CHROME".equalsIgnoreCase(GlobalConstants.mobileBrowserName)) {
					System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
					driver = new ChromeDriver();
				} else {
					// safari
				}
			}
		}
		System.out.println("Succesfully driver loaded..!");
		return driver;
	}

	// Getters Setters for class variables
	public WebDriver getDriver() {
		return driver;
	}

	public void closeDriver() {
		if (driver != null)
			driver.quit();
	}

	private void loadWait() {
		wait = new WebDriverWait(driver, 40);
	}

	public WebDriverWait getWait() {
		return wait;
	}

	public String getCurrentTimeStamp() {
		Date today = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String execDate = dateFormatter.format(today);
		return execDate;
	}
}
