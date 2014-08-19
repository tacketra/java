/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package src.com.myjayvah.guiFrame;

import java.awt.Color;
import java.awt.Robot;
import java.awt.AWTException;
import java.io.File;
import java.io.*;
import java.util.Scanner;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


/* FrameDemo.java requires no other files. */
public class FrameDemo extends JPanel implements ActionListener {
    String current25Version = "alpha";
    String killNineString = "kill -9 ";//kill all colorpickerdemo2 proccesses when stop called from gui(this class)
    String programPath ="bash runT25Program";
    JButton startButton, stopButton;
    JFrame frame = new JFrame("t-25 sound mod");
    FrameDemo(){}
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    public ImageIcon createImageIcon(String path,
                                     String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private void createAndShowGUI() {
        //String path = "images/pumpkin.gif";
        String path = "images/t25Cover.jpeg";
        //Create and set up the window.
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(300, 200));
        //frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        
        //frame.setIconImage(new ImageIcon("images/pumpkin.gif").getImage());
        //ImageIcon icon = new ImageIcon();
        //ImageIcon icon = createImageIcon("images/pumpkin.gif", "yep it is pumpkin");
        ImageIcon icon = new ImageIcon(getClass().getResource(path) ,"yep we pumpin this kin");
        JLabel label1 = new JLabel("", icon, JLabel.CENTER);
        frame.getContentPane().add(label1, BorderLayout.CENTER);
        
        //Set up the combo box (alpha,beta, gamma)
        String[] t25AlphaBetaGamma = {"-none selected-", "alpha", "beta", "gamma"};
        JComboBox t25AbgList = new JComboBox(t25AlphaBetaGamma);
        t25AbgList.setSelectedIndex(0);
        t25AbgList.addActionListener(this);
        frame.getContentPane().add( t25AbgList, BorderLayout.PAGE_START);
        
        //set up start and stop buttons
        startButton = new JButton("Start !");
        startButton.setActionCommand("disableStart");
        startButton.addActionListener(this);
        frame.getContentPane().add( startButton, BorderLayout.PAGE_END);
        
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }
    
    /** Listens to the combo box. */
    /*
     public void actionPerformed(ActionEvent e) {
     JComboBox cb = (JComboBox)e.getSource();
     current25Version = (String)cb.getSelectedItem();
     System.out.println(current25Version);
     }*/
    
    
    public void actionPerformed(ActionEvent e) {
        if ("disableStart".equals(e.getActionCommand()) ) {
            /*startButton.setVisible(false);
             stopButton.setVisible(true);
             System.out.println("start button pressed");*/
            startButton.setVisible(false);
            startButton = null;
            stopButton = new JButton("Stop !");
            stopButton.setActionCommand("disableStop");
            stopButton.addActionListener(this);
            frame.getContentPane().add( stopButton, BorderLayout.PAGE_END);
            System.out.println("START button pressed");
            try{
                //Runtime.getRuntime().exec(programPath + current25Version);
                String tVersionStr = programPath + current25Version + ".txt";
                System.out.println(tVersionStr);
                Runtime.getRuntime().exec(tVersionStr);
            }catch( java.io.IOException erorr){
                System.out.println("error trying to run Cpd2 from FrameDemo start button");
            }
        }
        else if( "disableStop".equals(e.getActionCommand()) ){
            stopButton.setVisible(false);
            stopButton = null;
            startButton = new JButton("Start !");
            startButton.setActionCommand("disableStart");
            startButton.addActionListener(this);
            frame.getContentPane().add( startButton, BorderLayout.PAGE_END);
            System.out.println("STOP button pressed");
            
            //kill all ColorPickerDemo2 processes, since gui called the program
            //to stop we kill all colorpickerdemo2 processes in the background
            try{
                String line =" ";
                Process process = Runtime.getRuntime ().exec("jps -v" );
                OutputStream stdin = process.getOutputStream ();
                InputStream stderr = process.getErrorStream ();
                InputStream stdout = process.getInputStream ();
                
                Scanner scan = new Scanner( System.in );
                
                BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
                String input = " ";
                //line = reader.readLine();
                //System.out.println(" line is = " + line);
                while( line != null){
                    line = reader.readLine();
                    //System.out.println(line);
                    if( line == null){ break; }
                    else if( line.contains("ColorPickerDemo2")){
                        String CpdProcessId = line.substring( 0, line.indexOf("ColorPickerDemo2") -1);
                        //System.out.println( CpdProcessId);
                        killNineString+= CpdProcessId + " ";
                        //add ColorPickerDemo2 process id's that are running in the background to the kill -9 statement, so when we run kill -9 all Cpd2's will be killed since stop is called
                        
                    }
                }
                //System.out.println("lines all read");
                try{
                    Runtime.getRuntime().exec(killNineString);
                }catch(java.io.IOException err){
                    System.out.println("error killing -9 all Cpd2s in background");
                }
            }catch( java.io.IOException erorr){
                System.out.println("error reading all Cpds running in backgroudn");
            }
        }
        else {
            JComboBox cb = (JComboBox)e.getSource();
            current25Version = (String)cb.getSelectedItem();
            System.out.println(current25Version);
        }
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        final FrameDemo fd = new FrameDemo();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                fd.createAndShowGUI();
            }
        });
    }
}