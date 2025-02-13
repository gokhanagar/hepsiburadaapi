package stepdefinitions;

import org.junit.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.User;
import utilities.UserDataGenerator;

public class UserAPISteps {
    private Response response;
    private User createdUser;

    @Given("user sets API base URL {string}")
    public void setBaseUrl(String url) {
        RestAssured.baseURI = url;
    }

    @And("user sets API endpoint {string}")
    public void setEndpoint(String path) {
        RestAssured.basePath = path;
    }

    @When("user creates new user with dynamic data")
    public void createNewUserWithDynamicData() {
        createdUser = UserDataGenerator.generateUser();
        
        response = given()
            .contentType(ContentType.JSON)
            .body(createdUser)  // RestAssured otomatik olarak JSON'a çevirecek
        .when()
            .post();
    }

    @And("store created user information")
    public void storeCreatedUserInformation() {
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("Created user with username: " + createdUser.getUsername());
    }

    @And("user sets API endpoint for last created user")
    public void setEndpointForLastCreatedUser() {
        String username = UserDataGenerator.getLastGeneratedUsername();
        RestAssured.basePath = "/user/" + username;
        System.out.println("Getting user details for: " + username);
    }

    @When("user sends GET request")
    public void sendGetRequest() {
        response = given()
            .contentType(ContentType.JSON)
        .when()
            .get();
    }

    @Then("validate the status code {int}")
    public void validateStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals("Status code does not match!", expectedStatusCode, actualStatusCode);
    }

    @And("validate user information matches with created user")
    public void validateUserInformationMatches() {
        Assert.assertNotNull("Response body is null!", response.getBody());
        
        User expectedUser = UserDataGenerator.getLastGeneratedUser();
        Assert.assertNotNull("No user data found from previous scenario", expectedUser);
        
        User actualUser = response.as(User.class);  // JSON response'u User objesine çevir
        
        Assert.assertEquals("Username doesn't match!", expectedUser.getUsername(), actualUser.getUsername());
        Assert.assertEquals("FirstName doesn't match!", expectedUser.getFirstName(), actualUser.getFirstName());
        Assert.assertEquals("Email doesn't match!", expectedUser.getEmail(), actualUser.getEmail());
    }
} 