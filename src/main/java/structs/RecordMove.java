package structs;

import com.almasb.fxgl.core.math.Vec2;
import model.pieces.Piece;

public class RecordMove {
    private Vec2 ori;
    private Piece pieceOri;
    private Vec2 des;
    private Piece pieceDes;

    public RecordMove(Vec2 ori, Piece pieceOri, Vec2 des, Piece pieceDes) {
        this.ori = ori;
        this.pieceOri = pieceOri;
        this.des = des;
        this.pieceDes = pieceDes;
    }

    public Vec2 getOri() {
        return ori;
    }

    public void setOri(Vec2 ori) {
        this.ori = ori;
    }

    public Piece getPieceOri() {
        return pieceOri;
    }

    public void setPieceOri(Piece pieceOri) {
        this.pieceOri = pieceOri;
    }

    public Vec2 getDes() {
        return des;
    }

    public void setDes(Vec2 des) {
        this.des = des;
    }

    public Piece getPieceDes() {
        return pieceDes;
    }

    public void setPieceDes(Piece pieceDes) {
        this.pieceDes = pieceDes;
    }
}
