import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Cashier implements Runnable {

    private Store store;
    private volatile boolean stopFlag;
    public Cashier(Store store) {
        this.store = store;
    }

    public void stop() {
        stopFlag = true;
    }
    @Override
    public void run() {

        //System.out.println(Thread.currentThread().getName() + " started==============================>");
        ConcurrentLinkedQueue<Cart> carts = store.getCheckoutQueue();
        //System.out.println(Thread.currentThread().getName() + " checkout queue started!!!!");
        while (!stopFlag) {
            Cart cart = null;
            try {
                cart = carts.remove();
                System.out.println(Thread.currentThread().getName() + " started checking out cart " + cart);
            } catch (NoSuchElementException e) {
                //System.out.println(Thread.currentThread().getName() + " No carts to checkout!!!!!!");
            }


            if (cart != null) {
                List<ShoppingItem> shoppingItems = cart.getAddedShoppingItems();
                while (!shoppingItems.isEmpty()) {
                    Iterator<ShoppingItem> shoppingItemIterator = shoppingItems.iterator();

                    while (shoppingItemIterator.hasNext()) {

                        System.out.println(Thread.currentThread().getName() + " started checking out the shopping list " + cart);
                        ShoppingItem item = shoppingItemIterator.next();
                        Product product = store.getProduct(item.getName());
                        boolean purchased = product.purchase(item.getQuantity());

                        if (purchased) {
                            System.out.println(Thread.currentThread().getName() + " " + item.getQuantity() + " " + item.getName() + " is available and purchased");
                            shoppingItemIterator.remove();
                            System.out.println(Thread.currentThread().getName() + " " + item.getName() + " removed from the shopping list");
                        } else {
                            store.addProductsToRestockList(product);
                            System.out.println(Thread.currentThread().getName() + " notified the admins to restock " + item.getQuantity() + " " + item.getName());
                        }
                    }
                }
            }
        }
    }
}
