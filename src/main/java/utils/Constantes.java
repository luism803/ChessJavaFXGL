package utils;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Constantes {
    public static final int squareNumber = 8;
    public static final double cooldown = 0.15;
    public static final int width = 1000;
    public static final int height = 600;
    public static final String tilesetURL = "C:\\Users\\paggi\\IdeaProjects\\demo\\src\\main\\resources\\img\\chessTileset.png";

    public static final int heightClock = height/3;
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
