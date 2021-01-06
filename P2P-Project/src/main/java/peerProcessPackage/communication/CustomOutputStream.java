package peerProcessPackage.communication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;

import peerProcessPackage.messages.HandshakeMessage;
import peerProcessPackage.messages.PeerMessage;


public class CustomOutputStream extends DataOutputStream implements ObjectOutput {

    public CustomOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void writeObject (Object obj) throws IOException {
        if (obj instanceof HandshakeMessage) {
            ((HandshakeMessage) obj).write(this);
        }
        else if (obj instanceof PeerMessage) {
            ((PeerMessage) obj).write (this);
        }
       
        else {
            throw new UnsupportedOperationException ("Error:- type: " + obj.getClass().getName() + " of message is not supported.");
        }
    }
}
