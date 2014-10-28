package com.example.trelloplus;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Task extends CustomComponent  {

	public Task(String title, String desc) {
		this.setStyleName("task");
		HorizontalLayout l1 = new HorizontalLayout();
		l1.addComponent(new Label(title));
		
		HorizontalLayout l2 = new HorizontalLayout();
		l2.addComponent(new Label(desc));
		
		/*HorizontalLayout l3 = new HorizontalLayout();
		l3.addComponent(new Label(desc));*/
		
		VerticalLayout vt = new VerticalLayout();
		
		vt.addComponent(l1);
		vt.addComponent(l2);
		//vt.addComponent(l3);//
		
		setCompositionRoot(vt);
		
	}
	
}
