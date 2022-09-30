import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server extends Thread {
	public boolean liberado = true;
	public static ArrayList<Socket> fila = new ArrayList<Socket>(); // fila de sockets - conexao
	public static Socket cliente;
	public static Socket novoCliente;
	public static ServerSocket servidor;
	public static PrintWriter out;
	public static BufferedReader in;
	public static int indexAtual = 0;

	// adiciona os clientes na fila
	public void run() {
		while (true) {
			try {
				novoCliente = servidor.accept();
				fila.add(novoCliente);
				isLiberado();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// verifica o estado da fila e retorna a mensagem pro cliente
	public void isLiberado() throws Exception {
		if (indexAtual < fila.size() - 1) {
			String msg = ("aguarde, voce esta na posiçao " + fila.size());

			PrintWriter outNovoCliente = new PrintWriter(novoCliente.getOutputStream(), true);
			BufferedReader inNovoCliente = new BufferedReader(new InputStreamReader(novoCliente.getInputStream()));

			outNovoCliente.println(msg);
			inNovoCliente.readLine();
		}
	}

	public static void main(String[] args) throws Exception {
		String mensagem;
		String mensagemQuadro;
		String quadro = "##### QUADRO DE AVISOS #####\n";

		// inicializa o servidor
		servidor = new ServerSocket(8080);
		System.out.println("Lendo porta 8080");

		while (true) {
			// se n tiver ninguem na fila, adiciona
			if (fila.size() == 0) {
				novoCliente = servidor.accept();
				fila.add(novoCliente);
			}

			if (fila.size() > 0) {
				Server thread = new Server(); // começa um objeto dessa propria classe
				thread.start();
				cliente = fila.get(indexAtual);

				System.out.println("Novo cliente conectado: " + servidor.toString());

				// entrada e saida
				out = new PrintWriter(cliente.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
				
				while ((mensagem = in.readLine()) != null) { // *
					System.out.println("recebeu: "+mensagem);
					if (mensagem.contains("solicitando acesso") || mensagem.contains("aguardando")) { //server recebe mensagem, mas não responde
						out.println("acesso permitido");
					}
//					out.println("acesso permitido");
					mensagemQuadro = in.readLine();
					quadro = (quadro + "\n" + mensagemQuadro);
					System.out.println(quadro);
				}		

				// terminando
				System.out.println("Cliente desconectando");
				fila.remove(indexAtual); //sempre importante
				cliente.close();
				out.close();
				in.close();
				thread.interrupt();
			}
		}
	}
}

//		if (server.isLiberado()) {
//			try {
//				// liberado
//				Socket socket;
//
//				if (server.fila.isEmpty()) {
//					socket = servidor.accept();
//				} else {
//					socket = server.fila.remove(0);
//				}
//				System.out.println("Cliente conectou: " + socket.getInetAddress().getHostName());
//				server.setLiberado(false);
//				System.out.println("Fechando o Acesso");
//				System.out.println(server.isLiberado());
//
//				server.usandoQuadro(socket);
//
//				server.setLiberado(true);
//				System.out.println("\nAcesso Liberado novamente");
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		} else {
//			// não liberado
//			// adiciona na lista + processo aguarda
//			// (?) - armazenando o endereço do client pra
//			// puxar depois (?)
//
//			System.out.println("Acesso negado - Adicionando socket a lista");
//			try {
//				Socket proximo = servidor.accept();
//				server.fila.add(proximo);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//
//		}
//	}
//
//	}}.start();
//// 	}
//
//	public void usandoQuadro(Socket socket) {
//		System.out.println(quadro);
//		Scanner leitura;
//		try {
//			leitura = new Scanner(socket.getInputStream());
//			while (leitura.hasNext()) {
//				String texto = leitura.nextLine();
//				System.err.println(texto);
//				quadro = quadro + ("\n" + texto);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
////	public boolean isLiberado() {
////		return liberado;
////	}
//
//	public void setLiberado(boolean liberado) {
//		this.liberado = liberado;
//	}
//
//	public String getQuadro() {
//		return quadro;
//	}
//
//	public void setQuadro(String quadro) {
//		this.quadro = quadro;
//	}
//
//}