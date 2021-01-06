package peerProcessPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

public class PeerProcess {
	static Logger log = null;
	public static int peerId;
	public static HashMap<String,PeerDataInfo> peerInfoMap = new HashMap<String,PeerDataInfo>();
	public static String localPort;
	public static boolean isFirst;
	public static BitField currentPeerBitField;
	public static boolean hasFile;
	public static volatile Queue<DataMessageDetails> messageQ = new LinkedList<DataMessageDetails>();
	
	public static void initializeCommonConfiguration() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader("common.cfg"));
			String line;
			while((line=buffer.readLine())!= null) {
				String[] props = line.split("\\s+");
				if(props[0].equalsIgnoreCase("NumberOfPreferredNeighbors")) {
					CommonConfigurationDAO.NumberOfPreferredNeighbors = Integer.parseInt(props[1]);
				}
				else if (props[0].equalsIgnoreCase("UnchokingInterval")) {
					CommonConfigurationDAO.UnchokingInterval = Integer.parseInt(props[1]);
				}
				else if (props[0].equalsIgnoreCase("OptimisticUnchokingInterval")) {
					CommonConfigurationDAO.OptimisticUnchokingInterval = Integer.parseInt(props[1]);
				}
				else if (props[0].equalsIgnoreCase("FileName")) {
					CommonConfigurationDAO.FileName = props[1];
				}
				else if (props[0].equalsIgnoreCase("FileSize")) {
					CommonConfigurationDAO.FileSize = Integer.parseInt(props[1]);
				}
				else if (props[0].equalsIgnoreCase("PieceSize")) {
					CommonConfigurationDAO.PieceSize = Integer.parseInt(props[1]);
				}
				else {
					throw new Exception("Error in the common configuration Properties");
				}
			}
			log.info(new Date().toInstant()+ ": Successfully set the common properties");
		} catch (IOException e) {
			log.info(": Exception occured while setting the common configuration properties:"+e.getMessage());
		}
		catch (Exception e) {
			log.info(": Exception occured while setting the common configuration properties:"+e.getMessage());
		}
	}
	
	public static synchronized void addMsgToQueue(DataMessageDetails msg)
	{
		messageQ.add(msg);
	}
	
	public static synchronized DataMessageDetails retrieveMsgFromQueue()
	{
		DataMessageDetails msg = null;
		if(!messageQ.isEmpty())
		{
			msg = messageQ.remove();
		}
		return msg;
	}

	
	public static void main(String[] args) {

		if (args.length <= 0) {
			log.error(": Required Arguments for starting the peer are not provided");
			System.exit(1);
		}
		
		peerId = Integer.parseInt(args[0]);
		log = Logger.getLogger(Integer.toString(peerId));
		initializeCommonConfiguration();	
		log.info(":Initializing the peer-" + peerId);
		IdentifyPeersinNetwork();
		setCurrentPeerBitField();
		Thread messageHandler = new Thread(new MessageHandler(Integer.toString(peerId)));
		messageHandler.start();
		isFirst = false;
		Set<String> peerIDSet = peerInfoMap.keySet();
		Iterator<String> it = peerIDSet.iterator();
		while (it.hasNext()) {
			String id = it.next();
			if (Integer.parseInt(id) == peerId) {
				PeerDataInfo peerData = peerInfoMap.get(id);
				localPort = peerData.peerPort;
				hasFile = peerData.getHasFile()==1?true:false;
				/*if (peerData.getPeerCount() == 1) {
					isFirst = true;
				}*/
			}
		}
		PeerProcess peerProcess = new PeerProcess();
		
		if(hasFile) {
			ConnectionHandler listener = new ConnectionHandler(Integer.parseInt(localPort),peerId,peerProcess);
			listener.setActiveConnection(true);
			new Thread(listener).start();
		}
		else {
			ConnectionHandler listener = new ConnectionHandler(Integer.parseInt(localPort),peerId,peerInfoMap.get(peerId).getPeerAddress(),peerProcess);
			new Thread(listener).start();
		}
	}


	private static void setCurrentPeerBitField() {
		// TODO Auto-generated method stub
		currentPeerBitField = new BitField();
	}


	private static void IdentifyPeersinNetwork() {
		// TODO Auto-generated method stub
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader("PeerInfo.cfg"));
			int i =1;
			String st ;
			while((st = in.readLine()) != null) 
			{	
				 String[] props = st.split("\\s+");
				 PeerDataInfo peerData = new PeerDataInfo(props[0], props[1], props[2], i);
				 peerInfoMap.put(props[0],peerData);
		         i++;
			}
			in.close();
		}
		catch (Exception ex) 
		{
			log.info("EXception:" +ex.toString());
		}
	}
}
