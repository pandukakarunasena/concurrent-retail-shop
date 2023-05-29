import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CashierTest {

    private Store store;
    private ConcurrentLinkedQueue<Customer> customerQueue;
    private CountDownLatch latch;
    private Cashier[] cashiers;

    @BeforeEach
    public void setup() {
        // Create a store with some initial products
        store = new Store();
        store.addProductToStore(new Product("Apple", 55,20));

        // Create a customer queue with some sample customers
        customerQueue = new ConcurrentLinkedQueue<>();
        List<ShoppingItem> shoppingItemList1 = new ArrayList();
        shoppingItemList1.add(new ShoppingItem("Apple", 5));
        customerQueue.add(new Customer(store, shoppingItemList1));

        List<ShoppingItem> shoppingItemList2 = new ArrayList();
        shoppingItemList1.add(new ShoppingItem("Apple", 5));
        customerQueue.add(new Customer(store, shoppingItemList2));

        List<ShoppingItem> shoppingItemList3 = new ArrayList();
        shoppingItemList1.add(new ShoppingItem("Apple", 5));
        customerQueue.add(new Customer(store, shoppingItemList3));

        // Create a latch to wait for all cashiers to finish
        latch = new CountDownLatch(customerQueue.size());

        // Create cashiers
        int numCashiers = 2;
        cashiers = new Cashier[numCashiers];
        for (int i = 0; i < numCashiers; i++) {
            cashiers[i] = new Cashier(store, latch);
        }
    }

    @AfterEach
    public void tearDown() {
        // Stop the cashier threads
        for (Cashier cashier : cashiers) {
            cashier.stop();
        }
    }

    @Test
    public void testCashiersProcessing() throws InterruptedException {
        // Start cashier threads
        Thread[] cashierThreads = new Thread[cashiers.length];
        for (int i = 0; i < cashiers.length; i++) {
            cashierThreads[i] = new Thread(cashiers[i], "Cashier " + (i + 1));
            cashierThreads[i].start();
        }

        // Assert that all customers have been processed
        assertEquals(0, store.getCustomerQueue().size(), "All customers should have been processed");
    }
}
