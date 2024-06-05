package de.ait_tr.g_40_shop.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


//@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Customer customer;

    private List<Product> products;

    // TODO - функционал корзины

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(customer, cart.customer) && Objects.equals(products, cart.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, products);
    }

    @Override
    public String toString() {
        return String.format("Cart: id - %d, contains %d products", id, products == null ? 0 : products.size());
    }

    Product addProduct(Product product){
        products.add(product);
        return product;
    }

    List<Product> getAllActiveProducts(){
        List<Product> productList = null;
        for(Product product : products){
            if(product.isActive())
                productList.add(product);
        }
        return productList;
    }

    void deleteProductById(Long id){
        products.forEach(x -> {
            if(x.getId() == id){
                products.remove(x);
        }
        });
    }

    void deleteAllProducts(){
        products.removeAll(products);
    }

    BigDecimal getCostOfActiveProducts(){
        BigDecimal cost = BigDecimal.valueOf(0);
        for (Product product : products){
            if(product.isActive()){
                cost.add(product.getPrice());
            }
        }
        return cost;
    }

    BigDecimal getAveragePriceOfProduct(){
        BigDecimal allCost = BigDecimal.valueOf(0);
        BigDecimal quantity = BigDecimal.valueOf(0);
        for (Product product : products){
            if (product.isActive()){
                allCost.add(product.getPrice());
                quantity.add(BigDecimal.valueOf(1));
            }
        }
        return allCost.divide(quantity);
    }


}
