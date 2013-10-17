/**
 * Main
 * Esta Activity principal nos mostrara las distintas opciones que 
 * tiene el programa (comenzar, configurar, acerca de y salir).
 * @author Romel Palomino Jaramillo
 * @author Andrea Rojas Jimenez
 * @version 1.0
 */
package com.geodroid.geodroidcx;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	//Botones del Menu Principal
	private Button btnAcercaDe;			
	private Button btnConfiguracion;
	private Button btnComenzar;
	private Button btnSalir;
	//Constante de depuración
	private static final String TAG = "Main::Activity";

	/**
	 * onCreate
	 * Se llama cuando se crea por primera vez la actividad.
	 * se conecta con la interfaz y son inicializados los 
	 * componentes de la misma.
	 * @param Bundle
	 */
    protected void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "llamada a onCreate");										//depuracion
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//pantalla acostada
        setContentView(R.layout.main);											//enlace con la interfaz
        initComponentes();														//inicializa los componentes
        listener();																//listener a la espera de su llamado
    }
    
    /**
     * initComponentes
     * Inicializa los componentes
     */
    public void initComponentes(){
    	btnAcercaDe=(Button) findViewById(R.id.btnAcercaDe);
    	btnConfiguracion=(Button) findViewById(R.id.btnConfiguracion);
    	btnComenzar=(Button) findViewById(R.id.btnComenzar);
    	btnSalir=(Button) findViewById(R.id.btnSalir);
    }
    /**
     * listener
     * Los diferentes listener estaran a la espera de ser llamados.
     */
    public void listener(){
    	/**
    	 * Esta preparado a escuchar cuando el usuario interactue con el 
    	 * btnAcercaDe
    	 */
    	btnAcercaDe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lanzarActivity(AcercaDe.class);
			}
		});
    	
    	/**
    	 * Esta preparado a escuchar cuando el usuario interactue con el 
    	 * btnConfiguracion
    	 */
    	btnConfiguracion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lanzarActivity(Preferencias.class);
			}
		});
    	
    	/**
    	 * Esta preparado a escuchar cuando el usuario interactue con el 
    	 * btnComenzar
    	 */
    	btnComenzar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lanzarActivity(Principal.class);
			}
		});
    	/**
    	 * Esta preparado a escuchar cuando el usuario interactue con el 
    	 * btnSalir
    	 */
    	btnSalir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lanzarActivity(Salir.class);
			}
		});
    }
    
    /**
     * Lanza la Activity claseLanzada
     * @param claseLanzada 
     */
    @SuppressWarnings("rawtypes")
	public void lanzarActivity(Class claseLanzada){
    	Intent i =new Intent(this, claseLanzada);
    	startActivity(i);
    }
    

    
}
