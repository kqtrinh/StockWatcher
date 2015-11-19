package com.ostendotech.stockwatcher.client;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ostendotech.stockwatcher.client.service.StockPriceServiceClientImpl;
import com.ostendotech.stockwatcher.shared.StockPrice;
import com.ostendotech.stockwatcher.shared.StockRequest;

public class TickerView extends Composite {

	private HorizontalPanel tickerPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addButton = new Button("Add");	
	private ArrayList<StockRequest> stocks = new ArrayList<StockRequest>();
	private MainView mainView = null;
	private FlexTable stocksFlexTable = null; 
	private Label lastUpdatedLabel = null;
	private StockPriceServiceClientImpl stockPriceSvc = null;	
		
	private int getStockListIndexOf(String symbol) {
		for(int i=0; i< stocks.size();i++) {
			if(stocks.get(i).getSymbol().equals(symbol))
				return i;
		}
		return -1;			
	}
	private boolean isStockListContains(String symbol) {		
		int len = stocks.size();
		for(int i=0; i< len;i++) {
			String stockSym = stocks.get(i).getSymbol();
			if(stockSym.equals(symbol))
				return true;
		}
		return false;		
	}
	private void addStocks() {
		final String symbols = newSymbolTextBox.getText().toUpperCase().trim();
		String[] symsArray = symbols.split("[,;\\t]");
	    for(String symbol : symsArray) {	    	
	    	addStock(symbol.trim());
	    }
		// get stock prices
		refreshWatchList();
	}
	private void addStock(final String symbol) {
		
		newSymbolTextBox.setFocus(true);
		  
		// symbol must be between 1 and 10 chars that are numbers, letters, or dots
		if (!symbol.matches("^[0-9a-zA-Z\\.]{1,10}$")) {
		    Window.alert("'" + symbol + "' is not a valid symbol to lookup.");
		    newSymbolTextBox.selectAll();
		    return;
		}
		newSymbolTextBox.setText("");
		
		// now we need to add the stock to the list...
		// don't add the stock if it's already in the watch list
		if (isStockListContains(symbol))
		  return;

		// add the stock to the list
		int row = stocksFlexTable.getRowCount();
		StockRequest stockData = new StockRequest(row, symbol);
		boolean b = stocks.add(stockData);
		if(b) {
			stocksFlexTable.setWidget(row, 2, new Label());
			stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
			stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
			stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

			// add button to remove this stock from the list
			Button removeStock = new Button("x");
			removeStock.setVisible(false);
			removeStock.addStyleDependentName("remove");
			removeStock.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					removeStock(symbol);
				}      
			});
			stocksFlexTable.setWidget(row, 3, removeStock);			
		}
	}
	@SuppressWarnings("deprecation")
	private void updateTable(StockPrice stock) {
		String symbol = stock.getSymbol();
		// make sure the stock is still in our watch list
		if (!isStockListContains(symbol)) {
		    return;
		}		
		// make the ticker symbol as hyperlink in stocksFlexTable
		int row = getStockListIndexOf(symbol)+1;
		//stocksFlexTable.setText(row, 0, symbol);
		final Hyperlink nameLink = new Hyperlink ();
		nameLink.setHTML (symbol);
		nameLink.setTargetHistoryToken ("edit="+new Integer (row));
		nameLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://finance.yahoo.com/q?s=" + nameLink.getText(), "_blank", "");
			}			
		});
		stocksFlexTable.setWidget (row, 0, nameLink);
		
		Button removeStock = (Button)stocksFlexTable.getWidget(row, 3);
		removeStock.setVisible(true);

		if(stock.isValidSymbol()) {
			// apply nice formatting to stock and change
			String priceText = NumberFormat.getFormat("#,##0.00").format(stock.getPrice());
			NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
			String changeText = changeFormat.format(stock.getChange());
			String changePercentText = changeFormat.format(stock.getChangePercent());
	
			// update the watch list with the new values
			stocksFlexTable.setText(row, 1, priceText);
			Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
			changeWidget.setText(changeText + " (" + changePercentText + "%)");
	
			String changeStyleName = "noChange";
			if (stock.getChangePercent() < -0.1f) {
			    changeStyleName = "negativeChange";
			}
			else if (stock.getChangePercent() > 0.1f) {
			    changeStyleName = "positiveChange";
			}
			changeWidget.setStyleName(changeStyleName);			
		}
		else {
			stocksFlexTable.setText(row, 1, "");
			stocksFlexTable.setText(row, 2, "INVALID");
		}
	}

	@SuppressWarnings("deprecation")
	public TickerView(MainView mainView) {
		
		initWidget(this.tickerPanel);
		this.stockPriceSvc = new StockPriceServiceClientImpl(mainView, this, GWT.getModuleBaseURL() + "stockprices");
		this.mainView = mainView;
		this.stocksFlexTable = this.mainView.getStocksFlexTable();
		this.lastUpdatedLabel = this.mainView.getLastUpdatedLabel();
		
		// set up event listeners for adding a new stock
		this.addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addStocks();
			}
		});
		newSymbolTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
			@Override
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			    if (keyCode == KEY_ENTER) {
			      addStocks();
			    }
			}
		});

		// move cursor focus to the text box
		this.newSymbolTextBox.setFocus(true);
	    
	    // assemble Add ticker panel
		this.tickerPanel.add(this.newSymbolTextBox);
		this.tickerPanel.add(this.addButton);
		this.tickerPanel.addStyleName("addPanel");		
	}
	public void removeStock(String symbol) {
	     int removedIndex = getStockListIndexOf(symbol);
	     stocks.remove(removedIndex);
	     stocksFlexTable.removeRow(removedIndex+1);	
	}	
	// This piece of code calls server side getPrices() to fetch the price list from server
	public boolean refreshWatchList() {
		// make the call to the stock price service on the server
		if(stocks.isEmpty()) return false;
		stockPriceSvc.getPrices(stocks);
		return true;
	}
	public void updateTable(StockPrice[] prices) {
		for (int i=0; i<prices.length; i++) {
		    updateTable(prices[i]);
		}		
		// change the last update timestamp
		lastUpdatedLabel.setText("Last update : " + prices[0].getTime());
	}
}
