package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.dto.CustomerDto;
import de.ait_tr.g_40_shop.domain.entity.Customer;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.repository.CustomerRepository;
import de.ait_tr.g_40_shop.service.Interfaces.CustomerService;
import de.ait_tr.g_40_shop.service.mapping.CustomerMappingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository repository;
    private final CustomerMappingService mappingService;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    @Override
    public CustomerDto save(CustomerDto dto) {

        Customer entity = mappingService.mapDtoToEntity(dto);
        repository.save(entity);
        return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<CustomerDto> getAll() {
        return repository.findAll()
                .stream().
                filter(Customer::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public CustomerDto getById(Long id) {
        Customer customer = repository.findById(id).orElse(null);
        if(customer == null || !customer.isActive()) {
            return null;
        }
        return mappingService.mapEntityToDto(customer);
    }

    @Override
    public CustomerDto update(CustomerDto customer) {
        return customer;
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
