package view;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.SquareModel;
import utils.Constantes;

import java.util.Observable;
import java.util.Observer;

public class SquareView implements Observer {
    static Color punteroColor = Color.web("rgb(20, 160, 20)", 1.0);
    static Color jugadaColor = Color.web("rgb(130, 200, 255)", 0.75);
    static Color seleccionColor = Color.web("rgb(120, 120, 120)", 1.0);
    static Color ataqueColor = Color.web("rgb(255, 50, 0)", 0.75);
    private Rectangle rectFiltro;
    private Rectangle rectBase;
    private ImageView image;

    public SquareView(double x, double y, double size, Color baseColor) {
        x *= size;
        y *= size;
        rectBase = new Rectangle(size, size, baseColor);
        rectFiltro = new Rectangle(size, size, Color.color(0, 0, 0, 0));
        image = new ImageView();
        image.setFitHeight(Constantes.height / Constantes.squareNumber);
        image.setFitWidth(Constantes.height / Constantes.squareNumber);
        FXGL.entityBuilder().at(x, y).view(rectBase).buildAndAttach();
        FXGL.entityBuilder().at(x, y).view(rectFiltro).buildAndAttach();
        FXGL.entityBuilder().at(x, y).view(image).buildAndAttach();
    }

    public void setPieceImage(Image pieceImage) {
        image.setImage(pieceImage);
    }

    @Override
    public void update(Observable o, Object arg) {
        SquareModel model = (SquareModel) o;
        rectFiltro.setFill(Color.color(0, 0, 0, 0));
        if (model.isJugada()) {
            if (model.getPiece() == null) rectFiltro.setFill(jugadaColor);
            else rectFiltro.setFill(ataqueColor);
        }
        if (model.isSeleccion()) rectFiltro.setFill(seleccionColor);
        if (model.isPuntero()) rectFiltro.setFill(punteroColor);
        if (model.getPiece() != null)
            image.setImage(Constantes.getTile(model.getPiece().getPieza(), model.getPiece().getLado()));
        else image.setImage(null);
    }

    public void setOpacity(int opacity) {
        rectBase.setOpacity(opacity);
        rectFiltro.setOpacity(opacity);
        image.setOpacity(opacity);
    }
}
