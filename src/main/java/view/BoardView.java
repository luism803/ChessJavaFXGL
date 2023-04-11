package view;

import javafx.scene.paint.Color;
import model.ClockModel;
import model.SquareModel;
import utils.Constantes;

import java.util.Arrays;

public class BoardView {
    private SquareView[][] squareViews;
    private ClockView clockView;

    public BoardView(SquareModel[][] squareModels, ClockModel clockModel) {
        squareViews = new SquareView[Constantes.squareNumber][Constantes.squareNumber];
        clockView = new ClockView(clockModel.timeToString(0));
        Color blanca = Color.web("rgb(250,220,175)");
        Color negra = Color.web("rgb(200,150,100)");

        for (int x = 0; x < Constantes.squareNumber; x++) {
            for (int y = 0; y < Constantes.squareNumber; y++) {
                Color color = (((x + y) & 1) == 0) ? blanca : negra;
                squareViews[x][y] = new SquareView(x, y, Constantes.height / Constantes.squareNumber, color);
                squareModels[x][y].addObserver(squareViews[x][y]);
            }
        }

        clockModel.addObserver(clockView);
    }

    public void setOpacity(int opacity) {
        Arrays.stream(squareViews).toList().forEach(e1 -> Arrays.stream(e1).toList().forEach(e2 -> e2.setOpacity(opacity)));
        clockView.setOpacity(opacity);
    }

    public void setOpacity(boolean opacity) {
        setOpacity(opacity ? 0 : 1);
    }
}
