package com.example.demo;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.example.demo.components.PlayerComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getDialogService;

public class CustomCollisionHandler {

    public static void init() {
        
        // Register collision handlers
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.WALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

                // Get the position of the wall
                Point2D wallPosition = wall.getPosition();

                // Get the position of the player
                Point2D playerPosition = player.getPosition();

                // Calculate the difference in position
                double deltaX = wallPosition.getX() - playerPosition.getX();
                double deltaY = wallPosition.getY() - playerPosition.getY();

                System.out.println(deltaX + " "+ deltaY );
                // Determine which side of the wall the player is colliding with
                if (FXGLMath.abs(deltaX) < FXGLMath.abs(deltaY)) {
                    playerComponent.stopX();
                    // Horizontal collision
                    if (deltaX < 0) {
                        // Wall is on the left side
                        playerComponent.canMoveLeft = false;
                    } else {
                        // Wall is on the right side
                        playerComponent.canMoveRight = false;
                    }
                } else {
                    playerComponent.stopY();
                    // Vertical collision
                    if (deltaY < 0) {
                        // Wall is on the top side
                        playerComponent.canMoveUp = false;
                    } else {
                        // Wall is on the bottom side
                        playerComponent.canMoveDown = false;
                    }
                }
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

                // Get the position of the wall
                Point2D wallPosition = wall.getPosition();

                // Get the position of the player
                Point2D playerPosition = player.getPosition();

                // Calculate the difference in position
                double deltaX = wallPosition.getX() - playerPosition.getX();
                double deltaY = wallPosition.getY() - playerPosition.getY();

                System.out.println(deltaX + " "+ deltaY );
                // Determine which side of the wall the player is colliding with
                if (FXGLMath.abs(deltaX) < FXGLMath.abs(deltaY)) {

                    // Horizontal collision
                    if (deltaX < 0) {
                        // Wall is on the left side
                        playerComponent.canMoveLeft = true;
                    } else {
                        // Wall is on the right side
                        playerComponent.canMoveRight = true;
                        System.out.println("canMoveRight");
                    }
                } else {
                    System.out.println("you souldnt see that");

                    // Vertical collision
                    if (deltaY < 0) {
                        // Wall is on the top side
                        playerComponent.canMoveUp = true;
                    } else {
                        // Wall is on the bottom side
                        playerComponent.canMoveDown = true;
                    }
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
                player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(10,10));

            }
        });

    }

    private static Point2D getNormal(Entity entityA, Entity entityB) {
        Point2D positionA = entityA.getPosition();
        Point2D positionB = entityB.getPosition();

        // Calculate the vector from entityA to entityB
        Point2D vector = positionB.subtract(positionA);

        // Normalize the vector to get the collision normal
        return vector.normalize();
    }
}
