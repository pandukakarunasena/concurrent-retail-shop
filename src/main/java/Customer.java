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
            System.out.println(Thread.currentThread().getName() + " has "
                    + shoppingList.stream().map(ShoppingItem::getName).collect(Collectors.joining(", "))
                    + " to buy.");
            Iterator<ShoppingItem> shoppingListIterator = shoppingList.iterator();
            while (shoppingListIterator.hasNext()) {
                ShoppingItem shoppingitem = shoppingListIterator.next();
                Product product = store.getProduct(shoppingitem.getName());

                if (product != null) {
                    boolean canPurchase = product.addToCart(shoppingitem.getQuantity());
                    if (canPurchase) {
                        cart.addShoppingItem(shoppingitem);
                        shoppingListIterator.remove();
                        System.out.println(Thread.currentThread().getName() + " removed " + shoppingitem.getName()
                                + " from shopping list");
                    } else {
                        System.out.println(Thread.currentThread().getName() + " waiting for restock to add to cart: "
                                + shoppingitem.getQuantity() + " " + shoppingitem.getName());
                    }
                } else {
                    System.out.println(shoppingitem.getName() + " is not available.");
                }
                System.out.println(Thread.currentThread().getName() + " has left "
                        + shoppingList.stream().map(ShoppingItem::getName).collect(Collectors.joining(", "))
                        + " to buy.");
            }
        }

        store.addToQueue(this);
        System.out.println(Thread.currentThread().getName() +" is in the checkout queue....");
    }
}
