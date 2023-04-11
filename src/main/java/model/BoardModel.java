package model;

import com.almasb.fxgl.core.math.Vec2;
import model.pieces.*;
import structs.RecordMove;
import utils.Constantes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BoardModel {
    private boolean promoting;
    private boolean showPuntero;
    private int ladoPromotion;
    private Vec2 puntero;
    private Vec2 seleccion;
    private SquareModel[][] squares;
    private SquareModel squareSelection;
    private Vec2[] positions;
    private List<Vec2> moves;
    private List<RecordMove> recordMoves;
    private Piece[] actualPieces;

    private ClockModel clock;

    public BoardModel(Vec2 puntero, double time) {
        this.puntero = puntero;
        showPuntero = true;
        promoting = false;
        ladoPromotion = 0;
        seleccion = null;
        squares = new SquareModel[Constantes.squareNumber][Constantes.squareNumber];
        squareSelection = null;
        moves = new ArrayList<>();
        positions = new Vec2[3];
        actualPieces = new Piece[3];
        recordMoves = new ArrayList<>();
        clock = new ClockModel(time);
        for (int x = 0; x < Constantes.squareNumber; x++) {
            for (int y = 0; y < Constantes.squareNumber; y++) {
                squares[x][y] = new SquareModel(x, y);
            }
        }
        colocarPiezas();
    }

    public BoardModel(double time) {
        this(new Vec2(), time);
    }

    public void updateObservers() {
        Arrays.stream(squares)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(e2 -> e2.notifyObservers()));
        clock.notifyObservers();
    }

    public SquareModel[][] getSquare() {
        return squares;
    }

    public ClockModel getClock() {
        return clock;
    }

    private int getCurrentTurn() {
        if(promoting) {
            return ladoPromotion;
        }return (recordMoves.size() == 0 || recordMoves.get(recordMoves.size() - 1).getPieceOri().getLado() == 1) ? 0 : 1;
    }

    private Vec2 getKing(int lado) {
        AtomicReference<Vec2> res = new AtomicReference<>();
        Arrays.stream(squares)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(square -> {
                            if (!isEnemy(square.getPos(), lado) && square.getPiece() instanceof King)
                                res.set(square.getPos());
                        }));
        return res.get();
    }

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

    private void colocarPiezas() {
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

    private boolean detectChecks() {
        if (!promoting) {
            return isInCheck(getCurrentTurn());
        } else
            return false;
    }

//    private boolean detectCheck(int currentTurn) {
//
//        //return JugadasPosibles(currentTurn).Count == 0;
//    }

    private void movePiece() { //se puede llamar a la funion copia
        movePiece(puntero, seleccion);
        quitarSeleccion();
    }

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

    public void quitarSeleccion() {
        seleccion = null;
        squareSelection = null;
        moves.clear();
    }

    public void seleccionar() { //FALTA CREAR UNA SOBRECARGA QUE RECIBA EL PUNTERO (Vec2) si se mete jugar con el raton
        seleccionar(puntero);
    }

    private void seleccionar(Vec2 pos) { //FALTA CREAR UNA SOBRECARGA QUE RECIBA EL PUNTERO (Vec2) si se mete jugar con el raton
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
                    quitarSeleccion();
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
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(e2 -> {
                            e2.setPuntero(showPuntero && e2.getPos().equals(puntero));
                            e2.setSeleccion(e2.getPos().equals(seleccion));
                            e2.setJugada((e2.getPiece() instanceof King && e2.getPiece().getLado() == getCurrentTurn() && isInCheck(getCurrentTurn()))
                                    || moves.stream()
                                    .anyMatch(m -> m.equals(e2.getPos())));
                        }));
        //CHECK STATUS GAME
        if (!promoting && posiblesMoves(getCurrentTurn()).size() == 0) {
            if (detectChecks())
                System.out.println(((getCurrentTurn() == 0) ? "BLACK" : "WHITE") + " WINS!!!");
            else
                System.out.println("STALE MATE");
        }
    }

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

    private Vec2 pawnPromoted() {
        AtomicReference<Vec2> res = new AtomicReference<>();
        Arrays.stream(squares)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(square -> {
                            if (square.getPiece() instanceof Pawn && (square.getPos().y == 0 || square.getPos().y == Constantes.squareNumber - 1))
                                res.set(square.getPos());
                        }));
        return res.get();
    }

    private List<Vec2> posiblesMoves(int lado) {
        List<Vec2> moves = new ArrayList<>();
        Arrays.stream(squares)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(square -> {
                            if (square.getPiece() != null && square.getPiece().getLado() == lado)
                                moves.addAll(square.getPiece().calculateMoves(this, square.getPos(), true));
                        }));
        return moves;
    }

    public void seleccionarRaton(Vec2 pos) {
        seleccionarRaton(pos, true);
    }

    public void seleccionarRaton(Vec2 pos, boolean normal) {
        if (normal || !promoting) {
            showPuntero = false;
            //ajustar posicion raton
            Vec2 posRaton = new Vec2((int) (pos.x / (Constantes.height / Constantes.squareNumber)), (int) (pos.y / (Constantes.height / Constantes.squareNumber)));
            if (isInside(posRaton))
                seleccionar(posRaton);
        }
    }

    public void onUpdate(double tpf) {
        if (recordMoves.size() > 1)
            clock.decreaseTime(tpf, getCurrentTurn());
        updateObservers();
    }

    public void goUp() {
        showPuntero = true;
        squares[(int)puntero.x][(int)puntero.y].setPuntero(false);
        if (!promoting && puntero.y > 0
                || puntero.y > 4 && ladoPromotion == 1
                || puntero.y > 0 && ladoPromotion == 0)
            puntero.y--;
        squares[(int)puntero.x][(int)puntero.y].setPuntero(true);
    }

    public void goDown() {
        showPuntero = true;
        squares[(int)puntero.x][(int)puntero.y].setPuntero(false);
        if (!promoting && puntero.y < Constantes.squareNumber - 1
                || promoting && puntero.y < Constantes.squareNumber - 1 - 4 && ladoPromotion == 0
                || promoting && puntero.y < Constantes.squareNumber - 1 && ladoPromotion == 1)
            puntero.y++;
        squares[(int)puntero.x][(int)puntero.y].setPuntero(true);
    }

    public void goLeft() {
        showPuntero = true;
        squares[(int)puntero.x][(int)puntero.y].setPuntero(false);
        if (puntero.x > 0 && !promoting)
            puntero.x--;
        squares[(int)puntero.x][(int)puntero.y].setPuntero(true);
    }

    public void goRight() {
        showPuntero = true;
        squares[(int)puntero.x][(int)puntero.y].setPuntero(false);
        if (puntero.x < Constantes.squareNumber - 1 && !promoting)
            puntero.x++;
        squares[(int)puntero.x][(int)puntero.y].setPuntero(true);
    }

    public boolean wasMoved(Vec2 pos) {
        return recordMoves.stream().anyMatch(r -> r.getOri().equals(pos));
    }

    private boolean isInside(Vec2 pos) {
        return pos.x >= 0 && pos.x <= Constantes.squareNumber - 1 &&
                pos.y >= 0 && pos.y <= Constantes.squareNumber - 1;
    }

    public boolean isPeon(Vec2 pos) {
        return isInside(pos) && squares[(int) pos.x][(int) pos.y].getPiece() instanceof Pawn;
    }

    public boolean isEmpty(Vec2 pos) {
        return isInside(pos) && squares[(int) pos.x][(int) pos.y].getPiece() == null;
    }

    public boolean isEnemy(Vec2 pos, int lado) {
        return isInside(pos) && squares[(int) pos.x][(int) pos.y].getPiece() != null &&
                squares[(int) pos.x][(int) pos.y].getPiece().getLado() != lado;
    }

    public boolean isInCheck(int lado) {
        return isAtacked(getKing(lado), lado);
    }

    public boolean isLastJugada(Vec2 origin, Vec2 destination) {
        return recordMoves.get(recordMoves.size() - 1).getOri().equals(origin) && recordMoves.get(recordMoves.size() - 1).getDes().equals(destination);
    }

    public boolean isAtacked(Vec2 pos, int lado) {
        List<Vec2> atacks = new ArrayList<Vec2>();
        Arrays.stream(squares)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(square -> {
                            if (isEnemy(square.getPos(), lado))
                                atacks.addAll(square.getPiece().calculateMoves(this, square.getPos(), false));
                        }));
        return atacks.stream().anyMatch(a -> a.equals(pos));
    }

    public void setTime(int time) {
        clock.setTime(time);
    }
}
