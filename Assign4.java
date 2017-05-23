/*
 * Team Logistic Solutions:  Danh Huynh, Brenna Eckel, Steven Hunt, Guadalupe Alejo, Norma Sanchez
 * CST338 Software Design
 * Assignment 4
 * May 23, 2017
 * 
 * Description: This assignment combines 2D arrays, interfaces (including Cloneable), and optical scanning and pattern recognition.
 * 
 */

import java.lang.*;
import java.lang.Math;

public class Assign4 {

	public static void main(String[] args) {

		String[] sImageIn = {

				"* * * * * * * * * * * * * * * * * * * * * ",
				"*                                       * ",
				"****** **** ****** ******* ** *** *****   ",
				"*     *    ****************************** ",
				"* **    * *        **  *    * * *   *     ",
				"*   *    *  *****    *   * *   *  **  *** ",
				"*  **     * *** **   **  *    **  ***  *  ",
				"***  * **   **  *   ****    *  *  ** * ** ",
				"*****  ***  *  * *   ** ** **  *   * *    ",
				"***************************************** ", 
		};

		String[] sImageIn_2 = {  

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
				"**************************************    "
		};


		String[] sImageIn_3 = {
				
				"  * * * * * * * * * * * * * * ",
				"  *                         * ",
				"  ***** ***** ** ** *******   ",
				"  * ************************* ",
				"  **  *     * *               ",
				"  * **  *      *     *  *   * ",
				"  **    ****  **  *  ** ***   ",
				"  *   * *   *  * *  *   *   * ", 
				"  *  **  ** *  *  * * ** * *  ",
				"  *************************** ",
				"                              ",
				"                              "
		};

		BarcodeImage bc = new BarcodeImage(sImageIn);
		DataMatrix dm = new DataMatrix(bc);

		System.out.println(""); 

		// First secret message:
		dm.translateImageToText();
		dm.displayTextToConsole();
		dm.displayImageToConsole();

		System.out.println(""); 

		// Second secret message:
		bc = new BarcodeImage(sImageIn_2);
		dm.scan(bc);
		dm.translateImageToText();
		dm.displayTextToConsole();
		dm.displayImageToConsole();

		System.out.println(""); 

		// Create your own message:
		dm.readText("Team Logistic Solutions!");
		dm.generateImageFromText();
		dm.displayTextToConsole();
		dm.displayImageToConsole();

		System.out.println(""); 					
		// Third secret message:
		bc = new BarcodeImage(sImageIn_3);
		dm.scan(bc);
		dm.translateImageToText();
		dm.displayTextToConsole();
		dm.displayImageToConsole();

	} // Close main

} // Close demo class

// PHASE 1: BarcodeIO ================================================================================================

// Defines the I/O and basic methods of any barcode class which might implement it.
// Any class that implements BarcodeIO is expected to store some version of an image and some version of the text associated with that image.
interface BarcodeIO {

	public boolean scan(BarcodeImage bc); // Accepts some image (Represented as a barcodeImage object) and stores a copy of this image. 
	public boolean readText(String text); // Accepts a string text to be eventuall encoded in an image. 
	public boolean generateImageFromText(); 
	public boolean translateImageToText();  
	public void displayTextToConsole(); 
	public void displayImageToConsole(); 
}

// PHASE 2: BarcodeImage ==============================================================================================

// BarcodeImage will describe the 2D dot-matrix pattern, or "image".
// It will contain some methods for storing, modifying and retrieving the data in a 2D image.  
// The interpretation of the data is not part of this class.  Its job is only to manage the optical data.
class BarcodeImage implements Cloneable {

	// The exact internal dimensions of 2D data. 
	public static final int MAX_HEIGHT = 30;
	public static final int MAX_WIDTH = 65;

	// Where image is stored. (false for white elements, true for black elements). 
	private boolean [][] image_data;

	// ---------------------- CONSTRUCTORS ---------------------------

	// Default Constructor: Instantiates 2D array and fills it all with blanks (false). 
	public BarcodeImage() {

		image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];

