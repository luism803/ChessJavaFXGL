package controller;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.BoardModel;
import model.MenuModel;
import structs.KeyInfo;
import utils.Constantes;
import view.BoardView;
import view.MenuView;

import java.util.HashMap;
import java.util.Map;

public class GameController {
    private BoardModel boardModel;
    private BoardView boardView;
    private MenuModel menuModel;
    private MenuView menuView;
    private Map<KeyCode, KeyInfo> keys;
    private int mode;
    private int time;

    public GameController() {
        mode = 0;
        addControls();
        initGame();
        addInputs();
        boardView.setOpacity(true);
    }

    private void addControls() {
        keys = new HashMap<KeyCode, KeyInfo>();
        keys.put(KeyCode.LEFT, new KeyInfo("LEFT", () -> {
            if (mode == 1)
                boardModel.goLeft();
        }));
        keys.put(KeyCode.RIGHT, new KeyInfo("RIGHT", () -> {
            if (mode == 1)
                boardModel.goRight();
        }));
        keys.put(KeyCode.UP, new KeyInfo("UP", () -> {
            if (mode == 0)
                menuModel.increaseTime();
            else if (mode == 1)
                boardModel.goUp();
        }));
        keys.put(KeyCode.DOWN, new KeyInfo("DOWN", () -> {
            if (mode == 0)
                menuModel.decreaseTime();
            else if (mode == 1)
                boardModel.goDown();
        }));
        keys.put(KeyCode.SPACE, new KeyInfo("SPACE", () -> {
            if (mode == 1)
                boardModel.removeSelection();
        }));
        keys.put(KeyCode.ENTER, new KeyInfo("ENTER", () -> {
            if (mode == 0) {
                boardModel.setTime(menuModel.enter());
                menuView.setOpacity(true);
                boardView.setOpacity(false);
                mode = 1;
            } else if (mode == 1)
                boardModel.select();
        }));

//        keys.put(KeyCode.BACK_SPACE, new KeyInfo("BACK_SPACE", () -> {
//            if (mode == 1)
//                boardModel.back();
//        }));
//        keys.put(KeyCode.TAB, new KeyInfo("TAB", () -> {
//            if (mode == 1)
//                boardModel.back();
//        }));
    }

    private void initGame() {
        menuModel = new MenuModel(180);
        menuView = new MenuView(menuModel.timeToString());
        menuModel.addObserver(menuView);
        boardModel = new BoardModel(0);
        boardView = new BoardView(boardModel.getSquare(), boardModel.getClock(), boardModel.getMessage());
    }

    private void addInputs() {
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
            @Override
            protected void onActionBegin() {
                boardModel.mouseSelect(new Vec2(FXGL.getInput().getMousePositionWorld().getX(), FXGL.getInput().getMousePositionWorld().getY()));
            }

            @Override
            protected void onActionEnd() {
                boardModel.mouseSelect(new Vec2(FXGL.getInput().getMousePositionWorld().getX(), FXGL.getInput().getMousePositionWorld().getY()), false);
            }
        }, MouseButton.PRIMARY);
        input.addAction(new UserAction("SECONDARY") {
            @Override
            protected void onActionBegin() {
                boardModel.removeSelection();
            }
        }, MouseButton.SECONDARY);
    }

    public void onUpdate(double tpf) {
        keys.forEach((code, info) -> {
                    if (info.getCooldown() > 0)
                        info.setCooldown(info.getCooldown() - tpf);
                }
        );
        boardModel.onUpdate(tpf);
    }
}
