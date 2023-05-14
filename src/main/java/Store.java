import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Store {

    private HashMap<String, Product> products = new HashMap<>();
    private ArrayList<Product> needsToRestocked = new ArrayList<>();

    public HashMap<String, Product> getProducts() { return products; }
    public void addProduct(Product product) {

        //add new products to the store
        products.put(product.getName(), product);
    }

    public Product getProduct(String productName) {

        return products.get(productName);
    }

    public List getNeedsToRestocked() {

        return needsToRestocked;
    }


}
