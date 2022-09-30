import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) throws Exception{
		Socket cliente;
		BufferedReader in;
		PrintWriter out;
		BufferedReader inReader;
		String mensagem;
		String mensagemQuadro;

		// incializando socket
		cliente = new Socket("localhost", 8080);

		// entrada e saida
		in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
		out = new PrintWriter(cliente.getOutputStream(), true);

		inReader = new BufferedReader(new InputStreamReader(System.in));

		out.println("solicitando acesso");
		String resposta = in.readLine();
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

//	public void run() {
//		while (true) {
//			PrintStream escrita = null;
//			try {
//				escrita = new PrintStream(cliente.getOutputStream());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			Scanner teclado = new Scanner(System.in);
//			// String teclado = "TEste \n teste teste \n amogos";
//			// escrita.println(teclado);
//			escrita.println(teclado.nextLine());
//			break;
//		}
//	}
//
//	}catch(
//
//	UnknownHostException e)
//	{
//		e.printStackTrace();
//	}catch(
//	IOException e)
//	{
//			e.printStackTrace();
//		}
//}
