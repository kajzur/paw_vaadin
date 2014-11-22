package com.paw.trelloplus.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Helper;




import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.models.Comment;
import com.paw.trelloplus.views.LoginView;
import com.paw.trelloplus.views.TasksView;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class TaskService extends AbstractService {

	private SQLContainer tasksContainer;
	private Task task;
	private SQLContainer commentsContainer;
	private final static Logger logger = Logger.getLogger(TaskService.class.getName());
	
	public TaskService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean moveTask(String taskId, int newListId) throws UnsupportedOperationException, SQLException {
		tasksContainer.removeAllContainerFilters();
		tasksContainer.addContainerFilter(new Compare.Equal("id", taskId));
		if (!(tasksContainer.size() > 0))
			return false;		
		Object object = tasksContainer.getIdByIndex(0);
				
		Item item = tasksContainer.getItem(object);
		item.getItemProperty("id_list").setValue(newListId);
		tasksContainer.commit();
		return true;
	}
	public Task addTask(String listId, String title, String description)
			throws UnsupportedOperationException, SQLException {

		Object rowId = tasksContainer.addItem();
		Item rowItem = tasksContainer.getItem(rowId);
		rowItem.getItemProperty("id_list").setValue(Integer.parseInt(listId));	
		rowItem.getItemProperty("name").setValue(title);
		rowItem.getItemProperty("description").setValue(description);
		tasksContainer.commit();
		
		Connection conn = connectionPool.reserveConnection();
        Statement statement = conn.createStatement();
        //statement.executeUpdate("INSERT INTO tasks_users values("+(((RowId) tasksContainer.lastItemId()).toString())+", "+"11"+")");
        statement.executeUpdate("INSERT INTO tasks_users values("+(((RowId) tasksContainer.lastItemId()).toString())+", "+LoginView.ID_USER+")");
        
        statement.close();
        conn.commit(); 
        
        
		String newId = (String)(((RowId)tasksContainer.lastItemId()).getId()[0]+"");
		Task t = new Task(newId, title, description, listId);
	
		return t;
	}

	public ArrayList<Task> getAllTask() throws SQLException {

		ArrayList<Task> listAllTasks = new ArrayList<>();

		for (int i = 0; i < tasksContainer.size(); i++) {
			Object id = tasksContainer.getIdByIndex(i);
			Item item = tasksContainer.getItem(id);
			Property task_id = item.getItemProperty("id");
			Property id_list = item.getItemProperty("id_list");
			Property name = item.getItemProperty("name");
			Property desc = item.getItemProperty("description");
			Task task = new Task(task_id.getValue().toString(),
					name.getValue().toString(),
					desc.getValue().toString(),
					id_list.getValue().toString());

			listAllTasks.add(task);
		}
		return listAllTasks;

	}

	@Override
	protected void initContainers() {
		try {
			TableQuery q1 = new TableQuery("tasks", connectionPool);
			q1.setVersionColumn("VERSION");
			tasksContainer = new SQLContainer(q1);
			if(getTask()!=null)
			{
				FreeformQuery q2 = new FreeformQuery("SELECT tc.id, tc.content, tc.created, u.login as usermail, tc.task_id FROM task_comments tc "
					+ " LEFT JOIN users u on u.id=tc.user_id WHERE tc.task_id="+getTask().getTask_id()+" and user_id="+LoginView.ID_USER, connectionPool);
				commentsContainer = new SQLContainer(q2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void setTask(Task t){
		task = t;
		initContainers();
	}
	
	public Task getTask() {
		
		return task;
	}
	public ArrayList<Comment> getCommentsToTaskByCurrentTask() {
		
		ArrayList<Comment> comments = new ArrayList<Comment>();
		for (int i = 0; i < commentsContainer.size(); i++) {
			Object id = commentsContainer.getIdByIndex(i);
			Item item = commentsContainer.getItem(id);
			Property idC = item.getItemProperty("id");
			Property login = item.getItemProperty("usermail");
			Property content = item.getItemProperty("content");
			Property task_id = item.getItemProperty("task_id");
			Property date = item.getItemProperty("created"); 
			Comment c = new Comment((String)login.getValue(), (String)content.getValue(), (int)idC.getValue(), (int)task_id.getValue(), (Timestamp)date.getValue());
			comments.add(c);
		}

		return comments;
	}

	public int addCommentToCurrentTask(String value) {
		
		try {
			PreparedStatement s = connection.prepareStatement("INSERT INTO task_comments VALUES(NULL,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			s.setInt(1, Integer.parseInt(getTask().getTask_id()));
			s.setString(2, Helper.getCurrentDateAsString());
			s.setInt(3, Integer.parseInt(LoginView.ID_USER));
			s.setString(4, value);
			int newId = s.executeUpdate();
			connection.commit();
			return newId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		
		
	}

}
