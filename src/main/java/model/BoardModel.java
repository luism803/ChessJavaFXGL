package model;

import com.almasb.fxgl.core.math.Vec2;
import model.pieces.*;
import structs.RecordMove;
import utils.Constantes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;
/**
 * Class BoardModel
 * Model of the board
 */
public class BoardModel {
    private boolean promoting;
    private boolean showPuntero;
    private boolean gameFinished;
    private int ladoPromotion;
    private Vec2 puntero;
    private Vec2 seleccion;
    private Vec2[] positions;
    private List<Vec2> moves;
    private List<RecordMove> recordMoves;
    private Piece[] actualPieces;
    private SquareModel[][] squares;
    private SquareModel squareSelection;
    private ClockModel clock;
    private MessageModel message;

    /**
     * Constructor
     * @param puntero Position of pointer
     * @param time Time of the game
     */
    public BoardModel(Vec2 puntero, double time) {
        this.puntero = puntero;
        showPuntero = true;
        promoting = false;
        gameFinished = false;
        ladoPromotion = 0;
        seleccion = null;
        squares = new SquareModel[Constantes.squareNumber][Constantes.squareNumber];
        squareSelection = null;
        moves = new ArrayList<>();
        positions = new Vec2[3];
        actualPieces = new Piece[3];
        recordMoves = new ArrayList<>();
        clock = new ClockModel(time);
        message = new MessageModel();
        for (int x = 0; x < Constantes.squareNumber; x++) {
            for (int y = 0; y < Constantes.squareNumber; y++) {
                squares[x][y] = new SquareModel(x, y);
            }
        }
        placePieces();
    }

    /**
     * Constructor
     * @param time Time of the game
     */
    public BoardModel(double time) {
        this(new Vec2(), time);
    }

    /**
     * Getter of square models of the board
     * @return Bidimensional array of square models
     */
    public SquareModel[][] getSquare() {
        return squares;
    }

    /**
     * Getter for clock model of the board
     * @return Clock model
     */
    public ClockModel getClock() {
        return clock;
    }

    /**
     * Getter for the side's turn
     * @return 0 if it is white's turn, 1 if it is black's turn
     */
    private int getCurrentTurn() {
        if (promoting) {
            return ladoPromotion;
        }
        return (recordMoves.size() == 0 || recordMoves.get(recordMoves.size() - 1).getPieceOri().getLado() == 1) ? 0 : 1;
    }

    /**
     * Getter for the message model of the board
     * @return Message model
     */
    public MessageModel getMessage() {
        return message;
    }

    /**
     * Getter for the position of the king's square
     * @param lado Side of the king
     * @return Position of the king
     */
    private Vec2 getKing(int lado) {
        AtomicReference<Vec2> res = new AtomicReference<>();
        Arrays.stream(squares)
                .flatMap(Arrays::stream)
                .forEach(square -> {
                    if (!isEnemy(square.getPos(), lado) && square.getPiece() instanceof King)
                        res.set(square.getPos());
                });
        return res.get();
    }

    /**
     * Getter for the message when the game is finished
     * @return Message of finished game
     */
    private String getFinalMessage() {
        if (!clock.isRun())
            return ((getCurrentTurn() == 0) ? "BLACK" : "WHITE") + " WINS";
        if (detectChecks())
            return ((getCurrentTurn() == 0) ? "BLACK" : "WHITE") + " WINS";
        else
            return "STALE MATE";
    }

