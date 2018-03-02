package server;

public enum Commands {
	
	exit("/ext", null), userlist("/usrlst", null), kick("/kick", null), ;
	
	public String com;
	public ServerMain server;
	
	Commands(String s, ServerMain server){
		this.server = server;
	}
	
	public void Action(){
		if (this.com.equals("/exit")) server.exit(0);
		if (this.com.equals("/usrlst"));
	}	
	
}
