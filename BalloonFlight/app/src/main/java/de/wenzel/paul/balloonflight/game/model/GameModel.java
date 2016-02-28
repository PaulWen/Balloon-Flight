package de.wenzel.paul.balloonflight.game.model;

import java.util.ArrayList;
import java.util.Iterator;

import de.wenzel.paul.balloonflight.game.GameActivity;
import de.wenzel.paul.balloonflight.game.model.DataObjects.Balloon;
import de.wenzel.paul.balloonflight.game.model.DataObjects.Platform;
import de.wenzel.paul.balloonflight.game.model.DataObjects.Player;

/**
 * Die Klasse {@link GameModel} ist das Model von {@link GameActivity}.
 */
public class GameModel {

    /** der Spieler */
    private Player player;
    /** Liste mit allen Plattformen **/
    private ArrayList<Platform> platformList;
    /** Liste mit allen Balloons **/
    private ArrayList<Balloon> balloonList;
    /** Anzahl der Punkte, welche der Spiele bisher gesammelt hat */
    private int points;

    // Farben
    private int backgroundColorResourceId;
    private int ballonColorResourceId;
    private int playerColorResourceId;
    private int platformColorResourceId;

    /**
     * Konstruktor der Klasse {@link GameModel}.
     *
     * @param backgroundColorResourceId die Resource ID der gewünschten Farbe für den Hintergrund
     * @param playerColorResourceId die Resource ID der gewünschten Farbe für den Spieler
     * @param ballonColorResourceId die Resource ID der gewünschten Farbe für die Ballons
     * @param platformColorResourceId die Resource ID der gewünschten Farbe für die Plattformen
     */
    public GameModel(int backgroundColorResourceId,int playerColorResourceId, int ballonColorResourceId, int platformColorResourceId) {
        this.backgroundColorResourceId = backgroundColorResourceId;
        this.ballonColorResourceId = ballonColorResourceId;
        this.platformColorResourceId = platformColorResourceId;
        this.playerColorResourceId = playerColorResourceId;

        player = new Player(20, 0, 25, 25, 200);
        platformList = new ArrayList<Platform>();
        balloonList = new ArrayList<Balloon>();
        points = 0;
    }

    ///////////Player///////////
    public Player getPlayer() {
        return player;
    }

    ///////////Plattformen///////////
    public Iterator<Platform> getPlatformList() {
        return platformList.iterator();
    }

    public void addPlatform (Platform platform) {
        platformList.add(platform);
    }

    public void addPlatforms(ArrayList<Platform> platformList) {
        this.platformList.addAll(platformList);
    }

    public void removePlatform(Platform platform) {
        platformList.remove(platform);
    }

    public float getXPositionOfLastPlatform() {
        return platformList.get(platformList.size() - 1).getX();
    }

    public int getPlatformListNumber() {
        return platformList.size();
    }

    ///////////Balloons///////////
    public Iterator<Balloon> getBalloonList() {
        return balloonList.iterator();
    }

    public void addBalloon (Balloon balloon) {
        balloonList.add(balloon);
    }

    public void removeBalloon(Balloon balloon) {
        balloonList.remove(balloon);
    }

    ///////////Punkte///////////
    public int getPoints() {
        return points;
    }

    public void addPoints (int pointToAdd) {
        points += pointToAdd;
    }

    ///////////Farben///////////
    public int getBackgroundColorResourceId() {
        return backgroundColorResourceId;
    }

    public int getBallonColorResourceId() {
        return ballonColorResourceId;
    }

    public int getPlayerColorResourceId() {
        return playerColorResourceId;
    }

    public int getPlatformColorResourceId() {
        return platformColorResourceId;
    }
}
