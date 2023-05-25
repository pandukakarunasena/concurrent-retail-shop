import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product {

    private String name;
    private float price;
    private int quantity;

    private Lock lock;
    private Condition stocks;

    public Product(String name, float price, int quantity) {

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        lock = new ReentrantLock(true);
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

    public boolean canPurchase(int requiredQuantity) {

        lock.lock();
        try {
            if ( this.quantity - requiredQuantity >= 0) {
                System.out.println(Thread.currentThread().getName() + " stocks available for add to cart " + this.getName());
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean purchase(int requiredQuantity) {

        lock.lock();
        try {
            if ( this.quantity - requiredQuantity >= 0 ) {
                System.out.println(Thread.currentThread().getName() + " stocks available for checkout " + this.getName());
                this.quantity -= requiredQuantity;
                System.out.println(Thread.currentThread().getName() + " purchased " + requiredQuantity + " " + this.getName());
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public void restock(int restockingQuantity) {

        lock.lock();
        System.out.println(Thread.currentThread().getName() + " restocking" + this.getName());
        try {
            this.quantity += restockingQuantity;
            System.out.println(Thread.currentThread().getName() + " restocked the " + this.getName() + ". total : " + this.getQuantity());
        } finally {
            lock.unlock();
        }
    }
}
