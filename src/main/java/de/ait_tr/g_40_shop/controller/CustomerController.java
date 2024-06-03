package de.ait_tr.g_40_shop.controller;


import de.ait_tr.g_40_shop.domain.entity.Customer;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.service.Interfaces.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/customer")
public class CustomerController {

    CustomerService service;

    public void setService(CustomerService service) {
        this.service = service;
    }


    @PostMapping("/customer")
    public Customer save(@RequestBody Customer customer){
        service.save(customer);
        return customer;
    }

    @GetMapping("/customer")
    public List<Customer> get(@RequestParam(required = false) Long id){
        if(id == null){
            service.getAll();
        }else{
            service.getById(id);
        }
        return null;
    }

    @PutMapping("/customer")
    public Customer update(Customer customer){
        service.update(customer);
        return customer;
    }

    @DeleteMapping("/customer")
    public void delete(@RequestParam(required = false) Long id,
                       @RequestParam(required = false) String name){
        if(id != null){
            service.deleteById(id);
        } else if (name != null) {
            service.deleteByName(name);
        }
    }

    @PutMapping("/customer/restore")
    public void restore(Long id){
        service.restoreById(id);
    }

    @GetMapping("/customer/quantity")
    public long getQuantity(){
        return service.getActiveCustomerQuantity();
    }

    @GetMapping("/customer/averageCost")
    public BigDecimal getCost(Long id){
        return service.getAverageCost(id);
    }

    @GetMapping("/customer/averagePrice")
    public BigDecimal getPrice(Long id){
        return service.getAveragePrice(id);
    }

    @PostMapping("/customer/addProduct")
    public Product addProduct(Long id){
        service.addProduct(id);
        return null;
    }

    @DeleteMapping("/customer/deleteProduct")
    public void deleteProduct(Long id){
        service.deleteProduct(id);
    }

    @DeleteMapping("customer/deleteAllProduct")
    public void deleteAllProduct(Long id){
        service.deleteAllProduct(id);
    }




}
