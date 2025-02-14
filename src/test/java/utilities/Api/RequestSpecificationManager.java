package utilities.Api;

import io.qameta.allure.restassured.AllureRestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecificationManager {
    
    public static RequestSpecification getDefaultRequestSpec() {
        return given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON);
    }
    
    public static RequestSpecification getUserCreationSpec(Object requestBody) {
        return getDefaultRequestSpec()
                .body(requestBody);
    }
    
    public static RequestSpecification getUserDetailsSpec() {
        return getDefaultRequestSpec();
    }


} 