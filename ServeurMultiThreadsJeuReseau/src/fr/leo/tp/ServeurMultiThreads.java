	package fr.leo.tp;
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
	import java.net.Socket;
import java.util.Random;
	public class ServeurMultiThreads extends Thread {
	
		private  int nbr = 0;
		private int nbrSecret;
		private boolean fin;
		private String gagnant;
		
		@Override
		public void run() {			
			try {
				ServerSocket serverSocket = new ServerSocket(1234);		
				this.nbrSecret = new Random().nextInt(100);
				System.out.println(nbrSecret);			
				System.out.println("Le serveur a généré un nombre secret !");	
				while (true) {
					nbr = nbr + 1;
					Socket socket = serverSocket.accept();
					new ServiceClient(socket, nbr).start();
				}			
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
	
		public static void main(String[] args) {
			new ServeurMultiThreads().start(); 
		}	
		

		public class ServiceClient extends Thread{
			private Socket socket;
			private int nbr ;		
			public ServiceClient(Socket socket, int nbr) {
				this.socket = socket;
				this.nbr = nbr;
			}
			
			@Override
			public void run() {
				try {
					InputStream is = socket.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					
					OutputStream os = socket.getOutputStream();
					PrintWriter pw = new PrintWriter(os, true);
					
					System.out.println("Connexion du client numéro " + this.nbr);
					pw.println("Vous etes le client numéro " + this.nbr);
					//pw.println("Devine le nombre secret entre 0 et 100");			
					
					while (true) { 
						String req =  br.readLine();
						boolean nbreCorrect = true;
						int nbreEnvoyeParClient = 0;
						try {
							nbreCorrect = true;
							nbreEnvoyeParClient = Integer.parseInt(req);
						} catch (NumberFormatException e) {
							nbreCorrect = false;
						}

						if (nbreCorrect) {
							if (fin == false) {
								if (nbreEnvoyeParClient < nbrSecret) {
									pw.println("Le nombre est trop petit");
								} else if (nbreEnvoyeParClient > nbrSecret) {
									pw.println("Le nombre est trop grand");
								} else {
									fin = true;
									pw.println("Bravo vous avez trouvé");
								}
							} else {
								pw.println("La partie est terminée !");
							} 
						}else{
							pw.println("Le nombre est incorrect");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}