package peerProcessPackage;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import org.apache.log4j.Logger;

public class ConnectionHandler implements Runnable{

	private static int sPort = 8000;   //The server will be listening on this port number
	static Logger log = Logger.getLogger(DataMessage.class.getName());
   	private String listenMessage;    //message received from the client
   	private String sendMsg;    //uppercase message send to the client
	private ObjectInputStream in;	//stream read from the socket
	private OutputStream out;    //stream write to the socket
	private ServerSocket connection;
	private String clientPeer;
	private int currentPeerID;
	private boolean isActiveConnection;
	private HashMap<String,Socket> connectedPeerMap;
	PeerProcess peerProcess;
	Socket peerSocket;
	
	
	public ConnectionHandler(int serverPort, int currentPeer, PeerProcess peerProcess) {
		this.sPort = serverPort;
		this.currentPeerID = currentPeer;
		this.peerProcess = peerProcess;
		connectedPeerMap = new HashMap<>();
	}
	
	public ConnectionHandler(int serverPort, int currentPeer, String addr, PeerProcess peerProcess) {
		this.sPort = serverPort;
		this.currentPeerID = currentPeer;
		this.peerProcess = peerProcess;
		connectedPeerMap = new HashMap<>();
		
		try {
			this.peerSocket = new Socket(addr, sPort);
		} catch (UnknownHostException e) {
			log.error("Unknown Host Found:-"+e);
		} catch (IOException e) {
			log.error("IO exception while connecting to peer:-"+ e);
		}
	}
	
	public boolean isActiveConnection() {
		return isActiveConnection;
	}

	public void setActiveConnection(boolean isNewConnection) {
		this.isActiveConnection = isNewConnection;
	}

	public boolean sendHandShakeMsg() 
	{
		try 
		{
			HandShakeMessage hsMsg = new HandShakeMessage(DataMessageConstants.HAND_SHAKE_HEADER,Integer.toString(currentPeerID));
			sendMessage(hsMsg.createSendHandshakeMsg());
			log.info("HandShake Request sent from Peer-"+currentPeerID + "to "+ clientPeer);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error( "Error while sending the  SendHandshake to : " + e.getMessage());
		}
		return true;
	}
	
	public boolean receiveHandShakeMsg() 
	{
		try 
		{
			byte[] receiveHandShakeMsg = new byte[DataMessageConstants.HAND_SHAKE_MESSAGE_LENGTH];
			int x = in.read(receiveHandShakeMsg);
			if(x>0) {
				HandShakeMessage hsMsg = HandShakeMessage.createReceiveHandshakeMsg(receiveHandShakeMsg);
				clientPeer = hsMsg.getPeerId().toString();
				log.info("Received HandShake Message from -"+clientPeer);}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(new Date().toInstant()+ "Error while sending the  SendHandshake to : " + e.getMessage());
		}
		return true;
	}

	public boolean sendDataMsg(String type,String payLoad) 
	{
		try 
		{
			DataMessage dataMsg = new DataMessage(type,payLoad);
			sendMessage(dataMsg.createSendDataMsg());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(new Date().toInstant()+ "Error while sending the  SendData Message to : " + e.getMessage());
		}
		return true;
	}
	
	public boolean receiveDataMsg() 
	{
		try 
		{
			byte[] receiveHandShakeMsg = null;
			int x = in.read(receiveHandShakeMsg);
			if(x != DataMessageConstants.HAND_SHAKE_MESSAGE_LENGTH) {
				throw new Exception("Error while receiving the incoming handshake message");
			}
			DataMessage dataMsg = DataMessage.createReceiveDataMsg(receiveHandShakeMsg);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(new Date().toInstant()+ "Error while sending the  SendHandshake to : " + e.getMessage());
		}
		return true;
	}
	
	public boolean sendChokeDataMsg() 
	{
		try 
		{
			DataMessage dataMsg = new DataMessage(Integer.toString(DataMessageConstants.CHOKE_MESSAGE_TYPE),null);
			sendMessage(dataMsg.createSendDataMsg());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(new Date().toInstant()+ "Error while sending the  SendData Message to : " + e.getMessage());
		}
		return true;
	}
	
	public boolean sendUnChokeDataMsg() 
	{
		try 
		{
			DataMessage dataMsg = new DataMessage(Integer.toString(DataMessageConstants.UNCHOKE_MESSAGE_TYPE),null);
			sendMessage(dataMsg.createSendDataMsg());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(new Date().toInstant()+ "Error while sending the  SendData Message to : " + e.getMessage());
		}
		return true;
	}
	public boolean sendInterestedDataMsg() 
	{
		try 
		{
			DataMessage dataMsg = new DataMessage(Integer.toString(DataMessageConstants.INTERESTED_MESSAGE_TYPE),null);
			sendMessage(dataMsg.createSendDataMsg());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(new Date().toInstant()+ "Error while sending the  SendData Message to : " + e.getMessage());
		}
		return true;
	}
	
	public boolean sendIUnnterestedDataMsg() 
	{
		try 
		{
			DataMessage dataMsg = new DataMessage(Integer.toString(DataMessageConstants.UNINTERESTED_MESSAGE_TYPE),null);
			sendMessage(dataMsg.createSendDataMsg());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(new Date().toInstant()+ "Error while sending the  SendData Message to : " + e.getMessage());
		}
		return true;
	}
	
	
	public void run() {
	 		try{
	 			 connection = new ServerSocket(sPort);
				//initialize Input and Output streams
	 			Socket remotePeer = null;
				if(isActiveConnection) {
					try{
						while(true)
						{
							remotePeer = connection.accept();
							out = remotePeer.getOutputStream();
							out.flush();
							in = new ObjectInputStream(remotePeer.getInputStream());
							break;
						}
					}
					catch(Exception e) {
						log.error("Exception occured while communicating For "+currentPeerID+"with peer-"+clientPeer + ":- "+ e.getMessage());
					}
				}
				else {
					
					try 
					{

						in = (ObjectInputStream) peerSocket.getInputStream();
						out = peerSocket.getOutputStream();
					} 
					catch (Exception ex) 
					{
						log.error("Error while creating input/output streams for peer:-"+ currentPeerID +"--"+ ex.getMessage());
					}
				}
				// the peer needs to receive files from others
				if(!isActiveConnection) {
					boolean isValidHandshakeSent = sendHandShakeMsg();
					if(isValidHandshakeSent) {
						while(true) {
								boolean isValidHandShakeRec = receiveHandShakeMsg();
								if(isValidHandShakeRec) {
									log.info("HandShakes Established from -"+ currentPeerID + " to remote Peer-"+ clientPeer);
									connectedPeerMap.put(clientPeer,remotePeer);
									break;
								}
						}
					}
					DataMessage d = new DataMessage(DataMessageConstants.BITFIELD_MESSAGE_TYPE, peerProcess.ownBitField.encode());
				}
				else {
					
				}
	 		}
			catch(IOException ioException){
				log.info("Disconnect with Client " + clientPeer);
			}
			finally{
				//Close connections
				try{
					in.close();
					out.close();
					connection.close();
				}
				catch(IOException ioException){
					log.info("Disconnect with Client " + clientPeer);
				}
			}
		}
	//send a message to the output stream
	public void sendMessage(byte[] msg)
	{
		try {
			out.write(msg);
			out.flush();
			log.info("Send message: " + msg + " to Client " + clientPeer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    }


