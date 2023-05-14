import java.util.ArrayList;
import java.util.List;

public class Customer implements Runnable{

    private Cart cart;
    private Store store;

    public Customer(Cart cart, Store store) {

        this.cart = cart;
        this.store = store;
    }

    public Cart getCart() {

        return this.cart;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() +" started");
        System.out.println(Thread.currentThread().getName() +" creating shopping list");
        List<ShoppingItem> shoppingList = new ArrayList();
        shoppingList.add(new ShoppingItem("apple", 10));
//        shoppingList.add(new ShoppingItem("banana", 5));
        System.out.println(Thread.currentThread().getName() +" shopping list created");

        for (ShoppingItem item: shoppingList) {
            Product product = store.getProduct(item.getName());

            if ( product != null ) {
                product.purchase(item.getQuantity());
            } else {
                System.out.println(product.getName() + " is not available.");
            }

        }
        // Simulate delay between purchases


        //one customer one cart
        //that mean cart is not shared

        //but when stuff are bought Store is shared and the product
        //Store has products
        //There are chatgpt.Product objects in the Store. chatgpt.Product has quantity. purchased, filled
        //when customer buy product filled and purchased should be synchronized
        //What about the Store should be shared but not synchronized
        //If the same chatgpt.Product object used it would block all the customers
        //only the same product should be blocked not other products
    }
}
