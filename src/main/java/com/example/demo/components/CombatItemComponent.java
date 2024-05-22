package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;


import java.util.Map;

public class CombatItemComponent extends ItemComponent{

    Runnable onUse = new Runnable() {

        @Override
        public void run() {

        }
    };
    public CombatItemComponent(Runnable onUse, Map<String, Object> data) {
        super(data);
        this.onUse = onUse;
    }

    @Override
    public void onUse(Entity entity) {
        onUse.run();
    }
}
