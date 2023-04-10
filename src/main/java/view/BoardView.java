package view;

import javafx.scene.paint.Color;
import model.ClockModel;
import model.SquareModel;
import utils.Constantes;

import java.time.Clock;

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
}
