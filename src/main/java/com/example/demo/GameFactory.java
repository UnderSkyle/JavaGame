package com.example.demo;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.Texture;
import com.example.demo.components.*;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.lang.reflect.Array;
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
                .zIndex(3)
                .at(getAppWidth() /2.0, getAppHeight() /2.0)
                .bbox(new HitBox(new Point2D(-8, -8), BoundingShape.box(30, 30)))
                .with(new PlayerComponent())
                .with(new PlayerInventoryComponent())
                .collidable()
                .buildAndAttach();
    }

    @Spawns("coin")
    public Entity spawnCoin(SpawnData data) {
        AnimatedTexture tex = texture("Items/Coin2.png").toAnimatedTexture(4, Duration.millis(500));
        tex.setScaleX(2);
        tex.setScaleY(2);
        tex.loop();
        return  entityBuilder(data)
                .type(COIN)
                .at(500, 200)
                .viewWithBBox(tex)
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

    @Spawns("removableWall")
    public Entity spawnRemovableWall(SpawnData data) {
        return  entityBuilder(data)
               .type(WALL)
               .at(data.getX(), data.getY())
               .viewWithBBox(new Rectangle(CELL_SIZE*2+1, CELL_SIZE*2, Color.YELLOW))
               .collidable()
               .with(new RemovableObstacleComponent())
               .buildAndAttach();
    }

    @Spawns("door")
    public Entity spawnDoor(SpawnData data) {

        Texture doorTexture = texture("Door.png", 32, 16);
        Texture closedTexture = doorTexture.subTexture(new Rectangle2D(0, 0, 16, 16));
        Texture openTexture = doorTexture.subTexture(new Rectangle2D(16, 0, 16, 16));
        closedTexture.setScaleX(2);
        closedTexture.setScaleY(2);
        openTexture.setScaleX(2);
        openTexture.setScaleY(2);

        return  entityBuilder(data)
                .type(WALL)
                .at(data.getX(), data.getY())
                .view(closedTexture)
                .bbox(new HitBox(new Point2D(-1, -1), BoundingShape.box(17, 16)))
                .collidable()
                .with(new DoorComponent(closedTexture, openTexture))
                .buildAndAttach();
    }

    @Spawns("npc")
    public Entity spawnNPC(SpawnData data) {
        Texture texture = texture("NPCS/StandardNPC.png");
        texture.setScaleX(2);
        texture.setScaleY(2);
        NPCComponent ncpComponent;
        if(data.hasKey("onInteract")){
            ncpComponent = new NPCComponent(data.get("text"), data.get("onInteract"));
        }
        else{
            ncpComponent = new NPCComponent(data.get("text"));
        }


        return  entityBuilder(data)
               .type(NPC)
               .at(data.getX(), data.getY())
               .viewWithBBox(texture)
               .collidable()
               .with(ncpComponent)
               .buildAndAttach();
    }

    @Spawns("shop npc")
    public Entity spawnShopNPC(SpawnData data) {
        Texture texture = texture("NPCS/ShopNPC.png");
        texture.setScaleX(2);
        texture.setScaleY(2);
        ShopComponent shop = new ShopComponent();

        Entity shopguy = entityBuilder(data)
                .type(NPC)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .collidable()
                .with(shop)
                .buildAndAttach();

        Runnable onInteract = new Runnable() {
            @Override
            public void run() {
                shopguy.getComponent(ShopComponent.class).show();
            }
        };

        shopguy.addComponent(new NPCComponent("Hey! Welcome to my shop!", onInteract));

        return shopguy;
    }

    @Spawns("rick")
    public Entity spawnRick(SpawnData data) {
        Texture texture = texture("NPCS/StandardNPC.png");
        texture.setScaleX(2);
        texture.setScaleY(2);

        Runnable onInteract = () -> {
            getAudioPlayer().pauseAllMusic();
            getAudioPlayer().playMusic(FXGL.getAssetLoader().loadMusic("Inconspicuous.mp3"));
            Duration duration = Duration.seconds(19);
            FXGL.getGameTimer().runOnceAfter(() -> {
                getAudioPlayer().stopAllMusic();
                Music backgroundMusic = FXGL.getAssetLoader().loadMusic("5 - Peaceful.mp3");;
                getAudioPlayer().loopMusic(backgroundMusic);
            }, duration);
        };
        NPCComponent rickNPC = new NPCComponent("You thought i was a random NPC.... BUT IT WAS ME, RICK!!!", onInteract);

        return entityBuilder(data)
                .type(NPC)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .collidable()
                .with(rickNPC)
                .buildAndAttach();
    }



    @Spawns("health potion")
    public Entity spawnHealthPotion(SpawnData data) {
        Runnable onUse = new Runnable() {
            final Entity player = getGameWorld().getSingleton(PLAYER);

            @Override
            public void run(){
                if(player.getComponent(PlayerComponent.class).getCurrentHealth() <= player.getComponent(PlayerComponent.class).getMaxHealth()-20) {
                    player.getComponent(PlayerComponent.class).setCurrentHealth(player.getComponent(PlayerComponent.class).getCurrentHealth() + 20);
                }
                else{
                    player.getComponent(PlayerComponent.class).setCurrentHealth(player.getComponent(PlayerComponent.class).getMaxHealth());
                }
            }
        };

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "health potion");
        Texture texture = texture("""
                       Items/health potion.png""");
        texture.setScaleX(2);
        texture.setScaleY(2);

        return  entityBuilder(data)
               .type(USABLE_ITEM)
               .at(data.getX(), data.getY())
               .viewWithBBox(texture)
                .with(new UsableItemComponent(onUse, itemData))
                .collidable()
               .buildAndAttach();
    }

    @Spawns("sword")
    public Entity spawnSword(SpawnData data) {

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "sword");
        Map<String, Integer> statusChange = new HashMap<>();
        statusChange.put("attack", 5);
        itemData.put("changeStatus", statusChange);
        Texture texture = texture("""
                       Items/sword.png""");
        texture.setScaleX(2);
        texture.setScaleY(2);

        Runnable onUse = () -> {
            // Get the player entity
            Entity player = getGameWorld().getSingleton(PLAYER);
            // Get the player component
            PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
            // Get the changeStatus data from the item
            Map<String, Integer> changeStatus = (Map<String, Integer>)itemData.get("changeStatus");
            // Update the player's stats
            playerComponent.updateStats(changeStatus);
        };

        return  entityBuilder(data)
                .type(EQUIPPED_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new EquipedItemComponent(itemData, onUse))
                .collidable()
                .buildAndAttach();
    }

    @Spawns("life potion")
    public Entity spawnLifePotion(SpawnData data) {
        Runnable onUse = new Runnable() {
            final Entity player = getGameWorld().getSingleton(PLAYER);

            @Override
            public void run(){
                player.getComponent(PlayerComponent.class).setMaxHealth((player.getComponent(PlayerComponent.class).getMaxHealth() + 10));
            }
        };

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "life potion");
        Texture texture = texture("""
                       Items/life potion.png""");
        texture.setScaleX(2);
        texture.setScaleY(2);

        return  entityBuilder(data)
                .type(USABLE_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new UsableItemComponent(onUse, itemData))
                .collidable()
                .buildAndAttach();
    }

    @Spawns("key")
    public Entity spawnKey(SpawnData data) {

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "key");
        Texture texture = texture("""
                       Items/key.png""");
        texture.setScaleX(2);
        texture.setScaleY(2);

        Runnable onUse = () -> {

        };

        return  entityBuilder(data)
                .type(EQUIPPED_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new EquipedItemComponent(itemData, onUse))
                .collidable()
                .buildAndAttach();
    }

    @Spawns("enemy")
    public Entity spawnEnemy(SpawnData data) {


        return  entityBuilder(data)
               .type(ENEMY)
               .at(data.getX(), data.getY())
               .viewWithBBox(new Rectangle(30, 30, Color.DARKVIOLET))
               .collidable()
               .buildAndAttach();
    }




}
