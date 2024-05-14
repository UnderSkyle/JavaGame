package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;

import java.util.Map;

public class EquipedItemComponent extends ItemComponent{

    public EquipedItemComponent(Map<String, Object> data, Runnable onUse) {
        super(data);
        this.onUse = onUse;
    }

    private Runnable onUse;


    @Override
    public void onUse(Entity entity) {
        onUse.run();
    }
}
