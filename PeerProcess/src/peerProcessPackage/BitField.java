package peerProcessPackage;

public class BitField {

	private int noOfPieces;
	private int[] pieces;
	
	public BitField() {
		noOfPieces = CommonConfigurationDAO.FileSize/CommonConfigurationDAO.PieceSize;
		pieces = new int[noOfPieces];
	}
	
	public BitField(boolean hasFile) {
		noOfPieces = CommonConfigurationDAO.FileSize/CommonConfigurationDAO.PieceSize;
		pieces = new int[noOfPieces];
		for(int i = 0; i<pieces.length;i++) {
			pieces[i] = (hasFile?1:0);
		}
	}
	
	public boolean isCompleteFileReceived() {
		
	return false;
	}
	
}
