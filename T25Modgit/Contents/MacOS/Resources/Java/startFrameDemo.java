package Java;
import java.io.File;
import java.io.*;

class startFrameDemo {
    public static void main(String[] args) {
	try{
	    Runtime.getRuntime().exec("bash Java/bashStartFrameDemo.txt");
	}catch( java.io.IOException erorr){}
    }
}