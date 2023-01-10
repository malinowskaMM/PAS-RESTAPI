// import io.restassured.RestAssured;
// import io.restassured.http.ContentType;
// import io.restassured.response.Response;
// import org.json.simple.JSONObject;
// import org.junit.Before;
// import org.junit.Test;
//
// import java.util.UUID;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.hamcrest.Matchers.equalTo;
//
// public class UserTest {
//
//     String exampleUUID;
//     String adminExampleUUID;
//     String managerExampleUUID;
//     String uniqueManagerLogin;
//     String uniqueClientLogin;
//     String uniqueAdminLogin;
//
//     @Before
//     public void initialize() {
//         JSONObject createClientRequest = new JSONObject();
//         uniqueClientLogin = generateRandomLogin();
//         createClientRequest.put("login", uniqueClientLogin);
//         createClientRequest.put("password", "examplePassword");
//         createClientRequest.put("accessLevel", "CLIENT");
//         createClientRequest.put("personalId", "12345678910");
//         createClientRequest.put("firstName", "Jan");
//         createClientRequest.put("lastName", "Kowalski");
//         createClientRequest.put("address", "Pawia 23/25 m 13 Warszawa 00-000");
//
//         exampleUUID = RestAssured.given().
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(createClientRequest.toJSONString()).when().
//                 post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/client").
//                 then().statusCode(200)
//                 .extract().path("uuid");
//
//
//         JSONObject createAdminRequest = new JSONObject();
//         uniqueAdminLogin = generateRandomLogin();
//         createAdminRequest.put("login", uniqueAdminLogin);
//         createAdminRequest.put("password", "examplePasswordAdmin");
//         createAdminRequest.put("accessLevel", "ADMIN");
//
//         adminExampleUUID = RestAssured.given().
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(createAdminRequest.toJSONString()).when().
//                 post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/admin").
//                 then().statusCode(200)
//                 .extract().path("uuid");
//
//
//         JSONObject createManagerRequest = new JSONObject();
//         uniqueManagerLogin = generateRandomLogin();
//         createManagerRequest.put("login", uniqueManagerLogin);
//         createManagerRequest.put("password", "examplePasswordManager");
//         createManagerRequest.put("accessLevel", "MANAGER");
//
//         managerExampleUUID = RestAssured.given().
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(createManagerRequest.toJSONString()).when().
//                 post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/manager").
//                 then().statusCode(200)
//                 .extract().path("uuid");
//     }
//
//     @Test
//     public void shouldGetClientWithGivenId() {
//
//         assertThat(exampleUUID).isNotNull();
//
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+uniqueClientLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"}");
//     }
//
//     @Test
//     public void shouldGetAdminWithGivenId() {
//
//         assertThat(adminExampleUUID).isNotNull();
//
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + adminExampleUUID);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"ADMIN\",\"active\":true,\"login\":\""+uniqueAdminLogin+"\",\"password\":\"examplePasswordAdmin\",\"uuid\":\""+adminExampleUUID+"\"}");
//     }
//
//     @Test
//     public void shouldGetManagerWithGivenId() {
//
//         assertThat(managerExampleUUID).isNotNull();
//
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + managerExampleUUID);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"MANAGER\",\"active\":true,\"login\":\""+uniqueManagerLogin+"\",\"password\":\"examplePasswordManager\",\"uuid\":\""+managerExampleUUID+"\"}");
//     }
//
//     @Test
//     public void shouldUpdateClientWithGivenId() {
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+uniqueClientLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"}");
//
//         JSONObject changeClientRequest = new JSONObject();
//         changeClientRequest.put("login", "exampleUser");
//         changeClientRequest.put("password", "examplePassword");
//         changeClientRequest.put("accessLevel", "CLIENT");
//         changeClientRequest.put("personalId", "12345678910");
//         changeClientRequest.put("firstName", "Jan");
//         changeClientRequest.put("lastName", "Nowak");
//         changeClientRequest.put("address", "Pawia 23/25 m 13 Warszawa 00-000");
//
//         RestAssured.given().contentType(ContentType.JSON).
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(changeClientRequest).
//                 when().put("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/client/" + exampleUUID);
//
//         Response response2 = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);
//
//         assertThat(response2.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\"exampleUser\",\"password\":\"examplePassword\",\"uuid\":\""+exampleUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Nowak\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"}");
//     }
//
//     @Test
//     public void shouldDeleteClientWithGivenId() {
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+uniqueClientLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"}");
//
//         RestAssured.given().when().delete("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);
//
//         Response response1 = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);
//
//     }
//
//     @Test
//     public void shouldCreateClient() {
//         JSONObject createClientRequest = new JSONObject();
//         String inTestUniqueClientLogin = generateRandomLogin();
//         createClientRequest.put("login", inTestUniqueClientLogin);
//         createClientRequest.put("password", "examplePassword");
//         createClientRequest.put("accessLevel", "CLIENT");
//         createClientRequest.put("personalId", "12345678910");
//         createClientRequest.put("firstName", "Jan");
//         createClientRequest.put("lastName", "Kowalski");
//         createClientRequest.put("address", "Pawia 23/25 m 13 Warszawa 00-000");
//
//         String uuid = RestAssured.given().
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(createClientRequest.toJSONString()).when().
//                 post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/client").
//                 then().statusCode(200)
//                 .body("login", equalTo(inTestUniqueClientLogin))
//                 .body("password", equalTo("examplePassword"))
//                 .body("accessLevel", equalTo("CLIENT"))
//                 .body("personalId", equalTo("12345678910"))
//                 .body("firstName", equalTo("Jan"))
//                 .body("lastName", equalTo("Kowalski"))
//                 .body("address", equalTo("Pawia 23/25 m 13 Warszawa 00-000"))
//                 .extract().path("uuid");
//
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/"+uuid);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+inTestUniqueClientLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+uuid+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"}");
//     }
//
//     @Test
//     public void shouldCreateManager() {
//         JSONObject createManagerRequest = new JSONObject();
//         String inTestUniqueManagerLogin = generateRandomLogin();
//         createManagerRequest.put("login", inTestUniqueManagerLogin);
//         createManagerRequest.put("password", "examplePassword");
//         createManagerRequest.put("accessLevel", "MANAGER");
//
//         String uuid = RestAssured.given().
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(createManagerRequest.toJSONString()).when().
//                 post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/manager").
//                 then().statusCode(200)
//                 .body("login", equalTo(inTestUniqueManagerLogin))
//                 .body("password", equalTo("examplePassword"))
//                 .body("accessLevel", equalTo("MANAGER"))
//                 .extract().path("uuid");
//
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/"+uuid);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"MANAGER\",\"active\":true,\"login\":\""+inTestUniqueManagerLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+uuid+"\"}");
//     }
//
//     @Test
//     public void shouldUpdateManager() {
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + managerExampleUUID);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"MANAGER\",\"active\":true,\"login\":\""+uniqueManagerLogin+"\",\"password\":\"examplePasswordManager\",\"uuid\":\""+managerExampleUUID+"\"}");
//
//         JSONObject updateManagerRequest = new JSONObject();
//         String inTestUniqueManagerLogin = generateRandomLogin();
//         updateManagerRequest.put("login", inTestUniqueManagerLogin);
//         updateManagerRequest.put("password", "examplePasswordManagerUpdate");
//         updateManagerRequest.put("accessLevel", "MANAGER");
//
//         RestAssured.given().contentType(ContentType.JSON).
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(updateManagerRequest).
//                 when().put("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/manager/" + managerExampleUUID);
//
//         Response response2 = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + managerExampleUUID);
//
//         assertThat(response2.asString()).isEqualTo("{\"accessLevel\":\"MANAGER\",\"active\":true,\"login\":\""+inTestUniqueManagerLogin+"\",\"password\":\"examplePasswordManagerUpdate\",\"uuid\":\""+managerExampleUUID+"\"}");
//     }
//
//
//     @Test
//     public void shouldCreateAdmin() {
//         JSONObject createAdminRequest = new JSONObject();
//         String inTestUniqueAdminLogin = generateRandomLogin();
//         createAdminRequest.put("login", inTestUniqueAdminLogin);
//         createAdminRequest.put("password", "examplePassword");
//         createAdminRequest.put("accessLevel", "ADMIN");
//
//         String uuid = RestAssured.given().
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(createAdminRequest.toJSONString()).when().
//                 post("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/admin").
//                 then().statusCode(200)
//                 .body("login", equalTo(inTestUniqueAdminLogin))
//                 .body("password", equalTo("examplePassword"))
//                 .body("accessLevel", equalTo("ADMIN"))
//                 .extract().path("uuid");
//
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/"+uuid);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"ADMIN\",\"active\":true,\"login\":\""+inTestUniqueAdminLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+uuid+"\"}");
//     }
//
//     @Test
//     public void shouldUpdateAdmin() {
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + adminExampleUUID);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"ADMIN\",\"active\":true,\"login\":\""+uniqueAdminLogin+"\",\"password\":\"examplePasswordAdmin\",\"uuid\":\""+adminExampleUUID+"\"}");
//
//         JSONObject updateAdminRequest = new JSONObject();
//         String inTestUniqueAdminLogin = generateRandomLogin();
//         updateAdminRequest.put("login", inTestUniqueAdminLogin);
//         updateAdminRequest.put("password", "examplePasswordAdminUpdate");
//         updateAdminRequest.put("accessLevel", "ADMIN");
//
//         RestAssured.given().contentType(ContentType.JSON).
//                 header("Content-Type","application/json" ).
//                 header("Accept","application/json" ).
//                 body(updateAdminRequest).
//                 when().put("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/admin/" + adminExampleUUID);
//
//         Response response2 = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + adminExampleUUID);
//
//         assertThat(response2.asString()).isEqualTo("{\"accessLevel\":\"ADMIN\",\"active\":true,\"login\":\""+inTestUniqueAdminLogin+"\",\"password\":\"examplePasswordAdminUpdate\",\"uuid\":\""+adminExampleUUID+"\"}");
//     }
//
//     @Test
//     public void shouldActivateClientWithGivenId() {
//
//         assertThat(exampleUUID).isNotNull();
//
//         Response response = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);
//
//         assertThat(response.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+uniqueClientLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"}");
//
//         RestAssured.given().contentType(ContentType.JSON).
//                 when().put("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/client/activate/" + exampleUUID);
//
//         Response response1 = RestAssured.given().contentType(ContentType.JSON).
//                 when().get("http://localhost:8080/PAS_Rest_API-1.0-SNAPSHOT/api/users/" + exampleUUID);
//
//         assertThat(response1.asString()).isEqualTo("{\"accessLevel\":\"CLIENT\",\"active\":true,\"login\":\""+uniqueClientLogin+"\",\"password\":\"examplePassword\",\"uuid\":\""+exampleUUID+"\",\"address\":\"Pawia 23/25 m 13 Warszawa 00-000\",\"firstName\":\"Jan\",\"lastName\":\"Kowalski\",\"moneySpent\":0.0,\"personalId\":\"12345678910\"}");
//
//     }
//
//     private String generateRandomLogin() {
//         return "exampleUser".concat(UUID.randomUUID().toString());
//     }
//
// }
