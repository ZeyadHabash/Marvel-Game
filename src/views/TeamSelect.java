package views;

import javafx.event.ActionEvent;
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
import model.abilities.Ability;
import model.world.Champion;

import java.util.ArrayList;


public class TeamSelect implements EventHandler<ActionEvent> {

    ArrayList<Label> champions;
    Champion currentlySelectedChampion = null;
    Button firstPlayerConfirmPick;
    Button secondPlayerConfirmPick;
    Label firstPlayerTeam;
    Label secondPlayerTeam;

    public Scene scene(double width, double height, Game game) {
        BorderPane container = new BorderPane();

        GridPane championSelect = new GridPane();
        HBox championInfo = new HBox(); // HBox is prob not the best idea, maybe change to stackpane or smth idk
        VBox firstPlayerInfo = new VBox(40);
        VBox secondPlayerInfo = new VBox(40);

        // champion info layout
        Label hoveredChampionInfoLabel = new Label();
        Label selectedChampionInfoLabel = new Label();
        championInfo.getChildren().addAll(selectedChampionInfoLabel, hoveredChampionInfoLabel); // TODO figure out how to set their locations properly

        // first player layout
        Text firstPlayerName = new Text(game.getFirstPlayer().getName());
        firstPlayerTeam = new Label("Champions: \n");
        firstPlayerConfirmPick = new Button("Confirm Pick");
        firstPlayerConfirmPick.setOnAction(this);
        firstPlayerInfo.getChildren().addAll(firstPlayerName, firstPlayerTeam, firstPlayerConfirmPick);

        // second player layout
        Text secondPlayerName = new Text(game.getSecondPlayer().getName());
        secondPlayerTeam = new Label("Champions: \n");
        secondPlayerConfirmPick = new Button("Confirm Pick");
        secondPlayerConfirmPick.setOnAction(this);
        secondPlayerInfo.getChildren().addAll(secondPlayerName, secondPlayerTeam, secondPlayerConfirmPick);

        // champ select layout
        championSelect.setPadding(new Insets(50, 100, 0, 100));
        championSelect.setVgap(20);
        championSelect.setHgap(100);
        champions = new ArrayList<Label>();
        for (int i = 0, j = 0; i < Game.getAvailableChampions().size(); i++, j++) {
            champions.add(new Label(Game.getAvailableChampions().get(i).getName()));
            int finalI = i;
            Champion thisChamp = Game.getAvailableChampions().get(finalI);
            String champAbilities = "";
            for (Ability a : thisChamp.getAbilities()) {
                // TODO add the rest of the ability details
                champAbilities += ("\n" + a.getName());
            }
            String finalChampAbilities = champAbilities;
            champions.get(i).setOnMouseEntered(e -> hoveredChampionInfoLabel.setText(thisChamp.getName() +
                    "\n Max HP: " + thisChamp.getMaxHP() + "\n Mana: " + thisChamp.getMana() +
                    "\n Max Action Points Per Turn: " + thisChamp.getMaxActionPointsPerTurn() +
                    "\n Attack Damage: " + thisChamp.getAttackDamage() + "      Attack Range: " + thisChamp.getAttackRange()
                    + "\n Speed: " + thisChamp.getSpeed() + "\n Abilities: " + finalChampAbilities));
            champions.get(i).setOnMouseExited(e -> hoveredChampionInfoLabel.setText(""));
            champions.get(i).setOnMouseClicked(e -> {
                currentlySelectedChampion = thisChamp;
                String currentChampAbilities = "";
                for (Ability a : currentlySelectedChampion.getAbilities()) {
                    // TODO add the rest of the ability details
                    currentChampAbilities += ("\n" + a.getName());
                }
                selectedChampionInfoLabel.setText(currentlySelectedChampion.getName() +
                        "\n Max HP: " + currentlySelectedChampion.getMaxHP() + "\n Mana: " + currentlySelectedChampion.getMana() +
                        "\n Max Action Points Per Turn: " + currentlySelectedChampion.getMaxActionPointsPerTurn() +
                        "\n Attack Damage: " + currentlySelectedChampion.getAttackDamage() + "      Attack Range: " + currentlySelectedChampion.getAttackRange()
                        + "\n Speed: " + currentlySelectedChampion.getSpeed() + "\n Abilities: " + currentChampAbilities);
            });
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
    public void handle(ActionEvent event) {
            if(currentlySelectedChampion == null)
                AlertBox.display("Champion Select", "Please select a champion before confirming");
            else if (event.getSource() == firstPlayerConfirmPick)
                firstPlayerTeam.setText(firstPlayerTeam.getText() + currentlySelectedChampion.getName() + "\n");
            else if(event.getSource() == secondPlayerConfirmPick)
                secondPlayerTeam.setText(secondPlayerTeam.getText() + currentlySelectedChampion.getName() +"\n");
    }
}
