package peerProcessPackage.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

import peerProcessPackage.communication.CustomStreamInterface;


public class HandshakeMessage implements CustomStreamInterface  {
    private final static String handShakeHeader = "P2PFILESHARINGPROJ";
    private final byte[] zeroBits = new byte[10];
    private final byte[] peerId = new byte[4];

    public HandshakeMessage() {
    }

    public HandshakeMessage (int peerId) {
        this (ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(peerId).array());
    }

    private HandshakeMessage (byte[] peerId) {
        if (peerId.length > 4) {
            throw new ArrayIndexOutOfBoundsException("Max length of peerId should be 4. Length of Peer:"+ Arrays.toString (peerId) + " is "+ peerId.length);
        }
        int i = 0;
        for (byte b : peerId) {
            this.peerId[i++] = b;    
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        byte[] peerId = handShakeHeader.getBytes(Charset.forName("US-ASCII"));
        if (peerId.length > handShakeHeader.length()) {
            throw new IOException("peerId length is " + peerId.length + " instead of " + handShakeHeader.length());
        }
        out.write (peerId, 0, peerId.length);
        out.write(zeroBits, 0, zeroBits.length);
        out.write(this.peerId, 0, this.peerId.length);
    }

    @Override
    public void read (DataInputStream in) throws IOException {
        // Read and check protocol Id
        byte[] protocolId = new byte[handShakeHeader.length()];
        if (in.read(protocolId, 0, handShakeHeader.length()) < handShakeHeader.length()) {
            throw new ProtocolException ("protocol id is " + Arrays.toString (protocolId) + " instead of " + handShakeHeader);
        }
        if (!handShakeHeader.equals (new String(protocolId, "US-ASCII"))) {
            throw new ProtocolException ("protocol id is " + Arrays.toString (protocolId) + " instead of " + handShakeHeader);
        }

        // Verify zero bits length
        if (in.read(zeroBits, 0, zeroBits.length) <  zeroBits.length) {
            throw new ProtocolException ("zero bit bytes read are less than " + zeroBits.length);
        }

        // Read and check peer id
        if (in.read(peerId, 0, peerId.length) <  peerId.length) {
            throw new ProtocolException ("peer id bytes read are less than " + peerId.length);
        }
    }

    public int getPeerId() {
        return ByteBuffer.wrap(peerId).order(ByteOrder.BIG_ENDIAN).getInt();
    }
}
