package peerProcessPackage.messages;

import java.util.Arrays;


public class PieceMessage extends ActualMessageOperations {

    PieceMessage (byte[] msgPayLoad) {
        super (MessageType.Piece, msgPayLoad);
    }

    public PieceMessage (int pieceIndex, byte[] msgContent) {
        super (MessageType.Piece, merge (pieceIndex, msgContent));
    }

    public byte[] getMsgContent() {
        if ((msgPayLoad == null) || (msgPayLoad.length <= 4)) {
            return null;
        }
        return Arrays.copyOfRange(msgPayLoad, 4, msgPayLoad.length);
    }

    private static byte[] merge (int pieceIndex, byte[] msgContent) { 
        byte[] result = new byte[4 + (msgContent == null ? 0 : msgContent.length)];
        System.arraycopy(getPieceIndexInBytes (pieceIndex), 0, result, 0, 4);
        System.arraycopy(msgContent, 0, result, 4, msgContent.length);
        return result;
    }
}
