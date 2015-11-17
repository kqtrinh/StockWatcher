package com.ostendotech.stockwatcher.client.service;

import java.util.ArrayList;

import com.ostendotech.stockwatcher.client.StockRequest;

public interface StockPriceServiceClientIntf {
	void getPrices(ArrayList<StockRequest> symbols);
}
