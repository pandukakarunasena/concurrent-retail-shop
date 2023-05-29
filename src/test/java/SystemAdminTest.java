import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemAdminTest {

    private Store store;
    private SystemAdmin systemAdmin;
    private CountDownLatch latch;

    @BeforeEach
    public void setup() {
        // Create a store with some initial products
        store = new Store();
        store.addProductToStore(new Product("Apple", 55,10));
        store.addProductToStore(new Product("Banana", 55,20));
        store.addProductToStore(new Product("Orange", 40,15));

        // Create a latch to wait for restock events
        latch = new CountDownLatch(1);

        // Create the system admin
        int restockTime = 20; // milliseconds
        systemAdmin = new SystemAdmin(store, restockTime);
    }

    @AfterEach
    public void tearDown() {
        // Stop the system admin
        systemAdmin.stop();
    }

    @Test
    public void testProductRestocking() throws InterruptedException {
        // Start the system admin thread
        Thread systemAdminThread = new Thread(systemAdmin);
        systemAdminThread.start();

        // Verify that all products have been restocked
        Map<String, Product> products = store.getProducts();
        for (Product product : products.values()) {
            assertEquals(product.getMAX_QUANTITY(), product.getQuantity(), "All products should have been restocked");
        }
    }
}
