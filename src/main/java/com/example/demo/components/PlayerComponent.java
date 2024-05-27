package com.example.demo.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.inventory.ItemStack;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.ui.ProgressBar;
import com.example.demo.PlayerInventoryView;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class PlayerComponent extends Component {

    TransformComponent transformComponent;
    private final double SPEED = 2;
    private int maxHealth = 100;
    private int currentHealth = 0;
    private int cointCount = 0;
    private Text coinCounter = new Text(String.valueOf(cointCount));
    private final PlayerInventoryView inventoryView = new PlayerInventoryView(getEntity());
    private final ProgressBar healthBar = new ProgressBar(false);
    private AnimationChannel animWalkRight, animWalkLeft, animWalkUp, animWalkDown;

    private final AnimatedTexture texture = texture("Player/DownWalk.png").toAnimatedTexture(4, Duration.seconds(2));
    private String currentDirection = "idleDown";
    private boolean isAnimationPlaying = true;
    private int tickDamage = 0;     //This is a type of self damage

    private final Map<String, Integer> status = new HashMap<>();



    public int getAttack() {
        return this.status.get("attack");
    }

    public PlayerInventoryView getInventoryView() {
        return inventoryView;
    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }


    @Override
    public void onAdded() {
        transformComponent = entity.getComponent(TransformComponent.class);
        currentHealth = maxHealth;
        healthBar.setMaxValue(getMaxHealth());
        healthBar.setCurrentValue(getCurrentHealth());
        healthBar.setLabelVisible(false);
        healthBar.setTranslateX(50);
        healthBar.setTranslateY(50);
        healthBar.setBackgroundFill(Color.RED);
        healthBar.setFill(Color.GREEN);
        healthBar.setWidth(getMaxHealth());
        healthBar.setHeight(30);

        animWalkRight = new AnimationChannel(FXGL.image("Player/RightWalk.png"), Duration.millis(400) , 4);
        animWalkDown = new AnimationChannel(FXGL.image("Player/DownWalk.png"), Duration.millis(400) , 4);
        animWalkLeft = new AnimationChannel(FXGL.image("Player/LeftWalk.png"), Duration.millis(400) , 4);
        animWalkUp = new AnimationChannel(FXGL.image("Player/UpWalk.png"), Duration.millis(400) , 4);


        entity.getViewComponent().addChild(texture.loop());


        status.put("attack", 30);
        status.put("defense", 10);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        healthBar.setCurrentValue(getCurrentHealth());
        healthBar.setMaxValue(getMaxHealth());
        healthBar.setWidth(getMaxHealth());
        coinCounter.setText(String.valueOf(this.cointCount));
        if(getCurrentHealth() > getMaxHealth()){
            setCurrentHealth(getMaxHealth());
        }

        switch (currentDirection) {
            case "right" -> {
                if (isAnimationPlaying) {
                    texture.loopNoOverride(animWalkRight);
                } else {
                    texture.loop();
                    isAnimationPlaying = true;
                }
            }
            case "left" -> {
                if (isAnimationPlaying) {
                    texture.loopNoOverride(animWalkLeft);
                } else {
                    texture.loop();
                    isAnimationPlaying = true;
                }
            }
            case "up" -> {
                if (isAnimationPlaying) {
                    texture.loopNoOverride(animWalkUp);
                } else {
                    texture.loop();
                    isAnimationPlaying = true;
                }
            }
            case "down" -> {
                if (isAnimationPlaying) {
                    texture.loopNoOverride(animWalkDown);
                } else {
                    texture.loop();
                    isAnimationPlaying = true;
                }
            }
            default -> {
                texture.stop();
                isAnimationPlaying = false;
            }
        }


    }

    public void setMaxHealth(int health) {
        this.maxHealth = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setCurrentHealth(int health) {
        this.currentHealth = health;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }


    public void moveRight() {
        getEntity().translateX(SPEED); // Adjust velocity as needed
    }


    public void moveLeft() {
        getEntity().translateX(-SPEED); // Adjust velocity as needed
    }

    public void moveUp() {
        getEntity().translateY(-SPEED); // Adjust velocity as needed
    }

    public void moveDown() {
        getEntity().translateY(SPEED); // Adjust velocity as needed
    }


    public void yeet(Point2D coords){
        getEntity().setPosition(coords);
    }

    public void changeHealth(int health){
        setCurrentHealth(getCurrentHealth()+health);
    }


    public void setCurrentDirection(String right) {
        this.currentDirection = right;
    }
    public String getCurrentDirection() {
        return currentDirection;
    }

    public void updateStats(Map<String,Integer> statusChanges) {
        for(Map.Entry<String,Integer> entry : statusChanges.entrySet()){
            status.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
        System.out.println(status);
    }

    public int getTickDamage() {
        return tickDamage;
    }
    public void setTickDamage(int tickDamage) {
        this.tickDamage = tickDamage;
    }
    public void addTickDamage(int i) {
        this.tickDamage = this.tickDamage + i;
    }


    public Text getCoinCount() {

        return this.coinCounter;
    }


    public void addCoin(int i) {
        this.cointCount++;
    }

    public String getStatsText() {
        StringBuilder statsText = new StringBuilder();
        statsText.append("Player Stats :\n");
        statsText.append("health : ").append(getCurrentHealth()).append("\n");
        for(Map.Entry<String, Integer> entry : status.entrySet()){
            statsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return statsText.toString();
    }

    public int getDefense() {
        return status.get("defense");
    }

    public void onDeath() {
        getDialogService().showMessageBox("Oh no you died... Game over i guess.");
        getGameTimer().runOnceAfter( () -> FXGL.getGameController().gotoMainMenu(), Duration.millis(50));

    }

    public void win() {
        getDialogService().showMessageBox("You WON!!!! GG.");
        getGameTimer().runOnceAfter( () -> FXGL.getGameController().gotoMainMenu(), Duration.millis(50));

    }


}
