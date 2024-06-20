package de.ait_tr.g_40_shop.domain.entity;

import jakarta.persistence.*;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

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

    public void addProduct(Product product) {
        if (product.isActive())
            products.add(product);
    }

    public List<Product> getAllActiveProducts() {

        return products.stream().filter(Product::isActive).toList();
    }

    public void removeProductById(Long id) {

        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(id)) {
                iterator.remove();
                break;
            }
        }

    }

    public void clear() {
        products.clear();
    }

    public BigDecimal getCartTotalCost() {
//        BigDecimal cost = BigDecimal.valueOf(0);
//        for (Product product : products) {
//            if (product.isActive()) {
//                cost.add(product.getPrice());
//            }
//        }
//        return cost;

        return products.stream().filter(Product::isActive)
                .map(Product::getPrice)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getAverageProductCost() {
        long count = products.stream().filter(Product::isActive).count();

        return count == 0 ? BigDecimal.ZERO : getCartTotalCost().divide(new BigDecimal(count), RoundingMode.DOWN);
    }


}
