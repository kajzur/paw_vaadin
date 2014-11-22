package com.paw.trelloplus.components;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.core.java.util.Arrays;
import com.paw.trelloplus.User;
import com.paw.trelloplus.service.AuthorizationService;
import com.paw.trelloplus.service.BoardService;
import com.paw.trelloplus.views.TasksView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class Task extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7689196414848406296L;
	
	private String id_list;
	private String task_id;
	private Window addUserWindow;
	private Button addUser;
	private AuthorizationService authorizationService;
	
	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	private String title;
	private String desc;
	private VerticalLayout vt;
	private final static Logger logger =
	          Logger.getLogger(TasksView.class.getName());

	public Task(String id, String title, String desc, String id_list) throws SQLException {
		this.task_id = id;
		this.title = title;
		this.desc = desc;
		this.id_list = id_list;
		
		authorizationService = new AuthorizationService();
		this.setStyleName("task");
		HorizontalLayout l1 = new HorizontalLayout();
		l1.addComponent(new Label(title));

		HorizontalLayout l2 = new HorizontalLayout();
		l2.addComponent(new Label(desc));
		
		vt = new VerticalLayout();
		
		
		addUser = new Button("+");
		addUser.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					prepareWindow();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addUserWindow.center();
				getUI().addWindow(addUserWindow);

			}
		});
		
		addUserWindow = new Window("Dodaj u¿ytkowników");
		addUserWindow.setModal(true);
		addUserWindow.setContent(vt);
		

		vt.addComponent(l1);
		vt.addComponent(l2);
		vt.addComponent(addUser);
		ArrayList<User> users = authorizationService.getUsersByTask(getTask_id());
		
		for(User user : users)
		{
			vt.addComponent(new Label(user.getLogin()));
		}

		setCompositionRoot(vt);

	}
	
private void prepareWindow() throws SQLException{
	 VerticalLayout vl = new VerticalLayout();
	 ArrayList users = authorizationService.getAllUsers();
	 final OptionGroup selectUser = new OptionGroup("", users);
	 selectUser.setSizeFull();
     selectUser.setMultiSelect(true);
  
     ArrayList<User> selectedUsers = authorizationService.getUsersByTask(getTask_id());
 	 logger.log(Level.SEVERE, "selected: "+ selectedUsers.size());
// 	 for(User u : selectedUsers)
// 	 {
// 		logger.log(Level.SEVERE, "user: "+ u.getId());
// 		 selectUser.select(new String("test@test.com"));
// 	 }
 	 selectUser.select(selectedUsers.toArray());
 
     Button save = new Button("Zapisz");
	 save.setSizeFull();
	 save.addClickListener(new Button.ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
			Collection<User> selected =  (Collection<User>) selectUser.getValue();
			try {
				authorizationService.deleteUserByTask(getTask_id());
				for(User user : selected)
				{
						authorizationService.addUserByTask(user, getTask_id());
						addUserWindow.close();
				}
				ArrayList<User> users;
				users = authorizationService.getUsersByTask(getTask_id());
				vt.removeAllComponents();
				vt.addComponent(new Label(title));
				vt.addComponent(new Label(desc));
				vt.addComponent(addUser);
				for(User u : users)
				{
					vt.addComponent(new Label(u.getLogin()));
				}
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	});

	Button cancel = new Button("Anuluj");
	cancel.setSizeFull();
	cancel.addClickListener(new Button.ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {

			addUserWindow.close();
		}
	});
	HorizontalLayout buttonGroupLayout = new HorizontalLayout();
	buttonGroupLayout.setSizeFull();
	buttonGroupLayout.addComponent(save);
	buttonGroupLayout.addComponent(cancel);
	
	
    vl.addComponent(selectUser);
    vl.addComponent(buttonGroupLayout);
    addUserWindow.setContent(vl);
	}

	public String getId_list() {
		return id_list;
	}

	public void setId_list(String id_list) {
		this.id_list = id_list;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