    /**
     * Undo the last move
     */
    public void back() {
        if (!promoting && recordMoves.size() > 0) {
            RecordMove lastRecord = recordMoves.get(recordMoves.size() - 1);
            squares[(int) lastRecord.getOri().x][(int) lastRecord.getOri().y].setPiece(lastRecord.getPieceOri());
            squares[(int) lastRecord.getDes().x][(int) lastRecord.getDes().y].setPiece(lastRecord.getPieceDes());
            recordMoves.remove(recordMoves.size() - 1);
            //SI HUBO PEON PASADO
            if (lastRecord.getPieceOri() instanceof Pawn && lastRecord.getPieceDes() == null
                    && lastRecord.getOri().x != lastRecord.getDes().x)
                squares[(int) lastRecord.getDes().x][(int) lastRecord.getOri().y].setPiece(new Pawn(lastRecord.getPieceOri().getLado() * (-1) + 1));
            //SI HUBO ENROQUE
            if (lastRecord.getPieceOri() instanceof King && Math.abs(lastRecord.getOri().x - lastRecord.getDes().x) == 2) {
                //DERECHA
                if (lastRecord.getOri().x - lastRecord.getDes().x < 0) {
                    squares[(int) lastRecord.getDes().x + 1][(int) lastRecord.getDes().y].setPiece(new Rook(lastRecord.getPieceOri().getLado()));
                    squares[(int) lastRecord.getDes().x - 1][(int) lastRecord.getDes().y].setPiece(null);
                }
                //IZQUIERDA
                else {
                    squares[(int) lastRecord.getDes().x - 2][(int) lastRecord.getDes().y].setPiece(new Rook(lastRecord.getPieceOri().getLado()));
                    squares[(int) lastRecord.getDes().x + 1][(int) lastRecord.getDes().y].setPiece(null);
                }
            }
        }
    }

    /**
     * Place the pieces in the board
     */
    private void placePieces() {
        //PAWNS
        for (int i = 0; i < 8; i++) {
            squares[i][6].setPiece(new Pawn(0));
            squares[i][1].setPiece(new Pawn(1));
        }
        //ROOKS
        squares[7][7].setPiece(new Rook(0));
        squares[0][7].setPiece(new Rook(0));
        squares[7][0].setPiece(new Rook(1));
        squares[0][0].setPiece(new Rook(1));
        //BISHOPS
        squares[2][7].setPiece(new Bishop(0));
        squares[5][7].setPiece(new Bishop(0));
        squares[2][0].setPiece(new Bishop(1));
        squares[5][0].setPiece(new Bishop(1));
        //KNIGHTS
        squares[1][7].setPiece(new Knight(0));
        squares[6][7].setPiece(new Knight(0));
        squares[1][0].setPiece(new Knight(1));
        squares[6][0].setPiece(new Knight(1));
        //QUEENS
        squares[3][7].setPiece(new Queen(0));
        squares[3][0].setPiece(new Queen(1));
        //KINGS
        squares[4][7].setPiece(new King(0));
        squares[4][0].setPiece(new King(1));
    }

    /**
     * Check if one of the kings is in check
     * @return True if one of the kings is in check, false otherwise
     */
    private boolean detectChecks() {
        if (!promoting) {
            return isInCheck(getCurrentTurn());
        } else
            return false;
    }
    /**
     * Move the piece selected to the square selected
     */
    private void movePiece() { //se puede llamar a la funion copia
        movePiece(puntero, seleccion);
        removeSelection();
    }

    /**
     * Move the piece of the square (ori) to the square (des)
     * @param des Destination square
     * @param ori Origin square
     */
    public void movePiece(Vec2 des, Vec2 ori) {
        //GUARDAR JUGADA EN EL REGISTRO
        Piece pieceOri = null;
        Piece pieceDes = null;
        if (squares[(int) ori.x][(int) ori.y].getPiece() != null)
            pieceOri = squares[(int) ori.x][(int) ori.y].getPiece().copy();
        if (squares[(int) des.x][(int) des.y].getPiece() != null)
            pieceDes = squares[(int) des.x][(int) des.y].getPiece().copy();
        RecordMove recordMove = new RecordMove(ori, pieceOri, des, pieceDes);
        recordMoves.add(recordMove);
        //PEON PASADO
        if (recordMove.getPieceOri() instanceof Pawn && recordMove.getPieceDes() == null
                && recordMove.getOri().x != recordMove.getDes().x)    //si el peon esta comiendo un peon pasado
            squares[(int) recordMove.getDes().x][(int) recordMove.getOri().y].setPiece(null);  //quitar el peon comido
        //ENROQUE
        if (recordMove.getPieceOri() instanceof King && Math.abs(recordMove.getOri().x - recordMove.getDes().x) == 2) { //si el rey se esta moviendo dos casillas a la derecha o izquierda
            if (recordMove.getOri().x - recordMove.getDes().x < 0) {  //DERECHA
                squares[(int) recordMove.getDes().x + 1][(int) recordMove.getDes().y].setPiece(null);
                squares[(int) recordMove.getDes().x - 1][(int) recordMove.getDes().y].setPiece(new Rook(recordMove.getPieceOri().getLado()));
            } else {  ////IZQUIERDA
                squares[(int) recordMove.getDes().x - 2][(int) recordMove.getDes().y].setPiece(null);
                squares[(int) recordMove.getDes().x + 1][(int) recordMove.getDes().y].setPiece(new Rook(recordMove.getPieceOri().getLado()));
            }
        }
        //MOVER
        squares[(int) des.x][(int) des.y].setPiece(pieceOri);
        squares[(int) ori.x][(int) ori.y].setPiece(null);
    }

