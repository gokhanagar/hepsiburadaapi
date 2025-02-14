package stepdefinitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.User;
import utilities.ConfigurationReader;
import utilities.UserDataGenerator;

public class UserAPISteps {
    private static final Logger logger = LogManager.getLogger(UserAPISteps.class);
    private Response response;
    private User createdUser;

    @Given("user sets API base URL {string}")
    public void setBaseUrl(String urlKey) {
        RestAssured.baseURI = ConfigurationReader.getProperty("api.base.url");
        logger.info("Base URL set to: " + RestAssured.baseURI);
    }

    @And("user sets API endpoint {string}")
    public void setEndpoint(String endpointKey) {
        RestAssured.basePath = ConfigurationReader.getProperty("api.users.endpoint");
        logger.info("API endpoint set to: " + RestAssured.basePath);
    }

    @When("user creates new user with dynamic data")
    public void createNewUserWithDynamicData() {
        createdUser = UserDataGenerator.generateUser();
        
        logger.info("Creating new user with username: " + createdUser.getUsername() + 
                    ", firstName: " + createdUser.getFirstName() + 
                    ", email: " + createdUser.getEmail());
        
        response = given()
            .filter(new AllureRestAssured())
            .contentType(ContentType.JSON)
            .body(createdUser)
            .when()
            .post();
        
        logger.info("POST request sent to create new user");
        logger.info("Response status code: " + response.getStatusCode());
        logger.info("Response body: " + response.asString());
    }

    @And("store created user information")
    public void storeCreatedUserInformation() {
        Assert.assertEquals(200, response.getStatusCode());
        logger.info("User created successfully with status code: " + response.getStatusCode());
    }

    @And("user sets API endpoint for last created user")
    public void setEndpointForLastCreatedUser() {
        String username = UserDataGenerator.getLastGeneratedUsername();
        String userEndpoint = ConfigurationReader.getProperty("api.user.endpoint")
            .replace("{username}", username);
        RestAssured.basePath = userEndpoint;
        logger.info("User endpoint set to: " + RestAssured.basePath);
    }

    @When("user sends GET request")
    public void sendGetRequest() {
        response = given()
            .filter(new AllureRestAssured())
            .contentType(ContentType.JSON)
            .when()
            .get();
        logger.info("GET request sent to fetch user details");
        logger.info("Response: " + response.asString());
    }

    @Then("validate the status code {int}")
    public void validateStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals("Status code does not match!", expectedStatusCode, actualStatusCode);
    }

    @And("validate user information matches with created user")
    public void validateUserInformationMatches() {
        Assert.assertNotNull("Response body is null!", response.getBody());

        JsonPath jsonPath = response.jsonPath();
        String actualUsername = jsonPath.getString("username");
        String actualFirstName = jsonPath.getString("firstName");
        String actualEmail = jsonPath.getString("email");
        
        User expectedUser = UserDataGenerator.getLastGeneratedUser();
        Assert.assertNotNull("No user data found from previous scenario", expectedUser);
        

        Assert.assertEquals(expectedUser.getUsername(), actualUsername);
        Assert.assertEquals(expectedUser.getFirstName(), actualFirstName);
        Assert.assertEquals(expectedUser.getEmail(), actualEmail);
        logger.info("User details verified successfully");
    }
} 