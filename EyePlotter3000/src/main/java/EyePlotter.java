/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admad
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.io.IOException;

public class EyePlotter {
    
    public static void main (String[] args) throws FileNotFoundException {
        
        String stringToIgnore = "2d statistical"; // compared to first line
        double EyePlotData[][] = new double[300][100]; // making array bigger than necessary ( ._.)
        int rowCount = 0;
        int i = 0; // for loop
        double min = 1e99;
        double max = -1e99;
        
        File file = new File(args[0]); // opened the user's file
        Scanner scan = new Scanner(file); // creating file scanner
        
        while (scan.hasNext()) { // scanned file until the end
            
            String strcurrent = scan.nextLine(); // reading line from the file
            String split[] = strcurrent.split(","); // split the string on commas
            
            int splitLength = split.length; // # of elements into which the string was split
            
            if(splitLength > 5) { // gets rid of unused lines (too short)
                
                int isFirstLine = split[0].compareTo(stringToIgnore); // compares first line to "2d statistical"
                
                if(isFirstLine != 0) { // ignore the line which contains "2d statistical"
                    
                    for (i=1; i<splitLength; i++) { // goes through all split elements
                        
                        double numConverted = Double.parseDouble(split[i]); // converts current split element to a double
                        double numLog = Math.log10(numConverted);
                        EyePlotData[rowCount][i-1] = numLog;
                        
                        if(numLog < min) min = numLog;
                        if(numLog > max) max = numLog;
                        
//                        System.out.print(numConverted + " ");
                        
                    }
                    
                    rowCount++;
                    
                }

            }
            
//            System.out.println("");
            
        }
        
        System.out.println("min & max: " + min + " " + max);
        System.out.println("rowCount & i: " + rowCount + " " + i);
        
        int ncol =  
        
        double range = max - min;
        double range1color = range / (double)(ncol);
        
        int colors[][] = new int[ncol+2][3];
        int rc = 0;
        int gc = 0;
        int bc = 0;
        double hue_step = 247. / (double)(ncol); // in degrees 
        double hue = 247; // starting from blue
        // source: rapidtables.com/convert/color/hsv-to-rgb.html
        for (int ci = 0; ci < ncol+2; ci ++)
        {
            
            double conv_x = 1 - Math.abs((hue/60)%2 - 1);
            int bin_x = (int)(conv_x * 255);
            System.out.println("hue_step: " + hue_step + " hue: " + hue + " conv_x: " + conv_x + " bin_x: " + bin_x);
            if (hue >= 0 && hue < 60)
            {
                colors[ci][0] = 0;
                colors[ci][1] = bin_x;
                colors[ci][2] = 0xff;
            } else if (hue >= 60 && hue < 120)
            {
                colors[ci][0] = 0;
                colors[ci][1] = 0xff;
                colors[ci][2] = bin_x;
            } else if (hue >= 120 && hue < 180)
            {
                colors[ci][0] = bin_x;
                colors[ci][1] = 0xff;
                colors[ci][2] = 0;
            } else if (hue >= 180 && hue < 240)
            {
                colors[ci][0] = 0xff;
                colors[ci][1] = bin_x;
                colors[ci][2] = 0;
            } else if (hue >= 240 && hue < 300)
            {
                colors[ci][0] = 0xff;
                colors[ci][1] = 0;
                colors[ci][2] = bin_x;
            } else if (hue >= 300 && hue < 360)
            {
                colors[ci][0] = bin_x;
                colors[ci][1] = 0;
                colors[ci][2] = 0xff;
            } 
            
            hue -= hue_step;
        }
        
        BufferedImage Eye = new BufferedImage(i-1, rowCount, BufferedImage.TYPE_INT_ARGB);
        File generatedImage = null;
        
        for(int y = 0; y < rowCount; y++) {
            
            for(int x = 0; x < i-1; x++) {
                
                double currentp = EyePlotData[y][x];
                int k = (int) (( currentp - min ) / range1color);
                
                int a = 255;
                int r = colors[k][2];
                int g = colors[k][1];
                int b = colors[k][0];
                
                int p = (a<<24) | (r<<16) | (g<<8) | b;
                
                Eye.setRGB(x, y, p);
                
            }
            
        }
        
        try{
            
            generatedImage = new File(args[0] + ".png");
            ImageIO.write(Eye, "png", generatedImage);
            
        } catch(IOException e){
            
            System.out.println("Error: " + e);
            
        }
        
    }
    
}
