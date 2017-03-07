import java.util.Random;

public class Card
{
	String CardSet [];
	int CardStatus [];
	int Storage[]; 
	int Check[]; 
	Random random;
	
	int Offset; 
	
	int Receive; 



	public Card() 
	{
		
		//Spade -> Clover ->  heart -> diamond
		//s = Spade 
		//c = Clover
		//h = Heart
		//d = Diamond
		//tj = Black Jocker
		//uj  = Color Jocker
		CardSet = new String[] {"s1","s2","s3","s4","s5","s6","s7", "s8", "s9", "sa", "sb", "sc", "sd", 
									 "c1","c2","c3","c4","c5","c6","c7","c8","c9","ca","cb","cc","cd",
									 "h1","h2","h3","h4","h5","h6","h7","h8","h9","ha","hb","hc","hd",
									 "d1","d2","d3","d4","d5","d6","d7","d8","d9","da","db","dc","dd","tj","uj" }; 

		Check= new int[] { 0,0,0,0,0,0,0,0,0,0,
								  0,0,0,0,0,0,0,0,0,0,
			                      0,0,0,0,0,0,0,0,0,0,
			                      0,0,0,0,0,0,0,0,0,0,
			                      0,0,0,0,0,0,0,0,0,0,
								  0,0,0,0  }; 
		
		CardStatus = new int[54];
		Storage = new int[54];

		Offset = 0;
		Receive = 0;
		
		random = new Random();
		
	}

	public void Card_Mix() 
	{
		Offset =0; 
		Receive = 0; 

		for(int i=0; i< 54; i++) 
		{
			Check[i] = 0; 
			CardStatus[i] = -1;
			Storage[i]=-1;
			
		}

		int num;
		for (int i=0; i<54; i++) 
		{
			while(true)
			{
				num= random.nextInt(54) ;
				if(Check[num] == 0) 
				{
					Check[num] = 1; 
					break;
				}
			}
				
			CardStatus[i]=num;
		}
	}

	public void Card_Demo() 
	{
		Offset = 0;
		Receive = 0;
		
		for(int i=0; i<54; i++)
		{
			Check[i] = 0;
			CardStatus[i] = -1;
			Storage[i] = -1;
		}

		int num;
		
		Check[32] =1;
		Check[52] =1;
		Check[0] =1;
		Check[1] = 1;
		Check[6] = 1;
		Check[10] = 1;
		Check[12] = 1;
		Check[53] = 1;
		Check[13] = 1;
		Check[14] = 1;
		Check[19] = 1;
		Check[23] = 1;
		Check[25] = 1;

		CardStatus[0] = 32; 

		CardStatus[1] = 52; 
		CardStatus[2] = 0; 
		CardStatus[3] = 1; 
		CardStatus[4] = 6; 
		CardStatus[5] = 10; 
		CardStatus[6] = 12; 
		
		CardStatus[7] = 53; 
		CardStatus[8] = 13; 
		CardStatus[9] = 14; 
		CardStatus[10] = 19; 
		CardStatus[11] =23; 
		CardStatus[12] =25; 

		for (int i=13; i<41; i++) 
		{
			while(true)
			{
				num= random.nextInt(54) ;
				if(Check[num] == 0) 
				{
					Check[num] = 1; 
					break;
				}
			}
				
			CardStatus[i]=num;
		}
	}

		
	public String Select_Card() 
	{
		int num;

		String card = CardSet[ (CardStatus[Offset++]) ];

		
		if(Offset ==54) 
		{
			
			for (int i=0; i<54; i++) 
			{
				Check[i] = 1;
			}
			
			for (int i=0; i<Receive; i++) 
			{
				Check[ Storage[i] ] = 0;
			}

			

			Offset = Offset- Receive ; 
			

			for (int i=0; i<Receive; i++)
			{
				while(true)
				{
					num=random.nextInt(54);
					if(Check[num] == 0 )
					{
						Check[num] = 1;
						break;
					}
				}
				CardStatus[ i+ Offset] =num;
			}
			Receive = 0; 

			
		}
		return card;
		
	}

	public int Input_Card(String s) 
	{
		int num;
		if ( ( num = Index_Card(s) )== -1) 
		{
			System.out.println("Input_Card() error");
			return -1;
		}
		else 
		{
			Storage[Receive] = num;
			Receive = Receive+1;
			return 0;
		}
		
	}



	public int Index_Card(String s) 
	{
		for(int i=0; i<54; i++)
		{
			if( s.equals(CardSet[i]) ) 
			{
				return i;
			}
		}
		return -1; 
	}




	public void Show()
	{
		for (int i=0; i<54; i++)
		{
			System.out.print( CardSet[CardStatus[i]] + " " );
		}
		System.out.println();
	}
	



}

