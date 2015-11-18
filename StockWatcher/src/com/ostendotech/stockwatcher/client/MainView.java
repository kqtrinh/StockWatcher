package com.ostendotech.stockwatcher.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainView extends Composite {
	
	private static final int REFRESH_INTERVAL = 5000; // ms
	private VerticalPanel mainPanel = new VerticalPanel();	
	private FlexTable stocksFlexTable = new FlexTable();
	private TickerView tickerView = null;
	private Label lastUpdatedLabel = new Label();	
	//private Label errorMsgLabel = new Label();
	private long errMsgShowTime = System.currentTimeMillis();
	
	/**
	 * get client's time zone
	 * @param type
	 * @return
	 */
	private String getTimeZone() {
        return DateTimeFormat.getFormat("Z").format(new Date()); //like "-0700"
	}
	private boolean isMarketOpen() {
		boolean isRefresh = false;
		int hr = new Date().getHours();
		String tzID = getTimeZone();
		if(tzID.equals("-1100")) {	// Hawaii
    		// only query for the stock list quotes within market open time
			if(hr > 3 && hr < 10) isRefresh = true;
		}
		else if(tzID.equals("-0800")) {	// PST
    		// only query for the stock list quotes within market open time
			if(hr > 6 && hr < 13) isRefresh = true;
		}
		else if(tzID.equals("-0700")) {	// MST
    		// only query for the stock list quotes within market open time
			if(hr > 7 && hr < 14) isRefresh = true;
		}
		else if(tzID.equals("-0600")) {	// CST
    		// only query for the stock list quotes within market open time
			if(hr > 8 && hr < 15) isRefresh = true;
		}
		else if(tzID.equals("-0500")) {	// EST
    		// only query for the stock list quotes within market open time
			if(hr > 9 && hr < 16) isRefresh = true;
		}
		return isRefresh;
	}
	private long getErrMsgShowTime() {
		return this.errMsgShowTime;
	}
	public FlexTable getStocksFlexTable() {
		return stocksFlexTable;
	}
	public void setStocksFlexTable(FlexTable stocksFlexTable) {
		this.stocksFlexTable = stocksFlexTable;
	}

	@SuppressWarnings("deprecation")
	public MainView() {
		
		initWidget(this.mainPanel);
	    
		this.tickerView = new TickerView(this);
		// set up stock list table
		this.stocksFlexTable.setText(0, 0, "Symbol");
	    this.stocksFlexTable.setText(0, 1, "Price");
	    this.stocksFlexTable.setText(0, 2, "Change");
	    this.stocksFlexTable.setText(0, 3, "Remove");
	    
	    this.stocksFlexTable.setCellPadding(5);
	    this.stocksFlexTable.addStyleName("watchList");
	    this.stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
	    this.stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
	    this.stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
	    this.stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");

	    // add widgets in the main view
	    this.mainPanel.add(this.lastUpdatedLabel);	
		this.mainPanel.add(this.tickerView);		
	    this.mainPanel.add(this.stocksFlexTable);
	    
		// change the last update timestamp
		lastUpdatedLabel.setText("Last update : " + 
		DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM).format(new Date()));

		// setup timer to refresh list automatically
	    Timer refreshTimer = new Timer() {
	    	public void run() {
	    		// refreshWatchList() returns true if there are stocks in the TikerView to be refreshed
	    		// refreshWatchList() returns false if the stock list is empty. When the list id empty,
	    		// refreshWatchList() won't send a request to the server to prevent redundant traffic
	    		// sent to the server. The last update time should not be updated if false		    		
	    		if(isMarketOpen()) {
		    		if(tickerView.refreshWatchList()) {
			    		// change the last update timestamp
//			    		lastUpdatedLabel.setText("Last update : " + 
//			    		DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM).format(new Date()));	    			
		    		}	    			
    			}
	    	}
	    };
	    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}
	public Label getLastUpdatedLabel() {
		return lastUpdatedLabel;
	}
	public void setLastUpdatedLabel(Label lastUpdatedLabel) {
		this.lastUpdatedLabel = lastUpdatedLabel;
	}	
}
