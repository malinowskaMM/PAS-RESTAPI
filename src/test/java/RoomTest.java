import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Test;
import pl.pas.hotel.model.room.Room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RoomTest {

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

        System.out.println(uuid); //TODO zwraca nulla do sprawdzenia

        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/rooms/"+uuid);


        System.out.println(response.asString());


    }

}
