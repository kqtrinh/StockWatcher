package com.ostendotech.stockwatcher.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DelistedException extends Exception implements IsSerializable {
	  
	private static final long serialVersionUID = 1L;
	private String errMsg;
	private String symbol;
	  
	public DelistedException() {
	}
	  
	public DelistedException(String symbol, String errMsg) {
		this.symbol = symbol;
	    this.errMsg = errMsg;
	}
	public String getSymbol() {
	    return this.symbol;
	}  
	  
	public String getErrMsg() {
	    return this.errMsg;
	}  
}