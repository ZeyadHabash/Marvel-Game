package views;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import model.world.Hero;
import model.world.Villain;

import java.util.ArrayList;


public class TeamSelect implements EventHandler<ActionEvent> {

    ArrayList<Label> champions;
    ArrayList<Champion> selectedChampions = new ArrayList<Champion>();
    Champion currentlySelectedChampion = null;
    Button firstPlayerConfirmPick;
    Button secondPlayerConfirmPick;
    Label firstPlayerTeam;
    Label secondPlayerTeam;
    double width;
    double height;
    Game game;
    VBox firstPlayerInfo;
    VBox secondPlayerInfo;
    HBox championInfo;
    HBox currentlySelecting;
    GridPane championSelect;
    boolean leaderSelectPhase = false;
    Label currentlyPickingText;
    Text firstPlayerName;
    Text secondPlayerName;
    Label currentPlayer;

    public Scene scene(double width, double height, Game game) {
        this.width = width;
        this.height = height;
        this.game = game;
        BorderPane container = new BorderPane();

        championSelect = new GridPane();
        championInfo = new HBox(); // HBox is prob not the best idea, maybe change to stackpane or smth idk
        firstPlayerInfo = new VBox(40);
        secondPlayerInfo = new VBox(40);
        currentlySelecting = new HBox(50);

        // champion info layout
        Label hoveredChampionInfoLabel = new Label();
        Label selectedChampionInfoLabel = new Label();
        championInfo.getChildren().addAll(selectedChampionInfoLabel, hoveredChampionInfoLabel); // TODO figure out how to set their locations properly

        // first player layout
        firstPlayerName = new Text(game.getFirstPlayer().getName());
        firstPlayerTeam = new Label("Champions: \n");
        firstPlayerConfirmPick = new Button("Confirm Pick");
        firstPlayerConfirmPick.setDisable(false);
        firstPlayerConfirmPick.setOnAction(this);
        firstPlayerInfo.getChildren().addAll(firstPlayerName, firstPlayerTeam, firstPlayerConfirmPick);
        firstPlayerConfirmPick.setAlignment(Pos.BOTTOM_CENTER);

        // second player layout
        secondPlayerName = new Text(game.getSecondPlayer().getName());
        secondPlayerTeam = new Label("Champions: \n");
        secondPlayerConfirmPick = new Button("Confirm Pick");
        secondPlayerConfirmPick.setDisable(true);
        secondPlayerConfirmPick.setOnAction(this);
        secondPlayerInfo.getChildren().addAll(secondPlayerName, secondPlayerTeam, secondPlayerConfirmPick);
        secondPlayerConfirmPick.setAlignment(Pos.BOTTOM_CENTER);

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
            String thisChampType;
            if (thisChamp instanceof Hero)
                thisChampType = "Hero";
            else if (thisChamp instanceof Villain)
                thisChampType = "Villain";
            else
                thisChampType = "AntiHero";
            String finalChampAbilities = champAbilities;
            champions.get(i).setOnMouseEntered(e -> hoveredChampionInfoLabel.setText(thisChamp.getName() +
                    "\n Type: " + thisChampType +
                    "\n Max HP: " + thisChamp.getMaxHP() + "\n Mana: " + thisChamp.getMana() +
                    "\n Max Action Points Per Turn: " + thisChamp.getMaxActionPointsPerTurn() +
                    "\n Attack Damage: " + thisChamp.getAttackDamage() + "      Attack Range: " + thisChamp.getAttackRange()
                    + "\n Speed: " + thisChamp.getSpeed() + "\n Abilities: " + finalChampAbilities));
            champions.get(i).setOnMouseExited(e -> hoveredChampionInfoLabel.setText(""));
            champions.get(i).setOnMouseClicked(e -> {
                if (!selectedChampions.contains(thisChamp) || leaderSelectPhase) {
                    currentlySelectedChampion = thisChamp;
                    String currentChampAbilities = "";
                    for (Ability a : currentlySelectedChampion.getAbilities()) {
                        // TODO add the rest of the ability details
                        currentChampAbilities += ("\n" + a.getName());
                    }
                    String currChampType;
                    if (thisChamp instanceof Hero)
                        currChampType = "Hero";
                    else if (thisChamp instanceof Villain)
                        currChampType = "Villain";
                    else
                        currChampType = "AntiHero";
                    selectedChampionInfoLabel.setText(currentlySelectedChampion.getName() + "\n Type: " + currChampType +
                            "\n Max HP: " + currentlySelectedChampion.getMaxHP() + "\n Mana: " + currentlySelectedChampion.getMana() +
                            "\n Max Action Points Per Turn: " + currentlySelectedChampion.getMaxActionPointsPerTurn() +
                            "\n Attack Damage: " + currentlySelectedChampion.getAttackDamage() + "      Attack Range: " + currentlySelectedChampion.getAttackRange()
                            + "\n Speed: " + currentlySelectedChampion.getSpeed() + "\n Abilities: " + currentChampAbilities);
                }
            });
            // making 3 columns of 5 rows each
            if (i % 5 == 0)
                j = 0;
            GridPane.setConstraints(champions.get(i), i / 5, j);
            championSelect.getChildren().add(champions.get(i));
        }

