package redes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientSemConexaoComDatagramas extends JFrame {

	private JTextField enterField;
	private JTextArea displayArea;
	private DatagramSocket socket;

	public ClientSemConexaoComDatagramas() {
		super("Client");
		enterField = new JTextField("Type message here");
		enterField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					String message = event.getActionCommand();
					displayArea.append("\nSending packet containing: "
							+ message + "\n");
					byte[] data = message.getBytes();

					DatagramPacket sendPacket = new DatagramPacket(data,
							data.length, InetAddress.getByName("172.16.253.108"), 8050);

					socket.send(sendPacket);
					displayArea.append("Packet sent\n");
					displayArea.setCaretPosition(displayArea.getText().length());

				} catch (IOException ioException) {
					displayMessage(ioException + "\n");
					ioException.printStackTrace();
				}
			}
		});

		add(enterField, BorderLayout.NORTH);
		displayArea = new JTextArea();
		add(new JScrollPane(displayArea), BorderLayout.CENTER);
		setSize(400, 300);
		setVisible(true);

		try {
			socket = new DatagramSocket();
		} catch (SocketException socketException) {
			socketException.printStackTrace();
			System.exit(1);
		}

	}

	public void waitForPackets() {
		while (true) {
			try {
				byte[] data = new byte[100];
				DatagramPacket receivePacket = new DatagramPacket(data,
						data.length);

				socket.receive(receivePacket);

				displayMessage("\nPacket received:"
						+ "\nFrom host:"
						+ receivePacket.getAddress()
						+ "\nHost port:"
						+ receivePacket.getPort()
						+ "\nLength:"
						+ receivePacket.getLength()
						+ "\nContaining:\n\t"
						+ new String(receivePacket.getData(), 0,
								receivePacket.getLength()));

			} catch (IOException ioException) {
				displayMessage(ioException + "\n");
				ioException.printStackTrace();
			}

		}
	}

	private void displayMessage(final String messageToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				displayArea.append(messageToDisplay);

			}
		});
	}

}
