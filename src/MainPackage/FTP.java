package MainPackage;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FTP {
    
    public static FTPClient client = null;
    private static String address;   
    private static String password;
    
    public static void setParameters(String addr, String pass) {
        address = addr;
        password = pass;
    }
    
    public static boolean setConnectionToFTP() {
        try {
            client = new FTPClient();
            client.connect(address);
            client.login("sweetr", password);
            return true;
        } catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public static File downloadFile(String filename, File file) throws Exception {
        try {
            client.download("/public_html/sweet-p-ro/images/"+filename, file);
        } catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException ex) {
            setConnectionToFTP();
            client.download("/public_html/sweet-p-ro/images/"+filename, file);
        }
        return file;
    }
    
    public static void uploadFile(File file, String name) throws Exception {
        client.upload(file);
        client.rename(name, "/public_html/sweet-p-ro/images/"+name);
    }
    
    public static Image readImageFromFileOrDownloadFromFTP(String filename) throws Exception {
        try {
            File imageFile = new java.io.File("c:\\temp\\" + filename);            
            if (imageFile.exists()) {
                return ImageIO.read(imageFile);
            }
        } catch (Exception ex) {
        }
        if (!client.isConnected()) {
            throw new Exception();
        }
        try {
            File imageFile = new java.io.File("c:\\temp\\" + filename);            
            return ImageIO.read(FTP.downloadFile(filename, imageFile));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }    
}
