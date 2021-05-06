Feature: As a Amazon user I should be able to login and logout with valid credentials

  Scenario: Add new list, add item to list and delete iteam from the list
  	Given I create new lists with name "List_One"
    When I create new lists with name "List_Two"
    And I add new list items with name "item_one"
    Then I delete an item from the list "item_one"
    
  Scenario: Add new list, add items to list and sort the list
  	Given I create new lists with name "List_One"
    When I add new list items with name "item_one"
    And I add new list items with name "item_Two" 
    Then I sort the list and validate the sorting of the list items
  
   
