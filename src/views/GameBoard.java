package views;

import engine.Game;
import engine.GameListener;
import engine.Player;
import engine.PriorityQueue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.abilities.Ability;
import model.abilities.AbilityListener;
import model.world.Champion;
import model.world.ChampionListener;
import model.world.Cover;
import model.world.Direction;

/**
 * Not sure if I should implement {@link ChampionListener} and {@link AbilityListener} here, in {@link Game}
 * or somewhere completely different
 */
public class GameBoard implements GameListener {

    GridPane boardGrid;
    Game game;
    Label[][] board = new Label[Game.getBoardheight()][Game.getBoardwidth()];
    BorderPane container;

    public Scene scene(double width, double height, Game game) {
        // setting the listener of the game, each champion and each ability to this class
        game.setListener(this);
        this.game = game;

        container = new BorderPane();
        makeBoard();


        Button temp = new Button("test1");
        temp.setOnAction(e -> {
            try {
                game.move(Direction.UP);
            } catch (Exception ex) {
                AlertBox.display("cant move", ex.getMessage());
            }
            makeBoard();
        });

        container.setBottom(temp);

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
                    board[i][j].setText(((Champion) game.getBoard()[i][j]).toString() + "\n i: " + i + " j: " + j);
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

    @Override
    public void onBoardUpdated(Object[][] board) {
        // TODO redraw the board grid (update every cell)
        // maybe give a diff colour to curr Champion
    }

    @Override
    public void onGameOver(Player winner) {
        // TODO show an alertbox with winning player name and send back to start screen?
    }

    @Override
    public void onTurnOrderUpdated(PriorityQueue turnOrder) {
        // TODO redraw the turn order thingy
    }

    @Override
    public void onPlayerTeamsUpdated() {
        // TODO redraw both teams
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
