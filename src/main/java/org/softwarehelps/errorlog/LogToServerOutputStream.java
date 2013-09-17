/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.softwarehelps.errorlog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

/**
 * LogToServerOutputStream is a ByteArrayOutputStream intended to be used as a 
 * destination in java.util.logging.  Data that is output to it is send to
 * error-log-server.appspot.com in chunks one second apart, and one second after
 * the first data is written to the stream.  The chunks also all include a 
 * random number and a byte offset for the chunk so that the chunks can be
 * reassembled by the Error Log server.  The Error Log server will use the IP
 * address combined with the random number to uniquely identify a collection
 * of log messages that belong to the same application.
 * 
 * @author Peter Dobson <pdobson@softwarehelps.com>
 */
public class LogToServerOutputStream extends ByteArrayOutputStream implements Runnable {
    
    private int limit = 4000; // longest log stream for one program execution
    private int wrote = 0;
    private String url = "http://localhost:8080/post";
    // private String url = "http://error-log-server.appspot.com/post";
    private String program;
    
    public LogToServerOutputStream(String program, String url, int limit) {
        this.program = program;
        this.url = url;
        this.limit = limit;
    }
    
    public LogToServerOutputStream(String program) {
        this.program = program;        
    }
    
    private synchronized void shutOffOutput() {
        limit = 0;
    }
    
    @Override
    public void write(int b) {
        synchronized (this) {
            if (wrote++ >= limit)
                return;
        }
        
        super.write(b);
        startSending();
    }
    
    @Override
    public void write(byte[] b) throws IOException {
        synchronized (this) {
            if ((wrote+=b.length) >= limit)
                return;
        }

        super.write(b);
        startSending();
    }
    
    @Override
    public void write(byte[] b,
                  int off,
                  int len) {
        synchronized (this) {
            if ((wrote+=len) >= limit)
                return;
        }
        
        super.write(b, off, len);        
        startSending();
    }
    
    @Override
    public void flush()
           throws IOException {
        super.flush();
        endSending();
    }
    
    @Override
    public void close()
           throws IOException {
        super.close();
        endSending();
    }
    
    private URL logServer;
    private Thread thread;
    private long random;
    private boolean endFlag = false;
    
    private void startSending() {           
        synchronized (this) {
            if (thread != null) {
                // already started
                return;
            }
            thread = new Thread(this);
            random = new Random().nextLong();
            try {
                logServer = new URL(url);
            } catch (MalformedURLException ex) {
                shutOffOutput();
            }
        }
        thread.start();
    }
    
    private void endSending() {
        synchronized(this) {
            if (thread == null) {
                // never started, nothing to do
                return;
            }
            endFlag = true;
            while (this.count > 0) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    // ignore
                }
            }            
        } 
    }

    private void send(String logText) {
        String data;
        try {
            long key = new String(program + random).hashCode();
            data = String.format("program=%s&random=%d&security=%d"
                    + "&offset=%s&message=%s", 
                    URLEncoder.encode(logText,"UTF-8"),
                    random, URLEncoder.encode(logText,"UTF-8"));
            Http.post(logServer, data);
        } catch (UnsupportedEncodingException ex) {
            shutOffOutput();
        }        
    }
    
    public void run() {
        String logText;
        while (!endFlag) {
            try {
                // first sleep for a second to let buffer fill up
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                // ignore
            }
            synchronized (this) {
                while (this.count == 0) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                }
                logText = this.toString();
                this.reset();
            }
            send(logText);
        }
    }    
}
