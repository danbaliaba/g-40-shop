package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.dto.ProductDto;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.exception_handling.exceptions.AlreadyExistingProductException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.DeletedProductException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.ProductNotFoundException;
import de.ait_tr.g_40_shop.repository.ProductRepository;
import de.ait_tr.g_40_shop.service.Interfaces.ProductService;
import de.ait_tr.g_40_shop.service.mapping.ProductMappingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

//    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;
    private final ProductMappingService mappingService;


    public ProductServiceImpl(ProductRepository repository, ProductMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }


    @Override
    public ProductDto save(ProductDto dto) {

        Product entity = mappingService.mapDtoToEntity(dto);
        Product dbEntity = repository.findByTitle(dto.getTitle()).orElse(null);
        if(dbEntity != null)
            throw new AlreadyExistingProductException(String.format("Product with title %s already exist", dbEntity.getTitle()));
        repository.save(entity);

        return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<ProductDto> getAllActiveProducts() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
        /*
         * List<Product> products = repository.findAll();
         * Iterator<Product> iterator = products.iterator();
         *
         * while(iterator.hasNext){
         *   if(!iterator.next.isActive()){
         *       iterator.remove();
         *   }
         * }
         *
         * return products;
         * */
    }

//    @Override
//    public ProductDto getById(Long id) {

    //  Демонстрация логирования
//
//        logger.info("Method getById called with parameter {}", id);
//        logger.warn("Method getById called with parameter {}", id);
//        logger.error("Method getById called with parameter {}", id);
//
//        Product product = repository.findById(id).orElse(null);
//        if (product == null || !product.isActive()) {
//            return null;
//        }
//        return mappingService.mapEntityToDto(product);
//    }

    @Override
    public ProductDto getById(Long id) {

        Product product = repository.findById(id).orElse(null);

        if (product == null ) {
            throw new ProductNotFoundException(String.format("Product with id %d not found", id));
        }
        else if (!product.isActive()){
            throw new DeletedProductException(String.format("Product with id %d is deleted", id));
        }
        return mappingService.mapEntityToDto(product);
    }

    @Override
    public ProductDto update(ProductDto product) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

        Product product = repository.findById(id).orElse(null);
        if (product == null ) {
            throw new ProductNotFoundException(String.format("Product with id %d not found", id));
        }
        else if (!product.isActive()){
            throw new DeletedProductException(String.format("Product with id %d is deleted", id));
        }
        product.setActive(true);

    }

    @Override
    public void deleteByTitle(String title) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getActiveProductsQuantity() {
        return 0;
    }

    @Override
    public BigDecimal getActiveProductsTotalPrice() {
        return null;
    }

    @Override
    public BigDecimal getActiveProductsAveragePrice() {
        return null;
    }
}
