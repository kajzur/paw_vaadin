package com.paw.trelloplus;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = TrelloPlusUI.class, widgetset = "com.paw.trelloplus.widgetset.TrelloplussWidgetset")
public class Servlet extends VaadinServlet {

}
