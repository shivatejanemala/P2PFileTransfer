package peerProcessPackage;
import java.util.Date;

public class PeerDataInfo 
{
	public String peerId;
	public String peerAddress;
	public String peerPort;
	public int isFirstPeer;
	public double dataRate = 0;
	public int isInterested = 1;
	public int isPreferredNeighbor = 0;
	public int isOptUnchokedNeighbor = 0;
	public int isChoked = 1;
	public BitField bitField;
	public int type = -1;
	public int hasFile = 0;
	public int isCompleted = 0;
	public int isHandShaked = 0;
	public Date startTime;
	public Date finishTime;
	
	public PeerDataInfo(String pId, String pAddress, String pPort, int hasFile)
	{
		this.peerId = pId;
		this.peerAddress = pAddress;
		this.peerPort = pPort;
		//this.bitField = new BitField();
		this.hasFile = hasFile;
	}
	public PeerDataInfo(String pId, String pAddress, String pPort, int pIsFirstPeer, int hasFile)
	{
		this.peerId = pId;
		this.peerAddress = pAddress;
		this.peerPort = pPort;
		this.isFirstPeer = pIsFirstPeer;
		this.bitField = new BitField();
		this.hasFile = hasFile;
	}
	public String getPeerId() {
		return peerId;
	}
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}
	public String getPeerAddress() {
		return peerAddress;
	}
	public void setPeerAddress(String peerAddress) {
		this.peerAddress = peerAddress;
	}
	public String getPeerPort() {
		return peerPort;
	}
	public void setPeerPort(String peerPort) {
		this.peerPort = peerPort;
	}
	public int getIsFirstPeer() {
		return isFirstPeer;
	}
	public void setIsFirstPeer(int isFirstPeer) {
		this.isFirstPeer = isFirstPeer;
	}
	public double getDataRate() {
		return dataRate;
	}
	public void setDataRate(double dataRate) {
		this.dataRate = dataRate;
	}
	public int getIsInterested() {
		return isInterested;
	}
	public void setIsInterested(int isInterested) {
		this.isInterested = isInterested;
	}
	public int getIsPreferredNeighbor() {
		return isPreferredNeighbor;
	}
	public void setIsPreferredNeighbor(int isPreferredNeighbor) {
		this.isPreferredNeighbor = isPreferredNeighbor;
	}
	public int getIsOptUnchokedNeighbor() {
		return isOptUnchokedNeighbor;
	}
	public void setIsOptUnchokedNeighbor(int isOptUnchokedNeighbor) {
		this.isOptUnchokedNeighbor = isOptUnchokedNeighbor;
	}
	public int getIsChoked() {
		return isChoked;
	}
	public void setIsChoked(int isChoked) {
		this.isChoked = isChoked;
	}
	public BitField getBitField() {
		return bitField;
	}
	public void setBitField(BitField bitField) {
		this.bitField = bitField;
	}
	public int gettype() {
		return type;
	}
	public void settype(int type) {
		this.type = type;
	}
	
	public int getHasFile() {
		return hasFile;
	}
	public void setHasFile(int hasFile) {
		this.hasFile = hasFile;
	}
	public int getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(int isCompleted) {
		this.isCompleted = isCompleted;
	}
	public int getIsHandShaked() {
		return isHandShaked;
	}
	public void setIsHandShaked(int isHandShaked) {
		this.isHandShaked = isHandShaked;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
}