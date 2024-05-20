package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.example.demo.components.PlayerInventoryComponent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.example.demo.GameTypes.PLAYER;


public class ShopView {
    private static SubScene shopSubScene;
    private static Map<String, ItemDetails> inventory;



    public void show(double x, double y, Map<String, ItemDetails> inventory) {
        if (shopSubScene != null) {
            FXGL.getSceneService().pushSubScene(shopSubScene);
            return;
        }

        this.inventory = inventory;
        VBox shopLayout = new VBox(10);
        shopLayout.setStyle("-fx-padding: 20; -fx-background-color: #222; -fx-border-color: #fff;");

        Label title = new Label("Shop");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #fff;");
        shopLayout.getChildren().add(title);

        // Adding items from the inventory
        for (Map.Entry<String, ItemDetails> entry : inventory.entrySet()) {
            HBox itemBox = new HBox(10);
            itemBox.setStyle("-fx-padding: 5; -fx-background-color: #333;");

            String itemName = entry.getKey();
            ItemDetails details = entry.getValue();

            Label itemNameLabel = new Label(itemName);
            itemNameLabel.setStyle("-fx-text-fill: #fff;");

            Label itemQuantityLabel = new Label("Quantity: " + details.getQuantity());
            itemQuantityLabel.setStyle("-fx-text-fill: #fff;");

            Label itemPriceLabel = new Label("Price: " + details.getPrice());
            itemPriceLabel.setStyle("-fx-text-fill: #fff;");

            Button buyButton = new Button("Buy");
            buyButton.setOnAction(event -> buyItem(itemName, itemQuantityLabel));

            itemBox.getChildren().addAll(itemNameLabel, itemQuantityLabel, itemPriceLabel, buyButton);
            shopLayout.getChildren().add(itemBox);
        }

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> hide());
        shopLayout.getChildren().add(closeButton);

        Pane shopPane = new Pane(shopLayout);
        shopPane.setTranslateX(x);
        shopPane.setTranslateY(y);

        shopSubScene = new SubScene() {
            @Override
            public void onCreate() {
                getRoot().getChildren().add(shopPane);
            }
        };

        FXGL.getSceneService().pushSubScene(shopSubScene);
    }

    private static void buyItem(String itemName, Label itemQuantityLabel) {
        ItemDetails details = inventory.get(itemName);
        if (details != null && details.getQuantity() > 0) {
            double currentCoin = FXGL.geti("coin");
            if( currentCoin >= details.getPrice()){
                details.setQuantity(details.getQuantity() - 1);
                int newCoin = (int) (currentCoin- details.getPrice());
                FXGL.set("coin",newCoin);
                itemQuantityLabel.setText("Quantity: " + details.getQuantity());
                PlayerInventoryComponent playerComponent = getGameWorld().getSingleton(PLAYER).getComponent(PlayerInventoryComponent.class);
                playerComponent.add(itemName);
            }
            else{
                FXGL.getNotificationService().pushNotification("Not enough coin");
            }
        } else {
            FXGL.getNotificationService().pushNotification(itemName + " is out of stock");
        }

    }

    public static void hide() {
        if (shopSubScene != null && FXGL.getSceneService().getCurrentScene() == shopSubScene) {
            FXGL.getSceneService().popSubScene();
            shopSubScene = null;  // Reset the reference, so it can be recreated if needed
        }
    }

    public static class ItemDetails {
        private int quantity;
        private double price;

        public ItemDetails(int quantity, double price) {
            this.quantity = quantity;
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

}
