package peerProcessPackage;

import org.apache.log4j.Logger;

public class DataMessageConstants {

	public static String HAND_SHAKE_HEADER = "P2PFILESHARINGPROJ";
	
	public static int HAND_SHAKE_HEADER_LENGTH = 18;
	
	public static int HAND_SHAKE_MESSAGE_LENGTH = 32;

	public static String HAND_SHAKE_ZERO_BIT = "0000000000";
	
	public static int HAND_SHAKE_ZERO_BIT_LEN = 10;
	
	public static int HAND_SHAKE_PEER_ID_LEN = 4;
	
	public static int DATA_MESSAGE_LENGTH = 4;
	
	public static int DATA_MESSAGE_TYPE_LENGTH = 1;
	
	public static int CHOKE_MESSAGE_TYPE = 0;
	
	public static int UNCHOKE_MESSAGE_TYPE = 1;
	
	public static int INTERESTED_MESSAGE_TYPE = 2;
	
	public static int UNINTERESTED_MESSAGE_TYPE = 3;

	public static int HAVE_MESSAGE_TYPE = 4;

	public static int BITFIELD_MESSAGE_TYPE = 5;

	public static int REQUEST_MESSAGE_TYPE = 6;

	public static int PIECE_MESSAGE_TYPE = 7;

}
