package org.softwarehelps.learncs;

import java.awt.Component;
import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Peter Dobson
 */
public class UpdateChecker extends Thread {

    Component parent;
    String version;
    String versionURL;
    String downloadURL;

    UpdateChecker(Component parent, String versionURL, String downloadURL) {

        this.parent = parent;
        version = VersionGetter.getInstance().getVersion(parent);
        this.versionURL = versionURL;
        this.downloadURL = downloadURL;
    }

    String getNewVersion() {
        try {
            Scanner content = new Scanner(new URL(versionURL).openStream());            
            return content.nextLine();
        } catch (MalformedURLException ex) {
            Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (IOException ex) {
            Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    int compareVersions(String a, String b) {

        int result = 0;
        // TODO: split at all non alphanumeric characters
        String[] aParts = a.split("\\.");
        String[] bParts = b.split("\\.");

        for (int i = 0; i < aParts.length; i++) {
            if (bParts.length <= i) {
                return 1; // a is bigger cause it matches and has more parts
            }
            if (aParts[i].matches("\\d+") && bParts[i].matches("\\d+")) {
                result = Integer.valueOf(aParts[i]).compareTo(
                        Integer.valueOf(bParts[i]));
            } else {
                result = aParts[i].compareTo(bParts[i]);
            }
            if (result != 0) {
                return result;
            }
        }
        if (bParts.length > aParts.length) {
            return 1; // b is bigger cause it matches and has more parts
        }
        return 0;
    }   
    
    void browseDownloadURL() throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URI(downloadURL));
    }
    
    @Override
    public void run() {

        if (this.version.isEmpty()) {
            return;
        }
                
        if (compareVersions(getNewVersion(), this.version) > 0) {
            int option = JOptionPane.showConfirmDialog(parent,
                    "There is a new version of this software for download.  "
                    + "Do you wish to download it?", "Download Update",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    browseDownloadURL();
                    Thread.sleep(1000L);
                    System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
