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
import com.almasb.fxgl.ui.FXGLScrollPane;
import com.almasb.fxgl.ui.InGamePanel;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.VerticalDirection;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    Entity player;
    private AStarGrid grid;
    private ProgressBar healthBar;

    private PlayerComponent playerComponent;

    VBox inventoryBox = new VBox();
    private boolean isInventoryOpen = false;


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
        settings.setTicksPerSecond(120);
    }


    @Override
    protected void initUI() {
        healthBar = new ProgressBar(false);
        healthBar.setMaxValue(playerComponent.getMaxHealth());
        healthBar.setCurrentValue(playerComponent.getCurrentHealth());
        healthBar.setLabelVisible(false);
        healthBar.setTranslateX(50);
        healthBar.setTranslateY(50);
        healthBar.setBackgroundFill(Color.RED);
        healthBar.setFill(Color.GREEN);
        healthBar.setWidth(playerComponent.getMaxHealth());
        healthBar.setHeight(30);

        // Create black background for the inventory
        Rectangle background = new Rectangle(100, 100, Color.BLACK);
        background.setOpacity(0.5); // Set opacity to make it slightly transparent

        // Create a VBox to hold inventory items
        VBox inventoryBox = new VBox(10);
        inventoryBox.setTranslateX(600);
        inventoryBox.setTranslateY(50);
        inventoryBox.setVisible(false); // Hide inventory initially

        // Add some sample items to the inventory
        for (int i = 0; i < 5; i++) {
            Label itemLabel = new Label("Item " + (i + 1));
            inventoryBox.getChildren().add(itemLabel);
        }

        // StackPane to hold the background and inventory
        StackPane inventoryPane = new StackPane();
        inventoryPane.getChildren().addAll(background, inventoryBox);
        FXGL.getGameScene().addUINode(inventoryPane);



        getGameScene().addUINode(healthBar);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());

        getGameScene().setBackgroundColor(Color.THISTLE);
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
        onKey(KeyCode.Z, () -> {
            playerComponent.moveUp();
        });
        onKey(KeyCode.Q, () -> {
            playerComponent.moveLeft();
        });
        onKey(KeyCode.S, () -> {
            playerComponent.moveDown();
        });
        onKey(KeyCode.D, () -> {
            playerComponent.moveRight();
        });

        onKeyUp( KeyCode.R, () -> {
            playerComponent.setMaxHealth(playerComponent.getMaxHealth()+5);
        });


        FXGL.getInput().addAction(new UserAction("Toggle Inventory") {
            @Override
            protected void onActionBegin() {
                inventoryBox.setVisible(!isInventoryOpen);
                isInventoryOpen = !isInventoryOpen;
            }
        }, KeyCode.E);

    }




    @Override
    protected void initPhysics() {

        CustomCollisionHandler.init();

        // order of types on the right is the same as on the left

    }

    @Override
    protected void onUpdate(double tpf) {
        healthBar.setCurrentValue(playerComponent.getCurrentHealth());
        healthBar.setMaxValue(playerComponent.getMaxHealth());
        healthBar.setWidth(playerComponent.getMaxHealth());

    }

    public static void main(String[] args) {
        launch(args);
    }
}