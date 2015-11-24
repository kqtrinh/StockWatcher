package com.ostendotech.stockwatcher.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements ValueChangeHandler<String> {

	private HasWidgets container;
	public AppController() {
		History.addValueChangeHandler(this);
	}

	public void go(final HasWidgets container) {
		this.container = container;
		if ("".equals(History.getToken())) {
			History.newItem("list");
		} 
		else {
			History.fireCurrentHistoryState();
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if (token != null) {
			go(container);
		}
	}
}
