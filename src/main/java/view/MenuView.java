package view;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.MenuModel;
import utils.Constantes;

import java.util.Observable;
import java.util.Observer;

public class MenuView implements Observer {
    private Rectangle background;
    private Text textTime;
    private ImageView imageMenu;

    public MenuView(String time) {
        textTime = new Text();
        textTime.setText(time);
        textTime.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        textTime.setFill(Color.WHITE);
        int width = (int) textTime.getLayoutBounds().getWidth();
        int height = (int) textTime.getLayoutBounds().getHeight();

        background = new Rectangle(Constantes.width, Constantes.height, Color.GREEN);
        FXGL.entityBuilder().view(background).buildAndAttach();

        imageMenu = new ImageView();
        imageMenu.setFitWidth(Constantes.width);
        imageMenu.setFitHeight(Constantes.height);
        imageMenu.setImage(new Image(Constantes.menuURL));
        FXGL.entityBuilder().view(imageMenu).buildAndAttach();

        int backgroundWidth = width * 2;
        int backgroudHeight = height * 2;

        FXGL.entityBuilder()
                .view(textTime)
                .at(Constantes.width / 2 - width / 2, Constantes.height / 2 + (3 * height) / 8)
                .buildAndAttach();
    }

    public void setOpacity(int opacity) {
        background.setOpacity(opacity);
        textTime.setOpacity(opacity);
        imageMenu.setOpacity(opacity);
    }

    public void setOpacity(boolean opacity) {
        setOpacity(opacity ? 0 : 1);
    }

    @Override
    public void update(Observable o, Object arg) {
        MenuModel model = (MenuModel) o;
        textTime.setText(model.timeToString());
    }
}
