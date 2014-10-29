package com.example.trelloplus;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Board extends CustomComponent{

	public Board(String title)
	{
		this.setStyleName("board");
		
		HorizontalLayout l1 = new HorizontalLayout();
		l1.addComponent(new Label(title));
		l1.setStyleName("list-header");
		VerticalLayout vt = new VerticalLayout();
		
		vt.addComponent(l1);
		
		setCompositionRoot(vt);
	}
	
}
