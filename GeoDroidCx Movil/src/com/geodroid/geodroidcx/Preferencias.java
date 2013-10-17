/**
 * Preferencias
 * Esta Activity nos permite modificar las configuraciones principales de la
 * aplicacion.
 * @author Romel Palomino Jaramillo
 * @author Andrea Rojas Jimenez
 * @version 1.0
 */
package com.geodroid.geodroidcx;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class Preferencias extends PreferenceActivity {
	//Constante de depuracion
	private static final String TAG = "Preferencias::Activity";
		
	/**
	 * onCreate
	 * Se crea la actividad y se conecta con la interfaz xml preferencias.
	 * En la cual crearemos todos los posibles cambios para la aplicación
	 * @param Bundle
	 */
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState){
		Log.i(TAG, "llamada a onCreate");										//depuracion
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//pantalla acostada
		addPreferencesFromResource(R.xml.preferencias);							//enlace con la interfaz(caso especial xml)
	}

}
