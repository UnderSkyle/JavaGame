package com.example.demo;

import com.almasb.fxgl.entity.Entity;
import com.example.demo.components.PlayerComponent;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.example.demo.BasicGameApp.CELL_SIZE;

public class PlayerInventoryView extends GridPane{
    private PlayerComponent playerComponent;
    private Rectangle selectedCell;


    public PlayerInventoryView(Entity player) {
        super();
        double gridSize = CELL_SIZE * 1.5;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                Rectangle cell = new Rectangle(gridSize, gridSize, Color.BLACK);
                cell.setOpacity(0.5);

                Text text = new Text("Item " + (row * 5 + col + 1));
                text.setFill(Color.WHITE);
                text.setFont(Font.font(10));
                int cellID = row * 5 + col + 1;
                cell.setId("Cell:"+cellID);
                text.setId("Text:" + cellID);

                addEventHandlers(cell, text);


                this.add(cell, col, row);
                this.add(text, col, row);

                selectCell(cell);

            }
        }
        this.setTranslateX(getAppWidth()- gridSize *5 - 20);
        this.setTranslateY(getAppHeight()- gridSize *4 - 20);
        this.setHgap(1.5);
        this.setVgap(3);


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
        return (Text) this.getChildren().get(getCellIndex(node) + 1);
    }


}