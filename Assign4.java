import java.lang.*;
import java.lang.Math;

public class Assign4
{
	public static void main(String[] args)
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
	            
	         
	      
	      String[] sImageIn_2 =
	      {
	            "                                          ",
	            "                                          ",
	            "* * * * * * * * * * * * * * * * * * *     ",
	            "*                                    *    ",
	            "**** *** **   ***** ****   *********      ",
	            "* ************ ************ **********    ",
	            "** *      *    *  * * *         * *       ",
	            "***   *  *           * **    *      **    ",
	            "* ** * *  *   * * * **  *   ***   ***     ",
	            "* *           **    *****  *   **   **    ",
	            "****  *  * *  * **  ** *   ** *  * *      ",
	            "**************************************    ",
	            "                                          ",
	            "                                          ",
	            "                                          ",
	            "                                          "

	      };
	     
	      BarcodeImage bc = new BarcodeImage(sImageIn);
	      DataMatrix dm = new DataMatrix(bc);
	     
	      // First secret message
	      dm.translateImageToText();
	      dm.displayTextToConsole();
	      dm.displayImageToConsole();
	      
	      // second secret message
	      bc = new BarcodeImage(sImageIn_2);
	      dm.scan(bc);
	      dm.translateImageToText();
	      dm.displayTextToConsole();
	      dm.displayImageToConsole();
	      
	      // create your own message
	      dm.readText("What a great resume builder this is!");
	      dm.generateImageFromText();
	      dm.displayTextToConsole();
	      dm.displayImageToConsole();
	   }
}

interface BarcodeIO
{
	public boolean scan(BarcodeImage bc);
	public boolean readText(String text);
	public boolean generateImageFromText();
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

class DataMatrix implements BarcodeIO
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
			this.text = "";

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

		if(text == null) return false;

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
		}
		catch (CloneNotSupportedException ex)
		{

		}

		cleanImage();
		actualWidth = computeSignalWidth();
		actualHeight = computeSignalHeight();

		return true;
	}

	//Accessors
	public int getWidth()
	{
		return actualWidth;
	}

	public int getHeight()
	{
		return actualHeight;
	}

	private int computeSignalWidth()
	{	
		int min_Width = 0;

		for(int i = BarcodeImage.MAX_WIDTH - 1; i >= 0; i--)
		{
			if(image.getPixel(BarcodeImage.MAX_HEIGHT - 1, 0) == true) min_Width = i;
		}

		return min_Width;
	}

	private int computeSignalHeight()
	{
		int min_Height = 0;

		for(int i = 0; i < BarcodeImage.MAX_HEIGHT; i++)
		{
			if(image.getPixel(i, 0) == true) min_Height = i;
		}

		return BarcodeImage.MAX_HEIGHT - min_Height;
	}

	private void cleanImage()
	{
		for(int row = BarcodeImage.MAX_HEIGHT - actualHeight; row >= 0; row--)
		{
			for(int col = BarcodeImage.MAX_WIDTH - actualWidth; col < BarcodeImage.MAX_WIDTH; col++)
			{
				image.setPixel(BarcodeImage.MAX_HEIGHT - (actualHeight - row) - 1, col - actualWidth, image.getPixel(row, col));
			}
		}
	}

	public void displayImageToConsole()
	{
		int row, col;
		char temp;
		System.out.println();
		for ( col = 0; col < actualWidth + 2; col++ )
			System.out.print("-");
		System.out.println();
		for (row = BarcodeImage.MAX_HEIGHT - actualHeight; 
				row < BarcodeImage.MAX_HEIGHT; row++)
		{
			System.out.print("|");
			for (col = 0; col < actualWidth; col++)
			{
				temp = boolToChar(image.getPixel(row, col));
				System.out.print(temp);
			}
			System.out.println("|");
		}
		for ( col = 0; col < actualWidth + 2; col++ )
			System.out.print("-");
		System.out.println();
	}
	private boolean[] charToBinary(char myChar)
        {
	   boolean[] column = new boolean[8];
           int charVal = myChar;
	      
	   //use bitshift ond then mod 2 to determine sequence of 1s and 0s
	   for (int k = column.length - 1; k >= 0; k--)
	   {
	   if (charVal % 2 == 1)
	      column[k] = true;
	   else 
	      column[k] = false;
	      charVal = charVal >> 1;
	   }
	   return column;
	}
	
	//static helper for next method
	public char boolToChar(boolean bool)
        {
	   if (bool == true)
	      return BLACK_CHAR;
	   else
	      return WHITE_CHAR;
        }  
	//Creates image from text -Norma
	public boolean generateImageFromText()
	{
		int row, col, digit;
		boolean[] columnVals;

		//check that the text is a legal length
		if (text == "" || text.length() > BarcodeImage.MAX_WIDTH - 2)
			return false;
		clearImage();
		scan(image);

		//this double loop takes help from a method that converts a char into 
		//an array of bools representing 1s and 0s
		for (col = 1; col < text.length() + 1; col++)
		{
			columnVals = charToBinary(text.charAt(col - 1));
			for (row = BarcodeImage.MAX_HEIGHT - 2, digit = columnVals.length - 1; 
					row >= BarcodeImage.MAX_HEIGHT - 9; row--, digit--)
			{
				image.setPixel(row, col, columnVals[digit]);
			}
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

		for (col = 1; col < actualWidth - 1; col++)
		{
			temp = 0;
			for (row = BarcodeImage.MAX_HEIGHT - 2, digit = 0; 
					row > BarcodeImage.MAX_HEIGHT - actualWidth; row--, digit++)
			{

				if (image.getPixel(row, col) == true)
					temp += (int)Math.pow(2, digit);
			}
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
				image.setPixel(row, col, false);
		}
	}
	
	public void displayTextToConsole()
	{
		System.out.print(text);
	}
}

