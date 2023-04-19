package multisnake;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements IServer {

	private volatile Map<Integer, IClient> clients = new HashMap<>();
	//private volatile Map<IClient, Thread> timeouts = new HashMap<>();
	private volatile GameState state;

	protected Server() throws RemoteException {
		super();
		this.state = new GameState();
		Ticker ticker = new Ticker(500);
		ticker.start();
	}

	public int joinMatch(IClient client) throws RemoteException {
		if (!clients.containsValue(client)) {
			int id = state.addSnake();
			clients.put(id, client);
			return id;
		}
		return -1;
	}

	public ArrayList<Integer> listPlayerIds() {
		return new ArrayList<Integer>(clients.keySet());
	}

	public int getBoardHeight() throws RemoteException {
		return state.getRows();
	}

	public int getBoardWidth() throws RemoteException {
		return state.getCols();
	}

	public void moveSnakeUp(int id) throws RemoteException {
		state.redirectSnake(id, Direction.N);
	}

	public void moveSnakeDown(int id) throws RemoteException {
		state.redirectSnake(id, Direction.S);
	}

	public void moveSnakeLeft(int id) throws RemoteException {
		state.redirectSnake(id, Direction.W);
	}

	public void moveSnakeRight(int id) throws RemoteException {
		state.redirectSnake(id, Direction.E);
	}

	public Pos getSnakeHead(int id) throws RemoteException {
		return state.getSnakeHead(id);
	}

	public ArrayList<Pos> getSnakeTail(int id) throws RemoteException {
		return new ArrayList<Pos>(state.getSnakeTail(id));
	}

	public ArrayList<Pos> getFoods() throws RemoteException {
		return new ArrayList<Pos>(state.getFoods());
	}

	public void leaveMatch(IClient client, int id) throws RemoteException {
		state.removeSnake(id);
		clients.remove(id);
	}

	private void notifyClients() {
		clients.forEach((k, v) -> {
			try {
				v.notifyNewState(this);
				//timeouts.remove(v);
			} catch (RemoteException e) {
				System.out.println("Notifying client "+k+": Net error");
				/*Thread timeout = new Thread(() -> { // a bit broken...
					try {
						Thread.sleep(10 * 1000);
						this.leaveMatch(v, k);
						timeouts.remove(v);
					} catch (InterruptedException ee) {
						ee.printStackTrace();
					}
				});
				timeouts.putIfAbsent(v, timeout);
				timeout.start();*/
			} catch (Exception e) {
				System.out.println("Notifying client "+k+": Client error");
			}
		});
	}
	
	private class Ticker extends Thread {
		private final int ms;
		public Ticker(int ms) { this.ms = ms; }
		@Override
		public void run() {
			while (true) {
				try {
					state.tick();
					notifyClients();
					Thread.sleep(ms);
				} catch (ConcurrentModificationException e) {
					System.out.println("Ticking: Concurrent error");
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

}
