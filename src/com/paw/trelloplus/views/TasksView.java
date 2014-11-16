package com.paw.trelloplus.views;

import java.awt.MenuItem;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import utils.ListDropHandler;

import com.paw.trelloplus.components.Board;
import com.paw.trelloplus.components.List;
import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.service.AuthorizationService;
import com.paw.trelloplus.service.BoardService;
import com.paw.trelloplus.service.ListService;
import com.paw.trelloplus.service.TaskService;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("trelloplus.scss")
public class TasksView extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = -473387470476587223L;
	public static String ID_USER;
	public static String ID_BOARD;
	public static String currentList = "";
	public static final String NAME = "main";
	public Window windowCreateList;
	public Window windowCreateBoard;
	public VerticalLayout subWindowForTask;
	public VerticalLayout subWindowForBoard;
	public VerticalLayout subWindowForList;
	private BoardService boardService;
	public HorizontalLayout mainLayout;
	private final static Logger logger =
	          Logger.getLogger(TasksView.class.getName());

	public Task task;
	public ArrayList<List> allLists;
	ArrayList<Board> boards;

	public TaskService taskService;
	public ListService listService;
	public AuthorizationService authorizationService;

	public TasksView() {
		
		authorizationService = new AuthorizationService();
		boardService = new BoardService();
		taskService = new TaskService();
		listService = new ListService();
		
		MenuBar menubar = new MenuBar();	

		final MenuBar.MenuItem chooseBoards = menubar.addItem("wybierz tablicê", null);
		boards = boardService.getAllBoard();
		ID_BOARD = boards.get(0).getBoardId();
		
		for(Board board : boards)
		{
			chooseBoards.addItem(board.getName(), menuCommand);
			chooseBoards.addSeparator();
		}
		addComponent(menubar);
		    

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
				} catch (UnsupportedOperationException | SQLException e) {
					// TODO Auto-generated catch block
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
				List l = null;
				try {
					l=listService.addList(Integer.parseInt(ID_BOARD), titleNewList.getValue(), Integer.parseInt(String.valueOf(getSession().getAttribute("id"))) );
					
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
		

		windowCreateList.setContent(subWindowForList);

		windowCreateBoard.setContent(subWindowForBoard);
	}

	private void generateLists(String idCurentBoard) {
		
		ArrayList<List> allLists2 = new ArrayList<List>();
		
		allLists2 = listService.getAllList();
			
		allLists = new ArrayList<List>();
		for(List list:allLists2)
		{
			if(list.getId_board().equals(idCurentBoard))
			{
				allLists.add(list);
			}
		}

		ArrayList<Task> listAllTasks = taskService.getAllTask();

		for (int i = 0; i < allLists.size(); i++) {
			
			List cList = allLists.get(i);
			for (int j = 0; j < listAllTasks.size(); j++) {

				if (cList.getId_list()
						.equals(listAllTasks.get(j).getId_list())) {
					cList.addComponent(listAllTasks.get(j));
				}

			}
			DragAndDropWrapper dd = new DragAndDropWrapper(cList);
			dd.setData(cList);
			
			dd.setDropHandler(new ListDropHandler(allLists));
			
			mainLayout.addComponent(dd);
			mainLayout.setExpandRatio(dd, 1f);
		}
	}
	

	@Override
	public void enter(ViewChangeEvent event) {

		String username = String.valueOf(getSession().getAttribute("user"));

	}
	
	 private Command menuCommand = new Command() {	       
			@Override
			public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
				//tutaj mamy zmieniæ tablicê!
			
				for(Board board : boards)
				{
					if(selectedItem.getText().equals(board.getName()))
					{		
						ID_BOARD = board.getBoardId();
						mainLayout.removeAllComponents();
						generateLists(ID_BOARD);

					}

				}
			}
	    };
}
