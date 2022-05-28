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

import views.TeamSelect;
import engine.Player;
import engine.Game;

import static engine.Game.loadChampions;
import static engine.Game.loadAbilities;


public class StartScreen extends Application implements EventHandler<ActionEvent> {

    // stuff on screen
    Label enterP1Name;
    TextField player1Name;
    Label enterP2Name;
    TextField player2Name;
    Button confirm;
    Game newGame;
    double width;
    double height;

    public Stage mainWindow;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            loadChampions("Champions.csv");
            loadAbilities("Abilities.csv");
            mainWindow = primaryStage;
            primaryStage.setTitle("marvel");
            primaryStage.setScene(scene(width = 900, height = 600));
            primaryStage.show();
        } catch (IOException e) {
            AlertBox.display(e.getLocalizedMessage(), "Please make sure the file is in the game directory and try running the game again");
        }
    }

    public Scene scene(double width, double height) {

        enterP1Name = new Label("Enter Player 1 name");
        player1Name = new TextField();
        enterP2Name = new Label("Enter Player 2 name");
        player2Name = new TextField();
        confirm = new Button("Confirm");
        confirm.setOnAction(this);

        VBox layout = new VBox(50);
        layout.getChildren().addAll(enterP1Name, player1Name, enterP2Name, player2Name, confirm);
        layout.setAlignment(Pos.TOP_CENTER);

        return new Scene(layout, width, height);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == confirm) {
            System.out.println("starting game");
            newGame = new Game(new Player(player1Name.getText()), new Player(player2Name.getText()));
            mainWindow.setScene(TeamSelect.scene(width, height));
        }
    }
}
