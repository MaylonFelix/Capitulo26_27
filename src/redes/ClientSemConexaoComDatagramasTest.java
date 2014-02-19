package redes;

import javax.swing.JFrame;

public class ClientSemConexaoComDatagramasTest {

	public static void main(String[] args) {
		ClientSemConexaoComDatagramas application = new ClientSemConexaoComDatagramas();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.waitForPackets(); 

	}

}
