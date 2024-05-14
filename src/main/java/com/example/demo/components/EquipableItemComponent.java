package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;

import java.util.Map;

public class EquipableItemComponent extends ItemComponent{

    public EquipableItemComponent(Map<String, Object> data) {
        super(data);
    }


    @Override
    public void onUse(Entity entity) {
        entity.getComponent(PlayerComponent.class).equip(this.entity);
    }
}
