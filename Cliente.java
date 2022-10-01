import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class RelogioCliente {
	public static void main(String[] args) throws Exception{
		Socket cliente;
		BufferedReader in;
		PrintWriter out;
		BufferedReader inReader;
		String mensagem;
		String mensagemQuadro;
		LocalTime relogio = LocalTime.now();

		// incializando socket
		cliente = new Socket("localhost", 8080);

		// entrada e saida
		in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
		out = new PrintWriter(cliente.getOutputStream(), true);

		inReader = new BufferedReader(new InputStreamReader(System.in));
		//conversão do relogio em string e vice versa 
		String resposta = in.readLine();
		System.out.println("recebido hora do server: "+ resposta);
		DateTimeFormatter parser = DateTimeFormatter.ofPattern("hh:mm");
		// ##revisar o formatter
		relogio = 

		out.println("solicitando acesso");
		//String resposta = in.readLine();
		if (resposta.contains("aguarde")) {
			System.out.println("Retornou: " + resposta);
			out.println("aguardando");
			while (true) {
				if ((resposta = in.readLine()).contains("permitido")) {
					break;
				}
			}
		}
		System.out.println("Retornou: " + resposta);
		System.out.println("Msg: ");
		while ((mensagemQuadro = inReader.readLine()) != null) {
			out.println(mensagemQuadro);
			break;
		}
		
		// terminando
		out.close();
		in.close();
		//cliente.close();
	}
}
