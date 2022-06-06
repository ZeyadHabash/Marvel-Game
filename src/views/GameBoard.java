package views;

import engine.Game;
import engine.GameListener;
import engine.Player;
import engine.PriorityQueue;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.abilities.Ability;
import model.abilities.AbilityListener;
import model.world.Champion;
import model.world.ChampionListener;

/** Not sure if I should implement {@link ChampionListener} and {@link AbilityListener} here, in {@link Game}
 * or somewhere completely different
  */
public class GameBoard implements GameListener{
    public Scene scene(double width, double height, Game game) {
        // setting the listener of the game
        game.setListener(this);

        BorderPane container = new BorderPane();


        return new Scene(container, width, height);
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
