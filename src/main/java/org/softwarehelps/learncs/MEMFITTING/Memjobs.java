import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Memjobs extends Applet implements ActionListener, TextListener
{
     Label lable1, lable2;
     TextField memsizeTF, jobnumTF;
     Button addjobB, killjobB, exampleB, compactB, clearB;
     Choice fitchoice;
     TextArea msgTF;

     Image buffer;
     Graphics gg;

     JobList joblist;
     Color acolor = new Color(176, 244, 184);
     int box1y, box2y;

     public void init() {
          setLayout(null);

          joblist = new JobList();

          Label lab = new Label("Placement of Jobs in Memory");
          lab.setBounds(110,5,430,38);
          lab.setFont (new Font("SansSerif", Font.BOLD, 28));
          lab.setBackground(acolor);
          add(lab);

          int y = 50 + lab.getSize().height;

          box1y = y;

          lable1=new Label("Memory size (Kbytes):");
          lable1.setBackground(acolor);
          lable1.setBounds(228,y,131,27);
          add(lable1);

          memsizeTF=new TextField(40);
          memsizeTF.setBounds(366,y,172,27);
          memsizeTF.addActionListener(this);
          memsizeTF.addTextListener(this);
          memsizeTF.setBackground(Color.white);
          add(memsizeTF);

          addjobB=new Button("Add job");
          addjobB.setBounds(546,y,79,27);
          addjobB.addActionListener(this);
          add(addjobB);

          y += 38;

          box2y = y;

          lable2=new Label("Job Number:");
          lable2.setBackground(acolor);
          lable2.setBounds(229,y,131,27);
          add(lable2);

          jobnumTF=new TextField(40);
          jobnumTF.setBounds(367,y,170,27);
          jobnumTF.addActionListener(this);
          jobnumTF.addTextListener(this);
          jobnumTF.setBackground(Color.white);
          add(jobnumTF);

          killjobB=new Button("End job");
          killjobB.setBounds(546,y,79,27);
          killjobB.addActionListener(this);
          add(killjobB);

          y += 38;

          int x = 298;

          exampleB=new Button("Example");
          exampleB.setBounds(x,y,97,27);
          exampleB.addActionListener(this);
          add(exampleB);
          x += 10 + exampleB.getSize().width;

          compactB=new Button("Compact Memory");
          compactB.setBounds(x,y,105,27);
          compactB.addActionListener(this);
          add(compactB);
          x += 10 + compactB.getSize().width;

          clearB=new Button("Clear");
          clearB.setBounds(x,y,80,27);
          clearB.addActionListener(this);
          add(clearB);

          y += exampleB.getSize().height * 2 + 10;

          fitchoice=new Choice();
          fitchoice.setBackground(Color.white);
          fitchoice.setBounds(314,y,187,25);
          add(fitchoice);
          fitchoice.addItem("First fit");
          fitchoice.addItem("Best fit");
          fitchoice.addItem("Worst fit");

          y += fitchoice.getSize().height + 10;

          msgTF=new TextArea(5,40);
          msgTF.setBackground(Color.white);
          msgTF.setBounds(230,y,398,70);
          add(msgTF);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(657,592);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();

          repaint();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == clearB) {
               joblist = new JobList();
               JobList.nextid = 2;
               repaint();
          }
          if (e.getSource() == addjobB || e.getSource() == memsizeTF) {
               boolean error = !U.isint(memsizeTF.getText());
               if (!error) {
                    int n = U.atoi(memsizeTF.getText());
                    error = (n < 1 || n > JobList.MAXMEM);
               }
               if (error) {
                    memsizeTF.setBackground(Color.pink);
                    new Popup("You must type a number between 1 and\n"+
                              "the maximum size of memory here.\n");
                    return;
               }
               String method = fitchoice.getSelectedItem();
               if (method.equals("First fit"))
                   joblist.newjob(U.atoi(memsizeTF.getText()), JobList.FIRSTFIT);
               else if (method.equals("Best fit"))
                   joblist.newjob(U.atoi(memsizeTF.getText()), JobList.BESTFIT);
               else if (method.equals("Worst fit"))
                   joblist.newjob(U.atoi(memsizeTF.getText()), JobList.WORSTFIT);
               memsizeTF.setText("");
               msgTF.setText("freememory="+joblist.freememory);
               repaint();
          }
          if (e.getSource() == killjobB || e.getSource() == jobnumTF) {
               if (!U.isint(jobnumTF.getText())) {
                    jobnumTF.setBackground(Color.pink);
                    new Popup("Job numbers must be whole numbers.\n");
                    return;
               }
               joblist.kill(U.atoi(jobnumTF.getText()));
               jobnumTF.setText("");
               msgTF.setText("freememory="+joblist.freememory);
               repaint();
          }
          if (e.getSource() == exampleB) {
               makeExample();
          }
          if (e.getSource() == compactB) {
               joblist.compact();
               repaint();
          }
     }

     public void textValueChanged(TextEvent e) {
          if (e.getSource() == memsizeTF) 
               memsizeTF.setBackground(Color.white);
          if (e.getSource() == jobnumTF) 
               jobnumTF.setBackground(Color.white);
     }

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          gg.fillRect(0,0,buffer.getHeight(this),buffer.getWidth(this));
          gg.setColor(Color.black);
          gg.drawRect(218, box1y-3, 410, 33);
          gg.drawRect(218, box2y-3, 410, 32);
          joblist.paint(gg);
          g.drawImage(buffer,0,0,this);
     }

     private void makeExample() {
          new Thread() {
          public void run() {
          joblist.newjob(100, JobList.FIRSTFIT);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          setVisible(true);
          U.sleep(500);

          joblist.newjob(300, JobList.FIRSTFIT);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          setVisible(true);
          U.sleep(500);

          joblist.newjob(75, JobList.FIRSTFIT);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          setVisible(true);
          U.sleep(500);

          joblist.newjob(75, JobList.FIRSTFIT);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          setVisible(true);
          U.sleep(500);

          joblist.newjob(200, JobList.FIRSTFIT);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          setVisible(true);
          U.sleep(500);

/*
          joblist.kill(3);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          U.sleep(500);

          joblist.kill(4);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          U.sleep(500);

          joblist.newjob(50, JobList.FIRSTFIT);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          setVisible(true);
          U.sleep(500);

          joblist.kill(2);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          U.sleep(500);

          joblist.kill(6);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          U.sleep(500);

          joblist.kill(5);
          msgTF.setText("freememory="+joblist.freememory);
          repaint();
          U.sleep(500);
*/
          }
          }.start();
     }


     public void update(Graphics g) {
          paint(g);
     }
}

