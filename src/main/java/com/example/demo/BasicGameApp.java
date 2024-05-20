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
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.ui.ProgressBar;
import com.example.demo.components.*;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BasicGameApp extends GameApplication {

    public static final int CELL_SIZE = 16;
    Entity player;
    private ProgressBar healthBar;

    private PlayerComponent playerComponent;

    private GridPane inventoryGrid;
    private final boolean inventoryVisible = true;

    private Text coinText;
    private HBox coinCounterBox;

    private Music backgroundMusic;
    private Entity enemy;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1080);
        settings.setHeight(920);
        settings.setTitle("Potato Game");
        settings.setVersion("0.3");
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory());
        settings.setTicksPerSecond(60);
        settings.setMainMenuEnabled(true);
    }


    @Override
    protected void initUI() {

        // Create black background for the inventory

        getGameScene().addUINode(playerComponent.getInventoryView());

        getGameScene().addUINode(playerComponent.getHealthBar());

        coinText = new Text("0");
        coinText.setStyle("-fx-font-size: 24px; -fx-fill: black;");

        // Load coin animation
        AnimatedTexture coinAnim = texture("Items/Coin2.png").toAnimatedTexture(4, Duration.millis(500)).loop(); // Load your coin animation sprite sheet

        // Create HBox for coin counter
        coinCounterBox = new HBox(5, coinAnim, coinText);
        coinCounterBox.setTranslateX(50);
        coinCounterBox.setTranslateY(90);
        coinCounterBox.setAlignment(Pos.CENTER_LEFT);
        coinText.textProperty().bind(getWorldProperties().intProperty("coin").asString());


        getGameScene().addUINode(coinCounterBox);


    }

    protected void initGameVars(Map<String, Object> vars) {
        vars.put("coin", 0);

    }



    @Override
    protected void initGame() {
        GameFactory gameFactory = new GameFactory();
        getGameWorld().addEntityFactory(gameFactory);
        setLevelFromMap("startLevel.tmx");

        // Loading the level from the TMX map

        player = spawn("player");
        getGameWorld().addEntity(player);
        System.out.println(player);

        getGameScene().setBackgroundColor(Color.THISTLE);

        backgroundMusic = FXGL.getAssetLoader().loadMusic("5 - Peaceful.mp3");
        FXGL.getAudioPlayer().loopMusic(backgroundMusic);

        playerComponent = player.getComponent(PlayerComponent.class);

        Viewport viewport = FXGL.getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setZoom(2.5);
        //viewport.setBounds(0, 0, (int) world.getWidth()*2, (int) world.getHeight()*2);
        viewport.setLazy(true);

    }


    @Override
    protected void initInput() {



        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveRight();
            }

            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).setCurrentDirection("right");
            }

            @Override
            protected void onActionEnd() {
                if (Objects.equals(player.getComponent(PlayerComponent.class).getCurrentDirection(), "right")){
                    player.getComponent(PlayerComponent.class).setCurrentDirection("stop");
                }
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveLeft();
            }

            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).setCurrentDirection("left");
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).setCurrentDirection("stop");
            }

        }, KeyCode.Q);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveUp();
                player.getComponent(PlayerComponent.class).setCurrentDirection("up");

            }


            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).setCurrentDirection("stop");


            }
        }, KeyCode.Z);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveDown();
                player.getComponent(PlayerComponent.class).setCurrentDirection("down");
            }


            @Override
            protected void onActionEnd() {
                    playerComponent.setCurrentDirection("stop");
            }

        }, KeyCode.S);

        onKeyDown(KeyCode.LEFT,() -> playerComponent.getInventoryView().selectCellToLeft());
        onKeyDown(KeyCode.RIGHT,() -> playerComponent.getInventoryView().selectCellToRight());
        onKeyDown(KeyCode.UP,() -> playerComponent.getInventoryView().selectCellAbove());
        onKeyDown(KeyCode.DOWN,() -> playerComponent.getInventoryView().selectCellBelow());

        onKeyDown(KeyCode.ENTER, () -> {

            String nameOfItem = playerComponent.getInventoryView().getNameFromSelectedNode();

            if (!Objects.equals(nameOfItem, "Empty")){
                Entity item = spawn(nameOfItem, 500, 500); // utilise in trick qui spawn l objet hors de l ecran pour l utiliser
                if(item.hasComponent(UsableItemComponent.class)){
                    item.getComponent(UsableItemComponent.class).onUse(player);
                    player.getComponent(PlayerInventoryComponent.class).remove(item.getComponent(UsableItemComponent.class).getName());

                }
                item.removeFromWorld();

            }

        });

        onKeyDown(KeyCode.J, () ->{
            String nameOfItem = playerComponent.getInventoryView().getNameFromSelectedNode();

            if (!Objects.equals(nameOfItem, "Empty")){
                Entity item = spawn(nameOfItem, 1000, 1000); // utilise in trick qui spawn l objet hors de l ecran pour l utiliser
                if(item.hasComponent(UsableItemComponent.class)){
                    item.getComponent(UsableItemComponent.class).onDrop(player.getPosition(), nameOfItem);
                    player.getComponent(PlayerInventoryComponent.class).remove(item.getComponent(UsableItemComponent.class).getName());
                }
                else{
                    item.getComponent(EquipedItemComponent.class).onDrop(player.getPosition(), nameOfItem);
                    player.getComponent(PlayerInventoryComponent.class).remove(item.getComponent(EquipedItemComponent.class).getName());

                }
                item.removeFromWorld();

            }
        });

        onKeyDown(KeyCode.K, () -> {
            setLevelFromMap("house.tmx");
            player.setPosition(75, 130);
        });



    }


    @Override
    protected void initPhysics() {

        CustomCollisionHandler.init();


    }



    public static void main(String[] args) {
        launch(args);
    }
}