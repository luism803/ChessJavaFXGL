package model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import model.BoardModel;

import java.util.List;

/**
 * Class Pawn
 */
public class Pawn extends Piece {

    /**
     * Constructor for Pawn
     * @param l Side of the piece
     */
    public Pawn(int l) {
        super(5, l);

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
        if (lado == 0) {  //blancas
            //movimiento
            if (addMove(board, new Vec2(pos.x, pos.y - 1)) && pos.y == 6)
                addMove(board, new Vec2(pos.x, pos.y - 2));
            //ataque
            addAttack(board, new Vec2(pos.x - 1, pos.y - 1));
            addAttack(board, new Vec2(pos.x + 1, pos.y - 1));
        } else {    //negras
            //movimiento
            if (addMove(board, new Vec2(pos.x, pos.y + 1)) && pos.y == 1)
                addMove(board, new Vec2(pos.x, pos.y + 2));
            //ataque
            addAttack(board, new Vec2(pos.x - 1, pos.y + 1));
            addAttack(board, new Vec2(pos.x + 1, pos.y + 1));
        }
        if (check)
            checkMoves(board, pos);
        return moves;
    }

    /**
     * Add a move to the list of possible moves
     * @param board Board of the game
     * @param pos Position of the move
     * @return True if the move is valid
     */
    @Override
    protected boolean addMove(BoardModel board, Vec2 pos) {
        if (board.isEmpty(pos))
            moves.add(pos.copy());
        return board.isEmpty(pos);
    }

    /**
     * Add an attack to the list of possible moves
     * @param board Board of the game
     * @param pos Position of the attack
     */
    private void addAttack(BoardModel board, Vec2 pos) {
        if (board.isEnemy(pos, lado))
            moves.add(pos.copy());
        if (lado == 0) {
            if (pos.y == 2 && board.isPeon(new Vec2(pos.x, pos.y + 1))
                    && board.isLastJugada(new Vec2(pos.x, pos.y - 1), new Vec2(pos.x, pos.y + 1)))
                moves.add(pos.copy());
        }
        if (lado == 1) {
            if (pos.y == 5 && board.isPeon(new Vec2(pos.x, pos.y - 1))
                    && board.isLastJugada(new Vec2(pos.x, pos.y + 1), new Vec2(pos.x, pos.y - 1)))
                moves.add(pos.copy());
        }
    }

    /**
     * Copy the piece
     * @return Copy of the piece
     */
    @Override
    public Piece copy() {
        return new Pawn(lado);
    }
}
