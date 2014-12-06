package com.paw.trelloplus.views;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;








import com.paw.trelloplus.components.Board;
import com.paw.trelloplus.components.List;
import com.paw.trelloplus.components.Organization;
import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.service.AuthorizationService;
import com.paw.trelloplus.service.BoardService;
import com.paw.trelloplus.service.ListService;
import com.paw.trelloplus.service.OrganizationService;
import com.paw.trelloplus.service.TaskService;
import com.paw.trelloplus.utils.EditBoardButtonHandler;
import com.paw.trelloplus.utils.ListDropHandler;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

@Theme("trelloplus.scss")
public class TasksView extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = -473387470476587223L;
	public static String ID_BOARD;
	public static String currentList = "";
	public static final String NAME = "main";
	public Window windowCreateList;
	public Window windowCreateBoard;
	public Window windowCreateOrganization;
	public Window windowAssignOrganization;
	public VerticalLayout subWindowForTask;
	public VerticalLayout subWindowForBoard;
	public VerticalLayout subWindowForOrganization;
	public VerticalLayout subWindowForList;
	private BoardService boardService;
	private OrganizationService organizationService;
	public HorizontalLayout mainLayout;
	private final static Logger logger = Logger.getLogger(TasksView.class
			.getName());
	private Label currentTableName;
	private HorizontalLayout currentTableNameContainer;
	public Task task;
	public ArrayList<List> allLists;
	ArrayList<Board> boards;

	public TaskService taskService;
	public ListService listService;
	public AuthorizationService authorizationService;

	private VerticalLayout vt;
	private HorizontalLayout ht;

	Label descriptionMarkedBoard;
	String valueMarkedBoard;

	MenuBar menubar = new MenuBar();
	final MenuBar.MenuItem chooseBoards = menubar.addItem("wybierz tablicê",
			null);

	public TasksView() throws SQLException {

		authorizationService = new AuthorizationService();
		boardService = new BoardService();
		organizationService = new OrganizationService();
		taskService = new TaskService();
		listService = new ListService();

		vt = new VerticalLayout();
		ht = new HorizontalLayout();
		currentTableNameContainer = new HorizontalLayout();

		boards = boardService.getAllBoard();
		Board currentTable = boards.get(0);
		ID_BOARD = currentTable.getBoardId();

		for (Board board : boards) {
			chooseBoards.addItem(board.getName(), menuCommand);
			chooseBoards.addSeparator();
		}
		addComponent(menubar);
		setCurrentTableName(currentTable.getName());
		addComponent(currentTableNameContainer);
		final MenuBar.MenuItem addNewBoard = menubar.addItem("dodaj tablicê",
				addTable);
		final MenuBar.MenuItem addNewList = menubar.addItem("dodaj listê",
				addList);
		final MenuBar.MenuItem addNewOrganization = menubar.addItem(
				"dodaj organizacje", addOrganization);
		final MenuBar.MenuItem assignOrganization = menubar.addItem(
				"przypisz organizacje", assignOrganizationCmd);
		final MenuBar.MenuItem markedBoard = menubar.addItem("*",
				markedBoardCmd);
		final MenuBar.MenuItem logoutMenu = menubar.addItem("wyloguj",
				logoutCommand);

		// Okienko do dodawania organizacji
		final TextField titleOrganization = new TextField();
		titleOrganization.setInputPrompt("Podaj nazwe organizacji");
		titleOrganization.setSizeFull();

		Button createOrganizationBtn = new Button("Utworz Organizacje");
		createOrganizationBtn.setSizeFull();
		createOrganizationBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					organizationService.addNewOrganization(titleOrganization
							.getValue());

				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				windowCreateOrganization.close();
			}

		});

		// okienko do przypisania organizacji
		windowAssignOrganization = new Window("Przypisz Organizacje");
		windowAssignOrganization.setModal(true);

		// Okienko do dodawania zadania
		final TextField titleBoard = new TextField();
		titleBoard.setInputPrompt("Podaj tytul tablicy");
		titleBoard.setSizeFull();

		Button createBoardBtn = new Button("Utworz tablice");
		createBoardBtn.setSizeFull();
		createBoardBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					boardService.addNew(titleBoard.getValue());
					Notification.show("Dodano..");
					chooseBoards.addItem(titleBoard.getValue(), menuCommand);
					chooseBoards.addSeparator();
					boards = boardService.getAllBoard();
				} catch (UnsupportedOperationException | SQLException e) {
					e.printStackTrace();
				}
				windowCreateBoard.close();

			}
		});

		// Okienko do dodawania tablicy
		windowCreateBoard = new Window("Dodawanie tablicy");
		windowCreateBoard.setModal(true);
		windowCreateBoard.setContent(subWindowForBoard);

		subWindowForBoard = new VerticalLayout();
		subWindowForBoard.setMargin(true);
		subWindowForBoard.setSpacing(true);

		subWindowForBoard.addComponent(titleBoard);
		subWindowForBoard.addComponent(createBoardBtn);

		// okienko do tworzenia nowej organizacji
		windowCreateOrganization = new Window("Dodawanie organizacji");
		windowCreateOrganization.setModal(true);
		windowCreateOrganization.setContent(subWindowForOrganization);

		subWindowForOrganization = new VerticalLayout();
		subWindowForOrganization.setMargin(true);
		subWindowForOrganization.setSpacing(true);

		subWindowForOrganization.addComponent(titleOrganization);
		subWindowForOrganization.addComponent(createOrganizationBtn);

		// okienko do tworzenia nowej listy
		windowCreateList = new Window("Tytul");
		windowCreateList.setModal(true);
		windowCreateList.setContent(subWindowForList);

		subWindowForList = new VerticalLayout();
		subWindowForList.setMargin(true);
		subWindowForList.setSpacing(true);

		final TextField titleNewList = new TextField();
		titleNewList.setInputPrompt("Podaj tytul listy");
		titleNewList.setSizeFull();

		Button createList = new Button("Dodaj Liste");
		createList.setSizeFull();
		createList.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				List l = null;
				try {
					l = listService.addList(Integer.parseInt(ID_BOARD),
							titleNewList.getValue(), Integer.parseInt(String
									.valueOf(getSession().getAttribute("id"))));

				} catch (SQLException e) {
					Notification.show(e.getMessage());
				}
				windowCreateList.close();

				DragAndDropWrapper dd = new DragAndDropWrapper(l);
				dd.setData(l);
				mainLayout.addComponent(dd);
				dd.setDropHandler(new ListDropHandler(allLists));
				mainLayout.setExpandRatio(dd, 1f);
				allLists.add(l);
				Notification.show("dodano liste");
			}
		});

		subWindowForList.addComponent(titleNewList);
		subWindowForList.addComponent(createList);

		mainLayout = new HorizontalLayout();
		mainLayout.setSpacing(true);

		generateLists(ID_BOARD);

		mainLayout.setStyleName("bordered-grid");

		addLabelMarkedTable();

		this.setSpacing(true);

		HorizontalLayout buttonMainGroupLayout = new HorizontalLayout();
		buttonMainGroupLayout.setSizeFull();
		buttonMainGroupLayout.setSpacing(true);
		this.addComponent(mainLayout);
		setExpandRatio(mainLayout, 1f);

		windowCreateList.setContent(subWindowForList);

		windowCreateBoard.setContent(subWindowForBoard);

		windowCreateOrganization.setContent(subWindowForOrganization);
	}

	private void prepareWindow() throws SQLException {

		final VerticalLayout vl = new VerticalLayout();
		ArrayList<Organization> organizations = organizationService
				.getAllOrganizations();
		ArrayList<Organization> selectOrganizations = organizationService.getOrganizationByBoard(ID_BOARD);
		for(Organization o : organizations)
		{
			CheckBox checkbox1 = new CheckBox(o.getName(), (selectOrganizations.contains(o)));
			checkbox1.setData(o);
			vl.addComponent(checkbox1);
		}

		Button save = new Button("Zapisz");
		save.setSizeFull();
		save.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					organizationService.deleteOrganizationByBoard(ID_BOARD);
					organizationService.deleteOrganizationByUser(ID_BOARD);
					
					for(Component organizationCheckBox : vl)
					{
						if(organizationCheckBox instanceof CheckBox)
						{
							CheckBox temp = (CheckBox) organizationCheckBox;
							
							if(temp.getValue())
							{
								Organization organization = (Organization) temp.getData();
								organizationService.addOrganizationByBoard(organization, ID_BOARD);
								organizationService.addOrganizationByUser(organization, ID_BOARD);
							}
						}
					}
					
					windowAssignOrganization.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

		Button cancel = new Button("Anuluj");
		cancel.setSizeFull();
		cancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				windowAssignOrganization.close();

			}
		});

		HorizontalLayout buttonGroupLayout = new HorizontalLayout();
		buttonGroupLayout.setSizeFull();
		buttonGroupLayout.addComponent(save);
		buttonGroupLayout.addComponent(cancel);

		vl.addComponent(buttonGroupLayout);
		windowAssignOrganization.setContent(vl);
	}

	private void generateLists(String idCurentBoard) throws SQLException {

		ArrayList<List> allLists2 = new ArrayList<List>();

		allLists2 = listService.getAllList();

		allLists = new ArrayList<List>();
		for (List list : allLists2) {
			if (list.getId_board().equals(idCurentBoard)) {
				allLists.add(list);
			}
		}

		ArrayList<Task> listAllTasks;
		try {
			listAllTasks = taskService.getAllTask();
		

		for (int i = 0; i < allLists.size(); i++) {

			List cList = allLists.get(i);
			for (int j = 0; j < listAllTasks.size(); j++) {

				if (cList.getId_list().equals(listAllTasks.get(j).getId_list())) {

					if (listAllTasks.get(j).getMarked().equals("5")) {
						listAllTasks.get(j).setStyleName("pink_marked_task");
					}
					
					if (listAllTasks.get(j).getMarked().equals("4")) {
						listAllTasks.get(j).setStyleName("purple_marked_task");
					}
					
					if (listAllTasks.get(j).getMarked().equals("3")) {
						listAllTasks.get(j).setStyleName("red_marked_task");
					}
					
					if (listAllTasks.get(j).getMarked().equals("2")) {
						listAllTasks.get(j).setStyleName("green_marked_task");
					}
					
					if (listAllTasks.get(j).getMarked().equals("1")) {
						listAllTasks.get(j).setStyleName("blue_marked_task");
					}
					if (listAllTasks.get(j).getMarked().equals("0")) {
						listAllTasks.get(j).setStyleName("task");
					}
					cList.addComponent(listAllTasks.get(j));
				}

			}
			cList.setDrop(new ListDropHandler(allLists));
			mainLayout.addComponent(cList);
			mainLayout.setExpandRatio(cList, 1f);
			
		}} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {

		String username = String.valueOf(getSession().getAttribute("user"));

	}

	public void addLabelMarkedTable() throws SQLException {

		String marked = boardService.getMarkedBoardById(ID_BOARD);

		if (marked.equals("0")) {
			descriptionMarkedBoard = new Label("Tablica odznaczona");
			mainLayout.addComponent(descriptionMarkedBoard);
		}

		if (marked.equals("1")) {
			descriptionMarkedBoard = new Label("Tablica oznaczona");
			mainLayout.addComponent(descriptionMarkedBoard);
		}

	}


	public void refreshMenu() throws SQLException {
		boards = boardService.getAllBoard();

		chooseBoards.removeChildren();

		for (Board board : boards) {
			chooseBoards.addItem(board.getName(), menuCommand);
			chooseBoards.addSeparator();
		}
	}
	
	public String getCurrentTableName() {
		return currentTableName.getValue();
	}

	public void setCurrentTableName(String tableName) {
		Logger.getGlobal().log(Level.SEVERE, tableName);
		Button edit = new Button();
		edit.setStyleName(BaseTheme.BUTTON_LINK+" edit-table-btn");
		edit.setIcon(new ThemeResource("icons/edit.png"));
		edit.addClickListener(new EditBoardButtonHandler(tableName, this));
		if(currentTableName == null)
		{
			currentTableName = new Label(tableName);
			currentTableNameContainer.addComponent(currentTableName);
			currentTableNameContainer.addComponent(edit);
		}
		else{
			currentTableName.setValue(tableName);
			currentTableNameContainer.replaceComponent(currentTableNameContainer.getComponent(1), edit);
		}
	}

	private Command menuCommand = new Command() {
		@Override
		public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {

			for (Board board : boards) {
				if (selectedItem.getText().equals(board.getName())) {
					ID_BOARD = board.getBoardId();
					mainLayout.removeAllComponents();
					try {
						generateLists(ID_BOARD);
						addLabelMarkedTable();
						setCurrentTableName(board.getName());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
	};

	private Command addTable = new Command() {
		@Override
		public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {

			windowCreateBoard.center();
			getUI().addWindow(windowCreateBoard);

		}
	};

	private Command addList = new Command() {
		@Override
		public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {

			windowCreateList.center();
			getUI().addWindow(windowCreateList);

		}
	};

	private Command addOrganization = new Command() {
		@Override
		public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {

			windowCreateOrganization.center();
			getUI().addWindow(windowCreateOrganization);

		}
	};

	private Command assignOrganizationCmd = new Command() {
		@Override
		public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
			try {
				prepareWindow();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			windowAssignOrganization.center();
			getUI().addWindow(windowAssignOrganization);

		}
	};

	private Command markedBoardCmd = new Command() {
		@Override
		public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {

			try {
				valueMarkedBoard = boardService.getMarkedBoardById(ID_BOARD);

				if (valueMarkedBoard.equals("0")) {
					boardService.editMarkedBoard(ID_BOARD, "1");
					Notification.show("Tablica zostala zaznaczona");
					mainLayout.removeComponent(descriptionMarkedBoard);

					addLabelMarkedTable();
				}
				if (valueMarkedBoard.equals("1")) {
					boardService.editMarkedBoard(ID_BOARD, "0");
					Notification.show("Tablica zostala odznaczona");
					mainLayout.removeComponent(descriptionMarkedBoard);

					addLabelMarkedTable();
				}

				refreshMenu();

			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

	};

	private Command logoutCommand = new Command() {
		@Override
		public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {

			getSession().setAttribute("user", null);
			getUI().getNavigator().navigateTo(NAME);

		}
	};

}
