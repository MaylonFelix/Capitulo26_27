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
	private JTextArea outputArea;
	private Player[] players;
	private ServerSocket server;// socket de servidor para conectar com clientes
	private int currentPlayer;
	private final static int PLAYER_X = 0;
	private final static int PLAYER_O = 1;
	private final static String[] MARKS = { "X", "O" };
	private ExecutorService runGame;
	private Lock gameLock;
	private Condition otherPlayerConnected;
	private Condition otherPlayerTurn;

	// configura o servidor de tic-tac-toe e a GUI que exibe as mensagens
	public TicTacToeServer() {
		super("Jogo da Velha Server");

		runGame = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock();

		otherPlayerConnected = gameLock.newCondition();
		otherPlayerTurn = gameLock.newCondition();

		for (int i = 0; i < 9; i++)
			board[i] = new String("");

		players = new Player[2];
		currentPlayer = PLAYER_X;

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

	// espera duas conexões para que o jogo possa ser jogado
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

		gameLock.lock();// bloqueia o jogo para sinalizar a thread do jogador X

		try {
			players[PLAYER_X].setSuspended(false);// retoma o jogador X
			otherPlayerConnected.signal();// acorda a thread do jogador X
		} finally {
			gameLock.unlock();// desbloqueia o jogo depois de sinalizar para o
								// jogador X
		}
	}

	private void displayMessage(final String messageToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				outputArea.append(messageToDisplay);

			}
		});

	}

	// determina se a jogada é válida
	public boolean validateAndMove(int location, int player) {
		while (player != currentPlayer) {
			gameLock.lock();
			try {
				otherPlayerTurn.await();
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			} finally {
				gameLock.unlock();
			}
		}

		// se a posição não estiver ocupada faz a jogada
		if (!isOccupied(location)) {
			board[location] = MARKS[currentPlayer];// configura uma jogada no
													// tabuleiro
			currentPlayer = (currentPlayer + 1) % 2;// troca o jogador

			// deixa que o novo jogador atual saiba que a jogada ocorreu
			players[currentPlayer].otherPlayerMoved(location);

			gameLock.lock();
			try {
				otherPlayerTurn.signal();
			} finally {
				gameLock.unlock();
			}
			return true;
		} else
			// a jogada não foi válida
			return false;
	}

	public boolean isOccupied(int location) {

		if (board[location].equals(MARKS[PLAYER_X])
				|| board[location].equals(MARKS[PLAYER_O])) {
			return true;
		} else
			return false;
	}

	private boolean isGameOver() {
		return false;
	}

	// Classe interna privada Player, gerencia cada Player como um executável.
	private class Player implements Runnable {

		private Socket connection;
		private Scanner input;
		private Formatter output;
		private int playerNumber;// monitora qual é o jogador
		private String mark;
		private boolean suspended = true;// se a thread está suspensa

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

		// Execução da thread de controle
		@Override
		public void run() {
			// Envia ao cliente a marca (X ou 0), processa as mensagens do
			// cliente
			try {
				displayMessage("Player " + mark + " connected\n");
				output.format("%s\n", mark);
				output.flush();

				// se for X espera que o outro jogador chegue
				if (playerNumber == PLAYER_X) {
					output.format("%s\n%s", "Player X connected",
							"Wainting for another player\n");
					output.flush();
					gameLock.lock();// bloqueia o jogo para esperar o segundo
									// jogador

					try {
						while (suspended) {
							otherPlayerConnected.await();// espera o jogador 0
						}
					} catch (InterruptedException exception) {
						exception.printStackTrace();
					} finally {
						gameLock.unlock();
					}
					output.format("Other player connected. Your move.\n");
					output.flush();
				} else {
					output.format("Player O connected, please wait\n");
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
