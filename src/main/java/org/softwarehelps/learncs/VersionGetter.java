package org.softwarehelps.learncs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author Peter Dobson
 */
public class VersionGetter {
    
    private static VersionGetter instance = new VersionGetter();
    
    public static VersionGetter getInstance() {
        return instance;
    }
    
    String version;
    
    public final String getVersion() {
        return getVersion(this);
    }
    
    public final synchronized String getVersion(Object obj) {
        
        // this method code clipped from
        // http://stackoverflow.com/questions/2712970/how-to-get-maven-artifact-version-at-runtime
        
        if (version != null) {
            return version;
        }

        // using Java API
        Package aPackage = obj.getClass().getPackage();
        if (aPackage != null) {
            version = aPackage.getImplementationVersion();
            if (version == null) {
                version = aPackage.getSpecificationVersion();
            }
        }        

        if (version != null) {
            return version;
        }

        // try to load from maven properties first
        try {
            Properties p = new Properties();
            File file = new File("target/maven-archiver/pom.properties");            
            InputStream is = new FileInputStream(file);
            if (is != null) {
                p.load(is);
                version = p.getProperty("version", "");
            }
        } catch (Exception e) {
            // ignore
        }

        if (version == null) {
            version = "";
        }
        return version;
    }         
}
