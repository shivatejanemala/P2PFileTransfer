package peerProcessPackage;

public class CommonConfigurationDAO {

	public static int NumberOfPreferredNeighbors ;
	public static int UnchokingInterval ;
	public static int OptimisticUnchokingInterval;
	public static String FileName;
	public static int FileSize;
	public static int PieceSize;
	
	public int getNumberOfPreferredNeighbors() {
		return NumberOfPreferredNeighbors;
	}
	public void setNumberOfPreferredNeighbors(int numberOfPreferredNeighbors) {
		NumberOfPreferredNeighbors = numberOfPreferredNeighbors;
	}
	public int getUnchokingInterval() {
		return UnchokingInterval;
	}
	public void setUnchokingInterval(int unchokingInterval) {
		UnchokingInterval = unchokingInterval;
	}
	public int getOptimisticUnchokingInterval() {
		return OptimisticUnchokingInterval;
	}
	public void setOptimisticUnchokingInterval(int optimisticUnchokingInterval) {
		OptimisticUnchokingInterval = optimisticUnchokingInterval;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public int getFileSize() {
		return FileSize;
	}
	public void setFileSize(int fileSize) {
		FileSize = fileSize;
	}
	public int getPieceSize() {
		return PieceSize;
	}
	public void setPieceSize(int pieceSize) {
		PieceSize = pieceSize;
	}
}
