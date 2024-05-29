import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.SQLOutput;

import static io.restassured.RestAssured.*;

public class APITesting {

    @Test //a simple GET request is made to the reqres.in API to retrieve user data.
    void test1() {
        Response response = RestAssured.get("https://reqres.in/api/users?page=2");

        System.out.println("Response: " + response.asString());
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody().asString());
        System.out.println("Time taken: " + response.getTime());
        System.out.println("Header: " + response.getHeader("content-type"));

    }

    @Test // this test case focuses on an authenticated GET request to fetch book data from a local API (http://localhost:7081/api/books)
    void test() {
        Response response = given()
                .auth().preemptive().basic("admin", "password") // Set the username and password
                .get("http://127.0.0.1:7081/api/books");
        Assert.assertEquals(response.getStatusCode(), 200);

    }

    //Successfully update the book - admin
    @Test
    void test2() {
        String baseUri = "http://127.0.0.1:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";

        try {
            Response response = given()
                    .auth().preemptive().basic("admin", "password")
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .put(baseUri);
            Assert.assertEquals(response.getStatusCode(),200);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    //Invalid | Empty Input Parameters in the Request
    @Test
    void test3() {
        String baseUri = "http://127.0.0.1:7081/api/books/1";

        String requestBody = "{\n" +
                "  " +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";

        try {
            Response response = given()
                    .auth().preemptive().basic("admin", "password")
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .put(baseUri);
            Assert.assertEquals(response.getStatusCode(),400);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    //not authorized to create the book
    @Test
    void test4() {
        String baseUri = "http://127.0.0.1:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";

        try {
            Response response = given()
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .put(baseUri);
            Assert.assertEquals(response.getStatusCode(),401);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    //Request api call is forbidden
    @Test
    void test5() {
        String baseUri = "http://127.0.0.1:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";

        try {
            Response response = given()
                    .auth().preemptive().basic("user", "password")
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .put(baseUri);
            Assert.assertEquals(response.getStatusCode(),403);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    //Book is not found
    @Test
    void test6() {
        String baseUri = "http://127.0.0.1:7081/api/books/2";

        String requestBody = "{\n" +
                "    \"id\": 2,\n" +
                "    \"title\": \"Lord of the Ring\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";

        try {
            Response response = given()
                    .auth().preemptive().basic("admin", "password")
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .put(baseUri);
            Assert.assertEquals(response.getStatusCode(),404);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }


    }

    //Invalid input parameter(integer value) for title
    @Test
    void test7() {
        String baseUri = "http://127.0.0.1:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\":  3,\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";

        try {
            Response response = given()
                    .auth().preemptive().basic("admin", "password")
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .put(baseUri);
            Assert.assertEquals(response.getStatusCode(),400);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

    }

    //Invalid input parameter(integer value) for author
    @Test
    void test8() {
        String baseUri = "http://127.0.0.1:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": 3\n" +
                "}";

        try {
            Response response = given()
                    .auth().preemptive().basic("admin", "password")
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .put(baseUri);
            Assert.assertEquals(response.getStatusCode(),400);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

    }
}

