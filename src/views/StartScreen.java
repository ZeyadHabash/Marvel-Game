package views;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import engine.Player;
import engine.Game;
import static engine.Game.loadChampions;
import static engine.Game.loadAbilities;


public class StartScreen extends Application implements EventHandler<ActionEvent> {

    // stuff on screen
    Text enterP1Name;
    TextField player1Name;
    Text enterP2Name;
    TextField player2Name;
    Button confirm;
    Game newGame;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("marvel");

        enterP1Name = new Text("Enter Player 1 name");
        player1Name = new TextField();
        enterP2Name = new Text("Enter Player 2 name");
        player2Name = new TextField();
        confirm = new Button("Confirm");
        confirm.setOnAction(this);

        VBox layout = new VBox(50);
        layout.getChildren().addAll(enterP1Name, player1Name, enterP2Name, player2Name, confirm);
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == confirm) {
            try {
                System.out.println("loading game");
                newGame = new Game(new Player(player1Name.getText()), new Player(player2Name.getText()));
                loadChampions("Champions.csv");
                loadAbilities("Abilities.csv");
            } catch (IOException e) {
                AlertBox.display(e.getLocalizedMessage(), "Please make sure the file is in the game directory and try again");
            }
        }
    }
}
