package de.wenzel.paul.balloonflight.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Iterator;

import de.wenzel.paul.balloonflight.GameOverActivity;
import de.wenzel.paul.balloonflight.R;
import de.wenzel.paul.balloonflight.SettingsActivity;
import de.wenzel.paul.balloonflight.game.model.DataObjects.Balloon;
import de.wenzel.paul.balloonflight.game.model.DataObjects.Platform;
import de.wenzel.paul.balloonflight.game.model.GameModel;
import de.wenzel.paul.balloonflight.util.Rectangle;


public class GameActivity extends Activity implements Runnable, OnClickListener {

	/////////////Intent-Extras/////////////
	/** unter welchem Namen die Information über die Anzahl der Punkte an die GameOverActivity im Intent übermittelt werden sollen */
	public static final String POINTS = "points";


	/////////////Konstanten/////////////
	/** der Abstand zwischen den einzelnen Platformen (in Pixel) */
	private final int SPACING_BETWEEN_PLATFORMS = 350;
	/** die maximale Höhe einer Platform */
	private int MAX_PLATFORM_HEIGHT;
	/** die Breite einer Plattform */
    private final int PLATFORM_WIDTH = 100;


	/////////////Datenfelder/////////////
	/** das Model vom Spiel */
	private GameModel model;

	/** die "Leinwand" auf die alle Spielobjekte gezeichnet werden */
	private GameView view;
	/** die Breite des Bildschirms (in Pixel) */
	private int screenWidth;
	/** die Höhe des Bildschirms (in Pixel) */
	private int screenHeight;

	/** zum steuern des Threads, damit der z.B. pausiert und fortgesetzt werden kann */
	private Handler handler;
	/** gibt an ob das Spiel gerade läuft oder z.B. gerade pausiert ist */
	private boolean gameRunning;
	/** wann das Spiel das letzte mal neu gerendert wurde */
	private long lastFrame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Datenfelder initialisieren
		model = new GameModel(R.color.color3, R.color.color2, R.color.color5, R.color.color1);
		view = new GameView(this, model);
		
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
        MAX_PLATFORM_HEIGHT = (int)(screenHeight * 0.5);

		handler = new Handler();
		gameRunning = false;
    	lastFrame = 0;

		// die Listener für die Spielsteuerung setzen
			// den "OnClickListener" bei der View registrieren
		view.setOnClickListener(this);
		
		// die ersten Platformen erstellen
		for (int i = 0; i < screenWidth * 2; i += SPACING_BETWEEN_PLATFORMS) {
			model.addPlatform(generateNewPlatform(i));
		}
		
		// das Spielfeld anzeigen
    	setContentView(view);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// das Spiel läuft
		gameRunning = true;
		// den Zeitpunkt des letzten Frames auf die aktuelle Zeit setzen
		lastFrame = System.currentTimeMillis();
		// den Render-Thread starten
		handler.postDelayed(this, 0);	
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// das Spiel pausiert
		gameRunning = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Menü- und Zurück-Button-Ereignisse werden abgefangen
		if (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK) {
			// die Optionen öffnen
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		// sagen, das das Ereignis bearbeitet wurde
		return true;
	}

