package com.example.trelloplus;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class List extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -854281813374146888L;
	private String id_list;
	private String id_board;
	private String name;
	private Window windowCreateTask;
	private VerticalLayout subWindowForTask;
	private TaskService taskService;

	public List(String title) {
		taskService = new TaskService();
		VerticalLayout vt = new VerticalLayout();
		vt.addComponent(new Label(title));
		vt.setStyleName("list-header");
		setStyleName("list");
		addComponent(vt);
		Button addNewListBtn = new Button("Dodaj zadanie");

		addNewListBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				List currentList = (List) event.getButton().getParent();
				TasksView.currentList = currentList.getId_list();
				windowCreateTask.center();
				getUI().addWindow(windowCreateTask);

			}
		});
		addComponent(addNewListBtn);
		
		subWindowForTask = new VerticalLayout();
		subWindowForTask.setMargin(true);
		subWindowForTask.setSpacing(true);
		
		windowCreateTask = new Window("Dodawanie zadania");
		windowCreateTask.setModal(true);
		windowCreateTask.setContent(subWindowForTask);
		
		prepareWindow();

	}
	
	private void prepareWindow(){

		
		
		final TextField title = new TextField();
		title.setInputPrompt("Podaj tytul");
		title.setSizeFull();

		final TextArea descriptionArea = new TextArea();
		descriptionArea.setInputPrompt("Podaj opis");
		descriptionArea.setSizeFull();
		
		Button save = new Button("Zapisz");
		save.setSizeFull();
		save.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Task newTask = null;
				try {
					newTask = taskService.addTask(getId_list(),
							title.getValue(), descriptionArea.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				windowCreateTask.close();

				List.this.addComponent(newTask);
				title.setValue("");
				descriptionArea.setValue("");

			}
		});

		Button cancel = new Button("Anuluj");
		cancel.setSizeFull();
		cancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				windowCreateTask.close();
			}
		});
		HorizontalLayout buttonGroupLayout = new HorizontalLayout();
		buttonGroupLayout.setSizeFull();
		buttonGroupLayout.addComponent(save);
		buttonGroupLayout.addComponent(cancel);
		
		subWindowForTask.addComponent(title);
		subWindowForTask.addComponent(descriptionArea);
		subWindowForTask.addComponent(buttonGroupLayout);
		
		windowCreateTask.setContent(subWindowForTask);
	}

	public String getId_board() {
		return id_board;
	}

	public void setId_board(String id_board) {
		this.id_board = id_board;
	}

	public String getId_list() {
		return id_list;
	}

	public void setId_list(String id_list) {
		this.id_list = id_list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
