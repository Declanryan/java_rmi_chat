import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import javafx.application.Application;
/*
@author declan ryan
*/

public class Main {
    public static void main(String[] args) {
    	// startRMIRegistry(); this is used to start the registry autimatically when not using shells
	// the registry starts autimatically when this is enabled and dispenses with the need for a second shell.
    	
    	// start the server
		try {// associate the server object on the registry
			Naming.rebind("RMIServer", new Server());
			System.out.println("Server Started");// testing
		} catch (RemoteException | MalformedURLException e1) {
			e1.printStackTrace();
		}
        Application.launch(AdminGui.class, args);// launch main javafx thread
    }
    // method to start registry
    public static void startRMIRegistry() {
		try{// create the registry on port 1099
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI Registry Started");// testing
		}
		catch(RemoteException e) {
			e.printStackTrace();
		}

	}
}
