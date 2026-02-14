Feature:Validate the CRUD of expense and revenue entries
  Items to validate:
  1. Log in to the system
  2. Create a new EXPENSE and INCOME entry
  3. Search for the entry
  4. Edit the entry
  5. Remove the entry

  Scenario: Log into the system with a predefined user via settings
    Given User and password existent in to the settings
    Then You must log in and access the home page


  Scenario: Search an existing entry by category
    Given Register entry whit provided data
      | description             | amount      | entryDate         | entryType | category    |
      | Cucumber - Lunch        | @MoneyValue | @DateCurrentMonth | EXPENSE   | FOOD        |
      | Cucumber - Bonus Salary | 2342.02     | @DateCurrentMonth | INCOME    | WAGE        |
      | Cucumber - Bonus Salary | 2342.02     | @DateCurrentMonth | TRANSF    | INVESTMENTS |
    Given Search an entry by category "INCOME"
    And Search an entry by category "EXPENSE"
    And Search an entry by category "TRANSF"



  Scenario: Edit the first found entry
    Given Register entry whit provided data
      | description | amount      | entryDate         | entryType | category    |
      | Dividendos  | @MoneyValue | @DateCurrentMonth | INCOME    | INVESTMENTS |
    Given Search an entry by category "RENDA"
    And Edit the first entry found

  Scenario: Remove the first found entry
    Given Register entry whit provided data
      | description | amount      | entryDate | entryType | category |
      | Roupa       | @MoneyValue | @AnyDate  | EXPENSE   | CLOTHING |
    Given Search an entry by category "EXPENSE"
    And Remove the first entry found

  Scenario: Add new entries and export entries to a file excel
    Given Register entry whit provided data
      | description             | amount      | entryDate         | entryType | category    |
      | Cucumber - car repair   | @MoneyValue | @DateCurrentMonth | EXPENSE   | CAR         |
      | Cucumber - Bonus Salary | 942.02      | @DateCurrentMonth | INCOME    | WAGE        |
      | Cucumber - Bonus Salary | 1892.02     | @DateCurrentMonth | TRANSF    | INVESTMENTS |
    Then Export entries to excel file and validate


  Scenario: Remove all entries and validate export with no entries
    Given Register entry whit provided data
      | description             | amount      | entryDate         | entryType | category        |
      | Cucumber - internet     | @MoneyValue | @DateCurrentMonth | EXPENSE   | PHONE_INTERNET  |
      | Cucumber - Bonus Salary | 942.02      | @DateCurrentMonth | INCOME    | WAGE            |
      | Cucumber - Bonus Salary | 1892.02     | @DateCurrentMonth | TRANSF    | INVESTMENTS     |
    Given Remove all entries
    And No entries must be found to the logged user
    Then Export entries to excel file and no entries must be found in the exported file
