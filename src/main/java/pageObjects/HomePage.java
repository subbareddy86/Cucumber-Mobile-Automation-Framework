package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utilities.BasePage;
import utilities.Utility;

public class HomePage extends BasePage {
	
	private static Utility util = Utility.getInstance();
	private static WebDriver driver = util.getDriver();
	
	public HomePage() {
		super(driver);
	}
	
	
	By navigationDrawer = By.xpath("//*[@contentDescription='Open navigation drawer']");
	By txtNewList = By.xpath("//*[@id='edittext']");
	By btnNewListSubmit = By.xpath("//*[@text='OK']");
	By txtNewItem = By.xpath("//*[@id='Itemtext']");
	By btnNewItem = By.xpath("//*[@text='OK']");
	By btnCleanupList = By.xpath("//*[@contentDescription='Clean up list']");
	
	String itemCheckBox = "//*[@id='check' AND @text='%s']";
	
	public void clickOnNavigationDrawer() {
		click(navigationDrawer, "Navigation Drawer");
	}
	
	public void addNewList(String listName) {
		sendKeys(txtNewList, listName, "New List");
		click(btnNewListSubmit, "New List Submit Button");
	}
	
	public void addNewItem(String itemName) {
		sendKeys(txtNewItem, itemName, "New Item");
		click(btnNewItem, "New Iteam Submit Button");
	}
	
	public void deleteItem(String itemName) {
		click(getByXapth(itemCheckBox, itemName), "Item Check Box");
		click(btnCleanupList, "Cleanup List Icon");
	}
	
}
