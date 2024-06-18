package de.ait_tr.g_40_shop.exception_handling.exceptions;

public class ProductWithTitleNotFoundException extends RuntimeException{

    public ProductWithTitleNotFoundException(String productTitle){
        super(String.format("Product with title %s not found", productTitle));
    }
}
