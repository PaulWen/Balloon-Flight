package de.wenzel.paul.balloonflight.game.model.DataObjects;

import de.wenzel.paul.balloonflight.util.Rectangle;

/**
 * Die Klasse {@link Platform} stellt ein Plattform da, auf welcher die Figur landen soll.
 */
public class Platform extends Rectangle {

	/**
	 * Der Konstruktor der Klasse {@link Rectangle}.
	 *
	 * @param x      die x-Koordinate der oberen-linken-Ecke des Rechtecks
	 * @param y      die Y-Koordinate der oberen-linken-Ecke des Rechtecks
	 * @param width  die Breite des Rechteckes (der Wert muss >= 0 sein!)
	 * @param height die Höhe des Rechteckes (der Wert muss >= 0 sein!)
	 */
	public Platform(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
}
