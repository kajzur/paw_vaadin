package com.paw.trelloplus.views;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.paw.trelloplus.service.AuthorizationService;
import com.paw.trelloplus.service.RegistrationService;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class LoginView extends CustomComponent implements View,
		Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7620830730207589253L;
	
	public static final String NAME = "login";

	private final Button signinButton;
	private final TextField user;
	private final PasswordField password;
	private final Button loginButton;
	private final Window win;
	private final VerticalLayout subWindowLayout;

	private RegistrationService registrationService;
	private AuthorizationService authorizationService;

	public LoginView() {
		
		setSizeFull();

		registrationService = new RegistrationService();
		authorizationService = new AuthorizationService();

		user = new TextField("U¿ytkownik:");
		user.setWidth("300px");
		user.setRequired(true);
		user.addValidator(new EmailValidator(
				"Nazwa u¿ytkownika musi byæ adresem e-mail"));
		user.setInvalidAllowed(false);

		password = new PasswordField("Has³o:");
		password.setWidth("300px");
		password.addValidator(new PasswordValidator());
		password.setRequired(true);
		password.setValue("");
		password.setNullRepresentation("");

		loginButton = new Button("Zaloguj", new Button.ClickListener() {

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				if (!user.isValid() || !password.isValid()) {
					return;
				}

				String username = user.getValue();
				String password1 = password.getValue();

				boolean isValid = authorizationService.checkUserCredentials(
						username, password1);

				if (isValid) {

					getSession().setAttribute("user", username);
					getSession().setAttribute("id", authorizationService.getUserId(username));
					getUI().getNavigator().navigateTo(TasksView.NAME);

				} else {

					password.setValue(null);
					password.focus();

				}
			}
		});
		signinButton = new Button("Zarejestruj", new Button.ClickListener() {

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				try {
					win.center();
					getUI().addWindow(win);

				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}

			}
		});

		loginButton.setSizeFull();
		signinButton.setSizeFull();

		HorizontalLayout loginButtonGroupLayout = new HorizontalLayout();
		loginButtonGroupLayout.setSizeFull();
		loginButtonGroupLayout.addComponent(loginButton);
		loginButtonGroupLayout.addComponent(signinButton);

		VerticalLayout fields = new VerticalLayout(user, password,
				loginButtonGroupLayout);
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);

		subWindowLayout = new VerticalLayout();
		subWindowLayout.setMargin(true);
		subWindowLayout.setSpacing(true);

		win = new Window("Rejestracja");
		win.setModal(true);
		win.setContent(subWindowLayout);

		final TextField login = new TextField();
		login.setInputPrompt("nazwa u¿ytkownika");
		login.setSizeFull();

		final TextField password = new TextField();
		password.setInputPrompt("has³o");
		password.setSizeFull();

		Button save = new Button("Zapisz");
		save.setSizeFull();
		save.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				try {
					registrationService.addUser(login.getValue(),
							password.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				} catch (UnsupportedOperationException e) {

					e.printStackTrace();
				} catch (ReadOnlyException e) {

					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {

					e.printStackTrace();
				}
				win.close();
				Notification.show("Dodano");

			}
		});

		Button cancel = new Button("Anuluj");
		cancel.setSizeFull();
		cancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				win.close();

			}
		});

		HorizontalLayout buttonGroupLayout = new HorizontalLayout();
		buttonGroupLayout.setSizeFull();
		buttonGroupLayout.addComponent(save);
		buttonGroupLayout.addComponent(cancel);

		subWindowLayout.addComponent(login);
		subWindowLayout.addComponent(password);
		subWindowLayout.addComponent(buttonGroupLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		user.focus();
	}

	private static final class PasswordValidator extends
			AbstractValidator<String> {

		public PasswordValidator() {
			super("The password provided is not valid");
		}

		@Override
		protected boolean isValidValue(String value) {

			if (value != null
					&& (value.length() < 8 || !value.matches(".*\\d.*"))) {
				return false;
			}
			return true;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}
	}

	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

	}
}