package utilities;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
//import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.offset.PointOption;

public class BasePage {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected WebDriverWait minWait;
	protected WebDriverWait maxWait;
	protected Actions actions;
	protected JavascriptExecutor executor;
	protected boolean acceptNextAlert = true;
	protected Dimension size;
	private double endPercentage = 50;

	public static boolean isMobileOS_IOS = false;
	public static boolean isMobileOS_Android = false;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		actions = new Actions(driver);
		executor = (JavascriptExecutor) driver;
		wait = new WebDriverWait(this.driver, 15);
		minWait = new WebDriverWait(this.driver, 10);
		maxWait = new WebDriverWait(this.driver, 100);
		size = driver.manage().window().getSize();
		if (GlobalConstants.mobileOS.equalsIgnoreCase("IOS"))
			isMobileOS_IOS = true;
		if (GlobalConstants.mobileOS.equalsIgnoreCase("Android"))
			isMobileOS_Android = true;
	}

	public void waitForInvisibilityOfLoader() {
		if (GlobalConstants.mobileOS.equalsIgnoreCase("Android")) {
			try {
				maxWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//AndroidLoading")));
				maxWait.until(ExpectedConditions
						.invisibilityOfElementLocated(By.xpath("//android.app.Dialog//android.widget.Image")));
			} catch (WebDriverException e) {
				System.out.println("***************** " + e.getMessage() + " *****************");
			}
		} else {
			try {
				maxWait.until(ExpectedConditions.invisibilityOfElementLocated(
						By.xpath("//XCUIElementTypeOther[@name='web dialog']/XCUIElementTypeOther")));
			} catch (WebDriverException e) {
				System.out.println("***************** " + e.getMessage() + " *****************");
			}
		}
	}

	public boolean isElementInDimension(By by) {
		boolean flag = true;
		MobileElement startElement = (MobileElement) waitForElementToBeClickable(by);
//		int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
		int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);
