import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Test Product", 10.0f, 100);
    }

    @Test
    void testAddToCart() {
        assertFalse(product.addToCart(200)); // Product quantity is 0 initially
        product.setQuantity(100);
        assertTrue(product.addToCart(50)); // Sufficient quantity available
        assertFalse(product.addToCart(200)); // Insufficient quantity available
    }

    @Test
    void testPurchase() {
        product.setQuantity(50);
        assertDoesNotThrow(() -> product.purchase(50)); // Sufficient quantity available
        assertEquals(0, product.getQuantity()); // Product quantity should be 0 after purchase
    }

    @Test
    void testRestock() {
        product.setQuantity(50);
        product.restock();
        assertEquals(100, product.getQuantity()); // Product quantity should be restocked to the max quantity
    }
}
