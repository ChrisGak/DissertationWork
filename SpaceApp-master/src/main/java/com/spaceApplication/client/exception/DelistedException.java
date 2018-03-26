package com.spaceApplication.client.exception;

import java.io.Serializable;

/**
 * Created by Chris
 */
public class DelistedException extends Exception implements Serializable {
    private String symbol;

    public DelistedException() {
    }

    public DelistedException(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
