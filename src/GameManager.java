import java.util.Random;
import java.net.*;
import java.io.*;
import java.util.*;
//카드 공격수 리턴

public class GameManager
{
	//순서를 제어
	//현재 그 사람들이 가지고 있는 카드 수를 알아야한다.

	Card deck; // 카드 클래스를 포함

	byte arr[];

	public int fail_flag;//원카드를 실패해서 카드를 한장먹었을때 다른사람들이 원카드를 외치면 늦었다는메시지를보내줘야한다

	public int type ; //50확인메시지를 받은후에 카드를 줄껏이냐 턴을 옮길것이냐
	public String msg;
	
	DataOutputStream Write;
	Random random;
	static public Player usr1;
	static public Player usr2;
	static public Player usr3;
	static public Player usr4;
	Socket sock; //데스크탑과 연결한 소켓을 말한다

	String player; //현재 플레이어
	
	String shape ; //모양

	String card; // 현재 카드 ( 실제 출력하는 카드 )

	String send_card; //보내는 카드 ( 보내는 카드 )

	

	int reverse ;// 0이면 순서대로, 1이면 역방향으로 
	int attack; //누적 공격 카드 수

	int number ; // 사람수
	
	
	

	public String getShape()
	{
		return shape;
	}
	public String getCard()
	{
		return card;
	}

	public void setSend_card(String card)
	{
		send_card = card;
	}
	public String getSend_card()
	{
		return send_card;
	}

	//턴이동
	
	public int getNumber()
	{
		return number;
	}
	

	public GameManager(Socket sock, Card deck ,Player usr1, Player usr2 ) //2인일때
	{
		fail_flag = 0;	
	
		this.usr1 = usr1;
		this.usr2 = usr2;
		this.sock = sock;
		arr = new byte [100];

		try
		{
			Write = new DataOutputStream(sock.getOutputStream());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		usr1.setNumber(6); //초기 각자 갖고 있는 카드 개수를 지정한다.
		usr2.setNumber(6); //초기 각자 갖고 있는 카드 개수를 지정한다
		this.deck = deck;
		usr3 =  null;
		usr4 =  null;
		attack = 1;
		number = 2;
		random = new Random();
		reverse = 0;

		int ran = random.nextInt(2) + 1;

		if ( ran == 1 ) //1인부터 시작
		{
			player = usr1.getId();
		}
		else
		{
			player = usr2.getId();
		}

	}


	public GameManager(Socket sock, Card deck,  Player usr1, Player usr2, Player usr3 ) //3인일때
	{

		
		fail_flag = 0;	
		this.deck = deck;
		this.usr1 = usr1;
		this.usr2 = usr2;
		this.usr3 = usr3;

		this.sock = sock;
		arr = new byte [100];

		try
		{
			Write = new DataOutputStream(sock.getOutputStream());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		
		usr1.setNumber(5);
		usr2.setNumber(5);
		usr3.setNumber(5);
		usr4 = null;
		attack = 1;
		number = 3;
		random = new Random();
		reverse = 0;

		int ran = random.nextInt(3) +1 ;

		if( ran == 1)
		{
			player =usr1.getId();
		}
		else if (ran == 2)
		{
			player =usr2.getId();
		}
		else
		{
			player =usr3.getId();
		}

		

	}

	public GameManager(Socket sock, Card deck, Player usr1, Player usr2, Player usr3, Player usr4 ) //4인일때
	{
		fail_flag = 0;	
		this.deck = deck;
		this.usr1 = usr1;
		this.usr2 = usr2;
		this.usr3 = usr3;
		this.usr4 = usr4;

		this.sock = sock;
		arr = new byte [100];

		try
		{
			Write = new DataOutputStream(sock.getOutputStream());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


		usr1.setNumber(5);
		usr2.setNumber(5);
		usr3.setNumber(5);
		usr4.setNumber(5);

		attack = 1;
		number = 4;
		random = new Random();
		reverse = 0;

		int ran = random.nextInt(4) + 1;

		if ( ran == 1 )
		{
			player =usr1.getId();
		}
		else if ( ran == 2 )
		{
			player = usr2.getId();
		}
		else if ( ran == 3 )
		{
			player = usr3.getId();
		}
		else
		{
			player = usr4.getId();
		}



	}

	public void Exit_Phone(String msg) //누군가 갑자기 게임을 나갔을 때 이다
	{
		//21usr1
		//누군가 갑자기 나갓을떄는 가지고 있는 카드를 모드 반납해야한다
		//반납하는 메소드
		String copy = msg.substring(2,msg.length()); //헤더를 자른다
		StringTokenizer st = new StringTokenizer(copy, "|"); //구분자로 읽는다
		
		String usr = st.nextToken(); //처음엔 아이디가 온다.

		if(usr.equals(usr1.getId()))
		{
			for(int i=0; i<usr1.getNumber(); i++)
			{
				deck.Input_Card(usr1.cards[i]);
				usr1.cards[i] = "null";
			}
		}
		else if(usr.equals(usr2.getId()))
		{
			for(int i=0; i<usr2.getNumber(); i++)
			{
				deck.Input_Card(usr2.cards[i]);
				usr2.cards[i] = "null";
			}
		}
		else if(usr.equals(usr3.getId()))
		{
			for(int i=0; i<usr3.getNumber(); i++)
			{
				deck.Input_Card(usr3.cards[i]);
				usr3.cards[i] = "null";
			}
		}
		else
		{
			for(int i=0; i<usr4.getNumber(); i++)
			{
				deck.Input_Card(usr4.cards[i]);
				usr4.cards[i] = "null";
			}
		}
		
		if(this.number == 2)
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;
			}
			else if (this.number ==3)
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;
			}
			else
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;
				
				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;

				usr3.setOnecard(0);
				usr3.setTurnflag(0);
				usr3.bankruptcy = 0;

				usr4.setOnecard(0);
				usr4.setTurnflag(0);
				usr4.bankruptcy = 0;
			}

	}

