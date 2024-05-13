package com.example.demo.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.ui.ProgressBar;
import com.example.demo.PlayerInventoryView;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

public class PlayerComponent extends Component {

    TransformComponent transformComponent;
    private final double SPEED = 3;
    private int maxHealth = 100;
    private int currentHealth = 0;
    private final PlayerInventoryView inventoryView = new PlayerInventoryView(getEntity());
    private final ProgressBar healthBar = new ProgressBar(false);
    private AnimationChannel animWalkRight, animWalkLeft, animWalkUp, animWalkDown;

    private AnimatedTexture texture = texture("Player/DownWalk.png").toAnimatedTexture(4, Duration.seconds(2));
    private String currentDirection = "idleDown";

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


        texture.setScaleX(2);
        texture.setScaleY(2);

        entity.getViewComponent().addChild(texture.loop());

    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        healthBar.setCurrentValue(getCurrentHealth());
        healthBar.setMaxValue(getMaxHealth());
        healthBar.setWidth(getMaxHealth());

        if(currentDirection.equals("right")){
            texture.loopNoOverride(animWalkRight);
        }
        else if(currentDirection.equals("left")){
            texture.loopNoOverride(animWalkLeft);
        }
        else if(currentDirection.equals("up")){
            texture.loopNoOverride(animWalkUp);
        }
        else if(currentDirection.equals("down")){
            texture.loopNoOverride(animWalkDown);
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


    public void setCurrentDirection(String right) {
        this.currentDirection = right;
    }
    public String getCurrentDirection() {
        return currentDirection;
    }

    public void stopAnim() {
        texture.stop();
    }
}
