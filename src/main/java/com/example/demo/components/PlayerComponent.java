package com.example.demo.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point2D;

public class PlayerComponent extends Component {

    TransformComponent transformComponent;
    private final double SPEED = 5;
    private int maxHealth = 100;
    private int currentHealth = 0;



    @Override
    public void onAdded() {
        transformComponent = entity.getComponent(TransformComponent.class);
        currentHealth = maxHealth;
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
