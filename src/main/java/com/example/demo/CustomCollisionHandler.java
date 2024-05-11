package com.example.demo;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.example.demo.components.PlayerComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class CustomCollisionHandler {

    public static void init() {
        
        // Register collision handlers
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

                if( wall.getWidth() <= wall.getHeight()) {
                    if (player.getX() < wall.getX()) {
                        playerComponent.setCanMoveRight(false);
                        playerComponent.stopX();
                    } else if (player.getX() > wall.getX()) {
                        playerComponent.setCanMoveLeft(false);
                        playerComponent.stopX();
                    }
                }
                if( wall.getWidth() >= wall.getHeight()) {
                    if (player.getY() < wall.getY()) {
                        playerComponent.setCanMoveDown(false);
                        playerComponent.stopY();
                    } else if (player.getY() > wall.getY()) {
                        playerComponent.setCanMoveUp(false);
                        playerComponent.stopY();
                    }
                }
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

                // Get the position of the wall
                if (player.getX() < wall.getX()) {
                    playerComponent.setCanMoveRight(true);
                } else if (player.getX() > wall.getX()) {
                    playerComponent.setCanMoveLeft(true);
                }

                if (player.getY() < wall.getY()) {
                    playerComponent.setCanMoveDown(true);

                } else if (player.getY() > wall.getY()) {
                    playerComponent.setCanMoveUp(true);
                }

            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.COIN) {

            @Override
            protected void onCollision(Entity player, Entity coin) {
                getDialogService().showMessageBox("COIN ++", coin::removeFromWorld);
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WARPZONE) {
            @Override
            protected void onCollisionBegin(Entity player, Entity warpzone) {
                System.out.println("hello");
                player.getComponent(PlayerComponent.class).stopX();
                player.getComponent(PlayerComponent.class).stopY();
                player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(getAppWidth()/2.0,getAppHeight()/2.0));

            }
        });

    }

}
