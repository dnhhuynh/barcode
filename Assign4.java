import java.lang.*;
import java.lang.Math;

public class Assign4
{
   public static void main(String args[])
   {
      String[] sImageIn =
         {
               "                                               ",
               "                                               ",
               "                                               ",
               "     * * * * * * * * * * * * * * * * * * * * * ",
               "     *                                       * ",
               "     ****** **** ****** ******* ** *** *****   ",
               "     *     *    ****************************** ",
               "     * **    * *        **  *    * * *   *     ",
               "     *   *    *  *****    *   * *   *  **  *** ",
               "     *  **     * *** **   **  *    **  ***  *  ",
               "     ***  * **   **  *   ****    *  *  ** * ** ",
               "     *****  ***  *  * *   ** ** **  *   * *    ",
               "     ***************************************** ",  
               "                                               ",
               "                                               ",
               "                                               "

         };  

      BarcodeImage name = new BarcodeImage(sImageIn);
      name.displayToConsole();
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
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];

      for(int i = 0; i < MAX_HEIGHT; i++)
      {
         for(int j = 0; j < MAX_WIDTH; j++)
         {
            image_data[i][j] = false;
         }
      }

      int arrayLength = str_data.length;
      int lineLength = str_data[0].length();
      int xHolder = 0, yHolder = 0;
      boolean found = false;

      while (found == false)
      {
         for(int line = arrayLength - 1; line >= 0; line--)
         {
            for(int character = 0; character < lineLength; character++)
            {
               if(str_data[line].charAt(character) == '*')
               {
                  xHolder = character;
                  yHolder = line;
                  found = true;
                  break;
               }
            }
            if(found == true) break;
         }
      }

      for(int row = yHolder; row >= 0; row--)
      {
         for(int col = xHolder; col < lineLength; col++)
         {
            if(str_data[row].charAt(col) == '*')
            {
               image_data[MAX_HEIGHT - (yHolder - row) - 1][col - xHolder] = true;
            }
            else
            {
               image_data[MAX_HEIGHT - (yHolder - row) - 1][col - xHolder] = false;
            }
         }
      }
   }

   /*
    * Returns true if there is a pixel and false if it is blank
    */
   public boolean getPixel(int row, int col)  //Brenna
   {
      boolean pixel;
      if (row > 0 && row <= MAX_HEIGHT && col > 0 && col <= MAX_WIDTH)
      {
         pixel = image_data[row][col];
         return pixel;
      }
      else
      {
         return false;
      }
   }

   public boolean setPixel(int row, int col, boolean value) //Brenna
   {
      if (row > 0 && row <= MAX_HEIGHT && col > 0 && col <= MAX_WIDTH)
      {
         image_data[row][col] = value;
         return true;
      }
      else
      {
         return false;
      }

   }
   public boolean checkSize(String[] data) //Brenna
   {
      int strLen;
      strLen = data.length;

      if( strLen > MAX_WIDTH )
      {
         return false;
      }
      else
      {
         return true;
      }

   }
   public boolean displayToConsole() //Brenna
   {
      int minRow = 0, maxCol = 0;

      //if exceeds maxs then return false

      for (int i = MAX_HEIGHT - 1; i >= 0; i-- )
      {
         if(image_data[i][0] == true)
         { 
            minRow = i;
         }
      }
      for (int j = 0; j < MAX_WIDTH; j++ )
      {
         if(image_data[0][j] == false)
         { 
            maxCol = j;
         }
      }

      if (minRow < MAX_HEIGHT && maxCol > 0 )
      {
         for ( int k = minRow; k < MAX_HEIGHT; k++ )
         {
            for ( int l = 0; l < maxCol; l++ )
            {
               if( image_data[k][l] == true )
               {
                  System.out.print('*');
               }
               else
               {
                  System.out.print(' ');
               }
            }
            System.out.print('\n');
         }
         return true;
      }
      else
      {
         return false;
      }
   }

   // Override clone() method 
   public Object clone()throws CloneNotSupportedException
   {  
      return (BarcodeImage)super.clone();  
   }
}




