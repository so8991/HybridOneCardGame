
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.net.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


class Recv implements Runnable
{
	Lock lock;
	Socket sock;
	DataInputStream Read;
	DataOutputStream Write;
	InputStream read; 
	String message;
	int state=0;	//101 : 1, 102 : 2, 103: 3

	
	byte arr[];
	
	public Recv(Socket soc, byte Arr[],Lock lock )
	{
		sock = soc;
		
		arr=Arr;
		this.lock = lock;
		
		try
		{
			Read = new DataInputStream(sock.getInputStream()); 
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public void run()
	{
		try
		{
			while(true)
			{
				
				int len = Read.read(arr,0,arr.length);
				message =  new String(arr,0, len);
				message = message.trim();
				//System.out.printf(message);
				System.out.println("Recv : "+message);
		//		fromServer = msg;
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
