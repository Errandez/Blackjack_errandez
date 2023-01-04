package Juego;

import java.util.*;

public class Jugador {
	  private String nombre;
	  private ArrayList<Carta> mano;

	  public Jugador(String nombre) {
	    this.nombre = nombre;
	    mano = new ArrayList<Carta>();
	  }

	  public void recibirCarta(Carta carta) {
	    // Añade una carta a la mano del jugador.
	    mano.add(carta);
	  }

	  public ArrayList<Carta> getMano() {
	    // Devuelve la mano del jugador.
	    return mano;
	  }

	  public int getPuntaje() {
	    // Calcula y devuelve el puntaje total de la mano del jugador.
	    int puntaje = 0;
	    int ases = 0;
	    int figuras = 0;
	    int cont = 0;
	    int tam = mano.size();
	    for (int i = 0; i<tam; i++) {
	    	Carta carta = mano.get(i);
	      puntaje += carta.getValor();
	      if (carta.getValor() == 1) {
	        ases++;
	      }
	      if(carta.getValor() == 11) {
	    	  puntaje--;
	      }
	      if(carta.getValor() == 12) {
	    	  puntaje = puntaje -2;
	      }
	    }
	    while (ases > 0 && puntaje + 10 <= 21) {
	      puntaje += 10;
	      ases--;
	    }
	    return puntaje;
	  }
	  public String getNombre() {
		  return this.nombre;
	  }
	  public void resetMano() {
		  mano.clear();
	  }
	}
