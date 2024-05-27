package com.example.demo.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
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
    private Map<String, Integer> inventory;
    private Map<String, Integer> stats;
    private int health = 100;
    private int tickDamage = 0;

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
                item.getComponent(CollidableComponent.class).addIgnoredType(PLAYER);
                pause.setOnFinished(event -> {
                    // Reactivate the collide component after 2 seconds
                    item.getComponent(CollidableComponent.class).removeIgnoredType(PLAYER);
                });
                pause.play();
            }
        }
        entity.removeFromWorld();
    }

    public void useItem(String itemName){
        if(inventory.containsKey(itemName)){
            Entity item = FXGL.spawn(itemName, 500, 500);
            if(item.hasComponent(CombatItemComponent.class)){
                item.getComponent(CombatItemComponent.class).onUse(item);
            }
            item.removeFromWorld();
        }
    }


    public int getCurrentHealth() {
        return health;
    }

    public int getAttack(){
        return stats.get("attack");
    }

    public void addHealth(int health){
        this.health += health;
    }

    public Map<String, Integer> getStats(){
        return stats;
    }

    public String getStatsText(){
        StringBuilder statsText = new StringBuilder();
        statsText.append("Enemy Stats :\n");
        statsText.append("health : ").append(getCurrentHealth()).append("\n");
        for(Map.Entry<String, Integer> entry : stats.entrySet()){
            statsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return statsText.toString();
    }

    public int getDefense() {
        return stats.get("defense");
    }

    public Entity getRandomItem() {
        int randomInt = FXGLMath.random(0, inventory.size()-1);
        int i = 0;
        for(Map.Entry<String, Integer> entry : inventory.entrySet()){
            if(i == randomInt){
                Entity item = FXGL.spawn(entry.getKey(), 500, 500);
                if(item.hasComponent(CombatItemComponent.class)){
                    return item;
                }
            }
            i++;
        }
        return null;
    }

    public int getTickDamage() {
        return tickDamage;
    }

    public void setTickDamage(int tickDamage) {
        this.tickDamage = tickDamage;
    }
}
