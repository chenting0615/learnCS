package org.softwarehelps.learncs;

import java.applet.Applet;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;

/**
 * 
 * Insert Applet in a JFrame and start it.
 * 
 * @author Peter Dobson
 */
public class AppletLauncher {

    /**
     * add a little extra height cause some Applets don't quite fit
     */
    final static int EXTRA_HEIGHT = 30;
    
    final static Pattern findCode = Pattern.compile(
            "CODE\\s*=\\s*\\W?(\\w+)", Pattern.CASE_INSENSITIVE);
    final static Pattern findHeight = Pattern.compile(
            "height\\s*=\\s*\\W?(\\d+)", Pattern.CASE_INSENSITIVE);
    final static Pattern findWidth = Pattern.compile(
            "width\\s*=\\s*\\W?(\\d+)", Pattern.CASE_INSENSITIVE);
        
    String packageName;
    String className;
    int height;
    int width;
    Class appletClass;
    JFrame appletWindow;
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    /**
     * Parse HTML with &lt;APPLET&gt; tag to get class name and size.
     * @param appletTag 
     */
    public void parseAppletTag(String appletTag) {
        Matcher matcher;
        matcher = findCode.matcher(appletTag);
        if (matcher.find()) {
            className = packageName + "." + matcher.group(1);
        }       
        matcher = findWidth.matcher(appletTag);
        if (matcher.find()) {
            width = Integer.parseInt(matcher.group(1));
        }
        matcher = findHeight.matcher(appletTag);
        if (matcher.find()) {
            height = Integer.parseInt(matcher.group(1));
        }
    }
    
    public Class getAppletClass() throws ClassNotFoundException {
        if (appletClass == null) {
            appletClass = Class.forName(className);            
        }
        return appletClass;
    }
    
    public void launch() 
            throws InstantiationException, 
                   IllegalAccessException,
                   ClassNotFoundException {
        
        Applet applet = (Applet)getAppletClass().newInstance();
        if (appletWindow == null) {
            appletWindow = new JFrame();
            appletWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            appletWindow.setSize(new Dimension(width, height+EXTRA_HEIGHT));
            appletWindow.add(applet);
            appletWindow.setVisible(true);
        } else {
            // bring window to front
            appletWindow.setVisible(true);
            appletWindow.toFront();
            appletWindow.repaint();
        }
        applet.init();
    }
}