package com.spaceApplication.client.exception;

import java.io.Serializable;

/**
 * Created by Chris
 */
public class SpaceModelException extends Exception implements Serializable {
    private String explanation;

    public SpaceModelException() {
    }

    public SpaceModelException(String symbol) {
        this.explanation = symbol;
    }

    public String getExplanation() {
        return this.explanation;
    }
}
