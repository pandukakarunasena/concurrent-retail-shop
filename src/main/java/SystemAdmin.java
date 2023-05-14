import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemAdmin implements Runnable{

    private Store store;

    public SystemAdmin(Store store) {

        this.store = store;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() +" started");
        List<Product> productsList = new ArrayList<Product>();

        for (Map.Entry<String, Product> entry : store.getProducts().entrySet()) {
            Product product = entry.getValue();
            productsList.add(product);
        }
        System.out.println(Thread.currentThread().getName() +" product list generated");
        //when get notified as any of the products are low lets say finish or last two fill the quantity
        //add products to a queue that needs to be refilled if products are in the queue refill it.
        while (true) {

            System.out.println(Thread.currentThread().getName() +" monitoring restock operation");
            for (Product product: productsList) {
                System.out.println(Thread.currentThread().getName() +" monitoring restock operation for " + product.getName());
                product.restock(5);
                System.out.println(Thread.currentThread().getName() +" is in : " + Thread.currentThread().getState());
            }
        }
    }
}
