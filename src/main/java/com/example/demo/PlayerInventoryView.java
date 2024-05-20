package com.example.demo;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.example.demo.components.PlayerComponent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.example.demo.BasicGameApp.CELL_SIZE;

public class PlayerInventoryView extends GridPane{
    private PlayerComponent playerComponent;
    private Rectangle selectedCell;


    public PlayerInventoryView(Entity player) {
        super();
        double gridSize = CELL_SIZE * 2;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                Rectangle cell = new Rectangle(gridSize, gridSize, Color.BLACK);
                cell.setOpacity(0.5);

                TextureWithNumberNode text = new TextureWithNumberNode( texture("Items/Empty.png"), "" );
                text.getNumberText().setFill(Color.WHITE);
                int cellID = row * 5 + col + 1;
                cell.setId("Cell:"+cellID);
                text.setId("Text:" + cellID);

                addEventHandlers(cell, text.getNumberText());

                this.add(cell, col, row);
                this.add(text, col, row);

                selectCell(cell);

            }
        }
        this.setTranslateX(getAppWidth()- gridSize *5 - 30);
        this.setTranslateY(getAppHeight()- gridSize *4 - 20);
        this.setHgap(2);
        this.setVgap(4);


    }

    private void addEventHandlers(Rectangle cell, Text text) {
        cell.setOnMouseClicked(event -> {
            selectCell(cell);
        });

        text.setOnMouseClicked(event -> {
            selectCell(cell); // Select the corresponding cell when text is clicked
        });
    }

    private void selectCell(Rectangle cell) {
        if (selectedCell != null) {
            selectedCell.setFill(Color.BLACK); // Reset the previous selected cell color
            getCellText(selectedCell).setFill(Color.WHITE);
        }

        selectedCell = cell;
        selectedCell.setFill(Color.LIGHTGRAY); // Highlight the selected cell
        getCellText(selectedCell).setFill(Color.BLACK);

        String cellId = cell.getId();

    }

    // Method to select the cell to the right of the currently selected cell
    public void selectCellToRight() {
        if (selectedCell == null) {
            return; // No cell is currently selected
        }

        int currentCellIndex = getCellIndex(selectedCell);


        // Check if the next column is within bounds
        if ((currentCellIndex/2)%5 < 4) { // max element is 39 but max odd element is text
            int cellIndex = currentCellIndex + 2;
            if (cellIndex < this.getChildren().size()) {
                Node node = this.getChildren().get(cellIndex);
                if (node instanceof Rectangle nextCell) {
                    selectCell(nextCell);
                }
            }
        }
    }

    // Method to select the cell to the left of the currently selected cell
    public void selectCellToLeft() {
        if (selectedCell == null) {
            return; // No cell is currently selected
        }

        int currentCellIndex = getCellIndex(selectedCell);


        // Check if the next column is within bounds
        if ((currentCellIndex/2)%5 > 0) { // max element is 39 but max odd element is text
            int cellIndex = currentCellIndex - 2;
            if (cellIndex < this.getChildren().size()) {
                Node node = this.getChildren().get(cellIndex);
                if (node instanceof Rectangle nextCell) {
                    selectCell(nextCell);
                }
            }
        }

    }

    // Method to select the cell above the currently selected cell
    public void selectCellAbove() {
        if (selectedCell == null) {
            return; // No cell is currently selected
        }

        int currentCellIndex = getCellIndex(selectedCell);


        // Check if the next column is within bounds
        if (currentCellIndex >=10) { // max element is 39 but max odd element is text
            int cellIndex = currentCellIndex - 10;
            if (cellIndex < this.getChildren().size()) {
                Node node = this.getChildren().get(cellIndex);
                if (node instanceof Rectangle nextCell) {
                    selectCell(nextCell);
                }
            }
        }


    }

    // Method to select the cell below the currently selected cell
    public void selectCellBelow() {
        if (selectedCell == null) {
            return; // No cell is currently selected
        }

        int currentCellIndex = getCellIndex(selectedCell);


        // Check if the next column is within bounds
        if (currentCellIndex <=28) { // max element is 39 but max odd element is text
            int cellIndex = currentCellIndex + 10;
            if (cellIndex < this.getChildren().size()) {
                Node node = this.getChildren().get(cellIndex);
                if (node instanceof Rectangle nextCell) {
                    selectCell(nextCell);
                }
            }
        }


    }


    private int getCellIndex(Node node) {
        return (GridPane.getRowIndex(node) * 5 + GridPane.getColumnIndex(node))*2 ; // *2 because of the text
    }


    private Text getCellText(Node node) {
        Node goodNode = this.getChildren().get(getCellIndex(node) + 1);// +1 because of the text
        if (goodNode instanceof TextureWithNumberNode nextCell) {
            return nextCell.getNumberText();
        }
        return new Text("");
    }


    public void update(Map<String, Integer> inventory) {
        int cellIndex = 1;
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {

            Node cellText = this.getChildren().get(cellIndex);
            int quantity = entry.getValue();
            ((TextureWithNumberNode) cellText).getNumberText().setText(String.valueOf(quantity));

            String name = entry.getKey();
            Texture texture = texture("Items/" +name +".png");
            if(name == "bomb"){
                texture.setFitHeight(16);
                texture.setFitWidth(16);
            }



            ((TextureWithNumberNode) cellText).setNameOfItem(name);

            ((TextureWithNumberNode) cellText).setTexture(texture);
            cellIndex = cellIndex + 2; // +2 because of the text
        }
        for(int i = 1+ inventory.size()*2; i<=39; i=i+2){

            TextureWithNumberNode workingCell = (TextureWithNumberNode) this.getChildren().get(i);

            Texture texture = texture("Items/Empty.png");
            texture.setFitHeight(16);
            texture.setFitWidth(16);

            workingCell.setTexture(texture);
            workingCell.setNumberText("");
            workingCell.setNameOfItem("Empty");
        }
    }

    public String getNameFromSelectedNode() {

        if (this.getChildren().get(getCellIndex(selectedCell)+1) instanceof TextureWithNumberNode cell) {
            return cell.getNameOfItem();
        }
        else return "bad cell";
    }

    public class TextureWithNumberNode extends HBox {
        private Texture texture;
        private Text numberText;
        private String nameOfItem = "Empty";

        public TextureWithNumberNode(Texture texture, String numberText) {

            // Set spacing between the texture and the number
            setSpacing(2); // Adjust as needed

            // Add padding around the node
            setPadding(new Insets(2)); // Adjust as needed

            // Center the content horizontally and vertically
            setAlignment(Pos.CENTER);


            // Create an ImageView for the texture
            this.texture = texture;
            // Create a Text node for the number
            Text text = new Text(numberText);
            this.numberText= text;
            text.setFont(Font.font("Arial", 16));


            // Add the ImageView and Text to the StackPane
            getChildren().addAll(this.texture, text);
        }

        public Texture getTexture() {
            return texture;
        }
        public void setTexture(Texture texture) {
            this.texture = texture;
            getChildren().remove(0);
            getChildren().add(0, texture);
        }
        public Text getNumberText() {
            return numberText;
        }
        public void setNumberText(String numberText) {
            this.numberText.setText(numberText);
        }

        public void setNameOfItem(String name) {
            this.nameOfItem = name;
        }

        public String getNameOfItem(){
            return this.nameOfItem;
        }
    }

}