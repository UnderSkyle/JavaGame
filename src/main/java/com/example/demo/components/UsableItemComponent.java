package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;

public class UsableItemComponent extends ItemComponent{

    private final Runnable onUse;

    public UsableItemComponent(Runnable onUse) {
        this.onUse = onUse;
    }

    public void onUse(Entity entity) {
        onUse.run();
    }
}
