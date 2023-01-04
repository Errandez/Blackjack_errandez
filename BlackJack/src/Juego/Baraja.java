package Juego;

import java.util.*;

public class Baraja {
	  private ArrayList<Carta> cartas;

	  public Baraja() {
	    // Inicializa la lista de cartas con todas las cartas del juego
	    cartas = new ArrayList<Carta>();
	    for (int palo = 0; palo <= 3; palo++) {
	      for (int valor = 1; valor <= 13; valor++) {
	        cartas.add(new Carta(valor, palo));
	      }
	    }
	  }

	  public void barajar() {
	    // Baraja las cartas
	    Collections.shuffle(cartas);
	  }

	  public Carta sacarCarta() {
	    // Saca la primera carta de la baraja
	    return cartas.remove(0);
	  }
	}
