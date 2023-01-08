package Juego;

import java.util.*;

public class Mesa {
	
	private int numero = 0;
	private static int numMesas = 0;
	private Jugador Banca;
	private List<Jugador> jugadores = new ArrayList<>();
	private int numeroJugadores = 0;
	private int preparado = 0;
	private int preparado2 = 0;
	private int preparado3 = 0;
	private Baraja bcrupier;
	private boolean permitir = false;
	private boolean permitir2 = false;
	private boolean permitir3 = false;
	private boolean empezada = false;
	
	public Mesa() {
		//Constructor de las Mesas.
		this.numMesas++;
		numero = numMesas;
		this.Banca = new Jugador("Banca");
		this.bcrupier = new Baraja();
		this.bcrupier.barajar();
		this.Banca.recibirCarta(bcrupier.sacarCarta());
		this.Banca.recibirCarta(bcrupier.sacarCarta());
	}
	public Jugador getBanca() {
		//Recupera el jugador "Banca".
		return this.Banca;
	}
	public int anadirJugador(Jugador g) {
		//Añadimos un jugador a la partida y devuelve el número de jugadores -1 para recorridos de la lista.
		this.jugadores.add(g);
		numeroJugadores++;
		return this.jugadores.size()-1;
	}
	public void eliminarJugador(Jugador g) {
		//Eliminamos al jugador g de la lista de jugadores.
		for(int i = 0; i<jugadores.size();i++) {
			if(jugadores.get(i).getNombre().equals(g.getNombre())) {
				jugadores.remove(jugadores.get(i));
			}
		}
		numeroJugadores--;
	}
	public List<Jugador> getJugadores(){
		//Devuelve la lista de jugadores de la mesa.
		return this.jugadores;
	}
	public  void setEmpezada(boolean empezar) {
		//Acciones auxiliares para el correcto funcionamiento del código.
		this.empezada=empezar;
	}
	public  boolean getEmpezada() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return this.empezada;
	}
	public String toString() {
		// Devuelve un String con la información de la mesa.
		String aux = "";
		for(Jugador jaux : jugadores) {
			aux = aux + jaux.getNombre() + "\n";
		}
		return "Mesa " + numero + " conformada por los jugadores: \n" + aux;
	}
	public String resultados() {
		// Devuelve todos los resultados en forma de cadena de los jugadores y de la banca.
		String aux = "Banca: " + Banca.getPuntaje() + "\n";
		for(Jugador jaux : jugadores) {
			aux = aux + jaux.getNombre() + ": " + jaux.getPuntaje() + "\n";
		}
		return aux;
	}
	public void anadircarta(int index, Carta c) {
		// Permite añadir una carta al jugador con el índice pasado por parámetro.
		jugadores.get(index).recibirCarta(c);
	}
	public void preparado() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		preparado++;
	}
	public void preparado2() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		preparado2++;
	}
	public void preparado3() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		preparado3++;
	}
	public boolean getPreparado() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return preparado >= numeroJugadores;
	}
	public boolean getPreparado2() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return preparado2 >= numeroJugadores;
	}
	public boolean getPreparado3() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return preparado3 >= numeroJugadores;
	}
	public int getnumPreparado() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return this.preparado;
	}
	public int getnumPreparado2() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return this.preparado2;
	}
	public int getnumPreparado3() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return this.preparado3;
	}
	public void resetPreparado() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		preparado=0;
	}
	public void setPreparado2(int num) {
		//Acciones auxiliares para el correcto funcionamiento del código.
		preparado = num;
	}
	public void resetPreparado2() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		preparado2=0;
	}
	public void resetPreparado3() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		preparado3=0;
	}
	public int resultado(int index) {
		// Devuelve el resultado de un jugador.
		return jugadores.get(index).getPuntaje();
	}
	public int getIndex(String nombre) {
		// Devuelve el index del jugador con el nombre pasado por parámetro.
		int cont = 0;
		for(Jugador j : jugadores) {
			if(j.getNombre().equals(nombre)) {
				return cont;
			}
			cont++;
		}
		return -1;
	}
	public Jugador getJugador(int index) {
		// Devuelve el jugador con el index pasado por parámetro.
		return jugadores.get(index);
	}
	public void juegaBanca(int index) {
		// Permite a la banca jugar al BlackJack, hasta sacar un puntaje por encima de 17.
		if(index==0) {
			this.setPermitir(false);
			while(this.Banca.getPuntaje()<17) {
				this.Banca.recibirCarta(bcrupier.sacarCarta());
			}
		}
	}
	public int getNumeroJugadores() {
		// Devuelve el número de jugadores de la mesa.
		return this.numeroJugadores;
	}
	
	public void resetJugador(Jugador aux) {
		// Resetea las cartas de los jugadores de la mesa.
		int index = this.getIndex(aux.getNombre());
		jugadores.get(index).resetMano();
		
	}
	
	public int getNumero() {
		// Devuelve el número de mesa.
		return this.numero;
	}
	public void resetBanca() {
		// Resetea las cartas de la banca.
		this.Banca.resetMano();
	}
	public void iniciarBanca() {
		// Inicia a la banca en la partida.
		if(this.Banca.getMano().size()==0 && this.getPermitir2()) {
			this.setPermitir2(false);
			
			this.bcrupier = new Baraja();
			this.bcrupier.barajar();
			if(this.Banca.getMano().size()<2) {
				this.Banca.recibirCarta(bcrupier.sacarCarta());
				this.Banca.recibirCarta(bcrupier.sacarCarta());
			}
		}
	}
	public void setPermitir(boolean b) {
		//Acciones auxiliares para el correcto funcionamiento del código.
		this.permitir = b;
	}
	public boolean getPermitir() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return this.permitir;
	}
	public void setPermitir2(boolean b) {
		//Acciones auxiliares para el correcto funcionamiento del código.
		this.permitir2 = b;
	}
	public boolean getPermitir2() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return this.permitir2;
	}
	public void setPermitir3(boolean b) {
		//Acciones auxiliares para el correcto funcionamiento del código.
		this.permitir3 = b;
	}
	public boolean getPermitir3() {
		//Acciones auxiliares para el correcto funcionamiento del código.
		return this.permitir3;
	}
}
