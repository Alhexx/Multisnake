package multisnake;

import java.util.ArrayList;
import java.util.List;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.lang.model.SourceVersion;
import javax.swing.*;
import java.awt.*;


import java.rmi.RemoteException;

public class UI implements KeyListener{
    private IServer server;
    private int boardWidth;
    private int boardHeight;
    private char[][] board;
    private int idPlayer;
    private Color HColor;
    private Color TColor;
    JFrame frame;
    JPanel tilePanel;

    public UI(IServer server) throws RemoteException {
        this.server = server;
        this.boardHeight = server.getBoardHeight();
        this.boardWidth = server.getBoardWidth();
        this.HColor = Color.RED;
        this.TColor = Color.MAGENTA;
        this.board = new char[boardHeight][boardWidth];
        this.frame = new JFrame("Multisnake");
        this.tilePanel = new JPanel(new GridLayout(boardHeight, boardWidth));
        final JMenuBar tableMenuBar = createTableMenuBar();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(tableMenuBar);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.addKeyListener(this);

        frame.add(tilePanel);

        render();
    }

	private boolean isValid(Pos pos) {
		return pos.x >= 0 && pos.x < boardWidth
			&& pos.y >= 0 && pos.y < boardHeight;
	}

    public void printState() throws RemoteException {
        System.out.println("Height: " + server.getBoardHeight());
        System.out.println("Width: " + server.getBoardWidth());
        System.out.println("Player Count: " +server.listPlayerIds().size());
        server.listPlayerIds().forEach(id -> {
            try {
                System.out.println("Snake Head: " + server.getSnakeHead(id));
            } catch (RemoteException e) {
                System.out.println("Snake Head: error");
            }
        });
    }

    public void setIdPlayer(int id) {
        this.idPlayer = id;
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createPreferencesMenu() {
        final JMenu gameMenu = new JMenu("Preferences");
        final JMenu head = new JMenu("Head Color");
        final ButtonGroup headGroup = new ButtonGroup();
        
        gameMenu.add(head);
        

        final JRadioButtonMenuItem HRed = new JRadioButtonMenuItem("Red");
        HRed.setSelected(true);
        final JRadioButtonMenuItem HMagenta = new JRadioButtonMenuItem("Magenta");
        final JRadioButtonMenuItem HGray = new JRadioButtonMenuItem("Gray");
        final JRadioButtonMenuItem HCyan = new JRadioButtonMenuItem("Cyan");
        final JRadioButtonMenuItem HWhite = new JRadioButtonMenuItem("White");
        final JRadioButtonMenuItem HYellow = new JRadioButtonMenuItem("Yellow");

        ActionListener headColorListener = e -> {
            if (HRed.isSelected()) {
                HColor = Color.RED;
            } else if (HMagenta.isSelected()) {
                HColor = Color.MAGENTA;
            } else if (HGray.isSelected()) {
                HColor = Color.GRAY;
            } else if (HCyan.isSelected()) {
                HColor = Color.CYAN;
            } else if (HWhite.isSelected()) {
                HColor = Color.WHITE;
            } else if (HYellow.isSelected()) {
                HColor = Color.YELLOW;
            }
            try {
                render();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } 
        };

        head.add(HRed);
        HRed.addActionListener(headColorListener);
        head.add(HMagenta);
        HMagenta.addActionListener(headColorListener);
        head.add(HGray);
        HGray.addActionListener(headColorListener);
        head.add(HCyan);
        HCyan.addActionListener(headColorListener);
        head.add(HWhite);
        HWhite.addActionListener(headColorListener);
        head.add(HYellow);
        HYellow.addActionListener(headColorListener);

        headGroup.add(HRed);
        headGroup.add(HMagenta);
        headGroup.add(HGray);
        headGroup.add(HCyan);
        headGroup.add(HWhite);
        headGroup.add(HYellow);
        
        final JMenu tail = new JMenu("Tail Color");
        final ButtonGroup tailGroup = new ButtonGroup(); 
        gameMenu.add(tail);

        final JRadioButtonMenuItem TRed = new JRadioButtonMenuItem("Red");
        final JRadioButtonMenuItem TMagenta = new JRadioButtonMenuItem("Magenta");
        TMagenta.setSelected(true);
        final JRadioButtonMenuItem TGray = new JRadioButtonMenuItem("Gray");
        final JRadioButtonMenuItem TCyan = new JRadioButtonMenuItem("Cyan");
        final JRadioButtonMenuItem TWhite = new JRadioButtonMenuItem("White");
        final JRadioButtonMenuItem TYellow = new JRadioButtonMenuItem("Yellow");

        ActionListener tailColorListener = e -> {
            if (TRed.isSelected()) {
                TColor = Color.RED;
            } else if (TMagenta.isSelected()) {
                TColor = Color.MAGENTA;
            } else if (TGray.isSelected()) {
                TColor = Color.GRAY;
            } else if (TCyan.isSelected()) {
                TColor = Color.CYAN;
            } else if (TWhite.isSelected()) {
                TColor = Color.WHITE;
            } else if (TYellow.isSelected()) {
                TColor = Color.YELLOW;
            }
            try {
                render();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } 
        };

        tail.add(TRed);
        TRed.addActionListener(tailColorListener);
        tail.add(TMagenta);
        TMagenta.addActionListener(tailColorListener);
        tail.add(TGray);
        TGray.addActionListener(tailColorListener);
        tail.add(TCyan);
        TCyan.addActionListener(tailColorListener);
        tail.add(TWhite);
        TWhite.addActionListener(tailColorListener);
        tail.add(TYellow);
        TYellow.addActionListener(tailColorListener);

        tailGroup.add(TRed);
        tailGroup.add(TMagenta);
        tailGroup.add(TGray);
        tailGroup.add(TCyan);
        tailGroup.add(TWhite);
        tailGroup.add(TYellow);



        return gameMenu;
    }

    public void createBoard() throws RemoteException {
        printState();
        
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = ' ';
            }
        }

