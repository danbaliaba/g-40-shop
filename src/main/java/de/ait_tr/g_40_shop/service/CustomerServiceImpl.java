package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.entity.Customer;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.repository.CustomerRepository;
import de.ait_tr.g_40_shop.service.Interfaces.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {

        customer.setId(null);
        customer.setActive(true);
        return repository.save(customer);
    }

    @Override
    public List<Customer> getAll() {
        return repository.findAll()
                .stream().
                filter(Customer::isActive)
                .toList();
    }

    @Override
    public Customer getById(Long id) {
        Customer customer = repository.findById(id).orElse(null);
        if(customer == null || !customer.isActive()) {
            return null;
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getActiveCustomerQuantity() {
        return 0;
    }

    @Override
    public BigDecimal getAverageCost(Long id) {
        return null;
    }

    @Override
    public BigDecimal getAveragePrice(Long id) {
        return null;
    }

    @Override
    public Product addProduct(Long customerId, Long productId) {
        return null;
    }

    @Override
    public void deleteProduct(Long customerId, Long productId) {

    }

    @Override
    public void deleteAllProduct(Long id) {

    }
}
