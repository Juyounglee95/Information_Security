package test3;

public class Connect {

	private static boolean ClientCon = false;
	private static boolean ServerCon = false;
	
	public void setClientCon(){
		ClientCon = !ClientCon;
	}
	
	public boolean getClientCon(){
		return ClientCon;
	}
}
