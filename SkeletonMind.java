
// a Mind for SkeletonWorld


import java.util.*;

import org.w2mind.net.*;




public class SkeletonMind  implements Mind 
{
	 	 

//====== Mind must respond to these methods: ==========================================================
//  newrun(), endrun()
//  getaction()
//======================================================================================================



 public void newrun()  throws RunError 
 {
 }


 public void endrun()  throws RunError
 {
 }
	 

 
public Action getaction ( State state )
{ 
// parse state:

  String 	s = state.toString();		 
  String[] 	x = s.split(",");			// parsed into x[0], x[1], ...  
  
  int coppos = Integer.parseInt ( x[0] );
  int robpos = Integer.parseInt ( x[1] );


// Generate non-random action.
// This ignores wraparound.
// You could easily make a better Mind that uses wraparound.

  int i;

  if ( robpos > coppos ) i = SkeletonWorld.ACTION_RIGHT;		
                    else i = SkeletonWorld.ACTION_LEFT;

// return Action as simply one field, but potentially provide extra fields  

  String a = String.format ( "%d", i );

  return new Action ( a );		 
}



}



