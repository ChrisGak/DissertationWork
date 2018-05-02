package com.spaceApplication.server.modeling;

/**
 * Created by Chris
 */
public class VectorSizeCustomException extends IndexOutOfBoundsException {
    public VectorSizeCustomException(String message){
        super(message);
    }

    public VectorSizeCustomException(){
        super("Выход за границы размера вектора - размер должен быть равен 6.");
    }

}
