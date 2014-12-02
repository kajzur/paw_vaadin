package com.paw.trelloplus.components;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import com.paw.trelloplus.models.User;
import com.paw.trelloplus.service.AuthorizationService;
import com.paw.trelloplus.service.TaskService;
import com.paw.trelloplus.utils.CommentsButtonHandler;
import com.paw.trelloplus.views.TasksView;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Task extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7689196414848406296L;

	private String id_list;
	private String task_id;
	private String complexity;
	private String marked;
	private Date deadline;
	private Window addUserWindow;
	private Window addMarkedWindow;
	private Button addUser, addLabelForTask;
	private AuthorizationService authorizationService;
	private TaskService taskService;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Label deadlineLabel;
	private String title;
	private String desc;
	private VerticalLayout vt;
	private HorizontalLayout ht;
	private final static Logger logger = Logger.getLogger(TasksView.class
			.getName());

	public Task(String id, final String title, final String desc, String id_list,
			final String marked1, String complexity1, final Date deadline) throws SQLException {
		
		this.task_id = id;
		this.title = title;
		this.desc = desc;
		this.id_list = id_list;
		this.marked = marked1;
		this.complexity = complexity1;
		this.deadline = deadline;

		taskService = new TaskService();

		authorizationService = new AuthorizationService();
		this.setStyleName("task");
		HorizontalLayout l1 = new HorizontalLayout();
		l1.addComponent(new Label(title));

		HorizontalLayout l2 = new HorizontalLayout();
		l2.addComponent(new Label(desc));

		HorizontalLayout l3 = new HorizontalLayout();
		l3.addComponent(new Label("zlozonosc: " + complexity1));
		
		final HorizontalLayout l4 = new HorizontalLayout();
		deadlineLabel = new Label("termin: " + formatter.format(deadline));
		if(deadline.before(new Date()))
		{
			deadlineLabel.setStyleName("red-label");
		}
		l4.addComponent(deadlineLabel);

		vt = new VerticalLayout();

		ht = new HorizontalLayout();
		

		addLabelForTask = new Button("*");
		addLabelForTask.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					prepareWindowMarked();
				} catch (SQLException e) {

					e.printStackTrace();
				}
				addMarkedWindow.center();
				getUI().addWindow(addMarkedWindow);
			}
		});

		addMarkedWindow = new Window("Wybierz kolor");
		addMarkedWindow.setModal(true);
		addMarkedWindow.setContent(vt);

		addUser = new Button("+");
		addUser.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					prepareWindow();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				addUserWindow.center();
				getUI().addWindow(addUserWindow);

			}
		});

		addUserWindow = new Window("Dodaj u¿ytkowników");
		addUserWindow.setModal(true);
		addUserWindow.setContent(vt);

		ht.addComponent(addLabelForTask);
		ht.addComponent(addUser);

		ht.addComponent(getCommentButton("..."));

		vt.addComponent(l1);
		vt.addComponent(l2);
		vt.addComponent(l3);
		vt.addComponent(l4);

		ArrayList<User> users = authorizationService.getUsersByTask(getTask_id());

		for (User user : users) {
			vt.addComponent(new Label(user.getLogin()));
		}
		vt.addComponent(ht);
		setCompositionRoot(vt);
		
	}

	@SuppressWarnings("deprecation")
	private void prepareWindowMarked() throws SQLException {

		final VerticalLayout vl = new VerticalLayout();

		final OptionGroup group = new OptionGroup("Wybierz kolor");
		group.addItem("brak");
		group.addItem("niebieski");
		group.addItem("zielony");
		group.addItem("czerwony");
		group.addItem("fioletowy");
		group.addItem("ró¿owy");

		group.addListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {

				try {

					if (group.getValue().equals("brak")) {

						taskService.editMarkedTask(task_id, "0");

						setStyleName("task");

					}

					if (group.getValue().equals("niebieski")) {

						taskService.editMarkedTask(task_id, "1");

						setStyleName("blue_marked_task");

					}

					if (group.getValue().equals("zielony")) {

						taskService.editMarkedTask(task_id, "2");

						setStyleName("green_marked_task");
					}

					if (group.getValue().equals("czerwony")) {

						taskService.editMarkedTask(task_id, "3");

						setStyleName("red_marked_task");
					}

					if (group.getValue().equals("fioletowy")) {

						taskService.editMarkedTask(task_id, "4");

						setStyleName("purple_marked_task");
					}

					if (group.getValue().equals("ró¿owy")) {

						taskService.editMarkedTask(task_id, "5");

						setStyleName("pink_marked_task");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});

		ArrayList<String> selectedMarked = new ArrayList<String>();

		vl.addComponent(group);

		Button close = new Button("Zamknij");
		close.setSizeFull();
		close.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				addMarkedWindow.close();
			}
		});

		HorizontalLayout buttonGroupLayout = new HorizontalLayout();
		buttonGroupLayout.setSizeFull();
		buttonGroupLayout.addComponent(close);

		vl.addComponent(buttonGroupLayout);
		addMarkedWindow.setContent(vl);

	}

	private void prepareWindow() throws SQLException {
		final VerticalLayout vl = new VerticalLayout();
		ArrayList<User> users = authorizationService.getAllUsers();
		ArrayList<User> selectedUsers = authorizationService
				.getUsersByTask(getTask_id());
		for (User u : users) {
			CheckBox checkbox1 = new CheckBox(u.getLogin(),
					(selectedUsers.contains(u)));
			checkbox1.setData(u);
			vl.addComponent(checkbox1);
		}

		Button save = new Button("Zapisz");
		save.setSizeFull();
		save.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				try {
					authorizationService.deleteUserByTask(getTask_id());
					for (Component userCheckBox : vl) {

						if (userCheckBox instanceof CheckBox) {
							CheckBox temp = (CheckBox) userCheckBox;

							if (temp.getValue()) {
								User user = (User) temp.getData();
								authorizationService.addUserByTask(user,
										getTask_id());
							}
						}
					}
					addUserWindow.close();
					ArrayList<User> users;
					users = authorizationService.getUsersByTask(getTask_id());
					vt.removeAllComponents();
					vt.addComponent(new Label(title));
					vt.addComponent(new Label(desc));
					vt.addComponent(new Label("zlozonosc: " + complexity));
					vt.addComponent(deadlineLabel);
					ht.removeAllComponents();
					ht.addComponent(addLabelForTask);
					ht.addComponent(addUser);
					ht.addComponent(getCommentButton("..."));

					for (User u : users) {
						vt.addComponent(new Label(u.getLogin()));
					}
					vt.addComponent(ht);
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

	public String getMarked() {
		return marked;
	}

	public void setMarked(String marked) {
		this.marked = marked;
	}
	
	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	private Button getCommentButton(String name) {
		Button b = new Button(name);
		b.addClickListener(new CommentsButtonHandler(this));
		return b;
	}

}
