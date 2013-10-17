package com.geodroid.geodroidclass;

public class Trama {
	private int sentidoMotorUno;
	private int velocidadMotorUno;
	private int sentidoMotorDos;
	private int velocidadMotorDos;
	private int GPS;
	
	public Trama(){
		sentidoMotorUno=0;
		velocidadMotorUno=0;
		sentidoMotorDos=0;
		velocidadMotorDos=0;
	}
	public int getSentidoMotorUno() {
		return sentidoMotorUno;
	}
	public void setSentidoMotorUno(int sentidoMotorUno) {
		this.sentidoMotorUno = sentidoMotorUno;
	}
	public int getVelocidadMotorUno() {
		return velocidadMotorUno;
	}
	public void setVelocidadMotorUno(int velocidadMotorUno) {
		this.velocidadMotorUno = velocidadMotorUno;
	}
	public int getSentidoMotorDos() {
		return sentidoMotorDos;
	}
	public void setSentidoMotorDos(int sentidoMotorDos) {
		this.sentidoMotorDos = sentidoMotorDos;
	}
	public int getVelocidadMotorDos() {
		return velocidadMotorDos;
	}
	public void setVelocidadMotorDos(int velocidadMotorDos) {
		this.velocidadMotorDos = velocidadMotorDos;
	}
	public int getGPS() {
		return GPS;
	}
	public void setGPS(int gPS) {
		GPS = gPS;
	}
	
	

}
