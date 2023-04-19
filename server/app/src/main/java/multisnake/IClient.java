package multisnake;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {

	public void notifyNewState(IServer server) throws RemoteException;

}