    /**
     * Remove the selection
     */
    public void removeSelection() {
        if (!gameFinished) {
            seleccion = null;
            squareSelection = null;
            moves.clear();
        }
    }

    /**
     * Select the square pointed by the pointer
     * if there is a selection, move the piece to the square pointed by the pointer
     */
    public void select() {
        select(puntero);
    }

    /**
     * Select the square pos, if there is a selection move the piece to the square pos
     * @param pos Position of the square to select
     */
    private void select(Vec2 pos) {
        if (!promoting) {
            //si hay seleccion
            if (seleccion != null && moves.stream().anyMatch(m -> m.equals(pos))) { //si se apunta hacia una casilla que es una jugada
                puntero = pos.copy();
                movePiece();   //mover la pieza seleecionada hacia la casilla apuntada
            } else {
                puntero = pos.copy();
                seleccion = pos.copy();  //actualizar la seleccion (Vec2)
                squareSelection = squares[(int) seleccion.x][(int) seleccion.y];  //actualizar la casilla de seleccion
                if (squareSelection.getPiece() == null               //si la casilla seleccionada esta vacia
                        || squareSelection.getPiece().getLado() != getCurrentTurn())    //o casilla seleccionada no es del color correspondiente
                    removeSelection();
            }
        } else {    //se elige la pieza de coronacion
            if (Arrays.asList(positions).contains(pos) ||   //la pieza seleccionada este en una de las positions guardadas
                    pos.equals(new Vec2(positions[0].x, positions[0].y * 2 - positions[1].y))) {   //la pieza seleccionada es la reina
                squares[(int) positions[0].x][(int) (positions[0].y * 2 - positions[1].y)].setPiece(squares[(int) pos.x][(int) pos.y].getPiece());
                squares[(int) positions[0].x][(int) positions[0].y].setPiece(actualPieces[0]);
                squares[(int) positions[1].x][(int) positions[1].y].setPiece(actualPieces[1]);
                squares[(int) positions[2].x][(int) positions[2].y].setPiece(actualPieces[2]);
                promoting = false;
            }
        }
        //CORONACION
        Vec2 pawnPromoted = pawnPromoted();
        if (pawnPromoted != null) {
            promoting = true;
            elegirCoronacion(pawnPromoted);
        }
        //update moves
        if (!promoting)
            moves.clear();    //vaciar las moves posibles
        if (seleccion != null) {    //si hay seleccionada una casilla
            if (squares[(int) seleccion.x][(int) seleccion.y].getPiece() != null) { //si hay una ficha en la casilla seleccionada
                moves = squares[(int) seleccion.x][(int) seleccion.y].getPiece().calculateMoves(this, seleccion, true);  //guardar las moves posibles de esa jugada
            }
        }
        //update squares color
        Arrays.stream(squares)
                .flatMap(Arrays::stream)
                .forEach(square -> {
                    square.setPuntero(showPuntero && square.getPos().equals(puntero));
                    square.setSeleccion(square.getPos().equals(seleccion));
                    square.setJugada((square.getPiece() instanceof King && square.getPiece().getLado() == getCurrentTurn() && isInCheck(getCurrentTurn()))
                            || moves.stream()
                            .anyMatch(m -> m.equals(square.getPos())));
                });
        //CHECK STATUS GAME
        if (!promoting && posiblesMoves(getCurrentTurn()).size() == 0) {
            gameFinished = true;
            message.setMessage(getFinalMessage());
        }
    }

