package Juego;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;

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
		         writer.writeBytes(mensaje +"\n");
		         if(mensaje.contains("desconectar")) {
		        	 off=true;
		         }
	    	  }
	    	  return true;
	      } catch (IOException e) {
	         e.printStackTrace();
	         return true;
	      }
	   }
	}
