import java.util.concurrent.ConcurrentLinkedDeque;
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

        //when get notified as any of the products are low lets say finish or last two fill the quantity
        //add products to a queue that needs to be refilled if products are in the queue refill it.
        ConcurrentLinkedDeque<Product> restockNeedProducts = store.getRestockNeedProducts();
        while(!stopFlag) {
            System.out.println(Thread.currentThread().getName() + " checking if any restocks are needed " + String.join(" ", restockNeedProducts.stream().map(Object::toString).collect(Collectors.joining(", "))));

            synchronized (restockNeedProducts) {
                while (!restockNeedProducts.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " restocks needed for " + String.join(" ", restockNeedProducts.stream().map(Object::toString).toArray(String[]::new)));
                    for (Product product: restockNeedProducts) {
                        product.restock(1, restockNeedProducts);
                    }
                }
            }
            //listen for 1000 and if no restock is reported stop admin threads from monitoring. This better replace with a pub sub model
            //so admins get notified when a product needs to be restocked.
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            if (restockNeedProducts.isEmpty()) {
//                break;
//            }
        }
    }
}
