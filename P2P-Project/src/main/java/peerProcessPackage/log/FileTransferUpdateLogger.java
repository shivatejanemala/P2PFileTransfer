package peerProcessPackage.log;

import java.util.BitSet;

public class FileTransferUpdateLogger {
    private final LoggerConfiguration logger;
    private final String _msgHeader; 

    public FileTransferUpdateLogger (int peerId) {
        this (peerId, LoggerConfiguration.getLogger());
    }

    public FileTransferUpdateLogger (int peerId, LoggerConfiguration logHelper) {
        _msgHeader = ": Peer " + peerId;
        logger = logHelper;
    }

    public void peerConnectSuccessLog (int peerId, boolean isConnectingPeer) {
        final String msg = getLogMsgHeader() + (isConnectingPeer ? " makes a connection to Peer %d." : " is connected from Peer %d.");
        logger.info (String.format (msg, peerId));
    }

    public void chokeMessageLog (int peerId) {
        final String msg = getLogMsgHeader() + " is choked by %d.";
        logger.info (String.format (msg, peerId));
    }

    public void unchokeMessageLog (int peerId) {
        final String msg = getLogMsgHeader() + " is unchoked by %d.";
        logger.info (String.format (msg, peerId));
    }

    public void haveMsgLog (int peerId, int pieceIndex) {
        final String msg = getLogMsgHeader() + " received the 'have' message from %d for the piece %d.";
        logger.info (String.format (msg, peerId, pieceIndex));
    }

    public void interestedMsgLog (int peerId) {
        final String msg = getLogMsgHeader() + " received the 'interested' message from %d.";
        logger.info (String.format (msg, peerId));
    }

    public void notInterestedMsgLog (int peerId) {
        final String msg = getLogMsgHeader() + " received the 'not interested' message from %d.";
        logger.info (String.format (msg, peerId));
    }

    public void prefereedNeighborsUpdateLog (String preferredNeighbors) {
        final String msg = getLogMsgHeader() + " has the preferred neighbors %s";
        logger.info (String.format (msg, preferredNeighbors));
    }

    public void optimisticallyUnchokedNeighborsUpdateLog (String preferredNeighbors) {
        final String msg = getLogMsgHeader() + " has the optimistically unchoked neighbor %s";
        logger.info (String.format (msg, preferredNeighbors));
    }

    
    public void downloadedPieceLog (int peerId, int pieceIndex, int currNumberOfPieces) {
        final String msg = getLogMsgHeader() + " has downloaded the piece %d from peer %d. Now the number of pieces it has is %d.";
        logger.info (String.format (msg, pieceIndex, peerId, currNumberOfPieces));
    }

    public void fileDownloadCompleteLog () {
        final String msg = getLogMsgHeader() + " has downloaded the complete file.";
        logger.info (String.format (msg));
    }

    private String getLogMsgHeader() {
        return (String.format (_msgHeader));
    }
    
    public void bitFieldArrived(int peerId, BitSet bitfield) {
    	String msg = "BitField message arrived from Peer:"+ peerId + "; bitfield data:" + bitfield;
    	logger.info (String.format (msg));
    }
}


