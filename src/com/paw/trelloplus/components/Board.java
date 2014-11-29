package com.paw.trelloplus.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Board extends CustomComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8769513290840624388L;
	private String name;
	private String boardId;
	private int marked;

	public Board(String boardId,String name, int marked)
	{
		this.name = name;
		this.boardId = boardId;
		this.marked = marked;
		
		this.setStyleName("board");
		
		HorizontalLayout l1 = new HorizontalLayout();
		l1.addComponent(new Label(name));
		l1.setStyleName("list-header");
		VerticalLayout vt = new VerticalLayout();
		
		vt.addComponent(l1);
		
		setCompositionRoot(vt);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}
	public void setBoardId(String boardId)
	{
		this.boardId = boardId;
	}
	public String getBoardId()
	{
		return this.boardId;
	}

	public int getMarked() {
		return marked;
	}

	public void setMarked(int marked) {
		this.marked = marked;
	}
	
	
}
