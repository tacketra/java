package com.myjayvah.t25mod;
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

public class tsj {
    tsj(){
        String bashDir = "bash bashQtimeBounds.txt";
        try{
            String line =" ";
            Process process = Runtime.getRuntime ().exec (bashDir);
            OutputStream stdin = process.getOutputStream ();
            InputStream stderr = process.getErrorStream ();
            InputStream stdout = process.getInputStream ();
            
            Scanner scan = new Scanner( System.in );
            
            BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
            String input = " ";
            line = reader.readLine() + " ";//we add space to force last number in the
            //bounds string to hit the first if statement in the for loop below
            System.out.println("line after reader = " + line);
            int[] boundsArray = new int[4];
            int count = -1;
            int curNumber = -1;
            for(int i = 0; i < line.length(); i++){
                if(line.substring(i,i+1).equals(" ") || line.substring(i,i+1).equals(",") ){
                    if (curNumber != -1){
                        String tempStr =  line.substring(curNumber, i);
                        int tempInt = Integer.parseInt(tempStr);
                        boundsArray[ count] = tempInt;
                        curNumber = -1;
                    }
                }
                else{
                    if(curNumber == -1){
                        curNumber = i;
                        count++;
                    }
                }
            }
        }catch (java.io.IOException e){
            System.out.println("error running volume adjuster");
        }
    }
    
    public static void main(String[] args){
        tsj t = new tsj();
    }
}