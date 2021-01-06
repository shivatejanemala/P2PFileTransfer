package peerProcessPackage;

/**
 * 
 * Interface which defines the functions for the file manager listener to perform 
 *
 */
public interface FileManagerListenerInterface {

    public void isFileDownloadComplete();
    public void isPieceArrived (int partIdx);
}
