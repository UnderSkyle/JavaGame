package com.example.demo.components;

import com.almasb.fxgl.core.Copyable;
import com.almasb.fxgl.entity.Entity;

import java.util.Map;
import java.util.Optional;

public class UsableItemComponent extends ItemComponent{

    private final Runnable onUse;


    public UsableItemComponent(Runnable onUse, Map<String, Object> data ) {
        super(data);
        this.onUse = onUse;

    }

    public Entity getEntityFromName(String name) {
        if(super.getName().equals(name)) {
            return entity;
        }
        return null;
    }


    @Override
    public void onUse(Entity entity) {
        onUse.run();
    }
}
