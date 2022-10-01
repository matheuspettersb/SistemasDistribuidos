import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RelogioServer extends Thread {
	public boolean liberado = true;
	public static ArrayList<Socket> fila = new ArrayList<Socket>(); // fila de sockets - conexao
	public static Socket cliente;
	public static Socket novoCliente;
	public static ServerSocket servidor;
	public static PrintWriter out;
	public static BufferedReader in;
	public static int indexAtual = 0;
	public static LocalTime relogio;

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
		relogio = LocalTime.now();

		// inicializa o servidor
		servidor = new ServerSocket(8080);
		System.out.println("Lendo porta 8080");

		while (true) {
			// se n tiver ninguem na fila, adiciona
			// ### mudar a logica aqui, usar um tempo limite pra conexão(?)
			if (fila.size() == 0) {
				novoCliente = servidor.accept();
				fila.add(novoCliente);
			}
			// ###
			
			// entrada e saida
			out = new PrintWriter(cliente.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			
			int i=0;
			while (i!=fila.size()) {
				//envia a hora do server pro cliente
				System.out.println("Requisitando a hora do cliente " + (i+1));
				cliente = fila.get(indexAtual);
				out.print(relogio);
				
				//cliente devolve a diferença de tempo
				i++;
			}
			
			//referencia do codigo antigo
			//if (fila.size() > 0) {
				RelogioServer thread = new RelogioServer(); // começa um objeto dessa propria classe
				thread.start();
				cliente = fila.get(indexAtual);

				System.out.println("Novo relogio conectado: " + cliente.toString());

//				// entrada e saida
//				out = new PrintWriter(cliente.getOutputStream(), true);
//				in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
				
				while ((mensagem = in.readLine()) != null) { // *
					System.out.println("recebeu: "+mensagem);
					if (mensagem.contains("solicitando acesso") || mensagem.contains("aguardando")) { //server recebe mensagem, mas não responde
						out.println("acesso permitido");
					}
//					out.println("acesso permitido");
//					mensagemQuadro = in.readLine();
//					quadro = (quadro + "\n" + mensagemQuadro);
//					System.out.println(quadro);
				}		

				// terminando
				System.out.println("Cliente desconectando");
				fila.remove(indexAtual); //sempre importante
				cliente.close();
				out.close();
				in.close();
				thread.interrupt();
			//}
		}
	}
}

