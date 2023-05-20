package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.List;

/**
 * Class Rook
 */
public class Rook extends Piece {
    /**
     * Constructor for Rook
     * @param l Side of the piece
     */
    public Rook(int l) {
        super(4, l);
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
        Vec2 newMove;
        moves.clear();
        //UP
        newMove = new Vec2(pos.x, pos.y - 1);
        while (addMove(board, newMove))
            newMove.y--;
        //DOWN
        newMove = new Vec2(pos.x, pos.y + 1);
        while (addMove(board, newMove))
            newMove.y++;
        //RIGHT
        newMove = new Vec2(pos.x + 1, pos.y);
        while (addMove(board, newMove))
            newMove.x++;
        //LEFT
        newMove = new Vec2(pos.x - 1, pos.y);
        while (addMove(board, newMove))
            newMove.x--;
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
        return new Rook(lado);
    }
}
