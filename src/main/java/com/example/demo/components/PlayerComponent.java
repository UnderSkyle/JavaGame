package com.example.demo.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.ui.ProgressBar;
import com.example.demo.PlayerInventoryView;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class PlayerComponent extends Component {

    TransformComponent transformComponent;
    private final double SPEED = 3;
    private int maxHealth = 100;
    private int currentHealth = 0;
    private PlayerInventoryView inventoryView = new PlayerInventoryView(getEntity());
    private ProgressBar healthBar = new ProgressBar(false);

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
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        healthBar.setCurrentValue(getCurrentHealth());
        healthBar.setMaxValue(getMaxHealth());
        healthBar.setWidth(getMaxHealth());

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


}
