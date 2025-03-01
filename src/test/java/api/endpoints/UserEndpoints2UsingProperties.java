package api.endpoints;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ResourceBundle;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

//This class created for crud operations.
public class UserEndpoints2UsingProperties {
	
	
	static ResourceBundle getURL() {
		// TODO Auto-generated method stub
		
		ResourceBundle routes = ResourceBundle.getBundle("Routes");
		return routes;

	}
	
	public static Response createUser(User payload)
	{
		
		String post_urlString = getURL().getString("post_url");
		
		Response response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(payload)
		
		.when()
		.post(post_urlString);
		
		return response;
		
	}
	
	
	public static Response readUser(String username)
	{
		System.out.println(username+" in class");
		Response response = given()
				.pathParam("username", username)
		
		.when()
		.get(getURL().getString("get_url"));
		
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
		.put(getURL().getString("update_url"));
		
		return response;
		
	}
	
	public static Response deleteUser(String username) {
	    String finalUrl = Routes.delete_url.replace("{username}", username);
	    System.out.println("Deleting User at URL: " + finalUrl);

	    Response response = given()
	            .pathParam("username", username)
	            .when()
	            .delete(getURL().getString("delete_url"));
	    
	    response.then().log().all();  // Log response for debugging
	    return response;
	}

}
