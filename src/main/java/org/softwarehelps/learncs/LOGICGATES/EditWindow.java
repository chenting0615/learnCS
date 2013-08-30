import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class EditWindow extends Frame implements ActionListener
{
     Button okB, cancelB;
     TextArea ta;
     Gate g;

     public EditWindow (Gate g) {
          setLayout(new BorderLayout());
          this.g = g;

          setSize(400, 300);
          setLocation(200, 100);

          setBackground(Color.green);

          ta = new TextArea (20, 40);
          ta.setBackground(Color.white);
          add("Center",ta);

          ta.setText(g.truthtable);

          Panel p = new Panel();
          p.setLayout (new FlowLayout(FlowLayout.CENTER,5,5));
          add("South", p);

          okB = new Button("OK");
          okB.addActionListener(this);
          p.add(okB);

          cancelB = new Button("Canel");
          cancelB.addActionListener(this);
          p.add(cancelB);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                    }
               }    
          );

          setVisible(true);
     }

     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == okB) {
               g.truthtable = ta.getText();
               dispose();
          }
          else if (e.getSource() == cancelB) {
               dispose();
          }
     }

}
