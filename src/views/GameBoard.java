package views;

import engine.Game;
import engine.GameListener;
import engine.Player;
import engine.PriorityQueue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.abilities.Ability;
import model.abilities.AbilityListener;
import model.world.Champion;
import model.world.ChampionListener;
import model.world.Cover;

/**
 * Not sure if I should implement {@link ChampionListener} and {@link AbilityListener} here, in {@link Game}
 * or somewhere completely different
  */
public class GameBoard implements GameListener{
    GridPane boardGrid;
    Game game;
    public Scene scene(double width, double height, Game game) {
        // setting the listener of the game, each champion and each ability to this class
        game.setListener(this);

        BorderPane container = new BorderPane();
        boardGrid = new GridPane();
        makeBoard();


        return new Scene(container, width, height);
    }

    private void makeBoard() {
        Label[][] board = new Label[Game.getBoardheight()][Game.getBoardwidth()];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = new Label();
                if (game.getBoard()[i][j] == null) { //TODO play bel border thickness
                    board[i][j].setText("");

                } else if (game.getBoard()[i][j] instanceof Champion) {
                    board[i][j].setText(((Champion) game.getBoard()[i][j]).toString());
                    if (((Champion) game.getBoard()[i][j]).equals(game.getCurrentChampion())) {
                        board[i][j].setTextFill(Color.TOMATO); //أسمى ماطم زى طماطم منغير ال"ط"
                    } else if (game.getFirstPlayer().getTeam().contains((Champion) game.getBoard()[i][j])) {
                        board[i][j].setTextFill(Color.BLUEVIOLET); // TODO nelawen hena blz bs 2l background
                    } else {
                        board[i][j].setTextFill(Color.YELLOW);
                    }
                } else {
                    board[i][j].setText("Cover, HP: " + ((Cover) game.getBoard()[i][j]).getCurrentHP());
                }
                GridPane.setConstraints(board[i][j], j, i);
                boardGrid.getChildren().add(board[i][j]); //Please run badal ma haneekak.
            }
        }
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
