import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

import org.w2mind.net.*;

/*

0	1	2
3	4	5
6	7	8


*/


public class toeWorld extends AbstractWorld
{
	public static final int GRID_X = 3;
	public static final int GRID_Y = 3;
	public static final int SQUARE = GRID_X * GRID_Y;
	
	int[] places = {0,0,0,0,0,0,0,0,0};//Init places
	

	 

	int o_wins;
	int x_wins;
	int draw;
	List<String> scorecols;
	int timestep;
	int MAX_STEPS = 100;
	int numPlaces = 9;
	String IMAGE_DIR = "images";
	String IMG_X = IMAGE_DIR+"/x.jpg";
	String IMG_O = IMAGE_DIR+"/o.jpg";
	String IMG_B = IMAGE_DIR+"/blank.jpg";
	private transient ArrayList<BufferedImage> buf;
	private transient InputStream xStream, oStream, bStream;
	private transient BufferedImage xImg, oImg, bImg;

	int imgWidth, imgHeight;
	private boolean checkWin(int type)
	{
		if(places[0] == type && places[1] == type && places[2] == type)
			return true;
		else if(places[0] == type && places[3] == type && places[6] == type)
			return true;
		else if(places[0] == type && places[4] == type && places[8] == type)
			return true;
		else if(places[1] == type && places[4] == type && places[7] == type)
			return true;
		else if(places[2] == type && places[5] == type && places[8] == type)
			return true;
		else if(places[3] == type && places[4] == type && places[5] == type)
			return true;
		else if(places[6] == type && places[7] == type && places[8] == type)
			return true;
		else
			return false;
	}
	private int randPos()
	{
		Random t = new Random();
		int ranX = t.nextInt(SQUARE);
		int i = 0;
		while(places[ranX] != 0)
		{
			ranX = t.nextInt(SQUARE);
			i++;
			if(i > SQUARE)
				break;
		}
		return ranX;
	}

	/*private int putX()
	{
		//Place an X
	}*/

	private boolean runFinished()
	{
			return (timestep >= MAX_STEPS);
	}

	private void initImages()
	{
		if( imagesDesired)
		{
			buf = new ArrayList<BufferedImage>(); //Buffer cleared
			if(xStream == null)
			{
				try
				{
					ImageIO.setUseCache(false);
					xStream = getClass().getResourceAsStream( IMG_X );
					oStream = getClass().getResourceAsStream( IMG_O);
					bStream = getClass().getResourceAsStream( IMG_B);

					bImg = javax.imageio.ImageIO.read(bStream);
					xImg = javax.imageio.ImageIO.read(xStream);
					oImg = javax.imageio.ImageIO.read(oStream);

					imgWidth = xImg.getWidth();
					imgHeight = xImg.getHeight();
				}
				catch(IOException e)
				{}
			}
		}
	}

	private void addImage()
	{
		if (imagesDesired)
		{
			BufferedImage img = new BufferedImage( ( imgWidth * GRID_X), (imgHeight * GRID_Y), BufferedImage.TYPE_INT_RGB);
			
			for(int i = 0; i < places.length; i++)
			{
				if(places[i] == 1) //Place an X
				{
					//image, create, draw         (Imag, X Cord,  Y Cord)
					img.createGraphics().drawImage(xImg, (i % GRID_X), (i / GRID_Y), null);
				}
				else if(places[i] == 2)// Place an O
				{
					img.createGraphics().drawImage(oImg, (i % GRID_X)*imgWidth, (i / GRID_Y)*imgHeight, null);
				}
				else if(places[i] == 0)
				{
					img.createGraphics().drawImage(bImg, (i % GRID_X)*imgWidth,(i/GRID_Y)*imgHeight, null);
				}
			}

			buf.add(img);

		}
	}

	public void newrun() throws RunError
	{
		timestep = 0;
		x_wins = 0;
		o_wins = 0;
		draw = 0;

		randPos();
		scorecols = new LinkedList<String>();
		scorecols.add("X Wins");
		scorecols.add("O Wins");
		//Finish this
	}

	public void endrun() throws RunError {}

	public State getstate() throws RunError
	{
		String x = String.format(places[0]+","+places[1]+","+places[2]+","+places[3]+","+places[4]+","+places[5]+","+places[6]+","+places[7]+","+places[8]);

		return new State (x);
	}
		
	public State takeaction(Action action) throws RunError
	{
		initImages();
		addImage();
		String s = action.toString();
		String[] a = s.split(",");
		int tmp = Integer.parseInt(a[0]);
		
		//O's Go
		if(places[tmp] == 0)
		{
			places[tmp] = 2;
			System.out.printf("O moves to:\t %d\n", tmp+1);
		}
		
		addImage();
		
		if(checkWin(2))
		{
			System.out.printf("O Wins!\n");
			o_wins++;
			printboard();
			addImage();
			clearBoard();
			randPos();
		}
		else
		{
			//X's go.
			Random rand = new Random();
			int ran = rand.nextInt(SQUARE-1);

			if(places[ran] == 0)
			{
				rand.nextInt(SQUARE-1);
				places[ran] = 1;
				System.out.printf("X moves to:\t %d\n", tmp+1);
			}

			if(checkWin(1))
			{
				System.out.printf("X Wins!\n");
				x_wins++;
				addImage();
				printboard();
				clearBoard();
				randPos();
			}
		}
		
		numPlaces--;
		if(numPlaces == 0)
		{
			draw++;
			addImage();
			printboard();
			clearBoard();
			randPos();
		}
		timestep++;
		if( runFinished() )
		{
			addImage();
		}
		return getstate();
	}


	public Score getscore() throws RunError
	{
		String s = String.format("%d%d%d", x_wins, o_wins, draw);
		List<Comparable> values = new LinkedList<Comparable>();
		values.add(x_wins);
		values.add(o_wins);
		values.add(draw);
		return new Score(s, runFinished(), scorecols, values);
	}

	public ArrayList<BufferedImage> getimage() throws RunError
	{
		return buf;
	}

	void clearBoard()
	{
		initImages();
		for(int i = 0; i < 9; i++)
		{
			places[i] = 0;
		}
	}

	void printboard()
	{
		for(int i = 0; i < 9; i++)
		{
			System.out.printf(" %d ", places[i]);
			if((i+1) % 3 == 0 && i != 0)
			{
				System.out.print("\n");
			}
		}
		numPlaces = 9;
		System.out.print("\n");
	}

}



