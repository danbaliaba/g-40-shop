package de.ait_tr.g_40_shop.service.Interfaces;

import de.ait_tr.g_40_shop.domain.dto.CustomerDto;
import de.ait_tr.g_40_shop.domain.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {

    CustomerDto save(CustomerDto customer);
    List<CustomerDto> getAllActiveCustomers();
    CustomerDto getActiveCustomerById(Long id);
    CustomerDto update(CustomerDto dto);
    void deleteById(Long id);
    void deleteByName(String name);
    void restoreById(Long id);
    long getActiveCustomersNumber();
    BigDecimal getCartTotalCost(Long customerId);
    BigDecimal getAverageProductCost(Long id);
    void addProductToCustomersCart(Long customerId, Long productId);
    void removeProductFromCustomersCart(Long customerId, Long product);
    void clearCart(Long id);
}
