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

//wersja Agi
@Theme("trelloplus.scss")
public class TasksView extends VerticalLayout implements View {
	public static String currentList = "";
	public static final String NAME = "main";
	public Window win;
	public Window createNewListWin;
	public Window createNewBoardWin;
	public VerticalLayout mainlayout;
	public VerticalLayout subWindowLayout;
	public VerticalLayout subBoardWindow;
	public VerticalLayout subCreateNewListLayout;
	public HorizontalLayout tableLayout;
	public Service service;
	public Task t;
	public VerticalLayout listGroupLayout;
	public ArrayList<List> allLists;

	public TasksView() {

		service = new Service();


		Button logout = new Button("Wyloguj", new Button.ClickListener() {

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
					service.addTask(TasksView.currentList, title.getValue(),
							descriptionArea.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				win.close();
				//t = new Task(title.getValue(), descriptionArea.getValue());

				Notification.show("Dodano");
				ArrayList<List> listsFromCache = service.fillTableList(false);
				for (List l : listsFromCache)
					if (l.getId_list().equals(TasksView.currentList)) {
						l.addComponent(t);
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

		// Okienko do dodawania tablicy
		createNewBoardWin = new Window("Dodawanie tablicy");
		createNewBoardWin.setModal(true);
		createNewBoardWin.setContent(subBoardWindow);

		subBoardWindow = new VerticalLayout();
		subBoardWindow.setMargin(true);
		subBoardWindow.setSpacing(true);

		final TextField titleBoard = new TextField();
		titleBoard.setInputPrompt("Podaj tytul tablicy");
		titleBoard.setSizeFull();

		Button createBoardBtn = new Button("Utworz tablice");
		createBoardBtn.setSizeFull();
		createBoardBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				createNewBoardWin.close();

			}
		});

		subBoardWindow.addComponent(titleBoard);
		subBoardWindow.addComponent(createBoardBtn);

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
				tableLayout.setStyleName("list");
				tableLayout.addComponent(newList);
				Notification.show("dodano liste");
			}
		});

		subCreateNewListLayout.addComponent(titleNewList);
		subCreateNewListLayout.addComponent(createList);

		tableLayout = new HorizontalLayout();
		tableLayout.setSpacing(true);

		Button addNewTaskBtn = new Button("Dodaj zadanie");
		addNewTaskBtn.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				win.center();
				getUI().addWindow(win);

			}
		});

		Button addNewBoardBtn = new Button("Dodaj tablice");
		addNewBoardBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				createNewBoardWin.center();
				getUI().addWindow(createNewBoardWin);
			}
		});

		gerenateLists();

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

		buttonMainGroupLayout.addComponent(addNewBoardBtn);
		buttonMainGroupLayout.addComponent(addNewListBtn);
		buttonMainGroupLayout.addComponent(logout);

		this.addComponent(buttonMainGroupLayout);

		this.addComponent(tableLayout);
		setExpandRatio(tableLayout, 1f);
		win.setContent(subWindowLayout);

		createNewListWin.setContent(subCreateNewListLayout);

		createNewBoardWin.setContent(subBoardWindow);
	}

	private void gerenateLists() {
		allLists = new ArrayList<>();
		allLists = service.fillTableList(true);

		ArrayList<Task> listAllTasks = service.fillTable();

		for (int i = 0; i < allLists.size(); i++) {

			final Button addNewListBtn = new Button("Dodaj zadanie");
			addNewListBtn.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					List currentList = (List) event.getButton().getParent();
					TasksView.currentList = currentList.getId_list();
					win.center();
					getUI().addWindow(win);

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
			tableLayout.setStyleName("list");
			tableLayout.addComponent(list);
			tableLayout.setExpandRatio(list, 1f);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {

		String username = String.valueOf(getSession().getAttribute("user"));
		
	}
}
