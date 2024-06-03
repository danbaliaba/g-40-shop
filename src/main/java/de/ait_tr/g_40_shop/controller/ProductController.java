package de.ait_tr.g_40_shop.controller;


import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.service.Interfaces.ProductService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // CRUD - Create(POST), Read(GET), Update(PUT), Delete(DELETE)

    // Create product - POST - http://localhost:8080/products

    @PostMapping
    public Product save(@RequestBody Product product){
       service.save(product);
        return product;
    }


    // Get product - GET - http://localhost:8080/products

    @GetMapping("products")
    public List<Product> get(@RequestParam(required = false) Long id){
        if(id == null){
            service.getAllActiveProducts();
        } else{
            service.getById(id);
        }
        return null;
    }

    // Update product - PUT - http://localhost:8080/products

    @PutMapping
    public Product update(@RequestBody Product product){
        service.update(product);
        return product;
    }

    // Delete product - DELETE - http://localhost:8080/products?id=3

    @DeleteMapping
    public void delete(@RequestParam(required = false) Long id, @RequestParam(required = false) String title){

        if(id != null){
            service.deleteById(id);
        } else if(title != null){
            service.deleteByTitle(title);
        }
    }

    // Restore product - PUT - http://localhost:8080/products/restore

    @PutMapping("/restore")
    public void restore(@RequestParam Long id){
        service.restoreById(id);
    }

    @GetMapping("/quantity")
    public long getQuantity(){
        return service.getActiveProductsQuantity();
    }

    @GetMapping("/totalPrice")
    public BigDecimal getTotalPrice(){
        return service.getActiveProductsTotalPrice();
    }

    @GetMapping("/averagePrice")
    public BigDecimal getAveragePrice(){
        return service.getActiveProductsAveragePrice();
    }



}
