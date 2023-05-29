import java.util.concurrent.CountDownLatch;

public class SystemAdmin implements Runnable{

    private Store store;
    private int restockTime;
    private boolean stopFlag;

    public SystemAdmin(Store store, int restockTime) {

        this.store = store;
        this.restockTime = restockTime;
    }

    public void stop() {
        stopFlag = true;
    }

    @Override
    public void run() {

        //restock products in an interval of restockTime (ms) to signify a stock have arrived and restock the product.

        while (!stopFlag) {
            try {
                Thread.sleep(restockTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            synchronized (store.getProducts()) {
                for (Product product : store.getProducts().values()) {
                    product.restock();
                }
            }
        }
    }
}
