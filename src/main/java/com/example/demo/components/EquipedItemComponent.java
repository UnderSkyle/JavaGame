package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

import java.util.Map;

public class EquipedItemComponent extends ItemComponent{

    public EquipedItemComponent(Map<String, Object> data, Runnable onUse, Runnable onDrop) {
        super(data);
        this.onUse = onUse;
        this.onDrop = onDrop;
    }

    // Constructor with default value for onDrop
    public EquipedItemComponent(Map<String, Object> data, Runnable onUse) {
        super(data);
        this.onUse = onUse;
        this.onDrop = () -> {

        };

    }

    private final Runnable onUse;
    private final Runnable onDrop;

    @Override
    public void onDrop(Point2D position, String Name) {
        super.onDrop(position, Name);
        onDrop.run();
    }


    @Override
    public void onUse(Entity entity) {
        onUse.run();
    }
}
