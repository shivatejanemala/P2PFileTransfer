package peerProcessPackage;
public class DataMessageDetails
{
	DataMessage dataMsg;
	String fromPeer;
	
	public DataMessageDetails() 
	{
		dataMsg = new DataMessage();
		fromPeer = null;
	}
    
    
    public void setDataMsg(DataMessage dataMsg) {
        this.dataMsg = dataMsg;
    }
	
    public DataMessage getDataMsg() {
		return dataMsg;
	}


	public String getFromPeer() {
		return fromPeer;
	}


	public void setFromPeer(String fromPeer) {
		this.fromPeer = fromPeer;
	}

	
	
	
	
}
