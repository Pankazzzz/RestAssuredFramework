package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

//This class created for crud operations.
public class UserEndpoints {
	
	public static Response createUser(User payload)
	{
		
		Response response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(payload)
		
		.when()
		.post(Routes.post_url);
		
		return response;
		
	}
	
	
	public static Response readUser(String username)
	{
		System.out.println(username+" in class");
		Response response = given()
				.pathParam("username", username)
		
		.when()
		.get(Routes.get_url);
		
		return response;
	}
	
	
	public static Response updateUser(String username,User payload)
	{
		
		Response response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("username", username)
				.body(payload)
		
		.when()
		.put(Routes.update_url);
		
		return response;
		
	}
	
	public static Response deleteUser(String username) {
	    String finalUrl = Routes.delete_url.replace("{username}", username);
	    System.out.println("Deleting User at URL: " + finalUrl);

	    Response response = given()
	            .pathParam("username", username)
	            .when()
	            .delete(Routes.delete_url);
	    
	    response.then().log().all();  // Log response for debugging
	    return response;
	}

}
