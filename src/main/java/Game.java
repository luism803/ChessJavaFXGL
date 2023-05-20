import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import controller.GameController;
import utils.Constantes;

/**
 * Class Game
 */
public class Game extends GameApplication {

    private GameController gameController;

    /**
     * Initialize the settings of the game
     * @param settings
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Chess");
        settings.setWidth(Constantes.width);
        settings.setHeight(Constantes.height);
        settings.setTicksPerSecond(60);
    }

    /**
     * Initialize the game
     */
    protected void initGame() {
        gameController = new GameController();
    }

    /**
     * Initialize the input
     */
    @Override
    protected void initInput() {
    }

    /**
     * Initialize the game
     * @param tpf Time per frame
     */
    @Override
    protected void onUpdate(double tpf) {
        gameController.onUpdate(tpf);
    }

    /**
     * Main
     * @param args Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