class Job {
     int start;         // starting address in memory
     int size;          // size in K bytes
     int number;        // id number (job pid)
     boolean empty;     // is this an empty memory holder?
     String name;       // "operating system" or "empty" or "job #"

     public Job(int size, int number, int startingAddress, String name) {
          this.size = size;
          this.number = number;
          this.empty = false;
          this.start = startingAddress;
          this.name = name;
     }
}

class JobList {
     Job[] jobs;
     int numjobs;
     int freememory;
     static int nextid = 2;
     final static int OSMEM = 150;

     final static int FIRSTFIT = 1;
     final static int BESTFIT = 2;
     final static int WORSTFIT = 3;

     public final static int MAXMEM = 1000;     // 1000K  (=1M)

     public JobList() {
          jobs = new Job[100];    
          jobs[0] = new Job(OSMEM, 0, 0, "operating system");
          jobs[0].empty = false;
          freememory = MAXMEM - OSMEM;
          jobs[1] = new Job(freememory, 1, OSMEM, "empty");
          jobs[1].empty = true;
          numjobs = 2;
     }

     public void paint (Graphics g) {
          g.setColor(Color.black);
          int xedge = 40;
          int yedge = 65;
          int width = 110;
          int overallheight = 400;

          g.drawRect(xedge,yedge,width,overallheight);

          for (int i=0; i<numjobs; i++) {
               int height = (int)(jobs[i].size / (double)MAXMEM * overallheight);
               int ypos = yedge + 
                          (int)(jobs[i].start / (double)MAXMEM * overallheight);
               if (jobs[i].empty)
                    g.setColor(Color.lightGray);
               else
                    g.setColor(Color.yellow);
               g.fillRect(xedge+1,ypos+1,width-1,height-1);
               g.setColor(Color.black);
               writeAddress (g, jobs[i].start, xedge, ypos);
               g.drawLine (xedge,height+ypos,xedge+width,height+ypos);
               int name_y = ypos + height / 2 - 10;
               if (height > 40) {
                    g.drawString(jobs[i].name, xedge+3, name_y);
                    g.drawString(jobs[i].size+"", xedge+3, name_y+14);
               }
               else
                    g.drawString(jobs[i].name+"  "+jobs[i].size+"", 
                                 xedge+3, name_y+15);
          }
          writeAddress (g, (MAXMEM-1), xedge, yedge+overallheight);
     }

     private void writeAddress (Graphics g, int start, int xedge, int ypos) {
          if (start < 10)
               g.drawString(start+"", xedge-10, ypos+5);
          else if (start < 100)
               g.drawString(start+"", xedge-15, ypos+5);
          else if (start < 1000)
               g.drawString(start+"", xedge-25, ypos+5);
     }

