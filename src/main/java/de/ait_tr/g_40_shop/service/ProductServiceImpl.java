package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.dto.ProductDto;
import de.ait_tr.g_40_shop.domain.dto.ProductSupplyDto;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.exception_handling.exceptions.AlreadyExistingProductException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.DeletedProductException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.ProductNotFoundException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.ProductWithTitleNotFoundException;
import de.ait_tr.g_40_shop.repository.ProductRepository;
import de.ait_tr.g_40_shop.service.Interfaces.ProductService;
import de.ait_tr.g_40_shop.service.mapping.ProductMappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d not found", id)));

        if (!product.isActive()){
            throw new DeletedProductException(String.format("Product with id %d is deleted", id));
        }

        return mappingService.mapEntityToDto(product);
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto product) {
        Product productDB = repository.findById(product.getId()).orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d not found", product.getId())));
        productDB.setPrice(product.getPrice());
        productDB.setQuantity(product.getQuantity());
        return mappingService.mapEntityToDto(productDB);
    }

    @Override
    public void deleteById(Long id) {

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d not found", id)));

        if (!product.isActive()){
            throw new DeletedProductException(String.format("Product with id %d is deleted", id));
        }
        product.setActive(true);

    }

    @Override
    @Transactional
    public void attachImage(String imageUrl, String productTitle) {

        Product product = repository.findByTitle(productTitle).orElseThrow(
//                () -> new ProductNotFoundException(String.format("Product with title %s not found", productTitle))
                () -> new ProductWithTitleNotFoundException(productTitle)
        );

        product.setImage(imageUrl);
    }

    @Override
    public List<ProductSupplyDto> getProductsForSupply() {
        return repository.findAll()
                .stream().filter(Product::isActive)
                .map(mappingService::mapEntityToSupplyDto)
                .toList();
    }

    @Override
    public void deleteByTitle(String title) {
        repository.deleteByTitle(title);
    }

    @Override
    @Transactional
    public void restoreById(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Product with id %d not found", id)));
        if (!product.isActive())
            product.setActive(true);
    }

    @Override
    public long getActiveProductsQuantity() {
        return repository.findAll().stream().filter(Product::isActive).count();
    }

    @Override
    public BigDecimal getActiveProductsTotalPrice() {
        return new BigDecimal(repository.findAll().stream().filter(Product::isActive).map(Product::getPrice).count());
    }

    @Override
    public BigDecimal getActiveProductsAveragePrice() {
        return getActiveProductsTotalPrice().divide(new BigDecimal(repository.findAll().stream().filter(Product::isActive).count()), RoundingMode.DOWN);
    }

}
