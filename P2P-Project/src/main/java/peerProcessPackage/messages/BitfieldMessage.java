package peerProcessPackage.messages;

import java.util.BitSet;

public class BitfieldMessage extends PeerMessage {

    BitfieldMessage (byte[] bitfield) {
        super (MessageType.BitField, bitfield);
    }

    public BitfieldMessage (BitSet bitset) {
        super (MessageType.BitField, bitset.toByteArray());
    }

    public BitSet getBitSet() {
        return BitSet.valueOf (msgPayLoad);
    }
}
