
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

// sets the class attributes
public class Server extends UnicastRemoteObject implements ChatInterface {
    private static final long serialVersionUID = 1L;
    private String clientName [] = {"User1","User2","User3","User4","User5","User6"};
    private ArrayList<String> connectedClients = new ArrayList<String>();
    private String clientPass [] = {"1234","1234","1234","1234","1234","1234"};
    private ArrayList<ChatInterface> clientList;
    
 
    // constructor for the server object
    protected Server() throws RemoteException {
        clientList = new ArrayList<ChatInterface>();
        
    }
    
    // check if the user login details are correct and adds the user to connected and client lists.
    public synchronized boolean checkClientCredintials(ChatInterface chatinterface,String clientname,String password) throws RemoteException {
        boolean chkLog = false;
        for(int i=0; i<clientName.length; i++) {
            if(clientName[i].equals(clientname) && clientPass[i].equals(password)) {
                chkLog = true;
                addClientName(clientname);
                this.clientList.add(chatinterface);
            }
        }
        return chkLog;
    }
    
     // adds clients to the connected clients array
    private void addClientName(String clientname2) {
		if(connectedClients.contains(clientname2)) {
		}else {
			connectedClients.add(clientname2);
		}
		
	}
    // sends the message to each of the connected clients
	public void broadcastMessage(String clientCountry, String clientname , String message) throws RemoteException {
        for(int i=0; i<clientList.size(); i++) {
            clientList.get(i).sendMessageToClient(clientCountry +": "+ clientname.toUpperCase() + " : "+ message + "\n");
            
        }
    }
	
	// unused
    public  synchronized void sendMessageToClient(String message) throws RemoteException{
    
    }

    // remove client from list of connected clients
	public synchronized void removeClient(String clientname) throws RemoteException {
		connectedClients.remove(clientname);
	}

	// display client with a list of all known chat clients when requested
	public synchronized String displayClientList() throws RemoteException {
		String message = "The connected clients are\n";
		for(int i=0; i<connectedClients.size(); i++) {
			message += (connectedClients.get(i)+"\n");
		}
		return message;
           
		
	}
	
}
