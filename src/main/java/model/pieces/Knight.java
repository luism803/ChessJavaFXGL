package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.List;

/**
 * Class Knight
 */
public class Knight extends Piece {
    /**
     * Constructor for Knight
     * @param l Side of the piece
     */
    public Knight(int l) {
        super(3, l);
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
        addMove(board, new Vec2(pos.x - 1, pos.y + 2));
        addMove(board, new Vec2(pos.x + 1, pos.y + 2));
        addMove(board, new Vec2(pos.x - 1, pos.y - 2));
        addMove(board, new Vec2(pos.x + 1, pos.y - 2));
        addMove(board, new Vec2(pos.x + 2, pos.y - 1));
        addMove(board, new Vec2(pos.x + 2, pos.y + 1));
        addMove(board, new Vec2(pos.x - 2, pos.y - 1));
        addMove(board, new Vec2(pos.x - 2, pos.y + 1));
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
        return new Knight(lado);
    }
}
