package restassured.test.http.methods;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class BasicHttpMethodsTest {

    @Test
    public void givenPetWhenPostPetThenPetIsCreatedTest() {

        String pet = "{\n" +
                "  \"id\": 123,\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"dogs\"\n" +
                "  },\n" +
                "  \"name\": \"Burek\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"http://photos.com/dog1.jpg\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"dogs-category\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";
//Po metodzie given() w ramach metody body() przekazujemy obiekt pet.
//log().all() - logowanie żądania/odpowiedzi
//ContentType() dodaje nagłówek o wartości "application/json". Nagłówek konieczny jest zawsze podczas używania POST
//when() metodą post() wysyłamy żadanie POST
//asercja oczekujemy kodu statusowego 200
        given().log().all().body(pet).contentType("application/json")
                .when().post("https://swaggerpetstore.przyklady.javastart.pl/v2/pet")
                .then().log().all().statusCode(200);

//metoda put zmiana imienia
        pet = "{\n" +
                "  \"id\": 123,\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"dogs\"\n" +
                "  },\n" +
                "  \"name\": \"Zula\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"http://photos.com/dog1.jpg\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"dogs-category\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        given().log().all().body(pet).contentType("application/json")
                .when().put("https://swaggerpetstore.przyklady.javastart.pl/v2/pet")
                .then().log().all().statusCode(200);

    }

    @Test
    public void givenExistingPetIdWhenDeletingPetThenIsDeletedTest(){

        String pet = "{\n" +
                "  \"id\": 425,\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"dogs\"\n" +
                "  },\n" +
                "  \"name\": \"Burek\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"http://photos.com/dog1.jpg\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"dogs-category\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        given().log().all().body(pet).contentType("application/json")
                .when().post("https://swaggerpetstore.przyklady.javastart.pl/v2/pet")
                .then().log().all().statusCode(200);

        given().log().all().contentType("application/json")
                .pathParam("petId",425)
                .when().delete("https://swaggerpetstore.przyklady.javastart.pl/v2/pet/{petId}")
                .then().log().all().statusCode(200);

    }

    @Test
    public void givenCorrectUserDataWhenCreateUserThenUserIsCreatedTest(){

        String user = "{\n" +
                "  \"id\": 445,\n" +
                "  \"username\": \"firstuser\",\n" +
                "  \"firstName\": \"Krzysztof\",\n" +
                "  \"lastName\": \"Kowalski\",\n" +
                "  \"email\": \"krzysztof@test.com\",\n" +
                "  \"password\": \"password\",\n" +
                "  \"phone\": \"+123456789\",\n" +
                "  \"userStatus\": 1\n" +
                "}";

        given().log().all().body(user).contentType("application/json")
                .when().post("https://swaggerpetstore.przyklady.javastart.pl/v2/user")
                .then().log().all().statusCode(200);

        given().log().all()
                .contentType("application/json")
                .pathParam("username","firstuser")
                .when().get("https://swaggerpetstore.przyklady.javastart.pl/v2/user/{username}")
                .then().log().all().statusCode(200);
    }

    @Test
    public void givenExistingPetIdWhenGetPetThenReturnPetTest(){
        given().log().method().log().uri()
                .pathParam("petId",1)
                .when().get("https://swaggerpetstore.przyklady.javastart.pl/v2/pet/{petId}")
                .then().log().all().statusCode(200);

    }
}
