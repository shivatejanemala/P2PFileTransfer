package peerProcessPackage.communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;

import peerProcessPackage.messages.HandshakeMessage;
import peerProcessPackage.messages.MessageType;
import peerProcessPackage.messages.PeerMessage;


public class CustomInputStream extends DataInputStream implements ObjectInput {

    private boolean isHandShakeReceived = false; // "true" for actual message ,"false" for handshake message

    public CustomInputStream(InputStream in) {
        super(in);
    }

    @Override
    public Object readObject() throws ClassNotFoundException, IOException {
        if (isHandShakeReceived) { // Check if the message to be read is handshake or actual message
            final int length = readInt();
            final int payloadLength = length - 1; // subtract 1 for the message type
            PeerMessage message = PeerMessage.getNewMessageObject(payloadLength, MessageType.valueOf (readByte()));
            message.read(this);
            return message;
        }
        else {
            HandshakeMessage handshake = new HandshakeMessage();
            handshake.read(this);
            isHandShakeReceived = true;
            return handshake;
        }
    }
}
