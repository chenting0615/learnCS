import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NodeWindow extends Frame 
        implements ActionListener, ComponentListener,
                   MouseListener, ItemListener
{
     Choice freqCH;
     TextArea conTA, routeTA, recvdTA, infoTA;
     TextField ipTF, sendTF, destTF;
     Label Label1, Label2, Label3, Label4, label5;
     Button okB, cancelB;

     Image buffer;
     Graphics gg;
     Color acolor = new Color(246,227,218);

     final static int X=0;
     final static int Y=0;

     Node node;
     Network parent;

     public NodeWindow(Node node, Network parent) {
          super ("Network edit window, node="+node.id);
          this.node = node;
          this.parent = parent;

          setLayout(null);

          int y = Y+40;

          Label1=new Label("IP Address");
          Label1.setBackground(acolor);
          Label1.setFont(new Font("SansSerif",Font.PLAIN,12));
          Label1.setBounds(X+30,y,178,21);
          add(Label1);

          ipTF=new TextField(40);
          ipTF.addActionListener(this);
          ipTF.setBackground(Color.white);
          ipTF.setBounds(X+221,y,190,22);
          add(ipTF);

          y += ipTF.getSize().height + 7;

          Label2 = new Label("Message to Send:");
          Label2.setBounds(X+30,y,177,21);
          add(Label2);

          sendTF = new TextField(40);
          sendTF.setBackground(Color.white);
          sendTF.setBounds(X+221, y, 190, 21);
          add(sendTF);

          y += sendTF.getSize().height + 7;

          Label2 = new Label("Destination node:");
          Label2.setBounds(X+30,y,177,21);
          add(Label2);

          destTF = new TextField(40);
          destTF.setBackground(Color.white);
          destTF.setBounds(X+221, y, 190, 21);
          add(destTF);

          y += destTF.getSize().height + 7;


          Label3=new Label("Connection Table");
          Label3.setBackground(acolor);
          Label3.setFont(new Font("SansSerif",Font.PLAIN,12));
          Label3.setBounds(X+136,y,178,21);
          add(Label3);

          y += Label3.getSize().height+5;

          conTA=new TextArea(5,40);
          conTA.setBackground(Color.white);
          conTA.setBounds(X+32,y,377,110);
          add(conTA);

          y += conTA.getSize().height+10;

          Label4=new Label("Routing Table");
          Label4.setBackground(acolor);
          Label4.setFont(new Font("SansSerif",Font.PLAIN,12));
          Label4.setBounds(X+136,y+10,176,21);
          add(Label4);

          y += Label4.getSize().height+5;

          Label4=new Label("Destination       Next hop");
          Label4.setBackground(acolor);
          Label4.setFont(new Font("SansSerif",Font.PLAIN,12));
          Label4.setBounds(X+35,y,176,21);
          add(Label4);

          y += Label4.getSize().height+5;

          routeTA=new TextArea(5,40);
          routeTA.setBackground(Color.white);
          routeTA.setBounds(X+32,y,379,114);
          add(routeTA);

          y += routeTA.getSize().height+5;

          okB = new Button("OK");
          okB.setBounds(X+165,y,40,25);
          okB.addActionListener(this);
          add(okB);

          cancelB = new Button("CANCEL");
          cancelB.setBounds(okB.getLocation().x+okB.getSize().width+5,y,60,25);
          cancelB.addActionListener(this);
          add(cancelB);

          y = Y+45;

          label5=new Label("Packets Received");
          label5.setBackground(acolor);
          label5.setFont(new Font("SansSerif",Font.PLAIN,12));
          label5.setBounds(X+455,y,172,21);
          add(label5);

          y += label5.getSize().height + 7;

          recvdTA=new TextArea(5,40);
          recvdTA.setBackground(Color.white);
          recvdTA.setBounds(X+427,y,242,200);
          recvdTA.setEditable(false);
          add(recvdTA);

          y += recvdTA.getSize().height + 10;

          label5=new Label("More information");
          label5.setBackground(acolor);
          label5.setFont(new Font("SansSerif",Font.PLAIN,12));
          label5.setBounds(X+455,y,172,21);
          add(label5);

          y += label5.getSize().height + 7;

          infoTA=new TextArea(5,40);
          infoTA.setBackground(Color.white);
          infoTA.setBounds(X+427,y,242,200);
          infoTA.setEditable(false);
          add(infoTA);

          y += recvdTA.getSize().height + 10;


          // The following sets up the initial values

          ipTF.setText(node.id);
          recvdTA.setText(node.packetsReceived.toString());
          conTA.setText(node.connectionTable.toString('\n'));
          routeTA.setText(node.routingTable.toString('\n'));
          destTF.setText(node.dest);
          sendTF.setText(node.messageToSend);
          infoTA.setText("Position: x="+node.x+" y="+node.y+"\n"+
                         node.status()+"\n"+node.info.toString('\n'));

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(700,600);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();

          addWindowListener(
             new WindowAdapter() {
                public void windowClosing (WindowEvent we) {
                     dispose();
                }
             }
          );
     }
     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == okB) {
               node.id = ipTF.getText();
               node.dest = destTF.getText();
               node.connectionTable = 
                   new StringList(U.fixNewlines(conTA.getText()),"\n");
               node.routingTable = 
                   new StringList(U.fixNewlines(routeTA.getText()),"\n");
               node.messageToSend = sendTF.getText();
               parent.repaint();
               dispose();
          }
          if (e.getSource() == cancelB) {
               dispose();
          }
     }

     public void itemStateChanged(ItemEvent e) {}
     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}
     public void mouseEntered(MouseEvent e) {}
     public void mouseExited(MouseEvent e) {}
     public void mousePressed(MouseEvent e) {}
     public void mouseReleased(MouseEvent e) {}
     public void mouseClicked(MouseEvent e) {
          if(!e.isMetaDown())
               if(e.getClickCount() == 2)
                    doubleClick(e);
     }
     public void doubleClick (MouseEvent e) {
     }
     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          // Write to gg first
          g.drawImage(buffer,0,0,this);
     }
     public void update (Graphics g) {
          paint (g);
     }
}
