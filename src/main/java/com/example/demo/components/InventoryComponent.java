package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.example.demo.PlayerInventoryView;

import java.util.HashMap;
import java.util.Map;

public class InventoryComponent extends Component {

    Map<String, Integer> inventory = new HashMap<>();

    PlayerInventoryView inventoryView;

    @Override
    public void onAdded() {
        super.onAdded();

        inventoryView = entity.getComponent(PlayerComponent.class).getInventoryView();
    }

    public void add(String name) {
        // Check if the inventory is full
        if (!isFull()) {
            // Add the item to the inventory
            inventory.merge(name, 1, Integer::sum);
            inventoryView.update(inventory);
        }
    }

    public void remove(String name) {
        // Check if the inventory contains the item
        if (inventory.containsKey(name)){
            // Remove the item from the inventory
            int quantity = inventory.get(name);
            if (quantity > 1) {
                inventory.put(name, quantity - 1);
            } else {
                inventory.remove(name);
            }
            inventoryView.update(inventory);
        }
    }

    public boolean isFull() {
        // Define your logic for determining if the inventory is full
        // For example, you can check the total number of items in the inventory
        return inventory.size() >= MAX_INVENTORY_SIZE;
    }

    // Define the maximum size of the inventory
    private static final int MAX_INVENTORY_SIZE = 20;

}
