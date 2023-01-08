package Juego;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;

//Permitirá escribir al cliente.

public class HiloEscritura implements Callable<Boolean> {
	   private Socket cliente;

	   public HiloEscritura(Socket cliente) {
	      this.cliente = cliente;
	   }

	   public Boolean call() {
	      try (DataOutputStream writer = new DataOutputStream(cliente.getOutputStream())) {
	    	  Scanner r = new Scanner(System.in);
	    	  boolean off = false;
	    	  while(!off) {
		    	 String mensaje = r.nextLine();
		         
		         if(mensaje.contains("desconectar")) {
		        	 off=true;
		         }else {
		        	 writer.writeBytes(mensaje +"\n");
		         }
	    	  }
	    	  return false;
	      } catch (IOException e) {
	         e.printStackTrace();
	         return false;
	      }
	   }
	}
