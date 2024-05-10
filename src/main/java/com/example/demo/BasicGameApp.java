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
import com.almasb.fxgl.entity.SpawnData;
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
    private boolean isMoveDownKeyPressed = false;
    private boolean isMoveLeftKeyPressed = false;
    private boolean isMoveRightKeyPressed = false;
    private boolean isMoveUpKeyPressed = false;


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
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());

        getGameScene().setBackgroundColor(Color.HOTPINK);
        Entity world = spawn("world");


        grid = AStarGrid.fromWorld(getGameWorld(), 40, 40, CELL_SIZE, CELL_SIZE, type -> CellState.WALKABLE);

        spawn("coin");

        player = spawn("player");
        playerComponent = player.getComponent(PlayerComponent.class);

        spawn("trigger", 200, 200);

        SpawnData wallSpawn = new SpawnData(0 ,0);
        wallSpawn.put("width", CELL_SIZE);
        wallSpawn.put("height", (int)world.getHeight());
        spawn("wall", wallSpawn);

        SpawnData wallSpawn2 = new SpawnData(0 ,0);
        wallSpawn2.put("width", (int)world.getWidth());
        wallSpawn2.put("height", CELL_SIZE);
        spawn("wall", wallSpawn2);

        SpawnData wallSpawn3 = new SpawnData((int)world.getWidth()-CELL_SIZE  ,0);
        wallSpawn3.put("width", CELL_SIZE);
        wallSpawn3.put("height", (int)world.getHeight());
        spawn("wall", wallSpawn3);

        SpawnData wallSpawn4 = new SpawnData(0 , (int)world.getHeight() - CELL_SIZE );
        wallSpawn4.put("width", (int)world.getWidth());
        wallSpawn4.put("height", CELL_SIZE);
        spawn("wall", wallSpawn4);

        Viewport viewport = FXGL.getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setBounds(0, 0, (int) world.getWidth()*2, (int) world.getHeight()*2);
        viewport.setLazy(true);

    }


    @Override
    protected void initInput() {

        // Track key presses and releases
        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                isMoveDownKeyPressed = true;
                playerComponent.moveDown();
            }

            @Override
            protected void onActionEnd() {
                isMoveDownKeyPressed = false;
                if (!isMoveUpKeyPressed) {
                    playerComponent.stopY();
                }
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                isMoveLeftKeyPressed = true;
                playerComponent.moveLeft();
            }

            @Override
            protected void onActionEnd() {
                isMoveLeftKeyPressed = false;
                if (!isMoveRightKeyPressed) {
                    playerComponent.stopX();
                }
            }
        }, KeyCode.Q);

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                isMoveRightKeyPressed = true;
                playerComponent.moveRight();
            }

            @Override
            protected void onActionEnd() {
                isMoveRightKeyPressed = false;
                if (!isMoveLeftKeyPressed) {
                    playerComponent.stopX();
                }
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Move UP") {
            @Override
            protected void onActionBegin() {
                isMoveUpKeyPressed = true;
                playerComponent.moveUp();
            }

            @Override
            protected void onActionEnd() {
                isMoveUpKeyPressed = false;
                if (!isMoveDownKeyPressed) {
                    playerComponent.stopY();
                }
            }
        }, KeyCode.Z);
    }




    @Override
    protected void initPhysics() {

        FXGL.getPhysicsWorld().setGravity(0, 0);

        CustomCollisionHandler.init();

        // order of types on the right is the same as on the left

    }



    public static void main(String[] args) {
        launch(args);
    }
}