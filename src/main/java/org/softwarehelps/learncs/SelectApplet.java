package org.softwarehelps.learncs;

import java.awt.Button;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Allow user to select Explorations Applet by clicking on button in
 * page that describes each Applet organized by Lab number.
 * 
 * @author Peter Dobson
 */
public class SelectApplet extends JPanel {
    
    GridBagConstraints c;
    
    SelectApplet() {
        
        setLayout(new GridBagLayout());        
        start();
        
        addTitle("Explorations in Computer Science");
        
        addLab("Lab 1: Introduction to the Labs");
        addApplet("Introduction", "INTRO", 
                "tell us about yourself and practice taking screenshots");
                        
        addLab("Lab 2: Exploring Number Systems");
        addApplet("Number systems", "NUMSYS", 
                "conversions between base 10 and some other bases");
        addApplet("Binary addition", "BINARYADD", 
                "convert numbers to binary and add them");
        
        addLab("Lab 3A: Representing Numbers");
        addApplet("Negative binary numbers", "NEGNUMS", 
                "sign-magnitude form and 2's complement form");
        addApplet("Real number representations", "REALS", 
                "scientific notation in decimal and in binary");
        
        addLab("Lab 3B: Colorful Characters");
        addApplet("Character codes (ASCII and Unicode)", "CHARCODES", 
                "show the corresponding ASCII or Unicode values for "
                + "\ncharacters (up to 255)");
        addApplet("Text translator into ASCII", "TEXTCODES", 
                "display a list of the Unicode values, and allow some "
                + "\ntext to be converted to numeric codes");
        addApplet("Color maker", "COLORS", 
                "display colors in RGB and HSB formats");
        
        addLab("Lab 3C: Compressing Text");
        addApplet("Text compression using keywords", "COMPRESSOR", 
                "compress and decompress text using a keyword table");
        addApplet("Text compression using Huffman encoding", "COMPRESSOR2", 
                "compress and decompress text using a Huffman encoding");
        
        addLab("Lab 4: Logic Circuits");
        addApplet("LogicGates", "LOGICGATES", 
                "A logic gate circuit simulator");
        
        
        addLab("Lab 5: Computer Cycling");
        addApplet("Super Simple CPU", "CPU", 
                "a complete, working computer that illustrates the "
                + "\nfetch/decode/execute cycle.");
        
        addLab("Lab 7: Low-Level Languages");
        addApplet("Super Simple CPU", "CPU", 
                "the same as used in Lab 5 (above)");
        
        addLab("Lab 8: Using Algorithms for Painting");
        addApplet("Palgo", "PALGO", 
                "\"painting algorithmically\", a simple paint environment "
                + "\nwhere a program directs the paintbrush. Also allows "
                + "\npurely textual programming.");
        
        addLab("Lab 9A: Searching for the Right Sort");
        addApplet("Stacks and queues", "STACKQUEUE", 
                "difference between stacks and queues");
        addApplet("Trees", "TREES", "see how binary trees work");
        
        addLab("Lab 9B: Searching for the Right Sort");
        addApplet("Sorting", "SORT", 
                "selection sort, bubble sort, quick sort");
        addApplet("Searching", "SEARCH", 
                "sequential versus binary search");
        
        addLab("Lab 10: Operating Systems");
        addApplet("Placement of jobs in memory", "MEMFITTING", 
                "contiguous allocation of memory for jobs, using various "
                + "\nfitting algorithms");
        addApplet("Scheduling of jobs", "SCHEDULING", 
                "using FSCS, SJF and Round Robin");
        
        addLab("Lab 11: Disk Scheduling");
        addApplet("Disk Scheduling", "DISKSCHED", 
                "FSCS, SSTF and SCAN (elevator)");
        
        addLab("Lab 12B: Databases");
        addApplet("Simple SQL", "SIMPLESQL", 
                "A tiny relational database program that processes "
                + "\nSQL queries");
        
        addLab("Lab 13: Artificial Intelligence");
        addApplet("Semantic networks", "SEMNET", "logic deduction");
        addApplet("Eliza therapist", "ELIZA", 
                "conversational computer program using some simple rules "
                + "\nfor textual transformation");
        
        addLab("Lab 14: Simulating Life and Heat");
        addApplet("Game of Life", "GAMEOFLIFE", 
                "the classic cellular automaton");
        addApplet("Heat transfer", "HEAT", 
                "colorful animation of dissipation of heat");
        
        addLab("Lab 15: Networking");
        addApplet("TCP/IP", "TCPIP", 
                "reliable connection, ensured delivery of packets between "
                + "\ntwo nodes in a network, allows user to damage or "
                + "\ndestroy packets");
        addApplet("Network router", "NETWORK", 
                "illustrates how routing decisions are made");
        
        addLab("Lab 17: Limits of Computing");
        addApplet("Comparison of several functions", "FUNCGROWTH", 
                "shows how f(N) gets very large");
        addApplet("Plotter", "PLOTTER", 
                "a way to visualize the growth rates of functions");
        addApplet("Traveling Salesperson Problem", "TSP", 
                "run the algorithm on various graphs to find the shortest "
                + "\ncomplete route (if there is one). Beware! If you animate "
                + "\nthe search and the graph is reasonably large, the applet "
                + "\nwill take a very long time to finish!");

        addEnd();
    }
    
