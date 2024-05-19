package com.example.demo.components;

import com.almasb.fxgl.entity.component.Component;
import com.example.demo.ShopView;

import java.util.HashMap;
import java.util.Map;

public class ShopComponent extends Component {
    private final Map<String, ShopView.ItemDetails> inventory = new HashMap<>();
    private final ShopView newShopView;

    public ShopComponent() {
        // Initialize the inventory with some items
        inventory.put("health potion", new ShopView.ItemDetails(10, 3));
        newShopView = new ShopView();

    }

    public void show() {
        newShopView.show(entity.getX(), entity.getY()-100, inventory);
    }

    public Map<String, ShopView.ItemDetails> getInventory() {
        return inventory;
    }
}


