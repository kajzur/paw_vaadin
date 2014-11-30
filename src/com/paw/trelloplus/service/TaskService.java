package com.paw.trelloplus.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.google.gwt.user.client.rpc.core.java.util.Collections;
import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.models.Comment;
import com.paw.trelloplus.utils.Helper;
import com.paw.trelloplus.views.LoginView;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

import fi.jasoft.dragdroplayouts.DDVerticalLayout;

public class TaskService extends AbstractService {


	private Task task;
	private final static Logger logger = Logger.getLogger(TaskService.class.getName());
	
	public TaskService() {
		super();
	}
	
	public boolean moveTaskToOtherList(String taskId, int newListId) throws UnsupportedOperationException, SQLException {
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
	
	public Task addTask(String listId, String title, String description, String marked, String complexity)
			throws UnsupportedOperationException, SQLException {

		String sql ="INSERT INTO tasks values(NUll, ?, ?, ?,0, IFNULL((select max(g.lp)+1 from tasks g where g.`id_list`=?), 0), ?)";
	    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    statement.setString(1, listId);
	    statement.setString(2, title);
	    statement.setString(3, description);
	    statement.setString(4, listId);
	    statement.setString(5, complexity);
	    statement.executeUpdate();
	    ResultSet rs = statement.getGeneratedKeys();
		int id = 0;
		if (rs.next()){
		    id =rs.getInt(1);
		}
	    statement.close();
	    connection.commit(); 
	    Task task = new Task(id+"",  title, description,listId+"", 0+"", complexity);
	    
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
	
	public String getComplexityTaskById(String idTask) throws SQLException
	{
		String sql = "select complexity from tasks where id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, idTask);
		ResultSet rs = statement.executeQuery();
		if( rs.next())
		{
			return rs.getString("complexity");
		}
		
		return null;
	}

	public ArrayList<Task> getAllTask() throws SQLException {

		ArrayList<Task> listAllTasks = new ArrayList<>();

		String sql = "SELECT * from tasks order by lp ASC";
		PreparedStatement statement =  connection.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) 
		{
			listAllTasks.add(new Task(rs.getString("id"), rs.getString("name"),rs.getString("description"),rs.getString("id_list"),rs.getString("marked"), rs.getString("complexity")));
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

	private String prepareSql(ArrayList<String> ids, Map<String, Integer> taskLpMap, int list){
		String partSql = Helper.taskIdLpMapToSqlString(taskLpMap);
		String idsString = StringUtils.join(ids, ",");
		
		String sql = "UPDATE tasks SET lp = (CASE id "+partSql+" END) WHERE id IN ("+idsString+") and id_list="+list;
		Logger.getGlobal().log(Level.SEVERE, sql);
		return sql;
	}
	
	public void moveTask(String task_id, int targetListId, HasComponents hasComponents) {
		 
		DDVerticalLayout ddv = (DDVerticalLayout)hasComponents;
		int compCount = ddv.getComponentCount();
		Map<String, Integer> taskLpMap = new LinkedHashMap<String, Integer>();
		ArrayList<String> ids = new ArrayList<String>();
		for(int i=0;i<compCount;i++)
		{
			Component c = ddv.getComponent(i);
			if(c instanceof Task){
				Task t = (Task)c;
				taskLpMap.put(t.getTask_id(), ddv.getComponentIndex(t));
				ids.add(t.getTask_id());
			}
		}
		
		
		try {
			Statement s = connection.createStatement();
			s.executeUpdate(prepareSql(ids, taskLpMap,targetListId));
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
