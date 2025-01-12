Feature: Check login for valid and invalid credentials
  Cases to validate:
  1. valid credentials, then access the system
  2. invalid user, valid password, then get error message
  3. invalid user, invalid password, then get error message
  4. valid user, invalid password, then get error message


  Scenario: Login in the system with valid credentials
    Given The following credentials then
      | User         | Password         |
      | @ValidUser   | @ValidPassword   |

  Scenario: Login in the system with INVALID credentials
    Given The following INVALID credentials are
      | User         | Password         |
      | @InvalidUser | @ValidPassword   |
      | @InvalidUser | @InvalidPassword |
      | @ValidUser   | @InvalidPassword |
    Then a UnrecognizedPropertyException should be thrown for all invalid credentials
