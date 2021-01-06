package peerProcessPackage;

import java.util.Date;

import org.apache.log4j.*;

public class HandShakeMessage  extends DataMessageConstants {

   static Logger log = Logger.getLogger(HandShakeMessage.class.getName());

	
	private byte[] messageheader = new byte[HAND_SHAKE_HEADER_LENGTH];
	
	private byte[] zeroBits = new byte[HAND_SHAKE_ZERO_BIT_LEN];
	
	private byte[] peerId = new byte[HAND_SHAKE_PEER_ID_LEN];
	
	
	public HandShakeMessage(String header, String peerId) {
		
		this.messageheader = header.getBytes();
		this.peerId = peerId.getBytes();
		this.zeroBits = HAND_SHAKE_ZERO_BIT.getBytes();
		
	}
	public HandShakeMessage(byte[] header, byte[] peerId) {
		
		this.messageheader = header;
		this.peerId = peerId;
		this.zeroBits = HAND_SHAKE_ZERO_BIT.getBytes();
		
	}

	public byte[] createSendHandshakeMsg() throws Exception {
		byte[] result = null;
		if(this.messageheader.length != HAND_SHAKE_HEADER_LENGTH) {
			log.error("Error in created HandShake Message Header-"+ this.messageheader);
			throw new Exception("Error in created HandShake Message Header-"+ this.messageheader);
		}
		if(this.zeroBits.length != HAND_SHAKE_ZERO_BIT_LEN) {
			log.error("Error in created HandShake Message Zero bit field-"+ this.zeroBits);
			throw new Exception("Error in created HandShake Message Zero bit field-"+ this.zeroBits);
		}
		if(this.peerId.length != HAND_SHAKE_PEER_ID_LEN) {
			log.error("Error in created HandShake Message PeerID field-"+ this.peerId);
			throw new Exception("Error in created HandShake Message PeerID field-"+ this.peerId);
		}
		System.arraycopy(messageheader, 0, result, 0, messageheader.length);
		System.arraycopy(zeroBits, 0, result, result.length, zeroBits.length);
		System.arraycopy(peerId, 0, result, result.length, peerId.length);
		
		return result;
	}

	public static HandShakeMessage createReceiveHandshakeMsg(byte[] input) throws Exception {
		byte[] messageheader = new byte[HAND_SHAKE_HEADER_LENGTH];
		byte[] peerId = new byte[HAND_SHAKE_PEER_ID_LEN];
		byte[] zeroBit = new byte[HAND_SHAKE_ZERO_BIT_LEN];
		
		System.arraycopy(input, 0, messageheader, 0, messageheader.length);
		System.arraycopy(input, messageheader.length, zeroBit, 0, zeroBit.length);
		System.arraycopy(input, 0, messageheader.length+zeroBit.length, 0, peerId.length);
		if(!HAND_SHAKE_ZERO_BIT.equals(zeroBit.toString())) {
			throw new Exception("Error in receiving hand shake message:Fault in the zero bits-"+ input);
		}
		HandShakeMessage result = new HandShakeMessage(messageheader, peerId);
		return result;
	}

	
	public byte[] getMessageheader() {
		return messageheader;
	}


	public void setMessageheader(byte[] messageheader) {
		this.messageheader = messageheader;
	}


	public byte[] getZeroBits() {
		return zeroBits;
	}


	public void setZeroBits(byte[] zeroBits) {
		this.zeroBits = zeroBits;
	}


	public byte[] getPeerId() {
		return peerId;
	}


	public void setPeerId(byte[] peerId) {
		this.peerId = peerId;
	}
   

}
