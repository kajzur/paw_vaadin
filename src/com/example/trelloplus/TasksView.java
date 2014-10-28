package com.example.trelloplus;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("trelloplus.scss")
public class TasksView extends VerticalLayout implements View {

	public static final String NAME = "main";
	public Window win;
	public Window createNewListWin;
	public VerticalLayout mainlayout;
	public VerticalLayout subWindowLayout;
	public VerticalLayout subCreateNewListLayout;
	public HorizontalLayout tableLayout;
	public Service service;
	public Label text;

	public TasksView() {

		service = new Service();

		text = new Label();

		Button logout = new Button("Logout", new Button.ClickListener() {

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				getSession().setAttribute("user", null);
				getUI().getNavigator().navigateTo(NAME);

			}
		});

		// Okienko do dodawania zadania
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
					service.addTask(title.getValue(),
							descriptionArea.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				win.close();
				Task t = new Task(title.getValue(), descriptionArea.getValue());
				tableLayout.addComponent(t);
				Notification.show("Dodano");

			}
		});

		Button cancel = new Button("Cancel");
		cancel.setSizeFull();
		cancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				win.close();
			}
		});

		HorizontalLayout buttonGroupLayout = new HorizontalLayout();
		buttonGroupLayout.setSizeFull();
		buttonGroupLayout.addComponent(save);
		buttonGroupLayout.addComponent(cancel);

		subWindowLayout.addComponent(title);
		subWindowLayout.addComponent(descriptionArea);
		subWindowLayout.addComponent(buttonGroupLayout);

		// okienko do tworzenia nowej listy

		createNewListWin = new Window("Tytul");
		createNewListWin.setModal(true);
		createNewListWin.setContent(subCreateNewListLayout);

		subCreateNewListLayout = new VerticalLayout();
		subCreateNewListLayout.setMargin(true);
		subCreateNewListLayout.setSpacing(true);

		final TextField titleNewList = new TextField();
		titleNewList.setSizeFull();

		Button createList = new Button("Dodaj Liste");
		createList.setSizeFull();
		createList.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					service.addList(1, titleNewList.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				createNewListWin.close();

				List newList = new List(title.getValue());
				tableLayout.addComponent(newList);
				Notification.show("dodano liste");

				// service.fillTableList(tableLayout);

			}
		});

		subCreateNewListLayout.addComponent(titleNewList);
		subCreateNewListLayout.addComponent(createList);

		/*
		 * tableGridLayout = new GridLayout(4, 2);
		 * service.fillTable(tableGridLayout);
		 */

		tableLayout = new HorizontalLayout();
		// service.fillTable(tableGridLayout);
		
		tableLayout.setSpacing(true);
		

		Button addNewTaskBtn = new Button("Dodaj zadanie");
		addNewTaskBtn.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				win.center();
				getUI().addWindow(win);
			}
		});
		
		ArrayList<List> allLists = service.fillTableList();
		
		for (List b : allLists) {
			// b.setWidth(widthPerList + "%");

			
			 // b.setWidth("30%"); b.setw
			 

			Button addNewListBtn = new Button("Dodaj zadanie");
			addNewListBtn.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					win.center();
					getUI().addWindow(win);

				}
			});
			
			//gridLayout.addComponent(b);
			
			VerticalLayout buttonMainGroupLayout = new VerticalLayout();
			buttonMainGroupLayout.setStyleName("list");
			buttonMainGroupLayout.setSizeFull();
			buttonMainGroupLayout.setSpacing(true);
			
			buttonMainGroupLayout.addComponent(b);
			buttonMainGroupLayout.addComponent(addNewListBtn);
			tableLayout.setStyleName("list");
			tableLayout.addComponent(buttonMainGroupLayout);
			//gridLayout.setExpandRatio(b, 1f);
			
			tableLayout.setExpandRatio(buttonMainGroupLayout, 1f);
			
		}

		Button addNewListBtn = new Button("Dodaj nowa liste");
		addNewListBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				createNewListWin.center();
				getUI().addWindow(createNewListWin);

			}
		});

		tableLayout.setStyleName("bordered-grid");

		this.setSpacing(true);
		
		HorizontalLayout buttonMainGroupLayout = new HorizontalLayout();
		buttonMainGroupLayout.setSizeFull();
		buttonMainGroupLayout.setSpacing(true);
		
		buttonMainGroupLayout.addComponent(addNewTaskBtn);
		buttonMainGroupLayout.addComponent(addNewListBtn);
		buttonMainGroupLayout.addComponent(logout);
		
		this.addComponent(text);
		this.addComponent(buttonMainGroupLayout);
		/*this.addComponent(button);
		this.addComponent(logout);
		this.addComponent(addNewListBtn);*/
		this.addComponent(tableLayout);
		setExpandRatio(tableLayout, 1f);
		win.setContent(subWindowLayout);

		createNewListWin.setContent(subCreateNewListLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		String username = String.valueOf(getSession().getAttribute("user"));
		text.setValue("Hello " + username);
	}

}
