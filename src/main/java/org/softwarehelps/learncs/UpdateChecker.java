package org.softwarehelps.learncs;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
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
    String versionURL;
    String downloadURL;

    UpdateChecker(Component parent, String versionURL, String downloadURL) {

        this.parent = parent;
        this.versionURL = versionURL;
        this.downloadURL = downloadURL;
    }

    public final synchronized String getThisVersion() {

        // code adapted from
        // http://stackoverflow.com/questions/2712970/how-to-get-maven-artifact-version-at-runtime

        String version = null;

        // try using Java API
        Package aPackage;
        if (parent == null) {
            aPackage = getClass().getPackage();
        } else {
            aPackage = parent.getClass().getPackage();
        }

        if (aPackage != null) {
            version = aPackage.getImplementationVersion();
            if (version == null) {
                version = aPackage.getSpecificationVersion();
            }
        }

        if (version == null) {

            // try to load from maven properties
            try {
                Properties p = new Properties();
                File file = new File("target/maven-archiver/pom.properties");
                p.load(new FileInputStream(file));
                version = p.getProperty("version", "");
                
            } catch (Exception e) {
                // ignore
            }
        }

        // default to empty string
        if (version == null) {
            version = "";
        }

        return version;
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

    static class Version {

        String number = "";
        String preRelease = "";
        String metaData = "";

        Version(String v) {
            String[] s1 = v.split("\\+", 2); // split out meta data
            if (s1.length > 1) {
                metaData = s1[1];
            }
            String[] s2 = s1[0].split("-", 2); // split out pre relase
            number = s2[0]; // 
            if (s2.length > 1) {
                preRelease = s2[1];
            }
        }
    }

    int compareVersionNumbers(String a, String b) {

        String[] aVals = a.split("\\.");
        String[] bVals = b.split("\\.");
        int i = 0;
        while (i < aVals.length && i < bVals.length && aVals[i].equals(bVals[i])) {
            i++;
        }

        if (i < aVals.length && i < bVals.length) {
            int diff = Integer.valueOf(aVals[i]).compareTo(Integer.valueOf(bVals[i]));
            return diff < 0 ? -1 : diff == 0 ? 0 : 1;
        }

        return aVals.length < bVals.length ? -1 : aVals.length == bVals.length ? 0 : 1;
    }

    int compareVersions(String aString, String bString) {

        Version a = new Version(aString);
        Version b = new Version(bString);

        int result = compareVersionNumbers(a.number, b.number);
        
        if (result != 0) {
            return result;
        }
        
        if (a.preRelease.isEmpty() && b.preRelease.isEmpty()) {
            return 0;                
        } 
        if (a.preRelease.isEmpty()) {
            return 1;
        }
        if (b.preRelease.isEmpty()) {
            return -1;
        }
        return a.preRelease.compareTo(b.preRelease);        
    }     

    void browseDownloadURL() throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URI(downloadURL));
    }

    @Override
    public void run() {

        String thisVersion = getThisVersion();
        if (thisVersion == null || thisVersion.isEmpty()) {
            return;
        }

        if (compareVersions(getNewVersion(), thisVersion) > 0) {
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
