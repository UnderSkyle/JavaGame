package com.example.demo.components;

import com.almasb.fxgl.entity.component.Component;

public class PlayerComponent extends Component {

    private final double speed;

    public PlayerComponent(double i) {
        this.speed = i;
    }

    public void move(double dx, double dy) {
        entity.translate(dx * speed, dy * speed);
    }

    public void moveUp() {
        move(0, -1);
    }

    public void moveDown() {
        move(0, 1);
    }

    public void moveLeft() {
        move(-1, 0);
    }

    public void moveRight() {
        move(1, 0);
    }

}
