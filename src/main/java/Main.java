public class Main {
    public static void main(String[] args) {


        //Prepare the Store by adding random Products.
        Store store = generateStore();

        //Add System.in for user inputs
        //user inputs => number of customers, number of admins, number of purchases per customer
        int numberOfCustomers = 5;
        int numberOfAdmins = 2;
        //each customer and admin should be spawned in a new thread
        //random shopping list


        //Customer class with Runnable to buy products
        //SystemAdmin class with Runnable to update the quantity

        for (int i = 0; i < numberOfAdmins; i++) {
            Thread systemAdminThread = new Thread( new SystemAdmin(store),"system admin thread " + i);
            systemAdminThread.start();
        }

        try {
            // Sleep for 5 seconds
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Handle the interruption if needed
        }

        for (int i = 0; i < numberOfCustomers; i++) {
            Cart cart = new Cart();
            Thread customerThread = new Thread( new Customer(cart, store),"customer thread " + i);
            customerThread.start();
        }

        try {
            // Sleep for 5 seconds
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // Handle the interruption if needed
        }

        System.out.println("apples left " + store.getProduct("apple").getQuantity());
//        System.out.println("banana left " + store.getProduct("banana").getQuantity());
    }

    public static Store generateStore() {

        Store store = new Store();

//        Soap soap = new Soap("soap", 55, 100);
//        store.addProduct(soap);
//
//        Banana banana = new Banana("banana", 20, 20);
//        store.addProduct(banana);

        Apple apple = new Apple("apple", 30, 15);
        store.addProduct(apple);

        return store;
    }
}
