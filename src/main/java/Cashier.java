import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class Cashier implements Runnable {

    private Store store;
    volatile boolean stopFlag;
    private CountDownLatch latch;

    public Cashier(Store store, CountDownLatch latch) {
        this.store = store;
        this.latch = latch;
    }

    public void stop() {
        stopFlag = true;
    }

    @Override
    public void run() {

        ConcurrentLinkedQueue<Customer> customerQueue = store.getCustomerQueue();

        while (!stopFlag) {
            Cart cart = null;
            try {
                //carts are checked out from the queue in first come, a first served basis.
                cart = customerQueue.remove().getCart();
                System.out.println(Thread.currentThread().getName() + " started checking out cart " + cart);
            } catch (NoSuchElementException e) {
                //System.out.println(Thread.currentThread().getName() + " No carts to checkout!!!!!!");
            }

            if (cart != null) {
                List<ShoppingItem> shoppingItems = cart.getAddedShoppingItems();
                while (!shoppingItems.isEmpty()) {
                    Iterator<ShoppingItem> shoppingItemIterator = shoppingItems.iterator();

                    while (shoppingItemIterator.hasNext()) {

                        System.out.println(Thread.currentThread().getName()
                                + " started checking out the shopping list " + cart);
                        ShoppingItem item = shoppingItemIterator.next();
                        Product product = store.getProduct(item.getName());
                        product.purchase(item.getQuantity());
                        shoppingItemIterator.remove();
                        System.out.println(Thread.currentThread().getName() + " " + item.getName()
                                + " removed from the shopping list");
                    }
                }
                latch.countDown();
                System.out.println("LATCH " + latch.getCount());
            }
        }
    }
}
