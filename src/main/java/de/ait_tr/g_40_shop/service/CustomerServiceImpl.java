package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.dto.CustomerDto;
import de.ait_tr.g_40_shop.domain.entity.Customer;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.exception_handling.exceptions.ProductNotFoundException;
import de.ait_tr.g_40_shop.repository.CustomerRepository;
import de.ait_tr.g_40_shop.repository.ProductRepository;
import de.ait_tr.g_40_shop.service.Interfaces.CustomerService;
import de.ait_tr.g_40_shop.service.mapping.CustomerMappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final ProductRepository productRepository;
    private final CustomerMappingService mappingService;

    public CustomerServiceImpl(CustomerRepository repository, ProductRepository productRepository, CustomerMappingService mappingService) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.mappingService = mappingService;
    }

    @Override
    public CustomerDto save(CustomerDto dto) {

        Customer entity = mappingService.mapDtoToEntity(dto);
        repository.save(entity);
        return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<CustomerDto> getAllActiveCustomers() {
        return repository.findAll()
                .stream().
                filter(Customer::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public CustomerDto getActiveCustomerById(Long id) {
        Customer customer = repository.findById(id).orElse(null);
        if(customer == null || !customer.isActive()) {
            return null;
        }
        return mappingService.mapEntityToDto(customer);
    }

    @Override
    public CustomerDto update(CustomerDto customer) {
        Customer customerDB = repository.findById(customer.getId()).orElseThrow(() -> new ProductNotFoundException(String.format("Customer with id %d not found", customer.getId())));
        customerDB.setName(customer.getName());
        return mappingService.mapEntityToDto(customerDB);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }

    @Override
    @Transactional
    public void restoreById(Long id) {

        Customer customer = repository.findById(id).orElseThrow( () -> new RuntimeException(String.format("Customer with id %d not found", id)));
        if (!customer.isActive()){
            customer.setActive(true);
        }
    }

    @Override
    public long getActiveCustomersNumber() {
        return repository.findAll().stream().filter(Customer::isActive).count();
    }

    @Override
    public BigDecimal getCartTotalCost(Long customerId) {
        Customer customer = repository.findById(customerId).orElseThrow(() -> new RuntimeException(String.format("Customer with id %d not found", customerId)));
        return customer.getCart().getCartTotalCost();
    }

    @Override
    public BigDecimal getAverageProductCost(Long id) {
        Customer customer = repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Customer with id %d not found", id)));
        return customer.getCart().getAverageProductCost();
    }

    @Override
    public void addProductToCustomersCart(Long customerId, Long productId) {
        Customer customer = repository.findById(customerId).orElseThrow(() -> new RuntimeException(String.format("Customer with id %d not found", customerId)));
        customer.getCart().addProduct(productRepository.findById(productId).orElseThrow(() ->
                new RuntimeException(String.format("Product with id %d not found", productId))));
    }

    @Override
    public void removeProductFromCustomersCart(Long customerId, Long productId) {
        Customer customer = repository.findById(customerId).orElseThrow(() -> new RuntimeException(String.format("Customer with id %d not found", customerId)));
        customer.getCart().removeProductById(productId);
    }

    @Override
    public void clearCart(Long id) {
        Customer customer = repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Customer with id %d not found", id)));
        customer.getCart().clear();
    }
}
