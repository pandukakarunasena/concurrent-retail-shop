import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class SystemAdmin implements Runnable{

    private Store store;
    private volatile boolean stopFlag;

    public SystemAdmin(Store store) {
        this.store = store;
    }

    public void stop() {
        stopFlag = true;
    }
    @Override
    public void run() {

        //add products to a queue that needs to be refilled if products are in the queue refill it.
        ConcurrentLinkedQueue<Product> restockNeedProducts = store.getRestockNeedProducts();
        while(!stopFlag) {
            System.out.println(Thread.currentThread().getName() + " checking if any restocks are needed " + String.join(" ", restockNeedProducts.stream().map(Object::toString).collect(Collectors.joining(", "))));

            synchronized (restockNeedProducts) {
                while (!restockNeedProducts.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " restocks needed for " + String.join(" ", restockNeedProducts.stream().map(Object::toString).toArray(String[]::new)));
                    for (Product product: restockNeedProducts) {
                        product.restock(1);
                        restockNeedProducts.remove(product);
                    }
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
