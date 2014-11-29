package com.paw.trelloplus.tests;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.paw.trelloplus.service.AuthorizationService;

public class AuthorizationServiceTest {

	private AuthorizationService as;
	String defaultUserName = "test@test.com";
	String defaultUserPassword="passw0rd";
	public AuthorizationServiceTest() {
		as = new AuthorizationService();
	}
	@Test
	public void setUserIdTest(){
		String id;
		try {
			id = as.getUserId(defaultUserName);
			Assert.assertNotNull("Id of "+defaultUserName+" is null!", id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void checkUserCredentialsTest(){
		boolean result;
		try {
			result = as.checkUserCredentials(defaultUserName, defaultUserPassword);
			Assert.assertTrue(result);
		} catch (NoSuchAlgorithmException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void checkUserCredentialsWhenLoginAndPassAreWrongTest(){
		boolean result;
		try {
			result = as.checkUserCredentials(defaultUserName+"1", defaultUserPassword+"1");
			Assert.assertFalse(result);
		} catch (NoSuchAlgorithmException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void checkUserCredentialsWhenPassIsWrong(){
		boolean result;
		try {
			result = as.checkUserCredentials(defaultUserName, defaultUserPassword+"1");
			Assert.assertFalse(result);
		} catch (NoSuchAlgorithmException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
