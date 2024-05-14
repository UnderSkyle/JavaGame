package com.example.demo;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.example.demo.components.InventoryComponent;
import com.example.demo.components.PlayerComponent;
import com.example.demo.components.UsableItemComponent;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.example.demo.BasicGameApp.CELL_SIZE;
import static com.example.demo.GameTypes.*;

public class GameFactory implements EntityFactory {

    @Spawns("player")
    public Entity spawnPlayer(SpawnData data) {
        return  entityBuilder(data)
                .type(PLAYER)
                .at(getAppWidth() /2.0, getAppHeight() /2.0)
                .bbox(new HitBox(new Point2D(-8, -8), BoundingShape.box(30, 30)))
                .with(new PlayerComponent())
                .with(new InventoryComponent())
                .collidable()
                .buildAndAttach();
    }

    @Spawns("coin")
    public Entity spawnCoin(SpawnData data) {
        return  entityBuilder(data)
                .type(COIN)
                .at(500, 200)
                .viewWithBBox(new Circle(20, 20, 15,Color.YELLOW))
                .collidable()
                .buildAndAttach();
    }

    @Spawns("wall")
    public Entity spawnWall(SpawnData data) {
        int width = data.get("width");
        int height = data.get("height");

        return  entityBuilder(data)
                .type(WALL)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(width, height, Color.DEEPSKYBLUE))
                .collidable()
                .buildAndAttach();
    }

    @Spawns("trigger")
    public Entity spawnTrigger(SpawnData data) {
        return  entityBuilder(data)
                .type(WARPZONE)
                .at(data.getX(), data.getY()) // Set the position using the arguments
                .viewWithBBox(new Rectangle(10, 10, Color.RED))
                .collidable()
                .buildAndAttach();

    }

    @Spawns("world")
    public Entity spawnWorld(SpawnData data) { //TODO: REPLACE THAT WITH MAP WHEN FINISHED
        return entityBuilder()
                .type(WORLD)
                .at(0,0)
                .viewWithBBox(new Rectangle(getAppWidth(), getAppHeight(), Color.BEIGE))
                .zIndex(-1)
                .buildAndAttach();
    }

    @Spawns("health potion")
    public Entity spawnItem(SpawnData data) {
        Runnable onUse = new Runnable() {
            final Entity player = getGameWorld().getSingleton(PLAYER);

            @Override
            public void run() {
                player.getComponent(PlayerComponent.class).setCurrentHealth(player.getComponent(PlayerComponent.class).getCurrentHealth()+5);
            }
        };

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "health potion");
        Texture texture = texture("""
                       Items/health potion.png""");
        texture.setScaleX(2);
        texture.setScaleY(2);

        return  entityBuilder(data)
               .type(ITEM)
               .at(data.getX(), data.getY())
               .viewWithBBox(texture)
                .with(new UsableItemComponent(onUse, itemData))
                .collidable()
               .buildAndAttach();
    }
    //TODO: DO THE INVENTORY BEFORE PLAYING WITH THE ITEMS
}