public class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' '; 
   private BarcodeImage image;
   private String text;
   private int actualWidth, actualHeight;


   //Default Constructor
   public DataMatrix()
   {
      image = new BarcodeImage();
      text = "";
      actualWidth = 0;
      actualHeight = 0;
   }


   //Constructor 
   public DataMatrix(String text)
   {
      image = new BarcodeImage();

      if(!readText(text))
         text = "";

      actualWidth = 0;
      actualHeight = 0;  
   }

   //Constructor 
   public DataMatrix(BarcodeImage image)
   {
      if(!scan(image))
         image = new BarcodeImage();
      else
      {
         actualWidth = 0;
         actualHeight = 0;
      }

      text = "";
   }

   //Mutator for text
   public boolean readText(String text)
   {

      if(text == null)
         return false;

      this.text = text;
      return true;
   }



   //Mutator for image 
   public boolean scan(BarcodeImage image)
   {

      //catches the exception to the BarcodeImage
      try
      {
         this.image = (BarcodeImage)image.clone();
         this.image = (BarcodeImage)image.cleanImage();
         actualWidth = computeSignalWidth();
         actualHeight = computeSignalHeight();
         return true; 
      }
      catch (CloneNotSupportedException ex)
      {
         return false;
      }
      signalWidth = computeSignalWidth(); //-Norma
      signalHeight = computeSignalHeight();
      actualWidth = signalWidth - 2;
      actualHeight = signalHeight - 2;
      return true;
   }
   
   //Accessors for actualWidth and actualHeight -Norma
   public int getActualWidth() 
   { 
      return actualWidth; 
   }
   public int getActualHeight() 
   { 
      return actualHeight; 
   }

   //use the left column to determine height-Norma
   private int computeSignalHeight()
   { 
      int height = 0;
      for (int row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
      {
      if (image.getPixel(row, 0) == true)
      {
         height++;
      }
      return height;
   }
   
   //use the bottom border to determine width -Norma
   private int computeSignalWidth()
   {
      int width = 0;
      for (int col = 0; col < BarcodeImage.MAX_WIDTH; col++)
      {
      if (image.getPixel(BarcodeImage.MAX_HEIGHT - 1, col) == true)
      {
         width++;
      }
      return width;
   }
   
   // displays only the relevant portion of the image,
   //clipping the excess blank/white from the top and right
   public void displayImageToConsole()
   {
      int row, col;
      char temp;
      System.out.println();
      for ( col = 0; col < signalWidth + 2; col++ )
      {
         System.out.print("-");
         System.out.println();
      }
      for (row = BarcodeImage.MAX_HEIGHT - signalHeight; 
      {
         row < BarcodeImage.MAX_HEIGHT; row++)
         System.out.print("|");
      }
      for (col = 0; col < signalWidth; col++)
      {
         temp = boolToChar(image.getPixel(row, col));
         System.out.print(temp);
      }
         System.out.println("|");
      }
      for ( col = 0; col < signalWidth + 2; col++ )
      {
         System.out.print("-");
         System.out.println();
      }
   }
   
   //Creates image from text -Norma
   public boolean generateImageFromText()
   {
      int row, col, digit;
      boolean[] columnVals;
      
      //check that the text is a legal length
      if (text == "" || text.length() > BarcodeImage.MAX_WIDTH - 2)
      {
      return false;
      }
      
      //rely on some member methods to clear the current data, set certain 
      //border elements, and reset height and width members
      clearImage();
      makeFrame();
      scan(image);
      
      //this double loop takes help from a method that converts a char into 
      //an array of bools representing 1s and 0s
      for (col = 1; col < text.length() + 1; col++)
      {
         columnVals = charToBinary(text.charAt(col - 1));
      }
      for (row = BarcodeImage.MAX_HEIGHT - 2, digit = columnVals.length - 1;
      {
         row >= BarcodeImage.MAX_HEIGHT - 9; row--, digit--) 
         image.setPixel(row, col, columnVals[digit]);
      } 
      return true;
    }
    
    //Turns image into text-Norma
    public boolean translateImageToText()
    {
      int row, col, digit;
      char temp;
      
      //clears the text field then fills it
      readText("");
      for (col = 1; col < signalWidth - 1; col++)
      {
         temp = 0;
         for (row = BarcodeImage.MAX_HEIGHT - 2, digit = 0; 
         {
            row > BarcodeImage.MAX_HEIGHT - signalHeight; row--, digit++)
         }
         if (image.getPixel(row, col) == true)
         {
            temp += (int)Math.pow(2, digit); 
            text += temp;
         }
         return true;
    }
    
    //sets image to white=false -Norma
    private void clearImage()
    {
       int row, col; 
       for (row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
       {
       for (col = 0; col < BarcodeImage.MAX_WIDTH; col++)
       {
          image.setPixel(row, col, false)
       }
    }
   
