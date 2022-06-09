package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String title, String text){
        Stage alert = new Stage();

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(title);
        alert.setMinWidth(400);
        alert.setMinHeight(200);

        Label label = new Label(text);
        Button button = new Button("Ok");
        button.setOnAction(e -> alert.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        alert.setScene(scene);
        alert.showAndWait();
    }
    public static void displayAndReturn(String title, String text){
        Stage alert = new Stage();

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(title);
        alert.setMinWidth(600);
        alert.setMinHeight(400);

        Label label = new Label(text);
        Button returnToStart = new Button("Play Again");
        Button quit = new Button("Exit Game");
        returnToStart.setOnAction(e -> {
            alert.close();
            StartScreen.mainWindow.setScene(new StartScreen().scene());
        });
        quit.setOnAction(e -> {
           alert.close();
           StartScreen.mainWindow.close();
        });

        VBox layout = new VBox(10);
        HBox buttons = new HBox(100);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        buttons.getChildren().addAll(returnToStart, quit);
        layout.getChildren().addAll(label,buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        alert.setScene(scene);
        alert.showAndWait();
    }
}
