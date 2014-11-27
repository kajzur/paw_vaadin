package com.paw.trelloplus.components;

import java.util.ArrayList;

import com.paw.trelloplus.models.Comment;
import com.paw.trelloplus.utils.SaveCommentClickButtonHandler;
import com.paw.trelloplus.views.LoginView;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CommentWindow extends Window {
	
	VerticalLayout vl;
	VerticalLayout commentsLayout;
	HorizontalLayout newCommentLayout;
	TextArea contentArea;
	Task task;
	public CommentWindow(Task task) {
		super("Komentarze");
		this.task = task;
		setModal(true);
		setWidth("50%");
		setHeight("50%");
		vl = new VerticalLayout();
		vl.setWidth("100%");
		commentsLayout = new VerticalLayout();
		commentsLayout.setWidth("100%");
		commentsLayout.setStyleName("comment-contener");
		newCommentLayout = new HorizontalLayout();
		newCommentLayout.setStyleName("new-comment-contener");
		prepareCommentForm();
		vl.addComponent(commentsLayout);
		vl.addComponent(newCommentLayout);
		setContent(vl);
	}
	
	public void setComments(ArrayList<Comment> comments){
		commentsLayout.removeAllComponents();
		for(Comment c : comments){
			commentsLayout.addComponent(c);
		}
	}
	
	private void prepareCommentForm(){
		contentArea = new TextArea();
		Button saveComment = new Button("Zapisz");
		saveComment.addClickListener(new SaveCommentClickButtonHandler(contentArea, task));
		newCommentLayout.addComponent(contentArea);
		newCommentLayout.addComponent(saveComment);
	}

	public void addComment(String value, String string,
			int isSuccess, String taskid, String currentDateAsString) {
		
		Comment c = new Comment(string, value, isSuccess, Integer.parseInt(taskid), currentDateAsString);
		commentsLayout.addComponent(c);
		
	}
}
