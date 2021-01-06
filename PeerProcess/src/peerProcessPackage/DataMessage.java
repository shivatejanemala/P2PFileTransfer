package peerProcessPackage;

import java.sql.Savepoint;
import java.util.Date;

import org.apache.log4j.*;

public class DataMessage extends DataMessageConstants {

static Logger log = Logger.getLogger(DataMessage.class.getName());

	
	private byte[] messageLength = new byte[DATA_MESSAGE_LENGTH];


	private byte[] messageType = new byte[DATA_MESSAGE_TYPE_LENGTH];
	
	private byte[] payLoad = null;
	
	public DataMessage()
	{
	//        
	}
	 
	public DataMessage(String msgLength,String type,String msgPayLoad) {
		
		this.messageLength = msgLength.getBytes();
		this.messageType = type.getBytes();
		this.payLoad= new byte[msgPayLoad.length()];
		payLoad = msgPayLoad.getBytes();
		
	}

	public DataMessage(byte[] messagelength, byte[] messageType, byte[] payLoad) {
		this.messageLength = messagelength;
		this.messageType = messageType;
		this.payLoad= new byte[payLoad.length];
		
	}
	
	public DataMessage(String type,String payLoad) {
        try {
            int t = Integer.parseInt(type);
            if(payLoad!=null) {
            	this.messageLength = Integer.toString(payLoad.length()+1).getBytes();
            	this.messageType = type.getBytes();
            	this.payLoad = payLoad.getBytes();
            }
            else {
            	if (t == CHOKE_MESSAGE_TYPE || t == INTERESTED_MESSAGE_TYPE || t == UNINTERESTED_MESSAGE_TYPE|| t == UNCHOKE_MESSAGE_TYPE)
                {
                    this.messageLength = "1".getBytes();
                    this.messageType=type.getBytes();
                    this.payLoad = null;
                }
                else
                    throw new Exception("Error in creating the data message due to the wrong usage of constructor");
                
            }
            
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        
    }
	public byte[] createSendDataMsg() throws Exception {
		byte[] result = null;
		if(this.messageLength.length != DATA_MESSAGE_LENGTH) {
			log.error("Error in created Data Message Length field-"+ this.messageLength);
			throw new Exception("Error in created send Data Message Length field-" + this.messageLength);
		}
		if(this.messageType.length != DATA_MESSAGE_TYPE_LENGTH) {
			log.error("Error in created Data  Message Type field-"+ this.messageType);
			throw new Exception("Error in created Data  Message Type field-"+ this.messageType);
		}
		System.arraycopy(messageLength, 0, result, 0, messageLength.length);
		System.arraycopy(messageType, 0, result, result.length, messageType.length);
		System.arraycopy(payLoad, 0, result, result.length, payLoad.length);
		
		return result;
	}

	public static DataMessage createReceiveDataMsg(byte[] input) throws Exception {
		byte[] messagelength = new byte[DATA_MESSAGE_LENGTH];
		byte[] messageType = new byte[DATA_MESSAGE_TYPE_LENGTH];
		byte[] payLoad = new byte[input.length-messagelength.length-messageType.length];
		
		System.arraycopy(input, 0, messagelength, 0, messagelength.length);
		System.arraycopy(input, messageType.length, messageType, 0, messageType.length);
		System.arraycopy(input,messagelength.length+messageType.length,payLoad, 0, payLoad.length);
		
		DataMessage result = new DataMessage(messagelength, messageType,payLoad);
		return result;
	}

	public byte[] getMessageLength() {
		return messageLength;
	}


	public void setMessageLength(byte[] messageLength) {
		this.messageLength = messageLength;
	}


	public byte[] getMessageType() {
		return messageType;
	}


	public void setMessageType(byte[] messageType) {
		this.messageType = messageType;
	}


	public byte[] getPayLoad() {
		return payLoad;
	}


	public void setPayLoad(byte[] payLoad) {
		this.payLoad = payLoad;
	}
	
	public void setPayLoad(String msgPayload) {
		this.payLoad= new byte[msgPayload.length()];
		payLoad = msgPayload.getBytes();
	}
}
