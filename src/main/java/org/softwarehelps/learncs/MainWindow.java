package org.softwarehelps.learncs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * Display SelectApplet JPanel in a scrolling window.
 */
public class MainWindow extends JFrame
{
    MainWindow() {
        
        setTitle("Explorations Applet Launcher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(750, 500));
        add(BorderLayout.CENTER, new SelectApplet());
        pack();
    }
    
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame window = new MainWindow();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
