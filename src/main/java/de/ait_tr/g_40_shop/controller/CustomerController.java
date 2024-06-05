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

    CustomerService service;

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
            return service.getAll();
        }else {
            return List.of(service.getById(id));
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
    public void restore(Long id) {
        service.restoreById(id);
    }

    @GetMapping("/quantity")
    public long getQuantity() {
        return service.getActiveCustomerQuantity();
    }

    @GetMapping("/average-cost")
    public BigDecimal getCost(Long id) {
        return service.getAverageCost(id);
    }

    @GetMapping("/average-price")
    public BigDecimal getPrice(Long id) {
        return service.getAveragePrice(id);
    }

    @PostMapping("/add-product")
    public Product addProduct(Long customerId, Long productId) {
        service.addProduct(customerId, productId);
        return null;
    }

    @DeleteMapping("/delete-product")
    public void deleteProduct(Long customerId, Long productId) {
        service.deleteProduct(customerId, productId);
    }

    @DeleteMapping("/delete-all-product")
    public void deleteAllProduct(Long id) {
        service.deleteAllProduct(id);
    }


}