//		if(5 > startY || startY > size.height * 0.99 || 3 > startX || startX > size.width * 0.99 ) {
		if (5 > startY || startY > size.height * 0.99) {
			flag = false;
		}
		return flag;
	}

	public boolean isElementInDimension(MobileElement element) {
		boolean flag = true;
//		int startX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
		int startY = element.getLocation().getY() + (element.getSize().getHeight() / 2);
//		if(5 > startY || startY > size.height * 0.99 || 3 > startX || startX > size.width * 0.99 ) {
		if (5 > startY || startY > size.height * 0.90) {
			flag = false;
		}
		return flag;
	}

	public MobileElement getElement(By by) {
		MobileElement element = null;
		waitForInvisibilityOfLoader();
		try {
			element = driver.findElement(by);
		} catch (Exception e) {
			try {
				element = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(by));
			} catch (Exception e1) {
				try {
					element = driver.findElement(by);
				} catch (NoSuchElementException | TimeoutException e2) {
					e2.printStackTrace();
				}
			}
		}
		return element;
	}

	@SuppressWarnings("rawtypes")
	public MobileElement verticalSwipe(MobileElement element) {
		try {
			int startY = (element.getLocation().getY() + (element.getSize().getHeight() / 2));
//			System.out.println("startY:" + startY);
			int flag = 1;
			while (flag <= 2 && (5 > startY || startY > size.height * 0.90)) {
//				System.out.println(size.height * 0.90);
				flag++;
				int endY = 0, startX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
				int defaultDownY = 5, defaultUpY = size.height;
				if (5 > startY) {
					endY = defaultDownY + (int) ((size.height * endPercentage) / 100);
					defaultDownY = endY;
				} else if (startY > size.height * 0.90) {
					endY = defaultUpY - (int) ((size.height * endPercentage) / 100);
					defaultUpY = endY;
				}
				if (startY < size.height * 0.10)
					startY = (int) (size.height * 0.10);
				if (startY > size.height * 0.80)
					startY = (int) (size.height * 0.80);
//				System.out.println("startX:" + startX + "startY:" + startY + "endY:" + endY);
				new TouchAction((MobileDriver) driver).press(point(startX, startY))
						.waitAction(waitOptions(ofMillis(1000))).moveTo(point(startX, endY)).release().perform();
				startX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
				startY = (element.getLocation().getY() + (element.getSize().getHeight() / 2));
//				System.out.println("startX:" + startX + "startY:" + startY + "endY:" + endY);
				sleepMillis(300);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element;
	}

	@SuppressWarnings("rawtypes")
	public void verticalSwipe(int percentage) {
//		System.out.println("inside verticalSwipe");
		try {
			int startX = (int) (size.width * 0.50);
			int startY = (int) (size.height * 0.80);
			int endY = (int) (size.height * (percentage / 100));
			new TouchAction((MobileDriver) driver).press(point(startX, startY)).waitAction(waitOptions(ofMillis(1000)))
					.moveTo(point(startX, endY)).release().perform();
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void verticalSwipeT2B(int percentage) {
//		System.out.println("inside verticalSwipe");
		try {
			int startX = (int) (size.width * 0.50);
			int startY = (int) (size.height * 0.10);
			int endY = (int) (size.height * (percentage / 100));
			new TouchAction((MobileDriver) driver).press(point(startX, startY)).waitAction(waitOptions(ofMillis(1000)))
					.moveTo(point(startX, endY)).release().perform();
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Will return By Object if visibility of Element Location found
	 * 
	 * @param by input as element
	 * @return WebElement Object
	 */
	public MobileElement waitForElementVisibility(By by) {
		waitForInvisibilityOfLoader();
		MobileElement element = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		return element;
	}

	/**
	 * Will wait until invisibility of Element
	 * 
	 * @param by input as element
	 * @return WebElement Object
	 */
	public void waitForElementInvisibility(By by) {
		try {
			maxWait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		} catch (Exception e) {
			System.out.println(" Excepton waitForElementInvisibility");
//			Assert.assertTrue(false);
		}
	}

	/**
	 * Will return By Object if presence of Element Location found
	 * 
	 * @param by input as element
	 * @return WebElement Object
	 */
	public MobileElement waitForElementPresence(By by) {
		waitForInvisibilityOfLoader();
		MobileElement element = (MobileElement) wait.until(ExpectedConditions.presenceOfElementLocated(by));
		return element;
	}

	/**
	 * Will return By Object if presence of Element Location found
	 * 
	 * @param by      input as element
	 * @param timeout long type
	 * @return WebElement Object
	 */
	public MobileElement waitForElementPresence(By by, long timeout) {
		waitForInvisibilityOfLoader();
		MobileElement element = (MobileElement) new WebDriverWait(this.driver, timeout)
				.until(ExpectedConditions.presenceOfElementLocated(by));
		return element;
	}

	/**
	 * Will return list of By Object if visibility of Element Location found
	 * 
	 * @param by input as element
	 * @return List<WebElement> Object
	 */
	public List<WebElement> waitForAllElements(By by) {
		waitForInvisibilityOfLoader();
		List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
		return elements;
	}

	/**
	 * Will return By Object if presence of Element Visibility
	 * 
	 * @param by input as element
	 * @return WebElement Object
	 */
	public MobileElement waitForElementToBeClickable(By by) {
		waitForInvisibilityOfLoader();
		return verticalSwipe(getElement(by));
	}

	public MobileElement waitForCalenderToBeClickable(By by) { // only for ios calender congtrol need to enhance later
		waitForInvisibilityOfLoader();
		return (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(waitForElementVisibility(by)));
	}

	/**
	 * Wait until given title present
	 * 
	 * @param by input as element
	 * @return WebElement Object
	 */
	public void waitForTitle(String title) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.titleContains(title));
	}

	/**
	 * Will return By Object, Xpath locater with dynamic text
	 * 
	 * @param xpathLocater input as String type xapth locater ex:
	 *                     //span[text()='%s']
	 * @param dynamicTxt   input as String type dynamic text ex: "����� ��������" or
	 *                     "Account Management"
	 * 
	 * @return By object
	 */
	public By getByXapth(String xpathLocater, String dynamicTxt) {
		System.out.println("getByXapth:: " + String.format(xpathLocater, dynamicTxt));
		return By.xpath(String.format(xpathLocater, dynamicTxt));
	}

	/**
	 * Will return By Object, Xpath locater with dynamic text
	 * 
	 * @param xpathLocater input as String type xapth locater ex:
	 *                     //span[text()='%s']
	 * @param dynamicTxt   input as String type dynamic text ex: "����� ��������" or
	 *                     "Account Management"
	 * 
	 * @return By object
	 */
	public By byXapth(String xpathLocater) {
		return By.xpath(xpathLocater);
	}

	/**
	 * Will return By Object, Xpath locater with dynamic text
	 * 
	 * @param xpathLocater input as String type xapth locater ex:
	 *                     //span[text()='%s']
	 * @param dynamicNo    input as int type dynamic number ex: 100 or 2051
	 * 
	 * @return By object
	 */
	public By getByXapth(String xpathLocater, int dynamicNo) {
		return By.xpath(String.format(xpathLocater, dynamicNo));
	}

	/**
	 * Will return By Object, Xpath locater with dynamic text
	 * 
	 * @param xpathLocater input as String type xapth locater ex:
	 *                     //span[text()='%s']
	 * @param dynamicNo    input as int type dynamic number ex: 100 or 2051
	 * 
	 * @return By object
	 */
	public By getByXapth(String xpathLocater, double dynamicNo) {
		return By.xpath(String.format(xpathLocater, dynamicNo));
	}

	/**
	 * Will return By Object, Xpath locater with dynamic text
	 * 
	 * @param xpathLocater input as String type xapth locater ex: span[name='%s']
	 * @param dynamicTxt   input as String type dynamic text ex: "����� ��������" or
	 *                     "Account Management"
	 * 
	 * @return By object
	 */
	public By getByCSS(String cssLocater, String dynamicTxt) {
		return By.cssSelector(String.format(cssLocater, dynamicTxt));
	}

	/**
	 * Will launch the URL
	 * 
	 * @param url as String type input
	 * @return void
	 */
	public void launchApp(String url) {
		driver.get(url);
	}

	/**
	 * Will scroll into view based on By Object with help of Java script.
	 * 
	 * @param by input as element
	 * @return void
	 */
	public void scrollIntoView(By by) {
		MobileElement element = null;
		try {
			element = waitForElementPresence(by, 2);
		} catch (Exception e) {
			element = driver.findElement(by);
		}
		executor.executeScript("arguments[0].scrollIntoView();", element);
	}

	/**
	 * Will return element attribute value based on By Object.
	 * 
	 * @param by            input as element
	 * @param attributeName as String type attribute name
	 * @return boolean type if PopUp visible return "true" else "false"
	 */
	public boolean isPopUpAlertVisible(By by) {
		try {
			if (waitForElementVisibility(by).isDisplayed())
				return true;
			else
				return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Will accept alert.
	 * 
	 * @param flag if flag is true switch to default frame else not.
	 * @return void
	 */
	public void acceptPopUpAlert(boolean flag) {
		try {
//			sleep(5);
			switchToDefault();
			minWait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			if (alert != null) {
				alert.accept();
				System.out.println(alert.getText());
			}

		} catch (UnhandledAlertException | TimeoutException e) {
			try {
				Alert alert = driver.switchTo().alert();
				if (alert != null) {
					alert.accept();
					System.out.println(alert.getText());
				}
			} catch (NoAlertPresentException insideEx) {
				System.out.println("Alert not present");
			}
		} catch (NoAlertPresentException e) {
			System.out.println("Alert not present");
		} finally {
			if (flag)
				switchToDefault();
		}
	}

	/**
	 * Will accept dismiss.
	 * 
	 * @param flag if flag is true switch to default frame else not.
	 * @return void
	 */
	public void dismissPopUpAlert(boolean defaultFlag) {
		try {
//			sleep(5);
			switchToDefault();
			minWait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			if (alert != null) {
				alert.dismiss();
				System.out.println(alert.getText());
			}

		} catch (UnhandledAlertException | TimeoutException e) {
			try {
				Alert alert = driver.switchTo().alert();
				if (alert != null) {
					alert.dismiss();
					System.out.println(alert.getText());
				}
			} catch (NoAlertPresentException insideEx) {
				System.out.println("Alert not present");
			}
		} catch (NoAlertPresentException e) {
			System.out.println("Alert not present");
		} finally {
			if (defaultFlag)
				switchToDefault();
		}
	}

	/**
	 * Suspend the current Thread's execution for the specified amount of time.
	 * 
	 * @param timeout the number of seconds to sleep, or 0 for forever
	 * @return void
	 */
	public void sleep(int timeout) {
		try {
			Thread.sleep(timeout * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sleepMillis(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tabKey() {
		// waitForElement(by).sendKeys(Keys.TAB);
		actions.sendKeys(Keys.TAB).build().perform();
	}
	public void ScrollKey(By by) {
		MobileElement element = waitForElementToBeClickable(by);
		((TouchActions) actions).scroll(element, 10, 100);
		actions.perform();
		actions.click();
	}

	public void switchToDefault() {
		driver.switchTo().defaultContent();
	}
	
	public void switchToFrame(By by, boolean defaultFlag) {
		if (defaultFlag)
			switchToDefault();
		MobileElement element = waitForElementToBeClickable(by);
		driver.switchTo().frame(element);
	}

	public void switchToparentFrame() {
		driver.switchTo().parentFrame();
	}

	public String getSelectedOption(By by) {
		MobileElement element = waitForElementToBeClickable(by);
		Select select = new Select(element);
		return select.getFirstSelectedOption().getText();
	}

	public void mouseHover(By by) {
		MobileElement element = waitForElementToBeClickable(by);
		actions.moveToElement(element).build().perform();
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	public String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	public String getCurrentDate(String format) {
		if ("default".equals(format))
			format = "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	public String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(new Date());
	}

	/**
	 * Will Today date in "dd/MM" format
	 * 
	 * @author K VENKAT SUBBAREDDY
	 * @return String return today date in "dd/MM" format
	 */
	public String getTodayDate() {
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM");
		return dateFormat.format(today);
	}

	/**
	 * Will Tomorrow date in "dd/MM" format
	 * 
	 * @author K VENKAT SUBBAREDDY
	 * @return String return Tomorrow date in "dd/MM" format
	 */
	@SuppressWarnings("deprecation")
	public String getTomorrowDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		if (tomorrow.getDay() >= 5) {
			Calendar nextCalendar = Calendar.getInstance();
			switch (tomorrow.getDay()) {
			case 5:
				nextCalendar.add(Calendar.DAY_OF_YEAR, 3);
				break;
			case 6:
				nextCalendar.add(Calendar.DAY_OF_YEAR, 2);
				break;
			case 7:
				nextCalendar.add(Calendar.DAY_OF_YEAR, 1);
				break;
			default:
				break;
			}
			tomorrow = nextCalendar.getTime();
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM");
		return dateFormat.format(tomorrow);
	}

	// -----------------------------------------------------------------------------------------------------------------------
	/**
	 * Will click the element based on By Object
	 * 
	 * @param by input as element
	 * @return void
	 */
	public void click(By by, String elementName) {
		try {
			waitForElementToBeClickable(by).click();
			waitForInvisibilityOfLoader();
			System.out.println(elementName + " Successfully clicked!!");
		} catch (Exception e) {
			System.out.println("Unable to click " + elementName);
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will click the element based on By Object
	 * 
	 * @param by input as element
	 * @return void
	 */
	public void sendKeysbackspace(By by, Keys backSpace, String elementName) {
		driver.findElement(by).sendKeys(Keys.BACK_SPACE);
	}

	/**
	 * Will click the element based on By Object
	 * 
	 * @param by input as element
	 * @return void
	 */
	public void waitAndClick(By by, String elementName) {
		try {
			waitForInvisibilityOfLoader();
			waitForElementToBeClickable(by);
			waitForElementToBeClickable(by).click();
			waitForInvisibilityOfLoader();
		} catch (Exception e) {
			System.out.println("Unable to click " + elementName);
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will click the element based on By Object
	 * 
	 * @param by input as element
	 * @return void
	 */
	public void clickScroll(By by, String elementName) {
		try {
			scrollIntoView(by);
			waitForElementToBeClickable(by).click();
		} catch (Exception e) {
			System.out.println("Unable to click " + elementName);
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will click the element with out scroll based on By Object
	 * 
	 * @param by input as element
	 * @return void
	 */
	public void clickWithoutScroll(By by, String elementName) {
		try {
			waitForInvisibilityOfLoader();
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			MobileElement element = (MobileElement) wait
					.until(ExpectedConditions.elementToBeClickable(waitForElementVisibility(by)));
			wait.until(ExpectedConditions.visibilityOf(element));
			element.click();
		} catch (Exception e) {
			System.out.println("Unable to click " + elementName);
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will click the element based on By Object with help of Action class.
	 * 
	 * @param by input as element
	 * @return void
	 */
	public void clickAction(By by, String elementName) {
		try {
			MobileElement element = waitForElementToBeClickable(by);
			actions.click(element);
		} catch (Exception e) {
			System.out.println("Unable to click " + elementName);
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will click the element based on By Object with help of Java script.
	 * 
	 * @param by input as element
	 * @return void
	 */
	public void clickJS(By by, String elementName) {
		try {
			try {
//				sleep(1);
				MobileElement element = waitForElementPresence(by);
				executor.executeScript("arguments[0].click();", element);
			} catch (TimeoutException e) {
				System.out.println("From clickExecutor Catch Block");
				executor.executeScript("arguments[0].click();", driver.findElement(by));
			}
		} catch (Exception e) {
			System.out.println("Unable to click " + elementName);
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will insert data to input/textBox based on By Object.
	 * 
	 * @param by   input as element
	 * @param data to insert to input/textBox
	 * @return void
	 */
	public void sendKeys(By by, String inputData, String elementName) {
		try {
			waitForElementToBeClickable(by).click();
			waitForElementToBeClickable(by).sendKeys(inputData);
			System.out.println(elementName + " Successfully Entered: " + inputData);
		} catch (Exception e) {
			System.out.println(elementName + " fail to Enter text.");
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will insert data to input/textBox based on By Object.
	 * 
	 * @param by   input as element
	 * @param data to insert to input/textBox
	 * @return void
	 */
	public void clear(By by, String elementName) {
		try {
			MobileElement element = waitForElementToBeClickable(by);
			System.out.println(element.getText());
			element.clear();
			System.out.println(elementName + " Successfully cleared");
		} catch (Exception e) {
			System.out.println(elementName + " fail to clear text.");
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will insert data to input/textBox based on By Object.
	 * 
	 * @param by   input as element
	 * @param data to insert to input/textBox
	 * @return void
	 */
	public void sendKeys(By by, double inputData, String elementName) {
		try {
			// clear existing data
			waitForElementToBeClickable(by).clear();
			waitForElementToBeClickable(by).sendKeys(Double.toString(inputData));
			System.out.println("Enter " + elementName + " with " + inputData);
			System.out.println(elementName + " Successfully Entered: " + inputData);
		} catch (Exception e) {
			System.out.println(elementName + " fail to Enter text.");
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will insert data to input/textBox based on By Object.
	 * 
	 * @param by   input as element
	 * @param data to insert to input/textBox
	 * @return void
	 */
	public void sendKeys(MobileElement element, double inputData, String elementName) {
		try {
			String amount = Double.toString(inputData);
			System.out.println("******: ");
			System.out.print(amount);
			element.sendKeys("10");
			System.out.println("Enter " + elementName + " with " + inputData);
			System.out.println(elementName + " Successfully Entered: " + inputData);
		} catch (Exception e) {
			System.out.println(elementName + " fail to Enter text.");
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will insert data to input/textBox based on By Object with help of Java
	 * script.
	 * 
	 * @param by   input as element
	 * @param data to insert to input/textBox
	 * @return void
	 */
	public void sendKeysExecutor(By by, String inputData, String elementName) {
		try {
			MobileElement element = waitForElementToBeClickable(by);
			executor.executeScript("arguments[0].setAttribute('value', arguments[1])", element, inputData);
			System.out.println(elementName + " Successfully Entered: " + inputData);
		} catch (Exception e) {
			System.out.println(elementName + " fail to Enter text.");
			Assert.assertTrue(false);
		}
	}

	/**
	 * Will return element text based on By Object.
	 * 
	 * @param by input as element
	 * @return String type element text
	 */
	public String getText(By by, String elementName) {
		String text = null;
		try {
			waitForInvisibilityOfLoader();
			text = wait.until(ExpectedConditions.visibilityOfElementLocated(by)).getText();
			System.out.println("text is " + text);
			if (text.length() >= 20)
				text = text.substring(0, 20);
		} catch (Exception e) {
			System.out.println("Unable get text from " + elementName);
			Assert.assertTrue(false);
		}
		return text;
	}

	/**
	 * Will return element attribute value based on By Object.
	 * 
	 * @param by            input as element
	 * @param attributeName as String type attribute name
	 * @return String type element text
	 */
	public String getAttribute(By by, String attributeName, String elementName) {
		String text = null;
		try {
			waitForInvisibilityOfLoader();
			text = waitForElementVisibility(by).getAttribute(attributeName);
		} catch (Exception e) {
			System.out.println("Unable get attribute from " + elementName);
			Assert.assertTrue(false);
		}
		return text;
	}

	public boolean isElementPresent(By by, String elementName) {
		try {
			 verticalSwipe(getElement(by));
			new WebDriverWait(driver, 2).until(ExpectedConditions.elementToBeClickable(by));
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException | TimeoutException | NullPointerException e) {
			System.out.println(elementName + " Element not Present !!");
			return false;
		}
	}

	public boolean isElementSelected(By by, String elementName) {
		boolean flag = false;
		try {
			flag = waitForElementVisibility(by).isSelected();
		} catch (Exception e) {
			System.out.println("Given element '" + elementName + "' is selected " + flag);
			Assert.assertTrue(false);
		}
		return flag;
	}

	public boolean isElementPresent(By by, String elementName, int timeout) {
		try {
			waitForInvisibilityOfLoader();
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(by));
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException | TimeoutException e) {
			System.out.println(elementName + " Element not Present !!");
			return false;
		}
	}

	public void selectByValue(By by, String value, String elementName) {
		try {
			MobileElement element = waitForElementToBeClickable(by);
			Select select = new Select(element);
			select.selectByValue(value);
		} catch (NoSuchElementException | TimeoutException e) {
			System.out.println(elementName + " Unable to select by value " + value);
			Assert.assertTrue(false);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void swipeOnDevice(String distance) {
		int endY = 6000;
		Dimension size = driver.manage().window().getSize();
		int startX = size.getWidth() / 2;
		int startY = size.getHeight() / 2;
		int endX = size.getWidth() / 2;
		if ("long".equalsIgnoreCase(distance)) {
			endY = 200;
		} else if ("medium".equalsIgnoreCase(distance)) {
			endY = (int) (startY * 1 * 0.50);
		} else if ("short".equalsIgnoreCase(distance)) {
			endY = (int) (startY * 1 * 0.80);
		}
		TouchAction action = new TouchAction((MobileDriver) driver);
		action.press(PointOption.point(startX, startY)).moveTo(PointOption.point(endX, endY)).release().perform();
		System.out.println("==SCREEN SWIPE Started at x: " + distance + "--swipeforce-- :" + startX + " and Y:" + startY
				+ "  End at x: " + endX + " End Y:" + endY);
	}

	@SuppressWarnings("rawtypes")
	public void horizontalSwipeByPercentage(MobileElement startElement, double endPercentage) {
		Dimension size = driver.manage().window().getSize();
		int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
		int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);
		int endX = (int) ((size.height * endPercentage) / 100);
		new TouchAction((MobileDriver) driver).press(point(startX, startY)).waitAction(waitOptions(ofMillis(1000)))
				.moveTo(point(endX, startY)).release().perform();
	}

	// Vertical Swipe by percentages
	@SuppressWarnings("rawtypes")
	public void verticalSwipeByPercentages(By startBy, double endPercentage) {
		Dimension size = driver.manage().window().getSize();
		MobileElement startElement = (MobileElement) waitForElementToBeClickable(startBy);
		int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
		int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);

		while (5 > startY || startY > size.height * 0.99) {
			int endY = 0;
			int defaultDownY = 5, defaultUpY = size.height;
			if (5 > startY) {
				endY = defaultDownY + (int) ((size.height * endPercentage) / 100);
				defaultDownY = endY;
			} else if (startY > size.height * 0.99) {
				endY = defaultUpY - (int) ((size.height * endPercentage) / 100);
				defaultUpY = endY;
			}
			new TouchAction((MobileDriver) driver).press(point(startX, startY)).waitAction(waitOptions(ofMillis(1000)))
					.moveTo(point(startX, endY)).release().perform();

			MobileElement startElement1 = (MobileElement) wait
					.until(ExpectedConditions.elementToBeClickable(waitForElementVisibility(startBy)));
			startX = startElement.getLocation().getX() + (startElement1.getSize().getWidth() / 2);
			startY = startElement.getLocation().getY() + (startElement1.getSize().getHeight() / 2);
		}
	}

	@SuppressWarnings("rawtypes")
	public void swipeByElements(MobileElement startElement, MobileElement endElement) {
		Dimension size = driver.manage().window().getSize();
		System.out.println("Max height:: " + size.height);
		System.out.println("Max width:: " + size.width);
		int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
		int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);

		int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
		int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);

		System.out.println("startX:: " + startX);
		System.out.println("startY:: " + startY);
		System.out.println("endX:: " + endX);
		System.out.println("endY:: " + endY);

		new TouchAction((MobileDriver) driver).press(point(startX, startY)).waitAction(waitOptions(ofMillis(500)))
				.moveTo(point(endX, endY)).release().perform();
	}

	@SuppressWarnings("rawtypes")
	public void swipeByPoints(int startX, int startY, int endX, int endY) {
		new TouchAction((MobileDriver) driver).press(point(startX, startY)).waitAction(waitOptions(ofMillis(500)))
				.moveTo(point(endX, endY)).release().perform();
	}

	public int[] getCalenderLocation(By endBy) {
		MobileElement endElement = (MobileElement) waitForCalenderToBeClickable(endBy); // for ios
		int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
		int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);
		int[] xy = { endX, endY };
		return xy;
	}

	/*public void reportFailedTestcases(String testcasesName, Throwable e) {
		Reports.logFail(testcasesName + ":: FAILED");
		Assert.assertEquals(true, false);
	}*/


	public void dateSwipe(int start, int end, String xpath) {
		System.out.println("the start and end date in swipe" + start + ": " + end + "  :" + xpath);
		int swipePixel = 30;
		int[] endXY;
		System.out.println(getCalenderLocation(getByXapth(xpath, get(end))));
		 endXY = getCalenderLocation(getByXapth(xpath, get(end)));
		while (start != end) {
			if (start > end) { // swipe up
				int flag = end + 1;
				if (start > flag) {
					System.out.println("start: " + start + " flag: " + flag + " end: " + end);
					if (end < 32) {
						swipeByPoints(endXY[0], endXY[1] + swipePixel, endXY[0], endXY[1]); // actual
					} else {
						swipeByPoints(endXY[0], endXY[1] - swipePixel, endXY[0], endXY[1]);
					}

					end = flag;
				} else {
					System.out.println("start: " + start + " end: " + end);
					if (end < 32) {
						swipeByPoints(endXY[0], endXY[1] + swipePixel, endXY[0], endXY[1]); // actual
					} else {
						swipeByPoints(endXY[0], endXY[1] - swipePixel, endXY[0], endXY[1]);
					}
					end = start;
				}
			} else { // swipe down
				int flag = end - 1; // 21
				if (start < flag) { // 10<21
					System.out.println("start: " + start + " flag: " + flag + " end: " + end);
					if (end < 32) {
						swipeByPoints(endXY[0], endXY[1] - swipePixel, endXY[0], endXY[1]); // actual
					} else {
						swipeByPoints(endXY[0], endXY[1] + swipePixel, endXY[0], endXY[1]);
					}
					end = flag;
				} else {
					System.out.println("start: " + start + " end: " + end);
					if (end < 32) {
						swipeByPoints(endXY[0], endXY[1] - swipePixel, endXY[0], endXY[1]); // actual
					} else {
						swipeByPoints(endXY[0], endXY[1] + swipePixel, endXY[0], endXY[1]);
					}
					end = start;
				}
			}
		}
		System.out.println("Done");
	}

	public String get(int input) {
		String day = Integer.toString(input);
		day = day.length() == 1 ? "0" + day : day;
		return day;
	}

	public String getTwoDecimal(double input) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(input);
	}

	public String getTwoDecimal(String input) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(Double.parseDouble(input));
	}

}
