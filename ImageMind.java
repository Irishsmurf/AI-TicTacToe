
// a Mind for ImageWorld


import java.util.*;

import org.w2mind.net.*;


// See SkeletonMind.java for background and extra comments.




public class ImageMind  implements Mind 
{
	 	 

//====== Mind must respond to these methods: ==========================================================
//  newrun(), endrun()
//  getaction()
======================================================================================================



 public void newrun()  throws RunError 
 {
 }


 public void endrun()  throws RunError
 {
 }
	 

 
public Action getaction ( State state )
{ 
  String 	s = state.toString();		 
  String[] 	x = s.split(",");			   
  
  int coppos = Integer.parseInt ( x[0] );
  int robpos = Integer.parseInt ( x[1] );

  int i;

  if ( robpos > coppos ) i = ImageWorld.ACTION_RIGHT;		
                    else i = ImageWorld.ACTION_LEFT;

  String a = String.format ( "%d", i );

  return new Action ( a );		 
}



}




