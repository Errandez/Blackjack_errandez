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
	public static void main(String[] args) {
		try(Socket cliente = new Socket("localhost", 8080);){
				ExecutorService pool = Executors.newFixedThreadPool(2);
				Hilolectura hl = new Hilolectura(cliente);
				HiloEscritura he = new HiloEscritura(cliente);
				Future<Boolean> result = pool.submit(hl);
				Future<Boolean> reulst1 = pool.submit(he);
				
				while(result.get()) {
					
				}
		}catch(IOException e) {
			System.out.println("> Desconectado");
		}catch(ExecutionException e) {
			e.printStackTrace();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	}

