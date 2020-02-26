package GameEngine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsWindow {
    Stage window;

    public void display(GameMap map){
        // window properties
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game Settings");
        window.setMinWidth(300);
        window.setMinHeight(150);

        // number of fields options
        Label fieldsLabel = new Label();
        fieldsLabel.setText("Number of fields in a row");

        TextField fieldsInput = new TextField();
        fieldsInput.setPromptText("(int)");
        fieldsInput.setMaxWidth(80);

        Button submitButton = new Button("Change");
        submitButton.setOnAction(e -> this.changeNumberOfFields(map, fieldsInput));

        HBox box = new HBox(10);
        box.getChildren().addAll(fieldsLabel, fieldsInput, submitButton);
        box.setAlignment(Pos.CENTER);

        // boolean for displaying sounds in game
        Label soundLabel = new Label();
        soundLabel.setText("Playing sounds");

        CheckBox sound = new CheckBox();
        sound.setSelected(map.sounds);
        sound.setOnAction(e -> map.sounds = sound.isSelected());

        HBox box2 = new HBox(10);
        box2.getChildren().addAll(soundLabel, sound);
        box2.setAlignment(Pos.CENTER);

        // close button for window
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        // layout with options and close button
        VBox layout = new VBox(20);
        layout.getChildren().addAll(box, box2, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15,15,15,15));

        // setting scene and showing
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private void changeNumberOfFields(GameMap map, TextField input){
        if(!input.getText().equals("")){
            try{
                map.numberOfFields = Integer.parseInt(input.getText());
                map.numberOfBombs = (int)(map.percentage * map.numberOfFields * map.numberOfFields);
                input.clear();
                map.reload();
                window.close();
            }catch(NumberFormatException e){
                AlertWindow alert = new AlertWindow();
                alert.display("Error", "Typed value is not a number!");
            }
        }
    }
}
