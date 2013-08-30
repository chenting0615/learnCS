package org.softwarehelps.learncs.SEMNET;

/* This file was automatically generated from a .mac file.*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Semnet extends Frame implements ActionListener,
                                             TextListener
{
     TextField queryTF, answerTF;
     Label lab1, lab2, lab3, lab4;
     Button enterB, loadB, saveB, exampleB;
     TextArea inferencesTA, rulesTA;

     Image buffer;
     Graphics gg;
     Color acolor = new Color(246,227,218);

     public Semnet() {
          setLayout(null);

          setTitle("Semantic Networks");

          int y = 35;

          Label lab = new Label("Knowledge Representation");
          lab.setBounds(30,y,460,38);
          lab.setFont (new Font("SansSerif", Font.BOLD, 36));
          lab.setBackground(acolor);
          add(lab);

          y += 5 + lab.getSize().height;

          lab = new Label("Semantic networks and logical deduction");
          lab.setBounds(60,y,380,38);
          lab.setFont (new Font("SansSerif", Font.BOLD, 18));
          lab.setBackground(acolor);
          add(lab);

          y += 10 + lab.getSize().height;

          lab1=new Label("Your query:");
          lab1.setBounds(8,y,122,30);
          lab1.setBackground(acolor);
          add(lab1);

          queryTF=new TextField(40);
          queryTF.setBounds(140,y,358,27);
          queryTF.addActionListener(this);
          queryTF.addTextListener(this);
          queryTF.setBackground(Color.white);
          add(queryTF);

          y += 10 + queryTF.getSize().height;

          lab2=new Label("The answer:");
          lab2.setBounds(9,y,123,26);
          lab2.setBackground(acolor);
          add(lab2);

          answerTF=new TextField(40);
          answerTF.setBounds(140,y,358,27);
          answerTF.setBackground(Color.white);
          add(answerTF);

          y += 10 + answerTF.getSize().height;

          int tempx = 120;

          enterB=new Button("Is this true?");
          enterB.setBounds(tempx,y,116,32);
          enterB.addActionListener(this);
          add(enterB);
          tempx += enterB.getSize().width + 10;

          exampleB=new Button("Example");
          exampleB.setBounds(tempx,y,70,32);
          exampleB.addActionListener(this);
          add(exampleB);
          tempx += exampleB.getSize().width + 10;

          loadB = new Button("Load rules");
          loadB.setBounds(tempx,y,90,32);
          loadB.addActionListener(this);
          tempx += loadB.getSize().width + 10;
          add(loadB);

          saveB = new Button("Save rules");
          saveB.setBounds(tempx,y,90,32);
          saveB.addActionListener(this);
          tempx += saveB.getSize().width + 10;
          add(saveB);

          y += 10 + enterB.getSize().height;
          int savedy = y;

          lab3=new Label("Deduced facts:");
          lab3.setBounds(30,y,144,25);
          lab3.setBackground(acolor);
          add(lab3);

          y += 5 + lab3.getSize().height;

          inferencesTA=new TextArea(5,40);
          inferencesTA.setBounds(15,y,190,260);
          inferencesTA.setBackground(Color.white);
          inferencesTA.setEditable(false);
          add(inferencesTA);

          y = savedy;

          lab4=new Label("The rules:");
          lab4.setBounds(251,y,129,25);
          lab4.setBackground(acolor);
          add(lab4);

          y += 5 + lab4.getSize().height;

          rulesTA=new TextArea(5,40);
          rulesTA.setBounds(211,y,311,260);
          rulesTA.setBackground(Color.white);
          rulesTA.addTextListener(this);
          add(rulesTA);

          y += 10 + rulesTA.getSize().height;

          setBackground(new Color(246,227,218));
          setVisible(true);
          setLocation(10,10);
          setSize(567,546);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                         System.exit(1);
                    }
               }    
          );
          buffer = createImage (getSize().width, getSize().height);

          repaint();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == queryTF || e.getSource() == enterB) {
               if (queryTF.getText().length() == 0) {
                    queryTF.setBackground(Color.pink);
                    new Popup("Please type a statement in the\n"+
                              "query box above, and this applet\n"+
                              "will determine if it is true or false.\n");
                    return;
               }
               if (rulesTA.getText().length() == 0) {
                    rulesTA.setBackground(Color.pink);
                    new Popup("Please enter 1 or more rules\n"+
                              "into the rules area.\n"+
                              "This applet cannot test a query\n"+
                              "unless there are some logical rules.\n");
                    return;
               }
               answerTF.setText("");
               inferencesTA.setText("");
               if (prove(queryTF.getText(), rulesTA.getText())) 
                    answerTF.setText("This is true");
               else
                    answerTF.setText("This is false or can't be answered.");
          }
          else if (e.getSource() == exampleB) 
               makeExample();
          else if (e.getSource() == loadB) 
               rulesTA.setText(loadRules());
          else if (e.getSource() == saveB) 
               saveRules(rulesTA.getText());
     }

     public void textValueChanged(TextEvent e) {
          if (e.getSource() == queryTF) {
               queryTF.setBackground(Color.white);
               answerTF.setText("");
          }
          if (e.getSource() == rulesTA) {
               rulesTA.setBackground(Color.white);
          }
     }

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          // Write to gg first
          g.drawImage(buffer,0,0,this);
     }

     private boolean findExactMatch (String[] query, String[] rules) {
          for (int i=0; i<rules.length; i++) {
               String[] rule = U.tokenize(rules[i]);
               if (query.length != rule.length)
                    continue;
               boolean matched = true;
               for (int j=0; j<query.length; j++)
                    if (!query[j].equals(rule[j])) {
                         matched = false;
                         break;
                    }
               if (matched)
                    return true;
          }
          return false;
     }

     private void pushSuperclasses (String current, String[] rules, Stack stack) {
          for (int i=rules.length-1; i>=0; i--) {
               String[] rule = U.tokenize(rules[i]);
               if (rule[0].equals(current) && rule[1].equals("isa")) 
                    stack.push(rule[2]);
          }
     }

     private boolean prove (String queryString, String rulesString) {
          Stack headstack = new Stack();      // head of rules stack

          String[] query = U.tokenize(fixup(queryString));
          String current = query[0];

          String[] rules = U.tokenize(rulesString, "\n");
          for (int i=0; i<rules.length; i++)
               rules[i] = fixup(rules[i]);

          while (true) {
               if (findExactMatch (query, rules))
                    return true;
               pushSuperclasses (current, rules, headstack);
               if (headstack.empty())
                    return false;
/*
               try {
                    Thread.sleep(500);
               } catch (InterruptedException ioe) {}
*/
               current = headstack.pop();
               query[0] = current;
               showAllDeductions (current, rules);
          }
     }

     public void showAllDeductions (String current, String[] rules) {
          for (int i=0; i<rules.length; i++) {
               String[] rule = U.tokenize(rules[i]);
               if (rule[0].equals(current))
                    inferencesTA.setText(inferencesTA.getText()+"\n"+
                                         U.detokenize(rule));
          }
     }

     public String fixup (String rule) {
          if (rule.endsWith("."))
               rule = rule.substring(0,rule.length()-1);
          String[] tokens = U.tokenize(rule);
          if (tokens[0].toUpperCase().equals("A") ||
              tokens[0].toUpperCase().equals("AN") ||
              tokens[0].toUpperCase().equals("THE")) {
                 for (int i=0; i<tokens.length-1; i++)
                      tokens[i] = tokens[i+1];
                 tokens[tokens.length - 1] = null;
               tokens = U.tokenize(U.detokenize(tokens));
          }
          if (tokens[0].endsWith("'s"))
               tokens[0] = tokens[0].substring(0,tokens[0].length()-2);
          if (tokens[1].equals("is")) {
               if (tokens.length >= 3) {
                    if (tokens[2].equals("a") || tokens[2].equals("an")) {
                         tokens[1] = "isa";
                         for (int i=2; i<tokens.length-1; i++)
                              tokens[i] = tokens[i+1];
                         tokens[tokens.length - 1] = null;
                         tokens = U.tokenize(U.detokenize(tokens));
                    }
               }
          }
          return U.detokenize(tokens);
     }

     private void makeExample() {
          rulesTA.setText("Mary is a woman\n"+
                          "a woman is a human\n"+
                          "a human isa animal\n"+
                          "an animal eats food\n"+
                          "a man is a human\n"+
                          "a human is a mammal\n"+
                          "a mammal is an animal\n"+
                          "a mammal's skin has hair\n"+
                          "an animal moves\n");
          queryTF.setText("Mary eats food");
     }


     public String loadRules () {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Knowledge Rules", FileDialog.LOAD);
          loadfile.setFile("*.txt");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return "";
          if (!filename.endsWith(".txt")) {
               System.err.println ("A rules file must have an extension of .txt");
               return "";
          }

          BufferedReader is;        // input stream

          if(filename == null || filename.length() == 0)
               return "";

          this.setTitle(filename);
          try {
               is = new BufferedReader(new FileReader(directory+"\\"+filename));
               String line;
               while ((line = is.readLine()) != null) {
                    if (text.length() == 0)
                         text += line;
                    else
                         text += "\n"+line;
               }
               is.close();
          }
          catch(IOException ioe) {
               System.err.println("Load Rules:  i/o exception.");
          }
          return text;
     }

     public void saveRules (String text) {
          FileDialog savefile =
               new FileDialog(this,"Save Knowledge Rules",FileDialog.SAVE);
          savefile.setFile("*.txt");
          savefile.show();

          String filename = savefile.getFile();
          String directory = savefile.getDirectory();
          String fullname = directory + "\\"+filename;
          
          if(filename == null)
               return;

          BufferedWriter os;        // output stream

          if(filename == null || filename.length() == 0)
               return;
          else {
               this.setTitle(filename);
               try {
                    os = new BufferedWriter(new FileWriter(fullname, false));

                    os.write(text);
                    os.close();
               }
               catch(IOException ioe) {
                    System.err.println("Save Rules:  i/o exception.");
               }
          }
     }
}
     
