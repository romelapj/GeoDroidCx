/**
 * GraficoView
 * Encargada de dibujar los puntos 
 * capturados con el gps
 */
package com.geodroid.geodroidclass;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

public class GraficoView extends View {
	//Coordenadas 
	private ArrayList<Double> latitud=new ArrayList<Double>();	//Arreglo con todas las latitudes del predio
	private ArrayList<Double> longitud=new ArrayList<Double>();	//Arreglo con todas las longitudes del predio
	private int lat=10;											//Latitud en valores cartesianos
	private int lon=10;											//Longitud en valores cartesianos
	//Banderas
	private boolean inicioDibujo=false;							//Dibujo por defecto hasta llegar la primera coordenada
	private boolean primerTrazo;								//Primer trazo sin rayar
	private boolean dibujoCoordenada=true;						//Dibujar primero coordenada  luego el trazo
	private boolean finDibujo=false;							//Cerrar el area
	//Depuracion
	private String TAG="gv";
	//Dimensiones del lienzo
	private double anchoMax=-180;								//Latitud de mayor valor
	private double altoMax=-180;								//Longitud de mayor valor 
	private double anchoMin=180;								//Latitud de menor valor
	private double altoMin=180;									//Longitud de menor valor
	private double canvasWMax;									//Ancho del lienzo a dibujar con un padding de 200
	private double canvasHMax;									//Alto del lienzo a dibujar con un padding  de 200
	private double ancho;										//Ancho del lienzo
	private double alto;										//Alto del lienzo

	public GraficoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Recibe las coordenadas a graficar.
	 * @param lat Latitud
	 * @param lon Longitud
	 */
	public void setLatLon(double lat, double lon) {
		Log.e(TAG, "ESTAS asignando latitud y longitud!");
		latitud.add(lat);
		longitud.add(lon); 
		inicioDibujo=true;
		invalidate(); //vuelve a dibujar el view
		
	}
	
	/**
	 * obtenerXY
	 * Transforma las coordenada en valores para
	 * un plano cartesiano
	 * @param x
	 * @param y
	 */
	public void obtenerXY(double x, double y){
		
		double auxlat=x-anchoMin;
		double auxlon=y-altoMin;
		
		lat=(int) (Math.round(auxlat*canvasWMax/ancho)+100);
		lon=(int) (Math.round(auxlon*canvasHMax/alto)+100);
		
		
	}
	/**
	 * getBounds
	 * Obtiene los limites del lienzo
	 */
	public void getBounds(){
		for(int i=0;i<latitud.size();i++){
			if (latitud.get(i)>anchoMax){
			   anchoMax = latitud.get(i);
			}
			if(latitud.get(i)<anchoMin){
			   anchoMin = latitud.get(i);
			}
			if (longitud.get(i)>altoMax){
			   altoMax = longitud.get(i);
			} 
			if(longitud.get(i)<altoMin){
			   altoMin = longitud.get(i);
			}
		}
		ancho=Math.abs(anchoMax-anchoMin);
		alto=Math.abs(altoMax-altoMin);
	}
	
	/**
	 * borrarTodoGPS
	 * Limpia el lienzo y borra las variables
	 */
	public void borrarTodoGPS(){
		if(latitud.isEmpty()){
			Log.e(TAG, "No tienes capturas para deshacer");
		}else{
			latitud.clear();
			longitud.clear();
			inicioDibujo=false;
			invalidate();
		}
		
	}
	
	/**
	 * finalizarCapturaGPS
	 * Une puntos muestra grafica total, 
	 * y realiza operaciones.
	 */
	public void finalizarCapturaGPS(){
		if(latitud.size()>2){
			latitud.add(latitud.get(0));
			longitud.add(longitud.get(0));
			invalidate();
		}else{
			Log.e(TAG,"Tiene pocos datos para finalizar la captura");
		}
	}
	
	/**
	 * deshacerCapturaGPS
	 * Deshace la ultima captura GPS
	 */
	public void deshacerCapturaGPS(){
		latitud.remove(latitud.size()-1);
		longitud.remove(longitud.size()-1);
		invalidate();
	}
	
	/**
	 * onDraw
	 * Empieza a dibujar el lienzo
	 * @param
	 */
	protected void onDraw(Canvas canvas){
		primerTrazo=true;
		canvasHMax=canvas.getHeight()-200;													//Genera padding en la trama
		canvasWMax=canvas.getWidth()-200;													//Genera padding en la trama
		Log.e(TAG, "ESTAS "+canvas.getHeight()+"Y"+canvas.getWidth());
		Paint pincel=new Paint();
		pincel.setColor(Color.BLUE);
		pincel.setStrokeWidth(8);
		pincel.setStyle(Style.FILL);
		if(inicioDibujo){
			if(dibujoCoordenada){
				canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, 10, pincel);
				dibujoCoordenada=false;
			}else{
				Path trazo=new Path();
				Paint pin=new Paint();
				pin.setColor(Color.BLUE);
				pin.setStrokeWidth(8);
				pin.setStyle(Style.STROKE);			
				getBounds();
				
				for(int i=0;i<latitud.size();i++){
					obtenerXY(latitud.get(i),longitud.get(i));
					canvas.drawCircle(lat, lon, 10, pincel);
					if(primerTrazo){
						trazo.moveTo(lat, lon);
						primerTrazo=false;
					}else{
						trazo.lineTo(lat, lon);
					}					
				}
				canvas.drawPath(trazo, pin);
			
			}
		}else{
			canvas.drawCircle(50, 50, 100, pincel);
		}
		
	}

}
