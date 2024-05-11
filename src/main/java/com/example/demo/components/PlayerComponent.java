package com.example.demo.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

public class PlayerComponent extends Component {

    PhysicsComponent physics;
    private boolean canMoveRight = true;
    private boolean canMoveLeft = true;
    private boolean canMoveUp = true;
    private boolean canMoveDown = true;
    private final int SPEED = 300;
    private int maxHealth = 100;
    private int currentHealth = 0;


    @Override
    public void onAdded() {
        physics = entity.getComponent(PhysicsComponent.class);

        if(physics == null) {
            System.out.println("Error: No physics component");
        }
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

    public void setCanMoveRight(boolean canMoveRight) {
        this.canMoveRight = canMoveRight;
    }

    public void setCanMoveLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
    }

    public void setCanMoveUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }

    public void setCanMoveDown(boolean canMoveDown) {
        this.canMoveDown = canMoveDown;
    }


    public void moveRight() {
        if (canMoveRight) {
            physics.setVelocityX(SPEED); // Adjust velocity as needed
        } else {
            stopX();
        }
    }

    public void moveLeft() {
        if (canMoveLeft) {
            physics.setVelocityX(-SPEED); // Adjust velocity as needed
        } else {
            stopX();
        }
    }

    public void moveUp() {
        if (canMoveUp) {
            physics.setVelocityY(-SPEED); // Adjust velocity as needed
        } else {
            stopY();
        }
    }

    public void moveDown() {
        if (canMoveDown) {
            physics.setVelocityY(SPEED); // Adjust velocity as needed
        } else {
            stopY();
        }
    }

    public void stopX() {
        physics.setVelocityX(0);
    }

    public void stopY() {
        physics.setVelocityY(0);
    }


}