package com.example.demo;/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static java.lang.Math.abs;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BasicGameApp extends GameApplication {


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("Potato Game");
        settings.setVersion("0.2");
    }

    public enum EntityType {
        PLAYER, COIN, WALL
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.D, () -> {
            player.translateX(5); // move right 5 pixels
        });

        onKey(KeyCode.Q, () -> {
            player.translateX(-5); // move left 5 pixels
        });

        onKey(KeyCode.Z, () -> {
            player.translateY(-5); // move up 5 pixels
        });

        onKey(KeyCode.S, () -> {
            player.translateY(5); // move down 5 pixels
        });

        onKeyDown(KeyCode.F, () -> {
            System.out.println(player.getX() + " " + player.getY());
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("PositionX", 300);
        vars.put("PositionY", 300);
    }

    private Entity player;

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.BLACK);;
        entityBuilder()
                .viewWithBBox(new Rectangle(getAppWidth(), getAppHeight(), Color.BEIGE))
                .zIndex(-1)
                .buildAndAttach();

        player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 300)
                .viewWithBBox(new Rectangle(20, 20, Color.BLUE))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        player.setProperty("prevX", player.getX());
        player.setProperty("prevY", player.getY());


        entityBuilder()
                .type(EntityType.COIN)
                .at(500, 200)
                .viewWithBBox(new Circle(15, 15, 15, Color.YELLOW))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        initScreenBounds();

        Viewport viewport = FXGL.getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setBounds(0, 0, getAppWidth(), FXGL.getAppHeight());
        viewport.bindToEntity(player, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
        viewport.setLazy(true);

    }

    @Override
    protected void initPhysics() {
        // order of types on the right is the same as on the left
        onCollisionBegin(EntityType.PLAYER, EntityType.COIN, (player, coin) -> {
            coin.removeFromWorld();

        });

        onCollisionBegin(EntityType.PLAYER, EntityType.WALL, (player, boundary) -> {
            /*Point2D center = boundary.getCenter();
            double dx = center.getX() - player.getX();
            double dy = center.getY() - player.getY();
            System.out.println(dx);
            System.out.println(dy);
            if (dx > 395) {
                //set center.X to something else
                player.translateX(15);

            }
            else if ( dy > 395) {
                //set center.Y to something else
                player.translateY(15);
            }
            else if ( dx > -395 && dx < -370) {
                player.translateX(-15);
            }
            else if (dy > -395 && dy < -370) {
                player.translateY(-15);

            }*/
            player.setPosition(boundary.getCenter());

        });

    }
    private void initScreenBounds(){
        Entity walls = entityBuilder()
                .type(EntityType.WALL)
                .collidable()
                .buildScreenBounds(50);

        getGameWorld().addEntity(walls);
    }

    public static void main(String[] args) {
        launch(args);
    }
}