package views;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;

import engine.Player;
import engine.Game;


public class StartScreen extends Application {

    // TODO check the difference between Text and Label

    private Text enterP1Name;
    private TextField player1Name;
    private Text enterP2Name;
    private TextField player2Name;
    private Button confirm;
    private Game newGame;
    private double width;
    private double height;

    public static Stage mainWindow;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Game.loadAbilities("Abilities.csv");
            Game.loadChampions("Champions.csv");
            mainWindow = primaryStage;
            primaryStage.setTitle("marvel");
            primaryStage.setScene(scene(width = 800, height = 600));
            primaryStage.setMaximized(false); // use this to test maximized
            primaryStage.setFullScreen(false); // use to test fullscreen
            primaryStage.show();
        } catch (IOException e) {
            AlertBox.display(e.getLocalizedMessage(), "Please make sure the file is in the game directory and try running the game again");
        }
    }

    public Scene scene(double width, double height) {

        enterP1Name = new Text("Enter Player 1 name");
        player1Name = new TextField("Player 1");
        enterP2Name = new Text("Enter Player 2 name");
        player2Name = new TextField("Player 2");
        confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            //newGame = new Game(new Player(player1Name.getText()), new Player(player2Name.getText()));
            mainWindow.setScene(new TeamSelect().scene(width, height, new Player(player1Name.getText()), new Player(player2Name.getText())));
        });

        VBox layout = new VBox(50);
        layout.getChildren().addAll(enterP1Name, player1Name, enterP2Name, player2Name, confirm);
        layout.setAlignment(Pos.TOP_CENTER);

        return new Scene(layout, width, height);
    }
}
