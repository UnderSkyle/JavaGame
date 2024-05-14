package com.example.demo.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public abstract class ItemComponent extends Component {

    private final Map<String, Object> data = new HashMap<>();

    public ItemComponent(Map<String, Object> data) {
        this.data.putAll(data);
    }

    public String getName() {
        return (String) data.get("name");
    }


    public void onPickup(Entity entity) {
        if(!entity.getComponent(InventoryComponent.class).isFull()){
            entity.getComponent(InventoryComponent.class).add(getName());
            this.entity.removeFromWorld();
        };
    }

    public abstract void onUse(Entity entity);
    
    

    public void onDrop(Point2D position, String Name) {

        SpawnData itemSpawn = new SpawnData(position.getX() ,position.getY());
        itemSpawn.put("itemData", this.data);
        itemSpawn.put("position", position);
        
        spawn("wall", itemSpawn);

    }
        
}
