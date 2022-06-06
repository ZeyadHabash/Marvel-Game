package views;

import engine.Game;
import engine.GameListener;
import engine.Player;
import engine.PriorityQueue;
import exceptions.AbilityUseException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.abilities.Ability;
import model.abilities.AbilityListener;
import model.world.*;

/**
 * Not sure if I should implement {@link ChampionListener} and {@link AbilityListener} here, in {@link Game}
 * or somewhere completely different
 */
public class GameBoard implements GameListener {

    GridPane boardGrid;
    Game game;
    Label[][] board = new Label[Game.getBoardheight()][Game.getBoardwidth()];
    BorderPane container;
    Label turns;
    VBox p1;
    VBox p2;
    Label p1Name;
    Label p2Name;
    Label p1Champs;
    Label p2Champs;
    public Scene scene(double width, double height, Game game) {
        // setting the listener of the game, each champion and each ability to this class
        game.setListener(this);
        this.game = game;
        container = new BorderPane();
        makeBoard();

        /////////////
        //top of the board
        /////////////
        p1Name = new Label(game.getFirstPlayer().getName());
        p1Name.setAlignment(Pos.TOP_RIGHT);
        p2Name = new Label(game.getSecondPlayer().getName());
        p2Name.setAlignment(Pos.TOP_RIGHT);
        p1Champs = new Label();
        p1Champs.setAlignment(Pos.TOP_LEFT);
        p2Champs = new Label();
        p2Champs.setAlignment(Pos.TOP_RIGHT);
        setTeams();

        Button p1LeaderAbility = new Button("Use " + ((Champion) game.getFirstPlayer().getLeader()).toString() + " Leader Ability");
        Button p2LeaderAbility = new Button("Use " + ((Champion) game.getSecondPlayer().getLeader()).toString() + " Leader Ability");

        p1 = new VBox();
        p1.getChildren().addAll(p1Name, p1Champs, p1LeaderAbility);
        p1.setAlignment(Pos.CENTER_LEFT);

        p2 = new VBox();
        p2.getChildren().addAll(p2Name, p2Champs, p2LeaderAbility);
        p2.setAlignment(Pos.CENTER_RIGHT);

        turns = new Label();
        turns.setText(game.getTurnOrder().toString());
        turns.setAlignment(Pos.CENTER);

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        HBox.setHgrow(p1,Priority.ALWAYS);
        HBox.setHgrow(p2, Priority.ALWAYS);
        top.getChildren().addAll(p1,turns,p2);

        container.setTop(top);


        //////////////////////////////////
        //    Button Functions
        /////////////////////////////////
        //TODO find a way to make it even cooler, eshtat?
        p1LeaderAbility.setOnAction(e -> {
            try {
                game.useLeaderAbility();
                p1LeaderAbility.setDisable(!p1LeaderAbility.isDisable());
            } catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException exception) {
                AlertBox.display("Can't use leader ability", exception.getMessage());
            } catch (CloneNotSupportedException exception) {
                exception.printStackTrace();
            }

        });
        p2LeaderAbility.setOnAction(e -> {
            try {
                game.useLeaderAbility();
                p2LeaderAbility.setDisable(!p2LeaderAbility.isDisable());
            } catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException exception) {
                AlertBox.display("Can't use leader ability", exception.getMessage());
            } catch (CloneNotSupportedException exception) {
                exception.printStackTrace();
            }

        });


        return new Scene(container, width, height);
    }

    private void makeBoard() {
        boardGrid = new GridPane();
        for (int i = 0; i < 5; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(100);
            boardGrid.getColumnConstraints().add(columnConstraints);
        }
        for (int i = 0; i < 5; i++) {
            RowConstraints rowConstraints = new RowConstraints(100);
            boardGrid.getRowConstraints().add(rowConstraints);
        }
        boardGrid.setStyle("-fx-backgroud-color: white; -fx-grid-lines-visible: true");
        for (int i = 0; i < Game.getBoardheight(); i++) {
            for (int j = 0; j < Game.getBoardwidth(); j++) {
                board[i][j] = new Label();
                if (game.getBoard()[i][j] == null) { //TODO play bel border thickness
                    board[i][j].setText("empty");
                    board[i][j].setTextFill(Color.DARKSLATEGRAY);
                } else if (game.getBoard()[i][j] instanceof Champion) {

                    Champion thisChamp = (Champion) game.getBoard()[i][j];
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
                    Tooltip.install(board[i][j], new Tooltip(thisChamp.getName() +
                            "\nType: " + thisChampType +
                            "\nMax HP: " + thisChamp.getMaxHP() + "\nMana: " + thisChamp.getMana() +
                            "\nMax Action Points Per Turn: " + thisChamp.getMaxActionPointsPerTurn() +
                            "\nAttack Damage: " + thisChamp.getAttackDamage() + "\nAttack Range: " + thisChamp.getAttackRange()
                            + "\nSpeed: " + thisChamp.getSpeed() + "\nAbilities: " + finalChampAbilities));

                    board[i][j].setText(((Champion) game.getBoard()[i][j]).toString());
                    if (((Champion) game.getBoard()[i][j]).equals(game.getCurrentChampion())) {
                        board[i][j].setTextFill(Color.TOMATO); //أسمى ماطم زى طماطم منغير ال"ط"

                    } else if (game.getFirstPlayer().getTeam().contains((Champion) game.getBoard()[i][j])) {
                        board[i][j].setTextFill(Color.BLUEVIOLET); // TODO nelawen hena blz bs 2l background
                    } else {
                        board[i][j].setTextFill(Color.CORNFLOWERBLUE);
                    }
                } else {
                    board[i][j].setText("Cover HP:\n" + ((Cover) game.getBoard()[i][j]).getCurrentHP());
                    board[i][j].setTextFill(Color.DEEPPINK);
                }
                GridPane.setConstraints(board[i][j], j, Game.getBoardheight() - 1 - i);
                boardGrid.getChildren().add(board[i][j]); //Please run badal ma haneekak.
            }
        }
        boardGrid.setAlignment(Pos.CENTER);
        container.setCenter(boardGrid);
    }

    private void setTeams(){
        String p1 = "";
        String p2 = "";
        for (int i =0; i< game.getFirstPlayer().getTeam().size(); i++){
            p1+= ((Champion) game.getFirstPlayer().getTeam().get(i)).toString() + "\n";
        }
        p1Champs.setText(p1);

        for (int j =0; j< game.getSecondPlayer().getTeam().size(); j++){
            p2+= ((Champion) game.getSecondPlayer().getTeam().get(j)).toString() + "\n";
        }
        p2Champs.setText(p2);
    }
    @Override
    public void onBoardUpdated() {
        makeBoard();
    }

    @Override
    public void onGameOver(Player winner) {
        // TODO show an alertbox with winning player name and send back to start screen?

    }

    @Override
    public void onTurnOrderUpdated(PriorityQueue turnOrder) {
        turns.setText(game.getTurnOrder().toString());
    }

    @Override
    public void onPlayerTeamsUpdated() {
        // TODO redraw both teams
        setTeams();
    }

    @Override
    public void onAbilityCast() {
        // TODO update all champion details
    }

    @Override
    public void onAttackHit() {
        // TODO update all champion details
    }
}
