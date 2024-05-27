package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.example.demo.PlayerInventoryView;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class PlayerInventoryComponent extends Component {

    private Map<String, Integer> inventory = new HashMap<>();

    private PlayerInventoryView inventoryView;

    @Override
    public void onAdded() {
        super.onAdded();

        inventoryView = entity.getComponent(PlayerComponent.class).getInventoryView();
    }

    public void add(String name) {
        // Check if the inventory is full
        if (!isFull()) {
            // Add the item to the inventory
            if(entity.hasComponent(EquipedItemComponent.class)){
                inventory.put(name, 0);
            }
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
        int itemCount = 0;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            itemCount += entry.getValue();
        }

        System.out.println("The inventory is  full");

        return itemCount >= MAX_INVENTORY_SIZE;

    }


    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    // Define the maximum size of the inventory
    private static final int MAX_INVENTORY_SIZE = 5;

    public boolean hasItem(String key) {
        return inventory.containsKey(key);
    }

    public Map<String, Integer> getCombatItems() {
        Map<String, Integer> combatItems = new HashMap<>();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            Entity candidate = spawn(entry.getKey(), 1000, 1000);
            if (candidate.hasComponent(CombatItemComponent.class)) {
                combatItems.put(entry.getKey(), entry.getValue());
            }
            candidate.removeFromWorld();
        }
        return combatItems;
    }
}
