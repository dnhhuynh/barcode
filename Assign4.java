import java.lang.*;

public class Assign4
{
   public static void main(String args[])
   {
      
   }
}

interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromTest();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}

class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   private boolean[][] image_data;
   
   // Default Constructor
   BarcodeImage()
   {
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      
      for(int i = 0; i < MAX_HEIGHT; i++)
      {
         for(int j = 0; j < MAX_WIDTH; j++)
         {
            image_data[i][j] = false;
         }
      }
   }
   
   // Constructor
   BarcodeImage(String[] str_data)
   {   
      for(int col = 1; col < MAX_WIDTH; col++)
      {
         for(int row = 1; row < MAX_HEIGHT; row++)
         {
            
         }
      }
   }
}

class DataMatrix implements BarcodeIO
{
   
}
