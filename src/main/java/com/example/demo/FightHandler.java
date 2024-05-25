package com.example.demo;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.scene.SubScene;
import com.example.demo.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class FightHandler {
    private SubScene subScene;
    private Entity player;
    private Entity enemy;
    private PlayerInventoryComponent playerInventory;
    private EnemyComponent enemyComponent;
    private PlayerComponent playerComponent;
    //Here are the modifyable fields
    Text playerStatsText;
    Text enemyStatsText;
    Text centerText;
    HBox buttonBox = new HBox(50);
    Button attackButton = new Button("Attack");
    Button itemButton = new Button("Items");
    Button fleeButton = new Button("Flee");
    Node itemBox;

    public FightHandler(Entity player, Entity enemy) {
        this.player = player;
        this.enemy = enemy;
        this.playerInventory = player.getComponent(PlayerInventoryComponent.class);
        this.playerComponent = player.getComponent(PlayerComponent.class);
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
        playerStatsText = new Text(playerComponent.getStatsText());
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
        enemyStatsText = new Text(this.enemyComponent.getStatsText());
        enemyStatsBox.getChildren().add(enemyStatsText);

        // Create the bottom HBox for buttons
        buttonBox.setPrefSize(600, 243);
        buttonBox.setAlignment(Pos.CENTER);
        root.getChildren().add(buttonBox);

        // Create buttons (adjust as needed)

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
        if (playerComponent.getAttack() - enemyComponent.getDefense() > 0){
            enemyComponent.addHealth( -1 * (playerComponent.getAttack() - enemyComponent.getDefense()));
            centerText.setText("Player dealt " + (playerComponent.getAttack() - enemyComponent.getDefense() + " damage to the enemy"));
        }
        else{
            centerText.setText("Player dealt no damage ... maybe you should use an item");
        }
        checkIfCombatEnd();
        updateStatsText();
        enemyTurn();
    }

    private void updateStatsText() {
        playerStatsText.setText(playerComponent.getStatsText());
        enemyStatsText.setText(enemyComponent.getStatsText());
    }

    private void checkIfCombatEnd() {
        if (enemyComponent.getCurrentHealth() <= 0){
            endCombat();
        }
        else if (playerComponent.getCurrentHealth() <= 0){
            endCombat();

        }
    }

    private void enemyTurn() {


        if(FXGLMath.random(0,10) > 2){
            if ((enemyComponent.getAttack() - playerComponent.getDefense()) > 0){
                playerComponent.changeHealth(-1* (enemyComponent.getAttack() - playerComponent.getDefense()));
                centerText.setText(centerText.getText()+ "and enemy dealt " + (enemyComponent.getAttack() - playerComponent.getDefense() +" damage"));
            }
            else{
                centerText.setText(centerText.getText()+ "and enemy dealt no damage");
            }


        }
        else{
            Entity enemyItem = enemyComponent.getRandomItem();
            if( enemyItem == null){
                centerText.setText(centerText.getText()+ " and Enemy failed it's attack");
            }
            else{
                enemyItem.getComponent(CombatItemComponent.class).onUse(player);
                enemyItem.removeFromWorld();
                centerText.setText(centerText.getText()+ " and Enemy used an item");
            }

        }
        updateStatsText();
    }

    private void useItem(Entity player, Entity enemy) {
        itemBox = createItemSelectionSubscene(playerInventory);
        buttonBox.getChildren().removeAll(attackButton, itemButton, fleeButton);
        buttonBox.getChildren().add(itemBox);
        checkIfCombatEnd();
    }

    private Node createItemSelectionSubscene(PlayerInventoryComponent playerInventory) {
        Map<String, Integer> items = playerInventory.getCombatItems();

        VBox vbox = new VBox(10);  // VBox with 10px spacing between elements
        vbox.setStyle("-fx-padding: 10;");

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            HBox hbox = new HBox(10);  // HBox with 10px spacing between elements
            Text itemName = new Text(entry.getKey());
            Text itemQuantity = new Text("Quantity: " + entry.getValue());
            Button useButton = new Button("Use");

            useButton.setOnAction(e -> {
                playerInventory.remove(itemName.getText());
                Entity usableItem = spawn(itemName.getText(), 10000, 10000);
                centerText.setText("You used " + itemName.getText());
                usableItem.getComponent(CombatItemComponent.class).onUse(player);
                usableItem.removeFromWorld();
                enemyTurn();
                updateStatsText();
                checkIfCombatEnd();
                goToMain();
            });

            hbox.getChildren().addAll(itemName, itemQuantity, useButton);
            vbox.getChildren().add(hbox);
        }

        // Add Close/Go Back button at the bottom
        Button closeButton = new Button("Close / Go Back");
        closeButton.setOnAction(e -> goToMain());
        vbox.getChildren().add(closeButton);

        return vbox;

    }



    private void goToMain() {
        System.out.println(buttonBox.getChildren());
        buttonBox.getChildren().removeAll(buttonBox.getChildren());
        buttonBox.getChildren().addAll(attackButton, itemButton, fleeButton);


    }

    private void flee() {
        // Placeholder for flee logic
         if(FXGLMath.random(0,10) >4){
             System.out.println("Flee!");
             endCombat();
         }
         else{
             centerText.setText("You failed to flee");
             enemyTurn();
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

        //ADD in combat component to be able to get it from the items

        enemy.addComponent(new InCombatComponent());

        FXGL.getSceneService().pushSubScene(combatSubScene);

    }

    private void endCombat() {
        Music backgroundMusic = FXGL.getAssetLoader().loadMusic("17 - Fight.mp3");
        FXGL.getAudioPlayer().stopMusic(backgroundMusic);
        backgroundMusic = FXGL.getAssetLoader().loadMusic("5 - Peaceful.mp3");
        FXGL.getAudioPlayer().loopMusic(backgroundMusic);
        FXGL.getSceneService().popSubScene();
        if(enemyComponent.getCurrentHealth() <= 0) {
            FXGL.getNotificationService().pushNotification("Nice the enemy is dead");
            enemyComponent.onDeath();
        }
        else if (playerComponent.getCurrentHealth() <= 0) {
            playerComponent.onDeath();
        }
    }

}
