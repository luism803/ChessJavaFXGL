package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.List;

/**
 * Class Bishop
 */
public class Bishop extends Piece {
    /**
     * Constructor for Bishop
     * @param l Side of the piece
     */
    public Bishop(int l) {
        super(2, l);
    }

    /**
     * Calculate the possible moves of the piece
     * @param board Board of the game
     * @param pos Position of the piece
     * @param check Check if the king is in check after the move
     * @return List of possible moves
     */
    @Override
    public List<Vec2> calculateMoves(BoardModel board, Vec2 pos, boolean check) {
        Vec2 newJugada;
        moves.clear();

        /*ARRIBA DERECHA*/
        newJugada = new Vec2(pos.x + 1, pos.y + 1);
        while (addMove(board, newJugada)) {
            newJugada.x++;
            newJugada.y++;
        }
        /*ARRIBA IZQUIERDA*/
        newJugada = new Vec2(pos.x - 1, pos.y + 1);
        while (addMove(board, newJugada)) {
            newJugada.x--;
            newJugada.y++;
        }
        /*ABAJO DERECHA*/
        newJugada = new Vec2(pos.x + 1, pos.y - 1);
        while (addMove(board, newJugada)) {
            newJugada.x++;
            newJugada.y--;
        }
        /*ABAJO IZQUIERDA*/
        newJugada = new Vec2(pos.x - 1, pos.y - 1);
        while (addMove(board, newJugada)) {
            newJugada.x--;
            newJugada.y--;
        }
        if (check)
            checkMoves(board, pos);
        return moves;
    }

    /**
     * Copy the piece
     * @return Copy of the piece
     */
    @Override
    public Piece copy() {
        return new Bishop(lado);
    }
}
