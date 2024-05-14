package com.example.demo.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public abstract class ItemComponent extends Component {

    private final Map<String, Object> data = new HashMap<>();

    public void onPickup(Entity entity) {
        if(!entity.getComponent(InventoryComponent.class).isFull()){
            entity.getComponent(InventoryComponent.class).add(this.entity);
            this.entity.removeFromWorld();
        };
    }

    public abstract void onUse(Entity entity);
    
    

    public void onDrop(Point2D position) {

        SpawnData wallSpawn = new SpawnData(0 ,0);
        wallSpawn.put("itemData", this.data);
        wallSpawn.put("position", position);
        
        spawn("wall", wallSpawn);

    }
        
}
