package Juego;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Servidor{
  public static void main(String[] args) throws IOException {
    // Crea el servidor y espera a que se conecten los clientes.
	  ServerSocket serverSocket = null;
	  try {
	    serverSocket = new ServerSocket(8080);
	    ArrayList<JugadorConectado> jugadores = new ArrayList<JugadorConectado>();
	    ExecutorService pool = Executors.newCachedThreadPool();
	    while (true) {
	    	Socket socket = null;
	    	try {
		      socket = serverSocket.accept();							//Cliente conectado, lanza aplicación del Blackjack.
		      JugadorConectado jc = new JugadorConectado(socket);
		      pool.execute(jc);
	    	}catch(IOException e) {
	    		if(socket!= null) socket.close();
	    	}
	    	
	    }
	  }catch(IOException e) {
		  e.printStackTrace();
	}finally {
		try {
			if(serverSocket!=null) serverSocket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
  }
}
