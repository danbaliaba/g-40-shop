package de.ait_tr.g_40_shop.controller;

import de.ait_tr.g_40_shop.domain.dto.ProductDto;
import de.ait_tr.g_40_shop.domain.entity.Product;
import de.ait_tr.g_40_shop.domain.entity.Role;
import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.repository.ProductRepository;
import de.ait_tr.g_40_shop.repository.RoleRepository;
import de.ait_tr.g_40_shop.repository.UserRepository;
import de.ait_tr.g_40_shop.security.sec_dto.TokenResponseDto;
import de.ait_tr.g_40_shop.service.mapping.ProductMappingService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProductRepository productRepository;

    private TestRestTemplate template;
    private HttpHeaders headers;
    private ProductDto testProduct;

    private final String TEST_PRODUCT_TITLE = "Test product title";
    private final BigDecimal TEST_PRODUCT_PRICE = new BigDecimal(777);
    private final String TEST_ADMIN_NAME = "Test Admin";
    private final String TEST_USER_NAME = "Test User";
    private final String TEST_PASSWORD = "Test password";
    private final String ADMIN_ROLE_TITLE = "ROLE_ADMIN";
    private final String USER_ROLE_TITLE = "ROLE_USER";

    private final String URL_PREFIX = "http://localhost:";
    private final String AUTH_RESOURCE_NAME = "/auth";
    private final String PRODUCTS_RESOURCE_NAME = "/products";
    private final String LOGIN_ENDPOINT = "/login";
    private final String ALL_ENDPOINT = "/all";
    private final String REQUESTED_PARAM = "?id=";
    private Long id;

    private String BEARER_PREFIX = "Bearer ";
    private String adminAccessToken;
    private String userAccessToken;
    private final String AUTH_HEADER_TITLE = "Authorization";

    @BeforeEach
    public void setUp() {

        template = new TestRestTemplate();
        headers = new HttpHeaders();

        testProduct = new ProductDto();
        testProduct.setTitle(TEST_PRODUCT_TITLE);
        testProduct.setPrice(TEST_PRODUCT_PRICE);

        BCryptPasswordEncoder encoder = null;
        Role roleAdmin;
        Role roleUser = null;

        User admin = userRepository.findByUsername(TEST_ADMIN_NAME).orElse(null);
        User user = userRepository.findByUsername(TEST_USER_NAME).orElse(null);

        if (admin == null) {
            encoder = new BCryptPasswordEncoder();
            roleAdmin = roleRepository.findByTitle(ADMIN_ROLE_TITLE).orElse(null);
            roleUser = roleRepository.findByTitle(USER_ROLE_TITLE).orElse(null);

            if (roleAdmin == null || roleUser == null) {
                throw new RuntimeException("Role admin or user is missing in the database");
            }

            admin = new User();
            admin.setUsername(TEST_ADMIN_NAME);
            admin.setPassword(encoder.encode(TEST_PASSWORD));
            admin.setRoles(Set.of(roleAdmin, roleUser));

            userRepository.save(admin);
        }

        if (user == null) {
            encoder = encoder == null ? new BCryptPasswordEncoder() : encoder;
            roleUser = roleUser == null ? roleRepository.findByTitle(USER_ROLE_TITLE).orElse(null) : roleUser;

            if (roleUser == null) {
                throw new RuntimeException("Role user is missing in the database");
            }

            user = new User();
            user.setUsername(TEST_USER_NAME);
            user.setPassword(encoder.encode(TEST_PASSWORD));
            user.setRoles(Set.of(roleUser));

            userRepository.save(user);
        }

        admin.setPassword(TEST_PASSWORD);
        admin.setRoles(null);

        user.setPassword(TEST_PASSWORD);
        user.setRoles(null);

        // http://localhost:${port}/auth/login
        String url = URL_PREFIX + port + AUTH_RESOURCE_NAME + LOGIN_ENDPOINT;
        HttpEntity<User> request = new HttpEntity<>(admin, headers);

        ResponseEntity<TokenResponseDto> response = template.exchange(url, HttpMethod.POST, request, TokenResponseDto.class);

        assertNotNull(response.getBody(), "Auth response body is empty");
        adminAccessToken = BEARER_PREFIX+response.getBody().getAccessToken();

        request = new HttpEntity<>(user, headers);

        response = template.exchange(url, HttpMethod.POST, request, TokenResponseDto.class);

        assertNotNull(response.getBody(), "Auth response body is empty");
        userAccessToken = BEARER_PREFIX+response.getBody().getAccessToken();
    }

    @Test
    public void positiveGettingAllProductsWithoutAuthorization() {

        String url = URL_PREFIX+port+PRODUCTS_RESOURCE_NAME+ALL_ENDPOINT;
        HttpEntity<Void> request = new HttpEntity<>(null);

        ResponseEntity<ProductDto[]> response = template.exchange(url, HttpMethod.GET, request, ProductDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has unexpected status");
        assertTrue(response.hasBody(), "Response has no body");
    }

    @Test
    public void negativeSavingProductsWithoutAuthorization(){

        String url = URL_PREFIX+port+PRODUCTS_RESOURCE_NAME;
        HttpEntity<ProductDto> request = new HttpEntity<>(testProduct,headers);

        ResponseEntity<ProductDto> response = template.exchange(url, HttpMethod.POST, request, ProductDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(),"Response has unexpected status");
        assertFalse(response.hasBody(), "Response has unexpected body");
    }

    @Test
    public void negativeSavingProductWithUserAuthorization(){

        String url = URL_PREFIX+port+PRODUCTS_RESOURCE_NAME;
        headers.put(AUTH_HEADER_TITLE, List.of(userAccessToken));
        HttpEntity<ProductDto> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDto> response = template.exchange(url, HttpMethod.POST, request, ProductDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response has unexpected status");
        assertFalse(response.hasBody(), "Response has unexpected body");
    }

    @Test
    @Order(1)
    public void positiveSavingProductWithAdminAuthorization(){

        String url = URL_PREFIX+port+PRODUCTS_RESOURCE_NAME;
        headers.put(AUTH_HEADER_TITLE, List.of(adminAccessToken));
        HttpEntity<ProductDto> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDto> response = template.exchange(url, HttpMethod.POST, request, ProductDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has unexpected status");
        assertTrue(response.hasBody(), "Response has unexpected body");
        assertEquals(response.getBody().getTitle(), testProduct.getTitle(), "Response has unexpected title");
        assertEquals(response.getBody().getPrice(), testProduct.getPrice(), "Response has unexpected price");

        id = response.getBody().getId();
        assertNotNull(id, "Response has unexpected id");

    }

    @Test
    @Order(2)
    public void negativeGettingProductByIdWithoutAuthorization(){

        Product testProductEntity = productRepository.findByTitle(testProduct.getTitle()).orElse(null);
        assertNotNull(testProductEntity, "Product is not in DB");

        String url = URL_PREFIX+port+PRODUCTS_RESOURCE_NAME+REQUESTED_PARAM+testProductEntity.getId();
        HttpEntity<Void> request = new HttpEntity<>(null);

        ResponseEntity<ProductDto[]> response =template.exchange(url, HttpMethod.GET, request, ProductDto[].class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response has unexpected status");
        assertFalse(response.hasBody(), "Response has unexpected body");
    }

    @Test
    @Order(3)
    public void negativeGettingProductByIdWithBasicAuthorization(){

        Product testProductEntity = productRepository.findByTitle(testProduct.getTitle()).orElse(null);
        assertNotNull(testProductEntity, "Product is not in DB");

        String url = URL_PREFIX+port+PRODUCTS_RESOURCE_NAME+REQUESTED_PARAM+testProductEntity.getId();
        HttpEntity<Void> request = new HttpEntity<>(null);
        

        ResponseEntity<ProductDto> response = template
                .withBasicAuth(TEST_USER_NAME, TEST_PASSWORD)
                .exchange(url, HttpMethod.GET, request, ProductDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response has unexpected status");
        assertFalse(response.hasBody(),"Response has unexpected body");
        
    }

    @Test
    @Order(4)
    public void negativeGettingProductByIdWithIncorrectToken(){

        Product testProductEntity = productRepository.findByTitle(testProduct.getTitle()).orElse(null);
        assertNotNull(testProductEntity, "Product is not in DB");

        String url = URL_PREFIX+port+PRODUCTS_RESOURCE_NAME+REQUESTED_PARAM+testProductEntity.getId();
        headers.put(AUTH_HEADER_TITLE, List.of(adminAccessToken+"Invalid Access Token"));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<ProductDto> response = template.exchange(url, HttpMethod.GET, request, ProductDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Response has unexpected status");
        assertFalse(response.hasBody(), "Response has unexpected body");

    }

    @Test
    @Order(5)
    public void positiveGettingProductByIdWithCorrectToken(){

//        Product testProductEntity = productRepository.findByTitle(testProduct.getTitle()).orElse(null);
//        assertNotNull(testProductEntity, "Product is not in DB");

        String url = URL_PREFIX+port+PRODUCTS_RESOURCE_NAME+REQUESTED_PARAM+id;
        headers.put(AUTH_HEADER_TITLE, List.of(userAccessToken));
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<ProductDto> response = template.exchange(url, HttpMethod.GET, request, ProductDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has unexpected status");
        assertTrue(response.hasBody(), "Response has unexpected body");
        assertEquals(response.getBody().getTitle(), testProduct.getTitle(), "Response has unexpected title");
        assertEquals(response.getBody().getPrice(), testProduct.getPrice(), "Response has unexpected price");

        productRepository.deleteById(id);
    }




}