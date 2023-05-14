import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product implements Buyable{

    private String name;
    private float price;
    private int quantity;

    private Lock lock;
    private Condition stockAvailable;
    private Condition stockFinish;
    public Product(String name, float price, int quantity) {

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        lock = new ReentrantLock(true);
        stockAvailable = lock.newCondition();
        stockFinish = lock.newCondition();
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

        return true;
    }
    @Override
    public void restock(int quantity) {

        //this should be synchronized
        lock.lock();
        try {
            while (this.quantity > 0) {
                // Wait until the stock is empty
                try {
                    System.out.println(Thread.currentThread().getName() +" no need to restock");
                    stockFinish.await();
                    System.out.println(Thread.currentThread().getName() +" notification received to restock");
                    System.out.println(Thread.currentThread().getName() +" state: " + Thread.currentThread().getState());

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // Stock is empty, admin restocks the product
            this.quantity += quantity;
            System.out.println(Thread.currentThread().getName() + " restocked the product. total : " + this.getQuantity());

            // Signal customers that the product is available
            stockAvailable.signalAll();

        } finally {
            lock.unlock();
        }
    }
    @Override
    public void purchase(int quantity) {

        //this should be synchronized
        lock.lock();
        try {
            while (this.quantity - quantity <= 0) {
                // Wait until the stock is available
                try {
                    //notify the admin to restock.....
                    System.out.println(Thread.currentThread().getName() +" notifying admins to restock " + this.getName());
                    stockFinish.signalAll();
                    System.out.println(Thread.currentThread().getName() +" notified admins to restock " + this.getName());
                    stockAvailable.await();
                    System.out.println(Thread.currentThread().getName() +" restocked " + this.getName() + " new quantity : " + this.getQuantity());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            //Product is available, customer makes a purchase

            this.quantity -= quantity;
            System.out.println(Thread.currentThread().getName() + " purchased " + quantity + " " + this.getName());

            // Notify admins to restock if the stock is empty
//            if (this.quantity - quantity <= 0) {
//                System.out.println("notify the admins");
//                stockAvailable.signalAll();
//            }
        } finally {
            lock.unlock();
        }
    }
}
