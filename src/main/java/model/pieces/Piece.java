package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class Piece
 */
public abstract class Piece {
    protected int pieza;
    protected int lado;
    protected List<Vec2> moves;

    /**
     * Constructor for Piece
     * @param p Type of the piece
     * @param l Side of the piece
     */
    protected Piece(int p, int l) {
        moves = new ArrayList<>();
        pieza = p;
        lado = l;
    }

    /**
     * Get the number of the piece
     * @return Number of the piece
     */
    public int getPieza() {
        return pieza;
    }

    /**
     * Set the number of the piece
     * @param pieza Number of the piece
     */
    public void setPieza(int pieza) {
        this.pieza = pieza;
    }

    /**
     * Get the side of the piece
     * @return Side of the piece
     */
    public int getLado() {
        return lado;
    }

    /**
     * Set the side of the piece
     * @param lado Side of the piece
     */
    public void setLado(int lado) {
        this.lado = lado;
    }

    /**
     * Calculate the possible moves of the piece
     * @param board Board of the game
     * @param pos Position of the piece
     * @param comprobar Check if the king is in check after the move
     * @return List of possible moves
     */
    public abstract List<Vec2> calculateMoves(BoardModel board, Vec2 pos, boolean comprobar);

    /**
     * Add a move to the list of possible moves
     * @param board Board of the game
     * @param pos Position of the move
     * @return True if the new move's square is empty (the piece can still move)
     */
    protected boolean addMove(BoardModel board, Vec2 pos) {
        if (board.isEmpty(pos) || board.isEnemy(pos, lado))
            moves.add(pos.copy());
        return board.isEmpty(pos);
    }

    /**
     * Copy the piece
     * @return Copy of the piece
     */
    public abstract Piece copy();

    /**
     * Remove the moves that put the king in check
     * @param board Board of the game
     * @param pos Position of the piece
     */
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
