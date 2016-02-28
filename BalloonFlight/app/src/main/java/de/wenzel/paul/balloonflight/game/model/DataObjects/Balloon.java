package de.wenzel.paul.balloonflight.game.model.DataObjects;


import de.wenzel.paul.balloonflight.util.Rectangle;

public class Balloon extends Rectangle {

    /** Kraft mit welcher der Ballon nach rechts zieht (in Pixel/Sekunde) */
    private float velocityRight;

    /** Kraft mit welcher der Ballon nach oben zieht (in Pixel/Sekunde) */
    private float velocityUp;

    /**
     * Der Konstruktor der Klasse {@link Rectangle}.
     *
     * @param x      die x-Koordinate der oberen-linken-Ecke des Rechtecks
     * @param y      die Y-Koordinate der oberen-linken-Ecke des Rechtecks
     * @param width  die Breite des Rechteckes (der Wert muss >= 0 sein!)
     * @param height die Höhe des Rechteckes (der Wert muss >= 0 sein!)
     * @param velocityRight Kraft mit welcher der Ballon nach rechts zieht (in Pixel/Sekunde)
     * @param velocityUp Kraft mit welcher der Ballon nach oben zieht (in Pixel/Sekunde)
     */
    public Balloon(float x, float y, float width, float height, float velocityRight, float velocityUp) {
        super(x, y, width, height);

        this.velocityRight = velocityRight;
        this.velocityUp = velocityUp;
    }


    public float getVelocityRight() {
        return velocityRight;
    }

    public float getVelocityUp() {
        return velocityUp;
    }
}
