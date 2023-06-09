package utils;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Class Constants
 */
public class Constantes {
    public static final int squareNumber = 8;
    public static final double cooldown = 0.15;
    public static final int width = 1000;
    public static final int height = 600;
    public static final int maxTime = (int) (3600 * 0.5);
    public static final String tilesetURL = System.getProperty("user.dir")+"\\src\\main\\resources\\img\\chessTileset.png";
    public static final String menuURL = System.getProperty("user.dir")+"\\src\\main\\resources\\img\\menu.png";
    public static final int heightClock = 2 * height / 7;

    /**
     * Get a tile from the tileset
     * @param x X position of the tile
     * @param y Y position of the tile
     * @return Tile
     */
    public static WritableImage getTile(int x, int y) {
        Image tileset = new Image(Constantes.tilesetURL);

        // Definir las dimensiones del tileset
        int tileWidth = (int) tileset.getWidth() / 6;
        int tileHeight = (int) tileset.getHeight() / 2;

        // Calcular las coordenadas del recorte en píxeles
        int startX = x * tileWidth;
        int startY = y * tileHeight;

        // Crear un nuevo objeto WritableImage con el recorte
        return new WritableImage(tileset.getPixelReader(), startX, startY, tileWidth, tileHeight);
    }
}
