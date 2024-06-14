package de.ait_tr.g_40_shop.exception_handling.exceptions;

public class AlreadyExistingProductException extends RuntimeException{

    public AlreadyExistingProductException(String message) {
        super(message);
    }
}
