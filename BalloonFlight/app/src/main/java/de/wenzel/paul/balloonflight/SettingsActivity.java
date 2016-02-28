package de.wenzel.paul.balloonflight;


import android.app.Activity;
import android.os.Bundle;

/**
 * Die Klasse {@link SettingsActivity} ist das Fenster, über welches man die Einstellungen des Spiels ändern kann.
 * (z.B. Lautstärke von Musik und Soundeffekten)
 */
public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
