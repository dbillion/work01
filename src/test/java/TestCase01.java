import com.alibaba.fastjson.JSONObject;
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
public class TestCase01 {
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
    public void AddBook(){
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response response = request.body("{ \"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                .post("/Account/v1/GenerateToken");
        String jsonString = response.asString();
        String token = JsonPath.from(jsonString).get("token");
        response = request.get("/BookStore/v1/Books");
        jsonString = response.asString();
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
        String bookId = books.get(0).get("isbn");
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
        response = request.body("{ \"userId\": \"" + userID + "\", " +
                        "\"collectionOfIsbns\": [ { \"isbn\": \"" + bookId + "\" } ]}")
                .post("/BookStore/v1/Books");
        Assert.assertEquals( 201, response.getStatusCode());
    }
}