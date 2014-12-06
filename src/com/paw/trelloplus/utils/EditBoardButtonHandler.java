package com.paw.trelloplus.utils;

import java.sql.SQLException;

import com.paw.trelloplus.service.BoardService;
import com.paw.trelloplus.views.TasksView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EditBoardButtonHandler extends Window implements ClickListener {

	private String oldNameToFillForm;
	private TasksView viewToCreateWindow;
	private VerticalLayout mainWindowLayout;
	private TextField name;
	private Button save;
	private BoardService tService;
	public EditBoardButtonHandler(String tableName, TasksView tasksView) {
		super("Edycja nazwy tabeli");
		tService = new BoardService();
		setModal(true);
		
		oldNameToFillForm=tableName;
		viewToCreateWindow=tasksView;
		mainWindowLayout = new VerticalLayout();
		mainWindowLayout.setSpacing(true);
		mainWindowLayout.setMargin(true);
		name = new TextField();
		name.setSizeFull();
		name.setValue(oldNameToFillForm);
		mainWindowLayout.addComponent(name);
		save = new Button("Zapisz");
		save.setSizeFull();
		
		save.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				String currentTableId = TasksView.ID_BOARD;
				String newVal = name.getValue();
				boolean result = tService.setNameById(newVal, currentTableId);
				if(result){
					EditBoardButtonHandler.this.close();
					viewToCreateWindow.setCurrentTableName(newVal);
					try {
						viewToCreateWindow.refreshMenu(); 
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					Notification.show("Error!!");
			}
		});
		mainWindowLayout.addComponent(save);
		this.setHeightUndefined();
		this.setWidthUndefined();
		setContent(mainWindowLayout);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		setContent(mainWindowLayout);
		viewToCreateWindow.getUI().addWindow(this);

	}

}
