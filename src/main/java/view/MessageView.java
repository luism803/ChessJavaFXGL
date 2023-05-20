package view;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.MessageModel;
import utils.Constantes;

import java.util.Observable;
import java.util.Observer;
/**
 * Class MessageView
 */
public class MessageView implements Observer {

    private Rectangle backgroundText;
    private Text text;
    /**
     * Constructor for MessageView
     */
    public MessageView() {
        backgroundText = new Rectangle(Constantes.width, Constantes.height, Color.BLACK);
        text = new Text("WHITE WINS");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        text.setFill(Color.WHITE);
        int width = (int) text.getLayoutBounds().getWidth();
        int height = (int) text.getLayoutBounds().getHeight();
        FXGL.entityBuilder().view(backgroundText).buildAndAttach();
        FXGL.entityBuilder()
                .view(text)
                .at(Constantes.width / 2 - width / 2, Constantes.height / 2 + (3 * height) / 8)
                .buildAndAttach()
                .getViewComponent();
        setOpacity(0);
    }
    /**
     * Set the opacity of the clock
     * @param opacity Opacity of the clock (0 = transparent, 1 = opaque)
     */
    public void setOpacity(int opacity) {
        backgroundText.setOpacity((opacity == 0) ? 0 : (2.0 * opacity / 3));
        text.setOpacity(opacity);
    }
    /**
     * Set the opacity of the clock
     * @param opacity Opacity of the clock (true = transparent, false = opaque)
     */
    public void setOpacity(boolean opacity) {
        setOpacity(opacity ? 0 : 1);
    }
    /**
     * Update the view
     * @param o Observable object
     * @param arg Object
     */
    @Override
    public void update(Observable o, Object arg) {
        MessageModel model = (MessageModel) o;
        text.setText(model.getMessage());
        setOpacity(model.getMessage().equals(""));
    }
}
