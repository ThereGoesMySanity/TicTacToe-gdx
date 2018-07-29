package tgms.ttt.Net;

public abstract class StringConnection extends Connection {
	
	public abstract void writeString(String s);
	public abstract String readString();
	
    public StringConnection(String username) {
    	super(username);
    }
    
    public Message read(){
//		String s = readString();
		//TODO: JSON
		return null;
    }
    public void send(Message m) {
    	String s = "";
    	//TODO: JSON
    	writeString(s);
    }
}