	public void Bankruptcy_Phone(String msg) //자기가 갖고있을수 있는 개수가 넘어서 파산한경우 보내는 메시지이다
	{
		//안드로이드가 30으로 보냄
		//31usr1  --> 데스크탑이 받았을경우



		
		String copy = msg.substring(2,msg.length()); //헤더를 자른다
		StringTokenizer st = new StringTokenizer(copy, "|"); //구분자로 읽는다

		String usr = st.nextToken();

		if(player.equals(usr)) //지금 유저가 자기턴이라면 턴을 옮겨줘야한다 -> 강제종료하는경우
		{
			Set_Player(); //플레이어를 갱신하고

			//서버로 보낼 메시지 ex ) 06CurrentCard | CurrentPlayer | CurrentAttackNum |

			String data = "06";
			data = data + send_card; //현재카드
			data = data + "|";
			data = data + player;
			data = data + "|";
			data = data + attack;
			data = data +"|\0";

			//System.out.println(data);
			
			
			try
			{
				arr = data.getBytes();
				Write.write(arr);
				Write.flush(); //메시지를 보낸다
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if(usr.equals(usr1.getId()))
		{
			for(int i=0; i<usr1.getNumber(); i++)
			{
				deck.Input_Card(usr1.cards[i]);
				usr1.cards[i] = "null";
			}
			usr1.bankruptcy = 1;
		}
		else if(usr.equals(usr2.getId()))
		{
			for(int i=0; i<usr2.getNumber(); i++)
			{
				deck.Input_Card(usr2.cards[i]);
				usr2.cards[i] = "null";
			}
			usr2.bankruptcy= 1;
		}
		else if(usr.equals(usr3.getId()))
		{
			for(int i=0; i<usr3.getNumber(); i++)
			{
				deck.Input_Card(usr3.cards[i]);
				usr3.cards[i] = "null";
			}
			usr3.bankruptcy = 1;
		}
		else
		{
			for(int i=0; i<usr4.getNumber(); i++)
			{
				deck.Input_Card(usr4.cards[i]);
				usr4.cards[i] = "null";
			}
			usr4.bankruptcy = 1;
		}



		if(this.number == 2)
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;
			}
			else if (this.number ==3)
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;
			}
			else
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;
				
				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;

				usr3.setOnecard(0);
				usr3.setTurnflag(0);
				usr3.bankruptcy = 0;

				usr4.setOnecard(0);
				usr4.setTurnflag(0);
				usr4.bankruptcy = 0;
			}

	}




	public int Give_Phone(String msg) 	
	{
		//누군가 카드를 냈을 때이다  // 리턴값이 0이면 게임이 끝난것이고 리턴값이 1이면 게임이 계속 진행중인걸 말한다!
		//13usr1|1|d6|
		//13usr1|2|a|d7|

		System.out.println("call Give_Phone() ");


		//이런식이다
		String copy = msg.substring(2,msg.length()); //헤더를 자른다
		StringTokenizer st = new StringTokenizer(copy, "|"); //구분자로 읽는다

		
		
		
		String usr = st.nextToken(); //처음엔 아이디가 온다

		

		
		//이제 유저를 찾아서 카드수를 하나씩 빼야한다.


		int check = 0; //1이면 카드를 다낸 유저가 있는것이다!

		if(number == 2 )
		{
			if ( usr.equals( usr1.getId()) ) //usr1이라면
			{
				int num = usr1.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr1.setNumber(num);
			}
			else
			{
				int num = usr2.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr2.setNumber(num);
			}
		}
		else if (number == 3 )
		{
			if ( usr.equals( usr1.getId()) ) //usr1이라면
			{
				int num = usr1.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr1.setNumber(num);
			}
			else if ( usr.equals(usr2.getId()))
			{
				int num = usr2.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr2.setNumber(num);
			}
			else
			{
				int num = usr3.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr3.setNumber(num);
			}
		}
		else //4인일때
		{
			if ( usr.equals( usr1.getId()) ) //usr1이라면
			{
				int num = usr1.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr1.setNumber(num);
			}
			else if ( usr.equals(usr2.getId()) )
			{
				int num = usr2.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr2.setNumber(num);
			}
			else if ( usr.equals(usr3.getId()) )
			{
				int num = usr3.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr3.setNumber(num);
			}
			else
			{
				int num = usr4.getNumber();
				num = num -1;
				if(num ==0)
					check = 1;
				usr4.setNumber(num);
			}
		}



		//System.out.println(a);

		String reason= st.nextToken(); //두번째는 case가 온다
		
		if( reason.equals("1")) // 평범하게 7을 사용하지않고 낸경우
		{
			String card = st.nextToken(); // 카드가 온다
			
			if(usr.equals(usr1.getId())) //usr1이면
			{
				for (int i=0 ; i<usr1.getNumber()+1; i++)
				{
					if(card.equals(usr1.cards[i])) //카드가 있으면? 그 카드를 빼고 밀어낸다.
					{
						for(int j=i; j<usr1.getNumber()+1; j++)
						{
							usr1.cards[j] = usr1.cards[j+1];
						}
						break;
					}
				}
			}
			else if (usr.equals(usr2.getId()))
			{
				for (int i=0 ; i<usr2.getNumber()+1; i++)
				{
					if(card.equals(usr2.cards[i])) //카드가 있으면? 그 카드를 빼고 밀어낸다.
					{
						for(int j=i; j<usr2.getNumber()+1; j++)
						{
							usr2.cards[j] = usr2.cards[j+1];
						}
						break;
					}
				}
			}
			else if (usr.equals(usr3.getId()))
			{
				for (int i=0 ; i<usr3.getNumber()+1; i++)
				{
					if(card.equals(usr3.cards[i])) //카드가 있으면? 그 카드를 빼고 밀어낸다.
					{
						for(int j=i; j<usr3.getNumber()+1; j++)
						{
							usr3.cards[j] = usr3.cards[j+1];
						}
						break;
					}
				}
			}
			else
			{
				for (int i=0 ; i<usr4.getNumber()+1; i++)
				{
					if(card.equals(usr4.cards[i])) //카드가 있으면? 그 카드를 빼고 밀어낸다.
					{
						for(int j=i; j<usr4.getNumber()+1; j++)
						{
							usr4.cards[j] = usr4.cards[j+1];
						}
						break;
					}
				}
			}

			deck.Input_Card(this.card); //카드를 낸다
			String s= card.substring(0,1); //문양을 확인
			shape = s; //문양을 바꾼다
			
			Set_Player(card); //플레이어순서를 세팅한다
			Set_card(card); //카드를 세팅한다
			send_card = card;
		}
		else // 7을 내서 문양을 바꾼 경우
		{
			String s = st.nextToken(); //문양이온다
			shape = s; //문양을 바꾼다
			String card = st.nextToken(); //카드가 온다

			if(usr.equals(usr1.getId())) //usr1이면
			{
				for (int i=0 ; i<usr1.getNumber()+1; i++)
				{
					if(card.equals(usr1.cards[i])) //카드가 있으면? 그 카드를 빼고 밀어낸다.
					{
						for(int j=i; j<usr1.getNumber()+1; j++)
						{
							usr1.cards[j] = usr1.cards[j+1];
						}
						break;  
					}
				}
			}
			else if (usr.equals(usr2.getId()))
			{
				for (int i=0 ; i<usr2.getNumber()+1; i++)
				{
					if(card.equals(usr2.cards[i])) //카드가 있으면? 그 카드를 빼고 밀어낸다.
					{
						for(int j=i; j<usr2.getNumber()+1; j++)
						{
							usr2.cards[j] = usr2.cards[j+1];
						}
						break;
					}
				}
			}
			else if (usr.equals(usr3.getId()))
			{
				for (int i=0 ; i<usr3.getNumber()+1; i++)
				{
					if(card.equals(usr3.cards[i])) //카드가 있으면? 그 카드를 빼고 밀어낸다.
					{
						for(int j=i; j<usr3.getNumber()+1; j++)
						{
							usr3.cards[j] = usr3.cards[j+1];
						}
						break;
					}
				}
			}
			else
			{
				for (int i=0 ; i<usr4.getNumber()+1; i++)
				{
					if(card.equals(usr4.cards[i])) //카드가 있으면? 그 카드를 빼고 밀어낸다.
					{
						for(int j=i; j<usr4.getNumber()+1; j++)
						{
							usr4.cards[j] = usr4.cards[j+1];
						}
						break;
					}
				}
			}
			deck.Input_Card(card); //카드를 내고
			Set_Player(card); //플레이어 순서를 세팅한다
			Set_card(card); //카드를 세팅한다
			send_card = s+ "7";
		}


		//여기까지오면서 세팅된것 
		// 1. 현재 앞의 카드
		// 2. 현재 플레이어 순서
		// 3. 현재 누적공격카드 수
		// 4. 플레이어가 가지고 있는 카드 수

		//이제 한턴이 지났으므로 턴을 해야한다

		if (check == 1) //게임이 끝나는 경우!
		{
			System.out.println("exit?");

			if(this.number == 2)
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;
			}
			else if (this.number ==3)
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;

				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;
			}
			else
			{
				usr1.setOnecard(0);
				usr1.setTurnflag(0);
				usr1.bankruptcy = 0;
				
				usr2.setOnecard(0);
				usr2.setTurnflag(0);
				usr2.bankruptcy = 0;

				usr3.setOnecard(0);
				usr3.setTurnflag(0);
				usr3.bankruptcy = 0;

				usr4.setOnecard(0);
				usr4.setTurnflag(0);
				usr4.bankruptcy = 0;
			}

			return 0; //
		}
		else //게임이 계속 진행하는경우
		{
		
			//서버로 보낼 메시지 ex ) 06CurrentCard | CurrentPlayer | CurrentAttackNum |

			String data = "06";
			data = data + send_card; //현재카드
			data = data + "|";
			data = data + player;
			data = data + "|";
			data = data + attack;
			data = data +"|\0";

			//System.out.println(data);
			
			
			try
			{
				arr = data.getBytes();
				Write.write(arr);
				Write.flush(); //메시지를 보낸다
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			if(this.number == 2 )
			{
				System.out.println("현재 " + usr1.getId() + "의 카드 수 : " + usr1.getNumber() );
				System.out.println("현재 " + usr2.getId() + "의 카드 수 : " + usr2.getNumber() );
				System.out.println("현재 player : " + player );
				System.out.println("현재 카드 : "  + card );
				for(int i= 0; i<usr1.getNumber(); i++)
				{
					System.out.print( usr1.cards[i] + " " );
				}
				System.out.print("현재 " + usr2.getId() + "의 카드 :  " );
				for(int i= 0; i<usr2.getNumber(); i++)
				{
					System.out.print( usr2.cards[i] + " " );
				}

			}
			else if (this.number == 3 )
			{
				System.out.println("현재 " + usr1.getId() + "의 카드 수 : " + usr1.getNumber() );
				System.out.println("현재 " + usr2.getId() + "의 카드 수 : " + usr2.getNumber() );
				System.out.println("현재 " + usr3.getId() + "의 카드 수 : " + usr3.getNumber() );
			}
			else if (this.number == 4 )
			{
				System.out.println("현재 " + usr1.getId() + "의 카드 수 : " + usr1.getNumber() );
				System.out.println("현재 " + usr2.getId() + "의 카드 수 : " + usr2.getNumber() );
				System.out.println("현재 " + usr3.getId() + "의 카드 수 : " + usr3.getNumber() );
				System.out.println("현재 " + usr4.getId() + "의 카드 수 : " + usr4.getNumber() );
			}
			
			
			return 1;
		}
	}


	public int Penalty()
	{
		fail_flag = 0 ; //이번턴은 다른유저들은 실패하면 안된다.
		int check = 0;
		try
		{
				if(number== 2)
				{
					usr1.setTurnflag(0);
					if(usr1.getCheck() == 1 )
						usr1.setOnecard(1);
					usr1.setCheck(0);

					usr2.setTurnflag(0);
					if(usr2.getCheck() == 1 )
						usr2.setOnecard(1);
					usr2.setCheck(0);


					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr1.getId();
						data1 = data1 + "|3|1|";
						String card = deck.Select_Card();

						int temp = usr1.getNumber();
						usr1.cards[temp] = card; //카드를 세팅
						int num = usr1.getNumber();
						num =  num + 1 ;
						usr1.setNumber(num);
						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 1;
							
					}
			
					
					


					if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr2.getId();
						data1 = data1 + "|3|1|";
						String card = deck.Select_Card();

						int temp = usr2.getNumber();
						usr2.cards[temp] = card; //카드를 세팅
						int num = usr2.getNumber();
						num =  num + 1 ;
						usr2.setNumber(num);

						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 2;
							
					}
					
				}
				else if (number == 3)
				{
					usr1.setTurnflag(0);
					if(usr1.getCheck() == 1 )
						usr1.setOnecard(1);
					usr1.setCheck(0);

					usr2.setTurnflag(0);
					if(usr2.getCheck() == 1 )
						usr2.setOnecard(1);
					usr2.setCheck(0);

					usr3.setTurnflag(0);
					if(usr3.getCheck() == 1 )
						usr3.setOnecard(1);
					usr3.setCheck(0);

					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr1.getId();
						data1 = data1 + "|3|1|";
						String card1 = deck.Select_Card();

						int temp = usr1.getNumber();
						usr1.cards[temp] = card; //카드를 세팅
						int num = usr1.getNumber();
						num =  num + 1 ;
						usr1.setNumber(num);
						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 1;
							
					}
					


					if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr2.getId();
						data1 = data1 + "|3|1|";
						String card = deck.Select_Card();

						int temp = usr2.getNumber();
						usr2.cards[temp] = card; //카드를 세팅
						int num = usr2.getNumber();
						num =  num + 1 ;
						usr2.setNumber(num);
						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 2;
							
					}
					

					if(usr3.getNumber() == 1 && usr3.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr3.getId();
						data1 = data1 + "|3|1|";
						String card = deck.Select_Card();

						int temp = usr3.getNumber();
						usr3.cards[temp] = card; //카드를 세팅
						int num = usr3.getNumber();
						num =  num + 1 ;
						usr3.setNumber(num);
						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 3;
							
					}
					
				}
				else
				{
					usr1.setTurnflag(0);
					if(usr1.getCheck() == 1 )
						usr1.setOnecard(1);
					usr1.setCheck(0);

					usr2.setTurnflag(0);
					if(usr2.getCheck() == 1 )
						usr2.setOnecard(1);
					usr2.setCheck(0);

					usr3.setTurnflag(0);
					if(usr3.getCheck() == 1 )
						usr3.setOnecard(1);
					usr3.setCheck(0);

					usr4.setTurnflag(0);
					if(usr4.getCheck() == 1 )
						usr4.setOnecard(1);
					usr4.setCheck(0);

					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr1.getId();
						data1 = data1 + "|3|1|";
						String card = deck.Select_Card();
						int temp = usr1.getNumber();
						usr1.cards[temp] = card; //카드를 세팅
						int num = usr1.getNumber();
						num =  num + 1 ;
						usr1.setNumber(num);
						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 1;
							
					}
					


					if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr2.getId();
						data1 = data1 + "|3|1|";
						String card = deck.Select_Card();
						int temp = usr2.getNumber();
						usr2.cards[temp] = card; //카드를 세팅
						int num = usr2.getNumber();
						num =  num + 1 ;
						usr2.setNumber(num);
						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 2;
							
					}
					


					if(usr3.getNumber() == 1 && usr3.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr3.getId();
						data1 = data1 + "|3|1|";
						String card = deck.Select_Card();
						int temp = usr3.getNumber();
						usr3.cards[temp] = card; //카드를 세팅
						int num = usr3.getNumber();
						num =  num + 1 ;
						usr3.setNumber(num);
						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 3;
							
					}
					


					if(usr4.getNumber() == 1 && usr4.getOnecard() == 0 )
					{
						
						String data1 = "08";
						data1 = data1 + usr4.getId();
						data1 = data1 + "|3|1|";
						String card = deck.Select_Card();
						int temp = usr4.getNumber();
						usr4.cards[temp] = card; //카드를 세팅
						int num = usr4.getNumber();
						num =  num + 1 ;
						usr4.setNumber(num);
						data1 = data1 + card;
						data1 = data1 + "|\0";
								
					
						arr = data1.getBytes();
						Write.write(arr);
						Write.flush(); //메시지를 보낸다
						check = 4;
							
					}
					
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return check;

	}
	public int Take_Phone(String msg) //핸드폰에서 카드를 먹으려고 요청을 했을 때 호출해야하는 메소드이다
	{
		int Return= 0;
		//요청하는경우는 공격카드이거나 일반카드일때이다.
		 
		//메시지의 예
		//14usr1|3|   usr1이 먹을 카드 수가 3이다

		//일단 현재 카드가 공격카드인경우 공격카드의 효과를 무시하게 만들어야한다.
		//System.out.println("현재카드 : "+card);
		

		System.out.println("call Take_Phone()");
		if(card.equals("s1")  ) // 스페이드 a이거나 스페이드면
		{
			send_card = "sz1"; // 스페이드문양의 모든 카드가 가능하다. 
		}
		else if ( card.equals("s2"))
		{
			send_card ="sz2";
		}
		else if(card.equals("c1"))
		{
			send_card = "cz1";
		}
		else if(card.equals("c2"))
		{
			send_card = "cz2";
		}
		else if(card.equals("h1"))
		{
			send_card = "hz1";
		}
		else if(card.equals("h2"))
		{
			send_card = "hz2";
		}
		else if(card.equals("d1"))
		{
			send_card = "dz1";
		}
		else if(card.equals("d2"))
		{
			send_card ="dz2";
		}
		else if(card.equals("tj")) //블랙조커면
		{
			send_card ="tz"; // 블랙문양만 가능
		}
		else if(card.equals("uj"))
		{
			send_card = "uz"; // 모든문양이 가능
		}
		else // 나머지 일반카드면
		{
			String number = card.substring(1,2);//넘버가 7이면
			if(number.equals("7"))
			{
				send_card = send_card;
			}
			else
			{
				System.out.println("else!!!");
				send_card = card;
			}
		}
		//send_card = card;

		if(card.equals("tj"))
		{
			send_card = "tz";
		}
		if(card.equals("uj"))
		{
			send_card = "uz";
		}
		//공격카드를 못막았던 일반카드를 못막았던 다음턴은 누적공격수를 1로 설정해야한다
		attack = 1 ; //누적공격수를 1로 설정한다.

		


		//이런식이다
		String copy = msg.substring(2,msg.length()); //헤더를 자른다
		StringTokenizer st = new StringTokenizer(copy, "|"); //구분자로 읽는다
		
		
		
		String usr = st.nextToken(); //처음엔 아이디가 온다

		String count = st.nextToken(); //먹을 카드 수가 온다

		int number = Integer.parseInt(count, 10); //숫자로 변환

		

		Set_Player(); //현재 플레이어를 갱신한다


		//먹을 카드 수 만큼 그 플레이어의 카드수를 늘려야 한다.

		if(usr.equals(usr1.getId() ) ) //유저가 유저1이라면
		{
			Return = 1;
			int num = usr1.getNumber();
			if ( num == 1) //원래 한장이였다면 원카드프레그를 0으로 세팅해야한다
			{
				usr1.setOnecard(0);
			}
			num = num + number;
			usr1.setNumber(num);
		}
		else if (usr.equals(usr2.getId()))
		{
			Return = 2;
			int num = usr2.getNumber();
			if ( num == 1) //원래 한장이였다면 원카드프레그를 0으로 세팅해야한다
			{
				usr2.setOnecard(0);
			}
			num = num + number;
			usr2.setNumber(num);
		}
		else if (usr.equals(usr3.getId()))
		{
			Return = 3;
			int num = usr3.getNumber();
			if ( num == 1) //원래 한장이였다면 원카드프레그를 0으로 세팅해야한다
			{
				usr3.setOnecard(0);
			}
			num = num + number;
			usr3.setNumber(num);
		}
		else
		{
			Return = 4;
			int num = usr4.getNumber();
			if ( num == 1) //원래 한장이였다면 원카드프레그를 0으로 세팅해야한다
			{
				usr4.setOnecard(0);
			}
			num = num +number;
			usr4.setNumber(num);
		}

		//여기까지오면 플레이어 객체에 현재 플레이어가 가지고 있는 카드 수를 갱신을 했다.

		//이제 카드를 카드수만큼 뽑아서 줘야한다

		// 05usr1|3|a1|a2|a3| 이런식으로 메시지를 만들어야한다

		String data= "05";
		data = data + usr;
		data = data + "|";
		data = data + count;
		data = data + "|";
		

		for(int i=0; i<number; i++) //카드 수만큼 반복
		{
			String card = deck.Select_Card(); //한장을 뽑고
			if(usr.equals(usr1.getId()))
			{
				int temp = usr1.getNumber()-number;
				usr1.cards[temp+i] = card; //카드를 세팅
			}
			else if (usr.equals(usr2.getId()))
			{
				int temp = usr2.getNumber()-number;
				usr2.cards[temp+i] = card;
			}
			else if (usr.equals(usr3.getId()))
			{
				int temp = usr3.getNumber()-number;
				usr2.cards[temp+i] = card;
			}
			else
			{
				int temp = usr4.getNumber()-number;
				usr4.cards[temp+i] = card;
			}
			//메시지에 추가한다
			data = data + card;
			data = data + "|";
		}
		data = data + "\0";
		//여기까지오면 메시지가 완성이 됬다

		try
		{
			arr = new byte[100];
			arr = data.getBytes();
			Write.write(arr);
			Write.flush(); //메시지를 보낸다
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return Return;
		


	}


	public int Turn()
	{
			//카드 정보를 보냇으니 이제 턴이 끝났고 턴정보를 넘겨줘야한다!
		int Return = 0;
		
		//서버로 보낼 메시지 ex ) 06CurrentCard | CurrentPlayer | CurrentAttackNum |
		System.out.println(" turn() " );
		String data5 = "06";
		data5 = data5 + send_card; //현재카드
		data5 = data5 + "|";
		data5 = data5 + player;
		data5 = data5 + "|";
		data5 = data5 + attack;
		data5 = data5 +"|\0";

		//System.out.println(data);
		
		
		try
		{
			
			//Thread.sleep(100);
			System.out.println("send turn information");
			System.out.println(data5);
			arr = new byte[100];
			arr = data5.getBytes();
			Write.write(arr);
			Write.flush(); //메시지를 보낸다
		
			if(getNumber() == 2)
			{
				Return = 2;
				usr1.setTurnflag(0);
				usr2.setTurnflag(0);
			}
			else if (getNumber() == 3)
			{
				Return = 3;
				usr1.setTurnflag(0);
				usr2.setTurnflag(0);
				usr3.setTurnflag(0);
			}
			else
			{
				Return = 4;
				usr1.setTurnflag(0);
				usr2.setTurnflag(0);
				usr3.setTurnflag(0);
				usr4.setTurnflag(0);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	

		if(this.number == 2 )
		{
			
			System.out.println("현재 " + usr1.getId() + "의 카드 수 : " + usr1.getNumber() );
			System.out.println("현재 " + usr2.getId() + "의 카드 수 : " + usr2.getNumber() );
			System.out.println("현재 player : " + player );
			System.out.println("현재 카드 : "  + card );
			System.out.print("현재 " + usr1.getId() + "의 카드 :  " );
			for(int i= 0; i<usr1.getNumber(); i++)
			{
				System.out.print( usr1.cards[i] + " " );
			}
			System.out.print("현재 " + usr2.getId() + "의 카드 :  " );
			for(int i= 0; i<usr2.getNumber(); i++)
			{
				System.out.print( usr2.cards[i] + " " );
			}

		}
		else if ( this.number == 3 )
		{
			System.out.println("현재 " + usr1.getId() + "의 카드 수 : " + usr1.getNumber() );
			System.out.println("현재 " + usr2.getId() + "의 카드 수 : " + usr2.getNumber() );
			System.out.println("현재 " + usr3.getId() + "의 카드 수 : " + usr3.getNumber() );
		}
		else
		{
			System.out.println("현재 " + usr1.getId() + "의 카드 수 : " + usr1.getNumber() );
			System.out.println("현재 " + usr2.getId() + "의 카드 수 : " + usr2.getNumber() );
			System.out.println("현재 " + usr3.getId() + "의 카드 수 : " + usr3.getNumber() );
			System.out.println("현재 " + usr4.getId() + "의 카드 수 : " + usr4.getNumber() );
		}
		return Return;
	}

	public void One_Card(String msg)
	{
		// 17usr1| //이런식으로 메시지가 온다

		
		String copy = msg.substring(2,msg.length()); //헤더를 자른다
		StringTokenizer st = new StringTokenizer(copy, "|"); //구분자로 읽는다

		
		
		
		String usr = st.nextToken(); //처음엔 아이디가 온다

		//원카드를 할 자격이 있는지 알아봐야한다!

		// case 1 : 요청할 자격이 없을때 한경우
		
		//한장남은사람이 있는지 없는지 체크부터한다

		//일단 자신의 아이디 확인하고 자신의 아이디가 아니면 다른아이디를 확인한다. 다른아이디가 2개일 수 있다

		int check = 0;

		if(fail_flag == 1 ) //이미 실패해서 카드수가 늘어났으므로 이 턴에는 다른유저는 늦었다는 메시지를 보내줘야한다
		{
			int send = 0;

			if(number == 2 )
			{
				if(usr.equals(usr1.getId()))
				{
					if(usr1.getCheck() == 1 ) //이미 실패했으면
					{
						send =1;
					}
				}
				else if(usr.equals(usr2.getId()))
				{
					if(usr2.getCheck() == 1 )
					{
						send = 1;
					}
				}
			}
			else if (number == 3)
			{
				if(usr.equals(usr1.getId()))
				{
					if(usr1.getCheck() == 1)
					{
						send = 1;
					}
				}
				else if(usr.equals(usr2.getId()))
				{
					if(usr2.getCheck() == 1)
					{
						send = 1;
					}
				}
				else if(usr.equals(usr3.getId()))
				{
					if(usr3.getCheck() == 1)
					{
						send = 1;
					}
				}
			}
			else //4인일때
			{
				if(usr.equals(usr1.getId()))
				{
					if(usr1.getCheck() == 1)
						send = 1;
				}
				else if(usr.equals(usr2.getId()))
				{
					if(usr2.getCheck() == 1)
						send = 1;
				}
				else if(usr.equals(usr3.getId()))
				{
					if(usr3.getCheck() == 1)
						send = 1;
				}
				else if(usr.equals(usr4.getId()))
				{
					if(usr4.getCheck() == 1)
						send = 1;
				}
			}
			if(send == 0 ) 
			{
				//case4의 경우이다 
				String data = "08";
				data = data + usr;
				data = data +"|4|\0";

				try
				{	
					arr = data.getBytes();
					Write.write(arr);
					Write.flush(); //메시지를 보낸다
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}

			return ;
		}

		if (number == 2 )
		{
			System.out.println("usr1의 getOnecard() : " + usr1.getOnecard() );
			System.out.println("usr2의 getOnecard() : " + usr2.getOnecard() );
			if ( usr1.getNumber() == 1 && usr1.getOnecard() == 0 ) //유저1이 한장남았고 원카드를 처음외치는경우
			{
				check = 1;
			}
			if (usr2.getNumber() == 1 && usr2.getOnecard() == 0)
				check = 1;
		}
		else if (number == 3 )
		{
			if ( usr1.getNumber() == 1 && usr1.getOnecard() == 0 ) //유저1이 한장남았다면
			{
				check = 1;
			}
			if (usr2.getNumber() == 1 && usr2.getOnecard() == 0)
				check = 1;
			if (usr3.getNumber() == 1 && usr3.getOnecard() == 0)
				check = 1;
		}
		else
		{
			if ( usr1.getNumber() == 1 && usr1.getOnecard() == 0 ) //유저1이 한장남았다면
			{
				check = 1;
			}
			if (usr2.getNumber() == 1 && usr2.getOnecard() == 0)
				check = 1;
			if (usr3.getNumber() == 1 && usr3.getOnecard() == 0)
				check = 1;
			if (usr4.getNumber() == 1 && usr4.getOnecard() == 0)
				check = 1;
		}

		if( check == 0 ) //아무도 한장이 된사람이 없거나 원카드를 이미 이전에 해서 가지고 있을 때!
		{
			//원카드 실패 case1 & case 3에 해당한다

			String data = "08";
			data = data + usr;
			data = data +"|1|2|";
			String card = deck.Select_Card(); //카드를 뽑고
			if(number == 2)
			{
				if(usr.equals(usr1.getId()))
				{
					int num = usr1.getNumber();
					usr1.cards[num] = card;
					num= num +1;
					usr1.setNumber(num);
				}
				else
				{
					int num = usr2.getNumber();
					usr2.cards[num] = card;
					num= num +1;
					usr2.setNumber(num);
				}
			}
			else if (number == 3 )
			{
				if(usr.equals(usr1.getId()))
				{
					int num = usr1.getNumber();
					usr1.cards[num] = card;
					num= num +1;
					usr1.setNumber(num);
				}
				else if (usr.equals(usr2.getId()))
				{
					int num = usr2.getNumber();
					usr2.cards[num] = card;
					num= num +1;
					usr2.setNumber(num);
				}
				else
				{
					int num = usr3.getNumber();
					usr3.cards[num] = card;
					num= num +1;
					usr3.setNumber(num);
				}
			}
			else
			{
				if(usr.equals(usr1.getId()))
				{
					int num = usr1.getNumber();
					usr1.cards[num] = card;
					num= num +1;
					usr1.setNumber(num);
				}
				else if (usr.equals(usr2.getId()))
				{
					int num = usr2.getNumber();
					usr2.cards[num] = card;
					num= num +1;
					usr2.setNumber(num);
				}
				else if(usr.equals(usr3.getId()))
				{
					int num = usr3.getNumber();
					usr3.cards[num] = card;
					num= num +1;
					usr3.setNumber(num);
				}
				else
				{
					int num = usr4.getNumber();
					usr4.cards[num] = card;
					num= num +1;
					usr4.setNumber(num);
				}
			}



			data = data + card;
			data = data + "|";
			card = deck.Select_Card(); //카드를 뽑고
			if(number == 2)
			{
				if(usr.equals(usr1.getId()))
				{
					int num = usr1.getNumber();
					usr1.cards[num] = card;
					num= num +1;
					usr1.setNumber(num);
				}
				else
				{
					int num = usr2.getNumber();
					usr2.cards[num] = card;
					num= num +1;
					usr2.setNumber(num);
				}
			}
			else if (number == 3 )
			{
				if(usr.equals(usr1.getId()))
				{
					int num = usr1.getNumber();
					usr1.cards[num] = card;
					num= num +1;
					usr1.setNumber(num);
				}
				else if (usr.equals(usr2.getId()))
				{
					int num = usr2.getNumber();
					usr2.cards[num] = card;
					num= num +1;
					usr2.setNumber(num);
				}
				else
				{
					int num = usr3.getNumber();
					usr3.cards[num] = card;
					num= num +1;
					usr3.setNumber(num);
				}
			}
			else
			{
				if(usr.equals(usr1.getId()))
				{
					int num = usr1.getNumber();
					usr1.cards[num] = card;
					num= num +1;
					usr1.setNumber(num);
				}
				else if (usr.equals(usr2.getId()))
				{
					int num = usr2.getNumber();
					usr2.cards[num] = card;
					num= num +1;
					usr2.setNumber(num);
				}
				else if(usr.equals(usr3.getId()))
				{
					int num = usr3.getNumber();
					usr3.cards[num] = card;
					num= num +1;
					usr3.setNumber(num);
				}
				else
				{
					int num = usr4.getNumber();
					usr4.cards[num] = card;
					num= num +1;
					usr4.setNumber(num);
				}
			}
			data = data + card;
			data = data +"|\0";
			//메시지가 완성되었다
				
			try
			{
				arr = data.getBytes();
				Write.write(arr);
				Write.flush(); //메시지를 보낸다
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			//패널티로 2장을 먹인다
			//플레그 1로 설정해야한다
		}
		else // 누군가 한장을 갖고있고 원카드를 안외친 사람이 있다!!
		{
			if(number == 2) //2인일때
			{
				if(usr.equals(usr1.getId())) //usr가 usr1일때를 생각해보자
				{
						//자기자신이 성공할 수 있는 케이스가 있다.
					if(usr1.getNumber() == 1 && usr1.getOnecard()==0 ) //자기자신이 한장이고 원카드를 외치지 않은경우!?
					{
						if(usr1.getTurnflag() == 1 ) //이미 자기자신이 원카드를 성공하거나 실패했으면?
						{
							//이미 실패했거나 성공했다는 메시지가 갔을것이다
							/*
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}*/
						}
						else //자기 자신이 성공하거나 실패하지않은경우이다
						{	
							
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 )  //usr2도 한장일경우와 아직 원카드 성공을 안한경우
					{
						if(usr2.getTurnflag() == 1 ) //이미 유저2가 원카드를 했을경우
						{
							
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else //유저2가 원카드를 아직 외치지 않았거나 이미 한장을 있을떄 원카드를 한 경우이다
						{
							
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr2.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							//그리고 2p에게 case2 의 실패 메시지를 보내야 한다

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.

							String data2 = "08";
							data2 = data2 + usr2.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr2.getNumber();
							usr2.cards[num] = card;
							num= num +1;
							usr2.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";
							
							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}	
				}
				else // 원카드를 요청한사람(usr)가 usr2일때를 생각해보자
				{
					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 ) //usr1이 한장을 갖고 있고, 아직 원카드를 안했을때
					{
						if(usr1.getTurnflag()==1 ) //이미 이사람이 원카드를 요청했을 때
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else // 원카드를 요청하지 않았을때 
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							//그리고 1p에게 case2 의 실패 메시지를 보내야 한다

								
							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							String data2 = "08";
							data2 = data2 + usr1.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr1.getNumber();
							usr1.cards[num] = card;
							num= num +1;
							usr1.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 ) 
					{
						if(usr2.getTurnflag()==1 ) //이미 1p가 원카드를 요청했을 때
						{
							/*
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}*/
						}
						else // 아무도 원카드를 요청안했을때
						{
							
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr2.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
			else if ( number == 3 ) //3인일때
			{
				if(usr.equals(usr1.getId())) // 원카드를 요청한 usr가 usr1일때
				{
					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 ) //자기 자신이 원카드를 외치지않았을때
					{
						if(usr1.getTurnflag() == 1) //이미 usr2나 usr3이 원카드를 외쳤을때
						{
							//이미 실패메시지가 갔다
						}
						else // 원카드를 자신이 외쳤을때
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if(usr2.getNumber() == 1 && usr2.getOnecard() == 0) 
					{
						if(usr2.getTurnflag() == 1 ) //이미 usr2나 usr3이 원카드를 외친경우
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else //아직 usr2가 원카드를 외치지 않은경우이다
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr2.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr2.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr2.getNumber();
							usr2.cards[num] = card;
							num= num +1;
							usr2.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if ( usr3.getNumber() == 1 && usr3.getOnecard() == 0 )
					{
						if(usr3.getTurnflag() == 1) //이미 usr3이나 usr2가 원카드를 외친 경우
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else //내가 (usr1) 원카드를 외치는 경우
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr3.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr3.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr3.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr3.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr3.getNumber();
							usr3.cards[num] = card;
							num= num +1;
							usr3.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
				else if ( usr.equals(usr2.getId())) //원카드를 외친 유저가 유저2일때
				{
					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 ) //유저 1이 원카드를 외쳐야할때
					{
						if(usr1.getTurnflag() == 1 ) //이미 누가 외친경우
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr1.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr1.getNumber();
							usr1.cards[num] = card;
							num= num +1;
							usr1.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if ( usr2.getNumber() == 1 && usr2.getOnecard() == 0 ) //자기 자신이 해야할차례일때
					{
						if(usr2.getTurnflag() == 1 )
						{
							
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						}
					}
					else if ( usr3.getNumber() == 1 && usr3.getOnecard() == 0 )
					{
						if(usr3.getTurnflag() == 1)
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr3.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr3.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr3.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr3.getNumber();
							usr3.cards[num] = card;
							num= num +1;
							usr3.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
				else // usr3가 usr일때
				{
					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 ) //유저 1이 원카드를 외쳐야할때
					{
						if(usr1.getTurnflag() == 1 ) //이미 누가 외친경우
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr1.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr1.getNumber();
							usr1.cards[num] = card;
							num= num +1;
							usr1.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if ( usr2.getNumber() == 1 && usr2.getOnecard() == 0 ) //자기 자신이 해야할차례일때
					{
						if(usr2.getTurnflag() == 1 )
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}


							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr2.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr2.getNumber();
							usr2.cards[num] = card;
							num= num +1;
							usr2.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						}
					}
					else if ( usr3.getNumber() == 1 && usr3.getOnecard() == 0 )
					{
						if(usr3.getTurnflag() == 1)
						{
							
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr3.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr3.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							
						}
					}
				}
			}
			else //4인일때
			{
				if(usr.equals(usr1.getId()))// usr가 usr1일때
				{
					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 )
					{
						if(usr1.getTurnflag() == 1 )
						{
							//자기자신은 이미 실패를 받았을것이다
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 )
					{
						if(usr2.getTurnflag() == 1 )
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr2.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr2.getNumber();
							usr2.cards[num] = card;
							num= num +1;
							usr2.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if ( usr3.getNumber() == 1 && usr3.getOnecard() == 0)
					{
						if(usr3.getTurnflag() == 1)
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr3.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr3.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr3.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr3.getNumber();
							usr3.cards[num] = card;
							num= num +1;
							usr3.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else // usr4 인경우
					{
						if(usr4.getTurnflag() == 1)
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr4.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr4.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr4.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr4.getNumber();
							usr4.cards[num] = card;
							num= num +1;
							usr4.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
				else if ( usr.equals(usr2.getId()))  //유저 2가 원카드를 외쳤을떄
				{
					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 )
					{
						if(usr1.getTurnflag() == 1 )
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							//자기자신은 이미 실패를 받았을것이다
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr1.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr1.getNumber();
							usr1.cards[num] = card;
							num= num +1;
							usr1.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 )
					{
						if(usr2.getTurnflag() == 1 )
						{
							
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						}
					}
					else if ( usr3.getNumber() == 1 && usr3.getOnecard() == 0)
					{
						if(usr3.getTurnflag() == 1)
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr3.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr3.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr3.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr3.getNumber();
							usr3.cards[num] = card;
							num= num +1;
							usr3.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else // usr4 인경우
					{
						if(usr4.getTurnflag() == 1)
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr4.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr4.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr4.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr4.getNumber();
							usr4.cards[num] = card;
							num= num +1;
							usr4.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
				else if( usr.equals(usr3.getId()))
				{
					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 )
					{
						if(usr1.getTurnflag() == 1 )
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							//자기자신은 이미 실패를 받았을것이다
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr1.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr1.getNumber();
							usr1.cards[num] = card;
							num= num +1;
							usr1.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 )
					{
						if(usr2.getTurnflag() == 1 )
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr2.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr2.getNumber();
							usr2.cards[num] = card;
							num= num +1;
							usr2.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						}
					}
					else if ( usr3.getNumber() == 1 && usr3.getOnecard() == 0)
					{
						if(usr3.getTurnflag() == 1)
						{
							
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr3.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr3.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						}
					}
					else // usr4 인경우
					{
						if(usr4.getTurnflag() == 1)
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr4.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr4.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr4.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr4.getNumber();
							usr4.cards[num] = card;
							num= num +1;
							usr4.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
				else //외친사람이 4인일때
				{
					if(usr1.getNumber() == 1 && usr1.getOnecard() == 0 )
					{
						if(usr1.getTurnflag() == 1 )
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							//자기자신은 이미 실패를 받았을것이다
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr1.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr1.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr1.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr1.getNumber();
							usr1.cards[num] = card;
							num= num +1;
							usr1.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else if(usr2.getNumber() == 1 && usr2.getOnecard() == 0 )
					{
						if(usr2.getTurnflag() == 1 )
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr2.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr2.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr2.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr2.getNumber();
							usr2.cards[num] = card;
							num= num +1;
							usr2.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						}
					}
					else if ( usr3.getNumber() == 1 && usr3.getOnecard() == 0)
					{
						if(usr3.getTurnflag() == 1)
						{
							//case4의 경우이다 
							String data = "08";
							data = data + usr;
							data = data +"|4|\0";

							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr3.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							//usr3.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
								Thread.sleep(150);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}

							fail_flag = 1 ; //이번턴은 다른유저들은 실패하면 안된다.
							//usr2에게 실패메시지를 뿌려줘야한다
							String data2 = "08";
							data2 = data2 + usr3.getId();
							data2 = data2 + "|2|1|";
							String card = deck.Select_Card();
							int num = usr3.getNumber();
							usr3.cards[num] = card;
							num= num +1;
							usr3.setNumber(num);
	
							data2 = data2 + card;
							data2 = data2 + "|\0";

							try
							{
								arr = data2.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else // usr4 인경우
					{
						if(usr4.getTurnflag() == 1)
						{
							
						}
						else
						{
							//case 5의 성공메시지를 자기자신한테 보내주면된다!
							String data = "08";
							data = data + usr;
							data = data +"|5|\0";
							usr4.setTurnflag(1); //이번턴에 또다른사람이 이타겟으로 원카드를 외쳐도 무효하게 하기위한것
							//usr1.setOnecard(1); //1장이 계속남아있는경우 카드수가 변하지않는이상 원카드를 무효처리
							usr4.setCheck(1);
							try
							{
								arr = data.getBytes();
								Write.write(arr);
								Write.flush(); //메시지를 보낸다
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						}
					}
				}
			}
		}
	}


							
						
						


								




					
		
	
		
			
		



	public void Game_Start() //게임시작을 하면 현재 앞 카드를 세팅, 그리고 각 플레이어에게 해당 카드를 분배한다
	{
		//보내야할정보, 현재 앞 카드, 현재 플레이어 순서, 플레이어수, 카드정보들

		

		String first ;
		while(true)
		{
			deck.Card_Mix(); //카드를 섞고!

			//deck.Card_Demo();

			first = deck.Select_Card(); // 카드 한장을 꺼낸다

			if(first.equals("tj") || first.equals("uj") || first.equals("s1") || first.equals("s2")
				||first.equals("c1") || first.equals("c2") || first.equals("d1") || first.equals("d2") || first.equals("h1") || first.equals("h2")) // 조커라면 다시 섞기
			{
				System.out.println("다시섞었을때 나옴");
			}
			else
			{
				break;
			}
		}
			

		
		
		String m = first.substring(0,1); //문양을 확인하기 위해서 앞자리를 본다
		
		if(first.equals("tj") ) //블랙조커라면 ?
		{
			shape ="t"; // 문양은 검은 문양이 다된다
		}
		else if (first.equals("uj")) //칼라조커라면
		{
			shape ="u"; //문양은 색깔있는문양이 다된다
		}
		else if( m.equals("c")) //크로버라면
		{
			shape = "c";
		}
		else if(m.equals("s"))
		{
			shape ="s";
		}
		else if(m.equals("h"))
		{
			shape="h";
		}
		else
		{
			shape="d";
		}


		
		Set_card(first); //현재 카드를 세팅

		send_card = first; //보낼카드에 first로 세팅
		

		String msg = "02";
		msg = msg + send_card;
		msg = msg + "|";
		msg = msg + player ;
		msg = msg + "|";
		msg = msg + attack;
		msg = msg + "|";
		msg = msg + number;
		msg = msg + "|";
		
		if (number == 2 ) //2인이면
		{
			
			msg = msg + usr1.getId();
			msg = msg + "|";
			//6장 뽑아서 줘야한다.
			for(int i=0; i<6; i++)
			{
				first = deck.Select_Card();
				usr1.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first ;
				msg = msg+"|";
			}
			msg = msg  + usr2.getId();
			msg = msg +"|";
			for(int i=0; i<6; i++)
			{
				first = deck.Select_Card();
				usr2.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first;
				msg = msg + "|";
			}
		}
		else if ( number == 3 ) //3인이면 5장 줘야한다
		{
			msg = msg + usr1.getId();
			msg = msg+ "|";
			for(int i=0; i<5; i++)
			{
				first = deck.Select_Card();
				usr1.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first ;
				msg = msg+"|";
			}
			msg = msg  + usr2.getId();
			msg = msg +"|";
			for(int i=0; i<5; i++)
			{
				first = deck.Select_Card();
				usr2.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first;
				msg = msg + "|";
			}
			msg = msg  + usr3.getId();
			msg = msg +"|";
			for(int i=0; i<5; i++)
			{
				first = deck.Select_Card();
				usr3.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first;
				msg = msg + "|";
			}
		}
		else   //4인일때
		{
			msg = msg  + usr1.getId();
			msg = msg +"|";
			for(int i=0; i<5; i++)
			{
				first = deck.Select_Card();
				usr1.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first;
				msg = msg + "|";
			}
			msg = msg  + usr2.getId();
			msg = msg +"|";
			for(int i=0; i<5; i++)
			{
				first = deck.Select_Card();
				usr2.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first;
				msg = msg + "|";
			}
			msg = msg  + usr3.getId();
			msg = msg +"|";
			for(int i=0; i<5; i++)
			{
				first = deck.Select_Card();
				usr3.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first;
				msg = msg + "|";
			}
			msg = msg  + usr4.getId();
			msg = msg +"|";
			for(int i=0; i<5; i++)
			{
				first = deck.Select_Card();
				usr4.cards[i] = new String(first); //카드를 넣는다
				msg = msg + first;
				msg = msg + "|";
			}
		}
		msg = msg + "\0"; //!! 메세지끝은 항상 널로 해야한다!!

		//여기까지오면 메시지가 완성되었다
		System.out.println(msg);
		try
		{
			/*
			String str = "001234|\0"; //가상으로 방을만들고
			arr = str.getBytes();
			Write.write(arr);
			Write.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			str = br.readLine(); //잠깐 방을 멈춘다*/

			arr = msg.getBytes();
			Write.write(arr);
			Write.flush(); //메시지를 보낸다
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		


	}
			

		
		
		
		

	public int Attack_Card(String s) //카드를 주면 그카드의 공격수를 리턴. 
	{
		String shape =s.substring(0,1);
		//System.out.println( "Shape is = " + shape);
		
		String num = s.substring(1,2);
		//System.out.println( "num is = " + num );
		if ( num.equals( "1") ) //문양이 A를 말한다
		{
			
			if( shape.equals( "s")) //스페이드 A이면
			{
				return 5;
			}
			//스페이드 A를 제외한 A이면 
			return 3;
		}
		if (num.equals( "2") ) //2인 아무 문양이면
		{
			return 2;
		}
		if ( s.equals("tj") ) //블랙조커면
		{
			return 7;
		}
		if( s.equals("uj") ) //칼라조커면
		{
			return 10;
		}

		//그외에 평범한 카드들이면 

		return 1;
	}


			

	public void Set_card ( String s) //현재 카드를 세팅
	{
		card = s; 
		
		int temp = Attack_Card( s ); //카드의 공격능력을 받아옴

		if ( card.equals("s1") || card.equals("s2") || card.equals("c1") || card.equals("c2") || card.equals("h1") || card.equals("h2") || card.equals("d1") || card.equals("d2") || card.equals("tj") || card.equals("uj") )
		{
			//공격 카드였다면
			if ( attack == 1 ) //그전에 일반카드였다면
			{
				attack = temp; // 현재 누적 공격 카드수를 지정
			}
			else //이미 누적된 공격카드수가 있다면 ? 즉 다른 카드를 내서 방어한경우
			{
				attack = attack + temp; // 누적 공격카드수를 갱신
			}

		}
		else //공격카드가 아니라면
		{
			attack = 1 ; // 기본 공격 카드수로 갱신
		}
	}

	public String getPlayer () // 현재 누구 순서인지 알려줌
	{
		return player;
	}

	public int getAttack() //현재 누적공격카드 수를 알려줌
	{
		return attack;
	}


	

	public void Set_Player() // 카드를 내지 않았을 때
	{
		if (number == 2) //2명이서 게임을 하고 있을 때
		{
			if (player.equals(usr1.getId())) //현재 플레이어가 usr1이였으면
			{
				player = usr2.getId();
			}
			else
				player = usr1.getId();
		}
		else if (number == 3 )
		{
			if(usr1.bankruptcy == 0 && usr2.bankruptcy == 0 && usr3.bankruptcy == 0 ) //세명다 파산을 안했을때
			{
				if(reverse == 0 )//정방향이면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr2.getId();
					}
					else if (player.equals(usr2.getId()))
					{
						player = usr3.getId();
					}
					else
					{
						player = usr1.getId();
					}
				}
				else //역방향이면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr3.getId();
					}
					else if (player.equals(usr2.getId()))
					{
						player = usr1.getId();
					}
					else
					{
						player = usr2.getId();
					}
				}
			}
			else if (usr1.bankruptcy == 1 ) //유저 1이 파산됬을때는
			{
				if(player.equals(usr2.getId()))//유저2가 차례였을때는
				{
					player = usr3.getId();
				}
				else
				{
					player = usr2.getId();
				}
			}
			else if(usr2.bankruptcy == 1) //유저2가 파산됬을때는
			{
				if(player.equals(usr1.getId()))
				{
					player = usr3.getId();
				}
				else
				{
					player = usr1.getId();
				}
			}
			else //유저3이 파산됬을떄는
			{
				if(player.equals(usr1.getId()))
				{
					player = usr2.getId();
				}
				else
				{
					player = usr1.getId();
				}
			}
						
		}
		else //4인이면
		{
			if(usr1.bankruptcy == 0 && usr2.bankruptcy == 0 && usr3.bankruptcy == 0 && usr4.bankruptcy == 0 ) //모드 파산을 안했으면
			{
				if(reverse == 0) //정방향이면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr2.getId();
					}
					else if (player.equals(usr2.getId()))
					{
						player = usr3.getId();
					}
					else if (player.equals(usr3.getId()))
					{
						player = usr4.getId();
					}
					else
						player = usr1.getId();
				}
				else //역방향이면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr4.getId();
					}
					else if (player.equals(usr2.getId()))
					{
						player = usr1.getId();
					}
					else if (player.equals(usr3.getId()))
					{
						player = usr2.getId();
					}
					else
						player = usr3.getId();
				}
			}
			else if ( usr1.bankruptcy == 1 && usr2.bankruptcy == 1 ) //유저 1 2 가 파산했을때
			{
				if(player.equals(usr3.getId()))
				{
					player = usr4.getId();
				}
				else
				{
					player = usr3.getId();
				}
			}
			else if (usr1.bankruptcy == 1 && usr3.bankruptcy == 1) //유저 1 3 파산
			{
				if(player.equals(usr2.getId()))
				{
					player = usr4.getId();
				}
				else
				{
					player = usr2.getId();
				}
			}
			else if(usr1.bankruptcy == 1 && usr4.bankruptcy == 1 ) //유저 1 4 파산
			{
				if(player.equals(usr2.getId()))
				{
					player = usr3.getId();
				}
				else
				{
					player = usr2.getId();
				}
			}
			else if(usr2.bankruptcy ==1 && usr3.bankruptcy == 1 ) // 유저 2 3 파산
			{
				if(player.equals(usr1.getId()))
				{
					player = usr4.getId();
				}
				else
				{
					player = usr1.getId();
				}
			}
			else if(usr2.bankruptcy == 1 && usr4.bankruptcy == 1 ) //유저 2 4 파산
			{
				if(player.equals(usr1.getId()))
				{
					player = usr3.getId();
				}
				else
				{
					player = usr1.getId();
				}
			}
			else if(usr3.bankruptcy == 1 && usr4.bankruptcy == 1) //유저 3 4 파산
			{
				if(player.equals(usr1.getId()))
				{
					player = usr2.getId();
				}
				else
				{
					player = usr1.getId();
				}
			}
			else if( usr1.bankruptcy ==1 ) //유저 1 이 파산일경우
			{
				if(reverse == 0) //정방향이면
				{
					if(player.equals(usr2.getId())) //유저 1이 시작하고있었으면
					{
						player = usr3.getId();
					}
					else if(player.equals(usr3.getId())) 
					{
						player =usr4.getId();
					}
					else if(player.equals(usr4.getId()))
					{
						player = usr2.getId();
					}
				}
				else //역방향이면
				{
					if(player.equals(usr2.getId()))
					{
						player = usr4.getId();
					}
					else if (player.equals(usr3.getId()))
					{
						player = usr2.getId();
					}
					else if (player.equals(usr4.getId()))
					{
						player =usr3.getId();
					}
				}
			}
			else if ( usr2.bankruptcy == 1) //유저 2가 파산할경우
			{
				if(reverse == 0) //정방향이면
				{
					if(player.equals(usr1.getId())) //유저 1이 시작하고있었으면
					{
						player = usr3.getId();
					}
					else if(player.equals(usr3.getId())) 
					{
						player =usr4.getId();
					}
					else if(player.equals(usr4.getId()))
					{
						player = usr1.getId();
					}
				}
				else //역방향이면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr4.getId();
					}
					else if (player.equals(usr3.getId()))
					{
						player = usr1.getId();
					}
					else if (player.equals(usr4.getId()))
					{
						player =usr3.getId();
					}
				}
			}
			else if(usr3.bankruptcy ==1 ) ///유저 3이 파산할 경우
			{
				if(reverse == 0) //정방향이면
				{
					if(player.equals(usr1.getId())) //유저 1이 시작하고있었으면
					{
						player = usr2.getId();
					}
					else if(player.equals(usr2.getId())) 
					{
						player =usr4.getId();
					}
					else if(player.equals(usr4.getId()))
					{
						player = usr1.getId();
					}
				}
				else //역방향이면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr4.getId();
					}
					else if (player.equals(usr2.getId()))
					{
						player = usr1.getId();
					}
					else if (player.equals(usr4.getId()))
					{
						player =usr2.getId();
					}
				}
			}
			else if(usr4.bankruptcy == 1) //유저4가 파산할경우
			{
				if(reverse == 0) //정방향이면
				{
					if(player.equals(usr1.getId())) //유저 1이 시작하고있었으면
					{
						player = usr2.getId();
					}
					else if(player.equals(usr2.getId())) 
					{
						player =usr3.getId();
					}
					else if(player.equals(usr3.getId()))
					{
						player = usr1.getId();
					}
				}
				else //역방향이면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr3.getId();
					}
					else if (player.equals(usr2.getId()))
					{
						player = usr1.getId();
					}
					else if (player.equals(usr3.getId()))
					{
						player =usr2.getId();
					}
				}
			}
			

		}
	}



	public void Set_Player(String card ) // 카드에 의해 순서가 바뀌는것을 알려줌
	{
	
		String num = card.substring(1,2);
		//숫자가 k j q 인지를 확인
		
		if ( num.equals( "b" ) )// j이면
		{
			if ( number == 2 )
			{
				player = player ; //2인에서 j면 다음턴도 그사람이다
			}
			else if ( number == 3 ) //3인이면 점프를해야한다
			{
				if(usr1.bankruptcy == 0 && usr2.bankruptcy == 0 && usr3.bankruptcy == 0 ) //모두 파산이 안난경우
				{
					if( player.equals( usr1.getId() ) ) //현재 플레이어가 usr1이였으면
					{
						if( reverse == 0 ) //순서대로면
						{
							player = usr3.getId(); // usr3이 다음 턴이다
						}
						else //순서대로가 아니면
						{
							player = usr2.getId() ;
						}
					}
					else if ( player.equals( usr2.getId() ) ) //현재 플레이어가 usr2이였으면
					{
						if (reverse == 0 ) //순서대로면
						{
							player = usr1.getId(); //usr1이 다음턴이다
						}
						else // 역방향이면
						{
							player = usr3.getId();
						}
					}
					else //현재플레이어가 usr3이였으면
					{
						if (reverse == 0 ) //순서대로면
						{
							player = usr2.getId(); //usr2가 다음턴이다
						}
						else //역방향이면
						{
							player = usr1.getId();
						}
					}
				}
				else if ( usr1.bankruptcy == 1 ) //유저 1 이 파산났으면
				{
					if(player.equals(usr2.getId()))
					{
						player = player ;
					}
					else
					{
						player = player;
					}
				}
				else if (usr2.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
					{
						player = player;
					}
					else
						player = player;
				}
				else if ( usr3.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
					{
						player = player;
					}
					else
						player = player;
				}
			}
			else //4인이면
			{
				if(usr1.bankruptcy == 0 && usr2.bankruptcy == 0 && usr3.bankruptcy == 0 && usr4.bankruptcy == 0 )
				{
					if (player.equals(usr1.getId())) //usr1이였으면
					{
						if ( reverse == 0) //순서대로면
						{
							player = usr3.getId();
						}
						else //역방향이면
						{
							player = usr3.getId();
						}
					}
					else if (player.equals(usr2.getId())) //usr2 였으면
					{
						if ( reverse == 0) //순서대로면
						{
							player = usr4.getId();
						}
						else
						{
							player = usr4.getId();
						}
					}
					else if ( player.equals(usr3.getId()) )
					{
						if(reverse == 0)
						{
							player = usr1.getId();
						}
						else
						{
							player = usr1.getId();
						}
					}
					else // usr 4면
					{
						if(reverse ==0)
						{
							player =usr2.getId();
						}
						else
						{
							player = usr2.getId();
						}
					}
				}
				else if ( usr1.bankruptcy == 1 && usr2.bankruptcy == 1 )
				{
					player = player;
				}
				else if (usr1.bankruptcy == 1 && usr3.bankruptcy == 1 )
				{
					player = player;
				}
				else if (usr1.bankruptcy == 1 && usr4.bankruptcy == 1 )
					player = player;
				else if (usr2.bankruptcy == 1 && usr3.bankruptcy == 1 )
					player = player;
				else if (usr2.bankruptcy == 1 && usr4.bankruptcy == 1 )
					player = player;
				else if ( usr3.bankruptcy == 1 && usr4.bankruptcy == 1 )
					player = player;
				else if ( usr1.bankruptcy == 1 ) //유저 1이파산이면
				{
					if(player.equals(usr2.getId()))
					{
						player = usr4.getId();
					}
					else if (player.equals(usr3.getId()))
					{
						player = usr2.getId();
					}
					else if(player.equals(usr4.getId()))
					{
						player = usr3.getId();
					}
				}
				else if (usr2.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr4.getId();
					else if (player.equals(usr3.getId()))
						player = usr1.getId();
					else if (player.equals(usr4.getId()))
						player = usr3.getId();
				}
				else if (usr3.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr4.getId();
					else if (player.equals(usr2.getId()))
						player = usr1.getId();
					else if ( player.equals(usr4.getId()))
						player = usr2.getId();
				}
				else if (usr4.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr3.getId();
					else if (player.equals(usr2.getId()))
						player = usr1.getId();
					else if (player.equals(usr3.getId()))
						player = usr2.getId();
				}
			}
		}
		else if ( num.equals("c") ) // 카드가 q였으면 순서를 바꿔야한다
		{
			if ( reverse == 0 ) //reverse를 셋팅
			{
				reverse = 1 ;
			}
			else
			{
				reverse = 0;
			}

			if ( number == 2 ) //2인일떄
			{
				if(player.equals( usr1.getId() )) //현재 usr1이였으면
				{
					player = usr2.getId();
				}
				else //usr2이였으면
				{
					player = usr1.getId();
				}
			}
			else if (number == 3 ) //3인일때
			{
				if(usr1.bankruptcy == 0 && usr2.bankruptcy == 0 && usr3.bankruptcy == 0 ) //모두 파산이 아닐때
				{

					if(player.equals( usr1.getId() )) //현재 usr1이였으면
					{
						if(reverse == 0) //정방향이면
						{
							player =usr2.getId();
						}
						else //역방향이면
						{
							player =usr3.getId();
						}
					}
					else if (player.equals(usr2.getId())) //현재 usr2이였으면
					{
						if(reverse ==0 ) //정방향이면
						{
							player = usr3.getId();
						}
						else
						{
							player = usr1.getId();
						}
					}
					else // usr3이였으면
					{
						if(reverse == 0 ) //정방향이면
						{
							player = usr1.getId();
						}
						else
						{
							player =usr2.getId();
						}
					}
				}
				else if ( usr1.bankruptcy == 1 ) //유저1이 파산햇을때
				{
					if(player.equals(usr2.getId()))
						player = usr3.getId();
					else
						player = usr2.getId();
				}
				else if (usr2.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr3.getId();
					else
						player =usr1.getId();
				}
				else if(usr3.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr2.getId();
					else
						player = usr1.getId();
				}
			}
			else if (number == 4 )
			{
				if(usr1.bankruptcy == 0 && usr2.bankruptcy == 0 && usr3.bankruptcy == 0 && usr4.bankruptcy == 0 )
				{
					if(player.equals(usr1.getId() )) //현재 usr1이면
					{
						if (reverse == 0)
						{
							player =usr2.getId();
						}
						else
						{
							player = usr4.getId();
						}
					}
					else if (player.equals(usr2.getId())) //현재 usr2이면
					{
						if (reverse == 0)
						{
							player = usr3.getId();
						}
						else
						{
							player = usr1.getId();
						}
					}
					else if (player.equals(usr3.getId())) //현재 usr3이면
					{
						if (reverse == 0)
						{
							player = usr4.getId();
						}
						else
						{
							player = usr2.getId();
						}
					}
					else //현재 usr4이면
					{
						if( reverse == 0)
						{
							player = usr1.getId();
						}
						else
						{
							player = usr3.getId();
						}
					}
				}
			}
			else if(usr1.bankruptcy == 1 && usr2.bankruptcy == 1 ) //유저 1과 유저2가 파산할때
			{
				if(player.equals(usr3.getId()))
					player = usr4.getId();
				else 
					player = usr3.getId();
			}
			else if (usr1.bankruptcy == 1 && usr3.bankruptcy == 1 )
			{
				if(player.equals(usr2.getId()))
					player = usr4.getId();
				else
					player = usr2.getId();
			}
			else if ( usr1.bankruptcy == 1 && usr4.bankruptcy ==1 )
			{
				if(player.equals(usr2.getId()))
					player = usr3.getId();
				else
					player = usr2.getId();
			}
			else if ( usr2.bankruptcy == 1 & usr3.bankruptcy == 1 )
			{
				if(player.equals(usr1.getId()))
					player = usr4.getId();
				else
					player = usr1.getId();
			}
			else if (usr2.bankruptcy == 1 && usr4.bankruptcy == 1 )
			{
				if(player.equals(usr1.getId()))
					player = usr3.getId();
				else
					player = usr1.getId();
			}
			else if ( usr3.bankruptcy == 1 && usr4.bankruptcy == 1 )
			{
				if(player.equals(usr1.getId()))
					player = usr2.getId();
				else
					player = usr1.getId();
			}
			else if ( usr1.bankruptcy == 1 )// 유저 1이 파산했으면
			{
				if(reverse == 0 ) //순서대로면
				{
					if(player.equals(usr2.getId()))
					{
						player = usr3.getId();
					}
					else if ( player.equals(usr3.getId()))
					{
						player = usr4.getId();
					}
					else if( player.equals(usr4.getId()))
						player = usr2.getId();
				}
				else //역방향
				{
					if(player.equals(usr2.getId()))
						player = usr4.getId();
					else if (player.equals(usr3.getId()))
						player = usr2.getId();
					else if (player.equals(usr4.getId()))
						player = usr3.getId();
				}
			}
			else if (usr2.bankruptcy == 1 )
			{
				if(reverse == 0 ) //순서대로면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr3.getId();
					}
					else if ( player.equals(usr3.getId()))
					{
						player = usr4.getId();
					}
					else if( player.equals(usr4.getId()))
						player = usr1.getId();
				}
				else //역방향
				{
					if(player.equals(usr1.getId()))
						player = usr4.getId();
					else if (player.equals(usr3.getId()))
						player = usr1.getId();
					else if (player.equals(usr4.getId()))
						player = usr3.getId();
				}
			}
			else if ( usr3.bankruptcy == 1 )
			{
				if(reverse == 0 ) //순서대로면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr2.getId();
					}
					else if ( player.equals(usr2.getId()))
					{
						player = usr4.getId();
					}
					else if( player.equals(usr4.getId()))
						player = usr1.getId();
				}
				else //역방향
				{
					if(player.equals(usr1.getId()))
						player = usr4.getId();
					else if (player.equals(usr2.getId()))
						player = usr1.getId();
					else if (player.equals(usr4.getId()))
						player = usr2.getId();
				}
			}
			else if ( usr4.bankruptcy == 1 )
			{
				if(reverse == 0 ) //순서대로면
				{
					if(player.equals(usr1.getId()))
					{
						player = usr2.getId();
					}
					else if ( player.equals(usr2.getId()))
					{
						player = usr3.getId();
					}
					else if( player.equals(usr3.getId()))
						player = usr1.getId();
				}
				else //역방향
				{
					if(player.equals(usr1.getId()))
						player = usr3.getId();
					else if (player.equals(usr2.getId()))
						player = usr1.getId();
					else if (player.equals(usr3.getId()))
						player = usr2.getId();
				}
			}
		}
		else if ( num.equals ( "d") ) // 카드가 k이였으면
		{
			player = player ; //player는 그대로이다;
		}
		else // J Q K 같은 특수카드가 아니라면
		{
			//System.out.println("여기와?");
			if ( number  == 2 ) //2인일때
			{
				if ( player.equals(usr1.getId())) //현재 usr1이였으면
				{
					player =usr2.getId();
				}
				else
				{
					player =usr1.getId();
				}
			}
			else if ( number == 3 ) //3인일때
			{
				if(usr1.bankruptcy == 0 && usr2.bankruptcy == 0 && usr3.bankruptcy == 0 )
				{
					if( player.equals(usr1.getId())) //usr1이였으면
					{
						if (reverse == 0 )
						{
							player = usr2.getId();
						}
						else
						{
							player = usr3.getId();
						}
					}
					else if ( player.equals(usr2.getId())) //현재 usr2였으면
					{
						if(reverse == 0)
						{
							player = usr3.getId();
						}
						else
						{
							player = usr1.getId();
						}
					}
					else //현재 usr3 이였으면
					{
						if( reverse == 0)
						{
							player =usr1.getId();
						}
						else
						{
							player = usr2.getId();
						}
					}
				}
				else if (usr1.bankruptcy == 1 )
				{
					if(player.equals(usr2.getId()))
						player = usr3.getId();
					else 
						player = usr2.getId();
				}
				else if (usr2.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr3.getId();
					else
						player = usr1.getId();
				}
				else if ( usr3.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr2.getId();
					else
						player = usr1.getId();
				}
			}
			else // 4인일떄
			{
				if(usr1.bankruptcy == 0 && usr2.bankruptcy == 0 && usr3.bankruptcy == 0 && usr4.bankruptcy == 0 )
				{
					if ( player.equals(usr1.getId())) //usr1이면
					{
						if (reverse == 0 )
						{
							player =usr2.getId();
						}
						else
						{
							player =usr4.getId();
						}
					}
					else if ( player.equals(usr2.getId()))
					{
						if (reverse == 0)
						{
							player =usr3.getId();
						}
						else
						{
							player =usr1.getId();
						}
					}
					else if ( player.equals(usr3.getId()))
					{
						if(reverse == 0 )
						{
							player = usr4.getId();
						}
						else
						{
							player = usr2.getId();
						}
					}
					else //usr4일떄
					{
						if(reverse == 0)
						{
							player = usr1.getId();
						}
						else
						{
							player = usr3.getId();
						}
					}
				}
				else if (usr1.bankruptcy == 1 && usr2.bankruptcy == 1 )
				{
					if(player.equals(usr3.getId()))
						player = usr4.getId();
					else
						player = usr3.getId();
				}
				else if ( usr1.bankruptcy == 1 && usr3.bankruptcy == 1 )
				{
					if( player.equals(usr2.getId()))
						player = usr4.getId();
					else
						player = usr2.getId();
				}
				else if(usr1.bankruptcy == 1 && usr4.bankruptcy == 1 )
				{
					if(player.equals(usr2.getId()))
						player =usr3.getId();
					else
						player = usr2.getId();
				}
				else if (usr2.bankruptcy == 1 && usr3.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr4.getId();
					else
						player = usr1.getId();
				}
				else if ( usr2.bankruptcy == 1 && usr4.bankruptcy == 1 )
				{
					if(player.equals(usr1.getId()))
						player = usr3.getId();
					else
						player = usr1.getId();
				}
				else if ( usr3.bankruptcy == 1 && usr4.bankruptcy ==1 )
				{
					if(player.equals(usr1.getId()))
						player = usr2.getId();
					else
						player = usr1.getId();
				}
				else if ( usr1.bankruptcy == 1 )// 유저 1이 파산했으면
				{
					if(reverse == 0 ) //순서대로면
					{
						if(player.equals(usr2.getId()))
						{
							player = usr3.getId();
						}
						else if ( player.equals(usr3.getId()))
						{
							player = usr4.getId();
						}
						else if( player.equals(usr4.getId()))
							player = usr2.getId();
					}
					else //역방향
					{
						if(player.equals(usr2.getId()))
							player = usr4.getId();
						else if (player.equals(usr3.getId()))
							player = usr2.getId();
						else if (player.equals(usr4.getId()))
							player = usr3.getId();
					}
				}
				else if (usr2.bankruptcy == 1 )
				{
					if(reverse == 0 ) //순서대로면
					{
						if(player.equals(usr1.getId()))
						{
							player = usr3.getId();
						}
						else if ( player.equals(usr3.getId()))
						{
							player = usr4.getId();
						}
						else if( player.equals(usr4.getId()))
							player = usr1.getId();
					}
					else //역방향
					{
						if(player.equals(usr1.getId()))
							player = usr4.getId();
						else if (player.equals(usr3.getId()))
							player = usr1.getId();
						else if (player.equals(usr4.getId()))
							player = usr3.getId();
					}
				}
				else if ( usr3.bankruptcy == 1 )
				{
					if(reverse == 0 ) //순서대로면
					{
						if(player.equals(usr1.getId()))
						{
							player = usr2.getId();
						}
						else if ( player.equals(usr2.getId()))
						{
							player = usr4.getId();
						}
						else if( player.equals(usr4.getId()))
							player = usr1.getId();
					}
					else //역방향
					{
						if(player.equals(usr1.getId()))
							player = usr4.getId();
						else if (player.equals(usr2.getId()))
							player = usr1.getId();
						else if (player.equals(usr4.getId()))
							player = usr2.getId();
					}
				}
				else if ( usr4.bankruptcy == 1 )
				{
					if(reverse == 0 ) //순서대로면
					{
						if(player.equals(usr1.getId()))
						{
							player = usr2.getId();
						}
						else if ( player.equals(usr2.getId()))
						{
							player = usr3.getId();
						}
						else if( player.equals(usr3.getId()))
							player = usr1.getId();
					}
					else //역방향
					{
						if(player.equals(usr1.getId()))
							player = usr3.getId();
						else if (player.equals(usr2.getId()))
							player = usr1.getId();
						else if (player.equals(usr3.getId()))
							player = usr2.getId();
					}
				}
			}
			
		}			
				
	} // 순서 구하는것 끝


	


	


}