package com.example.demo;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.example.demo.components.EquipedItemComponent;
import com.example.demo.components.ItemComponent;
import com.example.demo.components.PlayerComponent;
import com.example.demo.components.UsableItemComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class CustomCollisionHandler {

    public static void init() {
        
        // Register collision handlers
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WALL) {
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
                System.out.println("hey");
                coin.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WARPZONE) {
            @Override
            protected void onCollisionBegin(Entity player, Entity warpzone) {
                player.getComponent(PlayerComponent.class).yeet(new Point2D(getAppWidth()/2.0,getAppHeight()/2.0));

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
                item.getComponent(EquipedItemComponent.class).onPickup(player);
            }
        });

    }

}
