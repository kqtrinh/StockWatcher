package com.ostendotech.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class StockWatcher implements EntryPoint {
		
	public void onModuleLoad() {
	    
		MainView mainView = new MainView();
	    // add the main panel to the HTML element with the id "stockList"
		RootPanel rootPanel = RootPanel.get("stockList");
	    rootPanel.add(mainView);
	    
	    // This class here is to handle event from browser when user navigates out of
	    // its current page and later comes back. We want the ticker list to show
	    // rather than a blank list as if it's a new start
	    // Khai: Didn't work. We need the server side code to retain a copy of the
	    // stock list. So when user comes back from navigating away from the client
	    // app, the stock list can be refetched from the server
//	    AppController appViewer = new AppController();
//	    appViewer.go(rootPanel);
	}
}