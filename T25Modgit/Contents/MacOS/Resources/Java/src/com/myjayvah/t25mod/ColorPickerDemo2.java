package src.com.myjayvah.t25mod;
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

//make a scpt file to get information or modify app/start it etc. very cool
//due below to get all accesible properties for an application, example is quicktimels
/*
 tell application "QuickTime Player"
 tell front document
 get properties
 end tell
 end tell
 */
public class ColorPickerDemo2 {
    final static long SECOND = 1000000000;//nano
    final static long MOVE_LENGTH =4000;//mil,how long volume will be on, so t-25 move can be heard
    final static long TOTAL_TIME = SECOND*20;
    
    final static double lWhiteStartX = .115625, lWhiteEndX = .16, rWhiteStartX = .245;
    final static double rWhiteEndX= .289, wTopY = .691, wBottomY = .711, bStartX = .115625, bEndX = .275, bY = .793;
    //below should be in all caps!!!
    final static String[][] t25TitleArray = new String[][] { {"Ab Intervals.mp4","alpha"}, {"Cardio.mp4","alpha"}, {"Core Cardio.mp4","beta"} , {"Core Speed.mp4","idk"} , {"Dynamic Core.mp4","beta"} , {"Extreme Cardio.mp4","gamma"} , {"Lower Focus.mp4","alpha"} , {"Pyramid.mp4","gamma"} , {"Rip't Circuit.mp4","beta"} , {"Rip't Up.mp4","gamma"} , {"Speed 1.0.mp4","alpha"} , {"Speed 2.0.mp4","beta"} , {"Speed 3.0.mp4","gamma"} , {"Total Body Circuit.mp4","alpha"} , {"Upper Focus.mp4","beta"}};
    String currentVideoName = "";//total body circiutm.mp4 and below would be alpha
    String currentVideoVersion = ""; //alpha,beta or gamma
    final Grb yellowGrb = new Grb(195, 175, 0, 255, 255, 20);
    final Grb orangeGrb = new Grb(85, 200, 0, 130, 255, 20);//RGB: 244, 93, 0
    //orange examples RGB: 244, 94, 0
    final Grb greenGrb = new Grb(150, 30, 40, 240, 55, 85);//HAVE NOT TESTED
    //green examples: RGB: 28, 202, 21.  RGB: 28, 212, 0.  RGB: 28, 213, 41.
    //RGB: 30, 213, 0
    final Grb whiteGrb = new Grb(220, 220, 220, 255, 255, 255);
    String myColorName = " ";
    Grb myColor = new Grb(0,0,0,0,0,0);
    int[] qTimeWindowBoundsArray = new int[4];
    int qTimeWindowWidth = 0, qTimeWindowHeight = 0, qTimeWindowWidthOffset = 0, qTimeWindowHeightOffset = 0;// like the window could be 300x500 but starts 100 pixels from the top of the screen & over 200 or something etc.
    //0,1 = x,y of upper left corner position in screen. 2,3 = x,y lower right corner
    int leftWhiteStartX =0, leftWhiteEndX =0, rightWhiteStartX =0, rightWhiteEndX =0, whiteStartY=0 ,  whiteEndY=0, barStartX = 0, barEndX = 0, barY = 0;
    
