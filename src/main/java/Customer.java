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
                ShoppingItem shoppingitem = shoppingListIterator.next();
                System.out.println(Thread.currentThread().getName() + " checking for " + shoppingitem.getQuantity() + " " + shoppingitem.getName() + " to buy.");
                Product product = store.getProduct(shoppingitem.getName());

                if (product != null) {
                    System.out.println(Thread.currentThread().getName() + " found " + shoppingitem.getName());
                    boolean canPurchase = product.canPurchase(shoppingitem.getQuantity());
                    if (canPurchase) {
                        cart.addShoppingItem(shoppingitem);
                        System.out.println(Thread.currentThread().getName() + " added " + shoppingitem.getQuantity() + " " + shoppingitem.getName() + " to the cart");
                        shoppingListIterator.remove();
                        System.out.println(Thread.currentThread().getName() + " removed " + shoppingitem.getName() + " from shopping list");
                    } else {
                        System.out.println(Thread.currentThread().getName() + " no stocks available for adding to cart " + shoppingitem.getName());
                        store.addProductsToRestockList(product);
                        System.out.println(Thread.currentThread().getName() + " notified the admins to restock " + shoppingitem.getQuantity() + " " + shoppingitem.getName());
                    }
                } else {
                    System.out.println(shoppingitem.getName() + " is not available.");
                }
                System.out.println(Thread.currentThread().getName() + " has left " + shoppingList.stream().map(ShoppingItem::getName).collect(Collectors.joining(", ")) + " to buy.");
            }
        }

        //go to the checkout queue. this is not synced to each thread whoever adds the cart first is checked out first.
        //and if the stocks are not there the customer will hold the queue until stocks arrives.
        store.getCheckoutQueue().add(cart);
        System.out.println(Thread.currentThread().getName() +" is in the checkout queue....");
    }
}
