package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.List;

/**
 * Class King
 */
public class King extends Piece {
    /**
     * Constructor for King
     * @param l Side of the piece
     */
    public King(int l) {
        super(0, l);
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
        moves.clear();
        addMove(board, new Vec2(pos.x + 1, pos.y));
        addMove(board, new Vec2(pos.x - 1, pos.y));
        addMove(board, new Vec2(pos.x, pos.y + 1));
        addMove(board, new Vec2(pos.x, pos.y - 1));
        addMove(board, new Vec2(pos.x + 1, pos.y + 1));
        addMove(board, new Vec2(pos.x + 1, pos.y - 1));
        addMove(board, new Vec2(pos.x - 1, pos.y + 1));
        addMove(board, new Vec2(pos.x - 1, pos.y - 1));
        if (check) {
            addCastling(board, pos);
            checkMoves(board, pos);
        }
        return moves;
    }

    /**
     * Add the castling move if it's possible
     * @param board Board of the game
     * @param pos Position of the piece
     */
    private void addCastling(BoardModel board, Vec2 pos) {
        if (lado == 0 && pos.y == 7 || lado == 1 && pos.y == 0) {
            Vec2 pos1 = new Vec2(pos.x + 1, pos.y);
            Vec2 pos2 = new Vec2(pos.x + 2, pos.y);
            //DERECHA
            if (!board.wasMoved(pos) && !board.wasMoved(new Vec2(pos.x + 3, pos.y)) &&
                    board.isEmpty(pos1) && board.isEmpty(pos2) &&
                    !board.isAtacked(pos1, lado) && !board.isAtacked(pos2, lado))
                moves.add(pos2);
            //IZQUIERDA
            pos1 = new Vec2(pos.x - 1, pos.y);
            pos2 = new Vec2(pos.x - 2, pos.y);
            Vec2 pos3 = new Vec2(pos.x - 3, pos.y);
            if (!board.wasMoved(pos) && !board.wasMoved(new Vec2(pos.x - 4, pos.y)) &&
                    board.isEmpty(pos1) && board.isEmpty(pos2) && board.isEmpty(pos3) &&
                    !board.isAtacked(pos1, lado) && !board.isAtacked(pos2, lado) && !board.isAtacked(pos3, lado))
                moves.add(pos2);
        }
    }

    /**
     * Copy the piece
     * @return Copy of the piece
     */
    @Override
    public Piece copy() {
        return new King(lado);
    }
}
