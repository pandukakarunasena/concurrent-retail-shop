import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Store {

    private HashMap<String, Product> products = new HashMap<>();
    private ConcurrentLinkedQueue<Customer> customerQueue = new ConcurrentLinkedQueue();

    public HashMap<String, Product> getProducts() { return products; }

    public void addProductToStore(Product product) { products.put(product.getName(), product);}

    public Product getProduct(String productName) {
        return products.get(productName);
    }

    public void addToQueue(Customer customer) {
        customerQueue.add(customer);
    }

    public ConcurrentLinkedQueue<Customer> getCustomerQueue() {
        return customerQueue;
    }
}