		for (int i = 0; i < image_data.length; i++) {

			for (int j = 0; j < image_data[i].length; j++)

				image_data [i][j] = false;
		}
	} // Close default constructor

	// Constructor: Takes a 1D array of Strings and converts it to the internal 2D array of booleans. 
	public BarcodeImage(String[] str_data) {   

		image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];

		for (int i = 0; i < image_data.length; i++) {

			for (int j = 0; j < image_data[i].length; j++) 

				image_data[i][j] = false;
		}

		int arrayLength = str_data.length;
		int lineLength = str_data[0].length();
		int xHolder = 0, yHolder = 0;
		boolean found = false;

		while (found == false) {

			for (int line = arrayLength - 1; line >= 0; line--) {

				for(int character = 0; character < lineLength; character++) {

					if(str_data[line].charAt(character) == '*') {

						xHolder = character;
						yHolder = line;
						found = true;
						break;
					}
				}
				if(found == true) break;
			}
		}

		for(int row = yHolder; row >= 0; row--) {

			for(int col = xHolder; col < lineLength; col++) {

				if(str_data[row].charAt(col) == '*') {
					image_data[MAX_HEIGHT - (yHolder - row) - 1][col - xHolder] = true;    
				}
				else {
					image_data[MAX_HEIGHT - (yHolder - row) - 1][col - xHolder] = false;
				}
			}
		}
	} // Closing constructor

	// ---------------------- ACCESSORS AND MUTATORS ---------------------------

	// Accessor:    
	public boolean getPixel(int row, int col) {

		try {
			return image_data[row][col];   
		}

		catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	} // Close getPixel

	// Mutator: 
	public boolean setPixel(int row, int col, boolean value) {

		try {
			image_data[row][col] = value;
		}

		catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}

		return true;
	} // Close setPixel

	// ------------------------------------------------------------------------ 	

	// Overrides the method of that name in Cloneable interface
	public Object clone()throws CloneNotSupportedException {

		return (BarcodeImage)super.clone();  
	}

	// --------------------- OPTIONAL METHODS ------------------------------------------ 	

	// Optional: Useful for debugging this class, but not very useful for the assignment at large.
	public boolean displayToConsole() {

		int minRow = 0, maxCol = 0;

		//if exceeds maxs then return false

		for (int i = MAX_HEIGHT - 1; i >= 0; i-- ) {

			if(image_data[i][0] == true) {
				minRow = i;
			}
		}
		for (int j = 0; j < MAX_WIDTH; j++ ) {

			if(image_data[0][j] == false) {
				maxCol = j;
			}
		}

		if (minRow < MAX_HEIGHT && maxCol > 0 ) {

			for ( int k = minRow; k < MAX_HEIGHT; k++ ) {

				for ( int l = 0; l < maxCol; l++ ) {

					if( image_data[k][l] == true ) {
						System.out.print('*');
					}
					else {
						System.out.print(' ');
					}
				}
				System.out.print('\n');
			}
			return true;
		}
		else {
			return false;
		}
	} // Close displayToConsole()  

	private boolean checkSize(String[] data) {

		int strLen;
		strLen = data.length;

		if( strLen > MAX_WIDTH ) {
			return false;
		}
		else {
			return true;
		}
	} // Close checkSize()

} // Close BarcodeImage class

// PHASE 3: DataMatrix ==============================================================================================

class DataMatrix implements BarcodeIO {

	public static final char BLACK_CHAR = '*';
	public static final char WHITE_CHAR = ' '; 
	private BarcodeImage image; // a single internal copy of any image scanned-in OR passed-into the constructor OR created by BarcodeIO's generateImageFromText.
	private String text; // a single internal copy of any text read-in OR passed-into the constructor OR created by BarcodeIO's translateImageToText().
	private int actualWidth, actualHeight, signalWidth, signalHeight; 

	// ---------------------- CONSTRUCTORS ---------------------------

