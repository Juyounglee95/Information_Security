package test5;

public class Connect {

	private static boolean ClientCon = false;
	
	public void setClientCon(){
		ClientCon = !ClientCon; 
	}
	
	public boolean getClientCon(){
		return ClientCon;
	}
}
