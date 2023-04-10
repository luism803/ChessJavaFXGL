package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Piece {
    protected int pieza;
    protected int lado;
    protected List<Vec2> moves;

    protected Piece(int p, int l) {
        moves = new ArrayList<>();
        pieza = p;
        lado = l;
    }

    public int getPieza() {
        return pieza;
    }

    public void setPieza(int pieza) {
        this.pieza = pieza;
    }

    public int getLado() {
        return lado;
    }

    public void setLado(int lado) {
        this.lado = lado;
    }

    public abstract List<Vec2> calculateMoves(BoardModel board, Vec2 pos, boolean comprobar);

    protected boolean addMove(BoardModel board, Vec2 pos) {
        if (board.isEmpty(pos) || board.isEnemy(pos, lado))
            moves.add(pos.copy());
        return board.isEmpty(pos);
    }

    public abstract Piece copy();

    protected void checkMoves(BoardModel board, Vec2 pos) {
        moves = moves.stream()
                .filter(m -> {
                    board.movePiece(m.copy(), pos.copy());
                    boolean res = !board.isInCheck(lado);
                    board.back();
                    return res;
                })
                .collect(Collectors.toList());
    }
}
