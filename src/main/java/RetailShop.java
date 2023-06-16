import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class RetailShop {

    //   ASSUMPTIONS
    //1. This simulation has a predefined number of customers, system admins, and cashiers.
    //2. System admins restock the products in a recurring manner by up to the max quantity of each product.
    //3. Customers can have a shopping list for each, but here it is hard coded.
    //4. Introduced cashier threads to check out the customer's carts in first come, a first served basis.
    //5. Introduced a countDownLatch in order to track checking out.
    // Checking out and restocking hold when no customer is in the store.
    //6. Terminal logs might not be in the correct order.
    //7. Parameter values, flows are aligned with the real world retail shop good purchasing scenarios.

    //   FUTURE IMPROVEMENTS
    //1. Get user inputs for the retail shop parameters.
    //2. Add automatic kill switch for restocking operations.
    //3. Implement a mechanism to log the flows in a thread safe manner.

    int numberOfCustomers;
    int numberOfAdmins;
    int numberOfCashiers;
    int restockTime;
    SystemAdmin[] admins;
    Cashier[] cashier;
    CountDownLatch latch;
    Store store;

    public RetailShop (int numberOfCustomers, int numberOfAdmins, int numberOfCashiers,
                       int restockTime, CountDownLatch latch, Store store) {

        this.numberOfCustomers = numberOfCustomers;
        this.numberOfAdmins = numberOfAdmins;
        this.numberOfCashiers = numberOfCashiers;
        this.restockTime = restockTime;
        this.store = store;
        this.latch = latch;

        admins = new SystemAdmin[numberOfAdmins];
        for (int i = 0; i < numberOfAdmins; i++) {
            admins[i] = new SystemAdmin(store, restockTime);
        }

        cashier = new Cashier[numberOfCashiers];
        for (int i = 0; i < numberOfCashiers; i++) {
            cashier[i] = new Cashier(store,latch);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int numberOfCustomers = 10;
        int numberOfAdmins = 2;
        int numberOfCashiers = 1;
        int scheduledRestockTime = 200;
        CountDownLatch latch = new CountDownLatch(numberOfCustomers);
        Store store = new Store();

        RetailShop retailShop = new RetailShop(
                numberOfCustomers, numberOfAdmins, numberOfCashiers,
                scheduledRestockTime, latch, store
        );

        retailShop.generateStore();
        retailShop.startCashiers();
        retailShop.startScheduledRestocking();
        retailShop.allowCustomersToShop();
        retailShop.waitUntilAllCheckout();
        retailShop.stopCashiers();
        retailShop.stopRestocking();
        retailShop.printSummary();
    }

    private void  generateStore() {

        Product soap = new Product("soap", 55, 1);
        store.addProductToStore(soap);

        Product banana = new Product("banana", 20, 1);
        store.addProductToStore(banana);

        Product apple = new Product("apple", 30, 1);
        store.addProductToStore(apple);
    }

    private void startCashiers() {

        Thread[] cashierThreads = new Thread[numberOfCashiers];
        for (int i = 0; i < numberOfCashiers; i++) {
            cashierThreads[i] = new Thread(cashier[i], "cashier thread " + i);
            cashierThreads[i].start();
        }
    }

    private void startScheduledRestocking() {

        Thread[] adminThreads = new Thread[numberOfAdmins];
        for (int i = 0; i < numberOfAdmins; i++) {
            adminThreads[i] = new Thread(admins[i],"admin thread " + i);
            adminThreads[i].start();
        }
    }

    private void allowCustomersToShop() {

        Thread[] customerThreads = new Thread[numberOfCustomers];
        for (int i = 0; i < numberOfCustomers; i++) {
            List<ShoppingItem> shoppingList = new ArrayList<>();
            shoppingList.add(new ShoppingItem("apple", 1));
            //shoppingList.add(new ShoppingItem("banana", 20));
            customerThreads[i] = new Thread(new Customer(store, shoppingList), "customer thread " + i);
            customerThreads[i].start();
        }
    }

    private void waitUntilAllCheckout() {

        try {
            latch.await();
            System.out.println("All workers have completed their tasks.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopCashiers() {

        for (int i = 0; i < numberOfCashiers; i++) {
            cashier[i].stop();
        }
    }

    private void stopRestocking() {

        for (int i = 0; i < numberOfAdmins; i++) {
            admins[i].stop();
        }
    }

    private void printSummary() {

        System.out.println("Summary of the inventory==========================================================");
        for (Product product : store.getProducts().values()) {
            System.out.println(product.getName() + " left " + store.getProduct(product.getName()).getQuantity());
        }
    }
}
