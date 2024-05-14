package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.inventory.Inventory;
import com.almasb.fxgl.inventory.view.InventoryListCell;
import com.almasb.fxgl.inventory.view.InventoryView;

public class InventoryComponent extends Component {

    public void add(Entity item) {

    }

    public boolean isFull(Entity item) {

        return false;
    }
}
