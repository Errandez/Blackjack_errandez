package Juego;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Cliente {
	//Lanzo el cliente que se conectará al juego del BlackJack y creo un pool de hilos de valor fijo 2, uno para leer y otro para escribir.
	public static void main(String[] args) {
		Socket cliente = null;
		try{
				cliente = new Socket("localhost", 8080);
				ExecutorService pool = Executors.newFixedThreadPool(2);
				Hilolectura hl = new Hilolectura(cliente);
				HiloEscritura he = new HiloEscritura(cliente);
				Future<Boolean> result = pool.submit(hl);
				Future<Boolean> result1 = pool.submit(he);
				
				while(result1.get()) {
					
				}
				
				cliente.close();
		}catch(IOException e) {
			System.out.println("> Desconectado");
		}catch(ExecutionException e) {
			e.printStackTrace();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			try {
				System.out.println("Ha salido");
				if(cliente!= null) cliente.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	}

