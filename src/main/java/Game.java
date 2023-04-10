import View.BoardView;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.BoardModel;
import structs.KeyInfo;
import utils.Constantes;

import java.util.HashMap;
import java.util.Map;

public class Game extends GameApplication {
    private BoardModel boardModel;
    private BoardView boardView;
    private Map<KeyCode, KeyInfo> keys;
    private ImageView imageView;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(Constantes.width);
        settings.setHeight(Constantes.height);
        settings.setTicksPerSecond(60);
        keys = new HashMap<KeyCode, KeyInfo>();
        keys.put(KeyCode.LEFT, new KeyInfo("LEFT", () -> boardModel.goLeft()));
        keys.put(KeyCode.RIGHT, new KeyInfo("RIGHT", () -> boardModel.goRight()));
        keys.put(KeyCode.UP, new KeyInfo("UP", () -> boardModel.goUp()));
        keys.put(KeyCode.DOWN, new KeyInfo("DOWN", () -> boardModel.goDown()));
        keys.put(KeyCode.SPACE, new KeyInfo("SPACE", () -> boardModel.quitarSeleccion()));
        keys.put(KeyCode.ENTER, new KeyInfo("ENTER", () -> boardModel.seleccionar()));
        keys.put(KeyCode.BACK_SPACE, new KeyInfo("BACK_SPACE", () -> boardModel.back()));
        keys.put(KeyCode.TAB, new KeyInfo("TAB", () -> boardModel.back()));
    }

    protected void initGame() {
        boardModel = new BoardModel();
        boardView = new BoardView(boardModel.getSquareModels());
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        keys.forEach((code, info) ->
                input.addAction(new UserAction(info.getName()) {
                    @Override
                    protected void onAction() {
                        if (info.getCooldown() <= 0) {
                            info.getAccion().run();
                            info.setCooldown(Constantes.cooldown);
                        }
                    }
                }, code)
        );
        input.addAction(new UserAction("PRIMARY") {
            Point2D mouse = input.getMousePositionWorld();

            @Override
            protected void onActionBegin() {
                boardModel.seleccionarRaton(new Vec2(FXGL.getInput().getMousePositionWorld().getX(), FXGL.getInput().getMousePositionWorld().getY()));
            }

            @Override
            protected void onActionEnd() {
                boardModel.seleccionarRaton(new Vec2(FXGL.getInput().getMousePositionWorld().getX(), FXGL.getInput().getMousePositionWorld().getY()), false);
            }
        }, MouseButton.PRIMARY);
        input.addAction(new UserAction("SECONDARY") {
            @Override
            protected void onActionBegin() {
                boardModel.quitarSeleccion();
            }
        }, MouseButton.SECONDARY);
    }

    @Override
    protected void onUpdate(double tpf) {
        keys.forEach((code, info) -> {
                    if (info.getCooldown() > 0)
                        info.setCooldown(info.getCooldown() - tpf);
                }
        );
        boardModel.onUpdate();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
