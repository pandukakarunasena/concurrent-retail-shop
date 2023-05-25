import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Customer implements Runnable{

    private Cart cart = new Cart();
    private Store store;
    private List<ShoppingItem> shoppingList;

    public Customer(Store store, List<ShoppingItem> shoppingList) {

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
            Iterator<ShoppingItem> shoppingListIterator = shoppingList.iterator();
            while (shoppingListIterator.hasNext()) {
                ShoppingItem item = shoppingListIterator.next();
                System.out.println(Thread.currentThread().getName() + " checking for " + item.getQuantity() + " " + item.getName() + " to buy.");
                Product product = store.getProduct(item.getName());

                synchronized (product) {
                    if (product != null) {
                        System.out.println(Thread.currentThread().getName() + " found " + item.getName());
                        boolean canPurchase = product.canPurchase(item.getQuantity());
                        if (canPurchase) {
                            cart.addShoppingItem(item);
                            System.out.println(Thread.currentThread().getName() + " added " + item.getQuantity() + " " + item.getName() + " to the cart");
                            shoppingListIterator.remove();
                            System.out.println(Thread.currentThread().getName() + " removed " + item.getName() + " from shopping list");
                        } else {
                            System.out.println(Thread.currentThread().getName() + " no stocks available for adding to cart " + item.getName());
                            //customers wait until the product gets restocked. until that customers will roam around the store
                            //should we inform the admins to restock ????
                            if (!store.getRestockNeedProducts().contains(product)) {
                                System.out.println(Thread.currentThread().getName() + " notify the admins to restock " + item.getQuantity() + " " + item.getName());
                                store.addProductsToRestockList(product);
                                notify();
                            } else {
                                System.out.println(Thread.currentThread().getName() + " " + item.getName() + " is already in the need stocks queue");
                            }

                        }
                    } else {
                        System.out.println(item.getName() + " is not available.");
                    }
                    System.out.println(Thread.currentThread().getName() + " has left " + shoppingList.stream().map(ShoppingItem::getName).collect(Collectors.joining(", ")) + " to buy.");
                }
            }
        }

        //go to the checkout queue. this is not synced to each thread whoever adds the cart first is checked out first.
        //and if the stocks are not there the customer will hold the queue until stocks arrives.
        store.getCheckoutQueue().add(cart);
        System.out.println(Thread.currentThread().getName() +" is in the checkout queue....");
    }
}