    ColorPickerDemo2(){
        //myColorName = col; //used to pass String col into ColorPickerDemo2 from command line
        String bashqTimeBounds = "bash bashQtimeBounds.txt";
        String bashqTimeName = "bash bashQtimeName.txt";
        try{//putting the winodw bounds of quicktime t25 window into qTimeWindowBoundsArray
            String line =" ";
            Process process = Runtime.getRuntime ().exec(bashqTimeBounds );
            OutputStream stdin = process.getOutputStream ();
            InputStream stderr = process.getErrorStream ();
            InputStream stdout = process.getInputStream ();
            
            Scanner scan = new Scanner( System.in );
            
            BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
            String input = " ";
            line = reader.readLine() + " ";//we add space to force last number in the
            //bounds string to hit the first if statement in the for loop below
            //System.out.println("line after reader = " + line);
            System.out.println(" line is = " + line);
            int count = -1;
            int curNumber = -1;
            for(int i = 0; i < line.length(); i++){
                if(line.substring(i,i+1).equals(" ") || line.substring(i,i+1).equals(",") ){
                    if (curNumber != -1){
                        String tempStr =  line.substring(curNumber, i);
                        int tempInt = Integer.parseInt(tempStr);
                        qTimeWindowBoundsArray[ count] = tempInt;
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
            //gross lines below untill the try catch statement are just initiliazing the bounds of the current quicktimeplayer window
            //lowerright x cordinate - upper left x cordinate, then same for y below
            qTimeWindowWidth = qTimeWindowBoundsArray[2] - qTimeWindowBoundsArray[0];
            qTimeWindowWidthOffset = qTimeWindowBoundsArray[0]; //upper left x of 4 means = 4
            qTimeWindowHeight = qTimeWindowBoundsArray[3] - qTimeWindowBoundsArray[1];
            qTimeWindowHeightOffset = qTimeWindowBoundsArray[1];
            
            double DleftWhiteStartX =0, DleftWhiteEndX =0, DrightWhiteStartX =0, DrightWhiteEndX =0, DwhiteStartY=0 , DwhiteEndY =0, DbarStartX = 0, DbarEndX = 0, DbarY = 0;
            
            DleftWhiteStartX = (lWhiteStartX*qTimeWindowWidth) + qTimeWindowWidthOffset;
            DleftWhiteEndX = (lWhiteEndX*qTimeWindowWidth) + qTimeWindowWidthOffset;
            
            DrightWhiteStartX = (rWhiteStartX*qTimeWindowWidth) + qTimeWindowWidthOffset;
            DrightWhiteEndX = (rWhiteEndX*qTimeWindowWidth) + qTimeWindowWidthOffset;
            
            DwhiteStartY = (wTopY*qTimeWindowHeight) + qTimeWindowHeightOffset;
            DwhiteEndY = (wBottomY* qTimeWindowHeight) + qTimeWindowHeightOffset;
            
            DbarStartX = (bStartX * qTimeWindowWidth) + qTimeWindowWidthOffset;
            DbarEndX =   (bEndX * qTimeWindowWidth) + qTimeWindowWidthOffset;
            DbarY =     (bY * qTimeWindowHeight) + qTimeWindowHeightOffset;
            
            leftWhiteStartX =  (int) DleftWhiteStartX;
            leftWhiteEndX = (int) DleftWhiteEndX;
            rightWhiteStartX = (int) DrightWhiteStartX;
            rightWhiteEndX = (int) DrightWhiteEndX;
            whiteStartY = (int) DwhiteStartY;
            whiteEndY = (int) DwhiteEndY;
            barStartX = (int) DbarStartX;
            barEndX = (int) DbarEndX;
            barY = (int) DbarY;
            
        }catch (java.io.IOException e){
            System.out.println("error getting bounds of current t25 video");
        }
        //get name of current video playing e.g. total body circuit.mp4, speed 3.0.mp4 etc.
        getAndSetVideoName();
    }
    
    public class Grb{
        long green, red, blue, greenEnd, redEnd, blueEnd; //total green, red, blue
        Grb(){//default constructor
            setGrb(0,0,0,0,0,0);
        };
        
        Grb(long green, long red, long blue, long greenEnd, long redEnd, long blueEnd){
            setGrb(green, red, blue, greenEnd, redEnd, blueEnd);
        }
        
        void setGrb(long greenV, long redV, long blueV, long greenEndV, long redEndV, long blueEndV){
            green = greenV;
            red = redV;
            blue = blueV;
            greenEnd = greenEndV;
            redEnd = redEndV;
            blueEnd = blueEndV;
        }
        
        /*
         boolean compareGrb(long green, long red, long blue){
         if(Math.abs(this.green - green) <= 1000 && Math.abs(this.red - red) <= 1000 && Math.abs(this.blue - blue) <= 1000 ){
         return true;
         }
         return false;
         }*/
        
        
    }
    
    String getAndSetVideoName(){
        String bashqTimeName = "bash bashQtimeName.txt";//script that grabs name from the current t25 video playing in quickTimePlayer
        try{
            String line =" ";
            Process process = Runtime.getRuntime ().exec(bashqTimeName );
            OutputStream stdin = process.getOutputStream ();
            InputStream stderr = process.getErrorStream ();
            InputStream stdout = process.getInputStream ();
            
            Scanner scan = new Scanner( System.in );
            
            BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
            line = reader.readLine() ;
            System.out.println(" name of current video is: " + line);
            currentVideoName = line; //get name of current video from qtimeplayer
            for(int i = 0; i < ColorPickerDemo2.t25TitleArray.length; i++){
                if( ColorPickerDemo2.t25TitleArray[i][0].equals(currentVideoName) ){
                    currentVideoVersion = ColorPickerDemo2.t25TitleArray[i][1];
                    break;//get version value of current video e.g. speed 3.0 = gamma
                }
            }
            myColorName = currentVideoVersion; //lazy need to switch myCOlorName with this in other parts of code, other methods
            System.out.println("name of current version is: " + myColorName);
        }catch (java.io.IOException e){
            System.out.println("error getting name of t25 video playing");
        }
        return currentVideoName;//return name of current t25 video
    }
    
    //whitebox is the next move that appears above the current move counter for 5 seconds and after
    //it dissipears the next move starts, it is white from leftwhite start - end then says next move
    // in black letters, then after that is rightwhitestartx til endx.
    //x 140-200 is left side of white box, right x is 315-370
    //y is 530-550.
    /*boolean checkWhiteBox(  int leftWhiteStartX , int leftWhiteEndX , int rightWhiteStartX , int rightWhiteEndX , int whiteStartY , int whiteEndY ){*/
    boolean checkWhiteBox(){//this should use checkNextPixelColor, neater but slower
        try {
            Robot robot = new Robot();
            Color color = robot.getPixelColor(1000, 568);
            
            for( int y = whiteStartY; y < whiteEndY ; y++){
                for(int x = leftWhiteStartX; x < leftWhiteEndX; x++){
                    color = robot.getPixelColor(x,y);
                    if ( color.getGreen() <= 220 && color.getRed() <=220 && color.getBlue() <= 220){
                        // System.out.println( "g: " + color.getGreen() + ", r: " + color.getRed() + ", b: " + color.getBlue() );
                        return false; //if the rgb all have value of 255 then color of pixel is white
                    }
                    x+= ( (leftWhiteEndX - leftWhiteStartX) /5 );//we will just check 5 vals in range
                }
                y+= (whiteEndY - whiteStartY )/3;// 3 vals in range
            }
            
            for( int y = whiteStartY; y < whiteEndY ; y++){
                for(int x = rightWhiteStartX; x < rightWhiteEndX; x++){
                    color = robot.getPixelColor(x,y);
                    if ( color.getGreen() <= 220 && color.getRed() <=220 && color.getBlue() <= 220){
                        return false; //if the rgb all have value of 255 then color of pixel is white
                    }
                    x+= ( (rightWhiteEndX - rightWhiteStartX) /5 );
                }
                y+= (whiteEndY - whiteStartY )/3;
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    //for the yellow bar that fills up it is x 138 -374, y is 608, the end x should really be 325
    //because it only ever fills the full bar when t25 is switching moves at that exact second
    boolean checkBar(){
        try {
            Robot robot = new Robot();
            Color color = robot.getPixelColor(1000, 568);
            for(int x = barStartX ; x < barEndX; x++){
                color = robot.getPixelColor(x, barY);
                System.out.print("(" + x + "," + barY + ") ");
                if (checkNextMovePixels(color) == false){
                    //System.out.println("checkBar g: " + color.getGreen() + ", r: " + color.getRed() + ",b: " + color.getBlue() + " , x= " + x + " ,yellowbary= " +barY);
                    return false;
                }
                x+= ( (barEndX - barStartX) /5 );//we will just check 5 vals in range
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    /*
     takes in a color object and color string(yellow, orange, green, white) as
     perameters, then determines if the color object given is close enough to the
     color passed in with it. If the color object is yellow enough or whatever
     color than return true else false. E.g. t25 alpha would pass in yellow to
     check if the bar to indicate the next move is mostly yellow, indicating we
     are about to go to the next move, or if the up next white block above the
     timer is visible, also indiciating next move (when white is passed in).
     */
    boolean checkNextMovePixels(Color color){
        if (color.getGreen() >= myColor.green && myColor.greenEnd >= color.getGreen() &&  color.getRed() >= myColor.red && myColor.redEnd >= color.getRed()  && color.getBlue() >= myColor.blue && myColor.blueEnd >= color.getBlue() ){
            
            System.out.println("we true!!!!!");
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("green: " + color.getGreen() + ", red: " + color.getRed() + ", blue: " + color.getBlue() );
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            
            return true;
        }
        System.out.println("FALSE -  green: " + color.getGreen() + ", red: " + color.getRed() + ", blue: " + color.getBlue() );
        return false;
    }
    
    /*
     calls the bash file(.txt) for up  if up == true , down else. The bash file
     just contains osascript up.scpt or down.scpt which goes into the qtimescript
     directory and runs the correct .scpt (either tell qtime max volume or mute it)
     */
    void volumeUp(boolean up){
        //String bashDir = "bash com/myjayvah/bashstuff/bashQtimeVol";
        String bashDir = "bash bashQtimeVol";
        /*the bashu txt that osascripts to run the correct tell script for qtime for vol up/down is back one directory then into bashstuff*/
        try{
            if( up == true){
                bashDir+="Up.txt";
                //bashDir = "mkdir heyMan";
                //System.out.println(bashDir);
                Runtime.getRuntime().exec(bashDir);
                
            }
            else{
                //Runtime.getRuntime().exec("bash bashQtimeVolDown.txt");
                bashDir+="Down.txt";
                //bashDir = "mkdir com/myjayvah/bashstuff/yepper";
                //System.out.println("bashDir: " + bashDir);
                Runtime.getRuntime().exec(bashDir);
            }
        }catch (java.io.IOException e){
            System.out.println("error running volume adjuster");
        }
    }
    
    //returns current seconds of video just starting would be 0.0 and 1:20 would be 80.0 etc.
    double getVideoCurrentSeconds(){//return how long the video has been playing in seconds
        String bashqTimeDuration = "bash bashQtimeDuration.txt";
        double duration = 0;
        try{//putting the winodw bounds of quicktime t25 window into qTimeWindowBoundsArray
            String line =" ";
            Process process = Runtime.getRuntime ().exec( bashqTimeDuration );
            OutputStream stdin = process.getOutputStream ();
            InputStream stderr = process.getErrorStream ();
            InputStream stdout = process.getInputStream ();
            Scanner scan = new Scanner( System.in );
            BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
            line = reader.readLine(); //duration string
            duration = Double.parseDouble(line);//convert duration (as a string) to duration int
        }catch (java.io.IOException e){
            System.out.println("error running volume adjuster");
        }
        return duration;
    }
    
    void startVideo(int minutes){
        //System.out.println("my color name is " + myColorName);
        System.out.println("t25 version is: " + myColorName);
        if( myColorName.equals("white") ){
            myColor = whiteGrb;
        }
        else if(myColorName.equals("gamma") ){
            myColor = greenGrb;
        }
        else if(myColorName.equals("beta") ){
            myColor = orangeGrb;
        }
        else if(myColorName.equals("alpha") ){
            myColor = yellowGrb;
        }
        
        //System.out.println("green: " + myColor.green + " , red: " + myColor.red + " , blue: " + myColor.blue + " , greenEnd: " + myColor.greenEnd);
        /*
         System.out.println("whitebox = " + checkWhiteBox(140, 200, 315, 370, 540, 555) );
         System.out.println("checkBar = " + checkBar(150, 325, 615) );
         */
        
        
        long startTime = System.nanoTime();
        long videoTime = minutes * (SECOND*60); //second*60 = 1 minute
        //now that I have a qTimeDuration.scpt I can just get length of video left
        volumeUp(false);//make video init quiet untill actual 25 minute workout starts
        while( getVideoCurrentSeconds() < 25){
            try{
                Thread.sleep(1000);//sleep a second and try again
            }catch( java.lang.InterruptedException e){
                System.out.println("error trying to sleep in while loop for video duration in startVideo");
            }
        }
        double currentSeconds = getVideoCurrentSeconds();
        if ( currentSeconds >= 25 && currentSeconds <= 35){
            volumeUp(true);
            try{
                Thread.sleep(10000);//sleep 10 seconds( usually actual 25min workout starts between 25-35
            }catch( java.lang.InterruptedException e){
                System.out.println("error trying to sleep in while loop for video duration in startVideo");
            }
            volumeUp(false);//workout starting, volume will come back now only for 5 seconds for each move
        }
        
        //while( System.nanoTime() - startTime  < videoTime){//until video has fully played, e.g. minutes is 25, while this program has run < 25 minutes
        while( getVideoCurrentSeconds() < 1535){//25 minute workout = 1500 seconds, + 25-35 secs before workout
            //while(true){
            //WHITE: x 140-200 is left side of white box, right x is 315-370, y is 530-550.
            //Yellow BAR : x 138 -374, y is 608, the end x should really be 325
            
            if( checkWhiteBox() == true && checkBar() == true ){
                //change to 33 lower for doul monitors
                //System.out.println("about to change t-25 moves");
                //this if means next move box popped up, next move starts in 5 sec
                try{
                    Thread.sleep(4000);
                }catch( java.lang.InterruptedException e){
                    System.out.println("error trying to sleep");
                }
                volumeUp(true); //play sound for 4 seconds so new move can be heard
                try{
                    Thread.sleep(4000);
                }catch( java.lang.InterruptedException e){
                    System.out.println("error trying to sleep");
                }
                volumeUp(false);//just played move, sound go back to mute
            }
        }
    }
    
    
    void getRow(){
        try{
            //yellow row is 140-370 , 608
            Robot robot = new Robot();
            Color color = robot.getPixelColor(1000, 568);
            //int startX = 150, endX = 370; top box white that says next move
            //int startY = 510, endY = 525;
            int startX = 137, endX = 370, startY = 608, endY = 609; //yellow bar that fills up
            long  greenTotal = 0, blueTotal = 0, redTotal = 0;
            for( int y = startY; y < endY ; y++){
                for(int x = startX; x < endX; x++){
                    color = robot.getPixelColor(x,y);
                    //System.out.println("g: " + color.getGreen() + ", r: " + color.getRed() + ", b: " + color.getBlue() );
                    greenTotal += color.getGreen();
                    redTotal += color.getRed();
                    blueTotal += color.getBlue();
                    //x+=10;
                }
                //System.out.println();
                //y+=5;
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        /*try{
         Thread.sleep(4000);
         }catch ( java.lang.InterruptedException e ){}
         */
        System.out.println("starting t25 SOUND mod");
        
        //ColorPickerDemo2 cpd = new ColorPickerDemo2((String) args[0] );
        ColorPickerDemo2 cpd = new ColorPickerDemo2();
        cpd.startVideo(25);
        System.out.println("VIDEO OVER");
    }
}