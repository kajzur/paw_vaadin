package com.paw.trelloplus;

import com.paw.trelloplus.views.LoginView;
import com.paw.trelloplus.views.TasksView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("trelloplus")
public class TrelloPlusUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4444221129590707715L;

	@Override
	protected void init(VaadinRequest request) {

		new Navigator(this, this);

		getNavigator().addView("", LoginView.class);
		getNavigator().addView(TasksView.NAME, TasksView.class);

		getNavigator().addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {

				boolean isLoggedIn = getSession().getAttribute("user") != null;
				boolean isLoginView = event.getNewView() instanceof LoginView;

				if (!isLoggedIn && !isLoginView) {

					getNavigator().navigateTo("");
					return false;

				} else if (isLoggedIn && isLoginView) {

					return false;
				}

				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {

			}
		});
	}
}
