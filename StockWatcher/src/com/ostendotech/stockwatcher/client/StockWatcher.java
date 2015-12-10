package com.ostendotech.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.RootPanel;

public class StockWatcher implements EntryPoint {
		
	public void onModuleLoad() {
	    
	    String platform = Navigator.getPlatform();
	    String userAgent = Navigator.getUserAgent();
	    MainView mainView = new MainView();
//	    Window.alert(platform);
//	    Window.alert(userAgent);
	    
	    // must be mobile (iOS or Android(Linux) )
	    if(!platform.equals("Win32")) {
	    	// we want the 
	    }
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