package com.example.demo.components;

import com.almasb.fxgl.entity.component.Component;
import com.example.demo.ShopView;

import java.util.HashMap;
import java.util.Map;

public class ShopComponent extends Component {
    private final Map<String, ShopView.ItemDetails> inventory = new HashMap<>();

    public ShopComponent() {
        // Initialize the inventory with some items
        inventory.put("Item 1", new ShopView.ItemDetails(10, 5.99));
        inventory.put("Item 2", new ShopView.ItemDetails(8, 7.49));
        inventory.put("Item 3", new ShopView.ItemDetails(15, 3.99));
        inventory.put("Item 4", new ShopView.ItemDetails(5, 9.99));
        inventory.put("Item 5", new ShopView.ItemDetails(20, 2.49));
        inventory.put("Item 6", new ShopView.ItemDetails(12, 4.99));

    }

    public void show() {
        ShopView.show(entity.getX(), entity.getY()-100, inventory);
    }

    public Map<String, ShopView.ItemDetails> getInventory() {
        return inventory;
    }
}


