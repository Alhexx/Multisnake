/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package multisnake;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class App {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
		//System.setProperty("java.rmi.server.codebase", "/home/gus/Desktop/PD/multisnake/server/app/build/classes/java/main/server");
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");
		IServer server = new Server();
		LocateRegistry.createRegistry(1098);
		Naming.rebind("rmi://127.0.0.1:1098/ServerCallback", server);
		System.out.println("RMI Callback Server Started");
    }

}