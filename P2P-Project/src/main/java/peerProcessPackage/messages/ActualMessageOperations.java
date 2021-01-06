package peerProcessPackage.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ActualMessageOperations extends PeerMessage 
{
    protected ActualMessageOperations (MessageType type, byte[] payload) {
        super (type, payload);
    }

    
    protected static byte[] getPieceIndexInBytes (int pieceIndex) {
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(pieceIndex).array();
    }
    public int getPieceIndex() {
        return ByteBuffer.wrap(Arrays.copyOfRange(msgPayLoad, 0, 4)).order(ByteOrder.BIG_ENDIAN).getInt();
    }
}
