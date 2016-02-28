package de.wenzel.paul.balloonflight.game.model.DataObjects;


import de.wenzel.paul.balloonflight.util.Rectangle;

public class Player extends Rectangle {

    /** Kraft mit welcher der Ballon nach unten zieht (in Pixel/Sekunde) */
    private float velocityDown;

    /** der {@link Balloon} an welchen sich der {@link Player} festhält (null, wenn sich der Spieler gerade an keinem Ballon festhät!) */
    private Balloon balloon;

    /**
     * Der Konstruktor der Klasse {@link Rectangle}.
     *
     * @param x      die x-Koordinate der oberen-linken-Ecke des Rechtecks
     * @param y      die Y-Koordinate der oberen-linken-Ecke des Rechtecks
     * @param width  die Breite des Rechteckes (der Wert muss >= 0 sein!)
     * @param height die Höhe des Rechteckes (der Wert muss >= 0 sein!)
     * @param velocityDown Kraft mit welcher der Ballon nach unten zieht (in Pixel/Sekunde)
     */
    public Player(float x, float y, float width, float height, float velocityDown) {
        super(x, y, width, height);

        this. velocityDown = velocityDown;
        balloon = null;
    }

    public Balloon getBalloon() {
        return balloon;
    }

    public void setBalloon(Balloon balloon) {
        this.balloon = balloon;
    }

    public float getVelocityDown() {
        return velocityDown;
    }
}
