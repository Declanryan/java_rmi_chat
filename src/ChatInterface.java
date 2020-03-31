
import java.rmi.RemoteException;
// list the methods that will be available on the remote interfaces
public interface ChatInterface extends java.rmi.Remote {
    boolean checkClientCredintials(ChatInterface ci,String name, String pass) throws RemoteException;
    void broadcastMessage(String country, String name,String message) throws RemoteException;
    void sendMessageToClient(String message) throws RemoteException;
    void removeClient(String clientName) throws RemoteException;
    String displayClientList() throws RemoteException;
}