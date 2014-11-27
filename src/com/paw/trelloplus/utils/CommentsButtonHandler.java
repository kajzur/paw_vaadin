package com.paw.trelloplus.utils;

import java.util.ArrayList;

import com.paw.trelloplus.components.CommentWindow;
import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.models.Comment;
import com.paw.trelloplus.service.TaskService;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class CommentsButtonHandler implements ClickListener {

	private Task task;
	private CommentWindow cw;
	private TaskService ts;
	
	public CommentsButtonHandler(Task task) {
		super();
		this.task = task;
		this.ts=new TaskService();
		
	}


	@Override
	public void buttonClick(ClickEvent event) {
		showWindow();
	}
	
	private void showWindow(){

		cw = new CommentWindow(task);
		ts.setTask(task);
		ArrayList<Comment> comments = ts.getCommentsToTaskByCurrentTask();
		cw.setComments(comments);
		task.getUI().addWindow(cw);
		
	}

}
