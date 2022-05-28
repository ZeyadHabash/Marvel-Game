package views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import engine.Game;
import model.world.Champion;

import java.util.ArrayList;


public class TeamSelect implements EventHandler {

    ArrayList<Button> champions;

    public Scene scene(double width, double height, Game game) {
        BorderPane container = new BorderPane();

        GridPane championSelect = new GridPane();
        HBox championInfo = new HBox(); // HBox is prob not the best idea, maybe change to stackpane or smth idk
        VBox firstPlayerInfo = new VBox(40);
        VBox secondPlayerInfo = new VBox(40);


        // first player layout
        Text firstPlayerName = new Text(game.getFirstPlayer().getName());
        Label firstPlayerTeam = new Label("Champions: ");
        Button firstPlayerConfirmPick = new Button("Confirm Pick");
        firstPlayerInfo.getChildren().addAll(firstPlayerName, firstPlayerTeam, firstPlayerConfirmPick);

        // second player layout
        Text secondPlayerName = new Text(game.getSecondPlayer().getName());
        Label secondPlayerTeam = new Label("Champions: ");
        Button secondPlayerConfirmPick = new Button("Confirm Pick");
        secondPlayerInfo.getChildren().addAll(secondPlayerName, secondPlayerTeam, secondPlayerConfirmPick);

        // champ select layout
        championSelect.setPadding(new Insets(50, 100, 0, 100));
        championSelect.setVgap(20);
        championSelect.setHgap(100);
        champions = new ArrayList<Button>();
        for (int i = 0, j = 0; i < Game.getAvailableChampions().size(); i++, j++) {
            champions.add(new Button(Game.getAvailableChampions().get(i).getName()));
            int finalI = i;
            champions.get(i).setOnAction(e -> championInfo.getChildren().add(new Label(Game.getAvailableChampions().get(finalI).getName())));
            if (i % 5 == 0)
                j = 0;
            GridPane.setConstraints(champions.get(i), i / 5, j);
            championSelect.getChildren().add(champions.get(i));
        }

        container.setLeft(firstPlayerInfo);
        container.setRight(secondPlayerInfo);
        container.setCenter(championSelect);
        container.setBottom(championInfo);

        return new Scene(container, width, height);
    }

    @Override
    public void handle(Event event) {
        /*
        if(event.getEventType() == MouseEvent.DRAG_DETECTED && championLabels.contains(event.getSource())){
            event.getSource()
        }
         */
    }
}
