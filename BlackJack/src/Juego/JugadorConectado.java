package Juego;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

class JugadorConectado implements Runnable {
	  private Socket socket;
	  private static ArrayList<Jugador> jugadores;
	  private String nombre;
	  private Jugador jugador;
	  private Baraja baraja;
	  private static boolean juegoEnCurso;
	  private static List<Mesa> mesas = new ArrayList<>();
	  private Scanner entrada;
	  private PrintWriter salida;
	  private int opcionMesa;
	  private double fichas;
	  private int ronda = 0;

	  public JugadorConectado(Socket socket) {
		 
		    this.socket = socket;
		    fichas = 100;
		 
	  }

	  public void run() {
		  // Aquí atendemos las peticiones de los cliente.
		  boolean opcionValida = false;
		  Mesa mesaBlackJack = null;
		  boolean salir = false;
		  boolean espera = true;
		  boolean jugar = false;
		  boolean empezada = false;
	      
	      try {
	    	  
	    	  entrada = new Scanner(socket.getInputStream());
			  salida = new PrintWriter(socket.getOutputStream(), true);
		      salida.println("Indique su nombre: ");
		      nombre = entrada.nextLine();
		      this.jugador = new Jugador(nombre);							// Obtiene el nombre del jugador y crea un jugador.
		      while(!salir) {
		    	  jugar = false;
		    	  espera = true;
		    	  empezada = false;
			      salida.println("Bienvenido al juego de blackjack, " + nombre + "!" + "\n");
			      int opcion = 0;																	//Opción para crear una mesa.
			      if(mesas.isEmpty()) {
			    	  salida.println("Deseas crear una mesa? ");
			    	  salida.println("Sí.");
			    	  salida.println("No.");
			    	  String aa = entrada.nextLine();
			    	  if(aa.startsWith("Sí") || aa.startsWith("Si") || aa.startsWith("si") || aa.startsWith("sí") || aa.startsWith("SI")) {
			    		  Mesa m = new Mesa();
			    		  mesaBlackJack = m;
			    		  mesaBlackJack.anadirJugador(this.jugador);
			    		  mesas.add(mesaBlackJack);
			    		  opcion = 0;
			    		  espera = true;
			    		  jugar = true;
			    	  }else {
			    		  espera = false;
			    	  }
			      }else {
			    	  
			    	  while(!opcionValida) {
			    		  int cont = 0;
				    	  salida.println("Seleccione una mesa o cree una propia." + "\n");
				    	  for (Mesa maux : mesas) {
				    		  salida.println(cont + ". " + maux.toString());
				    		  cont++;
				    	  }
				    	  salida.println(cont + ". Crear mesa. \n");
				    	  salida.println("Seleccione una: ");
				    	  try {
				    		  opcion = Integer.parseInt(entrada.nextLine());
				    		  if(opcion<= mesas.size() && opcion>=0) {					//Opción para crear una mesa.
				    			  opcionValida = true;
				    			  if(opcion == mesas.size()) {
				    	    		  Mesa m = new Mesa();
				    	    		  mesaBlackJack = m;
				    	    		  mesaBlackJack.anadirJugador(this.jugador);
				    	    		  mesas.add(m);
				    	    		  jugar = true;
				    	    		  
				    	    		  espera = true;
				    			  }else {											//Opción para seleccionar una mesa ya creada, que no permitirá que se una el cliente si el juego está en curso o la mesa está completa.
				    				  mesaBlackJack = mesas.get(opcion);
				    				  if(mesaBlackJack.getEmpezada()) {
				    					  opcionValida = false;
				    					  salida.println("Mesa con partida en curso, no puedes unirte.");
				    					  espera = false;
				    				  }else {
				    					  if(mesaBlackJack.getNumeroJugadores()>=4) {
				    						  espera = false;
				    						  opcionValida=false;
				    						  salida.println("Mesa completa, no puedes unirte.");
				    					  }else {
				    						  mesas.get(opcion).anadirJugador(this.jugador);
				    						  jugar = true;
				    					  }
				    			  	  }
				    				  
				    			  }
				    		  }
				    	  }catch(NumberFormatException e) {
				    		  salida.println("Opción no válida");
				    	  }
			    	  }
			    	  
			      }
			      opcionMesa = opcion;
			      
			      boolean listo = false;
			    while(espera) {											//Espera a que todos los jugadores estén listos para empezar la partida.
			    	if(!listo) {
			    		salida.println("Escriba 'empezar' cuando esté listo");
			    		if(entrada.nextLine().startsWith("empezar")) {
			    			mesaBlackJack.preparado();
			    			listo = true;
			    			salida.println("Esperando a los demás usuarios.");
			    			salida.println("Listos: " + mesaBlackJack.getnumPreparado() +"/" + mesaBlackJack.getNumeroJugadores());
			    		}
			    	}
			    	if(mesaBlackJack.getPreparado()) {
			    		espera = false;
			    		mesaBlackJack.setEmpezada(true);
			    	}
			    }
			    if(jugar) {
				    if(mesaBlackJack!=null) {  
				    	juega(mesaBlackJack);
				    }
			    }
			    salida.println("Desea salir del BlackJack online? (si/no)");			//Desconexión normal del cliente.
			    String mensaje = entrada.nextLine();
			    if(mensaje.startsWith("si")) {
			    	salida.println("¡Hasta pronto!");
			    	salida.println("Desconectando del BlackJack online...");
			    	salida.println("Escriba 'desconectar'");
			    	salida.println("desconectar");
			    	salir = true;
			    }
		      }
	      }catch(IOException e) {
	    	  e.printStackTrace();
	    	  
	      }finally {
	    	  try {
	    		  if(socket!=null)socket.close();
	    	  }catch(IOException e) {
	    		  e.printStackTrace();
	    	  }
	      }
	    
	  }
	  
	  
	  public void juega(Mesa mesa) {
		  // Acción que permite a cada jugador jugar en la mesa escogida, las rondas que él desee.
		  Jugador auxiliar = null;
		  try {
			  boolean otra = true;
			  salida.println("Bienvenido a la mesa " + mesa.getNumero());
			  Thread.sleep(Calendar.SECOND,5);
			  while(otra) {
				  mesa.setPermitir(false);
				  mesa.setPermitir2(true);
				  mesa.resetJugador(this.jugador); 				// Permite resetear las cartas de los jugadores de la mesa.
				  Baraja bmesa = new Baraja();			// Genera la baraja para los jugadores de la mesa.
				  bmesa.barajar();
				  
				  mesa.resetPreparado();
				  boolean espera = false;
				  boolean piden = true;
				  boolean blackjack = false;
				  String mensaje;
				  Jugador j2 = null;
				  int index;
				  int apuesta = 0;
				  index  = mesa.getIndex(nombre);
				  auxiliar = mesa.getJugador(index);
				  mesa.resetPreparado2();
				  int p=0;
				  mesa.iniciarBanca();
				  
				  while(piden) {														// El juego espera para seguir hasta que todos los jugadores de la mesa hayan apostado y se les haya mostrado sus dos primeras cartas.
					  //Esta parte del código permite apostar y ver las cartas que le han tocado al jugador.
					  if(p == mesa.getNumeroJugadores() || mesa.getPermitir()) {
						  piden = false;
						  salida.println("Todos los jugadores listos");
					  }else {
						  if(!espera) {
							  Carta c = bmesa.sacarCarta();
							  Carta c1 = bmesa.sacarCarta();
							  mesa.anadircarta(index, c);
							  mesa.anadircarta(index, c1);
							  salida.println("Fichas disponibles: " + this.fichas);
							  while(apuesta<=0 || apuesta>this.fichas) {
								  salida.println("Indique la cantidad a apostar.");
								  try {
									  apuesta = Integer.parseInt(entrada.nextLine());
								  }catch(NumberFormatException e) {
									  salida.println("Introduce un entero!!");
								  }
							  }
							  this.fichas = this.fichas - apuesta;
							  salida.println("Has apostado: " + apuesta + " fichas.");
							  salida.println("Tus cartas son: " + c.getValor() + "    " + c1.getValor() + ".");
							  salida.println("Tienes una puntuación total de " + auxiliar.getPuntaje());
							  espera = true;
							  mesa.preparado2();
							  salida.println("Listos: " + mesa.getnumPreparado2() +"/" + mesa.getNumeroJugadores());
						  }
					  }
					  p = mesa.getnumPreparado2();		//Solución al problema que me daba de jugadores atascados en este bucle.
				  }
				  
				  mesa.setPermitir(true);
				  
				  salida.println("La Banca tiene las cartas: " + mesa.getBanca().getMano().get(0).getValor() + "   Y otra aún no visible");
				  
				  if(mesa.getBanca().getPuntaje()==21) {
					  blackjack = true;
					  salida.println("BlackJack de la banca!");
				  }
				  
				  int p2 = 0;
				  
				  mesa.setPermitir2(false);
				  
				  // Si la banca saca BlackJack (Puntuación de 21), es victoria directa para la banca (A menos que otro jugador saque directamente BlackJack también).
				  
				  if(!blackjack) {
					  espera = false;
					  piden = true;
					  mesa.resetPreparado3();
					  while(piden) {						//Espera a que todos los jugadores hayan terminado de recibir las cartas.
						  //Esta parte del código permite a los jugadores obtener más cartas o no.
						  if(p2 == mesa.getNumeroJugadores() || mesa.getPermitir2()) {
							  piden = false;
							  salida.println("Le toca a la banca. ");
						  }else {
							  if(!espera) {
								  salida.println("Quieres otra carta? 'si/no'");
								  mensaje = entrada.nextLine();
								  if(mensaje.equals("si")) {
									  Carta caux2 = bmesa.sacarCarta();
									  mesa.anadircarta(index, caux2);
									  j2 = mesa.getJugador(index);
									  salida.println("Has sacado " + caux2.toString());
									  salida.println("Tu puntuacion es de " + auxiliar.getPuntaje());
									  if(j2.getPuntaje() > 21) {
										  salida.println("Has perdido.	Tu puntuacion es de " + j2.getPuntaje());
										  espera = true;
										  mesa.preparado3();
										  salida.println("Listos: " + mesa.getnumPreparado3() +"/" + mesa.getNumeroJugadores());
										  salida.println("Esperando a los demás usuarios.");
									  }
									  if(j2.getPuntaje()==21) {
										  salida.println("¡¡¡Blackjack!!!");
										  mesa.preparado3();
										  espera = true;
										  salida.println("Listos: " + mesa.getnumPreparado3() +"/" + mesa.getNumeroJugadores());
										  salida.println("Esperando a los demás usuarios.");
									  }
								  }else if(mensaje.equals("no")) {
									  espera = true;
									  mesa.preparado3();
									  salida.println("Listos: " + mesa.getnumPreparado3() +"/" + mesa.getNumeroJugadores());
									  salida.println("Esperando a los demás usuarios...");
									  salida.println("La segunda carta de la banca era: " + mesa.getBanca().getMano().get(1).getValor());
									  salida.println("La banca tiene un total de : " + mesa.getBanca().getPuntaje());
								  }
							  }
						  }
						  p2 = mesa.getnumPreparado3();
					  }
					  mesa.setPermitir2(true);
					  mesa.juegaBanca(index);
					  
					  //Todo lo que sigue es para ver quién ha ganado la ronda.
					  
					  Jugador ganador = new Jugador("ganador");
					  salida.println("Resultados: ");
					  for(Jugador jfinal : mesa.getJugadores()) {
						  salida.println("Jugador " + jfinal.getNombre() + " " + jfinal.getPuntaje());
						  if(jfinal.getPuntaje()> ganador.getPuntaje() && jfinal.getPuntaje()<=21) {
							  ganador = jfinal;
						  }
					  }
					  salida.println("La banca ha obtenido un: " + mesa.getBanca().getPuntaje());
					  
					  
					  
					  if(mesa.getBanca().getPuntaje() == ganador.getPuntaje()) {
						  salida.println("Empate con la banca.");
						  
						  
					  }else {
						  if(mesa.getBanca().getPuntaje()>ganador.getPuntaje() && mesa.getBanca().getPuntaje()<=21) {
							  ganador = mesa.getBanca();
						  }
					  
						  if(ganador.getNombre().equals(mesa.getBanca().getNombre())) {
							  salida.println("Ha ganado la banca.");
						  }else {
							  if(ganador.getNombre().equals("ganador")) {
								  salida.println("No ha ganado nadie");
							  }else {
								  salida.println("Ha ganado el jugador " + ganador.getNombre() + " con un " + ganador.getPuntaje());
							  }
						  }
					  }					// Esta parte del código es para ver si un jugador gana o pierde fichas y cuántas.
					  if(auxiliar.getPuntaje() == mesa.getBanca().getPuntaje() && mesa.getBanca().getPuntaje()<=21) {
							 this.fichas = this.fichas + apuesta;
							 salida.println("Ganas " + apuesta);
						 }else {
							if(mesa.getBanca().getPuntaje()<=21) {
								if(auxiliar.getPuntaje()>mesa.getBanca().getPuntaje()) {
									if(auxiliar.getPuntaje()<21) {
										this.fichas = this.fichas + apuesta*2;
										salida.println("Ganas " + apuesta*2);
									}else if(auxiliar.getPuntaje() == 21) {
										this.fichas = this.fichas + apuesta*2.5;
										salida.println("Ganas " + apuesta*2.5);
									}else {
										salida.println("Has perdido " + apuesta);
									}
								}else {
									salida.println("Has perdido " + apuesta);
								}
							}else {
								if(auxiliar.getPuntaje()<=21) {
									if(auxiliar.getPuntaje()<21) {
										this.fichas = this.fichas + apuesta*2;
										salida.println("Ganas " + apuesta*2);
									}if(auxiliar.getPuntaje()==21) {
										this.fichas = this.fichas + apuesta*2.5;
										salida.println("Ganas " + apuesta*2.5);
									}
								}
							}
							
						 }
				  }else {														// Lo mismo que en los dos últimos extractos de código pero para cuando la banca saca BlackJack directo.
					  boolean alguien = false;
					  for(Jugador jaux2 : mesa.getJugadores()) {
						  if(jaux2.getPuntaje() == 21) {
							  alguien = true;
						  }
					  }
					  if(alguien) {
						  salida.println("Empate con la banca con blackjack.");
						  if(auxiliar.getPuntaje() == 21) {
							  salida.println("Tú también tienes blackjack");
							  this.fichas = this.fichas + apuesta*2.5;
							  salida.println("Ganas " + apuesta*2.5);
						  }
					  }else {
						  salida.println("Ha ganado la banca con blackjack.");
					  }
				  }
				  
				  boolean preparados = false;
				  mesa.resetPreparado();
				  int cont = mesa.getNumeroJugadores();
				  boolean mostrar = true;
				  mesa.setPermitir3(false);
				  int p3 = 0;
				  while(!preparados) {				//Permite hacer al programa que espere hasta que todos los jugadores hayan decidido si seguir jugando o no.
					  mesa.setEmpezada(false);
					  if(p3 == mesa.getNumeroJugadores() || mesa.getPermitir3()) {
						  mesa.setEmpezada(true);
						  preparados = true;
						  mesa.setPermitir3(true);
						  
					  }else {
						  if(mostrar) {
							  salida.println("Ahora tienes un total de: " + this.fichas + " fichas.");
							  salida.println("Desea jugar otra vez? (si/no)");
							  String mensaje1 = entrada.nextLine();
							  if(mensaje1.startsWith("no")) {
								  mesa.preparado();
								  otra = false;
								  salida.println("Te retiras con " + this.fichas + " fichas.");
								  salida.println("Desconectando de la mesa " + mesa.getNumero() + "...");
							  }else {
								  mesa.preparado();
								  salida.println("Esperando a los demás jugadores.");
								  otra = true;
								  mesa.setPermitir2(true);
							  }
							  salida.println("Listos: " + mesa.getnumPreparado() +"/" + mesa.getNumeroJugadores());
							  mostrar = false;
						  }
						  mesa.resetBanca();												
					  }
					  
					  p3 = mesa.getnumPreparado();
				  }
				  
				  mesa.setPermitir3(true);
			  }
		  }catch(InterruptedException e) {
			  mesa.eliminarJugador(auxiliar);
			  if(mesa.getNumeroJugadores()==0) {
				  mesas.remove(opcionMesa);
			  }
		  }finally {
			  mesa.eliminarJugador(auxiliar);
			  if(mesa.getNumeroJugadores()==0) {
				  mesas.remove(opcionMesa);
			  }
		  }
		  
	  }
}
