package model;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.Constantes;

public class MenuModel {
    Rectangle rect;
    public MenuModel() {
        rect = new Rectangle(Constantes.width, Constantes.height, Color.BLACK);
        FXGL.entityBuilder().view(rect).buildAndAttach();
    }

    public int enter(){
        return 3*60;
    }

    public void setOpacity(int opacity){
        rect.setOpacity(opacity);
    }

    public void setOpacity(boolean opacity){
        setOpacity(opacity?0:1);
    }
}