     public boolean newjob(int memoryRequired, int fitmethod) {
          if (numjobs >= jobs.length) {
               new Popup("Job table is full!");
               return false;
          }
          if (memoryRequired >= freememory) {
               new Popup("Out of memory!");
               return false;
          }
          int id = nextid++;
          Job job = new Job(memoryRequired, id, 0, "Job "+id);


          if (fitmethod == FIRSTFIT) {
               boolean foundPlace = false;
               for (int i=0; i<numjobs; i++) {
                    if (jobs[i].empty && jobs[i].size == memoryRequired) {
                         job.start = jobs[i].start;
                         jobs[i] = job;
                         foundPlace = true;
                         return true;
                    }
                    if (jobs[i].empty && jobs[i].size > memoryRequired) {
                         for (int j=numjobs; j>i; j--) 
                              jobs[j+1] = jobs[j];
                         jobs[i+1] = jobs[i];
                         job.start = jobs[i+1].start;
                         jobs[i+1].start = job.start + memoryRequired;
                         jobs[i+1].size -= memoryRequired;
                         jobs[i] = job;
                         foundPlace = true;
                         break;
                    }
               }
               if (foundPlace) {
                    numjobs++;
                    freememory -= memoryRequired;
                    return true;
               }
               else {
                    new Popup("Could not find a slot!");
                    return false;
               }
          }

          if (fitmethod == BESTFIT) {
               int pos = -1;    // where to put the new job
               int bestSizeSoFar = MAXMEM;
               boolean foundPlace = false;

               for (int i=0; i<numjobs; i++) {
                    if (jobs[i].empty && jobs[i].size == memoryRequired) {
                         job.start = jobs[i].start;
                         jobs[i] = job;
                         freememory -= memoryRequired;
                         return true;
                    }
                    if (jobs[i].empty && jobs[i].size > memoryRequired) {
                         if (jobs[i].size < bestSizeSoFar) {
                              pos = i;
                              bestSizeSoFar = jobs[i].size;
                              foundPlace = true;
                         }
                    }
               }

               if (bestSizeSoFar >= memoryRequired && foundPlace) {
                    int i = pos;
                    for (int j=numjobs; j>i; j--) 
                         jobs[j+1] = jobs[j];
                    jobs[i+1] = jobs[i];
                    job.start = jobs[i+1].start;
                    jobs[i+1].start = job.start + memoryRequired;
                    jobs[i+1].size -= memoryRequired;
                    jobs[i] = job;
                    numjobs++;
                    freememory -= memoryRequired;
                    return true;
               }
               else {
                    new Popup("Could not find a slot!");
                    return false;
               }
          }
 
          if (fitmethod == WORSTFIT) {
               int pos = -1;    // where to put the new job
               int worstSizeSoFar = 0;
               boolean foundPlace = false;

               for (int i=0; i<numjobs; i++) {
                    if (jobs[i].empty && jobs[i].size == memoryRequired) {
                         job.start = jobs[i].start;
                         jobs[i] = job;
                         freememory -= memoryRequired;
                         return true;
                    }
                    if (jobs[i].empty && jobs[i].size > memoryRequired) {
                         if (jobs[i].size > worstSizeSoFar) {
                              pos = i;
                              worstSizeSoFar = jobs[i].size;
                              foundPlace = true;
                         }
                    }
               }

               if (worstSizeSoFar >= memoryRequired && foundPlace) {
                    int i = pos;
                    for (int j=numjobs; j>i; j--) 
                         jobs[j+1] = jobs[j];
                    jobs[i+1] = jobs[i];
                    job.start = jobs[i+1].start;
                    jobs[i+1].start = job.start + memoryRequired;
                    jobs[i+1].size -= memoryRequired;
                    jobs[i] = job;
                    numjobs++;
                    freememory -= memoryRequired;
                    return true;
               }
               else {
                    new Popup("Could not find a slot!");
                    return false;
               }
          }

          return false;
     }

     public boolean kill (int id) {
          if (id == 0) {
               new Popup("ERROR:  You cannot kill the operating system!");
               return false;
          }
          for (int i=0; i<numjobs; i++)
               if (jobs[i].number == id && !jobs[i].empty) {
                    jobs[i].empty = true;
                    jobs[i].name = "empty";
                    freememory += jobs[i].size;
                    mergeEmpties();
                    return true;
               }
          new Popup("No job with number "+id);
          return false;
     }

     private void mergeEmpties() {
          boolean mustTryAgain;

          while (true) {
               mustTryAgain = false;
               for (int i=0; i<numjobs-1; i++) {
                    if (jobs[i].empty && jobs[i+1].empty) {
                         jobs[i].size += jobs[i+1].size;
                         for (int j=i+1; j<numjobs-1; j++) 
                              jobs[j] = jobs[j+1];
                         numjobs--;
                         mustTryAgain = true;
                         break;
                    }
               }
               if (!mustTryAgain) break;
          }
     }

     public void compact() {
          Job[] newjobs = new Job[jobs.length];
          int numnewjobs = 0;
          int startingaddress = 0;
          int biggestnumber = 0;
          for (int i=0; i<numjobs; i++) {
               if (!jobs[i].empty) {
                    newjobs[numnewjobs++] = new Job(jobs[i].size, jobs[i].number,
                                            startingaddress, jobs[i].name);
                    startingaddress += jobs[i].size;
                    if (jobs[i].number > biggestnumber)
                         biggestnumber = jobs[i].number;
               }
          }
          newjobs[numnewjobs] = new Job(freememory, biggestnumber+1,
                                        startingaddress, "empty");
          newjobs[numnewjobs].empty = true;
          newjobs[numnewjobs].size = freememory;
          newjobs[numnewjobs].name = "empty";
          numnewjobs++;
          jobs = newjobs;
          numjobs = numnewjobs;
     }
}
