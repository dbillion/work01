import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 *
 */
public class TestCase02 {
    String userID = "9b5f49ab-eea9-45f4-9d66-bcf56a531b85";
    String userName = "TOOLSQA-Test";
    String password = "Test@@123";
    String baseUrl = "https://bookstore.toolsqa.com";


    @Test
    public void UserIsLoggedin() {

        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        Response response = request.body("{ \"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                .post("/Account/v1/Authorized");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void DeleteBook(){


        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");

        Response response = request.body("{ \"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                .post("/Account/v1/GenerateToken");


       String jsonString = response.asString();
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
        String bookId = books.get(0).get("isbn");
        request.header("Content-Type", "application/json");
            request.header("Authorization", "Bearer " )
                .header("Content-Type", "application/json");

        response = request.body("{ \"isbn\": \"" + bookId + "\", \"userId\": \"" + userID + "\"}")
                .delete("/BookStore/v1/Book");

        Assert.assertEquals(204, response.getStatusCode());
    }
}


