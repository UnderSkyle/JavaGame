package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;

import java.util.Map;

public class UsableItemComponent extends ItemComponent{

    private final Runnable onUse;


    public UsableItemComponent(Runnable onUse, Map<String, Object> data ) {
        super(data);
        this.onUse = onUse;

    }


    @Override
    public void onUse(Entity entity) {
        onUse.run();
    }
}
