import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import controller.GameController;
import utils.Constantes;

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
    }

    @Override
    protected void onUpdate(double tpf) {
        gameController.onUpdate(tpf);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
