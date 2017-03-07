
public class Player
{

	private String id; 
	private int number; 
	private int onecard; 
	private int turnflag; 
	public String cards[]; 
	public int bankruptcy; 
	public int sync;

	public int check;




	public Player( String id)
	{
		sync = 0;
		bankruptcy =0;
		this.id = id;
		number = 0;
		onecard = 0;
		turnflag = 0;
		check = 0;
		cards = new String [54];
		for(int i=0; i<54; i++)
		{
			cards[0] ="null";
		}
		
	}

	public void setCheck(int n)
	{
		check = n;
	}


	public int getCheck()
	{
		return check;
	}

	public String getId()
	{
		return id;
	}

	public void setNumber(int n)
	{
		number = n;
	}

	public int getNumber()
	{
		return number;
	}

	public void setOnecard(int n)
	{
		onecard = n;
	}

	public int getOnecard()
	{
		return onecard;
	}

	public void setTurnflag(int n)
	{
		turnflag = n;
	}
	public int getTurnflag()
	{
		return turnflag;
	}

	
}