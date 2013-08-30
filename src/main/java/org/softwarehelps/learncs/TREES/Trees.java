import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Trees extends Applet 
        implements ActionListener, ItemListener
{
     Choice treeCommandCH;
     TextField dataTF, parentTF;
     Label topLabel, toLabel;
     Image buffer;
     Graphics gg;
     Color acolor = new Color(246,227,218);

     Treenode topnode = null;

     public void init() {
          setLayout(null);

          topLabel=new Label("Trees");
          topLabel.setBackground(acolor);
          topLabel.setBounds(10,18,65,28);
          topLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
          add(topLabel);

          treeCommandCH=new Choice();

          treeCommandCH.addItem("Clear");
          treeCommandCH.addItem("Reset");
          treeCommandCH.addItem("Attach as left child");
          treeCommandCH.addItem("Attach as right child");
          treeCommandCH.addItem("Insert alphabetically");
          treeCommandCH.addItem("Remove this and all children");
          treeCommandCH.addItem("Find");
          treeCommandCH.addItem("Example 1");
          treeCommandCH.addItem("Example 2");
          treeCommandCH.addItem("Example 3--complete binary tree");

          treeCommandCH.setBounds(100,20,200,25);
          treeCommandCH.setBackground(Color.white);
          treeCommandCH.addItemListener(this);
          add(treeCommandCH);

          dataTF=new TextField(40);
          dataTF.addActionListener(this);
          int x = treeCommandCH.getLocation().x + 
                  treeCommandCH.getSize().width + 5;
          dataTF.setBounds(x, 20, 100, 26);
          dataTF.setBackground(Color.white);
          dataTF.setFont(new Font("SansSerif", Font.PLAIN, 12));
          add(dataTF);

          x += dataTF.getSize().width + 5;

          toLabel=new Label("to");
          toLabel.setBackground(acolor);
          toLabel.setBounds(x,20,20,28);
          toLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
          add(toLabel);

          x += toLabel.getSize().width + 5;

          parentTF=new TextField(40);
          parentTF.addActionListener(this);
          parentTF.setBounds(x, 20, 100, 26);
          parentTF.setBackground(Color.gray);
          parentTF.setFont(new Font("SansSerif", Font.PLAIN, 12));
          parentTF.setEnabled(false);
          add(parentTF);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(577,350);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void actionPerformed(ActionEvent e) {
          String command = treeCommandCH.getSelectedItem();
          if (e.getSource() == dataTF) {
               if (command.equals("Insert alphabetically")) {
                    insertAlphabetically(dataTF.getText());
                    dataTF.setText("");
               }
               else if (command.equals("Remove this and all children")) {
                    if (topnode.data.equals(dataTF.getText()))
                         topnode = null;
                    else
                         topnode.delete(dataTF.getText());
                    dataTF.setText("");
                    repaint();
               }
               else if (command.equals("Find")) {
                    reset();
                    findAnimated(dataTF.getText());
               }
               else if (command.equals("Attach as left child")) {
                    attach (dataTF.getText(), parentTF.getText(), "left");
                    dataTF.setText("");
                    parentTF.setText("");
               }
               else if (command.equals("Attach as right child")) {
                    attach (dataTF.getText(), parentTF.getText(), "right");
                    dataTF.setText("");
                    parentTF.setText("");
               }
               repaint();
          }
          else if (e.getSource() == parentTF) {
               if (command.equals("Attach as left child")) {
                    attach (dataTF.getText(), parentTF.getText(), "left");
                    dataTF.setText("");
                    parentTF.setText("");
               }
               else if (command.equals("Attach as right child")) {
                    attach (dataTF.getText(), parentTF.getText(), "right");
                    dataTF.setText("");
                    parentTF.setText("");
               }
               repaint();
          }
     }

     public void itemStateChanged (ItemEvent e) {
          String command = treeCommandCH.getSelectedItem();
          if (!command.startsWith("Attach")) {
               parentTF.setEnabled(false);
               parentTF.setBackground(Color.gray);
          }
          if (command.equals("Clear")) {
               topnode = null;
               repaint();
          }
          else if (command.equals("Reset")) 
               reset();
          else if (command.startsWith("Attach")) {
               parentTF.setBackground(Color.white);
               parentTF.setEnabled(true);
          }
          else if (command.equals("Find")) {
               if (dataTF.getText().length() > 0) {
                    reset();
                    findAnimated(dataTF.getText());
               }
          }
          else if (command.startsWith("Example 1")) {
               topnode = new Treenode("Mark");
               topnode.leftattach("Kathy");
               Treenode sallynode = topnode.rightattach("Sally");
               sallynode.leftattach("Nick");
               sallynode.rightattach("Zeyla");
               repaint();
          }
          else if (command.startsWith("Example 2")) {
               topnode = new Treenode("Mark");
               Treenode kathy = topnode.leftattach("Kathy");
               kathy.leftattach("Doran");
               Treenode sallynode = topnode.rightattach("Sally");
               sallynode.leftattach("Noreen");
               Treenode wendy = sallynode.rightattach("Wendy");
               wendy.rightattach("Zoe");
               repaint();
          }
          else if (command.startsWith("Example 3")) {
               topnode = new Treenode("Mary");
               new Thread() {
                    public void run() {
                        insertAlphabetically("Alice");
                        int amt = 300;
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Betty");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Abigail");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Samantha");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Tracy");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Natasha");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();

                        insertAlphabetically("Aaron");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Adair");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Azalea");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Bob");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();

                        insertAlphabetically("Mazurka");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Nick");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("Steve");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                        insertAlphabetically("William");
                        try {Thread.sleep(amt);}catch(InterruptedException ie){}
                        repaint();
                    }
               }.start();
          }
     }

     private void insertAlphabetically(String newname) {
          Treenode newone = new Treenode(newname);
          if (topnode == null) 
               topnode = newone;
          else
               topnode.insert(newone);
     }

     private void reset() {
          if (topnode != null)
               topnode.reset();
          repaint();
     }

     private void attach (String newname, String parentName, String whichSide) {
          if (parentName.length() == 0 || newname.length() == 0)
               return;
          Treenode tn = topnode.findAnywhere(parentName);
          if (tn == null)
               new Popup("No node with that label!");
          else 
               if (whichSide.equals("left"))
                    tn.left = new Treenode(newname);
               else
                    tn.right = new Treenode(newname);
     }

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          gg.fillRect(0,0,buffer.getWidth(this),buffer.getHeight(this));
          int x = 185;                // the start of the tree
          int y =  75;                
          gg.setColor(Color.black);
          if (topnode != null) {
               gg.drawRect(x, y, 10, 10);
               gg.drawString("tree", x-30, y+10);
               gg.drawLine(x+5, y+5, x+50, y+10);
               topnode.paint(gg, x+50, y);
          }
          else {
               gg.drawRect(x, y, 10, 10);
               gg.drawString("tree", x-30, y+10);
               gg.drawLine(x, y, x+10, y+10);    // part of the X
               gg.drawLine(x+10, y, x, y+10);    // other half of the X
          }
          // Write to gg first
          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     String toFind;

     public void findAnimated (String data) {
          toFind = data;
          new Thread() {
          public void run() {
               Treenode whichNode = topnode;
               boolean found = false;
     
               while (!found) {
                    int n = toFind.compareTo(whichNode.data);
                    whichNode.color = Color.yellow;
                    repaint();
                    try {
                         Thread.sleep(500);
                    }
                    catch (InterruptedException ie) {
                    }
                    if (n == 0) {
                         whichNode.color = Color.red;
                         found = true;
                    }
                    else if (n < 0) {
                         if (whichNode.left != null) 
                              whichNode = whichNode.left;
                         else {
                              new Popup("Not found in this tree");
                              return;
                         }
                    }
                    else if (n > 0) {
                         if (whichNode.right != null) 
                              whichNode = whichNode.right;
                         else {
                              new Popup("Not found in this tree");
                              return;
                         }
                    }
               }
               repaint();
          }
          }.start();
     }

     public static void sleep (int milliseconds) {
          try {
               Thread.sleep(milliseconds);
          }
          catch (InterruptedException ie) {
          }
     }
}

