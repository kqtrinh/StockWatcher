package com.ostendotech.stockwatcher.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ostendotech.stockwatcher.client.StockPrice;
import com.ostendotech.stockwatcher.client.StockRequest;

public interface StockPriceServiceAsync {
	void getPrices(ArrayList<StockRequest> symbols, AsyncCallback<StockPrice[]> callback);
}
