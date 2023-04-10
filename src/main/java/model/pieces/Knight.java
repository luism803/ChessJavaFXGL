package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.List;

public class Knight extends Piece {
    public Knight(int l) {
        super(3, l);
    }

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

    @Override
    public Piece copy() {
        return new Knight(lado);
    }
}
