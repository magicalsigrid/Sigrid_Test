import org.testng.annotations.Test;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.path.json.JsonPath;

import java.util.Base64;

import org.json.simple.JSONObject;


public class AccountsTest {
	
	//********************************************************************//
	//                                                                    //
	//                            Objective:                              //
	//   Validate that the contents of the final JWT token matches the    //
	//    Profile ID from the first response, and that the account state  //
	//                is not automatically approved                       //
	//                                                                    //
	//********************************************************************//
	
	String name = "Sigrid";
	String password = "DogsForever";
	String email = "test@sigrid.com";
	String deviceId = "089d6a60-1b67-4c3a-982e-3fbef301748d";
	
	@Test
	void testAccountAuthentication() {
		
		RestAssured.baseURI="http://lifeserver-staging.tocaboca.com/api/v3";
		RequestSpecification httpRequest=RestAssured.given();
		
		// Create a new user for testing
		JSONObject requestParams = new JSONObject();
		httpRequest.header("Content-Type", "application/json");
		requestParams.put("name", name); 
		requestParams.put("password", password);
		requestParams.put("email", email);
		requestParams.put("deviceId", deviceId);
		
		httpRequest.body(requestParams.toJSONString());
		Response accountsResponse=httpRequest.request(Method.POST,"/accounts");
		String responseBody=accountsResponse.getBody().asString();
		
		String profileId = JsonPath.from(responseBody).get("profileId"); // Get profile id
		
		String jsonString = accountsResponse.getBody().asString();
		String tokenGenerated = JsonPath.from(jsonString).get("token"); // Get generated token to use for the authentication
		Assert.assertNotNull(tokenGenerated);
				
		JSONObject requestAuthParams = new JSONObject();
		httpRequest.header("Authorization","Bearer "+tokenGenerated)
		.header("Content-Type","application/json");
		requestAuthParams.put("identifier", email); 
		requestAuthParams.put("password", password);
		requestAuthParams.put("email", email);
		requestAuthParams.put("deviceId", deviceId);
		
		httpRequest.body(requestAuthParams.toJSONString());
		System.out.println("Authentication input: " +requestAuthParams);		
				
		Response authenticationResponse=httpRequest.request(Method.POST,"/accounts/authentication");
		String authenticationResponseBody=authenticationResponse.getBody().asString();
		System.out.println("Authentication output: " +authenticationResponseBody);
		
		String authTokenGenerated = JsonPath.from(authenticationResponseBody).get("token"); // Get the JWT token
		System.out.println("Authentiction token: " +authTokenGenerated);
		
		String[] chunks = authTokenGenerated.split("\\."); // Split up the JWT token
		Base64.Decoder decoder = Base64.getDecoder(); // Decode the JWT token
		String payload = new String(decoder.decode(chunks[1])); // We only care about the payload part 
		System.out.println("Decoded token: " +payload);
		
		String verifyProfileId = JsonPath.from(payload).get("profileId"); // Get the new profile id and make sure it's the same as before
		Assert.assertEquals(profileId, verifyProfileId);
		
		String accountState = JsonPath.from(payload).get("accountState"); // Get account state
		System.out.println("Account state: " +accountState);
		Assert.assertEquals(accountState, "pending_approval"); // Make sure it's pending
		
		// Delete the created account	
		Response deleteResponse=httpRequest.request(Method.DELETE,"/accounts");
		String deleteBody=deleteResponse.getBody().asString();
		
		int statusCode=deleteResponse.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		

	}
}
