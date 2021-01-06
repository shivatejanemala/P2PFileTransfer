package peerProcessPackage;

import peerProcessPackage.configuration.PeerInfoDAO;
import peerProcessPackage.log.FileTransferUpdateLogger;
import peerProcessPackage.log.LoggerConfiguration;
import peerProcessPackage.messages.ChokeMessage;
import peerProcessPackage.messages.HaveMessage;
import peerProcessPackage.messages.NotInterestedMessage;
import peerProcessPackage.messages.UnchokeMessage;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class FileTransferProcess implements Runnable, FileManagerListenerInterface, PeerManagerListenerInterface {
    private final int peerId; // Used to handle the peerId of the peer
    private final int portNo; // Maintains the port Number of the peer to which others need to be connected
    private final boolean hasFile; // Flag used to set true when peer has the complete file
    private final Properties commonProps; // Properties of the file transfer protocol read from Common.cfg
    private final FileManager fileManager; 
    private final PeerManager peerManager;
    private final FileTransferUpdateLogger ftupdateLogger; // ftupdateLogger used to log the updates in the file transfer protocol for individual peer
    private final AtomicBoolean isfileComplete = new AtomicBoolean(false);  // Flag used to set true when peer has the complete file
    private final AtomicBoolean isNeighboursFileComplete = new AtomicBoolean(false); // Flag used to set true when all the peers has the complete file 
    private final AtomicBoolean isTerminateFlag = new AtomicBoolean(false); // Flag used to set true when peer has recieved the complete file
    private final Collection<CommunicationChannelHandler> communicationChannelHandlers = Collections.newSetFromMap(new ConcurrentHashMap<CommunicationChannelHandler, Boolean>()); //List used to handle the  connections to other peers

    public FileTransferProcess(int peerId, String address, int port, boolean hasFile, Collection<PeerInfoDAO> peerInfo, Properties conf) {
        this.peerId = peerId;
        this.portNo = port;
        this.hasFile = hasFile;
        this.commonProps = conf;    
        this.fileManager = new FileManager(this.peerId, this.commonProps);
        ArrayList<PeerInfoDAO> remotePeers = new ArrayList<>(peerInfo);
        for (PeerInfoDAO ri : remotePeers) {
            if (Integer.parseInt(ri.peerId) == peerId) {
                remotePeers.remove(ri);
                break;
            }
        }
        this.peerManager = new PeerManager(this.peerId, remotePeers, this.fileManager.getBitmapSize(), commonProps);
        this.ftupdateLogger = new FileTransferUpdateLogger(peerId);
        this.isfileComplete.set(this.hasFile);
    }

    /*
    * Function used to initialize the file managers and the peer managers to handle the file transfer and communication between peers
    *
    */
    void initializeManagers() {
    	this.fileManager.registerListener(this);
    	this.peerManager.registerListener(this);

    	// If the peer already has a file, it splits the file to pieces for transferring to other peers
        if (this.hasFile) {
            
            fileManager.splitFile();
            fileManager.setAllPiecesAvailable();
        }
        LoggerConfiguration.getLogger().info("Bitfield initialized for peer succesfully. BitSet data:- " + fileManager.getIsPieceAvailable());
        // Initialize the  PeerManager Thread
        Thread t = new Thread(peerManager);
        t.setName("peerManager- "+ peerId);
        t.start();
    }

    /*
     * Function used to initialize the file transfer process between peers
     *
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(portNo);
            while (!isTerminateFlag.get()) {
                try {
                    LoggerConfiguration.getLogger().info(": Peer:" + peerId + " started listening on port " + portNo + ".");
                    // This is used to accept the connection requests from the peers to this peer
                    addConnectionHandlerToList(new CommunicationChannelHandler(peerId, serverSocket.accept(), fileManager, peerManager));

                } catch (Exception e) {
                    LoggerConfiguration.getLogger().warning(e);
                }
            }
        } catch (IOException ex) {
            LoggerConfiguration.getLogger().warning(ex);
        } finally {
            LoggerConfiguration.getLogger().warning("terminating, TCP connections will no longer be accepted.");
        }
    }

    /*
     * Function used to start the connection from the peer to other peers in the map
     *
     */
    void initializeConnectionToPeers(Collection<PeerInfoDAO> peersToBeConnected) {
        Iterator<PeerInfoDAO> iter = peersToBeConnected.iterator();
        while (iter.hasNext()) {
            do {
                Socket socket = null;
                PeerInfoDAO peer = iter.next();
                try {
                    LoggerConfiguration.getLogger().debug(" Initializing the Connection from peer:"+ peerId+ " to peer: " + peer.getPeerId()+ " (" + peer.peerAddr + ":" + peer.getPort() + ")");
                    socket = new Socket(peer.peerAddr, peer.getPort());
                    // This is used to send the TCP connection requests from this peer to the peers  
                    if (addConnectionHandlerToList(new CommunicationChannelHandler(peerId, true, peer.getPeerId(),socket, fileManager, peerManager))) {
                        iter.remove();
                        LoggerConfiguration.getLogger().debug("Peer: "+ peerId +"makes a connection to Peer: "+ peer.getPeerId());

                    }
                }
                catch (ConnectException ex) {
                    LoggerConfiguration.getLogger().warning("Connection Failed from Peer:" + peerId + " to peer: " + peer.getPeerId()+ " at address " + peer.peerAddr + ":" + peer.getPort());
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (Exception e)
                        {
                        	LoggerConfiguration.getLogger().warning("Socket Close failed at Peer:" + peerId + " Exception:"+ e);
                        }
                    }
                }
                catch (IOException ex) {
                	LoggerConfiguration.getLogger().warning("IO Exception Occured while connecting from peer:" + peerId + " to peer: " + peer.getPeerId()+ " at address " + peer.peerAddr + ":" + peer.getPort() + "\n Exception:"+ ex);
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e)
                        {
                        	LoggerConfiguration.getLogger().warning("Socket Close failed at Peer:" + peerId + " Exception:"+ e);
                        }
                    }
                }
            }
            while (iter.hasNext());
            iter = peersToBeConnected.iterator();
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
        }
    }

    /*
     * Function used to check if all the peers has downloaded the file successfully
     *
     */
    @Override
    public void isNeighbourFileDownloadComplete() {
        LoggerConfiguration.getLogger().info("File Download for All Neighbours is successful");
        isNeighboursFileComplete.set(true);
        if (isfileComplete.get() && isNeighboursFileComplete.get()) { // Check for all peers in the network to download the file successfully
            isTerminateFlag.set(true);
            System.exit(0);
        }
    }

    /*
     * Function used to check if this peer has downloaded the file successfully
     *
     */
    @Override
    public synchronized void isFileDownloadComplete() {

        ftupdateLogger.fileDownloadCompleteLog();
        isfileComplete.set(true);
        if (isfileComplete.get() && isNeighboursFileComplete.get()) { // Check for all peers in the network to download the file successfully
            isTerminateFlag.set(true);
            System.exit(0);
        }
    }
    /*
     * Function used to check if we have received a piece from the peer
     *
     */
    @Override
    public synchronized void isPieceArrived(int pieceIndex) {
    	// Once a piece has been received, 'HAVE' message for the piece will be sent to all the peers
        for (CommunicationChannelHandler connectionHandler : communicationChannelHandlers) {
            connectionHandler.send(new HaveMessage(pieceIndex));
            if (!peerManager.isInteresting(connectionHandler.getRemotePeerId(), fileManager.getReceivedParts())) {
                connectionHandler.send(new NotInterestedMessage());
            }
        }
    }

    /*
     * Function used to start the connection handler for communicating with other peers and store them in a list
     *
     */
    private synchronized boolean addConnectionHandlerToList(CommunicationChannelHandler connectionHandler) {
        if (!communicationChannelHandlers.contains(connectionHandler)) {
            communicationChannelHandlers.add(connectionHandler);
            new Thread(connectionHandler).start();
            try {
                wait(25);
            } catch (InterruptedException e) {
                LoggerConfiguration.getLogger().warning("Error occured while starting the connection Handler- " + e);
            }

        }
        else {
            LoggerConfiguration.getLogger().debug("Peer: " + connectionHandler.getRemotePeerId() + " is already connected");
        }
        return true;
    }

    /*
     * Function used to send the CHOKE message to peers if the peer is determined to be choked by PeerManager
     *
     */
    @Override
    public synchronized void chockedPeers(Collection<Integer> chokedPeersIds) {
        for (CommunicationChannelHandler ch : communicationChannelHandlers) {
            if (chokedPeersIds.contains(ch.getRemotePeerId())) {
                
                ch.send(new ChokeMessage());
            }
        }
    }

    /*
     * Function used to send the UNCHOKE message to peers if the peer is determined to be unchoked by PeerManager
     *
     */
    
    @Override
    public synchronized void unchockedPeers(Collection<Integer> unchokedPeersIds) {
        for (CommunicationChannelHandler handler : communicationChannelHandlers) {
            if (unchokedPeersIds.contains(handler.getRemotePeerId())) {
                handler.send(new UnchokeMessage());
            }
        }
    }
}
