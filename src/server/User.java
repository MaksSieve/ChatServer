package server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class User implements Runnable {
	
	@SuppressWarnings("unused")
	private BufferedReader userIn = null, serverIn = null;
	private OutputStream userOut = null;
	private PrintStream serverOut = null;
	private ArrayList <User> userList = null;
	private String msg = "";
	private int num = 0;
	public String nick = "";
	private Socket userSocket = null;
	private Thread trd = null;
		
	User(Socket uSocket, PrintStream sOut, int n, ArrayList <User> uList) throws IOException
	{
		
		this.userList = uList;
		this.num = userList.indexOf(this);
		this.serverOut = sOut;
		this.userSocket = uSocket;
		this.userIn = new BufferedReader(
			new InputStreamReader(userSocket.getInputStream())
		);
		this.userOut = userSocket.getOutputStream();
		
		sayToUser("Welcome!");
		sayToUser("Enter your nickname:");
		this.nick = userIn.readLine();
		//TODO Move to ServerMain
		trd = new Thread(this, "user "+num+" thread");
		trd.start();
	}
	
	synchronized private void sayToServer(String m)
	{
		serverOut.println(m);
		//sayToUser("Meesage resieved!");
	}
	
	synchronized public void sayToUser(String m)
	{
		try {
			userOut.write((m + "\n").getBytes());
		} catch (IOException e) {
			
		}
	}
	
	private int listen() throws IOException
	{
		while (true) {
			msg = userIn.readLine();
			if (msg == "exit") return 0;
			sayToServer(nick + " ::: " + msg);
			Iterator<User> itr = userList.iterator();
			while (itr.hasNext())
			{
				User u = (User) itr.next();
				if(this != u){
					u.sayToUser(nick + " ::: " + msg);
				}
			}
		}
	}
	
	@Override
	public void run()
	{
		try {
			if (listen()==0){
				sayToServer("User #" + num + " disconnected.");
				userList.remove(this);
			};
			
		} catch (IOException e) {
			sayToServer("ERROR! I/O Exception. ");
		}
	}
}
