import java.lang.*;

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
		int maxRow = 0, maxCol = 0;

		//if exceeds maxs then return false

		for (int i = 0; i < MAX_HEIGHT; i++ )
		{
			for (int j = 0; j < MAX_WIDTH; j++ )
			{
				if( image_data[i][j] == true )
				{
					if ( i > maxRow )
					{
						maxRow = i;
					}
					if( j > maxCol )
					{
						maxCol = j;
					}
				}
			}
		}

		for ( int k = 0; k <= maxRow; k++ )
		{
			for ( int l = 0; l <= maxCol; l++ )
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
}

/*
class DataMatrix implements BarcodeIO
{

}
 */
