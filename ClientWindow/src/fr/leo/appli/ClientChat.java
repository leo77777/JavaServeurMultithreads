package fr.leo.appli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observer;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//  Utilser le jdk 1.8 ! 
public class ClientChat extends Application {
	
	PrintWriter pw ;

	public static void main(String[] args) {
		// méthode static de la classe Application
		launch(args);
	}

	// Méthode abstraite de la classe Application
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Client Chat");
		BorderPane borderPane = new BorderPane();

		Label lblHost = new Label("Host : ");
		TextField txtfldHost = new TextField("localhost");
		Label lblPort = new Label("Port : ");
		TextField txtfldPort = new TextField("1234");
		Label lblSuccessConnect = new Label("");
		Button btnConnect = new Button("Connecter");
		HBox hbxConnect = new HBox();
		hbxConnect.setSpacing(10);
		hbxConnect.setPadding(new Insets(10.0));
		hbxConnect.getChildren().addAll(lblHost, txtfldHost, lblPort, 
				txtfldPort, btnConnect , lblSuccessConnect);
		hbxConnect.setBackground(new Background(new BackgroundFill(Color.AQUA,null, null)));
		borderPane.setTop(hbxConnect);
		
		ObservableList<String> listModel = FXCollections.observableArrayList();
		ListView<String> list = new ListView<String>(listModel);
		VBox vbxList = new VBox();
		vbxList.setSpacing(10);
		vbxList.setPadding(new Insets(10.0));
		vbxList.getChildren().add(list);
		borderPane.setCenter(vbxList);
		
		Label lblMessage = new Label("Envoyer un message : ");
		TextField txtfldMessage = new TextField();
		txtfldMessage.setPrefSize(300, 20);
		Button btnEnvoieMessage = new Button("Envoyer");
		HBox hbxMessage = new HBox();
		hbxMessage.setSpacing(10);
		hbxMessage.setPadding(new Insets(10.0));
		hbxMessage.getChildren().addAll(lblMessage,txtfldMessage, btnEnvoieMessage);
		borderPane.setBottom(hbxMessage);

		Scene scene = new Scene(borderPane, 800, 400); // 500 pixels !
		primaryStage.setScene(scene); 
		primaryStage.show();

		btnConnect.setOnAction( evt->{
			String host = txtfldHost.getText();
			Integer port = Integer.parseInt(txtfldPort.getText());
			try {
				Socket socket = new Socket(host, port);
				lblSuccessConnect.setText("Connection réussie");
				lblSuccessConnect.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW,null, null)));
				
				InputStream inputStream = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(isr);
				
				OutputStream outputStream = socket.getOutputStream();
				pw = new PrintWriter(outputStream, true);
				

//				new Thread( new  Runnable() {
//					Scanner clavier = new Scanner(System.in);
//					public void run() {						
//							while (true) {
//								System.out.println("Entrez une chaine de caractères");
//								String message = clavier.nextLine();
//								pw.println(message);
//								// pw.flush(); necessaire si on met le bolean à false !
//							}
//					}					
//				} ).start();
				
				new Thread(()->{
					while (true) {
						//////////////String reponseServeur;
						////////try {
							///////reponseServeur = br.readLine();
							////////listModel.add(reponseServeur);
							
							// SOLUTION							
								try {
									String reponseServeur = br.readLine();
									// Ci dessous, on le fait uniquement sur l'instruction
									//  qui communique avec  l'interface !
									Platform.runLater(()->{
										listModel.add(reponseServeur);										
									});
								} catch (IOException e1) {
									e1.printStackTrace();
								}
						
						//////} catch (IOException e) {
							///////e.printStackTrace();
						/////////}
					}
				}).start();	
			} catch (IOException e) {
				lblSuccessConnect.setText("Pbe connexion !");
				lblSuccessConnect.setBackground(new Background(new BackgroundFill(Color.RED,null, null)));
				e.printStackTrace();
			}
		});
		
		btnEnvoieMessage.setOnAction(e->{
			String messageEnvoie = txtfldMessage.getText();	
			pw.println(messageEnvoie);			
		});
	}
}