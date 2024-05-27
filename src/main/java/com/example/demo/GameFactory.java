package com.example.demo;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.collection.Array;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.Texture;
import com.example.demo.components.*;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.example.demo.BasicGameApp.CELL_SIZE;
import static com.example.demo.GameTypes.*;

public class GameFactory implements EntityFactory {

    

    @Spawns("player") //This is ne main player
    public Entity spawnPlayer(SpawnData data) {
        return entityBuilder(data)
               .type(PLAYER)
               .zIndex(3)
               .at(250, 250)
               .bbox(new HitBox(new Point2D(0, 0), BoundingShape.box(16, 16)))
               .with(new PlayerComponent())
               .with(new PlayerInventoryComponent())
                .with(new IrremovableComponent())
               .collidable()
               .build();
    }

    @Spawns("coin") // A coin item that gives you +5 coin
    public Entity spawnCoin(SpawnData data) {
        AnimatedTexture tex = texture("Items/Coin2.png").toAnimatedTexture(4, Duration.millis(500));
        tex.loop();
        return  entityBuilder(data)
                .type(COIN)
                .at(200, 200)
                .viewWithBBox(tex)
                .collidable()
                .build();
    }

    @Spawns("wall") // Simple wall
    public Entity spawnWall(SpawnData data) {
        int width = data.get("width");
        int height = data.get("height");


        return entityBuilder(data)
                .type(WALL)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(width, height, Color.TRANSPARENT))
                .collidable()
                .zIndex(3)
                .build();
    }

    @Spawns("trigger") // that teleports you to a specific location
    public Entity spawnTrigger(SpawnData data) {
        int width = data.get("width");
        int height = data.get("height");

        return  entityBuilder(data)
                .type(WARPZONE)
                .at(data.getX(), data.getY()) // Set the position using the arguments
                .viewWithBBox(new Rectangle(width, height, Color.TRANSPARENT))
                .collidable()
                .build();
    }

    @Spawns("removable wall") //wall that can be removed by an item
    public Entity spawnRemovableWall(SpawnData data) {

        int width = data.get("width");
        int height = data.get("height");

        return  entityBuilder(data)
               .type(WALL)
               .at(data.getX(), data.getY())
               .viewWithBBox(new Rectangle(width, height, Color.BROWN))
               .collidable()
               .with(new RemovableObstacleComponent())
               .build();
    }

    @Spawns("door") // Open with collision if you have an item in your inventory
    public Entity spawnDoor(SpawnData data) {

        Texture doorTexture = texture("Door.png", 32, 16);
        Texture closedTexture = doorTexture.subTexture(new Rectangle2D(0, 0, 16, 16));
        Texture openTexture = doorTexture.subTexture(new Rectangle2D(16, 0, 16, 16));

        return  entityBuilder(data)
                .type(WALL)
                .at(data.getX(), data.getY())
                .view(closedTexture)
                .bbox(new HitBox(new Point2D(-1, -1), BoundingShape.box(17, 16)))
                .collidable()
                .with(new DoorComponent(openTexture))
                .build();
    }

    @Spawns("npc")
    public Entity spawnNPC(SpawnData data) {
        Texture texture = texture("NPCS/StandardNPC.png");
        NPCComponent ncpComponent;
        if(data.hasKey("onInteract")){
            ncpComponent = new NPCComponent(data.get("text"), data.get("onInteract"));
        }
        else{
            ncpComponent = new NPCComponent(data.get("text"));
        }


        return  entityBuilder(data)
               .type(NPC)
               .at(data.getX(), data.getY())
               .viewWithBBox(texture)
               .collidable()
               .with(ncpComponent)
               .build();
    }

    @Spawns("shop npc")
    public Entity spawnShopNPC(SpawnData data) {
        Texture texture = texture("NPCS/ShopNPC.png");
        ShopComponent shop = new ShopComponent();

        Entity shopguy = entityBuilder(data)
                .type(NPC)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .collidable()
                .with(shop)
                .build();

        Runnable onInteract = () -> shopguy.getComponent(ShopComponent.class).show();

        shopguy.addComponent(new NPCComponent("Hey! Welcome to my shop!", onInteract));

        return shopguy;
    }
    @Spawns("quest npc")
    public Entity spawnQuestNPC(SpawnData data) {
        Entity player = getGameWorld().getSingleton(PLAYER);
        Texture texture = texture("NPCS/QuestNPC.png");

        Entity npcQuestGuy =entityBuilder(data)
                .type(NPC)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with()
                .collidable()
                .build();


        Runnable onInteract = () -> {
            if(player.getComponent(PlayerInventoryComponent.class).hasItem("health potion")){
                npcQuestGuy.removeFromWorld();
                player.getComponent(PlayerInventoryComponent.class).remove("health potion");
                player.getComponent(PlayerInventoryComponent.class).add("key");
            }

        };
        NPCComponent questGuyCompoenent = new NPCComponent("Si tu as une potion de vie dans ton Inventaire je te donnerai une cle.", onInteract);

        npcQuestGuy.addComponent(questGuyCompoenent);

        return npcQuestGuy;

    }

    @Spawns("rick")
    public Entity spawnRick(SpawnData data) {
        Texture texture = texture("NPCS/StandardNPC.png");

        Runnable onInteract = () -> {
            getAudioPlayer().pauseAllMusic();
            getAudioPlayer().playMusic(FXGL.getAssetLoader().loadMusic("Inconspicuous.mp3"));
            Duration duration = Duration.seconds(19);
            FXGL.getGameTimer().runOnceAfter(() -> {
                getAudioPlayer().stopAllMusic();
                Music backgroundMusic = FXGL.getAssetLoader().loadMusic("5 - Peaceful.mp3");
                getAudioPlayer().loopMusic(backgroundMusic);
            }, duration);
        };
        NPCComponent rickNPC = new NPCComponent("You thought i was a random NPC.... BUT IT WAS ME, RICK!!!", onInteract);

        return entityBuilder(data)
                .type(NPC)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .collidable()
                .with(rickNPC)
                .build();
    }



    @Spawns("health potion")
    public Entity spawnHealthPotion(SpawnData data) {
        Runnable onUse = new Runnable() {
            final Entity player = getGameWorld().getSingleton(PLAYER);

            @Override
            public void run(){
                if(player.getComponent(PlayerComponent.class).getCurrentHealth() <= player.getComponent(PlayerComponent.class).getMaxHealth()-20) {
                    player.getComponent(PlayerComponent.class).setCurrentHealth(player.getComponent(PlayerComponent.class).getCurrentHealth() + 20);
                }
                else{
                    player.getComponent(PlayerComponent.class).setCurrentHealth(player.getComponent(PlayerComponent.class).getMaxHealth());
                }
            }
        };

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "health potion");
        Texture texture = texture("""
                       Items/health potion.png""");

        return  entityBuilder(data)
               .type(USABLE_ITEM)
               .at(data.getX(), data.getY())
               .viewWithBBox(texture)
                .with(new UsableItemComponent(onUse, itemData))
                .with(new CombatItemComponent(onUse, itemData))
                .collidable()
               .build();
    }

    @Spawns("sword")
    public Entity spawnSword(SpawnData data) {

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "sword");
        Map<String, Integer> statusChange = new HashMap<>();
        statusChange.put("attack", 20);
        itemData.put("changeStatus", statusChange);
        Texture texture = texture("""
                       Items/sword.png""");

        Runnable onUse = () -> {
            // Get the player entity
            Entity player = getGameWorld().getSingleton(PLAYER);
            // Get the player component
            PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
            // Get the changeStatus data from the item
            Map<String, Integer> changeStatus = (Map<String, Integer>)itemData.get("changeStatus");
            // Update the player's stats
            playerComponent.updateStats(changeStatus);
        };
        Runnable onDrop = () -> {
            // Get the player entity
            Entity player = getGameWorld().getSingleton(PLAYER);
            // Get the player component
            PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

            Map<String, Integer> statusChangeWhenDroped = new HashMap<>();
            statusChangeWhenDroped.put("attack", -5);

            playerComponent.updateStats(statusChangeWhenDroped);
        };

        return  entityBuilder(data)
                .type(EQUIPPED_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new EquipedItemComponent(itemData, onUse, onDrop))
                .collidable()
                .build();
    }

    @Spawns("life potion")
    public Entity spawnLifePotion(SpawnData data) {
        Runnable onUse = new Runnable() {
            final Entity player = getGameWorld().getSingleton(PLAYER);

            @Override
            public void run(){
                player.getComponent(PlayerComponent.class).setMaxHealth((player.getComponent(PlayerComponent.class).getMaxHealth() + 10));
            }
        };

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "life potion");
        Texture texture = texture("""
                       Items/life potion.png""");

        return  entityBuilder(data)
                .type(USABLE_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new UsableItemComponent(onUse, itemData))
                .with(new CombatItemComponent(onUse, itemData))
                .collidable()
                .build();
    }

    @Spawns("key")
    public Entity spawnKey(SpawnData data) {

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "key");
        Texture texture = texture("""
                       Items/key.png""");

        Runnable onUse = () -> {

        };

        return  entityBuilder(data)
                .type(EQUIPPED_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new EquipedItemComponent(itemData, onUse))
                .collidable()
                .build();
    }

    @Spawns("enemy")
    public Entity spawnEnemy(SpawnData data) {
        Texture tex = texture("Enemy/Octopus.png");

        Map<String, Integer> inventory = new HashMap<>();
        Map<String, Integer> stats = new HashMap<>();
        stats.put("attack", 20);
        stats.put("defense", 5);


        return  entityBuilder(data)
               .type(ENEMY)
               .at(data.getX(), data.getY())
               .viewWithBBox(tex)
               .with(new EnemyComponent(inventory, stats, data.get("health")))
               .collidable()
               .build();
    }

    @Spawns("enemy2")
    public Entity spawnEnemy2(SpawnData data) {
        Texture tex = texture("Enemy/skull.png");

        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("sword", 1);
        inventory.put("key", 1);
        inventory.put("health potion", 1);
        Map<String, Integer> stats = new HashMap<>();
        stats.put("attack", 1);
        stats.put("defense", 100);


        return  entityBuilder(data)
                .type(ENEMY)
                .at(data.getX(), data.getY())
                .viewWithBBox(tex)
                .with(new EnemyComponent(inventory, stats, data.get("health")))
                .collidable()
                .build();

    }


    @Spawns("bomb")
    public Entity spawnBomb(SpawnData data) {

        Texture tex = texture("Items/bomb.png");
        tex.setScaleX(0.25);
        tex.setScaleY(0.25);
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "bomb");
        Entity bomb = entityBuilder(data)
                .type(USABLE_ITEM)
                .at(data.getX(), data.getY())
                .view(tex)
                .bbox(new HitBox(new Point2D(25, 20), BoundingShape.box(16, 16)))
                .with()
                .collidable()
                .build();


        Runnable onUse = () -> {

            final Entity player = getGameWorld().getSingleton(PLAYER);
            Entity floorbomb = spawn("bomb", player.getX()-25, player.getY()-20);
            floorbomb.getComponent(CollidableComponent.class).addIgnoredType(PLAYER);
            Rectangle2D areaSelection = new Rectangle2D(floorbomb.getX()+25, floorbomb.getY()+20, floorbomb.getWidth()*4, floorbomb.getHeight()*4);
            System.out.println(floorbomb.getX()+25);
            System.out.println(floorbomb.getY()+20);
            System.out.println(floorbomb.getWidth()*4);
            System.out.println(floorbomb.getHeight()*4);
            List<Entity> entityList = getGameWorld().getEntitiesInRange(areaSelection);

            FXGL.getGameTimer().runOnceAfter(() -> {
                // Reactivate the collide component after 2 seconds
                for(Entity entity : entityList){
                    if(entity.hasComponent(RemovableObstacleComponent.class)){
                        if(entity.hasComponent(CollidableComponent.class)){
                            entity.removeFromWorld();
                            entity.getViewComponent().setOpacity(0.5);
                        }
                    }
                }
                floorbomb.removeFromWorld();
            }, Duration.seconds(2));




        };

        Runnable onCombatUse = new Runnable(){

            @Override
            public void run() {
                List<Entity> enemy = getGameWorld().getEntitiesByComponent(InCombatComponent.class); //Singloton

                enemy.get(0).getComponent(EnemyComponent.class).addHealth(-1000);

            }
        };

        CombatItemComponent combatItemComponent = new CombatItemComponent(onCombatUse, itemData);

        UsableItemComponent usableItemComponent  = new UsableItemComponent(onUse, itemData);
        bomb.addComponent(usableItemComponent);
        bomb.addComponent(combatItemComponent);
        return bomb;
    }

    @Spawns("fire scroll")
    public Entity spawnFireScroll(SpawnData data) {
        Texture tex = texture("Items/fire scroll.png");
        tex.setScaleX(2);
        tex.setScaleY(2);
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "fire scroll");
        Runnable onUse = new Runnable() {
            final Entity player = getGameWorld().getSingleton(PLAYER);

            @Override
            public void run(){
                player.getComponent(PlayerComponent.class).addTickDamage(10);
            }
        };

        return entityBuilder(data)
               .type(COMBAT_ITEM)
               .at(data.getX(), data.getY())
               .viewWithBBox(tex)
               .with(new CombatItemComponent(onUse, itemData))
               .collidable()
               .build();
    }

    @Spawns("spawnpoint")
    public Entity spawnSpawnPoint(SpawnData data) {

        Entity spawnPoint = entityBuilder(data)
                .type(SPAWNPOINT)
                .at(data.getX(), data.getY())
                .build();
        spawnPoint.setProperty("Location", data.get("location"));
        return spawnPoint;
    }

    @Spawns("world trigger")
    public Entity spawnWorldTrigger(SpawnData data) {

        int width = data.get("width");
        int height = data.get("height");

        Entity worldTrigger = entityBuilder(data)
                .type(WORLD_TRIGGER)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(new Point2D(0, 0), BoundingShape.box(width, height)))
                .collidable()
                .build();
        worldTrigger.setProperty("to", data.get("to"));
        worldTrigger.setProperty("spawn name", data.get("spawn name"));
        return worldTrigger;
    }

    @Spawns("water")
    public Entity spawnWater(SpawnData data) {
        int width = data.get("width");
        int height = data.get("height");


        return  entityBuilder(data)
                .type(WALL)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Rectangle(width, height, Color.TRANSPARENT))
                .collidable()
                .with(new WaterComponent())
                .build();
    }

    @Spawns("404")
    public Entity spawn404(SpawnData data) {
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "404");
        Texture texture = texture("""
                       Items/Coin2.png""");

        Runnable onUse = () -> {

            PlayerComponent playerComponent = getGameWorld().getSingleton(PLAYER).getComponent(PlayerComponent.class);
            playerComponent.win();
        };

        return  entityBuilder(data)
                .type(USABLE_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new UsableItemComponent(onUse, itemData))
                .collidable()
                .build();

    }

    @Spawns("ender pearl")
    public Entity spawnEnderPearl(SpawnData data) {

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "ender pearl");
        Texture texture = texture("""
                       Items/key.png""");

        Runnable onUse = () -> {
            Entity player = getGameWorld().getSingleton(PLAYER);
            PlayerComponent playerComponent = getGameWorld().getSingleton(PLAYER).getComponent(PlayerComponent.class);
            List<Entity> spanPointList =getGameWorld().getEntitiesByType(SPAWNPOINT);

            Random random = new Random();
            Entity spawnPoint = spanPointList.get(random.nextInt(spanPointList.size()));

            int offsetX = FXGLMath.random(-50,50);
            int offsetY = FXGLMath.random(-50,50);



            playerComponent.yeet(new Point2D(spawnPoint.getX() + offsetX, spawnPoint.getY() + offsetY));
            FXGL.runOnce(() -> {
                // Action to be performed after 2 seconds
                player.getComponent(PlayerInventoryComponent.class).add("ender pearl");
            }, Duration.millis(20));


        };

        return  entityBuilder(data)
                .type(USABLE_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .with(new UsableItemComponent(onUse, itemData))
                .collidable()
                .build();

    }

    @Spawns("ds npc")
    public Entity SpawnDSNPC(SpawnData data) {
        Texture texture = texture("NPCS/StandardNPC.png");
        NPCComponent ncpComponent;

        Map<String,Integer> inventory = new HashMap<String,Integer>();
        if(((int)data.get("type") == 1)){
            inventory.put("ender pearl", 1);
            inventory.put("item 4", 3);
        }
        if((int)data.get("type") == 2){
            inventory.put("404", 1);
            inventory.put("item 2", 1);
        }

        ncpComponent = new DsNPCComponent(data.get("text"), inventory);


        return  entityBuilder(data)
                .type(DS_NPC)
                .at(data.getX(), data.getY())
                .viewWithBBox(texture)
                .collidable()
                .with(ncpComponent)
                .build();
    }

    @Spawns("item 4")
    public Entity spawnitem4(SpawnData data) {
        Texture tex = texture("Items/fire scroll.png");
        tex.setScaleX(2);
        tex.setScaleY(2);
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "item 4");
        Runnable onCombatUse = new Runnable(){

            @Override
            public void run() {

                Entity player = getGameWorld().getSingleton(PLAYER);
                List<Entity> enemy = getGameWorld().getEntitiesByComponent(InCombatComponent.class); //Singloton

                player.getComponent(PlayerComponent.class).changeHealth(enemy.get(0).getComponent(EnemyComponent.class).getCurrentHealth());
                enemy.get(0).getComponent(EnemyComponent.class).setHealth(0);

                FXGL.runOnce(() -> {
                    // Action to be performed after 2 seconds
                    player.getComponent(PlayerInventoryComponent.class).add("item 4");
                }, Duration.millis(40));


            }
        };

        return entityBuilder(data)
                .type(COMBAT_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(tex)
                .with(new CombatItemComponent(onCombatUse, itemData))
                .collidable()
                .build();
    }

    @Spawns("item 1")
    public Entity spawnitem1(SpawnData data) {
        Texture tex = texture("Items/life potion.png");
        tex.setScaleX(2);
        tex.setScaleY(2);
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "item 1");
        Runnable onUse = new Runnable(){

            @Override
            public void run() {
                List<Entity> enemy = getGameWorld().getEntitiesByComponent(MetNPCComponent.class); //Array
                Entity player = getGameWorld().getSingleton(PLAYER);
                SelectMetNPCView view = new SelectMetNPCView();
                view.show(player.getX(), player.getY(), 1);
                if(getGameWorld().getEntitiesByComponent(MetNPCComponent.class).isEmpty()){
                    FXGL.runOnce(() -> {
                        // Action to be performed after 2 seconds
                        player.getComponent(PlayerInventoryComponent.class).add("item 1");
                    }, Duration.millis(30));
                }
            }
        };

        return entityBuilder(data)
                .type(USABLE_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(tex)
                .with(new UsableItemComponent(onUse, itemData))
                .collidable()
                .build();
    }
    @Spawns("item 2")
    public Entity spawnitem2(SpawnData data) {
        Texture tex = texture("Items/health potion.png");
        tex.setScaleX(2);
        tex.setScaleY(2);
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", "item 2");
        Runnable onUse = new Runnable(){

            @Override
            public void run() {
                List<Entity> enemy = getGameWorld().getEntitiesByComponent(MetNPCComponent.class); //Array
                Entity player = getGameWorld().getSingleton(PLAYER);
                SelectMetNPCView view = new SelectMetNPCView();
                view.show(player.getX(), player.getY(), 2);
                if(getGameWorld().getEntitiesByComponent(MetNPCComponent.class).isEmpty()){
                    FXGL.runOnce(() -> {
                        // Action to be performed after 2 seconds
                        player.getComponent(PlayerInventoryComponent.class).add("item 2");
                    }, Duration.millis(30));
                }
            }
        };

        return entityBuilder(data)
                .type(USABLE_ITEM)
                .at(data.getX(), data.getY())
                .viewWithBBox(tex)
                .with(new UsableItemComponent(onUse, itemData))
                .collidable()
                .build();
    }


}
