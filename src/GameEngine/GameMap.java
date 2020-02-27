package GameEngine;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class GameMap extends Application{
    private Stage window;
    public int numberOfFields = 10;
    private int sizeOfButton = 53;
    private NumericMap numericMap;
    private MenuBar menu;
    private Pane pane;
    private Field[][] mapOfFields;
    private int timePassed;
    private static Timer timer = null;
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

        Label time = new Label();
        time.setMinHeight(40);
        time.setFont(Font.font("", FontWeight.BOLD,20));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO,
                e -> time.setText("Time passed: " + this.timePassed)),
                new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        HBox layoutForTimer = new HBox();
        layoutForTimer.setAlignment(Pos.CENTER);
        layoutForTimer.getChildren().add(time);

        VBox layout = new VBox();
        layout.getChildren().addAll(menu, pane, layoutForTimer);

        Scene scene = new Scene(layout);
        window.setOnCloseRequest(e -> {
            window.close();
            System.exit(0);
        });
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
        ToggleGroup levelToggle = new ToggleGroup();

        RadioMenuItem easy = new RadioMenuItem("Easy - 10% bombs");
        RadioMenuItem medium = new RadioMenuItem("Medium - 15% bombs");
        RadioMenuItem hard = new RadioMenuItem("Hard - 20% bombs");

        easy.setOnAction(e -> this.setLevel(0.1));
        medium.setOnAction(e -> this.setLevel(0.15));
        hard.setOnAction(e -> this.setLevel(0.2));

        easy.setToggleGroup(levelToggle);
        medium.setToggleGroup(levelToggle);
        hard.setToggleGroup(levelToggle);

        easy.setSelected(true);

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

        this.timePassed = 0;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timePassed++;
            }
        };

        if(this.timer != null) this.timer.cancel();
        this.timer = new Timer();
        this.timer.schedule(task,1000,1000);

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
        alert.display("Victory!", "Yeah, you won this game in " + this.timePassed + " seconds!");
        this.reload();
    }

}
