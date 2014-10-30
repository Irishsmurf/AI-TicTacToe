

// "Cops and robbers" world - no images


import java.util.*;

import org.w2mind.net.*;
 


// The demo world is a "cops and robbers" world.
// A cop chases a robber across a 1-dimensional grid.
// If the robber is caught, it resets to start position.
// Score = Number of times robber gets caught in a run.


// Note: This code has been stripped down to give you less to read.
// The aim of this code was not a perfect Java program, but to give you as little as possible to read.



public class SkeletonWorld extends AbstractWorld 
{

	public  static final   	int GRID_SIZE = 8;		// 1-dimensional grid	

		int 	coppos, robpos;		// positions of cop and robber, squares numbered from 0 to GRID_SIZE - 1

		int MAX_STEPS = 20;		// number of steps in a run
		int nocaught;			// primary score field = number of times robber has been caught in this run
		int nocaughtbyme;			// secondary score field = number of times robber caught due to my action
		List <String> scorecols;	// Headers for the score fields

	int 		timestep;		


	// actions (public static since used by Mind):

	public  static final   int ACTION_LEFT 		= 0;
	public  static final   int ACTION_RIGHT 		= 1;
	public  static final   int STAY_STILL		= 2;

	public  static final   int NO_ACTIONS = 3;





	
private int randomPos()				// random position
{
 Random r = new Random(); 
 return ( r.nextInt( GRID_SIZE ) );		// integer 0 to GRID_SIZE - 1
}


private void initPos()			// set both cop and robber to random initial position
{
	coppos = randomPos();  								// random position 
	do { robpos = randomPos(); } while ( coppos == robpos );		// repeat until different position
}


private int randomAction()				 
{
 Random r = new Random(); 
 return ( r.nextInt( NO_ACTIONS ) );		// integer 0 to NO_ACTIONS - 1
}


	
private int move ( int startpos, int direction )		// move cop or robber, modulo GRID_SIZE to wraparound
{
 if ( direction == ACTION_LEFT  ) return ( ( startpos - 1 + GRID_SIZE ) % GRID_SIZE );		 
 if ( direction == ACTION_RIGHT ) return ( ( startpos + 1 + GRID_SIZE ) % GRID_SIZE );
 // else
 return startpos;
}

  





//====== World must respond to these methods: ==========================================================
//  newrun(), endrun()
//  getstate(), takeaction()
//  getscore() 
//======================================================================================================



public void newrun() throws RunError
{
		timestep = 0;
		nocaught = 0;
		nocaughtbyme = 0;
				
		initPos();

	scorecols = new LinkedList <String> ();		// Headers for the score fields
	scorecols.add("Caught");
	scorecols.add("Caught_by_me");
}
	 


public void endrun() throws RunError
{
}





//====== Definition of state: ===========================================================================
// State in general:
//  World.getstate() constructs a string to describe World State. 
//  Pass string to State() constructor.
//  The format of the string is up to the World author.
//  Explain it on your World description page so people can write Minds.
//  Typically, state might be a string of fields separated by commas:
//   state x = "x1,x2,..,xn"
//  State may be partial state - this is what the Mind can see, maybe not the whole World.
//
// State in cop world:
//  Here, state will be the string:
//   state x = "c,r" (position of cop and robber)
//======================================================================================================



public State getstate() throws RunError
{

  String x = String.format ( "%d,%d", coppos, robpos );

  return new State ( x );
}
	



//====== Definition of action: ============================================================================
// Action in general:
//  Mind.getaction() constructs a string to describe the action. 
//  Passes string to Action() constructor.
//  The format of the string is up to the World author.
//  Explain it on your World description page so people can write Minds.
//  Typically, action might be a string of fields separated by commas:
//   action a = "a1,a2,..,an"
//
// Action in cop world:
//  Here, action will be the string:
//   action a = "i" (an integer describing how to move)
//=========================================================================================================


//====== Extra information in action: =====================================================================
// Each World should TOLERATE extra information in the action fields.
//  This extra information can be read by other Minds, but is ignored by World.  
//  This will allow Minds call other Minds and receive additional information (e.g. W-values)
//  to help them decide what action to send to the World.
//
// Here, allow Minds send other information (which the World will ignore):
//  action a = "i,w1,w2,...,wn"
//=========================================================================================================
  

public State takeaction ( Action action ) throws RunError 
{ 
  	String 	s = action.toString();		// parse the action
  	String[] 	a = s.split(",");			// parsed into a[0], a[1], ...  
  
  	int i = Integer.parseInt ( a[0] );		// ignore any other fields

	coppos = move ( coppos, i );			// take the action

	if (coppos == robpos)  
 	{
	 nocaught++;
	 nocaughtbyme++;		// caught due to our action, not robber's action
	 initPos();		 
	}

	else			// move the robber  
	{
	 robpos = move ( robpos, randomAction() );

	 if (coppos == robpos) 
 	 {
	  nocaught++;		// caught due to robber's action
	  initPos();
	 }
      }

      timestep++;
      return getstate();
}
	



//====== Definition of score: ==============================================================================================
// Score in general:
//  World.getscore() returns the score achieved by the Mind in this World. 
//  The score should consist of separated fields that the scoreboard can sort by.
//  Explain the score fields on your World description page.
//  The score as a string would just be the fields separated by commas:
//   score s = "s1,s2,..,sn"
//
// Score in cop world:
//  Here, score will be:
//   score s = "s1,s2"
//  s1 = number of times robber was caught (primary score, larger is better)
//  s2 = number of times robber was caught due to our action (secondary score, larger is better)
//==========================================================================================================================


 
public Score getscore() throws RunError
{

 String s = String.format ( "%d,%d", nocaught, nocaughtbyme );

// Setting finished = true will end the run.
// N.B. This is the only way the World has to tell the underlying w2m system to stop the run.

 boolean finished = ( timestep >= MAX_STEPS );
 
	List <Comparable> values = new LinkedList <Comparable> ();
	values.add( nocaught 		);
	values.add( nocaughtbyme	);

  return new Score ( s, finished, scorecols, values );
}

	

}