        // currently picking "layout"
        currentlyPickingText = new Label("Currently Picking: ");
        currentPlayer = new Label(firstPlayerName.getText());
        currentlySelecting.getChildren().addAll(currentlyPickingText, currentPlayer);

        container.setLeft(firstPlayerInfo);
        container.setRight(secondPlayerInfo);
        container.setCenter(championSelect);
        championSelect.setAlignment(Pos.TOP_CENTER);
        container.setBottom(championInfo);
        container.setTop(currentlySelecting);
        currentlySelecting.setAlignment(Pos.TOP_CENTER);

        return new Scene(container, width, height);
    }

    @Override
    public void handle(ActionEvent event) {
        if (currentlySelectedChampion == null)
            AlertBox.display("Champion Select", "Please select a champion before confirming");
        else if(!leaderSelectPhase){
            // TODO add the selected champion to the respective player's team arraylist
            if (event.getSource() == firstPlayerConfirmPick) {
                // TODO replace this with a loop that finds the label from the champions arraylist and adds that to the VBOX of first player
                for (Label l : champions) {
                    if (l.getText().equals(currentlySelectedChampion.getName())) {
                        firstPlayerInfo.getChildren().add(l);
                        break;
                    }
                }
                game.getFirstPlayer().getTeam().add(currentlySelectedChampion);
                currentPlayer.setText(secondPlayerName.getText());
            } else if (event.getSource() == secondPlayerConfirmPick) {
                // TODO replace this with a loop that finds the label from the champions arraylist and adds that to the VBOX of second player
                for (Label l : champions) {
                    if (l.getText().equals(currentlySelectedChampion.getName())) {
                        secondPlayerInfo.getChildren().add(l);
                        break;
                    }
                }
                game.getSecondPlayer().getTeam().add(currentlySelectedChampion);
                currentPlayer.setText(firstPlayerName.getText());
            }
            selectedChampions.add(currentlySelectedChampion);
            currentlySelectedChampion = null;
            firstPlayerConfirmPick.setDisable(!firstPlayerConfirmPick.isDisabled());
            secondPlayerConfirmPick.setDisable(!secondPlayerConfirmPick.isDisabled());
            if (selectedChampions.size() == 6) {
                System.out.println("Game Starting");
                System.out.println("first Player: ");
                game.getFirstPlayer().getTeam().forEach(c -> System.out.println(c.getName()));
                System.out.println("second Player: ");
                game.getSecondPlayer().getTeam().forEach(c -> System.out.println(c.getName()));
                leaderSelectPhase = true;
                currentlyPickingText.setText("Pick Your Leader ");
                // TODO move to actual game Scene
            }
        }
        else{
            if (event.getSource() == firstPlayerConfirmPick){
                if(game.getFirstPlayer().getTeam().contains(currentlySelectedChampion)) {
                    game.getFirstPlayer().setLeader(currentlySelectedChampion);
                    currentlySelectedChampion = null;
                    firstPlayerConfirmPick.setDisable(!firstPlayerConfirmPick.isDisabled());
                    secondPlayerConfirmPick.setDisable(!secondPlayerConfirmPick.isDisabled());
                }else
                    AlertBox.display("Champion not in your team", "Please select a champion from your team as a leader");
            }else if(event.getSource() == secondPlayerConfirmPick){
                if(game.getSecondPlayer().getTeam().contains(currentlySelectedChampion)){
                    game.getSecondPlayer().setLeader(currentlySelectedChampion);
                    currentlySelectedChampion = null;
                    firstPlayerConfirmPick.setDisable(!firstPlayerConfirmPick.isDisabled());
                    secondPlayerConfirmPick.setDisable(!secondPlayerConfirmPick.isDisabled());
                }else
                    AlertBox.display("Champion not in your team", "Please select a champion from your team as a leader");
            }
        }
    }
}
