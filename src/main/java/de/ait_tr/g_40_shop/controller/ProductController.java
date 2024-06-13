package de.ait_tr.g_40_shop.controller;


import de.ait_tr.g_40_shop.domain.dto.ProductDto;
import de.ait_tr.g_40_shop.exception_handling.Response;
import de.ait_tr.g_40_shop.exception_handling.exceptions.FirstTestException;
import de.ait_tr.g_40_shop.service.Interfaces.ProductService;
import org.springframework.http.HttpStatus;
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
    public ProductDto save(@RequestBody ProductDto product){
       return service.save(product);
    }


    // Get product - GET - http://localhost:8080/products

    @GetMapping
    public ProductDto getById(@RequestParam Long id) {
        return service.getById(id);
    }

    @GetMapping("/all")
    public List<ProductDto> getAll() {
        return service.getAllActiveProducts();
    }

    // Update product - PUT - http://localhost:8080/products

    @PutMapping
    public ProductDto update(@RequestBody ProductDto product){
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

    @GetMapping("/total-price")
    public BigDecimal getTotalPrice(){
        return service.getActiveProductsTotalPrice();
    }

    @GetMapping("/average-price")
    public BigDecimal getAveragePrice(){
        return service.getActiveProductsAveragePrice();
    }


    // 1 способ обработки исключений
    // ПЛЮС -  точечно настраиваем обработчик ошибок именно для данного контроллера,
    //         если нам требуется разная логика обработки исключений в разных контроллерах
    // МИНУС - если нам не требуется разная логика для разных контроллеров,
    //         придётся создавать такие одинаковые обработчики в каждом контроллере
    @ExceptionHandler(FirstTestException.class)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Response handleException(FirstTestException e){
        return new Response(e.getMessage());
    }

}
