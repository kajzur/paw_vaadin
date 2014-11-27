package com.paw.trelloplus.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.MenuBarElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

public class UITest extends TestBenchTestCase  {
	
	String defaultUserName = "test@test.com";
	String defaultUserPassword="passw0rd";
	@Rule
	public ScreenshotOnFailureRule screenshotOnFailureRule =
	        new ScreenshotOnFailureRule(this, true);
	
	@Before
	public void setUp() throws Exception {
	    setDriver(new ChromeDriver());
	}
	
	@Test
	public void testFailedLoginBehaviour() throws Exception {
	    getDriver().get("http://localhost:8080/TrelloPluss/");
	    
	    ButtonElement zalogujButton = $(ButtonElement.class).caption("Zaloguj").first();
	    TextFieldElement uytkownikTextField = $(TextFieldElement.class).caption("U¿ytkownik:").first();
	    PasswordFieldElement hasoPasswordField = $(PasswordFieldElement.class).caption("Has³o:").first();
	    
	    uytkownikTextField.setValue("jestem zlym mailem");
	    hasoPasswordField.setValue("jestem jakims haslem");
	    zalogujButton.click();
	    String css = uytkownikTextField.getAttribute("class");
	    Assert.assertTrue( css.contains("v-textfield-error") );
	    String css2 = hasoPasswordField.getAttribute("class");
	    Assert.assertTrue( css.contains("v-textfield-error") );
	    
	}
	
	@Test
	public void testSuccessLoginBehaviour() throws Exception {
	    getDriver().get("http://localhost:8080/TrelloPluss/");
	    
	    ButtonElement zalogujButton = $(ButtonElement.class).caption("Zaloguj").first();
	    TextFieldElement uytkownikTextField = $(TextFieldElement.class).caption("U¿ytkownik:").first();
	    PasswordFieldElement hasoPasswordField = $(PasswordFieldElement.class).caption("Has³o:").first();
	    
	    uytkownikTextField.setValue(defaultUserName);
	    hasoPasswordField.setValue(defaultUserPassword);
	    zalogujButton.click();
	    Assert.assertTrue($(MenuBarElement.class).exists());
	    
	}
}
