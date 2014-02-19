package redes;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TicTacToeServer extends JFrame {

	private String[] board = new String[9];
	private Player[] players;
	private ServerSocket server;
	private int currentPlayer;
	private final static int PLAYER_0 = 0;
	private final static int PLAYER_X = 1;
	private final static String[] MARKS = { "0", "X" };
	private ExecutorService runGame;
	private Lock gameLock;
	private Condition otherPlayerConnected;
	private Condition otherPlayerTurn;
	private JTextArea outputArea;

	// configura o servidor de tic-tac-toe e a GUI que exibe as mensagens
	public TicTacToeServer() {
		super("Tic-Tac-Toe Server");
		runGame = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock();
		otherPlayerConnected = gameLock.newCondition();
		otherPlayerTurn = gameLock.newCondition();

		for (int i = 0; i < 9; i++)
			board[i] = new String("");

		players = new Player[2];
		currentPlayer = PLAYER_0;

		try {
			server = new ServerSocket(12345, 2);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		outputArea = new JTextArea();
		add(outputArea, BorderLayout.CENTER);
		outputArea.setText("Server awainting connections\n");

		setSize(300, 300);
		setVisible(true);
	}

	// espra duas conexões para que o jogo possa ser jogado
	public void execute() {
		for (int i = 0; i < players.length; i++) {
			try {
				players[i] = new Player(server.accept(), i);
				runGame.execute(players[i]);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		gameLock.lock();
		// TODO
	}

	private void displayMessage(final String messageToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				outputArea.append(messageToDisplay);

			}
		});

	}

	public boolean validateAndMove(int location, int player) {
		return true;
		// TODO
	}

	public boolean isOccupied(int location) {
		return true;
		// TODO
	}

	private boolean isGameOver() {
		// TODO Auto-generated method stub
		return false;
	}

	private class Player implements Runnable {

		private Socket connection;
		private Scanner input;
		private Formatter output;
		private int playerNumber;
		private boolean suspended = true;
		private String mark;

		public Player(Socket socket, int number) {
			playerNumber = number;
			mark = MARKS[playerNumber];
			connection = socket;

			try {
				input = new Scanner(connection.getInputStream());
				output = new Formatter(connection.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		public void otherPlayerMoved(int location) {
			output.format("Opponet moved\n");
			output.format("%d\n", location);
			output.flush();
		}

		@Override
		public void run() {
			try {
				displayMessage("Player " + mark + " connected\n");
				output.format("%s\n", mark);
				output.flush();

				if (playerNumber == PLAYER_X) {
					output.format("%s\n%s", "player X connected",
							"Wainting for another player\n");
					output.flush();
					gameLock.lock();

					try {
						while (suspended) {
							otherPlayerConnected.await();
						}
					} catch (InterruptedException exception) {
						exception.printStackTrace();
					} finally {
						gameLock.unlock();
					}
					output.format("Other player connected. Your move.\n");
					output.flush();
				} else {
					output.format("Player 0 connected, please wait\n");
					output.flush();
				}

				while (!isGameOver()) {
					int location = 0;
					if (input.hasNext())
						location = input.nextInt();

					if (validateAndMove(location, playerNumber)) {
						displayMessage("\nlocation: " + location);
						output.format("Valid move.\n");
						output.flush();
					} else {
						output.format("Invalid move, try again\n");
						output.flush();
					}
				}
			} finally {

				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}

		public void setSuspended(boolean status) {
			suspended = status;
		}

	}

}
