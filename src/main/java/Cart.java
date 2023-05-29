import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<ShoppingItem> addedShoppingItems = new ArrayList<>();

    public List<ShoppingItem> getAddedShoppingItems() { return this.addedShoppingItems;}

    public boolean addShoppingItem(ShoppingItem product) {
        return addedShoppingItems.add(product);
    }

    public boolean removeShoppingItem(ShoppingItem product) { return addedShoppingItems.remove(product);}
}
