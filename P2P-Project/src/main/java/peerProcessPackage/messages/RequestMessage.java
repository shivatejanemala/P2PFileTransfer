package peerProcessPackage.messages;


public class RequestMessage extends ActualMessageOperations {

    RequestMessage (byte[] pieceIndex) {
        super (MessageType.Request, pieceIndex);
    }

    public RequestMessage (int pieceIndex) {
        super (MessageType.Request, getPieceIndexInBytes (pieceIndex));
    }
}
