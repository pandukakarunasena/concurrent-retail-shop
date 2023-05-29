import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerTest {

    @Test
    public void testCustomerShoppingAndCheckout() throws InterruptedException {
        // Create a store with some initial products
        Store store = new Store();
        store.addProductToStore(new Product("Apple", 55,10));
        store.addProductToStore(new Product("Banana", 55,20));
        store.addProductToStore(new Product("Orange", 40,15));

        // Create a customer queue
        ConcurrentLinkedQueue<Customer> customerQueue = new ConcurrentLinkedQueue<>();

        // Create a latch to wait for customer to finish shopping
        CountDownLatch latch = new CountDownLatch(1);

        // Create a shopping list for the customer
        List<ShoppingItem> shoppingList = new ArrayList<>();
        shoppingList.add(new ShoppingItem("Apple", 5));
        shoppingList.add(new ShoppingItem("Banana", 3));

        // Create the customer
        Customer customer = new Customer(store, shoppingList) {
            @Override
            public void run() {
                super.run();
                latch.countDown();
            }
        };

        // Start the customer thread
        Thread customerThread = new Thread(customer);
        customerThread.start();

        // Wait for the customer to finish shopping
        latch.await();

        // Verify that the customer has added items to the cart
        Cart cart = customer.getCart();
        List<ShoppingItem> addedItems = cart.getAddedShoppingItems();
        assertEquals(2, addedItems.size(), "Customer should have added items to the cart");

        // Verify that the customer's shopping list is empty
        assertTrue(shoppingList.isEmpty(), "Customer's shopping list should be empty");

        // Verify that the customer has been added to the store's customer queue
        assertEquals(1, store.getCustomerQueue().size(), "Customer should have been added to the customer queue");
        assertTrue(store.getCustomerQueue().contains(customer), "Customer should be in the customer queue");
    }
}
