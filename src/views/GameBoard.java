package views;

import engine.Game;
import engine.GameListener;
import engine.Player;
import engine.PriorityQueue;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import model.abilities.Ability;
import model.abilities.AbilityListener;
import model.world.Champion;
import model.world.ChampionListener;

/** Not sure if I should implement {@link ChampionListener} and {@link AbilityListener} here, in {@link Game}
 * or somewhere completely different
  */
public class GameBoard implements GameListener, ChampionListener, AbilityListener {
    public Scene scene(double width, double height, Game game) {
        game.setListener(this);
        HBox layout = new HBox();
        return new Scene(layout, width, height);
    }

    @Override
    public void onBoardUpdated(Object[][] board) {

    }

    @Override
    public void onGameOver(Player winner) {

    }

    @Override
    public void onTurnOrderUpdated(PriorityQueue turnOrder) {

    }

    @Override
    public void onPlayerTeamUpdated(Player player) {

    }

    @Override
    public void onAbilityDetailsUpdated(Ability ability) {

    }

    @Override
    public void onChampionDetailsUpdated(Champion champion) {

    }
}
