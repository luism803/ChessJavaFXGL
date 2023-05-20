package structs;

import com.almasb.fxgl.core.math.Vec2;
import model.pieces.Piece;

/**
 * Class RecordMove
 */
public class RecordMove {
    private Vec2 ori;
    private Piece pieceOri;
    private Vec2 des;
    private Piece pieceDes;

    /**
     * Constructor for RecordMove
     * @param ori Original position
     * @param pieceOri Original piece
     * @param des Destination position
     * @param pieceDes Destination piece
     */
    public RecordMove(Vec2 ori, Piece pieceOri, Vec2 des, Piece pieceDes) {
        this.ori = ori;
        this.pieceOri = pieceOri;
        this.des = des;
        this.pieceDes = pieceDes;
    }

    /**
     * Get the original position
     * @return Original position
     */
    public Vec2 getOri() {
        return ori;
    }

    /**
     * Set the original position
     * @param ori Original position
     */
    public void setOri(Vec2 ori) {
        this.ori = ori;
    }

    /**
     * Get the original piece
     * @return Original piece
     */
    public Piece getPieceOri() {
        return pieceOri;
    }

    /**
     * Set the original piece
     * @param pieceOri Original piece
     */
    public void setPieceOri(Piece pieceOri) {
        this.pieceOri = pieceOri;
    }

    /**
     * Get the destination position
     * @return Destination position
     */
    public Vec2 getDes() {
        return des;
    }

    /**
     * Set the destination position
     * @param des Destination position
     */
    public void setDes(Vec2 des) {
        this.des = des;
    }

    /**
     * Get the destination piece
     * @return Destination piece
     */
    public Piece getPieceDes() {
        return pieceDes;
    }

    /**
     * Set the destination piece
     * @param pieceDes Destination piece
     */
    public void setPieceDes(Piece pieceDes) {
        this.pieceDes = pieceDes;
    }
}
