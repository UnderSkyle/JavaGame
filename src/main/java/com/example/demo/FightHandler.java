package com.example.demo;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.scene.SubScene;
import com.example.demo.components.EnemyComponent;
import com.example.demo.components.PlayerInventoryComponent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class FightHandler {
    private SubScene subScene;
    private Entity player;
    private Entity enemy;
    private PlayerInventoryComponent playerInventory;
    private EnemyComponent enemyComponent;

    //Here are the modifyable fields
    Text playerStatsText;
    Text enemyStatsText;
    Text centerText;

    public FightHandler(Entity player, Entity enemy) {
        this.player = player;
        this.enemy = enemy;
        this.playerInventory = player.getComponent(PlayerInventoryComponent.class);
        this.enemyComponent = enemy.getComponent(EnemyComponent.class);
    }

    private Pane createCombatView(Entity player, Entity enemy) {
        // Create the root VBox
        VBox root = new VBox();
        root.setPrefSize(getAppWidth(), getAppHeight());
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: rgba(255,255,255,0.99);");

        // Create the top HBox
        HBox topBox = new HBox();
        topBox.setPrefSize(600, 300);
        topBox.setAlignment(Pos.CENTER);
        root.getChildren().add(topBox);

        // Create the left player stats section
        HBox playerStatsBox = new HBox();
        playerStatsBox.setAlignment(Pos.CENTER);
        playerStatsBox.setPadding(new Insets(0, 100, 0, 0)); // Adjust padding as needed
        topBox.getChildren().add(playerStatsBox);

        // Create the text node for player stats (replace with actual stats)
        playerStatsText = new Text("Player Stats");
        playerStatsBox.getChildren().add(playerStatsText);

        // Create the center section that will contain what happens on each turn
        HBox centerBox = new HBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(0, 100, 0, 100)); // Adjust padding as needed
        topBox.getChildren().add(centerBox);

        // Create the text node for what happens
        centerText = new Text("Action Details Here");
        centerBox.getChildren().add(centerText);

        // Create the right enemy stats section
        HBox enemyStatsBox = new HBox();
        enemyStatsBox.setAlignment(Pos.CENTER);
        enemyStatsBox.setPadding(new Insets(0, 0, 0, 100)); // Adjust padding as needed
        topBox.getChildren().add(enemyStatsBox);

        // Create the text node for enemy stats (replace with actual stats)
        enemyStatsText = new Text("Enemy Stats");
        enemyStatsBox.getChildren().add(enemyStatsText);

        // Create the bottom HBox for buttons
        HBox buttonBox = new HBox(50);
        buttonBox.setPrefSize(600, 243);
        buttonBox.setAlignment(Pos.CENTER);
        root.getChildren().add(buttonBox);

        // Create buttons (adjust as needed)
        Button attackButton = new Button("Attack");
        Button itemButton = new Button("Items");
        Button fleeButton = new Button("Flee");

        attackButton.setOnAction(e -> attack());
        itemButton.setOnAction(e -> useItem(player, enemy));
        fleeButton.setOnAction(e -> flee());

        // Set button properties (adjust as needed)
        attackButton.setPrefSize(200, 100);
        itemButton.setPrefSize(200, 100);
        fleeButton.setPrefSize(200, 100);

        // Add buttons to the button box
        buttonBox.getChildren().addAll(attackButton, itemButton, fleeButton);

        return new Pane(root);
    }



    private void attack() {
        // Placeholder for attack logic
        System.out.println("Attack!");
        FXGL.getSceneService().popSubScene();
        enemyComponent.onDeath();
    }

    private void useItem(Entity player, Entity enemy) {
        // Placeholder for item usage logic
    }

    private void flee() {
        // Placeholder for flee logic
         if(FXGLMath.random(0,10) >4){
             System.out.println("Flee!");
             endCombat();
         }
    }

    public void startCombat(Entity player, Entity enemy) {
        Music backgroundMusic = FXGL.getAssetLoader().loadMusic("5 - Peaceful.mp3");
        FXGL.getAudioPlayer().stopMusic(backgroundMusic);
        backgroundMusic = FXGL.getAssetLoader().loadMusic("17 - Fight.mp3");
        FXGL.getAudioPlayer().loopMusic(backgroundMusic);

        // Clear the game scene
        Pane combatView = createCombatView(player, enemy);

        SubScene combatSubScene = new SubScene() {
            @Override
            public void onCreate() {
                getContentRoot().getChildren().add(combatView);
            }
        };



        FXGL.getSceneService().pushSubScene(combatSubScene);

    }

    private void endCombat() {
        FXGL.getSceneService().popSubScene();
        if(enemyComponent.getCurrentHealth() <= 0) {
            enemyComponent.onDeath();
        }
    }

}
