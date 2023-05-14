import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        //Prepare the Store by adding random Products.
        Store store = generateStore();

        int numberOfCustomers = 10;
        int numberOfAdmins = 2;

        // Create and start admin threads
        SystemAdmin[] admins = new SystemAdmin[numberOfAdmins];
        Thread[] adminThreads = new Thread[numberOfAdmins];
        for (int i = 0; i < numberOfAdmins; i++) {
            admins[i] = new SystemAdmin(store);
            adminThreads[i] = new Thread(admins[i], "admin thread " + i);
            adminThreads[i].start();
        }

        // Create and start customer threads
        Thread[] customerThreads = new Thread[numberOfCustomers];
        for (int i = 0; i < numberOfCustomers; i++) {
            Cart cart = new Cart();
            List<ShoppingItem> shoppingList = new ArrayList();
            shoppingList.add(new ShoppingItem("apple", 1));
            shoppingList.add(new ShoppingItem("banana", 1));
            customerThreads[i] = new Thread(new Customer(cart, store, shoppingList), "customer thread " + i);
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

        // Stop admin threads
        for (int i = 0; i < numberOfAdmins; i++) {
            admins[i].stop();
            adminThreads[i].interrupt();
        }

        System.out.println("Summary of the inventory=========================================");
        for (Product product : store.getProducts().values()) {
            System.out.println(product.getName() + " left " + store.getProduct(product.getName()).getQuantity());
        }

    }

    public static Store generateStore() {

        Store store = new Store();

        Soap soap = new Soap("soap", 55, 100);
        store.addProduct(soap);

        Banana banana = new Banana("banana", 20, 1);
        store.addProduct(banana);

        Apple apple = new Apple("apple", 30, 1);
        store.addProduct(apple);

        return store;
    }
}
