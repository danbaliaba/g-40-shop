package de.ait_tr.g_40_shop.exception_handling.exceptions;

public class DeletedProductException extends RuntimeException{

    public DeletedProductException(String message) {
        super(message);
    }
}
