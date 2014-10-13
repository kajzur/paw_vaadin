package com.example.trelloplus;

import java.sql.SQLException;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@Theme("trelloplus")

public class TrelloplusUI extends UI {


	public Window win;
	public VerticalLayout mainlayout;
	public VerticalLayout subWindowLayout;
	public GridLayout tableGridLayout;
	public Service service;
	@Override
	protected void init(VaadinRequest request) {
		
		service = new Service();
		VaadinService vs = request.getService();
		
		mainlayout = new VerticalLayout();
		mainlayout.setMargin(true);
        mainlayout.setSpacing(true);
        
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
		
		setContent(mainlayout);
		
		tableGridLayout = new GridLayout(4, 2);
		service.fillTable(tableGridLayout);
		Button button = new Button("Dodaj zadanie");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {				
		        win.center();
		        addWindow(win);
			}
		}); 
		tableGridLayout.setStyleName("bordered-grid");

		mainlayout.addComponent(button);	
		mainlayout.addComponent(tableGridLayout);
		win.setContent(subWindowLayout);

	}

}