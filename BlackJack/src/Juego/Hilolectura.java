package Juego;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class Hilolectura implements Callable<Boolean>{
	private Socket cliente;
	
	public Hilolectura(Socket cliente) {
	   this.cliente = cliente;
	}
	
	public Boolean call() {
	   try (DataInputStream reader = new DataInputStream(cliente.getInputStream())) {
	      // Lee el archivo línea a línea
	      String linea;
	      boolean off = false;
	      while(!off) {
	    	  linea = reader.readLine();
		      if (linea != null) {
		         System.out.println("> "+ linea);
		         if(linea.contains("desconectar")) {
		        	 off = true;
		         }
		      }
	      }
	      return true;
	   } catch (IOException e) {
	      e.printStackTrace();
	      return true;
	   }
	}
}