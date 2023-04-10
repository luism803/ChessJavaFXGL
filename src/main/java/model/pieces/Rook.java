package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.List;

public class Rook extends Piece {
    public Rook(int l) {
        super(4, l);
    }

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

    @Override
    public Piece copy() {
        return new Rook(lado);
    }
}
