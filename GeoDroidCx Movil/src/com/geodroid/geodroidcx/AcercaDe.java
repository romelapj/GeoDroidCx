/**
 * AcercaDe
 * Esta Activity nos indica el motivo por el cual fue desarrollado el proyecto.
 * mediante los themas se logra que sea visualice como un dialogo.
 * @author Romel Palomino Jaramillo
 * @author Andrea Rojas Jimenez
 * @version 1.0
 */
package com.geodroid.geodroidcx;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public class AcercaDe extends Activity {
	//Constante de depuracion
	private static final String TAG = "AcercaDe::Activity";

	/**
	 * onCreate
	 * Se crea la actividad y se conecta con la interfaz.
	 * @param Bundle
	 */
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "llamada a onCreate");		//depuracion
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acercade);		//enlace con la interfaz
	}

	

}
