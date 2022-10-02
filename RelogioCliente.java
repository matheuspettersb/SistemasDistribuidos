import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Scanner;

public class RelogioCliente {
	public static void main(String[] args) throws Exception{
		Socket cliente;
		BufferedReader in;
		PrintWriter out;
		LocalTime relogio;

		// incializando socket
		cliente = new Socket("localhost", 8080);
		
		//setando uma hora aleatoria
		Random random = new Random();
		int h = random.nextInt(23);
		int m = random.nextInt(59);
		relogio = LocalTime.of(h, m);
		System.out.println("Hora desse cliente: "+relogio);
		
		String resposta;

		// entrada e saida
		in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
		out = new PrintWriter(cliente.getOutputStream(), true);

		//conversão do relogio em string e vice versa;
		resposta = in.readLine(); //*mensagem n chega aqui - pq?
		System.out.println("Chegou");
		System.out.println("recebido hora do server: "+ resposta);
		LocalTime respostaTemporal = LocalTime.parse(resposta);
		
		//calcula a diferença
		long diferencaMinutos = ChronoUnit.MINUTES.between(respostaTemporal, relogio);
		//envia pro server
		System.out.println("Enviando a diferença ("+diferencaMinutos+")");
		out.println(diferencaMinutos);
		
		//server responde com a média
		resposta = in.readLine();
		System.out.println("Recebido média:"+resposta);
		long diferenca = Integer.parseInt(resposta);
		diferenca = (diferenca - diferencaMinutos); 	//diferença  = resposta do server + (-1 * diferença anterior) > (resposta - difAnt)
		relogio = relogio.plusMinutes(diferenca);
		System.out.println("Relógio Ajudastado: "+relogio);
		
		// terminando
		out.close();
		in.close();
		cliente.close();
	}
}
