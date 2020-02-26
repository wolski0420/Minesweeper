package GameEngine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertWindow {
    public void display(String title, String message){
        // window properties
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);
        window.setMinHeight(150);

        // message in alert
        Label messageLabel = new Label();
        messageLabel.setText(message);

        // close button for window
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        // layout with message and close button
        VBox layout = new VBox(20);
        layout.getChildren().addAll(messageLabel, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,10,10,10));

        // setting scene and showing
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