    final void start() {
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.gridy = 0;
        c.ipadx = 15;
        c.ipady = 0;
    }
    
    final void addTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Verdana", Font.BOLD, 18));
        c.gridx = 0;
        c.gridwidth = 4;
        c.ipady = 20;
        c.anchor = GridBagConstraints.CENTER;
        add(label, c);
        c.gridwidth = 1;
        c.ipady = 0;
        c.gridy++;
        
    }
    
    final void addLab(String title) {
        
        c.gridx = 0;
        c.ipady = 20;
        add(new JLabel(" "), c);
        c.ipady = 0;
        c.gridy++;
        
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.LINE_START;
        add(new JLabel(" "+title), c);
        c.gridwidth = 1;
        c.gridy++;
    }
    
    final void addApplet(String title, final String folder, String description) {
        
        final AppletLauncher ea = makeExpressionsApplet(folder);
        if (ea == null) {
            // don't show applets that aren't installed correctly
            return;
        }
        
        c.gridx = 0;
        add(new JLabel(" "), c);
        c.gridy++;
        
        c.ipadx = 30;
        add(new JLabel(" "), c);
        c.ipadx = 0;
        
        Button button = new Button(title);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startApplet(ea);
            }
        });
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        add(button, c);
        
        c.gridx = 2;
        add(new JLabel(" "), c);
        
        JTextArea textArea = new JTextArea();  
        textArea.setColumns(35);        
        textArea.setBackground(this.getBackground());
        textArea.setText(description);
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 3;
        add(textArea, c);
        
        c.gridy++;                
    }
    
    final void addEnd() {
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 4;
        add(new JLabel(" "), c);
    }
    
    public AppletLauncher makeExpressionsApplet(String folderName) {
        
        String appletPackageName = getClass().getPackage().getName() 
                + "." + folderName;        
        String resourcePath = folderName+"/core.htm";
        InputStream is = getClass().getResourceAsStream(resourcePath);
        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("\\Z");
        String text = scanner.next();
        AppletLauncher ea = new AppletLauncher();
        ea.setPackageName(appletPackageName);
        ea.parseAppletTag(text);
        try {
            ea.getAppletClass();
        } catch (ClassNotFoundException ex) {
            // just leave out this applet from SelectApplet page
            return null;
        }
        
        return ea;
    }
    
    public void startApplet(AppletLauncher ae) {

        try {
            ae.launch();
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(this, "Illegal Access: "
                    + ae.className, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(this, "Instantiation Exception: "
                    + ae.className, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Class Not Found: "
                    + ae.className, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