	//////////Animation//////////
	public void run() {
		// berechnen wie lange der letzte Frame her ist 
		long renderDuration = System.currentTimeMillis() - lastFrame;
		lastFrame += renderDuration;
		
		if (renderDuration > 0) {
			// FPS ausgeben
			Log.i("RENDERER", "FPS: " + 1000 / renderDuration);
			
			// prüfen, ob das Spiel vorbei ist, weil der Spieler aus dem Spielfeld rausgefallen ist
			if (model.getPlayer().getY() > screenHeight) {
				// die GameOverActivity starten, wenn der Spieler aus dem Spielfeld rausgefallen ist
				finish();
				Intent intent = new Intent(this, GameOverActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(GameActivity.POINTS, model.getPoints());
				startActivity(intent);
			} else {
				// das Spiel animieren, wenn Spieler/Ball noch am "leben" ist
				moveObjects(calculateXMovement(renderDuration), calculateYMovement(renderDuration));

				// das Spielfeld neu zeichnen
				view.drawGame();
			}
		}
		
		// wenn das Spiel noch läuft in 12ms den nächsten Frame rendern
		if (gameRunning) {
			handler.postDelayed(this, 12);
		}		
	}
	

	//////////Bedienung//////////
	@Override
	public void onClick(View view) {
		// wenn der Bildschirm berührt wird soll der Luftballon, welchen der Spieler feshält platzen
		// (natürlich nur, wenn der Spieler auch wirklich einen festhält)
		if (model.getPlayer().getBalloon() != null) {
			model.getPlayer().setBalloon(null);
		}
	}

	//////////Bewegung berechnen vom Spieler//////////
	/**
	 * Die Methode berechnet um wie viel sich der Spieler seit dem letzten zeichnen auf der X-Achse bewegt hat.
	 *
	 * @param renderDuration
	 *
	 * @return x-Bewegung vom Spieler (in Pixel)
	 */
	private float calculateXMovement(long renderDuration) {
		// die x-Bewegung ergibt sich aus der Zugkraft vom Ballon, wenn der Spieler einen festhält
		if (model.getPlayer().getBalloon() != null) {
			return (float)(renderDuration / 1000.0 * (model.getPlayer().getBalloon().getVelocityRight()));
		} else {
			return 0;
		}
	}

	/**
	 * Die Methode berechnet um wie viel sich der Spieler seit dem letzten zeichnen auf der Y-Achse bewegt hat.
	 *
	 * @param renderDuration
	 *
	 * @return y-Bewegung vom Spieler (in Pixel)
	 */
	private float calculateYMovement(long renderDuration) {
		// die y-Bewegung ergibt sich aus dem Gewicht (=Kraft die den Spieler runterzieht) vom Spieler
		// und der Zugkraft vom Ballon, wenn der Spieler einen festhält

		if (model.getPlayer().getBalloon() != null) {
			return (float)(renderDuration / 1000.0 * (model.getPlayer().getVelocityDown() - model.getPlayer().getBalloon().getVelocityUp()));
		} else {
			return (float)(renderDuration / 1000.0 * (model.getPlayer().getVelocityDown()));
		}
	}

	//////////Objekte bewegen//////////
	/**
	 * Die Methode bewegt alle Objekte, sodass es aussieht, als ob sich der Spieler bewegen würde.
	 */
	private void moveObjects(float xMovement, float yMovement) {
        // alle Plattformen bewegen
            // Liste mit neuen Plattformen
        ArrayList<Platform> newPlatforms = new ArrayList<Platform>();

          // mit einem Iterator durch alle Plattformen gehen
        Iterator<Platform> platformIterator = model.getPlatformList();
        while (platformIterator.hasNext()) {
            Platform platform = platformIterator.next();

            // die Plattform auf ihre neue Position bewegen
            platform.setX(platform.getX() - xMovement);
            // prüfen ob sich die Plattform damit außerhalb des "Sichtfeldes" ist
            if (platform.getRight() < 0) {
                // Plattform entfernen, wenn es außerhalb des "Sichtfeldes" ist
                platformIterator.remove();

                // für jede entfernte Plattform eine neue Plattform hinzufügen
                newPlatforms.add(generateNewPlatform(model.getXPositionOfLastPlatform() + SPACING_BETWEEN_PLATFORMS));

            }
        }
            // die neu erstellten Plattformen der Plattform-Liste hinzufügen
        model.addPlatforms(newPlatforms);

        // Spieler bewegen
		model.getPlayer().setY(model.getPlayer().getY() + yMovement);

        // wenn Spieler mit Plattform kollidiert, gucken, ob der Spieler auf der Plattform gelandet ist oder daneben
        Rectangle intersectingPlatform = playerIntersectsPlatform(model.getPlatformList(), model.getPlayer());
        if (intersectingPlatform != null) {
            // gucken, ob der Spieler vor dem Frame noch höher als die Plattform war, oder nicht
            if (intersectingPlatform.getY() < model.getPlayer().getBottom() - yMovement) {
                // Spieler ist auf der Plattform gelandet
                model.getPlayer().setY(intersectingPlatform.getTop() - model.getPlayer().getHeight());
                model.getPlayer().setBalloon(new Balloon(model.getPlayer().getRight() + 30, model.getPlayer().getTop() - 20, 25, 25, 100, 300));
            } else {
                // alle Plattformen zurück bewegen
                Iterator<Platform> platformIterator2 = model.getPlatformList();
                while (platformIterator2.hasNext()) {
                    Platform platform = platformIterator2.next();

                    // die Plattform auf ihre neue Position bewegen
                    platform.setX(platform.getX() + (xMovement - (intersectingPlatform.getLeft() - model.getPlayer().getX())));
                }
                // Spieler ist neben der Plattform gelandet
                model.getPlayer().setX(intersectingPlatform.getLeft());

            }
        }

        // Ballon vom Spieler bewegen
        if (model.getPlayer().getBalloon() != null) {
            model.getPlayer().getBalloon().setY(model.getPlayer().getBalloon().getY() + yMovement);
        }
	}

	//////////Kollisionen überprüfen//////////
	/**
	 * Die Methode gibt aus, ob ein Objekt mit einem Elemnet aus der Liste von Objekten kollidiert ist.
	 *
	 * @return das {@link Rectangle} aus der Liste mit welchem das Objekt kollidiert oder null,
	 * 		wenn das Objekt mit keinem Objekt aus der Liste kollidiert
	 */
	private Rectangle playerIntersectsPlatform(Iterator<Platform> iterator, Rectangle object) {
		while (iterator.hasNext()) {
			Rectangle rectangle = iterator.next();
			if (rectangle.intersects(object)) {
				return rectangle;
			}
		}

		return null;
	}

	//////////Sonstiges//////////
	/**
	 * Die Methode generiert eine neue Plattform.
	 * Die linke Seite der Platform befindet sich dabei an der gewünschten X-Position.
	 *
	 * @param x die gewünschte X-Position der linken Seite der Platform
	 * @return die neue Platform
	 */
	private Platform generateNewPlatform(float x) {
		// zufällig bestimmen, wie hoch die Platform sein soll
		int platformHeight = (int)(Math.random() * MAX_PLATFORM_HEIGHT);

		// das neue Hindernis zurückgeben
		return new Platform(x, (float)screenHeight - platformHeight, PLATFORM_WIDTH, platformHeight);
	}
}