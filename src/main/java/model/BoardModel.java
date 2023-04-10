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
    private boolean coronando;
    private boolean mostrarPuntero;
    private int ladoCoronacion;
    private Vec2 puntero;
    private Vec2 seleccion;
    private SquareModel[][] squareModels;
    private SquareModel squareSelection;
    private Vec2[] positions;
    private List<Vec2> moves;
    private List<RecordMove> recordMoves;
    private Piece[] actualPieces;

    public BoardModel(Vec2 puntero) {
        this.puntero = puntero;
        mostrarPuntero = true;
        coronando = false;
        ladoCoronacion = 0;
        seleccion = null;
        squareModels = new SquareModel[Constantes.squareNumber][Constantes.squareNumber];
        squareSelection = null;
        moves = new ArrayList<>();
        positions = new Vec2[3];
        actualPieces = new Piece[3];
        recordMoves = new ArrayList<>();
        for (int x = 0; x < Constantes.squareNumber; x++) {
            for (int y = 0; y < Constantes.squareNumber; y++) {
                squareModels[x][y] = new SquareModel(x, y);
            }
        }
        colocarPiezas();
    }

    public BoardModel() {
        this(new Vec2());
    }

    public void updateObservers() {
        Arrays.stream(squareModels)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(e2 -> e2.notifyObservers()));
    }

    public SquareModel[][] getSquareModels() {
        return squareModels;
    }

    private int getCurrentTurn() {
        return (recordMoves.size() == 0 || recordMoves.get(recordMoves.size() - 1).getPieceOri().getLado() == 1) ? 0 : 1;
    }

    private Vec2 getRey(int lado) {
        AtomicReference<Vec2> res = new AtomicReference<>();
        Arrays.stream(squareModels)
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
        if (!coronando && recordMoves.size() > 0) {
            RecordMove lastRecord = recordMoves.get(recordMoves.size() - 1);
            squareModels[(int) lastRecord.getOri().x][(int) lastRecord.getOri().y].setPiece(lastRecord.getPieceOri());
            squareModels[(int) lastRecord.getDes().x][(int) lastRecord.getDes().y].setPiece(lastRecord.getPieceDes());
            recordMoves.remove(recordMoves.size() - 1);
            //SI HUBO PEON PASADO
            if (lastRecord.getPieceOri() instanceof Pawn && lastRecord.getPieceDes() == null
                    && lastRecord.getOri().x != lastRecord.getDes().x)
                squareModels[(int) lastRecord.getDes().x][(int) lastRecord.getOri().y].setPiece(new Pawn(lastRecord.getPieceOri().getLado() * (-1) + 1));
            //SI HUBO ENROQUE
            if (lastRecord.getPieceOri() instanceof King && Math.abs(lastRecord.getOri().x - lastRecord.getDes().x) == 2) {
                //DERECHA
                if (lastRecord.getOri().x - lastRecord.getDes().x < 0) {
                    squareModels[(int) lastRecord.getDes().x + 1][(int) lastRecord.getDes().y].setPiece(new Rook(lastRecord.getPieceOri().getLado()));
                    squareModels[(int) lastRecord.getDes().x - 1][(int) lastRecord.getDes().y].setPiece(null);
                }
                //IZQUIERDA
                else {
                    squareModels[(int) lastRecord.getDes().x - 2][(int) lastRecord.getDes().y].setPiece(new Rook(lastRecord.getPieceOri().getLado()));
                    squareModels[(int) lastRecord.getDes().x + 1][(int) lastRecord.getDes().y].setPiece(null);
                }
            }
        }
    }

    private void colocarPiezas() {
        //PAWNS
        for (int i = 0; i < 8; i++) {
            squareModels[i][1].setPiece(new Pawn(1));
            squareModels[i][6].setPiece(new Pawn(0));
        }
        //ROOKS
        squareModels[7][7].setPiece(new Rook(0));
        squareModels[0][7].setPiece(new Rook(0));
        squareModels[7][0].setPiece(new Rook(1));
        squareModels[0][0].setPiece(new Rook(1));
        //BISHOPS
        squareModels[2][7].setPiece(new Bishop(0));
        squareModels[5][7].setPiece(new Bishop(0));
        squareModels[2][0].setPiece(new Bishop(1));
        squareModels[5][0].setPiece(new Bishop(1));
        //KNIGHTS
        squareModels[1][7].setPiece(new Knight(0));
        squareModels[6][7].setPiece(new Knight(0));
        squareModels[1][0].setPiece(new Knight(1));
        squareModels[6][0].setPiece(new Knight(1));
        //QUEENS
        squareModels[3][7].setPiece(new Queen(0));
        squareModels[3][0].setPiece(new Queen(1));
        //KINGS
        squareModels[4][7].setPiece(new King(0));
        squareModels[4][0].setPiece(new King(1));
    }

    private void movePiece() { //se puede llamar a la funion copia
        movePiece(puntero, seleccion);
        quitarSeleccion();
    }

    public void movePiece(Vec2 des, Vec2 ori) {
        //GUARDAR JUGADA EN EL REGISTRO
        Piece pieceOri = null;
        Piece pieceDes = null;
        if (squareModels[(int) ori.x][(int) ori.y].getPiece() != null)
            pieceOri = squareModels[(int) ori.x][(int) ori.y].getPiece().copy();
        if (squareModels[(int) des.x][(int) des.y].getPiece() != null)
            pieceDes = squareModels[(int) des.x][(int) des.y].getPiece().copy();
        RecordMove recordMove = new RecordMove(ori, pieceOri, des, pieceDes);
        recordMoves.add(recordMove);
        //PEON PASADO
        if (recordMove.getPieceOri() instanceof Pawn && recordMove.getPieceDes() == null
                && recordMove.getOri().x != recordMove.getDes().x)    //si el peon esta comiendo un peon pasado
            squareModels[(int) recordMove.getDes().x][(int) recordMove.getOri().y].setPiece(null);  //quitar el peon comido
        //ENROQUE
        if (recordMove.getPieceOri() instanceof King && Math.abs(recordMove.getOri().x - recordMove.getDes().x) == 2) { //si el rey se esta moviendo dos casillas a la derecha o izquierda
            if (recordMove.getOri().x - recordMove.getDes().x < 0) {  //DERECHA
                squareModels[(int) recordMove.getDes().x + 1][(int) recordMove.getDes().y].setPiece(null);
                squareModels[(int) recordMove.getDes().x - 1][(int) recordMove.getDes().y].setPiece(new Rook(recordMove.getPieceOri().getLado()));
            } else {  ////IZQUIERDA
                squareModels[(int) recordMove.getDes().x - 2][(int) recordMove.getDes().y].setPiece(null);
                squareModels[(int) recordMove.getDes().x + 1][(int) recordMove.getDes().y].setPiece(new Rook(recordMove.getPieceOri().getLado()));
            }
        }
        //MOVER
        squareModels[(int) des.x][(int) des.y].setPiece(pieceOri);
        squareModels[(int) ori.x][(int) ori.y].setPiece(null);
    }

    public void quitarSeleccion() {
        seleccion = null;
        squareSelection = null;
    }

    public void seleccionar() { //FALTA CREAR UNA SOBRECARGA QUE RECIBA EL PUNTERO (Vec2) si se mete jugar con el raton
        seleccionar(puntero);
    }

    private void seleccionar(Vec2 pos) { //FALTA CREAR UNA SOBRECARGA QUE RECIBA EL PUNTERO (Vec2) si se mete jugar con el raton
        if (!coronando) {
            //si hay seleccion
            if (seleccion != null && moves.stream().anyMatch(m -> m.equals(pos))) { //si se apunta hacia una casilla que es una jugada
                puntero = pos.copy();
                movePiece();   //mover la pieza seleecionada hacia la casilla apuntada
            } else {
                puntero = pos.copy();
                seleccion = pos.copy();  //actualizar la seleccion (Vec2)
                squareSelection = squareModels[(int) seleccion.x][(int) seleccion.y];  //actualizar la casilla de seleccion
                if (squareSelection.getPiece() == null               //si la casilla seleccionada esta vacia
                        || squareSelection.getPiece().getLado() != getCurrentTurn())    //o casilla seleccionada no es del color correspondiente
                    quitarSeleccion();
            }

        } else {    //se elige la pieza de coronacion
            if (Arrays.asList(positions).contains(pos) ||   //la pieza seleccionada este en una de las positions guardadas
            pos.equals(new Vec2(positions[0].x, positions[0].y * 2 - positions[1].y)))
            {   //la pieza seleccionada es la reina
                squareModels[(int)positions[0].x][(int)(positions[0].y * 2 - positions[1].y)].setPiece(squareModels[(int)pos.x][(int)pos.y].getPiece());
                squareModels[(int)positions[0].x][(int)positions[0].y].setPiece(actualPieces[0]);
                squareModels[(int)positions[1].x][(int)positions[1].y].setPiece(actualPieces[1]);
                squareModels[(int)positions[2].x][(int)positions[2].y].setPiece(actualPieces[2]);
                coronando = false;
            }
        }
        //CORONACION
        Vec2 pawnPromoted = isPawnPromotionPossible();
        if (pawnPromoted != null) {
            coronando = true;
            elegirCoronacion(pawnPromoted);
        }
        //save moves
        if (!coronando)
            moves.clear();    //vaciar las moves posibles
        if (seleccion != null) {    //si hay seleccionada una casilla
            if (squareModels[(int) seleccion.x][(int) seleccion.y].getPiece() != null) { //si hay una ficha en la casilla seleccionada
                moves = squareModels[(int) seleccion.x][(int) seleccion.y].getPiece().calculateMoves(this, seleccion, true);  //guardar las moves posibles de esa jugada
            }
        }
    }

    private void elegirCoronacion(Vec2 pawnPromoted) {
        ladoCoronacion = 0;
        //blancas
        if (squareModels[(int) pawnPromoted.x][(int) pawnPromoted.y].getPiece().getLado() == 0) {
            //posiciones
            positions[0] = new Vec2(pawnPromoted.x, pawnPromoted.y + 1);
            positions[1] = new Vec2(pawnPromoted.x, pawnPromoted.y + 2);
            positions[2] = new Vec2(pawnPromoted.x, pawnPromoted.y + 3);
            ladoCoronacion = 0;
        } else {
            //positions
            positions[0] = new Vec2(pawnPromoted.x, pawnPromoted.y - 1);
            positions[1] = new Vec2(pawnPromoted.x, pawnPromoted.y - 2);
            positions[2] = new Vec2(pawnPromoted.x, pawnPromoted.y - 3);
            ladoCoronacion = 1;
        }
        //guardar fichas actuales
        actualPieces[0] = squareModels[(int) positions[0].x][(int) positions[0].y].getPiece();
        actualPieces[1] = squareModels[(int) positions[1].x][(int) positions[1].y].getPiece();
        actualPieces[2] = squareModels[(int) positions[2].x][(int) positions[2].y].getPiece();
        //Mostrar opciones
        squareModels[(int) pawnPromoted.x][(int) pawnPromoted.y].setPiece(new Queen(ladoCoronacion));
        squareModels[(int) positions[0].x][(int) positions[0].y].setPiece(new Knight(ladoCoronacion));
        squareModels[(int) positions[1].x][(int) positions[1].y].setPiece(new Rook(ladoCoronacion));
        squareModels[(int) positions[2].x][(int) positions[2].y].setPiece(new Bishop(ladoCoronacion));
        //añadir opciones a jugadas
        moves.add(pawnPromoted);
        moves.add(positions[0]);
        moves.add(positions[1]);
        moves.add(positions[2]);
    }

    private Vec2 isPawnPromotionPossible() {
        AtomicReference<Vec2> res = new AtomicReference<>();
        Arrays.stream(squareModels)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(square -> {
                            if (square.getPiece() instanceof Pawn && (square.getPos().y == 0 || square.getPos().y == Constantes.squareNumber - 1))
                                res.set(square.getPos());
                        }));
        return res.get();
    }

    public void seleccionarRaton(Vec2 pos) {
        seleccionarRaton(pos, true);
    }

    public void seleccionarRaton(Vec2 pos, boolean normal) {
        if (normal || !coronando) {
            mostrarPuntero = false;
            //ajustar posicion raton
            Vec2 posRaton = new Vec2((int) (pos.x / (Constantes.height / Constantes.squareNumber)), (int) (pos.y / (Constantes.height / Constantes.squareNumber)));
            if (isInside(posRaton))
                seleccionar(posRaton);
        }
    }

    public void onUpdate() {
        //update squares color
        Arrays.stream(squareModels)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(e2 -> {
                            e2.setPuntero(mostrarPuntero && e2.getPos().equals(puntero));
                            e2.setSeleccion(e2.getPos().equals(seleccion));
                            e2.setJugada(moves.stream()
                                    .anyMatch(m -> m.equals(e2.getPos())));
                        }));
        updateObservers();
    }

    public void goUp() {
        mostrarPuntero = true;
        if (!coronando && puntero.y > 0
                || puntero.y > 4 && ladoCoronacion == 1
                || puntero.y > 0 && ladoCoronacion == 0)
            puntero.y--;
    }

    public void goDown() {
        mostrarPuntero = true;
        if (!coronando && puntero.y < Constantes.squareNumber - 1
                || coronando && puntero.y < Constantes.squareNumber - 1 - 4 && ladoCoronacion == 0
                || coronando && puntero.y < Constantes.squareNumber - 1 && ladoCoronacion == 1)
            puntero.y++;
    }

    public void goLeft() {
        mostrarPuntero = true;
        if (puntero.x > 0 && !coronando)
            puntero.x--;
    }

    public void goRight() {
        mostrarPuntero = true;
        if (puntero.x < Constantes.squareNumber - 1 && !coronando)
            puntero.x++;
    }

    public boolean wasMoved(Vec2 pos) {
        return recordMoves.stream().anyMatch(r -> r.getOri().equals(pos));
    }

    private boolean isInside(Vec2 pos) {
        return pos.x >= 0 && pos.x <= Constantes.squareNumber - 1 &&
                pos.y >= 0 && pos.y <= Constantes.squareNumber - 1;
    }

    public boolean isPeon(Vec2 pos) {
        return isInside(pos) && squareModels[(int) pos.x][(int) pos.y].getPiece() instanceof Pawn;
    }

    public boolean isEmpty(Vec2 pos) {
        return isInside(pos) && squareModels[(int) pos.x][(int) pos.y].getPiece() == null;
    }

    public boolean isEnemy(Vec2 pos, int lado) {
        return isInside(pos) && squareModels[(int) pos.x][(int) pos.y].getPiece() != null &&
                squareModels[(int) pos.x][(int) pos.y].getPiece().getLado() != lado;
    }

    public boolean isInCheck(int lado) {
        return isAtacked(getRey(lado), lado);
    }

    public boolean isLastJugada(Vec2 origin, Vec2 destination) {
        return recordMoves.get(recordMoves.size() - 1).getOri().equals(origin) && recordMoves.get(recordMoves.size() - 1).getDes().equals(destination);
    }

    public boolean isAtacked(Vec2 pos, int lado) {
        List<Vec2> atacks = new ArrayList<Vec2>();
        Arrays.stream(squareModels)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(square -> {
                            if (isEnemy(square.getPos(), lado))
                                atacks.addAll(square.getPiece().calculateMoves(this, square.getPos(), false));
                        }));
        return atacks.stream().anyMatch(a -> a.equals(pos));
    }
}
