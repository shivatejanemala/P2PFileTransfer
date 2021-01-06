package peerProcessPackage.messages;

public enum MessageType {
    Choke ((byte) 0),
    Unchoke ((byte) 1),
    Interested ((byte) 2),
    NotInterested ((byte) 3),
    Have ((byte) 4),
    BitField ((byte) 5),
    Request ((byte) 6),
    Piece ((byte) 7);

    private final byte _type;
    
    MessageType (byte type) {
        _type = type;
    }

    public byte getValue() {
        return _type;
    }

    public static MessageType valueOf (byte b) {
        for (MessageType t : MessageType.values()) {
            if (t._type == b) {
                return t;
            }
        }
        throw new IllegalArgumentException();
    }
}