        List<Integer> playerIds = server.listPlayerIds();
        for (int i = 0; i < playerIds.size(); i++) {
            int id = playerIds.get(i);
            Pos headPos = server.getSnakeHead(id);
            ArrayList<Pos> tailPos = server.getSnakeTail(id);
            if (isValid(headPos))
                if(id == idPlayer) board[headPos.y][headPos.x] = 'P'; else board[headPos.y][headPos.x] = 'O';
            for (Pos p : tailPos) {
                if (isValid(p))
                if(id == idPlayer) board[p.y][p.x] = 'R'; else board[p.y][p.x] = 'T';
            }
            for (Pos p : server.getFoods()) {
                if (isValid(p))
                    board[p.y][p.x] = 'x';
            }
        }
    }

    public void render() throws RemoteException {
        createBoard();

        tilePanel.removeAll();

        for (int i = boardHeight-1; i >= 0; i--) {
            for (int j = 0; j < boardWidth; j++) {
                JLabel tileLabel = new JLabel();
                tileLabel.setOpaque(true);
                tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 

                if (board[i][j] == 'x') {
                    tileLabel.setBackground(Color.BLUE);
                } else if (board[i][j] == 'P'){
                    tileLabel.setBackground(HColor); 
                } else if (board[i][j] == 'R'){
                    tileLabel.setBackground(TColor);
                }  else if (board[i][j] == 'O'){
                    tileLabel.setBackground(Color.GRAY); 
                } else if (board[i][j] == 'T'){
                    tileLabel.setBackground(Color.PINK);
                } else {
                    tileLabel.setBackground(Color.GREEN);
                }

                tilePanel.add(tileLabel);
            }
        }

        SwingUtilities.invokeLater(() -> {
            tilePanel.revalidate();
            tilePanel.repaint();
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {switch(e.getKeyChar()) {
            case 'w':
                server.moveSnakeUp(idPlayer);
              break;
            case 'a':
                server.moveSnakeLeft(idPlayer);
              break;
            case 's':
                server.moveSnakeDown(idPlayer);
              break;
            case 'd':
                server.moveSnakeRight(idPlayer);
              break;
            default:
          }} catch (RemoteException e1) {
                    e1.printStackTrace();
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
