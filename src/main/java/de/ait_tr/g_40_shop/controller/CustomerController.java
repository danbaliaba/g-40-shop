package de.ait_tr.g_40_shop.controller;


import de.ait_tr.g_40_shop.domain.dto.CustomerDto;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.service.Interfaces.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }
    @PostMapping
    public CustomerDto save(@RequestBody CustomerDto customer) {

        return  service.save(customer);
    }

    @GetMapping
    public List<CustomerDto> get(@RequestParam(required = false) Long id) {
        if (id == null) {
            return service.getAllActiveCustomers();
        }else {
            CustomerDto customer = service.getActiveCustomerById(id);
            return customer == null ? null : List.of(customer);
        }
    }

    @PutMapping
    public CustomerDto update(CustomerDto customer) {
        service.update(customer);
        return customer;
    }

    @DeleteMapping
    public void delete(@RequestParam(required = false) Long id,
                       @RequestParam(required = false) String name) {
        if (id != null) {
            service.deleteById(id);
        } else if (name != null) {
            service.deleteByName(name);
        }
    }

    @PutMapping("/restore")
    public void restore(@RequestParam Long id) {
        service.restoreById(id);
    }

    @GetMapping("/number")
    public long getCustomersNumber() {
        return service.getActiveCustomersNumber();
    }

    @GetMapping("/cart-cost")
    public BigDecimal getCartTotalCost(@RequestParam Long customerId) {
        return service.getCartTotalCost(customerId);
    }

    @GetMapping("/avg-product-cost")
    public BigDecimal getAverageProductCost(@RequestParam Long customerId) {

        return service.getAverageProductCost(customerId);
    }

    @PostMapping("/add-product")
    public Product addProduct(@RequestParam Long customerId,@RequestParam Long productId) {
        service.addProductToCustomersCart(customerId, productId);
        return null;
    }

    @DeleteMapping("/delete-product")
    public void deleteProduct(@RequestParam Long customerId,@RequestParam Long productId) {
        service.removeProductFromCustomersCart(customerId, productId);
    }

    @DeleteMapping("/clear-cart")
    public void clearCart(@RequestParam Long id) {
        service.clearCart(id);
    }


}
