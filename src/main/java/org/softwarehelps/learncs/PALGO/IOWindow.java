package org.softwarehelps.learncs.PALGO;

import java.awt.*;
import java.awt.event.*;

class IOWindow extends Frame implements ActionListener {
     TextField input;
     TextArea response;
     Button clearB;
     Label lab;
     Label promptLabel;
    
     String current = null;     // used to communicate with Palgo which is
                                // trying to input from the "input" area.

     Color bgcolor = new Color(255,177,17);
     
     public IOWindow() { 
          super ("Palgo I/O Window");
          setLayout(null);
          setBackground (bgcolor);

          int y = 35;

          promptLabel = new Label("");
          promptLabel.setBounds(65,y,300,25);
          promptLabel.setForeground(Color.red);
          promptLabel.setBackground (bgcolor);
          add(promptLabel);

          y += promptLabel.getSize().height + 2;

          lab = new Label("Your input: ");
          lab.setBounds(5,y,60,25);
          add(lab);

          input = new TextField(35);
          input.addActionListener(this);
          input.setBounds(65,y,220,25);
          input.setEditable(false);
          add(input);

          y += input.getSize().height;

          lab = new Label("Output: ");
          lab.setBounds(5,y,60,25);
          add(lab);

          y += lab.getSize().height;

          response = new TextArea(10,40);
          response.setBounds(6,y,400,200);
          response.setEditable(false);
          response.setBackground(Color.white);
          add(response);

          y += response.getSize().height + 5;

          clearB = new Button("clear");
          clearB.addActionListener(this);
          clearB.setBounds(110,y,40,25);
          add(clearB);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                    }
               }    
          );

          setSize(412,350);
          setLocation(100,100);
          setVisible(false);
     }

     public void actionPerformed (ActionEvent e) {
          if (e.getSource() == input)
               current = input.getText();
          else if (e.getSource() == clearB) {
               response.setText("");
               input.setText("");
               input.requestFocus();
          }
     }

     public String getNext() {
          String ret = current;
          current = null;
          return ret;
     }

     public void print (String s) {
          response.setText(response.getText() + s);
          response.setCaretPosition(response.getText().length());
     }

     public void clearInput() {
          input.setText("");
     }

     public void setPrompt(String prompt) {
          promptLabel.setText(prompt);
          repaint();
     }

     public void allowInput (boolean allowinput) {
          input.setEditable(allowinput);
     }

     public void focus() {
          input.requestFocus();
     }
}
