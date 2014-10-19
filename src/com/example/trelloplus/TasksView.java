package com.example.trelloplus;

import java.sql.SQLException;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("trelloplus.scss")
public class TasksView extends VerticalLayout implements View {
	
	public static final String NAME = "main";
	public Window win;
	public VerticalLayout mainlayout;
	public VerticalLayout subWindowLayout;
	public GridLayout tableGridLayout;
	public Service service;
	public Label text;

	
	public TasksView()
	{
		
		service = new Service();
		
		
		text = new Label();

		 Button logout = new Button("Logout", new Button.ClickListener() {

		        
				@Override
				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				
		            getSession().setAttribute("user", null);
		            getUI().getNavigator().navigateTo(NAME);
					
				}
		    });
        
		win = new Window("Dodawanie zadania");
		win.setModal(true);
		win.setContent(subWindowLayout);

		
		subWindowLayout = new VerticalLayout();
		subWindowLayout.setMargin(true);
        subWindowLayout.setSpacing(true);
        
        final TextField title = new TextField();
        title.setSizeFull();
        
		final TextArea descriptionArea = new TextArea();
		descriptionArea.setSizeFull();
		
		Button save = new Button("Save");
		save.setSizeFull();
		save.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				try {
					service.addTask(title.getValue(), descriptionArea.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				win.close();
				Task t = new Task(title.getValue(), descriptionArea.getValue());
				tableGridLayout.addComponent(t);
				Notification.show("Dodano");
				
			}
		});
		
		Button cancel = new Button("Cancel");
		cancel.setSizeFull();
		
		HorizontalLayout buttonGroupLayout = new HorizontalLayout();
		buttonGroupLayout.setSizeFull();
		buttonGroupLayout.addComponent(save);
		buttonGroupLayout.addComponent(cancel);
		
		
		subWindowLayout.addComponent(title);
		subWindowLayout.addComponent(descriptionArea);
		subWindowLayout.addComponent(buttonGroupLayout);
		
		
		tableGridLayout = new GridLayout(4, 2);
		service.fillTable(tableGridLayout);
		Button button = new Button("Dodaj zadanie");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {				
		        win.center();
		        getUI().addWindow(win);
			}
		}); 
		tableGridLayout.setStyleName("bordered-grid");

		this.addComponent(text);
		this.addComponent(button);
		this.addComponent(logout);
		this.addComponent(tableGridLayout);
		win.setContent(subWindowLayout);
	}
	
	

	@Override
	public void enter(ViewChangeEvent event) {
		
        String username = String.valueOf(getSession().getAttribute("user"));
        text.setValue("Hello " + username);
	}

}
