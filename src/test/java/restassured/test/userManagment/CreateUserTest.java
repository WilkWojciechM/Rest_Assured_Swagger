package restassured.test.userManagment;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import pojo.user.User;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class CreateUserTest extends TestBase{
    @Test
    public void givenCorrectUserDataWhenCreateUserThenUserIsCreatedTest(){

        User user = new User();
        user.setId(445);
        user.setUsername("Gustavo");
        user.setFirstName("Krzysztof");
        user.setLastName("Folk");
        user.setEmail("kfolk@o2.pl");
        user.setPhone("111 333 444");
        user.setUserStatus(1);


        given().contentType("application/json").body(user)
                .when().post("user")
                .then().statusCode(200);

        given().contentType("application/json").pathParam("username",user.getUsername())
                .when().get("user/{username}")
                .then().statusCode(200);
    }

    @Test
    public void givenCorrectUserDataWhenGetUserThenFindInformationAboutUserUsingJsonPath(){

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("Vendetta");
        user.setEmail("johndoe@gmail.com");
        user.setPhone("666 666 666");

        given().body(user).contentType("application/json")
                .when().post("user")
                .then().statusCode(200);

        JsonPath jsonPathResponse = given().log().method().log().uri()
                .pathParam("username", user.getUsername())
                .when().get("user/{username}")
                .then().statusCode(200)
                .extract().jsonPath();

        String username = jsonPathResponse.getString("username");

        assertEquals(username,user.getUsername(),"");
    }
}

