Feature: GIT Hub API Tests

  Scenario: GET Call GitHub Users Valid Endpoint
    Given I call the users endpoint for "/Raj03042023"
    Then I get Response as "200 OK"
    And response json is not empty
    And response json contains user id 139493520
    And response json contains login as "Raj03042023"

  Scenario: GET Call GitHub Users Invalid Endpoint
    Given I call the users endpoint for "/Raj03042023123"
    Then I get Response as "404 Not Found"

  Scenario: POST Call GitHub - Create Repo
    When I call the repos endpoint to create repo "TestRepoCreation"
    Then I get Response as "201 CREATED"

  Scenario: POST Call GitHub - Create Duplicate Repo to validate error code
    When I call the repos endpoint to create repo "TestRepoCreation"
    Then I get Response as "422 Unprocessable Entity"

  Scenario: POST Call GitHub - Delete Repo
    When I call the repos endpoint to delete repo "/-TestRepoCreation-" for user "/Raj03042023"
    Then I get Response code as "204 NO_CONTENT"
    When I call the repos endpoint to delete repo "/-TestRepoCreation-" for user "/Raj03042023" again which is already deleted
    Then I get Response as "404 Not Found"

