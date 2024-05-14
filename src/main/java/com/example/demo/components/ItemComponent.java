package com.example.demo.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.animation.PauseTransition;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;
import static com.example.demo.GameTypes.PLAYER;

public abstract class ItemComponent extends Component {

    final Map<String, Object> data = new HashMap<>();

    public ItemComponent(Map<String, Object> data) {
        this.data.putAll(data);
    }

    public String getName() {
        return (String) data.get("name");
    } //has to be the same as spawn


    public void onPickup(Entity entity) {
        if(!entity.getComponent(InventoryComponent.class).isFull()){
            if (entity.hasComponent(EquipedItemComponent.class)) {
                entity.getComponent(EquipedItemComponent.class).onUse(entity);
            }
            entity.getComponent(InventoryComponent.class).add(getName());
            this.entity.removeFromWorld();
        };
    }

    public abstract void onUse(Entity entity);
    
    

    public void onDrop(Point2D position, String Name) {

        SpawnData itemSpawn = new SpawnData(position.getX(),position.getY());
        itemSpawn.put("itemData", this.data);
        itemSpawn.put("position", position);
        
        Entity item = spawn(Name, itemSpawn);
        item.getComponent(CollidableComponent.class).addIgnoredType(PLAYER);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            // Reactivate the collide component after 2 seconds
            item.getComponent(CollidableComponent.class).removeIgnoredType(PLAYER);
        });
        pause.play();


    }
        
}
