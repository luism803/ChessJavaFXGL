package view;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.ClockModel;
import utils.Constantes;

import java.util.Observable;
import java.util.Observer;

public class ClockView implements Observer {
    private Text timePlayer0;
    private Text timePlayer1;

    public ClockView(String time) {
        timePlayer1 = new Text();
        timePlayer1.setText(time);
        timePlayer1.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        timePlayer1.setFill(Color.WHITE);
        int width = (int)timePlayer1.getLayoutBounds().getWidth();
        int height = (int)timePlayer1.getLayoutBounds().getHeight();
        FXGL.entityBuilder()
                .view(timePlayer1)
                .at((Constantes.width/2+ Constantes.height/2)-width/2, Constantes.heightClock-height/2)
                .buildAndAttach();
        timePlayer0 = new Text();
        timePlayer0.setText(time);
        timePlayer0.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        timePlayer0.setFill(Color.WHITE);
        FXGL.entityBuilder()
                .view(timePlayer0)
                .at((Constantes.width/2+ Constantes.height/2)-width/2, Constantes.height - Constantes.heightClock)
                .buildAndAttach();
    }

    @Override
    public void update(Observable o, Object arg) {
        ClockModel model = (ClockModel) o;
        timePlayer0.setText(model.timeToString(0));
        timePlayer1.setText(model.timeToString(1));
    }

    public void setOpacity(int opacity) {
        timePlayer0.setOpacity(opacity);
        timePlayer1.setOpacity(opacity);
    }
}
