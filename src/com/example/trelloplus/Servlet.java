package com.example.trelloplus;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/main", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = TrelloplusUI.class)
public class Servlet extends VaadinServlet {

}
