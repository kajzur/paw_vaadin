package com.paw.trelloplus.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class Comment extends CustomComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1829980200582383547L;
	private VerticalLayout v;
	private HorizontalLayout contentLayout;
	private HorizontalLayout ownerLayout;
	
	private int id;
	private int task_id;
	private String created;
	private String owner;
	private String content;
	public Comment(String owner, String content, int id, int task_id, Timestamp date) {
		this(owner, content, id, task_id, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
	}
	public Comment(String owner, String content, int id, int task_id,
			String currentDateAsString) {
		
		v= new VerticalLayout();
		setStyleName("comment");
		contentLayout = new HorizontalLayout();
		ownerLayout = new HorizontalLayout();
		created=currentDateAsString;
		this.id=id;
		this.task_id=task_id;
		
		
		
		Label contentLabel  = new Label(content);
		contentLabel.setWidth("100%");
		contentLayout.addComponent(contentLabel);
		Label ownerLabel  = new Label(owner+" ("+created+")");
		ownerLabel.setWidth("100%");
		ownerLayout.addComponent(ownerLabel);
		v.addComponent(contentLayout);
		v.addComponent(ownerLayout);
		setCompositionRoot(v);
	}
	

}
