package GameEngine;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import java.util.HashSet;

public class GameMap extends Application{
    private Stage window;
    public int numberOfFields = 10;
    private int sizeOfButton = 53;
    private NumericMap numericMap;
    private MenuBar menu;
    private Pane pane;
    private Field[][] mapOfFields;
    public double percentage = 0.1;
    public int numberOfBombs = (int)(this.percentage * this.numberOfFields * this.numberOfFields);
    public int numberOfFoundBombs;
    public boolean sounds = true;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        this.menu = this.createMenu();
        this.createMap();

        VBox layout = new VBox();
        layout.getChildren().addAll(menu, pane);

        Scene scene = new Scene(layout);
        window.setTitle("Minesweeper");
        window.setScene(scene);
        window.show();
    }

    private MenuBar createMenu(){
        // creating bar
        MenuBar menu = new MenuBar();

        // creating bar menus
        Menu gameMenu = new Menu("Game");
        Menu levelMenu = new Menu("Level");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        // game options
        MenuItem newGame = new MenuItem("New");
        MenuItem closeGame = new MenuItem("Exit");
        newGame.setOnAction(e -> this.reload());
        closeGame.setOnAction(e -> window.close());
        gameMenu.getItems().addAll(newGame, closeGame);

        // difficulty level
        MenuItem easy = new MenuItem("Easy - 10% bombs");
        MenuItem medium = new MenuItem("Medium - 15% bombs");
        MenuItem hard = new MenuItem("Hard - 20% bombs");
        easy.setOnAction(e -> this.setLevel(0.1));
        medium.setOnAction(e -> this.setLevel(0.15));
        hard.setOnAction(e -> this.setLevel(0.2));
        levelMenu.getItems().addAll(easy, medium, hard);

        // edit panel with settings
        MenuItem settingsEdit = new MenuItem("Settings");
        settingsEdit.setOnAction(e -> new SettingsWindow().display(this));
        editMenu.getItems().addAll(settingsEdit);

        // help and about
        MenuItem about = new MenuItem("About");
        MenuItem help = new MenuItem("Help");
        about.setOnAction(e -> new AlertWindow().display("About", "Minesweeper written in JavaFX by wolski0420"));
        help.setOnAction(e -> new AlertWindow().display("Instructions",
                "You can play like in the old version - left mouse click reveals" +
                        "fields, right mouse click flags clicked position. If you flag all" +
                        "bombs = you win. If you click on bomb = you lose. Have fun!"));
        helpMenu.getItems().addAll(about, help);

        // inserting menus to bar
        menu.getMenus().addAll(gameMenu, levelMenu, editMenu, helpMenu);

        return menu;
    }

    private void setLevel(double percentage){
        this.percentage = percentage;
        this.numberOfBombs = (int)(this.percentage * this.numberOfFields * this.numberOfFields);
        this.reload();
    }

    private void createMap(){
        this.pane = new Pane();
        this.reload();
    }

    public void reload(){
        this.numericMap = new NumericMap(this.numberOfFields, this.numberOfFields, this.numberOfBombs);
        this.mapOfFields = new Field[this.numberOfFields][this.numberOfFields];
        this.numberOfFoundBombs = 0;

        pane.setPrefSize(this.numberOfFields * this.sizeOfButton, this.numberOfFields * this.sizeOfButton);
        pane.getChildren().clear();

        for(int x=0; x<this.numberOfFields; x++){
            for(int y=0; y<this.numberOfFields; y++){
                Field fieldToAdd = new Field(new Vector2D(x,y), this.sizeOfButton, numericMap, this);
                pane.getChildren().add(fieldToAdd);
                this.mapOfFields[fieldToAdd.positionOnMap.x][fieldToAdd.positionOnMap.y] = fieldToAdd;
            }
        }

        window.sizeToScene();
    }

    public void revealAllPossiblePositions(HashSet<Vector2D> setOfPositions){
        for(Vector2D position: setOfPositions){
            this.mapOfFields[position.x][position.y].reveal(this.numericMap.getValueFromPosition(position));
        }
    }

    public void gameLost(){
        for(Vector2D position: this.numericMap.mapOfBombs){
            this.mapOfFields[position.x][position.y].reveal(this.numericMap.getValueFromPosition(position));
        }

        if(this.sounds) {
            new AudioClip(GameMap.class.getResource("/Media/explosionSound.wav").toString()).play();
        }

        AlertWindow alert = new AlertWindow();
        alert.display("Game Over!", "Oh no, you lost this game...");
        this.reload();
    }

    public void gameWon(){
        for(Vector2D position: this.numericMap.mapOfBombs){
            this.mapOfFields[position.x][position.y].showBomb();
        }

        if(this.sounds) {
            new AudioClip(GameMap.class.getResource("/Media/winningSound.wav").toString()).play();
        }

        AlertWindow alert = new AlertWindow();
        alert.display("Victory!", "Yeah, you won this game!");
        this.reload();
    }

}
