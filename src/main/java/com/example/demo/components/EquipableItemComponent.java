package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;

public class EquipableItemComponent extends ItemComponent{
    @Override
    public void onUse(Entity entity) {
        entity.getComponent(PlayerComponent.class).equip(this.entity);
    }
}
