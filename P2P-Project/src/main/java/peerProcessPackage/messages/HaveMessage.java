package peerProcessPackage.messages;


public class HaveMessage extends ActualMessageOperations {

    HaveMessage (byte[] pieceIndex) {
        super (MessageType.Have, pieceIndex);
    }

    public HaveMessage (int pieceIndex) {
        this (getPieceIndexInBytes (pieceIndex));
    }
}
