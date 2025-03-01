package api.test;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.UserEndpoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DataDrivenTest {
    
    Faker faker;
    User userPayload;
    private String fixedUsername;  // Store username in a variable
    public Logger logger;
    

    @Test(priority = 1,dataProvider = "Data",dataProviderClass = DataProviders.class)
    public void testPostUser(String userId,String userName,String fname,String lastName,String userEmail,String password,String phone) {
    	
        logger = LogManager.getLogger(this.getClass());
    	
        logger.info("-------------Create User using Excel --------------------");
    	
    	 userPayload = new User();
         userPayload.setId(Integer.parseInt(userId));
         userPayload.setUsername(userName);
         userPayload.setFirstName(fname);
         userPayload.setLastName(lastName);
         userPayload.setEmail(userEmail);
         userPayload.setPassword(password);
         userPayload.setPhone(phone); 
         
         Response response= UserEndpoints.createUser(userPayload);
         Assert.assertEquals(response.getStatusCode(), 200);
    }
    
    @Test(priority = 2,dataProvider = "UserNames",dataProviderClass = DataProviders.class,dependsOnMethods = {"testPostUser"})
    public void testPostUser(String userName) {
    	
        logger.info("-------------Delete User using Excel --------------------");
        
         Response response= retryDeleteUser(userName);
         Assert.assertEquals(response.getStatusCode(), 200);
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
