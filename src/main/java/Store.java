import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Store {

    private HashMap<String, Product> products = new HashMap<>();
    private ConcurrentLinkedQueue<Product> restockNeedProducts = new ConcurrentLinkedQueue<>();//do we need this if we satisfy a customer until he gets all the shopping list items.
    private ConcurrentLinkedQueue<Cart> carts  = new ConcurrentLinkedQueue<>();

    public HashMap<String, Product> getProducts() { return products; }

    public void addProductToStore(Product product) {
        products.put(product.getName(), product);
    }

    public synchronized void addProductsToRestockList(Product product) {
        if (!restockNeedProducts.contains(product)) {
            restockNeedProducts.add(product);
        }
    }

    public Product getProduct(String productName) {
        return products.get(productName);
    }

    public ConcurrentLinkedQueue<Product> getRestockNeedProducts() {
        return restockNeedProducts;
    }

    public void addCartsToCheckoutQueue(Cart cart) {
        carts.add(cart);
    }

    public ConcurrentLinkedQueue getCheckoutQueue() {
        return carts;
    }
}
