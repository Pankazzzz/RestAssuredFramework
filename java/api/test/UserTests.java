package api.test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.UserEndpoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
    
    Faker faker;
    User userPayload;
    private String fixedUsername;  // Store username in a variable

    @BeforeClass
    public void setUpData() {
        faker = new Faker();
        userPayload = new User();

        userPayload.setId(faker.idNumber().hashCode());
        fixedUsername = faker.name().username();  // Store fixed username
        userPayload.setUsername(fixedUsername);
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password(5, 10));
        userPayload.setPhone(faker.phoneNumber().cellPhone());
        System.out.println("Generated Username: " + fixedUsername);
    }

    @Test(priority = 1)
    public void testPostUser() {
        Response response = UserEndpoints.createUser(userPayload);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2, dependsOnMethods = {"testPostUser"})
    public void testGetUser() {
        Response response = retryGetUser(fixedUsername);  // Retry GET request
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3, dependsOnMethods = {"testGetUser"})
    public void testUpdateUser() {
        // Update Data
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());

        Response response = UserEndpoints.updateUser(fixedUsername, userPayload);
        response.then().log().body().statusCode(200); //Chai assertion
        
        response = retryGetUser(fixedUsername);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4, dependsOnMethods = {"testUpdateUser"})
    public void testDeleteUser() {
        Response response = retryDeleteUser(fixedUsername); // Use retry logic
        response.then().log().all();

        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == 200 || statusCode == 204, 
                          "Expected 200 or 204 but got: " + statusCode);
    }
    
    
    // Retry Mechanism for GET Requests
    private Response retryGetUser(String username) {
        int attempts = 0;
        Response response = null;
        
        while (attempts < 5) {  // Try 5 times
            response = UserEndpoints.readUser(username);
            if (response.getStatusCode() == 200) {
                return response;
            }
            attempts++;
            System.out.println("Retrying GET request... Attempt: " + attempts);
            try {
                Thread.sleep(2000);  // Wait 2 seconds before retrying
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return response;  // Return the last response (if still failed)
    }
    
    // Retry Mechanism for DELETE Requests (✅ Fixed)
    private Response retryDeleteUser(String username) {
        int attempts = 0;
        Response response = null;

        while (attempts < 5) {  // Try 5 times
            response = UserEndpoints.deleteUser(username);
            int statusCode = response.getStatusCode();

            // ✅ Stop retrying if DELETE is successful (200 or 204)
            if (statusCode == 200 || statusCode == 204) {
                return response;
            }

            attempts++;
            System.out.println("Retrying DELETE request... Attempt: " + attempts);
            try {
                Thread.sleep(2000);  // Wait 2 seconds before retrying
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return response;  // Return the last response
    }
}
