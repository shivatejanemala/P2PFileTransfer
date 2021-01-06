/*
 *                     CEN5501C Project2
 * This is the program starting remote processes.
 * This program was only tested on CISE SunOS environment.
 * If you use another environment, for example, linux environment in CISE 
 * or other environments not in CISE, it is not guaranteed to work properly.
 * It is your responsibility to adapt this program to your running environment.
 */

package peerProcessPackage.configuration;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PeerInfoDAO {
    public final String peerId;
    public final String peerAddr;
    public final String peerPortNo;
    public final boolean hasFile;
    public AtomicInteger noOfBytesDownloaded; // Variable used to identify no of Bytes downloaded from a peer between 2 successive unchokingInterval
    public BitSet receivedParts;
    private final AtomicBoolean isInterested;

    public PeerInfoDAO (int peerId) {
        this.peerId = Integer.toString (peerId);
        this.peerAddr = "127.0.0.1";
        this.peerPortNo = "0";
        this.hasFile = false;
        this.isInterested = new AtomicBoolean(false);
        noOfBytesDownloaded = new AtomicInteger (0);
        receivedParts = new BitSet();
    }

    public PeerInfoDAO(String pId, String pAddress, String pPort, boolean hasFile) {
        peerId = pId;
        peerAddr = pAddress;
        peerPortNo = pPort;
        this.hasFile = hasFile;
        noOfBytesDownloaded = new AtomicInteger (0);
        receivedParts = new BitSet();
        isInterested = new AtomicBoolean (false);
    }

    public int getPeerId() {
        return Integer.parseInt(peerId);
    }

    public int getPort() {
        return Integer.parseInt(peerPortNo);
    }

    public String getPeerAddress() {
        return peerAddr;
    }

    public boolean hasFile() {
        return hasFile;
    }

    public boolean isInterested() {
        return isInterested.get();
    }

    public void setInterested() {
        isInterested.set (true);
    }

    public void setNotIterested() {
        isInterested.set (false);
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof PeerInfoDAO) {
            return (((PeerInfoDAO) obj).peerId.equals (peerId));
        }
        return false;
    }

    public static Collection<Integer> toIdSet (Collection<PeerInfoDAO> peers) {
        Set<Integer> ids = new HashSet<>();
        for (PeerInfoDAO peer : peers) {
            ids.add(peer.getPeerId());
        }
        return ids;
    }
}