	//Default Constructor: Constructs an empty, but non-null, image and text value. 
	public DataMatrix() {

		image = new BarcodeImage();
		text = "";
		actualWidth = 0;
		actualHeight = 0;
	}

	// Sets the image, but leaves the text at its default value.  
	public DataMatrix(BarcodeImage image) {

		this.image = image;
		this.actualHeight = BarcodeImage.MAX_HEIGHT;
		this.actualWidth = BarcodeImage.MAX_WIDTH;

		if(!scan(image)) {

			image = new BarcodeImage();
			actualWidth = BarcodeImage.MAX_WIDTH;
			actualHeight = BarcodeImage.MAX_HEIGHT;
			text = image.toString();
		}

		else {
			actualWidth = 0;
			actualHeight = 0;
		}

		text = "";

	} // Close constructor

	// Sets the text, but leaves the image at its default value.
	public DataMatrix(String text) {

		image = new BarcodeImage();

		if(!readText(text))
			this.text = "";

		actualWidth = 0;
		actualHeight = 0; 

	} // Close constructor 

	// -------------------------- MUTATORS --------------------------------------

	// For Text:
	public boolean readText(String text) {

		if(text == null) return false;

		this.text = text;

		return true;
	}

	// For Image: 
	public boolean scan(BarcodeImage image) {

		try {

			this.image = (BarcodeImage)image.clone();
			this.cleanImage();

			// Set actual width and height: 
			signalWidth = computeSignalWidth();
			signalHeight = computeSignalHeight();
			actualWidth = signalWidth - 2;
			actualHeight = signalHeight - 2;    

			return true; 
		}

		catch (CloneNotSupportedException e) {

			return false;
		}
	}

	// -------------------------- ACCESSORS --------------------------------------

	// For actualWidth and actualHeight:
	public int getWidth() { return actualWidth; }

	public int getHeight() { return actualHeight; }

	// --------------------------------------------------------------------------- 	   

	// Assuming that the image is correctly situated in the lower-left corner of the larger boolean array, these methods use the "spine" of the array (left and bottom BLACK) to determine the actual size.
	private int computeSignalHeight() {

		int min_Height = 0;

		for (int row = 0; row < BarcodeImage.MAX_HEIGHT; row++) {

			if (image.getPixel(row, 0) == true)
				min_Height++;
		}

		return min_Height;
	}

	private int computeSignalWidth() {

		//use the bottom border to determine width
		int min_Width = 0;

		for (int col = 0; col < BarcodeImage.MAX_WIDTH; col++) {

			if (image.getPixel(BarcodeImage.MAX_HEIGHT - 1, col) == true)
				min_Width++;
		}

		return min_Width;
	}

	// --------------------- Implementation of BarcodeIO Methods: ------------------- 	   

	// Looks at the internal text stored and produces a companion barCodeImage internally.  
	public boolean generateImageFromText() {

		int row, col, digit;

		boolean [] columnVals;

		if (text == "" || text.length() > BarcodeImage.MAX_WIDTH - 2)
			return false;

		// Utility   
		clearImage();
		makeFrame();
		scan(image);

		for (col = 1; col < text.length() + 1; col++) {
			columnVals = charToBinary(text.charAt(col - 1));

			for (row = BarcodeImage.MAX_HEIGHT - 2, digit = columnVals.length - 1; row >= BarcodeImage.MAX_HEIGHT - 9; row--, digit--) {
				image.setPixel(row, col, columnVals[digit]);
			}
		}

		return true;
	}

	// Looks at the internal text stored and produces a companion text string internally.
	public boolean translateImageToText() {

		int row, col, digit;
		char temp;

		readText("");

		for (col = 1; col < signalWidth - 1; col++) {
			temp = 0;

			for (row = BarcodeImage.MAX_HEIGHT - 2, digit = 0; row > BarcodeImage.MAX_HEIGHT - signalHeight; row--, digit++) {

				if (image.getPixel(row, col) == true)
					temp += (int)Math.pow(2, digit);
			}
			text += temp;
		}
		return true;
	}

