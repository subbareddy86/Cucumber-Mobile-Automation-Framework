package utilities;

//This class to create Global constant variables
public class GlobalConstants {
	public static final String rootProjectPath = System.getProperty("user.dir");
	public static String configurationFilePath = rootProjectPath+"/src/main/resources/config/config.properties";
	public static String automationType;
	public static String browserName;	
	public static String mobileAutomationType;
	public static String mobileBrowserName;
	public static String deviceName;
	public static String mobileOS;
	public static String automationName;
	public static String appPackage;
	public static String appActivity;
	public static String appiumUrl;
	public static String iplatformVersion;
	public static String ibundleId;
	public static String iautomationName;
	public static String iudid;
	public static String ideviceName;
	public static String unicodeKeyboard;
	public static String resetKeyboard;
	
	public static final boolean headlessBrowser = false;
}
