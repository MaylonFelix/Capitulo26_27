package redes;

import javax.swing.JFrame;

public class ServerSemConexaoComDatagramasTest {

	public static void main(String[] args) {
		ServerSemConexaoComDatagramas application =  new ServerSemConexaoComDatagramas();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.waitForPackets();

	}

}
