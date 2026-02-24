package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    // --- Konstanta ditambahkan di sini untuk mengatasi PMD AvoidDuplicateLiterals ---
    private static final String TEST_ID = "eb558e9f-1c39-460e-8860-71af6af63bd6";
    private static final String PRODUCT_BAMBANG = "Sampo Cap Bambang";
    private static final String PRODUCT_USEP = "Sampo Cap Usep";

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId(TEST_ID);
        product.setProductName(PRODUCT_BAMBANG);
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();

        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testCreateProductIdNull_shouldGenerateUUID() {
        Product product = new Product();
        product.setProductName(PRODUCT_BAMBANG);
        product.setProductQuantity(100);

        Product result = productRepository.create(product);

        assertNotNull(result.getProductId());
        assertFalse(result.getProductId().isBlank());

        Product found = productRepository.findById(result.getProductId());
        assertNotNull(found);
        assertEquals(PRODUCT_BAMBANG, found.getProductName());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId(TEST_ID);
        product1.setProductName(PRODUCT_BAMBANG);
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096"); // Yang ini tidak diprotes PMD karena cuma muncul sekali
        product2.setProductName(PRODUCT_USEP);
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());

        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());

        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProductPositive() {
        Product product = new Product();
        product.setProductId(TEST_ID);
        product.setProductName(PRODUCT_BAMBANG);
        product.setProductQuantity(100);
        productRepository.create(product);

        Product editedProduct = new Product();
        editedProduct.setProductId(TEST_ID);
        editedProduct.setProductName(PRODUCT_USEP);
        editedProduct.setProductQuantity(50);

        Product result = productRepository.edit(editedProduct);

        assertNotNull(result);
        assertEquals(PRODUCT_USEP, result.getProductName());
        assertEquals(50, result.getProductQuantity());

        Product foundProduct = productRepository.findById(TEST_ID);
        assertEquals(PRODUCT_USEP, foundProduct.getProductName());
    }

    @Test
    void testEditProductNegative() {
        Product product = new Product();
        product.setProductId(TEST_ID);
        product.setProductName(PRODUCT_BAMBANG);
        product.setProductQuantity(100);
        productRepository.create(product);

        Product editedProduct = new Product();
        editedProduct.setProductId("invalid-id");
        editedProduct.setProductName(PRODUCT_USEP);
        editedProduct.setProductQuantity(50);

        Product result = productRepository.edit(editedProduct);
        assertNull(result);
    }

    @Test
    void testDeleteProductPositive() {
        Product product = new Product();
        product.setProductId(TEST_ID);
        product.setProductName(PRODUCT_BAMBANG);
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.delete(TEST_ID);

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
        assertNull(productRepository.findById(TEST_ID));
    }

    @Test
    void testDeleteProductNegative() {
        Product product = new Product();
        product.setProductId(TEST_ID);
        product.setProductName(PRODUCT_BAMBANG);
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.delete("invalid-id");

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        assertNotNull(productRepository.findById(TEST_ID));
    }
}