import java.awt.*;
import java.awt.image.ImageProducer;
import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.color.*;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.TextArea;
import java.net.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.DefaultCaret;

import java.util.*;
import java.net.*;
import java.lang.Thread;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class MainWindow extends JFrame implements Runnable{
	public static Image curImg;
	static int command=0;
	public static GPanel gp;
	public static GPanel gpanel[];
	public static URL gpnURL[], flagURL;
	public static GPanel flagImg[];
	
        
	JButton createRoom;
	public static JLabel topLabel;	
	
	public static JPanel panel[];
	public static JLabel label;
	
	public static JLabel user[];
	JPanel superPanel, roomPanel;
	int btnstatus=0, btn2status=0,btn3status=0,btn4status=0;
	JTextField tfUsername, tfUsername2, tfUsername3, tfUsername4;
	JPasswordField pfPassword, pfPassword2, pfPassword3, pfPassword4, pfRoom;
	public static JTextArea game_status=null;	
	JButton btnStart;	
	public static JButton btnGameStart;
	public static int user_count=0;	
	  
	DataInputStream read;    
	DataOutputStream write;
	BufferedReader br;
	byte arr[];
	
	Lock lock;
	Socket socket;
	public static String s_msg;		
	public static Player pl1, pl2, pl3, pl4;	
	public static Card card;		
	public static GameManager game;
	
	public static String curr_card;	
	public static String curr_player;	
	public static String curr_shape;
	
	ImageIcon bgIcon;	
	ImageIcon left_icon;	
	public static JButton left_image;	
	ImageIcon right_icon;	
	public static JButton right_image;	
	
	ImageIcon shape_icon;	
	public static JButton shape_image;	
	
	
	
	public static JLabel lbShape;
	public static int retGivPhone=1;
	public static URL imageURL,firstURL,cardURL, shapeURL,bgimageURL;
	
	
	public static String eachPlayer[];
	public static Player player[];
	public static String room_name;	
	public static String endGame = "09\0";	
	
	public static int gameFlag=-100;	

	
	
	public MainWindow(Socket soc, byte Arr[],Lock lock)
	{
		socket = soc;
		eachPlayer = new String[4];
		player = new Player[4];
		arr=new byte[100];
		
		this.lock = lock;
		
		try
		{
			read = new DataInputStream(socket.getInputStream()); 
			write = new DataOutputStream(socket.getOutputStream());
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}


	

	public MainWindow(){
		super();
		setTitle("HYBRID ONE CARD");
		Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
		int screenwidth = (int)screen.getWidth();
		int screenheight = (int)screen.getHeight()-50;
		setSize(screenwidth,screenheight);
		JScrollPane scrollPane;
		
		bgimageURL = getClass().getClassLoader().getResource("bgimg.png");
		bgIcon = new ImageIcon(bgimageURL);
		
		JPanel background = new JPanel(){
			public void paintComponent(Graphics g){
				g.drawImage(bgIcon.getImage(), 0, 0,null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		
		background.setLayout(null);
		
		
		
		superPanel = new JPanel();

		
		superPanel.setLayout(null);
	
	
		panel = new JPanel[4];
		user = new JLabel[4];
		gpanel = new GPanel[4];
		gpnURL = new URL[4];
		flagImg = new GPanel[4];
		/*gp = new GPanel(imageURL);
		gp.setLayout(null);
		gp.setBounds(0, 0, 300, 300);*/
		Image a;

		flagURL = getClass().getClassLoader().getResource("flag.png");
		for(int j=0;j<4;j++){
			panel[j] = new JPanel();
			panel[j].setLayout(null);
			user[j]= new JLabel();
			gpnURL[j] = getClass().getClassLoader().getResource("panel"+j+".png");
			
			
			gpanel[j] = new GPanel(gpnURL[j]);
			
			flagImg[j] = new GPanel(flagURL);
			gpanel[j].setLayout(null);
			curImg = null;
		
		}
		
	
		roomPanel = new JPanel();
		roomPanel.setLayout(null);
		
		
		
		
		topLabel = new JLabel("NAME :  ");
		Font fff = new Font("Calibri", 0, screenwidth/30);
		topLabel.setFont(fff);
		topLabel.setBounds(this.getX()+10, this.getY()+10, 300, 120);
		topLabel.setVisible(false);
		
		
		
		
		Font f = new Font("Calibri", 0, screenwidth/25);
		Font f_user = new Font("BareunDotum", 0, 25);
		
		gpanel[0].setBounds(this.getX()+15,(screenheight)/3,150,165);
		
		gpanel[0].setBorder(null);
		gpanel[0].setBackground(null);
		user[1] = new JLabel("");
		
		user[0].setFont(f_user);
		user[0].setVisible(true);
		user[0].setBounds(gpanel[0].getX(), gpanel[0].getY()-65, 165, 100);
		flagImg[0].setBounds(gpanel[0].getX()+150,gpanel[0].getY() , 87, 100);
		
		/*panel[0].add(user[0]);
		
		
		panel[0].setBackground(Color.lightGray);
		panel[0].setBorder(new LineBorder(Color.GRAY));
		panel[0].setBounds(this.getX()+15,(screenheight)/3,(screenwidth)/6,(screenheight)/5);
		label.setBounds(panel[0].getWidth()/4+15, panel[0].getHeight()/5, 100, 100);
		user[0].setBounds(panel[0].getWidth()/4, panel[0].getHeight()/2, 100, 100);
		label.setVisible(true);
		panel[0].add(user[0]);*/
		
		

		
		
		
		gpanel[1].setBounds((screenwidth)/2-(screenwidth/12),(screenheight)-(screenheight/3)+40,150,165);
		gpanel[1].setBorder(null);
		flagImg[1].setBounds(gpanel[1].getX()+150,gpanel[1].getY() , 87, 100);
		
		
	
		
		user[1] = new JLabel("");
		user[1].setFont(f_user);
		
		user[1].setBounds(gpanel[1].getX(), gpanel[1].getY()-65, 165, 100);
		/*user[1].setVisible(false);
		panel[1].add(user[1]);
		
		panel[1].setBackground(Color.LIGHT_GRAY);
		panel[1].setBorder(new LineBorder(Color.GRAY));
		
		panel[1].setBounds((screenwidth)/2-(screenwidth/12),(screenheight)-(screenheight/4),(screenwidth)/6,(screenheight)/5);
		label2.setBounds(panel[1].getWidth()/4+10, panel[1].getHeight()/5, 100, 100);
		user[1].setBounds(panel[0].getWidth()/4, panel[0].getHeight()/2, 100, 100);*/
		
		
		/*JLabel label3 = new JLabel("3P");
		label3.setFont(f);
		panel[2].add(label3);
	
		
		user[2] = new JLabel("");
		user[2].setFont(f_user);
		
		user[2].setBounds(50, 60, 100, 100);
		user[2].setVisible(false);
		panel[2].add(user[2]);
		
		panel[2].setBackground(Color.LIGHT_GRAY);
		panel[2].setBorder(new LineBorder(Color.GRAY));
		panel[2].setBounds((screenwidth)-(screenwidth/5)+15,(screenheight)/3,(screenwidth)/6,(screenheight)/5);
		label3.setBounds(panel[2].getWidth()/4+10, panel[2].getHeight()/5, 100, 100);
		user[2].setBounds(panel[0].getWidth()/4, panel[0].getHeight()/2, 100, 100);*/
		gpanel[2].setBounds((screenwidth)-(screenwidth/6),(screenheight)/3,150,165);
		gpanel[2].setBorder(null);
		flagImg[2].setBounds(gpanel[2].getX()-87,gpanel[2].getY() , 87, 100);
		user[2] = new JLabel("");
		user[2].setFont(f_user);
		user[2].setBounds(gpanel[2].getX(), gpanel[2].getY()-65, 165, 100);
		
		
		
		/*JLabel label4 = new JLabel("4P");
		label4.setFont(f);
		panel[3].add(label4);
	
		
		user[3] = new JLabel("");
		user[3].setFont(f_user);
		
	
		user[3].setVisible(false);
		panel[3].add(user[3]);
		
		panel[3].setBackground(Color.LIGHT_GRAY);
		panel[3].setBorder(new LineBorder(Color.GRAY));
		panel[3].setBounds((screenwidth)/2-(screenwidth/12),this.getY(),(screenwidth)/6,(screenheight)/5);
		label4.setBounds(panel[3].getWidth()/4+10, panel[3].getHeight()/5, 100, 100);
		user[3].setBounds(panel[0].getWidth()/4, panel[0].getHeight()/2, 100, 100);*/
		gpanel[3].setBounds((screenwidth)/2-(screenwidth/12), 0,150,165);
		gpanel[3].setBorder(null);
		flagImg[3].setBounds(gpanel[3].getX()+150,gpanel[3].getY() , 87, 100);
		user[3] = new JLabel("");
		user[3].setFont(f_user);
		user[3].setBounds(gpanel[3].getX()+150, gpanel[3].getY()+100, 165, 100);
		
		imageURL = getClass().getClassLoader().getResource("roomicon.png");
		
		ImageIcon room_icon = new ImageIcon(imageURL);
		btnStart = new JButton(room_icon);
		btnStart.setEnabled(true);
		
		btnStart.setBounds((screenwidth)/2-(screenwidth/12),(screenheight)/3+60,160,93);
	
		
		
		
		
		
	
		
			imageURL = getClass().getClassLoader().getResource("back.png");
			
			ImageIcon left_icon = new ImageIcon(imageURL);
			left_image = new JButton(left_icon);
			
			
			left_image.setBounds((gpanel[1].getX()-gpanel[0].getX()+gpanel[0].WIDTH)-100,(screenheight)/3-45,200,250);
			left_image.setBackground(Color.lightGray);
			
			
			
			
			shapeURL = getClass().getClassLoader().getResource("clover.png");
			ImageIcon shape_icon = new ImageIcon(shapeURL);
			shape_image = new JButton(shape_icon);
			
			shape_image.setVisible(false);
			///
			
			imageURL = getClass().getClassLoader().getResource("back.png");
			ImageIcon right_icon = new ImageIcon(imageURL);
			
			right_image = new JButton(right_icon);
			
			right_image.setBounds(left_image.getX()+225,(screenheight)/3-45,200,250);
			right_image.setBackground(Color.lightGray);
		
			///

			Font ff = new Font("Calibri", 0, screenwidth/35);
			lbShape = new JLabel("Diamond");
		
		
		
			shape_image.setBounds(right_image.getX()+210, right_image.getY()-113, 97, 98);
			
		
		
		
		JLabel roomPassword = new JLabel("�� ��й�ȣ : ");
		roomPanel.add(roomPassword);
		roomPassword.setBounds(20,70,100,50);
		pfRoom = new JPasswordField(20);
		roomPanel.add(pfRoom);
		pfRoom.setBounds(100,85,150,20);
		
		
		createRoom = new JButton("�� ����");
		roomPanel.add(createRoom);
		createRoom.setBounds(125,130,100,30);
		roomPanel.setBorder(new LineBorder(Color.GRAY));
		roomPanel.setBounds((screenwidth)/4+(screenwidth/8),(screenheight)/3,(screenwidth)/4,(screenheight)/4);
		
		
		btnGameStart = new JButton("Game Start!");
		btnGameStart.setEnabled(false);
		btnGameStart.setBounds((screenwidth)/2-(screenwidth/12),(screenheight)/3+60,160,93);
		
		
		JLabel gameLabel = new JLabel("[ Game Status ]");
		game_status = new JTextArea();	
		 
		game_status.setBounds(this.getX()+20,(screenheight)-(screenheight/3),screenwidth/3,screenheight/4);
		gameLabel.setBounds(this.getX()+40, game_status.getY()-30, 100, 20);
		
		game_status.setBorder(new LineBorder(Color.GRAY));
		game_status.setEditable(false);
		game_status.setFocusable(true);
		
		DefaultCaret caret = (DefaultCaret)game_status.getCaret();  
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		

		imageURL = getClass().getClassLoader().getResource("spade.png");
		GPanel shpPanel = new GPanel(imageURL);
		shpPanel.setBounds(gpanel[3].getX()-100,gpanel[3].HEIGHT,100,100);
		
		
		for(int j=0;j<4;j++){
			
			background.add(gpanel[j]);
			background.add(user[j]);

			background.add(flagImg[j]);
			flagImg[j].setVisible(false);
		}
		
		background.add(btnStart);
		background.add(roomPanel);
		roomPanel.setVisible(false);
		background.add(btnGameStart);
		background.add(game_status);
		background.add(gameLabel);
		
		background.add(left_image);
		background.add(right_image);
		background.add(topLabel);
		background.add(shape_image);
		
	
		
		left_image.setVisible(false);
		right_image.setVisible(false);
		
		
		scrollPane = new JScrollPane(background);
		setContentPane(scrollPane);
		setVisible(true);
		add(superPanel);
		
		setVisible(true);

		MouseClick mc = new MouseClick();
		btnStart.addMouseListener(mc);
		createRoom.addMouseListener(mc);
		btnGameStart.addMouseListener(mc);
		left_image.addMouseListener(null);
		left_image.addMouseListener(null);
		System.out.println("ddd5");
		
	}
	
	public static void main(String args[]){
		MainWindow frame = new MainWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	
		
	}
	class MouseClick extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			Object obj = e.getSource();
			
			if(obj==btnStart){
				btnStartClicked(e);
			}else if(obj==createRoom){
				btnRoomClicked(e);
				System.out.println("ddd4");
			}else if(obj==btnGameStart){
				if(user_count>1){
				btnGameStartClicked(e);		
				}
			}
			
		}
		public void btnGameStartClicked(MouseEvent e){
			int j;
			int k;
			
			card = new Card();
			System.out.println("btn1");
			try{
			switch(user_count){
			case 1:
				System.out.println("btn");
				break;
			case 2:
				j = 0;
				k =0;
				for(j=0; j<4; j++)
				{
					if(player[j] != null)
					{
						pl1 = player[j];
						break;
					}
				}
				
				for(k= j+1; k<4; k++)
				{
					if(player[k] != null)
					{
						pl2 = player[k];
						break;
					}
				}

				game = new GameManager(socket, card, pl1,pl2); 
				System.out.println("btn2");
				break;
			case 3: 
				
				j = 0;
				k =0;
				
				for(j=0; j<4; j++)
				{
					if(player[j] != null)
					{
						pl1 = player[j];
						break;
					}
				}
				
				
				for(k= j+1; k<4; k++)
				{
					if(player[k] != null)
					{
						pl2 = player[k];
						break;
					}
				}
				
				for(int i=k+1; i<4; i++)
				{
					if(player[i] != null)
					{
						pl3 = player[i];
						break;
					}
				}
				
				/*
				for(int t=0;t<4;t++){
					
						pl1 ;
					if(player[t]!=null){
						switch(t){
						case 0:
							pl1 = player[t];
							break;
						case 1:
							pl2 = player[t];
							break;
						case 2: 
							pl3 = player[];
							break;
						}
					}
				}*/
				game = new GameManager(socket, card, pl1,pl2, pl3);
				System.out.println("btn3");
				break;
			case 4:
				game = new GameManager(socket, card, player[0],player[1],player[2],player[3]);
				System.out.println("btn4");
				break;
			default:
					break;
			}
			}catch(Exception eg){
				eg.printStackTrace();
			}
			game.Game_Start();		
			gameFlag = 100;
			curr_card = game.getCard();
			curr_player = game.getPlayer();
			curr_shape = game.getShape();
			
			game_status.setText("*** current player. ***\n");
			game_status.append(" "+curr_player + ".\n");
			game_status.append(" : "+ curr_shape+" .\n");
			
			topLabel.setText("Next : "+curr_player);
			btnGameStart.setVisible(false);
			right_image.setVisible(true);
			left_image.setVisible(true);
			
			firstURL = getClass().getClassLoader().getResource(curr_card+".png");
			ImageIcon dicon = new ImageIcon(firstURL);
			right_image.setIcon(dicon);
			lbShape.setVisible(true);
			shape_image.setVisible(true);
			
			if(curr_shape.equals("h")){
				
				lbShape.setText("Shape : HEART");
				lbShape.setBackground(Color.RED);
				shapeURL = getClass().getClassLoader().getResource("heart.png");
				shape_icon = new ImageIcon(shapeURL);
				shape_image.setIcon(shape_icon);
			}else if(curr_shape.equals("c")){
				
				lbShape.setText("Shape : CLOVER");
				shapeURL = getClass().getClassLoader().getResource("clover.png");
				shape_icon = new ImageIcon(shapeURL);
				shape_image.setIcon(shape_icon);
			}else if(curr_shape.equals("s")){
				
				lbShape.setText("Shape : SPADE");
				shapeURL = getClass().getClassLoader().getResource("spade.png");
				shape_icon = new ImageIcon(shapeURL);
				shape_image.setIcon(shape_icon);
			}else if(curr_shape.equals("d")){
				
				lbShape.setText("Shape : DIAMOND");
				shapeURL = getClass().getClassLoader().getResource("diamond.png");
				shape_icon = new ImageIcon(shapeURL);
				shape_image.setIcon(shape_icon);
			}else if(curr_shape.equals("t")){
				
				lbShape.setText("Shape : BLACK J");
				shapeURL = getClass().getClassLoader().getResource("blackJoker.png");
				shape_icon = new ImageIcon(shapeURL);
				shape_image.setIcon(shape_icon);
			}else if(curr_shape.equals("u")){
				
				lbShape.setText("Shape : COLOR J");
				shapeURL = getClass().getClassLoader().getResource("colorJoker.png");
				shape_icon = new ImageIcon(shapeURL);
				shape_image.setIcon(shape_icon);
			}
			for(int i=0;i<4;i++){
				
				System.out.println(eachPlayer);
				if(curr_player.equals(eachPlayer[i])){
					System.out.println("i : "+i);
					
					flagImg[i].setVisible(true);
				}
				else{
					
					flagImg[i].setVisible(false);
				}
			}
			
		}
		public void btnStartClicked(MouseEvent e){
			btnStart.setVisible(false);
			roomPanel.setVisible(true);

			
			
		}
		public void btnRoomClicked(MouseEvent e){
			if(pfRoom.getText().equals("")){
				game_status.append("\n");
				return;
			}
			roomPanel.setVisible(false);
			try
			{
				try{
					socket = new Socket("127.0.0.1", 9590 );
					
				}catch(IOException ie){
					game_status.append("error.\n");
					
					btnStart.setVisible(true);
					btnStart.setEnabled(true);
					return;
					
				}
				br = new BufferedReader(new InputStreamReader(System.in));
				arr = new byte [100];
				String login_str = "00"+pfRoom.getText().trim()+"|\0";
				System.out.println(login_str);
				arr = login_str.getBytes();
				lock = new ReentrantLock();
				
				
				new Send(socket,  arr, br, lock);
				
			
				
				Thread thread2 = new Thread(new MainWindow(socket, arr, lock));
				thread2.start();
				
			
			}
			catch(Exception ee)
			{
				ee.printStackTrace();
			}
		}
		
	}
	
	public void eachGameTurn(){
		
		curr_card = game.getCard();
		curr_player = game.getPlayer();
		curr_shape = game.getShape();
		System.out.println("curr_player: "+curr_player);

		cardURL = getClass().getClassLoader().getResource(curr_card+".png");
		ImageIcon dicon = new ImageIcon(cardURL);
		right_image.setIcon(dicon);
		game_status.setText(" "+curr_shape+".\n");
		game_status.append(" player "+curr_player+".\n");
		topLabel.setText("Next : "+curr_player);
		if(curr_shape.equals("h")){
			
			lbShape.setText("Shape : HEART");
			lbShape.setBackground(Color.RED);
			shapeURL = getClass().getClassLoader().getResource("heart.png");
			shape_icon = new ImageIcon(shapeURL);
			shape_image.setIcon(shape_icon);
		}else if(curr_shape.equals("c")){
			
			lbShape.setText("Shape : CLOVER");
			shapeURL = getClass().getClassLoader().getResource("clover.png");
			shape_icon = new ImageIcon(shapeURL);
			shape_image.setIcon(shape_icon);
		}else if(curr_shape.equals("s")){
			
			lbShape.setText("Shape : SPADE");
			shapeURL = getClass().getClassLoader().getResource("spade.png");
			shape_icon = new ImageIcon(shapeURL);
			shape_image.setIcon(shape_icon);
		}else if(curr_shape.equals("d")){
			
			lbShape.setText("Shape : DIAMOND");
			shapeURL = getClass().getClassLoader().getResource("diamond.png");
			shape_icon = new ImageIcon(shapeURL);
			shape_image.setIcon(shape_icon);
		}else if(curr_shape.equals("t")){
			
			lbShape.setText("Shape : BLACK J");
			shapeURL = getClass().getClassLoader().getResource("blackJoker.png");
			shape_icon = new ImageIcon(shapeURL);
			shape_image.setIcon(shape_icon);
		}else if(curr_shape.equals("u")){
			
			lbShape.setText("Shape : COLOR J");
			shapeURL = getClass().getClassLoader().getResource("colorJoker.png");
			shape_icon = new ImageIcon(shapeURL);
			shape_image.setIcon(shape_icon);
		}
		for(int i=0;i<4;i++){
			
				if(curr_player.equals(eachPlayer[i])){
					System.out.println("i : "+i);
					flagImg[i].setVisible(true);
					
				}
				else{
					flagImg[i].setVisible(false);
					
				}
		}
	}
	
	public void run()
	{
		try
		{
				while(true)
				{
					arr = new byte[200];
				int len = read.read(arr,0,arr.length);
				
				String s_msg = new String(arr,0, len);
				s_msg = s_msg.trim();
				
				String com = s_msg;
				
				
				System.out.println("Recv : "+s_msg);
				String status = s_msg.substring(0,2);	
				
				
				String user_name="";
				if(status.equals("10")){	
					room_name = s_msg.substring(2,5);
					
					
					game_status.setText(".\n");
					game_status.append("�� �̸� : "+room_name);
					btnGameStart.setVisible(true);
					topLabel.setVisible(true);
					topLabel.setText("Name : "+room_name);
				}else if(status.equals("13")){
					
					int check = game.Penalty();
					if(check != 0) 
					{
						game.type = 2;
						game.msg = com;
					}
					else  
					{
				

						String winner="";
						retGivPhone = game.Give_Phone(com);
						if(retGivPhone==0){
							gameFlag = -100;
							for(int i=0;i<4;i++){
								if(eachPlayer[i]!=null){
									 System.out.println("cyan");
									
										flagImg[i].setVisible(false);
								}
								if(game.usr1.getNumber()==0){
									winner=game.usr1.getId();
								}else if(game.usr2.getNumber()==0){
									winner=game.usr2.getId();
								}else if(game.usr3.getNumber()==0){
									winner=game.usr3.getId();
								}else if(game.usr4.getNumber()==0){
									winner=game.usr4.getId();
								}
							}
							JOptionPane.showMessageDialog(null, "Winner! "+winner+"!!", "Game Over",
									 JOptionPane.INFORMATION_MESSAGE);
							game_status.setText("Game over.\n");
							arr = endGame.getBytes();	
							new Send(socket,  arr, br, lock);
							shape_image.setVisible(false);
							
							 topLabel.setText("Name : "+room_name);
							
							 
							 
								btnGameStart.setVisible(true);
								if(user_count<2)
								btnGameStart.setEnabled(false);
								right_image.setVisible(false);
								left_image.setVisible(false);
								lbShape.setVisible(false);
								game_status.setText(".\n");
								for(int k=0;k<4;k++){
									if(eachPlayer[k]==null){
										player[k]= null;
									}
								}
								game=null;
								topLabel.setText("ROOM : "+room_name);
								
								
								
								
							
						}else{
							eachGameTurn();
						}
					}
					
					
					
					
				}else if(status.equals("50"))
				{
					String copy = com.substring(2,com.length()); 
					StringTokenizer st = new StringTokenizer(copy, "|"); 
					String usr = st.nextToken(); 
					

					if(game.type == 0)
					{
						game.Take_Phone(game.msg);
						game.type=1;
						
					}
					else if(game.type == 1 )
					{
						game.Turn();
						eachGameTurn();
					}
					else if(game.type ==2 )
					{
						String winner="";
						retGivPhone = game.Give_Phone(game.msg);
						if(retGivPhone==0){
							gameFlag = -100;
							for(int i=0;i<4;i++){
								if(eachPlayer[i]!=null){
									 System.out.println("cyan");
									
									flagImg[i].setVisible(false);
								}
								if(game.usr1.getNumber()==0){
									winner=game.usr1.getId();
								}else if(game.usr2.getNumber()==0){
									winner=game.usr2.getId();
								}else if(game.usr3.getNumber()==0){
									winner=game.usr3.getId();
								}else if(game.usr4.getNumber()==0){
									winner=game.usr4.getId();
								}
							}
							JOptionPane.showMessageDialog(null, "Winner!!  "+winner+"!!", "Game Over",
									 JOptionPane.INFORMATION_MESSAGE);
							game_status.setText("Game over.\n");
							shape_image.setVisible(false);
							arr = endGame.getBytes();	
							new Send(socket,  arr, br, lock);
							
							 topLabel.setText("Name : "+room_name);
							
							 
							 
								btnGameStart.setVisible(true);
								if(user_count<2)
									btnGameStart.setEnabled(false);
								right_image.setVisible(false);
								left_image.setVisible(false);
								lbShape.setVisible(false);
								game_status.setText(".\n");
								game = null;
								topLabel.setText("ROOM : "+room_name);
								
								
								
							
						}else{
							eachGameTurn();
						}
					}
					
				}
				else if(status.equals("14")){
					game_status.setText(curr_player+" is current player.\n");
					
					int check = game.Penalty();
					if(check == 0) 
					{
						game.Take_Phone(com);
						game.type = 1;
					}
					else 
					{
						game.type = 0;
						game.msg = com;
					}
					

					
					
					
					
					
				
				}
				
				else if(status.equals("14")){
					game_status.setText(curr_player+" is current player.\n");
					game.Take_Phone(com);
					eachGameTurn();
				
				}
				else if(status.equals("17")){
					
					System.out.println("��ī��!");	
					game_status.setText(curr_player+" is current player.\n");
					game.One_Card(com);
					eachGameTurn();
				}else if(status.equals("31")){
					
					
					 String closed_user = s_msg.substring(2,6);
					 System.out.println(closed_user+" closed the game!");
					 
					 int validUser=0, removedUser=0;
					 for(int i=0;i<4;i++){
						 if(eachPlayer[i]!=null){
							if(eachPlayer[i].equals(closed_user)){
								
								
								gpnURL[i] = getClass().getClassLoader().getResource("panel"+i+".png");
								gpanel[i].imageUpdate(gpnURL[i], ALLBITS, 0, 0, 0, 0);
								user[i].setVisible(false);
								eachPlayer[i]=null;
								
								game_status.append(closed_user+" closed the game.\n");
								user[i].setText("");
								removedUser = i;
								flagImg[i].setVisible(false);
								System.out.println(com);
								 if(gameFlag!=-100)	
									 game.Bankruptcy_Phone(com);
							}
						 }
						 
					}
					 user_count--;	
					 System.out.println(user_count);
					 if(user_count<2){
						 game_status.setText("name!.\n");
						 gameFlag = -100;
						 topLabel.setText("Name : "+room_name);
						
							btnGameStart.setVisible(true);
							btnGameStart.setEnabled(false);
							right_image.setVisible(false);
							left_image.setVisible(false);
							lbShape.setVisible(false);
							shape_image.setVisible(false);
							
							arr = endGame.getBytes();	
							game = null;
							for(int k=0;k<4;k++){
								if(eachPlayer[k]==null){
									player[k]= null;
								}
								flagImg[k].setVisible(false);
							}
							new Send(socket,  arr, br, lock);
					 }else{
						
						
						
							curr_player = game.getPlayer();
							System.out.println("curr_player: "+curr_player);
							game_status.append("current player: "+curr_player+".\n");
							topLabel.setText("Next : "+curr_player);
							for(int i=0;i<4;i++){
								
								System.out.println(eachPlayer);
								if(curr_player.equals(eachPlayer[i])){
									System.out.println("i : "+i);
									
									flagImg[i].setVisible(true);
								}
							}
						 System.out.println("next : "+curr_player);
						 
					 }
				}
				else if(status.equals("21")){
					
					 String closed_user = s_msg.substring(2,6);
					 System.out.println(closed_user+" closed the game!");
					 
					 int validUser=0, removedUser=0;
					 for(int i=0;i<4;i++){
						 if(eachPlayer[i]!=null){
							if(eachPlayer[i].equals(closed_user)){
								
								
								
								gpnURL[i] = getClass().getClassLoader().getResource("panel"+i+".png");
								gpanel[i].imageUpdate(gpnURL[i], ALLBITS, 0, 0, 0, 0);
								eachPlayer[i]=null;
								
								game_status.append(closed_user+" closed the game.\n");
								user[i].setText("");
								removedUser = i;

								flagImg[i].setVisible(false);
								System.out.println("flagimg test!");
								
								if(gameFlag!=-100)	
								 game.Bankruptcy_Phone(com);
								else
									player[i]=null;
								
							}else{
								
							}
						 }
						 
					}
					 user_count--;	
					 System.out.println(user_count);
					 if(gameFlag != -100) 
					 {
						 game.Exit_Phone(s_msg);
						 
						 if(user_count<2){
							 game_status.setText(".\n");
							 topLabel.setText("Name : "+room_name);
							 gameFlag = -100;
							
								btnGameStart.setVisible(true);
								btnGameStart.setEnabled(false);
								right_image.setVisible(false);
								left_image.setVisible(false);
								lbShape.setVisible(false);
								shape_image.setVisible(false);
								arr = endGame.getBytes();	
								for(int k=0;k<4;k++){
									if(eachPlayer[k]==null){
										player[k]= null;
									}
									flagImg[k].setVisible(false);
								}
								game = null;
								new Send(socket,  arr, br, lock);
						 }else{
							
							
							
								curr_player = game.getPlayer();
								System.out.println("curr_player: "+curr_player);
								game_status.append("current player :  "+curr_player+".\n");
								topLabel.setText("Next : "+curr_player);
								for(int i=0;i<4;i++){
									
									System.out.println(eachPlayer);
									if(curr_player.equals(eachPlayer[i])){
										System.out.println("i : "+i);
										
										flagImg[i].setVisible(true);
									}
								}
							 System.out.println("next : "+curr_player);
							 
						 }
					 }
					
				}
				else if(status.equals("24")){
					

					String copy = s_msg.substring(2,s_msg.length()); 
					StringTokenizer st = new StringTokenizer(copy, "|"); 
					user_name = st.nextToken();
					for(int i=0;i<4;i++){
						if(player[i]==null){
							System.out.println("user"+i);
							user[i].setText("<html><font color='white'>"+user_name+"</font></html>");
							user[i].setVisible(true);
							

							gpnURL[i] = getClass().getClassLoader().getResource("panel"+i+"_i.png");
							
							
							gpanel[i].imageUpdate(gpnURL[i], ALLBITS, 0, 0, 0, 0);
						
							game_status.append("\n"+user_name+".\n");
							System.out.println(user_name);
							player[i] = new Player(user_name);
							eachPlayer[i] = user_name;
							break;
						}
					}
					if(user_count>=2){
						
						btnGameStart.setEnabled(true);
					}
				}
				else{
					game_status.append("error! \n");
					
					
				}


				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	class GPanel extends JPanel {
		URL ur;
        public GPanel(URL s) {
        	ur = s;
        	setPreferredSize(new Dimension(300, 300));
        
    	
        }

        public GPanel(Image curImg, int allbits, int i, int j, int k, int l) {
			
		}

		@Override
        public void paint(Graphics g){
        	super.paint(g);
			gpanel[0].removeAll();
			gpanel[1].removeAll();
			gpanel[2].removeAll();
			gpanel[3].removeAll();
    		
    		
    		curImg = Toolkit.getDefaultToolkit().getImage(ur);
    		g.drawImage(curImg, 0, 0, this);
    		
    		
    	}
    	
    	public boolean imageUpdate(URL str, int flags, int x, int y, int w, int h){
    		ur = str;
    		
    		curImg = Toolkit.getDefaultToolkit().getImage(ur);
    		if((flags&ALLBITS)!=0){
    			System.out.println("repaint!");
    			repaint();
    		}
    		return (flags&(ALLBITS|ERROR))==0;
    	}
    }
	
	
}
