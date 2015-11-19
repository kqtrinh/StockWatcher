package com.ostendotech.stockwatcher.client.service;

import java.util.ArrayList;

import com.ostendotech.stockwatcher.shared.StockRequest;

public interface StockPriceServiceClientIntf {
	void getPrices(ArrayList<StockRequest> symbols);
}
