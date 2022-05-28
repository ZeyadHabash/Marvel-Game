package views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TeamSelect implements EventHandler {

    public static Scene scene(double width, double height){
        VBox b = new VBox();
        return new Scene(b,width,height);
    }

    @Override
    public void handle(Event event) {

    }
}
