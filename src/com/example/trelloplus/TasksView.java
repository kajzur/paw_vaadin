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
	public Window createNewBoardWin;
	public Window createNewListWin;
	public VerticalLayout mainlayout;
	public VerticalLayout subWindowLayout;
	public VerticalLayout subCreateNewListLayout;
	public VerticalLayout subBoardWindow;
	public HorizontalLayout tableLayout;
	public Service service;
	public Label text;
	Task t;
	VerticalLayout listGroupLayout;
	ArrayList<List> allLists;

	public TasksView() {

		t = new Task("tytul", "opis");

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

		final TextField id = new TextField();
		id.setInputPrompt("Podaj nr listy");
		id.setSizeFull();

		final TextField title = new TextField();
		title.setInputPrompt("Podaj tytul");
		title.setSizeFull();

		final TextArea descriptionArea = new TextArea();
		descriptionArea.setInputPrompt("Podaj opis");
		descriptionArea.setSizeFull();

		Button save = new Button("Save");
		save.setSizeFull();
		save.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					service.addTask(id.getValue(), title.getValue(),
							descriptionArea.getValue());
				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				win.close();
				t = new Task(title.getValue(), descriptionArea.getValue());

				// t = new Task(service.CompareTasksToList("2"),
				// service.CompareTasksToList("2")); //
				// service.CompareTasksToList("2");
				// listGroupLayout.addComponent(t);
				// tableLayout = new HorizontalLayout();
				// subCreateNewListLayout = new VerticalLayout();
				// subWindowLayout = new VerticalLayout();
				// createNewListWin = new Window("Tytul");
				// gerenateLists();
				// tableLayout.addComponent(t);

				Notification.show("Dodano");

				title.setValue("");
				descriptionArea.setValue("");

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

		subWindowLayout.addComponent(id);
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
				//tableLayout = new HorizontalLayout();

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

		Button addNewBoardBtn = new Button("Dodaj tablice");
		addNewBoardBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
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

		this.addComponent(text);
		this.addComponent(buttonMainGroupLayout);
		/*
		 * this.addComponent(button); this.addComponent(logout);
		 * this.addComponent(addNewListBtn);
		 */
		this.addComponent(tableLayout);
		setExpandRatio(tableLayout, 1f);
		win.setContent(subWindowLayout);

		createNewListWin.setContent(subCreateNewListLayout);
		
		createNewBoardWin.setContent(subBoardWindow);
	}

	private void gerenateLists() {
		allLists = new ArrayList<>();
		allLists = service.fillTableList();

		ArrayList<Task> listAllTasks = service.fillTable();

		for (int i = 0; i < allLists.size(); i++) {

			// for (List b : allLists) {
			// b.setWidth(widthPerList + "%");

			// b.setWidth("30%"); b.setw

			// b.get

			final Button addNewListBtn = new Button("Dodaj zadanie");
			addNewListBtn.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					win.center();
					getUI().addWindow(win);
					addNewListBtn.getParent();
					// Notification.show(addNewListBtn.getParent() + "");

				}
			});

			// gridLayout.addComponent(b);
			
			listGroupLayout = new VerticalLayout();
			listGroupLayout.setStyleName("list");
			listGroupLayout.setSizeFull();
			listGroupLayout.setSpacing(true);

			listGroupLayout.addComponent(allLists.get(i));
			listGroupLayout.addComponent(addNewListBtn);

			for (int j = 0; j < listAllTasks.size(); j++) {

				if (i == Integer.parseInt(listAllTasks.get(j).getId_list())) {
					listGroupLayout.addComponent(listAllTasks.get(j));
				}

			}
			

			// buttonMainGroupLayout.addComponent(t);
			tableLayout.setStyleName("list");
			tableLayout.addComponent(listGroupLayout);
			// gridLayout.setExpandRatio(b, 1f);

			tableLayout.setExpandRatio(listGroupLayout, 1f);

		}

	}

	@Override
	public void enter(ViewChangeEvent event) {

		String username = String.valueOf(getSession().getAttribute("user"));
		text.setValue("Hello " + username);
	}

}
