package stepdefinitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pojo.ObjectRepository;

public class ShoppingListTest implements ObjectRepository {

	@Given("^I create new lists with name \"([^\"]*)\"$")
	public void i_create_new_lists_with_name(String listName) {
	    homePage.clickOnNavigationDrawer();
	    homePage.addNewList(listName);
	}

	@When("^I add new list items with name \"([^\"]*)\"$")
	public void i_add_new_list_items_with_name(String itemName)  {
	    homePage.addNewItem(itemName);
	}

	@Then("^I delete an item from the list \"([^\"]*)\"$")
	public void i_delete_an_item_from_the_list(String itemName) {
		homePage.deleteItem(itemName);
	}
	
	@Then("^I sort the list and validate the sorting of the list items$")
	public void i_sort_the_list_and_validate_the_sorting_of_the_list_items() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}
	
}