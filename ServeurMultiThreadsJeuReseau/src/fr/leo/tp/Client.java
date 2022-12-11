package fr.leo.tp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 1234);
			OutputStream outputStream = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(outputStream, true);
			
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			System.out.println( br.readLine());
			Scanner clavier = new Scanner(System.in);
			while (true) {
				System.out.println("Entrez un nombre entre 0 et 100: ");
				int nbr =  clavier.nextInt();
				pw.print(nbr);
				pw.println("");
				pw.flush();
				String reponseServeur  = br.readLine();	
				System.out.println(reponseServeur);
			}
			//socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
