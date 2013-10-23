package com.geodroid.geodroidcx;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.geodroid.geodroidclass.GraficoView;
import com.geodroid.geodroidclass.HermesBluetooth;
import com.geodroid.geodroidclass.Trama;

public class Principal extends Activity implements  SensorEventListener {
	//Constante de depuración
    private static final String TAG = "Principal::Activity";
	private static String error;
	private int contador=0;
    //Acelerometro
    private long last_update = 0;								//guardar el timestamp de la última actualización. 
    private long last_movement = 0;								//guardar el timestamp de la última vez que se detectó movimiento.
    private float prevX = 0; 									//eje x
    private float prevY = 0; 									//eje y
    private float prevZ = 0;									//eje z
    private float curX = 0;										//valor de instancia eje x
    private float curY = 0;										//valor de instancia eje y
    private float curZ = 0;										//valor de instancia eje z	
    //conexion Bluetooth
	private BluetoothAdapter adaptador;							//Adaptador bluetooth del Android
	private static final int REQUEST_ENABLE_BT = 3;				//Constante para encender bluetooth;
	private ArrayList<BluetoothDevice> dispositivos;			//Arreglo con todos los dispositivos vinculados
	public static BluetoothDevice dispositivo;					//Dispositivo Bluetooth seleccionado
	private String[] nombresBluetooth;							//Nombres de dispositivos vinculados
	private HermesBluetooth hb;									//Clase encargada de la comunicacion
	//Botones
	//Camara
	private WebView camarita;
	private String urlCamara="http://192.168.0.22/"; 
	//Movimiento de Camara
	private WebView movCamara;
	private ImageButton ibRight;
	private ImageButton ibLeft;
	private ImageButton ibTop;
	private ImageButton ibDown;
	private ImageButton ibHome;
	//Datos GPS
private boolean tramAprobada;										//Verifica que la trama sea correcta
	private double latitud;											//Latitud capturada 
	private double longitud;										//Longitud capturados
	private LinearLayout llMapa;									//Lienzo donde se dibujaran los puntos.
	private GraficoView gr; 										//Clase encargada de dibujar el mapa.
	private TableLayout tlLatLon;									//Tabla con todas las capturas
	//Velocidad
	private SeekBar sbVelocidad;
	//Trama
	private Trama tTrama;
	//Estilo
	private static final int COMPLEX_UNIT_SP = 2;					//Unidad del tamaño de la letra SP
	
    public Principal() {
        Log.i(TAG, "Instanciado new " + this.getClass());			//depuración
    }

