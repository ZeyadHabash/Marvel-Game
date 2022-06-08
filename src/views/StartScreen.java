package views;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;


import java.io.File;
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
    private Label marvel;

    public static Stage mainWindow;

    public static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

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

            primaryStage.setScene(scene());
            primaryStage.setMaximized(true); // use this to test maximized
            primaryStage.setFullScreen(false); // use to test fullscreen
            primaryStage.show();

        } catch (IOException e) {
            AlertBox.display(e.getLocalizedMessage(), "Please make sure the file is in the game directory and try running the game again");
        }
    }

    public Scene scene() {

        marvel = new Label("MARVEL AVENGERS ASSEMBLE: \nTHE CLASH OF THE UNIVERSES");
        marvel.setStyle("-fx-font: 36 arial;");
        marvel.setTextFill(Color.YELLOW);
        VBox title = new VBox();
        title.getChildren().add(marvel);
        title.setAlignment(Pos.TOP_CENTER);
        enterP1Name = new Text("Enter Player 1 name");
        enterP1Name.setStyle("-fx-font: 24 arial;");
        player1Name = new TextField("Player1");
        player1Name.setMaxSize(500,100);
        enterP2Name = new Text("Enter Player 2 name");
        enterP2Name.setStyle("-fx-font: 24 arial;");
        player2Name = new TextField("Player2");
        player2Name.setMaxSize(500,100);
        confirm = new Button("Confirm");
        confirm.setStyle("-fx-font: 16 arial");
        confirm.setOnAction(e -> {
            //newGame = new Game(new Player(player1Name.getText()), new Player(player2Name.getText()));
            mainWindow.setScene(new TeamSelect().scene( new Player(player1Name.getText()), new Player(player2Name.getText())));
        });

        VBox p1 = new VBox(25);
        p1.getChildren().addAll(enterP1Name, player1Name);
        VBox p2 = new VBox(25);
        p2.getChildren().addAll(enterP2Name, player2Name);
        HBox playerNames = new HBox(50);
        playerNames.getChildren().addAll(p1, p2);
        playerNames.setAlignment(Pos.CENTER);
        VBox layout = new VBox(50);
        layout.getChildren().addAll(title, playerNames, confirm);
        layout.setAlignment(Pos.CENTER);
        StackPane root = new StackPane();
        //root.setStyle("-fx-background: navy;");
        root.getChildren().add(layout);
        Image image = new Image(getClass().getResourceAsStream("start_screen.png"));


        BackgroundImage bImg = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);


        return new Scene(root, screenSize.getWidth(), screenSize.getHeight()-35);
    }
}
