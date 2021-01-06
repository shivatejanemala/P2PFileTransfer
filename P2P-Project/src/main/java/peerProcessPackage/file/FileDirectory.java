package peerProcessPackage.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import peerProcessPackage.log.LoggerConfiguration;

public class FileDirectory {

    private final File file;
    private final File  pieceDirectory;
    private static final String piecesLoc = "files/pieces/";

    public FileDirectory(int peerId, String fileName){
        pieceDirectory = new File("./peer_" + peerId + "/" + piecesLoc + fileName);
        pieceDirectory.mkdirs();
        file = new File(pieceDirectory.getParent() + "/../" + fileName);
    }

    /**
     * Function which creates byte array of the piece requested
     * @param partId
     * @return
     */
   public byte[] getPieceAsBytes(int pieceIndex) {
        File file = new File(pieceDirectory.getAbsolutePath() + "/" + pieceIndex);
        return getBytesFromFilePiece(file);
    }
   /**
    * Function which gets the piece byte array from provided file
    * @param pieceIndex
    * @return
    */
   private byte[] getBytesFromFilePiece(File file){
       FileInputStream fis = null;
       try {
           fis = new FileInputStream(file);
           byte[] fileBytes = new byte[(int) file.length()];
           int bytesRead = fis.read(fileBytes, 0, (int) file.length());
           fis.close();
           assert (bytesRead == fileBytes.length);
           assert (bytesRead == (int) file.length());
           return fileBytes;
       } catch (FileNotFoundException e) {
           LoggerConfiguration.getLogger().warning(e);
       } catch (IOException e) {
           LoggerConfiguration.getLogger().warning(e);
       }
       finally {
           if (fis != null) {
               try {
                   fis.close();
               }
               catch (IOException ex) {}
           }
       }
       return null;

   }
   /**
    * Function which writes the received piece byte array into file
    * @param pieceIndex
    * @return
    */
    public void writeBytesAsPieces(byte[] piece, int pieceIndex){
        FileOutputStream fos;
        File ofile = new File(pieceDirectory.getAbsolutePath() + "/" + pieceIndex);
        try {
            fos = new FileOutputStream(ofile);
            fos.write(piece);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            LoggerConfiguration.getLogger().warning(e);
        } catch (IOException e) {
            LoggerConfiguration.getLogger().warning(e);
        }
    }
    
    /**
     * Function for splitting the file into pieces in case the complete file exists in the peer
     * @param pieceSize
     */

    public void splitFile(int pieceSize){
        createPiecesFromFile(file, pieceSize);
        LoggerConfiguration.getLogger().debug("File has been split");
    }

    /**
     * Function for generating the file from pieces
     * @param pieceSize
     */
    public void generateCompletedFile(int numParts) {
        File createdFile = file;
        FileOutputStream fos;
        FileInputStream fis;
        byte[] fileBytesRead;
        List<File> list = new ArrayList<>();
        for (int i = 0; i < numParts; i++) {
            list.add(new File(pieceDirectory.getPath() + "/" + i));
        }
        try {
            fos = new FileOutputStream(createdFile);
            for (File file : list) {
                fis = new FileInputStream(file);
                fileBytesRead = new byte[(int) file.length()];
                fis.read(fileBytesRead, 0, (int) file.length());
                fos.write(fileBytesRead);
                fos.flush();
                fileBytesRead = null;
                fis.close();
                fis = null;
            }
            fos.close();
            fos = null;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * Function for creating the file pieces from file in case the complete file exists in the peer
     * @param pieceSize
     */
    public void createPiecesFromFile(File completeFile, int pieceSize){

        FileInputStream inputStream;
        String newFileName;
        FileOutputStream filePiece;
        int fileSize = (int) completeFile.length();
        int newPieceIndex = 0, read = 0, readLength = pieceSize;
        byte[] pieceReadFromFile;
        try {
            inputStream = new FileInputStream(completeFile);
            while (fileSize > 0) {
                if (fileSize <= 5) {
                    readLength = fileSize;
                }
                pieceReadFromFile = new byte[readLength];
                read = inputStream.read(pieceReadFromFile, 0, readLength);
                fileSize -= read;
                newPieceIndex++;
                newFileName = completeFile.getParent() + "/pieces/" +completeFile.getName() + "/" + Integer.toString(newPieceIndex - 1);
                filePiece = new FileOutputStream(new File(newFileName));
                filePiece.write(pieceReadFromFile);
                filePiece.flush();
                filePiece.close();
                pieceReadFromFile = null;
                filePiece = null;
            }
            inputStream.close();
        } catch (IOException e) {
            LoggerConfiguration.getLogger().warning(e);
        }
    }
}
