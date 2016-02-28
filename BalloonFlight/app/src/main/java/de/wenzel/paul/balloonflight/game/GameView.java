package de.wenzel.paul.balloonflight.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;

import de.wenzel.paul.balloonflight.game.model.DataObjects.Platform;
import de.wenzel.paul.balloonflight.game.model.GameModel;

/**
 * Die Klasse {@link GameView} ist die View für die KLasse {@link GameActivity}.
 * Über die Methode "drawGame(...)" kann man die Spielobjekte auf den Bildschirm zeichnen.
 */
public class GameView extends SurfaceView {
	/** das Model dieser View */
	private GameModel model;

	/** über den {@link SurfaceHolder} kann man sich ein Canvas (Leinwand) hohlen, auf welche man dann zeichnen kann */
	private SurfaceHolder surfaceHolder;
	
	/**
	 * Der Konstruktor der Klasse {@link GameView}.
	 * 
	 * @param context welcher Activity die View angehört, um auf System Daten zugreifen zu dürfen
	 */
	public GameView(Context context, GameModel model) {
		super(context);

		this.model = model;
		surfaceHolder = this.getHolder();
	}
	
	/**
	 * Die Methode zeichnet die übergebenen Objekte in der gewünschten Farbe auf das Canvas(Leinwand).
	 */
	public void drawGame() {
		if (surfaceHolder.getSurface().isValid()) { 
			//Canvas hohlen und zum Zeichnen bereitmachen
			Canvas canvas = surfaceHolder.lockCanvas(); 
			
			///////////////Hier wird gezeichnet/////////////////
			Paint paint = new Paint();

			// Hintergrund zeichnen
			canvas.drawColor(getResources().getColor(model.getBackgroundColorResourceId()));

            // Spieler zeichnen
			paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(model.getPlayerColorResourceId()));
			canvas.drawRect(model.getPlayer().getLeft(), model.getPlayer().getTop(), model.getPlayer().getRight(), model.getPlayer().getBottom(), paint);

			// Ballons zeichnen, wenn der Speiler einen hat
			if (model.getPlayer().getBalloon() != null) {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(getResources().getColor(model.getBallonColorResourceId()));
				canvas.drawCircle(model.getPlayer().getBalloon().getCenterX(), model.getPlayer().getBalloon().getCenterY(), model.getPlayer().getBalloon().getHeight() / 2, paint);
			}

			// Plattformen zeichnen
			Log.d("TEST", "Plattformen Anzahl: " + model.getPlayer().getTop());
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(getResources().getColor(model.getPlatformColorResourceId()));
            Iterator<Platform> platformIterator = model.getPlatformList();
            while (platformIterator.hasNext()) {
                Platform platform = platformIterator.next();
			    canvas.drawRect(platform.getLeft(), platform.getTop(), platform.getRight(), platform.getBottom(), paint);
            }
			////////////////////////////////////////////////////
			
			surfaceHolder.unlockCanvasAndPost(canvas); //Zeichnen Beenden und zum Anzeigen Freigeben
		} else {
			Log.e("SpielView", "Es kann nicht auf das Canvas gezeichnet werden!");
		}
	}
}
