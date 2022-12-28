import io.restassured.RestAssured;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class RentTest {

    String exampleRoomUUID;
    String exampleClientUUID;
    String exampleRentUUID;

    @Before
    public void initialize() {
        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 120.0);
        createRoomRequest.put("roomCapacity", 2);

        exampleRoomUUID = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms").
                then().statusCode(200)
                .extract().path("uuid");


        JSONObject createClientRequest = new JSONObject();
        createClientRequest.put("login", "exampleUser");
        createClientRequest.put("password", "examplePassword");
        createClientRequest.put("accessLevel", "CLIENT");
        createClientRequest.put("personalId", "12345678910");
        createClientRequest.put("firstName", "Jan");
        createClientRequest.put("lastName", "Kowalski");
        createClientRequest.put("address", "Pawia 23/25 m 13 Warszawa 00-000");

        exampleClientUUID = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createClientRequest.toJSONString()).when().
                post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/client").
                then().statusCode(200)
                .extract().path("uuid");


        JSONObject createRentRequest = new JSONObject();
        createRentRequest.put("clientId", exampleClientUUID);
        createRentRequest.put("roomId", exampleRoomUUID);
        createRentRequest.put("startDate", "2020-12-10T13:45:00.000");
        createRentRequest.put("endDate", "2020-12-15T13:45:00.000");

        RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRentRequest.toJSONString()).when().
                post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents").
                then().statusCode(200)
                .extract().path("id");
    }

    @Test
    public void test() {

    }


}
