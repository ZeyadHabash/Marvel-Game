package views;

import engine.Game;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

public class GameBoard {
    public Scene scene(double width, double height, Game game){
        HBox layout = new HBox();
        return new Scene(layout, width, height);
    }
}
