package View;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.SquareModel;
import utils.Constantes;

public class BoardView {
    private SquareView[][] squareViews;

    public void prueba(int x, int y, Image image) {
        squareViews[x][y].setPieceImage(image);
    }

    public BoardView(SquareModel[][] squareModels) {
        squareViews = new SquareView[Constantes.squareNumber][Constantes.squareNumber];
        Color blanca = Color.web("rgb(250,220,175)");
        Color negra = Color.web("rgb(200,150,100)");

        for (int x = 0; x < Constantes.squareNumber; x++) {
            for (int y = 0; y < Constantes.squareNumber; y++) {
                Color color = (((x + y) & 1) == 0) ? blanca : negra;
                squareViews[x][y] = new SquareView(x, y, Constantes.height / Constantes.squareNumber, color);
                squareModels[x][y].addObserver(squareViews[x][y]);
            }
        }
    }
}
