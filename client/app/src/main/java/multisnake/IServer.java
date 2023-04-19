package multisnake;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {

	public int joinMatch(IClient client) throws RemoteException;

	public ArrayList<Integer> listPlayerIds() throws RemoteException;

	public int getBoardHeight() throws RemoteException;

	public int getBoardWidth() throws RemoteException;

	public void moveSnakeUp(int id) throws RemoteException;

	public void moveSnakeDown(int id) throws RemoteException;

	public void moveSnakeLeft(int id) throws RemoteException;

	public void moveSnakeRight(int id) throws RemoteException;

	public Pos getSnakeHead(int id) throws RemoteException;

	public ArrayList<Pos> getSnakeTail(int id) throws RemoteException;

	public ArrayList<Pos> getFoods() throws RemoteException;

	public void leaveMatch(IClient client, int id) throws RemoteException;

}
