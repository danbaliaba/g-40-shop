package de.ait_tr.g_40_shop.service.Interfaces;

import de.ait_tr.g_40_shop.domain.dto.CustomerDto;
import de.ait_tr.g_40_shop.domain.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {

    CustomerDto save(CustomerDto customer);
    List<CustomerDto> getAll();
    CustomerDto getById(Long id);
    CustomerDto update(CustomerDto customer);
    void deleteById(Long id);
    void deleteByName(String name);
    void restoreById(Long id);
    long getActiveCustomerQuantity();
    BigDecimal getAverageCost(Long id);
    BigDecimal getAveragePrice(Long id);
    Product addProduct(Long customerId, Long productId);
    void deleteProduct(Long customerId, Long product);
    void deleteAllProduct(Long id);
}
