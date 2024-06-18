package de.ait_tr.g_40_shop.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Название должно быть длиной хотя бы 3 символа
    // Название должно начинаться с заглавной буквы
    // Остальные буквы в названии должны быть строчными латинскими (разрешаются пробелы)
    // Название не должно содержать цифры и служебные символы
    // Название не должно быть null
    // Banana - V
    // Ba - X
    // banana - X
    // BANANA - X
    // BananA - X
    // Banana7 - X
    // Banana# - X

    @NotNull(message = "Product title can`t be null")
    @NotBlank(message = "Product title can`t be empty")
    @Pattern(
            regexp = "[A-Z][a-z ]{2,}",
            message = "Product title should be at least 3 character length" +
                    "and start with capital letter"
    )
    @Column(name = "title")
    private String title;

    //    @Min(5)
//    @Max(100000)
    @DecimalMin(
            value = "5.00",
            message = "Product price should be greater or equal than 5.00"
    )
    @DecimalMax(
            value = "100000.00",
            inclusive = false,
            message = "Product price should be less than 100000.00"
    )
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "active")
    private boolean active;

    @Column(name = "image")
    private String image;

    @Column(name = "quantity")
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return active == product.active && quantity == product.quantity && Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(price, product.price) && Objects.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, active, image, quantity);
    }

    @Override
    public String toString() {
        return String.format("Product: id - %d, title - %s, price - %s, active - %s, quantity - %d", id, title, price, active ? "yes" : "no", quantity);
    }

}
