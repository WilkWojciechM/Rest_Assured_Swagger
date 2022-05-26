package restassured.test.http.methods;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.pet.Category;
import pojo.pet.Pet;
import pojo.pet.Tag;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class PetManagementUsingPojoTests {

    @BeforeClass
    public void setupConfig(){
        RestAssured.baseURI =  "https://swaggerpetstore.przyklady.javastart.pl";
        RestAssured.basePath = "v2";
        //zamiast log().all() w żądaniu i odpowiedzi
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        //Wpływają na wielowątkowość
        //zamiast contentType("application/json)
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        //zamiast then() z asercja o kodzie statusowym
        RestAssured.responseSpecification = new ResponseSpecBuilder().expectStatusCode(200).build();
    }

    @Test
    public void givenPetWhenPostPetThenPetIsCreatedResponsePractiseTest(){

        Category category = new Category();
        category.setId(1);
        category.setName("dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("dogs-category");

        Pet pet = new Pet();
        pet.setId(123);
        pet.setCategory(category);
        pet.setPhotoUrls(Collections.singletonList("http://photos.com/dog1.jpg"));
        pet.setTags(Collections.singletonList(tag));
        pet.setStatus("available");

        Response response = given().body(pet)
                .when().post("pet")
                .then().extract().response();

        int statusCode = response.getStatusCode();
        String statusLine = response.getStatusLine();
        Headers responseHeaders = response.getHeaders();
        Map<String,String> cookies = response.getCookies();

        assertEquals(statusLine, "HTTP/1.1 200 OK");
        assertEquals(statusCode, 200);
        assertNotNull(responseHeaders.get("Date"));
        assertEquals(responseHeaders.get("Content-Type").getValue(), "application/json");
        assertEquals(responseHeaders.get("Server").getValue(), "openresty");
        assertTrue("Cookies are empty", cookies.isEmpty());
    }

    @Test
    public void givenPetIsCreatedThenUpdateNameTest() {

        Category category = new Category();
        category.setId(1);
        category.setName("dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("dogs-category");

        Pet pet = new Pet();
        pet.setId(123);
        pet.setCategory(category);
        pet.setPhotoUrls(Collections.singletonList("http://photos.com/dog1.jpg"));
        pet.setTags(Collections.singletonList(tag));
        pet.setName("Azor");
        pet.setStatus("available");

        given().body(pet)
                .when().post("pet");


        pet.setName("Gus");

        given().body(pet)
                .when().put("pet");

    }

    @Test
    public void givenExistingPetIdWhenDeletingPetThenIsDeletedTest(){


        Category category = new Category();
        category.setId(1);
        category.setName("dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("dogs-category");

        Pet pet = new Pet();
        pet.setId(425);
        pet.setCategory(category);
        pet.setPhotoUrls(Collections.singletonList("http://photos.com/dog1.jpg"));
        pet.setTags(Collections.singletonList(tag));
        pet.setStatus("available");


        given().body(pet)
                .when().post("pet");


        given().pathParam("petId",pet.getId())
                .when().delete("pet/{petId}");
    }

    @Test
    public void givenExistingPetWithStatusSoldWhenGetPetWithSoldStatusThenPetWithStatusIsReturnedTest(){

        Category category = new Category();
        category.setId(399);
        category.setName("dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("dogs-category");

        Pet pet = new Pet();
        pet.setId(777);
        pet.setCategory(category);
        pet.setPhotoUrls(Collections.singletonList("http://photos.com/dog1.jpg"));
        pet.setTags(Collections.singletonList(tag));
        pet.setStatus("sold");

        given().body(pet)
                .when().post("pet");
//z użyciem query parameters
        Pet[] pets = given().body(pet).contentType("application/json")
                .queryParam("status", "sold")
                .when().get("pet/findByStatus")
                .then().statusCode(200).extract().as(Pet[].class);

        assertTrue("List of pets", Arrays.asList(pets).size() > 0);
    }

    @Test
    public void givenExistingPetWithStatusSoldWhenGetPetWithSoldStatusThenPetWithStatusIsReturnedUsingJsonPathTest(){

        Category category = new Category();
        category.setId(400);
        category.setName("dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("dogs-category");

        Pet pet = new Pet();
        pet.setId(778);
        pet.setCategory(category);
        pet.setPhotoUrls(Collections.singletonList("http://photos.com/dog1.jpg"));
        pet.setTags(Collections.singletonList(tag));
        pet.setStatus("sold");

        given().body(pet)
                .when().post("pet");

        //z użyciem JsonPath
        List<Pet> petList = given().body(pet)
                .queryParam("status","sold")
                .when().get("pet/findByStatus")
                .then().statusCode(200).extract().jsonPath().getList("",Pet.class);

        assertTrue("List of pets",petList.size() > 0 );
    }

    @Test
    public void givenPetWhenPostPetThenPetIsCreatedTest(){
        Category category = new Category();
        category.setId(1);
        category.setName("dogs");

        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("dogs-category");

        Pet pet = new Pet();
        pet.setName("Burek");
        pet.setId(123);
        pet.setCategory(category);
        pet.setPhotoUrls(Collections.singletonList("http://photos.com/dog1.jpg"));
        pet.setTags(Collections.singletonList(tag));
        pet.setStatus("available");

        given().body(pet)
                .when().post("pet");

        JsonPath jsonPathResponse = given().log().method().log().uri()
                .pathParam("petId", pet.getId())
                .when().get("pet/{petId}")
                .then().statusCode(200)
                .extract().jsonPath();

        String petName = jsonPathResponse.getString(pet.getName());
        String actualCategoryName = jsonPathResponse.getString(category.getName());
        String tagName = jsonPathResponse.getString(tag.getName());

        assertEquals(petName, pet.getName(), "Burek");
        assertEquals(actualCategoryName, pet.getCategory().getName(), "dogs");
        assertEquals(tagName, pet.getTags().get(0).getName(), "dogs-category");
    }
}
