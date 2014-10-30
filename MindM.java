

// a skeleton Mind_M (that calls other Minds) 

// this one runs in ImageWorld



import org.w2mind.net.*;




public class MindM  implements Mind 
{
	 	
 
// call this Mind:

 public String MIND_NAME   = "ImageMind";
 public String MIND_SERVER = "som://mbio-server.computing.dcu.ie";

// in this World:

 public String WORLD_NAME   = "ImageWorld";
 public String WORLD_SERVER = "som://mbio-server.computing.dcu.ie";



 public RemoteMind rm = new RemoteMind ( MIND_SERVER, MIND_NAME, WORLD_SERVER, WORLD_NAME );


 

 public void newrun()  throws RunError 
 {
	rm.newrun();
 }


 public void endrun()  throws RunError
 {
	rm.endrun();
 }
	 


// N.B. When calling another Mind, always consider the possibility that the called Mind is broken / deleted.
// Other users may change or delete their Minds, in which case this is like a broken link.
// So always ensure your Mind returns an action even if the called Mind does not work.

 
public Action getaction ( State state )
{  	
 Action a;

 try { a = rm.getaction(state); }  
 catch ( Exception e ) 
 {  
	int i = ImageWorld.ACTION_LEFT;
  	String s = String.format ( "%d", i );
   	a = new Action ( s );
 }

 return ( a ); 
}



}




