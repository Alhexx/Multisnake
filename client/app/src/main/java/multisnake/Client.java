package multisnake;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IClient {

	//... constructor must receive UI instance ...
	private UI ui;
	//protected Client(UI ui) throws RemoteException {
	//	this.ui = ui;
	//}
	protected Client(IServer server) throws RemoteException {
		super();
		this.ui = new UI(server);
		int id = server.joinMatch(this);
		ui.setIdPlayer(id);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				server.leaveMatch(this, id);
			} catch (RemoteException e) {
				System.out.println("Failed leaving match");
			}
		}));
		
	}

	public void notifyNewState(IServer server) throws RemoteException {

		ui.render();
	}

}
