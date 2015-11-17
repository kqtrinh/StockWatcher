package com.ostendotech.stockwatcher.client.service;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.ostendotech.stockwatcher.client.DelistedException;
import com.ostendotech.stockwatcher.client.MainView;
import com.ostendotech.stockwatcher.client.StockPrice;
import com.ostendotech.stockwatcher.client.StockRequest;
import com.ostendotech.stockwatcher.client.TickerView;

public class StockPriceServiceClientImpl implements StockPriceServiceClientIntf {

	private MainView mainView;
	private TickerView tickerView;
	private StockPriceServiceAsync service;
	
	// accept the url of the stock price service from the server
	public StockPriceServiceClientImpl(MainView mainView, TickerView tickerView, String url) {
		System.out.println("StockPrice URL: " + url);
		this.service = GWT.create(StockPriceService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget)this.service;
		endpoint.setServiceEntryPoint(url);
		this.mainView = mainView;
		this.tickerView = tickerView;
	}
	private class DefaultCallback implements AsyncCallback<StockPrice[]> {

		@Override
		public void onFailure(Throwable caught) {
			System.out.println("An Server Error has occured");
			// display the error message above the watch list
			getTickerView().removeStock(((DelistedException)caught).getSymbol());
		}

		@Override
		public void onSuccess(StockPrice[] prices) {
			System.out.println("Response from server received");
			getTickerView().updateTable(prices);
		}
	}
	// get the main view
	private MainView getMainView() {
		return this.mainView;
	}

	private TickerView getTickerView() {
		return this.tickerView;
	}
	@Override
	public void getPrices(ArrayList<StockRequest> symbols) {
		this.service.getPrices(symbols, new DefaultCallback());		
	}
}
