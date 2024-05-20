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
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.fxgl.scene.SubScene;
import com.example.demo.components.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGL.*;


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
        vars.put("combatActive", false);

    }



    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameFactory());
        backgroundMusic = FXGL.getAssetLoader().loadMusic("5 - Peaceful.mp3");
        FXGL.getAudioPlayer().loopMusic(backgroundMusic);

        getGameScene().setBackgroundColor(Color.THISTLE);
        Entity world = spawn("world");

        spawn("coin");

        player = spawn("player");
        playerComponent = player.getComponent(PlayerComponent.class);

        spawn("trigger", 200, 200);

        spawn("health potion", 200, 300);
        spawn("health potion", 300, 300);
        spawn("health potion", 400, 400);
        spawn("sword", 400, 200);

        spawn("life potion", 350 ,450);
        spawn("removableWall", 350, 600);

        spawn("rick", 100, 100);
        SpawnData npcSpawn2 = new SpawnData(140 ,100);
        npcSpawn2.put("text", "BAKA");
        spawn("npc", npcSpawn2);
        spawn("shop npc", 200, 500);

        spawn("door", 800 ,800);
        spawn("key", 350 ,350);

        spawn("quest npc", 400 , 500);

        spawn("enemy", new SpawnData(500,100).put("health", 100));
        spawn("bomb", 600, 600);
        spawn("fire scroll", 900, 400);

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

        onKeyDown(KeyCode.B, () -> {getDialogService().showMessageBox("You Win!", () -> getGameController().gotoMainMenu());});

        onKeyDown(KeyCode.LEFT,() -> playerComponent.getInventoryView().selectCellToLeft());
        onKeyDown(KeyCode.RIGHT,() -> playerComponent.getInventoryView().selectCellToRight());
        onKeyDown(KeyCode.UP,() -> playerComponent.getInventoryView().selectCellAbove());
        onKeyDown(KeyCode.DOWN,() -> playerComponent.getInventoryView().selectCellBelow());

        onKeyUp( KeyCode.R, () -> playerComponent.setMaxHealth(playerComponent.getMaxHealth()+5));
        onKeyDown( KeyCode.T, () -> playerComponent.setCurrentHealth((playerComponent.getCurrentHealth()+5)));

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

            if (nameOfItem != "Empty"){
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

        onKeyDown(KeyCode.K, () -> System.out.println(player.getComponent(PlayerInventoryComponent.class).getInventory()));
        onKeyDown(KeyCode.U, () -> {
            Rectangle2D areaSelection = new Rectangle2D(player.getX()-player.getWidth()*2, player.getY()-player.getHeight()*2, player.getWidth()*4, player.getHeight()*4);
            List<Entity> entityList = getGameWorld().getEntitiesInRange(areaSelection);
            for(Entity entity : entityList){
                if(entity.hasComponent(RemovableObstacleComponent.class)){
                    if(entity.hasComponent(CollidableComponent.class)){
                        entity.getComponent(RemovableObstacleComponent.class).RemoveObstacleComponent();
                        entity.getViewComponent().setVisible(false);
                    }
                    else{
                        entity.getComponent(RemovableObstacleComponent.class).AddObstacleComponent();
                        entity.getViewComponent().setVisible(true);
                    }

                }
            }
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