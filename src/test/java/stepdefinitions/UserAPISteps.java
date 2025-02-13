package stepdefinitions;

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
            .filter(new AllureRestAssured())
            .contentType(ContentType.JSON)
            .body(createdUser)
            .when()
            .post();
    }

    @And("store created user information")
    public void storeCreatedUserInformation() {
        Assert.assertEquals(200, response.getStatusCode());
    }

    @And("user sets API endpoint for last created user")
    public void setEndpointForLastCreatedUser() {
        String username = UserDataGenerator.getLastGeneratedUsername();
        RestAssured.basePath = "/user/" + username;
    }

    @When("user sends GET request")
    public void sendGetRequest() {
        response = given()
            .filter(new AllureRestAssured())
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

        JsonPath jsonPath = response.jsonPath();
        String actualUsername = jsonPath.getString("username");
        String actualFirstName = jsonPath.getString("firstName");
        String actualEmail = jsonPath.getString("email");
        
        User expectedUser = UserDataGenerator.getLastGeneratedUser();
        Assert.assertNotNull("No user data found from previous scenario", expectedUser);
        

        Assert.assertEquals(expectedUser.getUsername(), actualUsername);
        Assert.assertEquals(expectedUser.getFirstName(), actualFirstName);
        Assert.assertEquals(expectedUser.getEmail(), actualEmail);
    }
} 