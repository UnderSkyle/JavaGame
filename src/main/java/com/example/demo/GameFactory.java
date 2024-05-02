package com.example.demo;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.example.demo.BasicGameApp.CELL_SIZE;
import static com.example.demo.GameTypes.*;

public class GameFactory implements EntityFactory {

    @Spawns("player")
    public Entity spawnPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        BodyDef bd = new BodyDef();
        bd.setFixedRotation(true);
        bd.setType(BodyType.DYNAMIC);
        physics.setBodyDef(bd);


        return  entityBuilder(data)
                .type(PLAYER)
                .at(0,0)
                .viewWithBBox(new Rectangle(CELL_SIZE, CELL_SIZE, Color.BLUE))
                .with(physics)
                .collidable()
                .buildAndAttach();
    }

    @Spawns("coin")
    public Entity spawnCoin(SpawnData data) {
        return  entityBuilder(data)
                .type(COIN)
                .at(500, 200)
                .viewWithBBox(new Circle(20, 20, 15,Color.YELLOW))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    @Spawns("wall")
    public Entity spawnWall(SpawnData data) {
        return  entityBuilder(data)
                .type(WALL)
                .at(200, 200)
                .viewWithBBox(new Rectangle(20, 20, Color.BLACK))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }
}
