package com.paw.trelloplus.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.paw.trelloplus.components.List;
import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.models.Comment;
import com.paw.trelloplus.models.User;
import com.paw.trelloplus.utils.Helper;
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


	private Task task;
	private final static Logger logger = Logger.getLogger(TaskService.class.getName());
	
	public TaskService() {
		super();
	}
	
	public boolean moveTask(String taskId, int newListId) throws UnsupportedOperationException, SQLException {
		String sql = "select * from tasks where id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, taskId);
		ResultSet rs = statement.executeQuery();
		if(rs.next())
		{
			 sql ="update tasks set id_list = ? where id = ?";
			 statement = connection.prepareStatement(sql);
			 statement.setInt(1, newListId);
			 statement.setString(2, taskId);
			 statement.executeUpdate();
			 statement.close();
			 connection.commit(); 
			 return true;
		}
		else
			return false;

	}
	
	public Task addTask(String listId, String title, String description, String marked)
			throws UnsupportedOperationException, SQLException {

		String sql ="INSERT INTO tasks values(NUll, ?, ?, ?, 0)";
	    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    statement.setString(1, listId);
	    statement.setString(2, title);
	    statement.setString(3, description);
	    statement.executeUpdate();
	    ResultSet rs = statement.getGeneratedKeys();
		int id = 0;
		if (rs.next()){
		    id =rs.getInt(1);
		}
	    statement.close();
	    connection.commit(); 
	    Task task = new Task(id+"",  title, description,listId+"", 0+"");
	    
	    sql="INSERT INTO tasks_users values(?, ?)";
	    statement = connection.prepareStatement(sql);
	    statement.setInt(1, id);
	    statement.setString(2, LoginView.ID_USER);
	    statement.executeUpdate();
      
	    statement.close();
	    connection.commit(); 
	    
	    return task;
	}
	
	public void editMarkedTask(String idTask, String marked) throws SQLException
	{
		 String sql ="update tasks set marked = ? where id = ?";
		 PreparedStatement statement = connection.prepareStatement(sql);
		 statement.setString(1, marked);
		 statement.setString(2, idTask);
		 statement.executeUpdate();
		 statement.close();
		 connection.commit();   
	}
	
	public String getMarkedTaskById(String idTask) throws SQLException
	{
		String sql = "select marked from tasks where id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, idTask);
		ResultSet rs = statement.executeQuery();
		if (rs.next()) 
		{
			return rs.getString("marked");
		}
		return null;
	}

	public ArrayList<Task> getAllTask() throws SQLException {

		ArrayList<Task> listAllTasks = new ArrayList<>();

		String sql = "SELECT * from tasks";
		PreparedStatement statement =  connection.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) 
		{
			listAllTasks.add(new Task(rs.getString("id"), rs.getString("name"),rs.getString("description"),rs.getString("id_list"),rs.getString("marked")));
		}
		return listAllTasks;
	}
	
	public ArrayList<Comment> getCommentsToTaskByCurrentTask(String id_task) throws SQLException {
		
		ArrayList<Comment> comments = new ArrayList<Comment>();
		String sql = "SELECT tc.id, tc.content, tc.created, u.login as usermail, tc.task_id FROM task_comments tc LEFT JOIN users u on u.id=tc.user_id WHERE tc.task_id=? and user_id= ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, id_task);
		statement.setString(2, LoginView.ID_USER);
		ResultSet rs = statement.executeQuery();
		
		while (rs.next()) 
		{
			Comment c = new Comment(rs.getString("usermail"), rs.getString("content"), rs.getInt("id"), rs.getInt("task_id"), rs.getTimestamp("created"));
			comments.add(c);
		}
		return comments;
	}

	public int addCommentToCurrentTask(String value, String task_id) {
		
		try {
			PreparedStatement s = connection.prepareStatement("INSERT INTO task_comments VALUES(NULL,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			s.setString(1, task_id);
			s.setString(2, Helper.getCurrentDateAsString());
			s.setInt(3, Integer.parseInt(LoginView.ID_USER));
			s.setString(4, value);
			int newId = s.executeUpdate();
			connection.commit();
			return newId;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		
		
	}

}
