package com.example.trelloplus;

import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@Theme("trelloplus")
public class TrelloplusUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = TrelloplusUI.class)
	public static class Servlet extends VaadinServlet {
	}
	public Window win;
	public VerticalLayout mainlayout;
	public VerticalLayout subWindowLayout;
	public GridLayout tableGridLayout;
	@Override
	protected void init(VaadinRequest request) {
		mainlayout = new VerticalLayout();
		win = new Window("Dodawanie zadania");
		subWindowLayout = new VerticalLayout();
		// options..
		win.setModal(true);
		mainlayout.setMargin(true);
        mainlayout.setSpacing(true);
        subWindowLayout.setMargin(true);
        subWindowLayout.setSpacing(true);
        
        TextField tx = new TextField();
        tx.setSizeFull();
		mainlayout.setMargin(true);
		subWindowLayout.addComponent(tx);
		
		win.setContent(subWindowLayout);
		setContent(mainlayout);
		
		tableGridLayout = new GridLayout(4, 2);
		Button button = new Button("Dodaj zadanie");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {				
		        win.center();
		        addWindow(win);
			}
		}); 
		mainlayout.addComponent(button);
		
		mainlayout.addComponent(tableGridLayout);
		tableGridLayout.setStyleName("bordered-grid");
	}

}