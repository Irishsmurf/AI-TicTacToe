
// "Cops and robbers" world - with images


import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*; 

import org.w2mind.net.*;
 


// See SkeletonWorld.java for background and extra comments.
 
// Note: This code has been stripped down to give you less to read.
// The aim of this code was not a perfect Java program, but to give you as little as possible to read.




public class ImageWorld extends AbstractWorld 
{

	public  static final   	int GRID_SIZE = 8;		 	

		int 	coppos, robpos;		 

		int MAX_STEPS = 20;		 
		int nocaught;			 
		int nocaughtbyme;			 
		List <String> scorecols;	 

	int 		timestep;		


	public  static final   int ACTION_LEFT 		= 0;
	public  static final   int ACTION_RIGHT 		= 1;
	public  static final   int STAY_STILL		= 2;

	public  static final   int NO_ACTIONS = 3;



	 	String SUPPORT_DIR 	= "images";					// support files 

	 	String IMG_COP 		= SUPPORT_DIR + "/cop.jpg";
	 	String IMG_ROB 		= SUPPORT_DIR + "/robber.jpg";
	 	String IMG_CAUGHT 	= SUPPORT_DIR + "/caught.jpg";	


	// transient - don't serialise these:

	private transient      ArrayList <BufferedImage> buf;

	private transient InputStream 	copStream, robStream, caughtStream;		
    	private transient BufferedImage 	copImg, robImg, caughtImg; 

	int 			imgwidth, imgheight;




	
private int randomPos()				 
{
 Random r = new Random(); 
 return ( r.nextInt( GRID_SIZE ) );		 
}


private void initPos()			 
{
	coppos = randomPos();  								  
	do { robpos = randomPos(); } while ( coppos == robpos );		 
}


private int randomAction()				 
{
 Random r = new Random(); 
 return ( r.nextInt( NO_ACTIONS ) );		 
}

	
private int move ( int startpos, int direction )
{
 if ( direction == ACTION_LEFT  ) return ( ( startpos - 1 + GRID_SIZE ) % GRID_SIZE );
 if ( direction == ACTION_RIGHT ) return ( ( startpos + 1 + GRID_SIZE ) % GRID_SIZE );
 return startpos;
}


private boolean runFinished() { return ( timestep >= MAX_STEPS ); }		 

  

private void initImages()					// sets up new buffer to hold images  
{
 if ( imagesDesired ) 
 {
  buf = new ArrayList <BufferedImage> ();			// buffer is cleared for each timestep, multiple images per timestep

  if ( copStream  == null ) 					// block is only executed once (only read from disk once)
  {
   try
   {
	ImageIO.setUseCache(false);		// use memory, not disk, for temporary images

	copStream    = getClass().getResourceAsStream ( IMG_COP );		 // read from disk
	robStream    = getClass().getResourceAsStream ( IMG_ROB );
	caughtStream = getClass().getResourceAsStream ( IMG_CAUGHT );
		
    	copImg    = javax.imageio.ImageIO.read( copStream );
    	robImg    = javax.imageio.ImageIO.read( robStream );
	caughtImg = javax.imageio.ImageIO.read( caughtStream );

	imgwidth  = copImg.getWidth();		// dimensions of jpg covering one square of the grid
	imgheight = copImg.getHeight();
   }
   catch ( IOException e ) {}
  }
 }
}


 
private void addImage()			// adds image to buffer
{
 if ( imagesDesired ) 
 {
	BufferedImage img = new BufferedImage ( ( imgwidth * GRID_SIZE ), imgheight, BufferedImage.TYPE_INT_RGB );
		  
	if (coppos == robpos)
	 img.createGraphics().drawImage ( caughtImg, (imgwidth * coppos), 0, null );
	else
	{
	 img.createGraphics().drawImage ( copImg, (imgwidth * coppos), 0, null );
	 img.createGraphics().drawImage ( robImg, (imgwidth * robpos), 0, null );
	}

	buf.add(img);	
 }
}








//====== World must respond to these methods: ==========================================================
//  newrun(), endrun()
//  getstate(), takeaction()
//  getscore(), getimage()
//======================================================================================================



public void newrun() throws RunError
{
		timestep = 0;
		nocaught = 0;
		nocaughtbyme = 0;
				
		initPos();

	scorecols = new LinkedList <String> ();		
	scorecols.add("Caught");
	scorecols.add("Caught_by_me");
}
	 


public void endrun() throws RunError
{
}




public State getstate() throws RunError
{

  String x = String.format ( "%d,%d", coppos, robpos );

  return new State ( x );
}
	



public State takeaction ( Action action ) throws RunError 
// Add any number of images to a list of images for this step.
// The first image on the list for this step should be the image before we take the action.
{   
	initImages();			// If run with images off, imagesDesired = false and this does nothing.
 
 	addImage();				// image before my move 
						// If run with images off, imagesDesired = false and this does nothing.  

  	String 	s = action.toString();		 
  	String[] 	a = s.split(",");			   
  
  	int i = Integer.parseInt ( a[0] );		 

	coppos = move ( coppos, i );

 	addImage();				// intermediate image, before opponent moves   

	if (coppos == robpos)  
 	{
	 nocaught++;			// have already shown the "capture" image
	 nocaughtbyme++;	 
	 initPos();				// loop round, new image will be shown in next step
	}					  

	else		 
	{
	 robpos = move ( robpos, randomAction() );

	 // addImage(); 			// new image will be shown in next step		

	 if (coppos == robpos) 
 	 {
	  addImage();			// show the "capture" image    
	  nocaught++;
	  initPos();			// loop round, new image will be shown in next step
	 }
      }

      timestep++;

	if ( runFinished() )		// there will be no loop round
	 addImage();			

// The last timestep of the run shows the final state, and no action can be taken in this state.
// Whatever is the last image built on the run will be treated as the image for this final state.

      return getstate();
}
	



 
public Score getscore() throws RunError
{
 String s = String.format ( "%d,%d", nocaught, nocaughtbyme );
 
	List <Comparable> values = new LinkedList <Comparable> ();
	values.add( nocaught 		);
	values.add( nocaughtbyme	);

  return new Score ( s, runFinished(), scorecols, values );
}

		 
 

public ArrayList <BufferedImage> getimage() throws RunError
// Return image(s) of World.
// Image may show more information than State (what the Mind sees).
// This method returns a list of images for this step - we allow multiple images per step.
// e.g. You move, get one image, the opponent moves, next image, your turn again (this is 2 images per step).
// This list of images should normally be built in takeaction method.
// The first image on the list for this step should be the image before we take the action on this step.

{
   return buf;
}



}