	// Prints out the text string to the console.   
	public void displayTextToConsole() {

		System.out.println(text);
	}

	// Prints out the image to the console. 
	public void displayImageToConsole() {

		int row, col;
		char temp;

		System.out.println();

		for ( col = 0; col < signalWidth + 2; col++ )
			System.out.print("-");

		System.out.println();

		for (row = BarcodeImage.MAX_HEIGHT - signalHeight; row < BarcodeImage.MAX_HEIGHT; row++) {
			System.out.print("|");

			for (col = 0; col < signalWidth; col++) {
				temp = boolToChar(image.getPixel(row, col));
				System.out.print(temp);
			}
			System.out.println("|");
		}

		for ( col = 0; col < signalWidth + 2; col++ )
			System.out.print("-");

		System.out.println();
	}

	// --------------------------------------------------------------------------- 	   

	// Will move the signal to the lower-left of the larger 2D array  
	private void cleanImage() {

		for(int row = BarcodeImage.MAX_HEIGHT - actualHeight; row >= 0; row--) {

			for(int col = BarcodeImage.MAX_WIDTH - actualWidth; col < BarcodeImage.MAX_WIDTH; col++) {
				image.setPixel(BarcodeImage.MAX_HEIGHT - (actualHeight - row) - 1, col - actualWidth, image.getPixel(row, col));

			}
		}
	}

	// --------------------------------------------------------------------------- 	   

	private void makeFrame()
	{
		int row, col;

		//make the top border
		for (col = 0; col < text.length() + 2; col++)
		{
			image.setPixel(BarcodeImage.MAX_HEIGHT - 1, col, true);
			if (col % 2 == 0)
				image.setPixel(BarcodeImage.MAX_HEIGHT - 10, col, true);
		}

		//make the left border
		for (row = BarcodeImage.MAX_HEIGHT - 1; 
				row >= BarcodeImage.MAX_HEIGHT - 10; row--)
		{
			image.setPixel(row, 0, true);
			if (row < BarcodeImage.MAX_HEIGHT)
				image.setPixel(row, text.length() + 1, 
						!image.getPixel(row + 1, text.length() + 1));      
		}
	}

	// --------------------------------------------------------------------------- 	   

	// 
	private boolean [] charToBinary(char myChar) {

		boolean[] column = new boolean[8];
		int charVal = myChar;

		for (int k = column.length - 1; k >= 0; k--) {

			if (charVal % 2 == 1)
				column[k] = true;
			else 
				column[k] = false;

			charVal = charVal >> 1;
		}

		return column;
	}

	// ---------------------------------------------------------------------------

	// Sets the image to white = false. 
	private void clearImage() {

		int row, col;

		for (row = 0; row < BarcodeImage.MAX_HEIGHT; row++) {

			for (col = 0; col < BarcodeImage.MAX_WIDTH; col++)
				image.setPixel(row, col, false);
		}
	}

	// ---------------------------------------------------------------------------

	// Static helper.
	public static char boolToChar(boolean bool) {

		if (bool == true)
			return BLACK_CHAR;
		else
			return WHITE_CHAR;
	}

} // Close DataMatrix Class


/** ============================== OUTPUT =====================================


CSUMB CSIT online program is top notch.

-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * *|
|*                                       *|
|****** **** ****** ******* ** *** *****  |
|*     *    ******************************|
|* **    * *        **  *    * * *   *    |
|*   *    *  *****    *   * *   *  **  ***|
|*  **     * *** **   **  *    **  ***  * |
|***  * **   **  *   ****    *  *  ** * **|
|*****  ***  *  * *   ** ** **  *   * *   |
|*****************************************|
-------------------------------------------

You did it!  Great work.  Celebrate.

----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
----------------------------------------
Logistic Solutions

----------------------
|* * * * * * * * * * |
|*                  *|
|********* ********* |
|* ******** *********|
|*    **   *  **   * |
|*** *  *   **  *** *|
|****  *    **** **  |
|* ** *  * **    ****|
|* **** ** ** * ** * |
|********************|
----------------------

 */
