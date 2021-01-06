package peerProcessPackage.messages;

public class ChokeMessage extends PeerMessage {

    public ChokeMessage() {
        super (MessageType.Choke);
    }
}
