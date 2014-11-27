package com.paw.trelloplus.utils;

import com.mysql.jdbc.Connection;
import com.paw.trelloplus.components.CommentWindow;
import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.service.TaskService;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextArea;

public class SaveCommentClickButtonHandler implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2023457936704611355L;
	TextArea textArea;
	TaskService ts;
	Task task;
	public SaveCommentClickButtonHandler(TextArea contentArea, Task t) {
		textArea = contentArea;
		task = t;
		ts= new TaskService();
	}

	@Override
	public void buttonClick(ClickEvent event) {
		ts.setTask(task);
		int isSuccess = ts.addCommentToCurrentTask(textArea.getValue() );
		if(isSuccess > -1){
			CommentWindow cw = (CommentWindow)textArea.getParent().getParent().getParent();
			cw.addComment(textArea.getValue(),"Ty", isSuccess, task.getTask_id(), Helper.getCurrentDateAsString());
		}
		
	}

}