class Treenode {
     public String data;
     public Treenode left, right;
     public Color color = null;

     final static int width = 50;
     final static int height = 20;
     final static int xSeparation = 50;
     final static int ySeparation = 40;

     public Treenode (String data) {
          this.data = data;
          left = right = null;
     }

     public void attach (Treenode leftTree, Treenode rightTree) {
          left = leftTree;
          right = rightTree;
     }

     public Treenode leftattach (String leftdata) {
          left = new Treenode(leftdata);
          return left;
     }

     public Treenode rightattach (String rightdata) {
          right = new Treenode(rightdata);
          return right;
     }

     public void insert (Treenode newone) {
          if (newone.data.equals(data)) {
               newone.left = left;
               left = newone;
          }
          else if (newone.data.compareTo(data) < 0)
               if (left == null)
                    left = newone;
               else
                    left.insert(newone);
          else
               if (right == null)
                    right = newone;
               else
                    right.insert(newone);
     }

     public void delete (String name) {
          int n = name.compareTo(data);
          if (n < 0) {
               if (left == null) return;
               if (left.data.equals(name))
                    left = null;
               else
                    left.delete(name);
          }
          else if (n > 0) {
               if (right == null) return;
               if (right.data.equals(name))
                    right = null;
               else
                    right.delete(name);
          }
     }