    /**
     * Show the pieces that can be promoted
     * @param pawnPromoted Position of the pawn to be promoted
     */
    private void elegirCoronacion(Vec2 pawnPromoted) {
        ladoPromotion = 0;
        //blancas
        if (squares[(int) pawnPromoted.x][(int) pawnPromoted.y].getPiece().getLado() == 0) {
            //posiciones
            positions[0] = new Vec2(pawnPromoted.x, pawnPromoted.y + 1);
            positions[1] = new Vec2(pawnPromoted.x, pawnPromoted.y + 2);
            positions[2] = new Vec2(pawnPromoted.x, pawnPromoted.y + 3);
            ladoPromotion = 0;
        } else {
            //positions
            positions[0] = new Vec2(pawnPromoted.x, pawnPromoted.y - 1);
            positions[1] = new Vec2(pawnPromoted.x, pawnPromoted.y - 2);
            positions[2] = new Vec2(pawnPromoted.x, pawnPromoted.y - 3);
            ladoPromotion = 1;
        }
        //guardar fichas actuales
        actualPieces[0] = squares[(int) positions[0].x][(int) positions[0].y].getPiece();
        actualPieces[1] = squares[(int) positions[1].x][(int) positions[1].y].getPiece();
        actualPieces[2] = squares[(int) positions[2].x][(int) positions[2].y].getPiece();
        //Mostrar opciones
        squares[(int) pawnPromoted.x][(int) pawnPromoted.y].setPiece(new Queen(ladoPromotion));
        squares[(int) positions[0].x][(int) positions[0].y].setPiece(new Knight(ladoPromotion));
        squares[(int) positions[1].x][(int) positions[1].y].setPiece(new Rook(ladoPromotion));
        squares[(int) positions[2].x][(int) positions[2].y].setPiece(new Bishop(ladoPromotion));
        //añadir opciones a jugadas
        moves.add(pawnPromoted);
        moves.add(positions[0]);
        moves.add(positions[1]);
        moves.add(positions[2]);
    }

    /**
     * Return the position of the pawn to be promoted
     * @return Position of the pawn to be promoted
     */
    private Vec2 pawnPromoted() {
        AtomicReference<Vec2> res = new AtomicReference<>();
        Arrays.stream(squares)
                .flatMap(Arrays::stream)
                .forEach(square -> {
                    if (square.getPiece() instanceof Pawn && (square.getPos().y == 0 || square.getPos().y == Constantes.squareNumber - 1))
                        res.set(square.getPos());
                });
        return res.get();
    }

    /**
     * Return the possible moves of the side
     * @param lado
     * @return List of possible moves
     */
    private List<Vec2> posiblesMoves(int lado) {
        List<Vec2> moves = new ArrayList<>();
        Arrays.stream(squares)
                .flatMap(Arrays::stream).forEach(square -> {
                    if (square.getPiece() != null && square.getPiece().getLado() == lado)
                        moves.addAll(square.getPiece().calculateMoves(this, square.getPos(), true));
                });
        return moves;
    }

    /**
     * Select using the position of the mouse if the game is not finished
     * @param pos Position of the mouse
     */
    public void mouseSelect(Vec2 pos) {
        if (!gameFinished)
            mouseSelect(pos, true);
    }

    /**
     * Select using the position of the mouse if the game is not finished
     * @param pos Position of the mouse
     * @param normal If the selection is normal or not
     */
    public void mouseSelect(Vec2 pos, boolean normal) {
        if (!gameFinished && (normal || !promoting)) {
            showPuntero = false;
            //ajustar posicion raton
            Vec2 posRaton = new Vec2((int) (pos.x / (Constantes.height / Constantes.squareNumber)), (int) (pos.y / (Constantes.height / Constantes.squareNumber)));
            if (isInside(posRaton))
                select(posRaton);
        }
    }

    /**
     * Update the game
     * @param tpf Time per frame
     */
    public void onUpdate(double tpf) {
        if (!gameFinished && recordMoves.size() > 1 && clock.decreaseTime(tpf, getCurrentTurn())) {
            gameFinished = true;
            message.setMessage(getFinalMessage());
        }
        updateObservers();
    }

    /**
     * Update the observers of the board (squares, clock and message)
     */
    public void updateObservers() {
        Arrays.stream(squares).flatMap(Arrays::stream).forEach(Observable::notifyObservers);
        clock.notifyObservers();
        message.notifyObservers();
    }

    /**
     * Move the pointer up
     */
    public void goUp() {
        if (!gameFinished) {
            showPuntero = true;
            squares[(int) puntero.x][(int) puntero.y].setPuntero(false);
            if (!promoting && puntero.y > 0
                    || puntero.y > 4 && ladoPromotion == 1
                    || puntero.y > 0 && ladoPromotion == 0)
                puntero.y--;
            squares[(int) puntero.x][(int) puntero.y].setPuntero(true);
        }
    }

