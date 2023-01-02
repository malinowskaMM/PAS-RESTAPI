import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RentTest {

    String exampleRoomUUID;
    String exampleClientUUID;
    String exampleRentUUID;
    String userLogin;

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
        userLogin = generateRandomLogin();
        createClientRequest.put("login", userLogin);
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
        createRentRequest.put("startDate", "2024-12-10T13:45:00.000");
        createRentRequest.put("endDate", "2024-12-15T13:45:00.000");

        exampleRentUUID = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRentRequest.toJSONString()).when().
                post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents").
                then().statusCode(200)
                .extract().path("id");
    }

    @AfterEach
    public void clean() {
        RestAssured.given().delete("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/" + exampleRentUUID)
                .then().statusCode(200);
    }

    @Test
    public void testRentsByClientUUID() {
        assertThat(exampleClientUUID).isNotNull();
        assertThat(exampleRoomUUID).isNotNull();
        assertThat(exampleRentUUID).isNotNull();

        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/client/" + exampleClientUUID);

        assertThat(response.asString()).isEqualTo("[{\"beginTime\":\"2024-12-10T13:45:00\",\"client\":{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+userLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleClientUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"},\"endTime\":\"2024-12-15T13:45:00\",\"id\":\""+exampleRentUUID+"\",\"room\":{\"price\":120.0,\"roomCapacity\":2,\"roomNumber\":1,\"uuid\":\""+exampleRoomUUID+"\"}}]");
    }

    @Test
    public void testRentsByRoomUUID() {
        assertThat(exampleClientUUID).isNotNull();
        assertThat(exampleRoomUUID).isNotNull();
        assertThat(exampleRentUUID).isNotNull();

        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/room/" + exampleRoomUUID);

        assertThat(response.asString()).isEqualTo("[{\"beginTime\":\"2024-12-10T13:45:00\",\"client\":{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+userLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleClientUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"},\"endTime\":\"2024-12-15T13:45:00\",\"id\":\""+exampleRentUUID+"\",\"room\":{\"price\":120.0,\"roomCapacity\":2,\"roomNumber\":1,\"uuid\":\""+exampleRoomUUID+"\"}}]");
    }

    @Test
    public void testCreateRent(){
        assertThat(exampleClientUUID).isNotNull();
        assertThat(exampleRoomUUID).isNotNull();
        assertThat(exampleRentUUID).isNotNull();

        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents");

        assertThat(response.asString()).contains("{\"beginTime\":\"2024-12-10T13:45:00\",\"client\":{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+userLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleClientUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"},\"endTime\":\"2024-12-15T13:45:00\",\"id\":\""+exampleRentUUID+"\",\"room\":{\"price\":120.0,\"roomCapacity\":2,\"roomNumber\":1,\"uuid\":\""+exampleRoomUUID+"\"}}");
    }

    @Test
    public void testGetRent() {
        assertThat(exampleClientUUID).isNotNull();
        assertThat(exampleRoomUUID).isNotNull();
        assertThat(exampleRentUUID).isNotNull();
        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/" + exampleRentUUID);

        assertThat(response.asString()).isEqualTo("{\"beginTime\":\"2024-12-10T13:45:00\",\"client\":{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+userLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleClientUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"},\"endTime\":\"2024-12-15T13:45:00\",\"id\":\""+exampleRentUUID+"\",\"room\":{\"price\":120.0,\"roomCapacity\":2,\"roomNumber\":1,\"uuid\":\""+exampleRoomUUID+"\"}}");
    }

    @Test
    public void testDeleteRent() {
        assertThat(exampleRentUUID).isNotNull();

        RestAssured.given().contentType(ContentType.JSON)
                .when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/" + exampleRentUUID)
                .then().statusCode(200);

        RestAssured.given().delete("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/" + exampleRentUUID)
                .then().statusCode(200);

        RestAssured.given()
                .when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/" + exampleRentUUID)
                .then().statusCode(404);
    }

    @Test
    public void testGetRentsByStartDate() {
        String startDate = "2024-12-10T13:45:00";
        RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/startDate/" + startDate)
                .then().statusCode(200);
    }

    @Test
    public void testGetRentsByEndDate() {
        String endDate = "2024-12-15T13:45:00";
        RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rents/endDate/" + endDate)
                .then().statusCode(200);
    }

    private String generateRandomLogin() {
        return "exampleUser".concat(UUID.randomUUID().toString());
    }

}
