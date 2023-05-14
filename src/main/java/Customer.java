import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Customer implements Runnable{

    private Cart cart;
    private Store store;
    private List<ShoppingItem> shoppingList;

    public Customer(Cart cart, Store store, List<ShoppingItem> shoppingList) {

        this.cart = cart;
        this.store = store;
        this.shoppingList = shoppingList;
    }

    public Cart getCart() {

        return this.cart;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() +" started shopping....");

        while(!shoppingList.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " has " + shoppingList.stream().map(ShoppingItem::getName).collect(Collectors.joining(", ")) + " to buy.");
            Iterator<ShoppingItem> iterator = shoppingList.iterator();
            while (iterator.hasNext()) {
                ShoppingItem item = iterator.next();
                System.out.println(Thread.currentThread().getName() + " checking for " + item.getQuantity() + " " + item.getName() + " to buy.");
                Product product = store.getProduct(item.getName());
                if (product != null) {
                    System.out.println(Thread.currentThread().getName() + " found " + item.getName());
                    boolean canPurchase = product.canPurchase(item.getQuantity());
                    if (canPurchase) {
                        iterator.remove();
                        System.out.println(Thread.currentThread().getName() + " removed " + item.getName() + " from shopping list");
                    } else {
                        System.out.println(Thread.currentThread().getName() + " no stocks available for " + item.getName());
                        if (!store.getRestockNeedProducts().contains(product)) {
                            store.getRestockNeedProducts().add(product);
                            System.out.println(Thread.currentThread().getName() + " added " + item.getName() + " to the needsRestocks queue");
                            System.out.println(Thread.currentThread().getName() + " Admins have to restock " + product.getName());
                        } else {
                            System.out.println(Thread.currentThread().getName() + " " + product.getName() + " is already in the queue. come back when restocked");
                        }
                    }
                } else {
                    System.out.println(item.getName() + " is not available.");
                }
                System.out.println(Thread.currentThread().getName() + " has left " + shoppingList.stream().map(ShoppingItem::getName).collect(Collectors.joining(", ")) + " to buy.");
            }
        }

    }
}