    /** 
     * onCreate
     * Se llama cuando se crea por primera vez la actividad. 
     * 
     */
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "llamada a onCreate");
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//pantalla acostada
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 	//garantiza la iluminacion de la pantalla
        setContentView(R.layout.principal);										//enlace con la con la interfaz
        initComponentes();														//inicializa los componentes
        listener();																//listener a la espera de su llamado
        //refrescarCamara();														//Refresca la imagen en el buffer
        
    }
    
    public void refrescarCamara() {
	    new Handler().postDelayed(new Runnable(){
	        
	        public void run() {
	                camarita.loadUrl(urlCamara+"cgi-bin/jpg/image");
	                refrescarCamara();
	        }
	    }, 100);
		
	}

	/**
     * initComponentes
     * Inicializa los componentes
     */
    public void initComponentes(){
    	
        camarita=(WebView) findViewById(R.id.camarita);
        camarita.loadUrl(urlCamara+"/cgi-bin/jpg/image");
        gr=new GraficoView(this);
        llMapa=(LinearLayout) findViewById(R.id.llMapa);
		llMapa.addView(gr);
        movCamara=(WebView) findViewById(R.id.wvMovCamara);
        ibRight=(ImageButton)findViewById(R.id.ibRight);
        ibLeft=(ImageButton)findViewById(R.id.ibLeft);
        ibDown=(ImageButton)findViewById(R.id.ibDown);
        ibTop=(ImageButton)findViewById(R.id.ibTop);
        ibHome=(ImageButton)findViewById(R.id.ibHome);
        tTrama=new Trama();
        sbVelocidad=(SeekBar) findViewById(R.id.sbVelocidad);
        tlLatLon =(TableLayout) findViewById(R.id.tlLatLon);
        
    }

    /**
     * listener
     * Los diferentes listener estaran a la espera de ser llamados.
     */
    public void listener(){
    	    	
    	ibDown.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("depuracion", "Pasando y mirando "+(++contador));
				movCamara.loadUrl(urlCamara+"cgi-bin/operator/ptzset?move=down");
				return false;
			}
		});
    	
    	ibRight.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				movCamara.loadUrl(urlCamara+"cgi-bin/operator/ptzset?move=right");
				return false;
			}
		});
    	
    	ibLeft.setOnTouchListener(new OnTouchListener() {
				
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				movCamara.loadUrl(urlCamara+"cgi-bin/operator/ptzset?move=left");
				return false;
			}
		});
    	
    	ibTop.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				movCamara.loadUrl(urlCamara+"cgi-bin/operator/ptzset?move=up");
				return false;
			}
		});
    	
    	ibHome.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				movCamara.loadUrl(urlCamara+"cgi-bin/operator/ptzset?move=home");
				return false;
			}
		});
    	
    	sbVelocidad.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int velocidad,
					boolean fromUser) {
				tTrama.setVelocidadMotorUno(velocidad);
				tTrama.setVelocidadMotorDos(velocidad);				
			}
		});
            	
    }
    
    /** 
     * onPause
     * Deshabilita la camara cuando se pausa la actividad. 
     * 
     */
    @Override
    public void onPause()
    {
        //Camara
    	Toast.makeText(getApplicationContext(), "Hay movimiento de pause" , Toast.LENGTH_SHORT).show();
        super.onPause();
    }
    
    /** 
     * onResume
     * Con el administrador de sensores vuelve disponible el acelerometro.
     * Activa de nuevo el callback loader. 
     * 
     */
    @Override
    protected void onResume() {
        super.onResume();
        //Acelerometro
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);        
        if (sensors.size() > 0) {
        	sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }        
    }
    
    /** 
     * onDestroy
     * Deshabilita la camara cuando se destruye la actividad. 
     * 
     */
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    protected void onStop() {
    	//Acelerometro
    	Toast.makeText(getApplicationContext(), "Hay movimiento de stop" , Toast.LENGTH_SHORT).show();
    	SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);    	
        sm.unregisterListener(this);
        super.onStop();
    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	/**
	 * onSensorChanged
	 * Se ejecutar si el sensor sufre algun cambio, se sobrecarga el metodo
	 * del implements SensorEventListener
	 * @param event
	 */
	public void onSensorChanged(SensorEvent event) {
		//Evita problemas de concurrencia al estar trabajando con los sensores.
        synchronized (this) {		
        	//Del evento recibido como parámetro vamos a obtener el timestamp de la fecha/hora actual
        	long current_time = event.timestamp;					 
        	//valores para los 3 ejes del acelerómetro (X, Y, Z).
        	curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];
            //revisamos si este código ya se ha ejecutado alguna vez
            if (prevX == 0 && prevY == 0 && prevZ == 0) {
                last_update = current_time;
                last_movement = current_time;
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
            }
            
            //Se obtiene la diferencia entre la última actualización y el timestamp actual, esto para calcular el movimiento.
            long time_difference = current_time - last_update;
            if (time_difference > 0) {
                float movement = Math.abs((curX + curY + curZ) - (prevX - prevY - prevZ)) / time_difference;
                float min_movement = 1E-6f;
                if (movement > min_movement) {
                    last_movement = current_time;
                }
                //actualiza los valores de X, Y y Z para la próxima vez que se registre cambio en los sensores
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
                last_update = current_time;
            }
            
            //Envia la direccion hacia donde iria el carrito           
            
            if(curX<=-1 && curY>=0 && curZ>=1){
//            	((TextView) findViewById(R.id.txtDireccion)).setText("Derecha-arriba");	
            }else if(curX>=1 && curY>=0 && curZ>=1){
//            	((TextView) findViewById(R.id.txtDireccion)).setText("Izquierda-arriba");
            }else if(curX<=-1 && curY>=0 && curZ<=-1){
//            	((TextView) findViewById(R.id.txtDireccion)).setText("Derecha-abajo");	
            }else if(curX>=1 && curY>=0 && curZ<=-1){
//            	((TextView) findViewById(R.id.txtDireccion)).setText("Izquierda-abajo");	
            }else if(curX<=1 && curX>=-1 && curY>=0 && curZ>=1){
//            	((TextView) findViewById(R.id.txtDireccion)).setText("Aldelante");
            }else if(curX<=1 && curX>=-1 && curY>=0 && curZ<=-1){
//            	((TextView) findViewById(R.id.txtDireccion)).setText("Atras");
            }else if(curX<=1 && curX>=-1 && curY>9){
//            	((TextView) findViewById(R.id.txtDireccion)).setText("Neutro");
            }           
        	           
        }
        
	}
    
	

	/**
     * Lanza la Activity claseLanzada
     * @param claseLanzada 
     */
    public void lanzarActivity(Class claseLanzada){
    	Intent i =new Intent(this, claseLanzada);
    	startActivity(i);
    }
    
    /**
     * Obtiene y parte la latitud y la longitud
     * @param receiveInfo
     */
    private void gps(String receiveInfo) {
    	tramAprobada=true;
    	try{
    		Log.e(TAG, "Referencias recibe"+receiveInfo);
    		String auxlat=receiveInfo.substring(4, 11);
    		String auxlon=receiveInfo.substring(12);
    		Log.e(TAG, "Referencias aux |"+auxlat+"|"+auxlon);
    		verificarTramaGPS(auxlat);
    		verificarTramaGPS(auxlon);
    		if(tramAprobada==true){
    			
				latitud=Double.parseDouble(auxlat);
				longitud=Double.parseDouble(auxlon);
				gr.setLatLon(latitud,longitud);
				dibujaTablaGPS();
    		}else{
    			Toast.makeText(Principal.this, "Trama Incompleta |"+auxlon+"|"+auxlat, Toast.LENGTH_SHORT).show();
    		}
    		
    	}catch(Exception ex){
    		error=ex.toString();
	    	Log.i(TAG, "Errores 445: "+error);
    	}	
		
	}
    
    private void dibujaTablaGPS() {
    	tlLatLon.removeAllViews();
    	ArrayList<Double> latitudArray=gr.getLatitud();
    	ArrayList<Double> longitudArray=gr.getLongitud();
    	int i=0;
    	for(Double lat:latitudArray){
    		TableRow tr= new TableRow(this);   		
    		tlLatLon.addView(tr);
    		
    		
    		TextView tvTCapt= new TextView(this);
    		tvTCapt.setText((i+1)+"");
    		tvTCapt.setWidth(70);
    		tvTCapt.setTextSize(COMPLEX_UNIT_SP, 20);
    		tr.addView(tvTCapt);
    		
    		TextView tvTLatitud= new TextView(this);
    		tvTLatitud.setText(lat+"");
    		tvTLatitud.setWidth(100);
    		tvTLatitud.setTextSize(COMPLEX_UNIT_SP, 20);
    		tr.addView(tvTLatitud); 
    		
    		TextView tvTLongitud=new TextView(this);
    		tvTLongitud.setText(longitudArray.get(i++)+"");
    		tvTLongitud.setWidth(100);
    		tvTLongitud.setTextSize(COMPLEX_UNIT_SP, 20);
    		tr.addView(tvTLongitud);
    	}
		
	}

	/**
     * verificarTramaGPS
     * Verifica que la trama gps que llega por el bluetooth sea 
     * correcta.
     * @param auxCordenada
     * Latitudo o Longitud a verificar
     */
    public void verificarTramaGPS(String auxCordenada) {
    	if(tramAprobada==true){
			for(int i=0; i<auxCordenada.length();i++){
    			if((int)auxCordenada.charAt(i)==0){
    				Log.i("pruebas", auxCordenada+"");
    				Toast.makeText(getApplicationContext(), "Vuelve a intentar"+auxCordenada, Toast.LENGTH_SHORT).show();
    				tramAprobada=false;
    				break;		
    			}
    		}
			
		}
		
	}

	/**
     * onCreateOptionMenu
     * Crea el menu en la parte superior. 
     * En el action bar
     * @param menu
     * @return true
     * 
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }
    
    /**
     * onOptionsItemSelected
     * Captura las opciones del menu.
     * @param item
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.i("ActionBar", "Intento de conexion Bluetooth");
                conexionBluetooth();
                return true;
            case R.id.capturaGPS:
            	try{
					Toast.makeText(getApplicationContext(), "Capturando GPS", Toast.LENGTH_SHORT).show();
					
					hb.sendInfo("A");
			    	gps(hb.receiveInfo());
				}catch(Exception ex){
					error=ex.toString();
			    	Log.i(TAG, "Errores ontouch: "+error);
				}
                return true;
            case R.id.reiniciarGPS:
            	gr.borrarTodoGPS();
            	dibujaTablaGPS();
            	return true;
            case R.id.deshacerGPS:
            	gr.deshacerCapturaGPS();
            	dibujaTablaGPS();
            	return true;
            case R.id.finalizarGPS:
            	gr.finalizarCapturaGPS();
            	dibujaTablaGPS();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    
    /**
     * conexionBluetooth
     * Inicia la conexion con Bluetooth.
     * Verifica que el dispositivo tenga un adaptador 
     * bluetooth y si esta encendido.
     */
	public void conexionBluetooth() {
		try{
			adaptador = BluetoothAdapter.getDefaultAdapter();
			if (adaptador != null){
				if (adaptador.isEnabled()){
					listaBluetooth();
					showDialog(1);
				}else{
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}
			}
		}catch(Exception ex){
	    	  error=ex.toString();
	    }
		
	}

	/**
	 * listaBluetooth
	 * Se crea una lista con los bluetooth vinculados
	 */
	public void listaBluetooth() {
		dispositivos = new ArrayList();
        for (BluetoothDevice dispositivo : adaptador.getBondedDevices()){
        	dispositivos.add(dispositivo);
        }
        nombresBluetooth=new String[dispositivos.size()];
    	if(dispositivos != null){
    		for(int indice = 0; indice < dispositivos.size(); indice++){
	          BluetoothDevice dispositivo = (BluetoothDevice) dispositivos.get(indice);
	          
	          if(dispositivo.getName() != null){
	        	  nombresBluetooth[indice]=dispositivo.getName();
	          }
    		}
    	}
	}
	
	/**
	 * onActivityResult
	 * Captura respuesta dada por el usuario en el momento
	 * de pedir encender el bluetooth.
	 * @param 
	 */
	public void onActivityResult (int requestCode, int respuesta, Intent data){
		System.out.println("onActivityResult");
		if(respuesta == RESULT_OK){
			listaBluetooth();
		}else{
	    	error = "No se activo el bluetooth";
    	}
    }
	
	
	/**
	 * onCreateDialog
	 * Crea un dialogo dependiendo de la opcion
	 * @param opcion
	 * 1 dialogo de seleccion
	 * 2 dialogo de alerta
	 * @return
	 * El dialogo creado
	 */
	protected Dialog onCreateDialog(int opcion) {
    	Dialog dialogo = null;
	    switch(opcion){
	    case 1:dialogo = crearDialogoSeleccion();break;
	    case 2:dialogo= crearDialogoAlerta();break;
    }
	    
	    return dialogo;
	}

	/**
	 * crearDialogoSeleccion
	 * Dialogo con la posibilidad de seleccionar una opcion
	 * @return
	 * El dialogo creado
	 */
	public Dialog crearDialogoSeleccion() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setIcon(R.drawable.blue);
	    builder.setTitle("Bluetooth Vinculados");
	    builder.setItems(nombresBluetooth, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int pos) {
	        	dispositivo=(BluetoothDevice) dispositivos.get(pos);
	        	Toast.makeText(getApplicationContext(), "Has seleccionado: "+ dispositivo.getName(), Toast.LENGTH_SHORT).show();
	        	hiloDeConexion();
	            Log.i("aparec", "Has seleccionado: "+ dispositivo.getName());
	        }
	    });
	 
	    return builder.create();
	}
	/**
	 * hiloDeConexion
	 * Inicia un hilo de conexion
	 * 
	 */
	public void hiloDeConexion(){
		hb=new HermesBluetooth(this);
        Thread t=new Thread(hb);
        t.start();
	}
	
	/**
	 * Crea dialogo de alerta.
	 * @return
	 * El dialogo creado.
	 */
	private Dialog crearDialogoAlerta(){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Error en Ejecucion");
	    builder.setMessage(error);
	    builder.setPositiveButton("OK", new OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	        }
	    });
	 
	    return builder.create();
	}

	public BluetoothDevice getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(BluetoothDevice dispositivo) {
		this.dispositivo = dispositivo;
	}
    
    
}