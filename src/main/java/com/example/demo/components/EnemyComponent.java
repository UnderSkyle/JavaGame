package com.example.demo.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;
import static com.example.demo.GameTypes.PLAYER;

public class EnemyComponent extends Component {
    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Integer> stats = new HashMap<>();
    private int health = 100;

    public EnemyComponent(Map<String, Integer> inventory, Map<String, Integer> stats, int health ) {
        this.inventory = inventory;
        this.stats = stats;
        this.health = health;
    }

    public void onDeath(){
        for(Map.Entry<String, Integer> entry : inventory.entrySet()){
            for(int i = 0; i < entry.getValue(); i++){
                int randomInt = FXGLMath.random(-10, 10) * i;
                SpawnData spanwPos = new SpawnData(entity.getX()+randomInt, entity.getY()+randomInt);
                Entity item = spawn(entry.getKey(),spanwPos);
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> {
                    // Reactivate the collide component after 2 seconds
                    item.getComponent(CollidableComponent.class).removeIgnoredType(PLAYER);
                });
                pause.play();
            }
        }

    }


    public int getCurrentHealth() {
        return health;
    }


    public Map<String, Integer> getStats(){
        return stats;
    }
}
