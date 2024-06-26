package com.example.demo;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.example.demo.components.*;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getDialogService;


public class CustomCollisionHandler {

    public static void init() {
        
        // Register collision handlers
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                if (wall.hasComponent(DoorComponent.class)){
                    if(player.getComponent(PlayerInventoryComponent.class).hasItem("key")){
                        wall.getComponent(DoorComponent.class).openDoor();
                        player.getComponent(PlayerInventoryComponent.class).remove("key");
                    }
                    else{
                        getDialogService().showMessageBox("You need a key to open the door");
                    }
                } else if (wall.hasComponent(WaterComponent.class)){
                    if(player.getComponent(PlayerInventoryComponent.class).hasItem("raft")){
                        wall.getComponent(WaterComponent.class).RemoveObstacleComponent();
                    }
                    else{
                        player.getComponent(PlayerComponent.class).onDeath();
                    }
                    
                }
            }

            @Override
            protected void onCollision(Entity player, Entity wall) {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
                if( wall.getWidth() <= wall.getHeight()) {
                    if (player.getX() < wall.getX()) {

                        playerComponent.moveLeft();
                    } else if (player.getX() > wall.getX()) {

                        playerComponent.moveRight();
                    }
                }
                if( wall.getWidth() >= wall.getHeight()) {
                    if (player.getY() < wall.getY()) {
                        playerComponent.moveUp();
                    } else if (player.getY() > wall.getY()) {

                        playerComponent.moveDown();
                    }
                }
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.COIN) {

            @Override
            protected void onCollision(Entity player, Entity coin) {
                player.getComponent(PlayerComponent.class).addCoin(1);
                coin.removeFromWorld();
                FXGL.inc("coin", +5);
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WARPZONE) {
            @Override
            protected void onCollisionBegin(Entity player, Entity warpzone) {
                player.getComponent(PlayerComponent.class).yeet(new Point2D(100, 200));

            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.USABLE_ITEM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity item){
                item.getComponent(UsableItemComponent.class).onPickup(player);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.EQUIPPED_ITEM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity item){
                if(!player.getComponent(PlayerInventoryComponent.class).hasItem(item.getComponent(EquipedItemComponent.class).getName())){
                    item.getComponent(EquipedItemComponent.class).onPickup(player);
                    item.getComponent(EquipedItemComponent.class).onUse(player);
                }

            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.COMBAT_ITEM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity item){
                item.getComponent(CombatItemComponent.class).onPickup(player);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.NPC) {
            @Override
            protected void onCollisionBegin(Entity player, Entity npc){
                npc.getComponent(NPCComponent.class).interact();
                player.setPosition(npc.getX(), npc.getY()+30);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.ENEMY) {
            protected void onCollisionBegin(Entity player, Entity enemy) {
                new FightHandler(player, enemy).startCombat(player, enemy);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WORLD_TRIGGER) {
            protected void onCollisionBegin(Entity player, Entity trigger) {
                LevelChangeHandler.setLevelTo(trigger.getString("to"), player, trigger.getString("spawn name"));
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WIN_ITEM) {
            protected void onCollisionBegin(Entity player, Entity winItem) {
                player.getComponent(PlayerComponent.class).win();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.DS_NPC) {
            @Override
            protected void onCollisionBegin(Entity player, Entity npc){
                npc.getComponent(DsNPCComponent.class).interact();
                player.setPosition(npc.getX(), npc.getY()+30);
            }
        });


    }

}
