import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product {

    private String name;
    private float price;
    private int quantity;

    private Lock lock;
    private Condition stocks;
    private final int MAX_QUANTITY;

    public Product(String name, float price, int maxQuantity) {

        this.name = name;
        this.price = price;
        MAX_QUANTITY = maxQuantity;
        quantity = maxQuantity;
        lock = new ReentrantLock(true);
        stocks = lock.newCondition();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean addToCart(int requiredQuantity) {

        lock.lock();
        try {
            if ( this.quantity < requiredQuantity) {
                System.out.println(Thread.currentThread().getName() + " current stock of the product : "
                        + this.getQuantity());
                return false;
            }
            System.out.println(Thread.currentThread().getName() + " stocks available for add to cart "
                    + this.getName());
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void purchase(int requiredQuantity) {

        lock.lock();
        try {
            while ( this.quantity < requiredQuantity ) {
                stocks.await();
            }
            System.out.println(Thread.currentThread().getName() + " stocks available for checkout "
                    + this.getName());

            this.quantity -= requiredQuantity;

            System.out.println(Thread.currentThread().getName() + " purchased " + requiredQuantity + " "
                    + this.getName());

        } catch (InterruptedException ex) {
            System.out.println("Error occurred while purchasing the product " + ex);
        }  finally {
            lock.unlock();
        }
    }

    public void restock() {

        lock.lock();
        try {
             if ( this.quantity < MAX_QUANTITY) {
                this.quantity = MAX_QUANTITY;
                System.out.println(Thread.currentThread().getName() + " restocked " + this.getName()
                        + ". total : " + this.getQuantity());
                stocks.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public int getMAX_QUANTITY() {
        return MAX_QUANTITY;
    }
}
