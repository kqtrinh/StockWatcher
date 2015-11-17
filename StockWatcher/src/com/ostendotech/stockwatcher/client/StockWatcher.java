package com.ostendotech.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class StockWatcher implements EntryPoint {
		
	public void onModuleLoad() {
	    
		MainView mainView = new MainView();
	    // add the main panel to the HTML element with the id "stockList"
	    RootPanel.get("stockList").add(mainView);	    
	}		
}