     public void paint (Graphics g, int x, int y) {
          if (color != null) {
               g.setColor(color);
               g.fillRoundRect(x, y, width, height, 10, 10);
               g.setColor(Color.black);
          }
          else 
               g.drawRoundRect(x, y, width, height, 10, 10);
          g.drawString (data, x+5, y+15);

          // Elaborate calculations to give enough room to everyone!

          int midpoint = x + width/2;
          int totalWidth = width();
          int leftEdge = midpoint - totalWidth/2;
          int rightEdge = 0;
          if (right != null)
               rightEdge = midpoint + totalWidth/2 - right.width();

          // If there is a left child, then its box will be painted
          // at the center of the left half, less 1/2 of the box width

          if (left != null) {
               int leftWidth = left.width();
               int leftStart = leftEdge + leftWidth/2 - width/2;
               left.paint(g, leftStart, y + ySeparation);
               g.drawLine(x+width/2, y+height, leftStart + width/2,
                          y+ySeparation);
          }

          // Same thing for the right child (if any)

          if (right != null) {
               int rightWidth = right.width();
               int rightStart = rightEdge + rightWidth/2 - width/2;
               right.paint(g, rightStart, y + ySeparation);
               g.drawLine(x+width/2, y+height, rightStart + width/2,
                          y+ySeparation);
          }
     }

     public String toString() {
          return "NODE: " + data;
     }

     public int width() {
          if (left == null && right == null) 
               return width;
          if (left == null)
               return width/2 + right.width();
          if (right == null)
               return width/2 + left.width();
          return left.width() + 5 + right.width();
     }
 
     public Treenode find (String data) {
          int n = data.compareTo(this.data);
          if (n == 0) {
               return this;
          }
          else if (n < 0) {
               if (left != null) 
                    return left.find(data);
               else {
                    new Popup("Not found in this tree");
                    return null;
               }
          }
          else if (n > 0) {
               if (right != null) 
                    return right.find(data);
               else {
                    new Popup("Not found in this tree");
                    return null;
               }
          }
          return null;
     }

     public Treenode findAnywhere (String data) {
          if (data.equals(this.data))
               return this;
          if (left != null) {
               Treenode tn = left.findAnywhere(data);
               if (tn != null)
                    return tn;
          }
          if (right != null) {
               Treenode tn = right.findAnywhere(data);
               if (tn != null)
                    return tn;
          }
          return null;
     }

     public void reset () {
          color = null;
          if (left != null) 
               left.reset();
          if (right != null)
               right.reset();
     }
}
