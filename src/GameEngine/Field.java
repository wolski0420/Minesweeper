package GameEngine;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Field extends StackPane {
    public Vector2D positionOnMap;
    private Button btn;
    private int sizeOfButtons;
    private NumericMap numericMap;
    private GameMap map;
    private boolean isFlagged;
    private static Image flag = new Image("/Media/flag.png");
    private static Image mine = new Image("/Media/mine.png");
    private static Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.DARKBLUE,
            Color.DARKRED, Color.CYAN, Color.BLACK, Color.DARKGRAY };

    public Field(Vector2D position, int sizeBtn, NumericMap numericMap, GameMap map){
        this.positionOnMap = position;
        this.sizeOfButtons = sizeBtn;
        this.numericMap = numericMap;
        this.map = map;
        this.isFlagged = false;

        // settings for button
        this.btn = new Button();
        this.btn.setMinHeight(sizeBtn);
        this.btn.setMinWidth(sizeBtn);
        this.btn.setOnMouseClicked(e -> this.onClick(e));

        // adding button to stackpane
        getChildren().add(this.btn);

        // setting position for field
        setTranslateX(position.x * sizeBtn);
        setTranslateY(position.y * sizeBtn);
    }

    private void onClick(MouseEvent mouseEvent){
        if(map.sounds){
            new AudioClip(GameMap.class.getResource("/Media/clickSound.wav").toString()).play();
        }

        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            if(!this.isFlagged){
                int number = numericMap.getValueFromPosition(this.positionOnMap);
                if(number == -1){
                    map.gameLost();
                }
                else if(number == 0){
                    map.revealAllPossiblePositions(numericMap.findPositionsToReveal(this.positionOnMap));
                }
                else{
                    this.reveal(number);
                }
            }
        }
        else if(mouseEvent.getButton() == MouseButton.SECONDARY){
            if(!this.isFlagged){
                this.isFlagged = true;
                this.btn.setGraphic(new ImageView(Field.flag));

                if(this.numericMap.mapOfBombs.contains(this.positionOnMap)){
                    this.map.numberOfFoundBombs++;

                    if(this.map.numberOfFoundBombs == this.map.numberOfBombs){
                        this.map.gameWon();
                    }
                }
            }
            else{
                this.isFlagged = false;
                this.btn.setGraphic(null);

                if(this.numericMap.mapOfBombs.contains(this.positionOnMap)){
                    this.map.numberOfFoundBombs--;
                }
            }
        }
    }

    public void reveal(int value){
        this.btn.setDisable(true);
        if(value == -1) this.btn.setGraphic(new ImageView(Field.mine));
        if(value > 0) {
            this.btn.setText(value + "");
            this.btn.setTextFill(colors[value-1]);
            this.btn.setFont(Font.font("", FontWeight.BOLD,25));
        }
    }

    public void showBomb(){
        this.btn.setGraphic(new ImageView(Field.mine));
        this.btn.setDisable(false);
    }
}
