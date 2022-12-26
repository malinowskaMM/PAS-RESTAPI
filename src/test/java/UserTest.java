import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    String exampleUUID;

    @Before
    public void initialize() {
        JSONObject createClientRequest = new JSONObject();
        createClientRequest.put("login", "exampleUser");
        createClientRequest.put("password", "examplePassword");
        createClientRequest.put("personalId", "12345678910");
        createClientRequest.put("firstName", "Jan");
        createClientRequest.put("lastName", "Kowalski");
        createClientRequest.put("address", "Pawia 23/25 m 13 Warszawa 00-000");

        exampleUUID = RestAssured.given().
                header("Content-Type","application/json" ).
                header("Accept","application/json" ).
                body(createClientRequest.toJSONString()).when().
                post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/client").
                then().statusCode(200) //TODO: gives 400
                .extract().path("id");
    }

    @Test
    public void shouldGetClientWithGivenId() {

        assertThat(exampleUUID).isNotNull();

        Response response = RestAssured.given().contentType(ContentType.JSON).
                when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);

        //assertThat(response.asString()).isEqualTo("{\"price\":120.0,\"roomCapacity\":2,\"roomId\":\""+exampleUUID+"\",\"roomNumber\":1}");
    }
}
