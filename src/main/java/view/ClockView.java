package view;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.ClockModel;
import utils.Constantes;

import java.util.Observable;
import java.util.Observer;

/**
 * Class ClockView
 */
public class ClockView implements Observer {
    private Text timePlayer0;
    private Text timePlayer1;

    /**
     * Constructor for ClockView
     * @param time Time to display for both players
     */
    public ClockView(String time) {
        timePlayer1 = new Text(time);
        timePlayer1.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        timePlayer1.setFill(Color.BLACK);
        int width = (int) timePlayer1.getLayoutBounds().getWidth();
        int height = (int) timePlayer1.getLayoutBounds().getHeight();
        FXGL.entityBuilder()
                .view(timePlayer1)
                .at((Constantes.width / 2 + Constantes.height / 2) - width / 2, Constantes.heightClock + (3 * height) / 8)
                .buildAndAttach();
        timePlayer0 = new Text(time);
        timePlayer0.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        timePlayer0.setFill(Color.BLACK);
        FXGL.entityBuilder()
                .view(timePlayer0)
                .at((Constantes.width / 2 + Constantes.height / 2) - width / 2, Constantes.height - Constantes.heightClock + (3 * height) / 8)
                .buildAndAttach();
    }

    /**
     * Update the clock view
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(Observable o, Object arg) {
        ClockModel model = (ClockModel) o;
        timePlayer0.setText(model.timeToString(0));
        timePlayer1.setText(model.timeToString(1));
    }

    /**
     * Set the opacity of the clock
     * @param opacity Opacity of the clock (0 = transparent, 1 = opaque)
     */
    public void setOpacity(int opacity) {
        timePlayer0.setOpacity(opacity);
        timePlayer1.setOpacity(opacity);
    }
}
