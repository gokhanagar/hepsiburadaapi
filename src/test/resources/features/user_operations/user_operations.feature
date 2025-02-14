Feature: User API Tests

  Scenario: Create and verify user
    Given user sets API base URL "api.base.url"
    And user sets API endpoint "api.users.endpoint"
    When user creates new user with dynamic data
    Then validate the status code 200
    And store created user information

  Scenario: Get user information with GET request
    Given user sets API base URL "api.base.url"
    And user sets API endpoint for last created user
    When user sends GET request
    Then validate the status code 200
    And validate user information matches with created user 