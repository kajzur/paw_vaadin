package com.paw.trelloplus.components;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paw.trelloplus.service.TaskService;
import com.paw.trelloplus.utils.ListDropHandler;
import com.paw.trelloplus.views.TasksView;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.drophandlers.DefaultVerticalLayoutDropHandler;
import fi.jasoft.dragdroplayouts.interfaces.DragFilter;

public class List extends VerticalLayout  {

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
	private DDVerticalLayout taskContainer;
	private final static Logger logger =Logger.getLogger(List.class.getName());
	private static final float EQUAL_VERTICAL_RATIO = 0.3f;
	@Override
	public void addComponent(Component c) {
		if(c instanceof Task){
//			DragAndDropWrapper dd = new DragAndDropWrapper(c);
//			dd.setDragStartMode(DragStartMode.COMPONENT);
//			dd.setData(c);
			taskContainer.addComponent(c);
		}
		else
		super.addComponent(c);
	}
	
	public void setDrop(DropHandler dh){

		taskContainer.setDropHandler(dh);
	}
	
	public DDVerticalLayout getTaskContainer(){
		return taskContainer;
	}
	
	public List(String id_list, String id_board, String title) {
		this.id_board = id_board;
		this.id_list = id_list;
		this.name = title;
		taskContainer=new DDVerticalLayout();
		taskContainer.setStyleName("tasks");
		taskService = new TaskService();
		taskContainer.setComponentVerticalDropRatio(EQUAL_VERTICAL_RATIO);
		taskContainer.setDragMode(LayoutDragMode.CLONE);
		taskContainer.setDragFilter(new DragFilter() {
			public boolean isDraggable(Component component) {
				return component instanceof Task;
			}
		});

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
		addComponent(taskContainer);
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
							title.getValue(), descriptionArea.getValue(), "0");
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
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof List)) return false;
		List o = (List)obj;
		return this.getId_list().equals(o.getId_list());
	}

}
