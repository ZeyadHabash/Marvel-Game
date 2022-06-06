package views;

import engine.Player;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import engine.Game;
import model.abilities.Ability;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;
import model.world.Hero;
import model.world.Villain;

import java.util.ArrayList;


public class TeamSelect implements EventHandler<ActionEvent> {

    private ArrayList<Label> champions;
    private ArrayList<Champion> selectedChampions = new ArrayList<Champion>();
    private Champion currentlySelectedChampion = null;
    private Button firstPlayerConfirmPick;
    private Button secondPlayerConfirmPick;
    private Label firstPlayerTeam;
    private Label secondPlayerTeam;
    private double width;
    private double height;
    private VBox firstPlayerInfo;
    private VBox secondPlayerInfo;
    private VBox championInfo;
    private HBox currentlySelecting;
    private GridPane championSelect;
    private boolean leaderSelectPhase = false;
    private Label currentlyPickingText;
    private Text firstPlayerName;
    private Text secondPlayerName;
    private Label currentPlayer;
    private ArrayList<Label> abilityInfo;

    private Player player1;
    private Player player2;

    public Scene scene(double width, double height, Player player1, Player player2) {
        this.width = width;
        this.height = height;
        this.player1 = player1;
        this.player2 = player2;
        abilityInfo = new ArrayList<Label>();
        for (int i = 0; i < 3; i++)
            abilityInfo.add(new Label());
        BorderPane container = new BorderPane();

        championSelect = new GridPane();
        championInfo = new VBox(); // HBox is prob not the best idea, maybe change to stackpane or smth idk
        firstPlayerInfo = new VBox(40);
        secondPlayerInfo = new VBox(40);
        currentlySelecting = new HBox(50);

        // champion info layout
        //Label hoveredChampionInfoLabel = new Label();
        Label selectedChampionInfoLabel = new Label();

        // first player layout
        firstPlayerName = new Text(player1.getName());
        firstPlayerTeam = new Label("Champions: \n");
        firstPlayerConfirmPick = new Button("Confirm Pick");
        firstPlayerConfirmPick.setDisable(false);
        firstPlayerConfirmPick.setOnAction(this);
        firstPlayerInfo.getChildren().addAll(firstPlayerName, firstPlayerTeam, firstPlayerConfirmPick);
        firstPlayerConfirmPick.setAlignment(Pos.BOTTOM_CENTER);

        // second player layout
        secondPlayerName = new Text(player2.getName());
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
            Tooltip.install(champions.get(i), new Tooltip(thisChamp.getName() +
                    "\nType: " + thisChampType +
                    "\nMax HP: " + thisChamp.getMaxHP() + "\nMana: " + thisChamp.getMana() +
                    "\nMax Action Points Per Turn: " + thisChamp.getMaxActionPointsPerTurn() +
                    "\nAttack Damage: " + thisChamp.getAttackDamage() + "\nAttack Range: " + thisChamp.getAttackRange()
                    + "\nSpeed: " + thisChamp.getSpeed() + "\nAbilities: " + finalChampAbilities));
            champions.get(i).setOnMouseClicked(e -> {
                if (!selectedChampions.contains(thisChamp) || leaderSelectPhase) {
                    currentlySelectedChampion = thisChamp;
                    for (Ability a : currentlySelectedChampion.getAbilities()) {
                        String extraInfo = "";
                        String type;
                        if (a instanceof DamagingAbility) {
                            type = "\nType: Damaging";
                            extraInfo += "\nDamage Amount: " + ((DamagingAbility) a).getDamageAmount();
                        } else if (a instanceof HealingAbility) {
                            type = "\nType: Healing";
                            extraInfo += "\nHeal Amount: " + ((HealingAbility) a).getHealAmount();
                        } else {
                            type = "\nType: Crowd Control";
                            extraInfo += "\nEffect: " + ((CrowdControlAbility) a).getEffect().getName() + "   Duration: " + ((CrowdControlAbility) a).getEffect().getDuration();
                        }
                        int currIndex = currentlySelectedChampion.getAbilities().indexOf(a);
                        abilityInfo.get(currIndex).setText(a.getName());
                        abilityInfo.get(currIndex).setTooltip(new Tooltip(a.getName() + type + "\nMana Cost: " + a.getManaCost() +
                                "\nAP Required: " + a.getRequiredActionPoints() + "\nBase Cooldown: " + a.getBaseCooldown() +
                                "\nArea of Effect: " + a.getCastArea() + "\nRange: " + a.getCastRange() + extraInfo));
                    }
                    String currChampType;
                    if (thisChamp instanceof Hero)
                        currChampType = "Hero";
                    else if (thisChamp instanceof Villain)
                        currChampType = "Villain";
                    else
                        currChampType = "AntiHero";
                    selectedChampionInfoLabel.setText(currentlySelectedChampion.getName() + "\nType: " + currChampType +
                            "\nMax HP: " + currentlySelectedChampion.getMaxHP() + "\nMana: " + currentlySelectedChampion.getMana() +
                            "\nMax Action Points Per Turn: " + currentlySelectedChampion.getMaxActionPointsPerTurn() +
                            "\nAttack Damage: " + currentlySelectedChampion.getAttackDamage() + "      Attack Range: " + currentlySelectedChampion.getAttackRange()
                            + "\nSpeed: " + currentlySelectedChampion.getSpeed() + "\nAbilities: ");
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

        championInfo.getChildren().addAll(selectedChampionInfoLabel, abilityInfo.get(0), abilityInfo.get(1), abilityInfo.get(2));
        container.setBottom(championInfo);
        championInfo.setAlignment(Pos.CENTER); // TODO find a way to center

        container.setTop(currentlySelecting);
        currentlySelecting.setAlignment(Pos.TOP_CENTER);
        return new Scene(container, width, height);
    }

    @Override
    public void handle(ActionEvent event) {
        if (currentlySelectedChampion == null)
            AlertBox.display("Champion Select", "Please select a champion before confirming");
        else if (!leaderSelectPhase) {
            // TODO add the selected champion to the respective player's team arraylist
            if (event.getSource() == firstPlayerConfirmPick) {
                for (Label l : champions) {
                    if (l.getText().equals(currentlySelectedChampion.getName())) {
                        firstPlayerInfo.getChildren().add(l);
                        break;
                    }
                }
                player1.getTeam().add(currentlySelectedChampion);
                currentPlayer.setText(secondPlayerName.getText());
            } else if (event.getSource() == secondPlayerConfirmPick) {
                for (Label l : champions) {
                    if (l.getText().equals(currentlySelectedChampion.getName())) {
                        secondPlayerInfo.getChildren().add(l);
                        break;
                    }
                }
                player2.getTeam().add(currentlySelectedChampion);
                currentPlayer.setText(firstPlayerName.getText());
            }
            selectedChampions.add(currentlySelectedChampion);
            currentlySelectedChampion = null;
            firstPlayerConfirmPick.setDisable(!firstPlayerConfirmPick.isDisabled());
            secondPlayerConfirmPick.setDisable(!secondPlayerConfirmPick.isDisabled());
            if (selectedChampions.size() == 6) {
                leaderSelectPhase = true;
                currentlyPickingText.setText("Pick Your Leader: ");
            }
        } else {
            if (event.getSource() == firstPlayerConfirmPick) {
                if (player1.getTeam().contains(currentlySelectedChampion)) {
                    player1.setLeader(currentlySelectedChampion);
                    currentlySelectedChampion = null;
                    firstPlayerConfirmPick.setDisable(!firstPlayerConfirmPick.isDisabled());
                    secondPlayerConfirmPick.setDisable(!secondPlayerConfirmPick.isDisabled());
                    currentPlayer.setText(secondPlayerName.getText());
                } else
                    AlertBox.display("Champion not in your team", "Please select a champion from your team as a leader");
            } else if (event.getSource() == secondPlayerConfirmPick) {
                if (player2.getTeam().contains(currentlySelectedChampion)) {
                    player2.setLeader(currentlySelectedChampion);
                    currentlySelectedChampion = null;
                    firstPlayerConfirmPick.setDisable(!firstPlayerConfirmPick.isDisabled());
                    secondPlayerConfirmPick.setDisable(!secondPlayerConfirmPick.isDisabled());
                    Game newGame = new Game(player1, player2);
                    StartScreen.mainWindow.setScene(new GameBoard().scene(width, height, newGame));
                } else
                    AlertBox.display("Champion not in your team", "Please select a champion from your team as a leader");
            }
        }
    }
}
