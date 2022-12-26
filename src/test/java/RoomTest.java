import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RoomTest {

    String exampleUUID;

    @Before
    public void initialize() {
        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 120.0);
        createRoomRequest.put("roomCapacity", 2);

        exampleUUID = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms").
                then().statusCode(200)
                .extract().path("roomId");

    }

    @Test
    public void shouldCreateRoom() {
        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 120.0);
        createRoomRequest.put("roomCapacity", 2);

        String uuid = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms").
                then().statusCode(200)
                .body("roomNumber", equalTo(1))
                .body("price", equalTo(120.0F))
                .body("roomCapacity", equalTo(2))
                .extract().path("roomId");

        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+uuid);

        assertThat(response.asString()).isEqualTo("{\"price\":120.0,\"roomCapacity\":2,\"roomId\":\""+uuid+"\",\"roomNumber\":1}");
    }

    @Test
    public void shouldDeleteRoomWithGivenId() {

        assertThat(exampleUUID).isNotNull();

        RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+exampleUUID)
                .then().statusCode(200);

        RestAssured.given().delete("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+exampleUUID)
                .then().statusCode(200);

        RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+exampleUUID)
                .then().statusCode(404);
    }

    @Test
    public void shouldUpdateRoom() {
        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+exampleUUID);

        assertThat(response.asString()).isEqualTo("{\"price\":120.0,\"roomCapacity\":2,\"roomId\":\""+exampleUUID+"\",\"roomNumber\":1}");

        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 1200.0);
        createRoomRequest.put("roomCapacity", 2);

        RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                put("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+exampleUUID).
                then().statusCode(200);

        response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+exampleUUID);

        assertThat(response.asString()).isEqualTo("{\"price\":1200.0,\"roomCapacity\":2,\"roomId\":\""+exampleUUID+"\",\"roomNumber\":1}");
    }

    @Test
    public void shouldGetRoomWithGivenId() {

        assertThat(exampleUUID).isNotNull();

        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+exampleUUID);

        assertThat(response.asString()).isEqualTo("{\"price\":120.0,\"roomCapacity\":2,\"roomId\":\""+exampleUUID+"\",\"roomNumber\":1}");
    }

    @Test
    public void shouldNotGetRoomWithGivenId() {
        RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+"XD");

    }

}
