import io.restassured.RestAssured;
import org.json.simple.JSONObject;
import org.junit.Test;

public class RoomTest {

    @Test
    public void shouldCreateRoom() {
        JSONObject createRoomRequest = new JSONObject();
        createRoomRequest.put("roomNumber", 1);
        createRoomRequest.put("price", 120.0);
        createRoomRequest.put("roomCapacity", 2);

        RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createRoomRequest.toJSONString()).when().
                post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms").
                then().statusCode(200).extract();
    }
}
