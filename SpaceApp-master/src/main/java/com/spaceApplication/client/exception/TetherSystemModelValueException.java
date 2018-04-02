package com.spaceApplication.client.exception;

        import java.io.Serializable;

/**
 * Created by Chris
 */
public class TetherSystemModelValueException extends Exception implements Serializable {
    public TetherSystemModelValueException() {
        super();
    }

    public TetherSystemModelValueException(String message) {
        super(message);
    }
}
