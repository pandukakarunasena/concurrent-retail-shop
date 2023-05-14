import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<ShoppingItem> addedProducts = new ArrayList<>();

    public List getAddedProducts() {

        return this.addedProducts;
    }

    public boolean addProduct(ShoppingItem product) {

        return addedProducts.add(product);
    }

    public boolean removeProduct(ShoppingItem product) {

        return addedProducts.remove(product);
    }
}
