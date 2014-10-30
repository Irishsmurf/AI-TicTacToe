// A mind for toeWorld. Really basic. Randomly chooses places

import java.util.*;
import org.w2mind.net.*;

public class toeMind implements Mind
{
	public void newrun() throws RunError
	{
	}

	public void endrun() throws RunError
	{
	}

	public Action getaction( State state )
	{
		String s = state.toString();
		String[] elements = s.split(",");
		int[] board = new int[9];
		for(int i = 0; i < 9; i++)
		{
			board[i] = Integer.parseInt(elements[i]);
		}

		Random t = new Random();

		int go = t.nextInt(9);
		while(board[go] != 0)
		{
			go = t.nextInt(9);
		}

		String action = String.format("%d", go);

		return new Action( action );
	}
}
