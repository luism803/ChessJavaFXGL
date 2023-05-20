package model;


import com.almasb.fxgl.core.math.Vec2;
import model.pieces.Piece;

import java.util.Observable;

/**
 * Class SquareModel
 * Model of a square of the board
 */
public class SquareModel extends Observable {
    private Vec2 pos;
    private boolean jugada;
    private boolean seleccion;
    private boolean puntero;
    private Piece piece;
    /**
     * Constructor
     * @param x x position
     * @param y y position
     */
    public SquareModel(int x, int y) {
        this.puntero = false;
        seleccion = false;
        jugada = false;
        piece = null;
        pos = new Vec2(x, y);
    }

    /**
     * Get the position of the square
     * @return Position
     */
    public Vec2 getPos() {
        return pos;
    }

    /**
     * Check if the square is a possible move
     * @return True if it is a possible move
     */
    public boolean isJugada() {
        return jugada;
    }
    /**
     * Set the square as a possible move
     * @param jugada True if it is a possible move
     */
    public void setJugada(boolean jugada) {
        if (this.jugada != jugada)
            setChanged();
        this.jugada = jugada;
    }

    /**
     * Check if the square is selected
     * @return True if it is selected
     */
    public boolean isSeleccion() {
        return seleccion;
    }
    /**
     * Set the square as selected
     * @param seleccion True if it is selected
     */
    public void setSeleccion(boolean seleccion) {
        if (this.seleccion != seleccion)
            setChanged();
        this.seleccion = seleccion;
    }
    /**
     * Check if the square is the pointer
     * @return True if it is the pointer
     */
    public boolean isPuntero() {
        return puntero;
    }
    /**
     * Set the square as the pointer
     * @param puntero True if it is the pointer
     */
    public void setPuntero(boolean puntero) {
        if (this.puntero != puntero)
            setChanged();
        this.puntero = puntero;
    }
    /**
     * Get the piece of the square
     * @return The piece of the square
     */
    public Piece getPiece() {
        return piece;
    }
    /**
     * Set the piece of the square
     * @param piece The piece of the square
     */
    public void setPiece(Piece piece) {
        if (this.piece != piece)
            setChanged();
        this.piece = piece;
    }
}
