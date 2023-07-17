package com.springboot.test.stepDefinitions;

import com.springboot.test.APIClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubAPITests {

    private final Logger log = LoggerFactory.getLogger(GitHubAPITests.class);

    @Autowired
    private APIClient apiClient;

    @Given("I call the users endpoint for {string}")
    public void i_call_the_users_endpoint_for(String user) {
        apiClient.users(user);
    }

    @Then("I get Response as {string}")
    public void i_get_response_as(String expected) {
        log.info("Veriying response = '{}'", expected);
        if(apiClient.exception == null) {
            String receivedResponseCode = apiClient.responsecode();
            assertThat(receivedResponseCode).isEqualTo((expected));
        }
        else
            assertThat(apiClient.exception.contains(expected)).isTrue();
    }

    @Then("response json is not empty")
    public void response_json_is_not_empty() {
        String responseJson = apiClient.responseBody();
        Assertions.assertFalse(responseJson.isEmpty());
        log.info("Veriying response Json is not empty", responseJson);
    }

    @Then("response json contains user id {int}")
    public void response_json_contains_user_id(int userid){
        float responseID = apiClient.getID();
        log.info("Veriying id = '{}'", userid);
        Assertions.assertTrue(responseID==userid);
    }

    @Then("response json contains login as {string}")
    public void response_json_contains(String expected) {
        String name = apiClient.getLogin();
        log.info("Veriying login name = '{}'", expected);
        Assertions.assertTrue(name.equals(expected));
    }

    @When("I call the repos endpoint to create repo {string}")
    public void i_call_the_repos_endpoint_to_create_repo(String repoName) {
        apiClient.repos(repoName);
    }

    @When("I call the repos endpoint to delete repo {string} for user {string}")
    @When("I call the repos endpoint to delete repo {string} for user {string} again which is already deleted")
    public void i_call_the_repos_endpoint_to_delete_repo(String repoName, String user) {
        apiClient.deleteRepo(user, repoName);
    }
    @Then("I get Response code as {string}")
    public void i_get_response_code_as(String expected) {
        log.info("Expected response code = '{}', actual is '{}'", expected, apiClient.responsecode);
        Assertions.assertTrue(apiClient.responsecode.equals(expected));
    }
}