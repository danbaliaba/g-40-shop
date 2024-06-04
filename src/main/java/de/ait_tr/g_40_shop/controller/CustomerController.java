package de.ait_tr.g_40_shop.controller;


import de.ait_tr.g_40_shop.domain.entity.Customer;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.service.Interfaces.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    CustomerService service;

    public void setService(CustomerService service) {
        this.service = service;
    }


    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        service.save(customer);
        return customer;
    }

    @GetMapping
    public List<Customer> get(@RequestParam(required = false) Long id) {
        if (id == null) {
            service.getAll();
        } else {
            service.getById(id);
        }
        return List.of();
    }

    @PutMapping
    public Customer update(Customer customer) {
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
    public void restore(Long id) {
        service.restoreById(id);
    }

    @GetMapping("/quantity")
    public long getQuantity() {
        return service.getActiveCustomerQuantity();
    }

    @GetMapping("/averageCost")
    public BigDecimal getCost(Long id) {
        return service.getAverageCost(id);
    }

    @GetMapping("/averagePrice")
    public BigDecimal getPrice(Long id) {
        return service.getAveragePrice(id);
    }

    @PostMapping("/addProduct")
    public Product addProduct(Long customerId, Long productId) {
        service.addProduct(customerId, productId);
        return null;
    }

    @DeleteMapping("/deleteProduct")
    public void deleteProduct(Long customerId, Long productId) {
        service.deleteProduct(customerId, productId);
    }

    @DeleteMapping("/deleteAllProduct")
    public void deleteAllProduct(Long id) {
        service.deleteAllProduct(id);
    }


}