    /**
     * Move the pointer down
     */
    public void goDown() {
        if (!gameFinished) {
            showPuntero = true;
            squares[(int) puntero.x][(int) puntero.y].setPuntero(false);
            if (!promoting && puntero.y < Constantes.squareNumber - 1
                    || promoting && puntero.y < Constantes.squareNumber - 1 - 4 && ladoPromotion == 0
                    || promoting && puntero.y < Constantes.squareNumber - 1 && ladoPromotion == 1)
                puntero.y++;
            squares[(int) puntero.x][(int) puntero.y].setPuntero(true);
        }
    }

    /**
     * Move the pointer to the left
     */
    public void goLeft() {
        if (!gameFinished) {
            showPuntero = true;
            squares[(int) puntero.x][(int) puntero.y].setPuntero(false);
            if (puntero.x > 0 && !promoting)
                puntero.x--;
            squares[(int) puntero.x][(int) puntero.y].setPuntero(true);
        }
    }

    /**
     * Move the pointer to the right
     */
    public void goRight() {
        if (!gameFinished) {
            showPuntero = true;
            squares[(int) puntero.x][(int) puntero.y].setPuntero(false);
            if (puntero.x < Constantes.squareNumber - 1 && !promoting)
                puntero.x++;
            squares[(int) puntero.x][(int) puntero.y].setPuntero(true);
        }
    }

    /**
     * Check if a square was moved
     * @param pos Position of the square
     * @return If the square was moved
     */
    public boolean wasMoved(Vec2 pos) {
        return recordMoves.stream().anyMatch(r -> r.getOri().equals(pos));
    }

    /**
     * Check if a position is inside the board
     * @param pos Position
     * @return If the position is inside the board
     */
    private boolean isInside(Vec2 pos) {
        return pos.x >= 0 && pos.x <= Constantes.squareNumber - 1 &&
                pos.y >= 0 && pos.y <= Constantes.squareNumber - 1;
    }

    /**
     * Check if a square's piece is a pawn
     * @param pos Position of the square
     * @return If the square's piece is a pawn
     */
    public boolean isPeon(Vec2 pos) {
        return isInside(pos) && squares[(int) pos.x][(int) pos.y].getPiece() instanceof Pawn;
    }

    /**
     * Check if a square is empty
     * @param pos Position of the square
     * @return If the square is empty
     */
    public boolean isEmpty(Vec2 pos) {
        return isInside(pos) && squares[(int) pos.x][(int) pos.y].getPiece() == null;
    }

    /**
     * Check if a square's piece is an enemy
     * @param pos Position of the square
     * @param lado Side of the piece
     * @return
     */
    public boolean isEnemy(Vec2 pos, int lado) {
        return isInside(pos) && squares[(int) pos.x][(int) pos.y].getPiece() != null &&
                squares[(int) pos.x][(int) pos.y].getPiece().getLado() != lado;
    }

    /**
     * Check if a side is in check
     * @param lado Side
     * @return
     */
    public boolean isInCheck(int lado) {
        return isAtacked(getKing(lado), lado);
    }

    /**
     * Check if position origin and the position destination are the last move
     * @param origin Origin
     * @param destination Destination
     * @return If the position origin and the position destination are the last move
     */
    public boolean isLastJugada(Vec2 origin, Vec2 destination) {
        return recordMoves.get(recordMoves.size() - 1).getOri().equals(origin) && recordMoves.get(recordMoves.size() - 1).getDes().equals(destination);
    }

    /**
     * Check if a position is in attacked by an enemy
     * @param pos Position
     * @param lado Side of the piece
     * @return If a position is in attacked by an enemy
     */
    public boolean isAtacked(Vec2 pos, int lado) {
        List<Vec2> atacks = new ArrayList<Vec2>();
        Arrays.stream(squares)
                .flatMap(Arrays::stream)
                .forEach(square -> {
                    if (isEnemy(square.getPos(), lado))
                        atacks.addAll(square.getPiece().calculateMoves(this, square.getPos(), false));
                });
        return atacks.stream().anyMatch(a -> a.equals(pos));
    }

    /**
     * Setter of the clock time
     * @param time Time
     */
    public void setTime(int time) {
        clock.setTime(time);
    }
}