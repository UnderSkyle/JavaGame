package com.example.demo;
/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import com.example.demo.components.PlayerComponent;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.example.demo.GameTypes.*;


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BasicGameApp extends GameApplication {

    public static final int CELL_SIZE = 30;
    Entity player;
    private ProgressBar healthBar;

    private PlayerComponent playerComponent;

    private GridPane inventoryGrid;
    private boolean inventoryVisible = true;



    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1080);
        settings.setHeight(920);
        settings.setTitle("Potato Game");
        settings.setVersion("0.3");
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory());
        settings.setTicksPerSecond(120);
    }


    @Override
    protected void initUI() {

        // Create black background for the inventory

        getGameScene().addUINode(playerComponent.getInventoryView());

        getGameScene().addUINode(playerComponent.getHealthBar());


    }


    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());

        getGameScene().setBackgroundColor(Color.THISTLE);
        Entity world = spawn("world");

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
        onKey(KeyCode.Z, () -> playerComponent.moveUp());
        onKey(KeyCode.Q, () -> playerComponent.moveLeft());
        onKey(KeyCode.S, () -> playerComponent.moveDown());
        onKey(KeyCode.D, () -> playerComponent.moveRight());

        onKeyDown(KeyCode.LEFT,() -> playerComponent.getInventoryView().selectCellToLeft());
        onKeyDown(KeyCode.RIGHT,() -> playerComponent.getInventoryView().selectCellToRight());
        onKeyDown(KeyCode.UP,() -> playerComponent.getInventoryView().selectCellAbove());
        onKeyDown(KeyCode.DOWN,() -> playerComponent.getInventoryView().selectCellBelow());

        onKeyUp( KeyCode.R, () -> playerComponent.setMaxHealth(playerComponent.getMaxHealth()+5));
        onKeyDown( KeyCode.T, () -> playerComponent.setCurrentHealth((playerComponent.getCurrentHealth()+5)));

    }




    @Override
    protected void initPhysics() {

        CustomCollisionHandler.init();

        // order of types on the right is the same as on the left

    }


    public static void main(String[] args) {
        launch(args);
    }
}