package views;


import java.io.IOException;


import engine.Game;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;


public class quiz extends Application {
    Champion c1;
    Champion c2;
    Champion c3;


    String champ1;
    String champ2;
    String champ3;


    public static Rectangle2D sceneSize = Screen.getPrimary().getVisualBounds();


    private static final int HEIGHT = (int) sceneSize.getHeight() - 50;
    private static final int WIDTH = (int) sceneSize.getWidth();


    public void start(Stage primaryStage) {
        try {
            Game.loadAbilities("Abilities.csv"); // loads abilities and champions;
            Game.loadChampions("Champions.csv");
        } catch (IOException e) {
        }


        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);


        Button Champion1 = new Button("component1");
        Champion1.setPrefWidth(WIDTH);
        Champion1.setPrefHeight(HEIGHT / 3);


        Button Champion2 = new Button("component2");
        Champion2.setPrefWidth(WIDTH);
        Champion2.setPrefHeight(HEIGHT / 3);


        Button Champion3 = new Button("component3");
        Champion3.setPrefWidth(WIDTH);
        Champion3.setPrefHeight(HEIGHT / 3);


        int random = (int) (Math.random() * 15);
        c1 = Game.getAvailableChampions().get(random);
        champ1 = "";
        champ1 += c1.getName() + "\n" + c1.getCurrentHP();
        Champion1.setText(champ1);


        int random2 = (int) (Math.random() * 15);
        c2 = Game.getAvailableChampions().get(random2);
        champ2 = "";
        champ2 += c2.getName() + "\n" + c2.getCurrentHP();
        Champion2.setText(champ2);


        int random3 = (int) (Math.random() * 15);
        c3 = Game.getAvailableChampions().get(random3);
        champ3 = "";
        champ3 += c3.getName() + "\n" + c3.getCurrentHP();
        Champion3.setText(champ3);


        root.getChildren().addAll(Champion1, Champion2, Champion3);


        Champion1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                c1.setCurrentHP(c1.getCurrentHP() - 500);
                Champion1.setText(c1.getName() + "\n" + c1.getCurrentHP());
            }


        });


        Champion2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {


                c2.setCurrentHP(c2.getCurrentHP() - 500);
                Champion2.setText(c2.getName() + "\n" + c2.getCurrentHP());
            }


        });


        Champion3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {


                c3.setCurrentHP(c3.getCurrentHP() - 500);
                Champion3.setText(c3.getName() + "\n" + c3.getCurrentHP());
            }


        });


        Scene mainScene = new Scene(root, 1080, 720); // sets the scene
        primaryStage.setTitle("Quiz");
        primaryStage.setScene(mainScene);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}