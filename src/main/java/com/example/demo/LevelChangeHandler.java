package com.example.demo;

import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dev.Console;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;
import static com.example.demo.GameTypes.*;

public abstract class LevelChangeHandler {

    public static void setLevelTo(String levelName, Entity player, String spawnPointName){
        setLevelFromMap(levelName+".tmx");

        Entity spawnPoint = getCorrectSpawnPoint(spawnPointName);
        player.getTransformComponent().setPosition(spawnPoint.getX(), spawnPoint.getY());

        Viewport viewport = FXGL.getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth()/2.0,getAppHeight()/2.0);
        viewport.setZoom(2.5);
        if (getLevelWidth() >= getAppWidth() && getLevelHeight() >= getAppHeight()) {
            System.out.println("binded");
            FXGL.getGameScene().getViewport().setBounds(0, 0, getLevelWidth(), getLevelHeight());

        }
        viewport.setLazy(true);



    }

    private static Entity getCorrectSpawnPoint(String name) {
        return FXGL.getGameWorld().getEntitiesByType(SPAWNPOINT).stream()
                .filter(e -> name.equals(e.getString("location")))
                .findFirst()
                .orElse(null);

    }

    private static int getLevelWidth(){
        System.out.println("width is " + getGameScene().getWidth());
        return (int) getGameScene().getWidth();
    }

    private static int getLevelHeight(){
        System.out.println("height is " + getGameScene().getHeight());
        return (int) getGameScene().getHeight();
    }
}
