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
	  private static Jugador banca;
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
		boolean opcionValida = false;
		Mesa mesaBlackJack = null;
		boolean salir = false;
		boolean espera = true;
		boolean empezada = false;
	      // Obtiene el nombre del jugador y lo añade a la lista de jugadores
	      try {
	    	  entrada = new Scanner(socket.getInputStream());
			  salida = new PrintWriter(socket.getOutputStream(), true);
		      salida.println("Indique su nombre: ");
		      nombre = entrada.nextLine();
		      this.jugador = new Jugador(nombre);
		      while(!salir) {
			      salida.println("Bienvenido al juego de blackjack, " + nombre + "!" + "\n");
			      int opcion = 0;
			      if(mesas.isEmpty()) {
			    	  salida.println("Deseas crear una mesa? ");
			    	  salida.println("Sí.");
			    	  salida.println("No.");
			    	  String aa = entrada.nextLine();
			    	  if(aa.startsWith("Sí") || aa.startsWith("Si") || aa.startsWith("si") || aa.startsWith("sí") || aa.startsWith("SI")) {
			    		  Mesa m = new Mesa();
			    		  mesaBlackJack = m;
			    		  mesaBlackJack.anadirJugador(new Jugador(nombre));
			    		  mesas.add(mesaBlackJack);
			    		  opcion = 0;
			    	  }else {
			    		  espera = false;
			    	  }
			      }else {
			    	  
			    	  while(!opcionValida) {
			    		  int cont = 0;
				    	  salida.println("Estan son las mesas disponibles." + "\n");
				    	  for (Mesa maux : mesas) {
				    		  salida.println(cont + ". " + maux.toString());
				    		  cont++;
				    	  }
				    	  salida.println(cont + ". Crear mesa. \n");
				    	  salida.println("Seleccione una: ");
				    	  try {
				    		  opcion = Integer.parseInt(entrada.nextLine());
				    		  if(opcion<= mesas.size() && opcion>=0) {
				    			  opcionValida = true;
				    			  if(opcion == mesas.size()) {
				    	    		  Mesa m = new Mesa();
				    	    		  mesaBlackJack = m;
				    	    		  mesaBlackJack.anadirJugador(new Jugador(nombre));
				    	    		  
				    	    		  mesas.add(m);
				    			  }else {
				    				  mesaBlackJack = mesas.get(opcion);
				    				  if(mesaBlackJack.getEmpezada()) {
				    					  opcionValida = false;
				    					  salida.println("Mesa con partida en curso, no puedes unirte.");
				    				  }else {
				    					  mesas.get(opcion).anadirJugador(jugador);
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
			    while(espera) {
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
			    if(mesaBlackJack!=null) {  
			    	juega(mesaBlackJack);
			    }
			    
			    salida.println("Desea salir del BlackJack online? (si/no)");
			    String mensaje = entrada.nextLine();
			    if(mensaje.startsWith("si")) {
			    	salida.println("¡Hasta pronto!");
			    	salida.println("Desconectando del BlackJack online...");
			    	salida.println("Escriba 'desconectar'");
			    	entrada.nextLine();
			    	salir = true;
			    }
		      }
	      }catch(IOException e) {
	    	  e.printStackTrace();
	    	  
	      }finally {
	    	  try {
	    		  if(this.socket != null) this.socket.close();
	    		  if(mesaBlackJack!=null) {
		    		  mesaBlackJack.eliminarJugador(this.jugador);
		    		  if(mesaBlackJack.getNumeroJugadores() == 0) {
		    			  mesas.remove(opcionMesa);
		    		  }
		    	  }
	    	  }catch(IOException e) {
	    		  e.printStackTrace();
	    	  }
	      }
	    
	  }
	  
	  
	  public void juega(Mesa mesa) {
		  Jugador auxiliar = null;
		  try {
			  boolean otra = true;
			  salida.println("Bienvenido a la mesa " + mesa.getNumero());
			  Thread.sleep(Calendar.SECOND,5);
			  while(otra) {
				  mesa.setPermitir(false);
				  mesa.setPermitir2(false);
				  mesa.setPermitir3(false);
				  mesa.resetJugadores();
				  mesa.iniciarBanca();
				  this.banca = mesa.getBanca();
				  Baraja bmesa = new Baraja();
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
				  while(piden) {
					  if(mesa.getPreparado2() || mesa.getPermitir()) {
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
				  }
				  salida.println("Ha Salido"); 							/////////////////////////////////////////////////////////////////////////////////
				  mesa.setPermitir(true);
				  Thread.sleep(Calendar.SECOND,5);
				  
				  salida.println("La Banca tiene las cartas: " + mesa.getBanca().getMano().get(0).getValor() + "   Y otra aún no visible");
				  
				  if(this.banca.getPuntaje()==21) {
					  blackjack = true;
				  }
				  
				  if(!blackjack) {
					  espera = false;
					  piden = true;
					  mesa.resetPreparado3();
					  while(piden) {
						  if(mesa.getPreparado3() || mesa.getPermitir2()) {
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
					  }
					  mesa.setPermitir2(true);
					  mesa.juegaBanca();
					  
					  Jugador ganador = new Jugador("ganador");
					  salida.println("Resultados: ");
					  for(Jugador jfinal : mesa.getJugadores()) {
						  salida.println("Jugador " + jfinal.getNombre() + " " + jfinal.getPuntaje());
						  if(jfinal.getPuntaje()> ganador.getPuntaje() && jfinal.getPuntaje()<=21) {
							  ganador = jfinal;
						  }
					  }
					  salida.println("La banca ha obtenido un: " + this.banca.getPuntaje());
					  
					  if(mesa.getBanca().getPuntaje() == ganador.getPuntaje()) {
						  salida.println("Empate con la banca.");
						  
						  
					  }else {
						  if(mesa.getBanca().getPuntaje()>ganador.getPuntaje() && mesa.getBanca().getPuntaje()<=21) {
							  ganador = mesa.getBanca();
						  }
					  
						  if(ganador.getNombre().equals(this.banca.getNombre())) {
							  salida.println("Ha ganado la banca.");
						  }else {
							  if(ganador.getNombre().equals("ganador")) {
								  salida.println("No ha ganado nadie");
							  }else {
								  salida.println("Ha ganado el jugador " + ganador.getNombre() + " con un " + ganador.getPuntaje());
							  }
						  }
					  }
					  if(auxiliar.getPuntaje() == this.banca.getPuntaje() && this.banca.getPuntaje()<=21) {
							 this.fichas = this.fichas + apuesta;
							 salida.println("Ganas " + apuesta);
						 }else {
							if(this.banca.getPuntaje()<=21) {
								if(auxiliar.getPuntaje()>this.banca.getPuntaje()) {
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
				  }else {
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
				  while(!preparados) {
					  mesa.setEmpezada(false);
					  if(mesa.getPreparado() || mesa.getPermitir3()) {
						  mesa.setEmpezada(true);
						  preparados = true;
						  mesa.setPermitir(true);
						  mesa.setPermitir2(true);
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
								  mesa.eliminarJugador(auxiliar);
								  if(mesa.getNumeroJugadores()==0) {
									  mesas.remove(opcionMesa);
								  }
								  return;
							  }else {
								  mesa.preparado();
								  salida.println("Esperando a los demás jugadores.");
								  otra = true;
								  mesa.setPermitir2(true);
							  }
							  salida.println("Listos: " + mesa.getnumPreparado() +"/" + mesa.getNumeroJugadores());
							  mostrar = false;
						  }
					  }
				  }
				  mesa.setPermitir3(true);
			  }
		  }catch(InterruptedException e) {
			  mesa.eliminarJugador(auxiliar);
			  if(mesa.getNumeroJugadores()==0) {
				  mesas.remove(opcionMesa);
			  }
		  }
		  
	  }
}
