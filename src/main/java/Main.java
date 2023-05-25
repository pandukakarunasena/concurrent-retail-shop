import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {


        //Prepare the Store by adding random Products.
        Store store = generateStore();

        int numberOfCustomers = 100;
        int numberOfAdmins = 5;
        int numberOfCashiers = 5;

        // Create and start admin threads
        SystemAdmin[] admins = new SystemAdmin[numberOfAdmins];
        Thread[] adminThreads = new Thread[numberOfAdmins];
        for (int i = 0; i < numberOfAdmins; i++) {
            admins[i] = new SystemAdmin(store);
            adminThreads[i] = new Thread(admins[i], "admin thread " + i);
            adminThreads[i].start();
        }

        Cashier[] cashier = new Cashier[numberOfCashiers];
        Thread[] cashierThreads = new Thread[numberOfCashiers];
        for (int i = 0; i < numberOfCashiers; i++) {
            cashier[i] = new Cashier(store);
            cashierThreads[i] = new Thread(cashier[i], "cashier thread " + i);
            cashierThreads[i].start();
        }

        // Create and start customer threads
        Thread[] customerThreads = new Thread[numberOfCustomers];
        for (int i = 0; i < numberOfCustomers; i++) {
            Cart cart = new Cart();
            List<ShoppingItem> shoppingList = new ArrayList();
            shoppingList.add(new ShoppingItem("apple", 1));
            shoppingList.add(new ShoppingItem("banana", 1));
            customerThreads[i] = new Thread(new Customer(store, shoppingList), "customer thread " + i);
            customerThreads[i].start();
        }

        // Wait for customer threads to complete
        for (int i = 0; i < numberOfCustomers; i++) {
            try {
                customerThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread.sleep(5000);
        // Stop admin threads
        for (int i = 0; i < numberOfAdmins; i++) {
            admins[i].stop();
            adminThreads[i].interrupt();
        }

        for (int i = 0; i < numberOfCashiers; i++) {
            cashier[i].stop();
            customerThreads[i].interrupt();
        }

        Thread.sleep(200);

        System.out.println("Summary of the inventory=========================================");
        for (Product product : store.getProducts().values()) {
            System.out.println(product.getName() + " left " + store.getProduct(product.getName()).getQuantity());
        }
    }

    public static Store generateStore() {

        Store store = new Store();

        Soap soap = new Soap("soap", 55, 100);
        store.addProductToStore(soap);

        Banana banana = new Banana("banana", 20, 100);
        store.addProductToStore(banana);

        Apple apple = new Apple("apple", 30, 100);
        store.addProductToStore(apple);

        return store;
    }
}
