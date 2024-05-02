package com.example.demo;
/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.example.demo.components.PlayerComponent;
import com.almasb.fxgl.entity.level.Level;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.example.demo.GameTypes.*;
import javafx.scene.media.Media;


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BasicGameApp extends GameApplication {

    public static final int CELL_SIZE = 30;
    public static final int SPEED = 200;
    Entity player;
    private AStarGrid grid;

    private PlayerComponent playerComponent;

    public AStarGrid getGrid() {
        return grid;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("Potato Game");
        settings.setVersion("0.3");
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory());
        settings.setDeveloperMenuEnabled(true);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);

    }


    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Move Down") {

            @Override
            protected void onActionBegin() {
                player.getComponent(PhysicsComponent.class).setVelocityY(SPEED);

            }
            @Override
            protected void onActionEnd() {
                PhysicsComponent playerPhysics= player.getComponent(PhysicsComponent.class);

                if(playerPhysics.getVelocityY() > 0 ){
                    player.getComponent(PhysicsComponent.class).setVelocityY(0);

                }
            }
        }, KeyCode.S);
        FXGL.getInput().addAction(new UserAction("Move Left") {


            @Override
            protected void onActionBegin() {
                player.getComponent(PhysicsComponent.class).setVelocityX(-SPEED);

            }

            @Override
            protected void onActionEnd() {
                PhysicsComponent playerPhysics= player.getComponent(PhysicsComponent.class);

                if(playerPhysics.getVelocityX() < 0 ){
                    player.getComponent(PhysicsComponent.class).setVelocityX(0);

                }
            }
        }, KeyCode.Q);
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PhysicsComponent.class).setVelocityX(SPEED);
            }

            @Override
            protected void onActionEnd() {
                PhysicsComponent playerPhysics= player.getComponent(PhysicsComponent.class);
                if(playerPhysics.getVelocityX() > 0 ){
                    player.getComponent(PhysicsComponent.class).setVelocityX(0);

                }}
        }, KeyCode.D);
        FXGL.getInput().addAction(new UserAction("Move UP") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PhysicsComponent.class).setVelocityY(-SPEED);
            }

            @Override
            protected void onActionEnd() {
                PhysicsComponent playerPhysics= player.getComponent(PhysicsComponent.class);
                if(playerPhysics.getVelocityY() < 0 ){
                    player.getComponent(PhysicsComponent.class).setVelocityY(0);

                }
            }
        }, KeyCode.Z);

        getInput().addAction(new UserAction("Speak Woman") {
            @Override
            protected void onAction() {
                getDialogService().showMessageBox("SHUT UP WOMAN", () -> {
                    // code to run after dialog is dismissed
                });
            }
        },KeyCode.T);

    }



    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());

        getGameScene().setBackgroundColor(Color.BLACK);
        Entity world = entityBuilder()
                .type(WORLD)
                .viewWithBBox(new Rectangle(getAppWidth(), getAppHeight(), Color.BEIGE))
                .zIndex(-1)
                .buildAndAttach();




        grid = AStarGrid.fromWorld(getGameWorld(), 40, 40, CELL_SIZE, CELL_SIZE, type -> CellState.WALKABLE);

        spawn("coin");

        player = spawn("player");
        //playerComponent = player.getComponent(PlayerComponent.class);

        initScreenBounds();

        Viewport viewport = FXGL.getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setBounds(0, 0, (int) world.getWidth()*2, (int) world.getHeight()*2);
        viewport.setLazy(true);

    }

    @Override
    protected void initPhysics() {

        FXGL.getPhysicsWorld().setGravity(0, 0);

        // order of types on the right is the same as on the left
        onCollisionBegin(GameTypes.PLAYER, GameTypes.COIN, (player, coin) -> {
            coin.removeFromWorld();

        });

        onCollisionBegin(GameTypes.PLAYER, WALL, (player, boundary) -> {
            /*Point2D center = boundary.getCenter();        //this is the function to have collisions
            double dx = center.getX() - player.getX();
            double dy = center.getY() - player.getY();
            System.out.println(dx);
            System.out.println(dy);
            if (dx > 395) {
                //set center.X to something else
                player.translateX(15);

            }
            else if ( dy > 395) {
                //set center.Y to something else
                player.translateY(15);
            }
            else if ( dx > -395 && dx < -370) {
                player.translateX(-15);
            }
            else if (dy > -395 && dy < -370) {
                player.translateY(-15);

            }
            */
            player.getComponent(PhysicsComponent.class).setVelocityY(0);
            player.getComponent(PhysicsComponent.class).setVelocityX(0);

        });

    }
    private void initScreenBounds(){
        Entity walls = entityBuilder()
                .type(WALL)
                .collidable()
                .with(new CollidableComponent(true))
                .buildScreenBounds(40);

        getGameWorld().addEntity(walls);
    }


    public static void main(String[] args) {
        launch(args);
    }
}