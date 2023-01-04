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
		this.numMesas++;
		numero = numMesas;
		this.Banca = new Jugador("Banca");
		this.bcrupier = new Baraja();
		this.bcrupier.barajar();
		this.Banca.recibirCarta(bcrupier.sacarCarta());
		this.Banca.recibirCarta(bcrupier.sacarCarta());
	}
	public Jugador getBanca() {
		return this.Banca;
	}
	public int anadirJugador(Jugador g) {
		this.jugadores.add(g);
		numeroJugadores++;
		return this.jugadores.size()-1;
	}
	public void eliminarJugador(Jugador g) {
		for(int i = 0; i<jugadores.size();i++) {
			if(jugadores.get(i).getNombre().equals(g.getNombre())) {
				jugadores.remove(jugadores.get(i));
			}
		}
		numeroJugadores--;
	}
	public List<Jugador> getJugadores(){
		return this.jugadores;
	}
	public  void setEmpezada(boolean empezar) {
		this.empezada=empezar;
	}
	public  boolean getEmpezada() {
		return this.empezada;
	}
	public String toString() {
		String aux = "";
		for(Jugador jaux : jugadores) {
			aux = aux + jaux.getNombre() + "\n";
		}
		return "Mesa " + numero + " conformada por los jugadores: \n" + aux;
	}
	public String resultados() {
		String aux = "Banca: " + Banca.getPuntaje() + "\n";
		for(Jugador jaux : jugadores) {
			aux = aux + jaux.getNombre() + ": " + jaux.getPuntaje() + "\n";
		}
		return aux;
	}
	public void anadircarta(int index, Carta c) {
		jugadores.get(index).recibirCarta(c);
	}
	public void preparado() {
		preparado++;
	}
	public void preparado2() {
		preparado2++;
	}
	public void preparado3() {
		preparado3++;
	}
	public boolean getPreparado() {
		return preparado >= numeroJugadores;
	}
	public boolean getPreparado2() {
		return preparado2 >= numeroJugadores;
	}
	public boolean getPreparado3() {
		return preparado3 >= numeroJugadores;
	}
	public int getnumPreparado() {
		return this.preparado;
	}
	public int getnumPreparado2() {
		return this.preparado2;
	}
	public int getnumPreparado3() {
		return this.preparado3;
	}
	public void resetPreparado() {
		preparado=0;
	}
	public void setPreparado2(int num) {
		preparado = num;
	}
	public void resetPreparado2() {
		preparado2=0;
	}
	public void resetPreparado3() {
		preparado3=0;
	}
	public int resultado(int index) {
		return jugadores.get(index).getPuntaje();
	}
	public int getIndex(String nombre) {
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
		return jugadores.get(index);
	}
	public void juegaBanca() {
		while(this.Banca.getPuntaje()<17) {
			this.Banca.recibirCarta(bcrupier.sacarCarta());
		}
	}
	public int getNumeroJugadores() {
		return this.numeroJugadores;
	}
	public void resetJugadores() {
		if(this.permitir) {
			for(Jugador j : jugadores) {
				j.resetMano();
			}
			this.Banca.resetMano();	
			this.permitir = false;
		}
	}
	public int getNumero() {
		return this.numero;
	}
	public void iniciarBanca() {
		if(this.Banca.getMano().size()==0 && this.permitir2) {
			this.Banca.resetMano();
			this.bcrupier = new Baraja();
			this.bcrupier.barajar();
			if(this.Banca.getMano().size()<2) {
				this.Banca.recibirCarta(bcrupier.sacarCarta());
				this.Banca.recibirCarta(bcrupier.sacarCarta());
			}
			this.permitir2 = false;
		}
	}
	public void setPermitir(boolean b) {
		this.permitir = b;
	}
	public boolean getPermitir() {
		return this.permitir;
	}
	public void setPermitir2(boolean b) {
		this.permitir2 = b;
	}
	public boolean getPermitir2() {
		return this.permitir2;
	}
	public void setPermitir3(boolean b) {
		this.permitir3 = b;
	}
	public boolean getPermitir3() {
		return this.permitir3;
	}
}
