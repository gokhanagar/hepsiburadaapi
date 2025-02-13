Feature: User API Tests

  Scenario: Create a new user with POST request
    Given user sets API base URL "https://petstore.swagger.io/v2"
    And user sets API endpoint "/user"
    When user creates new user with dynamic data
    Then validate the status code 200
    And store created user information

  Scenario: Get user information with GET request
    Given user sets API base URL "https://petstore.swagger.io/v2"
    And user sets API endpoint for last created user
    When user sends GET request
    Then validate the status code 200
    And validate user information matches with created user 