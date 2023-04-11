import controller.GameController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import view.BoardView;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.BoardModel;
import structs.KeyInfo;
import utils.Constantes;

import java.util.HashMap;
import java.util.Map;

public class Game extends GameApplication {

    private GameController gameController;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(Constantes.width);
        settings.setHeight(Constantes.height);
        settings.setTicksPerSecond(60);
    }

    protected void initGame() {
        gameController = new GameController();
    }

    @Override
    protected void initInput() {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure do you want to close?");
//        alert.showAndWait().ifPresent(response -> {
//            if (response == ButtonType.OK) {
//                Platform.exit();
//            }
//        });
    }

    @Override
    protected void onUpdate(double tpf) {
        gameController.onUpdate(tpf);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
