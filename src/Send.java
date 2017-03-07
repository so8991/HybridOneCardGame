

import java.net.*;
import java.io.*;

import javax.swing.*;
import javax.swing.table.*;

import java.util.*;
import java.net.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


class Send implements Runnable
{
	Socket sock;
	DataOutputStream Write;
	DataInputStream Read;
	byte arr[];
	BufferedReader br;
	Lock lock;
	public Send(Socket soc,  byte Arr[],BufferedReader Br,  Lock lock  )
	{	
		
		this.lock = lock;
		sock = soc;
		try
		{
			System.out.println(Arr);

			Write = new DataOutputStream(sock.getOutputStream());
			Write.write(Arr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		arr = Arr;
		br = Br;
	}

	public void run()
	{
		
		while(true)
		{
			try
			{
				
				//String str = br.readLine();
				//arr = str.getBytes();
				//Write.write(arr);
				//Write.flush();
				/*
				String str = "00usr1|1234|\0";
				arr = str.getBytes();
				Write.write(arr);
				Write.flush();

				String str1 = "01usr1|1234|\0";
				arr = str1.getBytes();
				Write.write(arr);
				Write.flush();

*/

				

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
				
	
		
		
	}
}


