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
	public static int qntdRelogios = 3;


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
			String msg = ("Relogio adicionado [" + fila.size() + "]");

			PrintWriter outNovoCliente = new PrintWriter(novoCliente.getOutputStream(), true);
			BufferedReader inNovoCliente = new BufferedReader(new InputStreamReader(novoCliente.getInputStream()));

			outNovoCliente.println(msg);
			inNovoCliente.readLine();
		}
	}

	public static void main(String[] args) throws Exception {
		String mensagem;
		relogio = LocalTime.now();
		long diferencaTotal = 0;

		// inicializa o servidor
		servidor = new ServerSocket(8080);
		System.out.println("Lendo porta 8080");
		System.out.println("Hora do servidor: " + relogio);

		while (true) {
			// adiciona a fila até ter qntd de relogios desejada (qntdRelogios)
			while (fila.size() < qntdRelogios) {
				novoCliente = servidor.accept();
				fila.add(novoCliente);
			}

			int i = 0;
			while (i != fila.size()) {
				cliente = fila.get(indexAtual);
				// entrada e saida
				out = new PrintWriter(cliente.getOutputStream(), true);
				

				// envia a hora e os minutos pro cliente do server pro cliente
				System.out.println("Requisitando a hora do relogio " + (i + 1));
				out.println(relogio.toString()); //ultima linha que roda

				// cliente devolve a diferença de tempo
				in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
				mensagem = in.readLine();
				System.out.println("Voltou");
				int diferenca = Integer.parseInt(mensagem);
				System.out.println("diferença = "+diferenca);
				diferencaTotal += diferenca;

				// gira a fila, tipo anel
				Socket ultimoUsado = fila.remove(indexAtual);
				fila.add(ultimoUsado);
				i++;
			}

			// calcula o tanto que deve repassar de volta / ajustando relogio servidor
			System.out.println("diferenca total = "+diferencaTotal);
			diferencaTotal = diferencaTotal / (fila.size() + 1); // +1 pra contar o server tmb
			System.out.println("Diferença média: " + diferencaTotal + " minutos");
			relogio = relogio.plusMinutes(diferencaTotal);
			System.out.println("HORA AJUSTADA DO SERVIDOR: " + relogio);

			// repassando o tempo de volta
			i = 0;
			while (i != fila.size()) {
				cliente = fila.get(indexAtual);
				// entrada e saida
				out = new PrintWriter(cliente.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

				// envia a diferença pro cliente
				System.out.println("Enviando a diferença para o relogio " + (i + 1));
				out.println(diferencaTotal);

				// gira a fila, tipo anel
				Socket ultimoUsado = fila.remove(indexAtual);
				fila.add(ultimoUsado);
				i++;
			}

			System.out.println("Todos ajustados.\n Finalizando...");
			
			// terminando
			cliente.close();
			out.close();
			in.close();
			//thread.interrupt();
		}
	}
}
