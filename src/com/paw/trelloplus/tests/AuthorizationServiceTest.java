package com.paw.trelloplus.tests;

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
		String id = as.getUserId(defaultUserName);
		Assert.assertNotNull("Id of "+defaultUserName+" is null!", id);
	}
	@Test
	public void checkUserCredentialsTest(){
		boolean result = as.checkUserCredentials(defaultUserName, defaultUserPassword);
		Assert.assertTrue(result);
	}
	@Test
	public void checkUserCredentialsWhenLoginAndPassAreWrongTest(){
		boolean result = as.checkUserCredentials(defaultUserName+"1", defaultUserPassword+"1");
		Assert.assertFalse(result);
	}
	
	@Test
	public void checkUserCredentialsWhenPassIsWrong(){
		boolean result = as.checkUserCredentials(defaultUserName, defaultUserPassword+"1");
		Assert.assertFalse(result);
	}
	
}
