package com.example.trelloplus;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("trelloplus.scss")
public class TasksView extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = -473387470476587223L;
	public static String currentList = "";
	public static final String NAME = "main";
	public Window windowCreateTask;
	public Window windowCreateList;
	public Window windowCreateBoard;
	public VerticalLayout subWindowForTask;
	public VerticalLayout subWindowForBoard;
	public VerticalLayout subWindowForList;
	public HorizontalLayout mainLayout;

	public Task task;
	public ArrayList<List> allLists;

	public TaskService taskService;
	public ListService listService;

	public TasksView() {

		taskService = new TaskService();
		listService = new ListService();

		Button addNewBoardBtn = new Button("Dodaj tablice");
		addNewBoardBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				windowCreateBoard.center();
				getUI().addWindow(windowCreateBoard);
			}
		});

		Button addNewListBtn = new Button("Dodaj nowa liste");
		addNewListBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				windowCreateList.center();
				getUI().addWindow(windowCreateList);

			}
		});

		Button logout = new Button("Wyloguj", new Button.ClickListener() {

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {

				getSession().setAttribute("user", null);
				getUI().getNavigator().navigateTo(NAME);

			}
		});

		// Okienko do dodawania zadania
		windowCreateTask = new Window("Dodawanie zadania");
		windowCreateTask.setModal(true);
		windowCreateTask.setContent(subWindowForTask);

		subWindowForTask = new VerticalLayout();
		subWindowForTask.setMargin(true);
		subWindowForTask.setSpacing(true);

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

				try {
					taskService.addTask(TasksView.currentList,
							title.getValue(), descriptionArea.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				windowCreateTask.close();

				Notification.show("Dodano");
				ArrayList<List> listsFromCache = listService.getAllList();
				for (List l : listsFromCache)
					if (l.getId_list().equals(TasksView.currentList)) {
						l.addComponent(task);
					}

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

		Button createBoardBtn = new Button("Utworz tablice");
		createBoardBtn.setSizeFull();
		createBoardBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				windowCreateBoard.close();

			}
		});

		HorizontalLayout buttonGroupLayout = new HorizontalLayout();
		buttonGroupLayout.setSizeFull();
		buttonGroupLayout.addComponent(save);
		buttonGroupLayout.addComponent(cancel);

		subWindowForTask.addComponent(title);
		subWindowForTask.addComponent(descriptionArea);
		subWindowForTask.addComponent(buttonGroupLayout);

		// Okienko do dodawania tablicy
		windowCreateBoard = new Window("Dodawanie tablicy");
		windowCreateBoard.setModal(true);
		windowCreateBoard.setContent(subWindowForBoard);

		subWindowForBoard = new VerticalLayout();
		subWindowForBoard.setMargin(true);
		subWindowForBoard.setSpacing(true);

		final TextField titleBoard = new TextField();
		titleBoard.setInputPrompt("Podaj tytul tablicy");
		titleBoard.setSizeFull();

		subWindowForBoard.addComponent(titleBoard);
		subWindowForBoard.addComponent(createBoardBtn);

		// okienko do tworzenia nowej listy
		windowCreateList = new Window("Tytul");
		windowCreateList.setModal(true);
		windowCreateList.setContent(subWindowForList);

		subWindowForList = new VerticalLayout();
		subWindowForList.setMargin(true);
		subWindowForList.setSpacing(true);

		final TextField titleNewList = new TextField();
		titleNewList.setSizeFull();

		Button createList = new Button("Dodaj Liste");
		createList.setSizeFull();
		createList.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					listService.addList(1, titleNewList.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				windowCreateList.close();

				List newList = new List(title.getValue());
				mainLayout.setStyleName("list");
				mainLayout.addComponent(newList);
				Notification.show("dodano liste");
			}
		});

		subWindowForList.addComponent(titleNewList);
		subWindowForList.addComponent(createList);

		mainLayout = new HorizontalLayout();
		mainLayout.setSpacing(true);

		Button addNewTaskBtn = new Button("Dodaj zadanie");
		addNewTaskBtn.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				windowCreateTask.center();
				getUI().addWindow(windowCreateTask);

			}
		});

		gerenateLists();

		mainLayout.setStyleName("bordered-grid");

		this.setSpacing(true);

		HorizontalLayout buttonMainGroupLayout = new HorizontalLayout();
		buttonMainGroupLayout.setSizeFull();
		buttonMainGroupLayout.setSpacing(true);

		buttonMainGroupLayout.addComponent(addNewBoardBtn);
		buttonMainGroupLayout.addComponent(addNewListBtn);
		buttonMainGroupLayout.addComponent(logout);

		this.addComponent(buttonMainGroupLayout);

		this.addComponent(mainLayout);
		setExpandRatio(mainLayout, 1f);
		windowCreateTask.setContent(subWindowForTask);

		windowCreateList.setContent(subWindowForList);

		windowCreateBoard.setContent(subWindowForBoard);
	}

	private void gerenateLists() {
		allLists = new ArrayList<>();
		allLists = listService.getAllList();

		ArrayList<Task> listAllTasks = taskService.getAllTask();

		for (int i = 0; i < allLists.size(); i++) {

			final Button addNewListBtn = new Button("Dodaj zadanie");
			addNewListBtn.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					List currentList = (List) event.getButton().getParent();
					TasksView.currentList = currentList.getId_list();
					windowCreateTask.center();
					getUI().addWindow(windowCreateTask);

				}
			});

			List list = new List(allLists.get(i).getName());
			list.setStyleName("list");
			list.setId_list(allLists.get(i).getId_list());
			list.setSizeFull();
			list.setSpacing(true);

			list.addComponent(addNewListBtn);

			for (int j = 0; j < listAllTasks.size(); j++) {

				if (allLists.get(i).getId_list()
						.equals(listAllTasks.get(j).getId_list())) {
					list.addComponent(listAllTasks.get(j));
				}

			}
			mainLayout.setStyleName("list");
			mainLayout.addComponent(list);
			mainLayout.setExpandRatio(list, 1f);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {

		String username = String.valueOf(getSession().getAttribute("user"));

	}
}
