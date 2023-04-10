package model;


import com.almasb.fxgl.core.math.Vec2;
import model.pieces.Piece;

import java.util.Observable;


public class SquareModel extends Observable {
    private Vec2 pos;
    private boolean jugada;
    private boolean seleccion;
    private boolean puntero;
    private Piece piece;

    public SquareModel(int x, int y) {
        this.puntero = false;
        seleccion = false;
        jugada = false;
        piece = null;
        pos = new Vec2(x, y);
    }

    public Vec2 getPos() {
        return pos;
    }

    public boolean isJugada() {
        return jugada;
    }

    public void setJugada(boolean jugada) {
        if (this.jugada != jugada)
            setChanged();
        this.jugada = jugada;
    }

    public boolean isSeleccion() {
        return seleccion;
    }

    public void setSeleccion(boolean seleccion) {
        if (this.seleccion != seleccion)
            setChanged();
        this.seleccion = seleccion;
    }

    public boolean isPuntero() {
        return puntero;
    }

    public void setPuntero(boolean puntero) {
        if (this.puntero != puntero)
            setChanged();
        this.puntero = puntero;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        if (this.piece != piece)
            setChanged();
        this.piece = piece;
    }
}