class U {
     public static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     public static String[] copy (String array[]) {
          String[] newone = new String[array.length];
          for (int i=0; i<array.length; i++)
               newone[i] = new String(array[i]);
          return newone;
     }

     public static String[] tokenize(String s) {
          StringTokenizer st = new StringTokenizer(s);
          String[] tokens = new String[st.countTokens()];
          int i=0;
          while (st.hasMoreTokens()) {
               String token = st.nextToken();
               tokens[i++] = token;
          }
          return tokens;
     }

     public static String[] tokenize(String s, String delim) {
          StringTokenizer st = new StringTokenizer(s, delim);
          String[] tokens = new String[st.countTokens()];
          int i=0;
          while (st.hasMoreTokens()) {
               String token = st.nextToken();
               tokens[i++] = token;
          }
          return tokens;
     }

     public static String detokenize(String[] tokens) {
          String s = "";
          for (int i=0; i<tokens.length; i++)
               if (tokens[i] != null)
                    s += tokens[i] + " ";
          return s;
     }

     public static boolean equals (String[] list1, String[] list2) {
          if (list1.length != list2.length)
               return false;
          for (int i=0; i<list1.length; i++)
               if (list1[i] != null && list2[i] != null)
                    if (list1[i].equals(list2[i]))
                         return false;
          return true;
     }
}

class Stack {
     String[] contents;
     int top;

     public Stack() {
          contents = new String[25];
          top = -1;
     }

     public void push (String s) {
          if (top == contents.length)
               grow();
          contents[++top] = s;
     }

     public String pop () {
          if (top == -1)
               return null;
          return contents[top--];
     }

     public String head () {
          if (top == -1)
               return "";
          return contents[top];
     }

     public boolean empty() {
          return top == -1;
     }

     private void grow() {
          String[] newcontents = new String[contents.length*2];
          for (int i=0; i<contents.length; i++)
               newcontents[i] = contents[i];
          contents = newcontents;
     }

     public String toString() {
          if (empty()) 
               return "<<EMPTY>>";
          String s = "";
          for (int i=top; i>=0; i--)
               s += contents[top]+"\n";
          return s;
     }
}
