import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Store {

    private HashMap<String, Product> products = new HashMap<>();
    private ConcurrentLinkedDeque<Product> restockNeedProducts = new ConcurrentLinkedDeque<>();
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();

    public HashMap<String, Product> getProducts() { return products; }
    public void addProduct(Product product) {

        //add new products to the store
        products.put(product.getName(), product);
    }

    public Product getProduct(String productName) {

        return products.get(productName);
    }

    public ConcurrentLinkedDeque<Product> getRestockNeedProducts() {

        return restockNeedProducts;
    }

    public void addProductsToRestockList(Product product) {

        lock.lock();
        try {
            restockNeedProducts.add(product);
            notEmpty.signalAll();
        } finally {
             lock.unlock();
        }
    }

    public void removeProductFromRestockList(Product product) {

        lock.lock();
        try {
            while (restockNeedProducts.isEmpty()) {
                notEmpty.await();
            }

            restockNeedProducts.remove(product);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }
}
