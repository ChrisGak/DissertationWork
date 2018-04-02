package com.spaceApplication.client.exception;

import java.io.Serializable;

/**
 * Created by Chris
 */
public class DelistedException extends Exception implements Serializable {
    public DelistedException() {
        super();
    }

    public DelistedException(String message) {
        super(message);
    }
}
