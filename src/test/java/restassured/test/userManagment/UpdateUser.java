package restassured.test.userManagment;

import org.testng.annotations.Test;
import pojo.user.User;

import static io.restassured.RestAssured.given;

public class UpdateUser extends TestBase{
    @Test
    public void givenCorrectUserDataWhenFirstNameLastNameAreUpdatedThenUserDataIsUpdatedTest(){

        User user = new User();
        user.setId(446);
        user.setUsername("Gustavo2");
        user.setFirstName("Tomasz");
        user.setLastName("Folke");
        user.setEmail("tfolke@wp.pl");
        user.setPhone("222 333 444");
        user.setUserStatus(1);

        given().body(user).contentType("application/json")
                .when().post("user")
                .then().statusCode(200);

        user.setFirstName("Grzegorz");
        user.setLastName("Noga");

        given().body(user).contentType("application/json")
                .pathParam("username",user.getUsername())
                .when().put("user/{username}")
                .then().statusCode(200);

        given().body(user).contentType("application/json")
                .pathParam("username",user.getUsername())
                .when().get("user/{username}")
                .then().statusCode(200);
    }
}
