package peerProcessPackage;  
import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

public class InitializeRemotePeers
{
	static Logger log = Logger.getLogger(InitializeRemotePeers.class.getName());

	public ArrayList<PeerDataInfo> peerDataInfo = new ArrayList<PeerDataInfo>();
	
	public void getConfiguration()
	{
		String st;
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader("PeerInfo.cfg"));
			int i =0;
			while((st = in.readLine()) != null) 
			{	
				 String[] tokens = st.split("\\s+");
				 peerDataInfo.add(new PeerDataInfo(tokens[0], tokens[1], tokens[2], i));
		         i++;
			}
			in.close();
		}
		catch (Exception ex) 
		{
			log.info("EXception:" +ex.toString());
		}
	}
	/**
	 * Checks if all peer has down loaded the file
	 */
	public static synchronized boolean isRemotePeersCompleted() {

		String line;
		int hasFileCount = 1;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("PeerInfo.cfg"));

			while ((line = in.readLine()) != null) {
				hasFileCount = hasFileCount* Integer.parseInt(line.trim().split("\\s+")[3]);
			}
			if (hasFileCount == 0) {
				in.close();
				return false;
			} else {
				in.close();
				return true;
			}

		} catch (Exception e) {
			
			return false;
		}

	}
	/**
	 * @param args
	 **/
	public static void main(String[] args) 
	{
		try 
		{
			InitializeRemotePeers initializePeers = new InitializeRemotePeers();
			initializePeers.getConfiguration();
					
			// get current path
			String path = System.getProperty("user.dir");
						
			//initializing the remote peers at their respective address through peers
			for (int i = 0; i < initializePeers.peerDataInfo.size(); i++) 
			{
				PeerDataInfo peerData = (PeerDataInfo) initializePeers.peerDataInfo.get(i);
				
				log.info("Starting the remote peer " + peerData.peerId +  " at " + peerData.peerAddress );
				String command = "ssh " + peerData.peerAddress + " cd " + path + "; java peerProcess " + peerData.peerId; 
				Runtime.getRuntime().exec(command);
				log.info(command);
			}		
			
			
			while(true)
			{
				if (isRemotePeersCompleted()) 
				{
				    
					log.info(new Date().toInstant()+":All Remote Peers received the required File");
					break;
				}
				else
				{
					try {
						Thread.sleep(5000);
					} catch (InterruptedException ex) {
					}
				}
			}
			
		}
		catch (Exception ex) 
		{
			log.info("Exception occured while handling the remote peers: "+ex.toString());
		}
	}
}
