package Juego;

import java.util.*;

public class Carta {
	  private int valor;
	  private int palo;

	  public Carta(int valor, int palo) {
	    this.valor = valor;
	    this.palo = palo;
	  }

	  public int getValor() {
	    // Devuelve el valor de la carta
	    if (valor > 10) {
	      return 10;
	    } else {
	      return valor;
	    }
	  }

	  public String toString() {
	    // Devuelve una representación en forma de cadena de la carta
	    String valorCarta = "";
	    if (valor == 1) {
	      valorCarta = "As";
	    } else if (valor == 11) {
	      valorCarta = "J";
	    } else if (valor == 12) {
	      valorCarta = "Q";
	    } else if (valor == 13) {
	      valorCarta = "K";
	    } else {
	      valorCarta = Integer.toString(valor);
	    }
	    String paloCarta = "";
	    if (palo == 0) {
	      paloCarta = "Corazones";
	    } else if (palo == 1) {
	      paloCarta = "Diamantes";
	    } else if (palo == 2) {
	      paloCarta = "Tréboles";
	    } else if (palo == 3) {
	      paloCarta = "Espadas";
	  }
	    return valorCarta + " " + paloCarta;
	}
}